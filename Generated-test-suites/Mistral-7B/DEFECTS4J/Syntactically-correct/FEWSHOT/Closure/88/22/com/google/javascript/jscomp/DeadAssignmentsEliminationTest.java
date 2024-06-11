package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testEmpty() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    elimination.process(null, null);
  }

  @Test
  public void testNoAssignments() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(Token.empty(), null);
    elimination.process(Node.newObjectLiteral(null), root);
  }

  @Test
  public void testNoDeadAssignments() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(
        Token.RETURN,
        Node.newName("x"),
        Node.newName("y")
    );
    elimination.process(Node.newObjectLiteral(null), root);
    elimination.compiler.printTree(System.out);
    assertTrue(root.toString().contains("return x;"));
  }

  @Test
  public void testDeadAssignment() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(
        Token.RETURN,
        Node.newAssignment(
            Node.newName("x"),
            Node.newName("y")
        ),
        Node.newName("y")
    );
    elimination.process(Node.newObjectLiteral(null), root);
    elimination.compiler.printTree(System.out);
    assertTrue(root.toString().contains("return y;"));
  }

  @Test
  public void testDeadAssignmentWithMultipleAssignments() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(
        Token.RETURN,
        Node.newAssignment(
            Node.newName("x"),
            Node.newAssignment(
                Node.newName("y"),
                Node.newNumber(1)
            )
        ),
        Node.newName("z")
    );
    elimination.process(Node.newObjectLiteral(null), root);
    elimination.compiler.printTree(System.out);
    assertTrue(root.toString().contains("return y;"));
  }

  @Test
  public void testDeadAssignmentWithCondition() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(
        Token.IF,
        Node.newCondition(
            Node.newName("x"),
            Node.newComparison(Node.newName("x"), Token.LEQ, Node.newNumber(0)),
            Node.newBlock(
                Node.newAssignment(
                    Node.newName("y"),
                    Node.newName("z")
                ),
                Node.newReturn(Node.newName("y"))
            )
        ),
        Node.newName("z")
    );
    elimination.process(Node.newObjectLiteral(null), root);
    elimination.compiler.printTree(System.out);
    assertTrue(root.toString().contains("return y;"));
  }

  @Test
  public void testDeadAssignmentWithMultipleConditions() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(
        Token.IF,
        Node.newCondition(
            Node.newName("x"),
            Node.newComparison(Node.newName("x"), Token.LEQ, Node.newNumber(0)),
            Node.newBlock(
                Node.newAssignment(
                    Node.newName("y"),
                    Node.newName("z")
                ),
                Node.newReturn(Node.newName("y"))
            )
        ),
        Node.newCondition(
            Node.newName("x"),
            Node.newComparison(Node.newName("x"), Token.GTR, Node.newNumber(0)),
            Node.newBlock(
                Node.newAssignment(
                    Node.newName("w"),
                    Node.newName("v")
                ),
                Node.newReturn(Node.newName("w"))
            )
        ),
        Node.newName("z")
    );
    elimination.process(Node.newObjectLiteral(null), root);
    elimination.compiler.printTree(System.out);
    assertFalse(root.toString().contains("z"));
    assertTrue(root.toString().contains("return y;"));
  }

  @Test
  public void testDeadAssignmentWithIncrementDecrement() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(
        Token.RETURN,
        Node.newAssignment(
            Node.newName("x"),
            Node.newIncrement(Node.newName("x"))
        ),
        Node.newName("x")
    );
    elimination.process(Node.newObjectLiteral(null), root);
    elimination.compiler.printTree(System.out);
    assertTrue(root.toString().contains("return x + 1;"));
  }

  @Test
  public void testDeadAssignmentWithFunction() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(
        Token.RETURN,
        Node.newName("f"),
        Node.newFunction(
            Node.newName("f"),
            null,
            Node.newBlock(
                Node.newAssignment(
                    Node.newName("x"),
                    Node.newName("y")
                ),
                Node.newReturn(Node.newName("x"))
            )
        )
    );
    elimination.process(Node.newObjectLiteral(null), root);
    elimination.compiler.printTree(System.out);
    assertTrue(root.toString().contains("return y;"));
  }

  @Test
  public void testDeadAssignmentWithFunctionWithFunction() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(
        Token.RETURN,
        Node.newName("f"),
        Node.newFunction(
            Node.newName("f"),
            null,
            Node.newBlock(
                Node.newAssignment(
                    Node.newName("x"),
                    Node.newName("g")
                ),
                Node.newReturn(Node.newName("x"))
            )
        ),
        Node.newFunction(
            Node.newName("g"),
            null,
            Node.newBlock(
                Node.newAssignment(
                    Node.newName("y"),
                    Node.newNumber(1)
                ),
                Node.newReturn(Node.newName("y"))
            )
        )
    );
    elimination.process(Node.newObjectLiteral(null), root);
    elimination.compiler.printTree(System.out);
    assertTrue(root.toString().contains("return g;"));
  }

  @Test
  public void testDeadAssignmentWithFunctionWithFunctionAndVariable() {
    AbstractCompiler compiler = new Compiler();
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    Node root = Node.newProgram(
        Token.RETURN,
        Node.newName("f"),
        Node.newFunction(
            Node.newName("f"),
            null,
            Node.newBlock(
                Node.newAssignment(
                    Node.newName("x"),
                    Node.newName("g")
                ),
                Node.newReturn(Node.newName("x"))
            )
        ),
        Node.newFunction(
            Node.newName("g"),
            Node.newList(Node.newName("x")),
            Node.newBlock(
                Node.newVariable("x", Node.newNumber(1)),
                Node.newAssignment(
                    Node.newName("y"),
                    Node.newName("x")
                ),
                Node.newReturn(Node.newName("y"))
            )
        )
    );
    elimination.process(Node.newObjectLiteral(null), root);
    elimination.compiler.printTree(System.out);
    assertFalse(root.toString().contains("x"));
    assertTrue(root.toString().contains("return g;"));
  }
}