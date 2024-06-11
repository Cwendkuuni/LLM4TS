package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph.AbstractCfgNodeTraversalCallback;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  private static final String JS_CODE = "function foo() {" +
      "  var a = 1;" +
      "  var b = 2;" +
      "  var c = 3;" +
      "  var d = 4;" +
      "  var e = 5;" +
      "  var x = 6;" +
      "  var y = 7;" +
      "  if (true) {" +
      "    var a = 10;" +
      "  } else {" +
      "    var a = 20;" +
      "  }" +
      "  return a * b * c;" +
      "}";

  private Scope jsScope;
  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private MustBeReachingVariableDef analysis;

  @Before
  public void setUp() {
    jsScope = new Scope();
    compiler = new AbstractCompiler();
    cfg = new ControlFlowGraph<>(jsScope, compiler);
    analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testBasicFunctionality() {
    analysis.process(Node.createTree(JS_CODE));
    Node returnNode = cfg.getReturnNode();

    // Test getting the definition of a local variable.
    assertSame(cfg.getEntryNode(), analysis.getDef("a", returnNode));

    // Test getting the definition of an undefined local variable.
    assertNull(analysis.getDef("z", returnNode));

    // Test getting the definition of a variable that depends on outer scope.
    assertNull(analysis.getDef("x", returnNode));
  }

  @Test
  public void testConditionalBlocks() {
    analysis.process(Node.createTree(JS_CODE));

    // Test getting the definition of a variable in an if-true block.
    assertSame(cfg.getNode(2), analysis.getDef("a", cfg.getNode(12)));

    // Test getting the definition of a variable in an if-false block.
    assertSame(cfg.getNode(5), analysis.getDef("a", cfg.getNode(11)));
  }

  @Test
  public void testForLoops() {
    analysis.process(Node.createTree(JS_CODE + " for (var i = 0; i < 10; i++) {}"));

    // Test getting the definition of a variable in the initialization part of a for loop.
    assertSame(cfg.getNode(1), analysis.getDef("i", cfg.getNode(2)));

    // Test getting the definition of a variable in the condition part of a for loop.
    assertSame(cfg.getNode(1), analysis.getDef("i", cfg.getNode(3)));

    // Test getting the definition of a variable in the update part of a for loop.
    assertSame(cfg.getNode(1), analysis.getDef("i", cfg.getNode(4)));
  }

  @Test
  public void testDefineAndUseInSameBlock() {
    analysis.process(Node.createTree(JS_CODE + " var a = 10; return a;"));

    // Test getting the definition of a variable in the same block as the use.
    assertSame(cfg.getNode(1), analysis.getDef("a", cfg.getNode(1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullUseNode() {
    analysis.getDef("a", null);
  }

  @Test(expected = IllegalStateException.class)
  public void testUseNodeOutsideOfControlFlowGraph() {
    analysis.process(Node.createTree(JS_CODE));
    analysis.getDef("a", Node.createTree(new Token(Token.END, 0)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullName() {
    analysis.getDef(null, cfg.getNode(0));
  }

  @Test
  public void testMultipleDefinitionsForVariable() {
    analysis.process(Node.createTree(JS_CODE + " var a = 1; var a = 2;"));

    Set<Node> definitions = new HashSet<>();
    definitions.add(cfg.getNode(1));
    definitions.add(cfg.getNode(3));

    Set<Node> uses = new HashSet<>();
    uses.add(cfg.getNode(8));

    for (Node definition : definitions) {
      for (Node use : uses) {
        Node def = analysis.getDef("a", use);
        assertSame(definition, def);
      }
    }
  }

  @Test
  public void testEscapedVariables() {
    analysis.process(Node.createTree(JS_CODE + " (function() {\n" +
        "  var x = 1;\n" +
        "  function bar() {\n" +
        "    var x = 2;\n" +
        "  }\n" +
        "  bar();\n" +
        "})();"));

    Node barFunction = cfg.getNode(1);
    Node barFunctionEntry = cfg.getNode(2);

    // Test getting the definition of a variable in the outer scope.
    assertSame(cfg.getNode(0), analysis.getDef("x", barFunctionEntry));

    // Test getting the definition of a variable in the inner function.
    assertSame(cfg.getNode(4), analysis.getDef("x", barFunction));
  }
}