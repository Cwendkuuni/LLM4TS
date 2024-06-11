package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class UnreachableCodeEliminationTest {
  @Test
  public void testUnreachableCodeElimination() {
    

    // Test case 1: Function with dead code after return statement
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createCompilationUnit(
        "function foo() {" +
            "if (true) {" +
            "  return;" +
            "  alert('unreachable');" +
            "}" +
            "}");
    CompilerPass pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);
    assertTrue(NodeUtil.isEmpty(root.getLastChild()));

    // Test case 2: Function with dead statement with no side effects
    root = NodeFactory.createCompilationUnit(
        "function foo() {" +
            "return a.b.MyClass.prototype.propertyName;" +
            "}" +
            "function MyClass() {}");
    pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);
    assertTrue(NodeUtil.isEmpty(root.getLastChild().getLastChild()));

    // Test case 3: Function with dead statement with side effects
    root = NodeFactory.createCompilationUnit(
        "function foo() {" +
            "return a.b.MyClass.prototype.propertyName;" +
            "alert('Dead code removed');" +
            "}" +
            "function MyClass() {}");
    pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);
    assertFalse(NodeUtil.isEmpty(root.getLastChild()));

    // Test case 4: Function with unconditional branching to the same node
    root = NodeFactory.createCompilationUnit(
        "function foo() {" +
            "if (true) {" +
            "  break foo;" +
            "}" +
            "}" +
            "foo: {}");
    pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);
    assertTrue(NodeUtil.isEmpty(root.getLastChild()));

    // Test case 5: Function with unconditional branching from a loop
    root = NodeFactory.createCompilationUnit(
        "function foo() {" +
            "for (var i = 0; i < 10; i++) {" +
            "  break foo;" +
            "}" +
            "foo: {}" +
            "}" +
            "foo: {}");
    pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);
    assertTrue(NodeUtil.isEmpty(root.getLastChild()));

    
  }
}