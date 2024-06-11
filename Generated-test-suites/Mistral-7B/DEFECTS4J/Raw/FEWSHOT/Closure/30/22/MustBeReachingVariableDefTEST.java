package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.util.NodeTraversal;
import com.google.javascript.rhino.Node;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  @Test
  public void testMustDef() {
    Scope scope = new Scope("test");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("test");

    // Define x
    Node xDef = Node.newName("x", root);
    Node xInit = Node.newAssignment(xDef, Node.newNumberLiteral(10));
    root.addChild(xDef);
    root.addChild(xInit);

    // Use x
    Node xUse = Node.newName("x");
    root.addChild(xUse);

    // Define y
    Node yDef = Node.newName("y", root);
    Node yInit = Node.newAssignment(yDef, Node.newNumberLiteral(20));
    root.addChild(yDef);
    root.addChild(yInit);

    // Use y
    Node yUse = Node.newName("y");
    root.addChild(yUse);

    // Define z = x
    Node zDef = Node.newName("z", root);
    Node zDefValue = Node.newName("x");
    Node zInit = Node.newAssignment(zDef, zDefValue);
    root.addChild(zDef);
    root.addChild(zInit);

    // Use z
    Node zUse = Node.newName("z");
    root.addChild(zUse);

    // Define w
    Node wDef = Node.newName("w", root);
    Node wInit = Node.newAssignment(wDef, Node.newName("x"));
    root.addChild(wDef);
    root.addChild(wInit);

    // Use w
    Node wUse = Node.newName("w");
    root.addChild(wUse);

    // Define v = y
    Node vDef = Node.newName("v", root);
    Node vDefValue = Node.newName("y");
    Node vInit = Node.newAssignment(vDef, vDefValue);
    root.addChild(vDef);
    root.addChild(vInit);

    // Use v
    Node vUse = Node.newName("v");
    root.addChild(vUse);

    // Define u = x
    Node uDef = Node.newName("u", root);
    Node uDefValue = Node.newName("x");
    Node uInit = Node.newAssignment(uDef, uDefValue);
    root.addChild(uDef);
    root.addChild(uInit);

    // Use u
    Node uUse = Node.newName("u");
    root.addChild(uUse);

    analyzer.analyze(root);

    // Test getting definition of x
    Node xDefNode = analyzer.getDef("x", xUse);
    assertEquals(xDef, xDefNode);

    // Test getting definition of y
    Node yDefNode = analyzer.getDef("y", yUse);
    assertEquals(yDef, yDefNode);

    // Test getting definition of z
    Node zDefNode = analyzer.getDef("z", zUse);
    assertEquals(xDef, zDefNode);

    // Test getting definition of w
    Node wDefNode = analyzer.getDef("w", wUse);
    assertEquals(xDef, wDefNode);

    // Test getting definition of v
    Node vDefNode = analyzer.getDef("v", vUse);
    assertEquals(yDef, vDefNode);
  }

  @Test
  public void testMustDef_dependsOnOuterScopeVars() {
    Scope scope = new Scope("test");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("test");

    // Define x in outer scope
    Node xDef = Node.newName("x", Node.newBlock("outer"));
    Node xInit = Node.newAssignment(xDef, Node.newNumberLiteral(10));
    Node outer = Node.newBlock("outer");
    outer.addChild(xDef);
    outer.addChild(xInit);
    root.addChild(outer);

    // Use x
    Node xUse = Node.newName("x");
    root.addChild(xUse);

    analyzer.analyze(root);

    // Test that x depends on outer scope variable
    boolean dependsOnOuterScope =
        analyzer.dependsOnOuterScopeVars("x", xUse);
    assertTrue(dependsOnOuterScope);
  }

  @Test
  public void testMustDef_nullUseNode() {
    Scope scope = new Scope("test");
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);

    Node root = Node.newBlock("test");

    Node xDef = Node.newName("x", root);
    Node xInit = Node.newAssignment(xDef, Node.newNumberLiteral(10));
    root.addChild(xDef);
    root.addChild(xInit);

    Node xUse = null;
    root.addChild(xUse);

    analyzer.analyze(root);

    // Test that getDef returns null for null use node
    assertNull(analyzer.getDef("x", xUse));
  }
}