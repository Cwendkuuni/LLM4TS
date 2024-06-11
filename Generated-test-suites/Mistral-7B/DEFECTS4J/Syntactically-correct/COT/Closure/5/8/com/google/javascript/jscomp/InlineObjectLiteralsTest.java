package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerOptions options;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");
  }

  // Basic Test Case - Test if the pass correctly identifies and inlines an object literal.
  @Test
  public void testBasicObjectLiteral() {
    String input = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "}" +
        "foo();";

    Node externs = Node.rootNode();
    Node root = compiler.parseTree(input, externs, options);

    inlineObjectLiterals.process(externs, root);

    List<Node> expectedNodes = Lists.newArrayList(
        IR.assign(IR.name("a"), IR.intLit(1)),
        IR.assign(IR.name("b"), IR.intLit(2)),
        IR.assign(IR.name("c"), IR.intLit(3))
    );

    Node actualNode = root.getLastChild();
    List<Node> actualList = NodeUtil.asList(actualNode);

    assertEquals(expectedNodes, actualList);
  }

  // Edge Case Test - Test if the pass correctly handles an object literal with a single property.
  @Test
  public void testSinglePropertyObjectLiteral() {
    String input = "function foo() {" +
        "  var obj = {" +
        "    a: 1" +
        "  };" +
        "}" +
        "foo();";

    Node externs = Node.rootNode();
    Node root = compiler.parseTree(input, externs, options);

    inlineObjectLiterals.process(externs, root);

    List<Node> expectedNodes = Lists.newArrayList(
        IR.assign(IR.name("a"), IR.intLit(1))
    );

    Node actualNode = root.getLastChild();
    List<Node> actualList = NodeUtil.asList(actualNode);

    assertEquals(expectedNodes, actualList);
  }

  // Edge Case Test - Test if the pass correctly handles an object literal with no properties.
  @Test
  public void testEmptyObjectLiteral() {
    String input = "function foo() {" +
        "  var obj = {};" +
        "}" +
        "foo();";

    Node externs = Node.rootNode();
    Node root = compiler.parseTree(input, externs, options);

    inlineObjectLiterals.process(externs, root);

    List<Node> expectedNodes = Lists.newArrayList();

    Node actualNode = root.getLastChild();
    List<Node> actualList = NodeUtil.asList(actualNode);

    assertEquals(expectedNodes, actualList);
  }

  // Edge Case Test - Test if the pass correctly handles an object literal with a cycle.
  @Test(expected = IllegalStateException.class)
  public void testCyclicObjectLiteral() {
    String input = "function foo() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: obj2" +
        "  };" +
        "  var obj2 = {" +
        "    a: obj1" +
        "  };" +
        "}" +
        "foo();";

    Node externs = Node.rootNode();
    Node root = compiler.parseTree(input, externs, options);

    inlineObjectLiterals.process(externs, root);
  }
}