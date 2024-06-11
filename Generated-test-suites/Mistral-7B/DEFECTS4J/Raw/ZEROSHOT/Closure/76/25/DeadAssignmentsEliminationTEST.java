package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.test.TestNodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class DeadAssignmentsEliminationTest {

  @org.junit.Test
  public void testDeadAssignmentsElimination() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // do nothing
      }
    };

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    compiler.setOptions(options);

    Parser parser = new Parser(null);
    Node root = parser.parse("function foo() { a = b; b = 1; a = 2; }", null);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    TestNodeTraversal t = new TestNodeTraversal(compiler);
    t.traverse(root);

    Node expected = NodeFactory.createBlock(
        Lists.newArrayList(
            new Node(Token.ASSIGN, Node.newName("a"), Node.newNumber(1)),
            new Node(Token.RETURN, Node.newName("a"))),
        null, null);

    Node actual = t.getCurrentNode();
    assertTrue("Unexpected result: " + NodePrinter.printNode(actual),
        actual.equals(expected));
  }
}