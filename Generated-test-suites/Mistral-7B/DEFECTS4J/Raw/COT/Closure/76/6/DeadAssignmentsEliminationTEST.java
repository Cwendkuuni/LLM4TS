package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    

    // Test case for a simple assignment that is dead
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(new TestCompiler());
    Node root = Node.createJsDocComment("test");
    Node n1 = Node.newName("x").setLine(1);
    Node a1 = Node.newAssign(Token.ASSIGN, n1, Node.newNumber(1));
    Node n2 = Node.newName("y").setLine(2);
    Node a2 = Node.newAssign(Token.ASSIGN, n2, Node.newNumber(2));
    root.appendChild(n1);
    root.appendChild(a1);
    root.appendChild(n2);
    root.appendChild(a2);
    root.appendChild(Node.newReturn(Node.newName("x")));

    Scope scope = new Scope(root);
    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    assertTrue(a1.isDead());
    assertFalse(a2.isDead());

    // Test case for a dead assignment within a loop
    root = Node.createJsDocComment("test2");
    Node loopStart = Node.newForInit(Token.ASSIGN, Node.newName("i"), Node.newNumber(0));
    Node loopCondition = Node.newBinaryOp(Token.LESS, Node.newName("i"), Node.newNumber(5));
    Node loopUpdate = Node.newAssign(Token.ASSIGN, Node.newName("i"), Node.newIncrement(Node.newName("i")));
    Node body = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newName("i"));
    root.appendChild(loopStart);
    root.appendChild(loopCondition);
    root.appendChild(loopUpdate);
    root.appendChild(body);
    root.appendChild(Node.newReturn(Node.newName("x")));

    scope = new Scope(root);
    nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    assertTrue(body.isDead());

    // Test case for a dead assignment within a conditional statement
    root = Node.createJsDocComment("test3");
    Node condition = Node.newBinaryOp(Token.LESS, Node.newName("x"), Node.newNumber(5));
    Node a = Node.newAssign(Token.ASSIGN, Node.newName("y"), Node.newNumber(1));
    Node branchTrue = Node.newAssign(Token.ASSIGN, Node.newName("z"), Node.newNumber(2));
    Node branchFalse = Node.newAssign(Token.ASSIGN, Node.newName("z"), Node.newNumber(3));
    root.appendChild(condition);
    root.appendChild(a);
    root.appendChild(branchTrue);
    root.appendChild(Node.newIf(Node.newLabel(), branchFalse, Node.newLabel()));
    root.appendChild(Node.newReturn(Node.newName("z")));

    scope = new Scope(root);
    nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    assertTrue(a.isDead());

    // Test case for a dead assignment within a function call
    root = Node.createJsDocComment("test4");
    Node functionCall = Node.newCall(
        Node.newName("myFunction"), Node.newArray(Node.newNumber(1), Node.newNumber(2)));
    Node a3 = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newNumber(3));
    root.appendChild(functionCall);
    root.appendChild(a3);
    root.appendChild(Node.newReturn(Node.newName("x")));

    scope = new Scope(root);
    nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    assertTrue(a3.isDead());

    // Test case for a dead assignment within a try statement
    root = Node.createJsDocComment("test5");
    Node tryBlock = Node.newTry();
    Node tryBody = Node.newAssign(Token.ASSIGN, Node.newName("x"), Node.newNumber(4));
    Node catchBlock = Node.newCatch(
        Node.newLiteral(Token.THROW, "ExceptionMessage"), Node.newEmptyStatement());
    root.appendChild(tryBlock);
    root.appendChild(tryBody);
    root.appendChild(catchBlock);
    root.appendChild(Node.newReturn(Node.newName("x")));

    scope = new Scope(root);
    nodeTraversal = new NodeTraversal(root, null);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse(deadAssignmentsElimination, root);

    assertTrue(tryBody.isDead());

    
  }

  private static class TestCompiler extends CompilerOptions {
    @Override
    public Compiler pass(CompilerPass pass) {
      return pass.process(this);
    }
  }
}