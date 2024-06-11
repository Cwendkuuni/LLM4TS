package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  @Test
  public void testMustDef() {
    String jsSource = "function foo() {" +
        "  var a = 1; " +
        "  var b = 2; " +
        "  var c = a; " +
        "  return b; " +
        "}";

    Scope jsScope = new Scope(null, null, null, jsSource);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = cfg.getEntryNode();
    Node returnNode = cfg.getNode("return");

    Map<Node, Node> expectedDefs = Maps.newHashMap();
    expectedDefs.put(rootNode, null);
    expectedDefs.put(returnNode, cfg.getNode("b"));

    for (Entry<Node, Node> entry : analyzer.getDataFlow().getResult().entrySet()) {
      assertEquals(expectedDefs.get(entry.getKey()), entry.getValue().getIn().reachingDef.get(jsScope.getVar("a")));
      assertEquals(expectedDefs.get(entry.getKey()), entry.getValue().getIn().reachingDef.get(jsScope.getVar("b")));
      assertEquals(expectedDefs.get(entry.getKey()), entry.getValue().getIn().reachingDef.get(jsScope.getVar("c")));
    }
  }

  @Test
  public void testMustDefWithBranching() {
    String jsSource = "function foo() {" +
        "  var a = 1; " +
        "  if (1) {" +
        "    var b = 2; " +
        "    return b; " +
        "  } else {" +
        "    var c = 3; " +
        "    return c; " +
        "  }" +
        "}";

    Scope jsScope = new Scope(null, null, null, jsSource);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = cfg.getEntryNode();
    Node ifTrueNode = cfg.getNode("if (1)");
    Node ifFalseNode = cfg.getNode("else");
    Node returnNodeA = cfg.getNode("return 2");
    Node returnNodeB = cfg.getNode("return 3");

    Map<Node, Node> expectedDefs = Maps.newHashMap();
    expectedDefs.put(rootNode, null);
    expectedDefs.put(ifTrueNode, cfg.getNode("b"));
    expectedDefs.put(ifFalseNode, cfg.getNode("c"));
    expectedDefs.put(returnNodeA, cfg.getNode("b"));
    expectedDefs.put(returnNodeB, cfg.getNode("c"));

    for (Entry<Node, Node> entry : analyzer.getDataFlow().getResult().entrySet()) {
      assertEquals(expectedDefs.get(entry.getKey()), entry.getValue().getIn().reachingDef.get(jsScope.getVar("a")));
      assertEquals(expectedDefs.get(entry.getKey()), entry.getValue().getIn().reachingDef.get(jsScope.getVar("b")));
      assertEquals(expectedDefs.get(entry.getKey()), entry.getValue().getIn().reachingDef.get(jsScope.getVar("c")));
    }
  }

  @Test
  public void testMustDefWithCycle() {
    String jsSource = "function foo() {" +
        "  var a = 1; " +
        "  while (true) {" +
        "    var b = a; " +
        "    a = b; " +
        "  }" +
        "}";

    Scope jsScope = new Scope(null, null, null, jsSource);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = cfg.getEntryNode();
    Node whileNode = cfg.getNode("while (true)");
    Node assignmentNodeA = cfg.getNode("var b = a");
    Node assignmentNodeB = cfg.getNode("a = b");

    Map<Node, Node> expectedDefs = Maps.newHashMap();
    expectedDefs.put(rootNode, null);
    expectedDefs.put(whileNode, null);
    expectedDefs.put(assignmentNodeA, null);
    expectedDefs.put(assignmentNodeB, null);

    for (Entry<Node, Node> entry : analyzer.getDataFlow().getResult().entrySet()) {
      assertEquals(expectedDefs.get(entry.getKey()), entry.getValue().getIn().reachingDef.get(jsScope.getVar("a")));
      assertEquals(expectedDefs.get(entry.getKey()), entry.getValue().getIn().reachingDef.get(jsScope.getVar("b")));
    }
  }

  @Test
  public void testMustDefWithEscape() {
    String jsSource = "function foo(x) {" +
        "  var y = x; " +
        "  return y; " +
        "}";

    Scope jsScope = new Scope(null, null, null, jsSource);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope);
    AbstractCompiler compiler = new AbstractCompiler(cfg, jsScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node rootNode = cfg.getEntryNode();
    Node argumentNode = cfg.getNode("x");
    Node assignmentNode = cfg.getNode("var y = x");
    Node returnNode = cfg.getNode("return y");

    Map<Node, Node> expectedDefs = Maps.newHashMap();
    expectedDefs.put(rootNode, null);
    expectedDefs.put(argumentNode, null);
    expectedDefs.put(assignmentNode, null);
    expectedDefs.put(returnNode, null);

    analyzer.escaped.add(jsScope.getVar("x"));

    for (Entry<Node, Node> entry : analyzer.getDataFlow().getResult().entrySet()) {
      assertEquals(expectedDefs.get(entry.getKey()), entry.getValue().getIn().reachingDef.get(jsScope.getVar("a")));
      assertNull(entry.getValue().getIn().reachingDef.get(jsScope.getVar("y")));
    }
  }
}