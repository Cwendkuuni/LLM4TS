package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Nodeops;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test cases for {@link CheckSideEffects} class.
 */
public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @org.junit.Test
  public void testProcess_EmptyScript() {
    Node root = Nodeops.createBlock(null);
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_EmptyStatement() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.VOID, null));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithSideEffect() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"),
        Nodeops.createCall(Nodeops.createName("foo"), null)));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild(), checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProcess_StatementWithSimpleOperator() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"),
        Nodeops.createBinaryOp(Token.MINUS, Nodeops.createNumber(5), Nodeops.createName("x"))));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithMultipleAssignments() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"),
        Nodeops.createBinaryOp(Token.PLUS, Nodeops.createName("x"), Nodeops.createNumber(1))),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"),
            Nodeops.createBinaryOp(Token.PLUS, Nodeops.createName("y"), Nodeops.createNumber(2))));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithIfElse() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.IF, Nodeops.createExpression(
            Nodeops.createBinaryOp(Token.LOGICAL_AND, Nodeops.createName("x"), Nodeops.createNumber(0))),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ELSE, Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"),
            Nodeops.createNumber(2)))));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithFor() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.FOR, Nodeops.createName("i"),
        Nodeops.createName("start"), Nodeops.createName("end"), Nodeops.createStatement(Token.ASSIGN,
            Nodeops.createName("x"), Nodeops.createCall(Nodeops.createName("myFunction"), null))));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithFor_EmptyStatementInside() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.FOR, Nodeops.createName("i"),
        Nodeops.createName("start"), Nodeops.createName("end"), Nodeops.createStatement(Token.VOID, null)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"), Nodeops.createNumber(1)));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild().getLastChild(), checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProcess_StatementWithCall() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"),
        Nodeops.createCall(Nodeops.createName("myFunction"), Nodeops.createArray(Nodeops.createNumber(1),
            Nodeops.createNumber(2)))));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild(), checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProcess_StatementWithStringConcatenation() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"),
        Nodeops.createBinaryOp(Token.PLUS, Nodeops.createString("foo"), Nodeops.createString("bar"))));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithSimpleStatement() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"), Nodeops.createNumber(1))));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithDeclaration() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.VAR, Nodeops.createName("x"), null),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"), Nodeops.createNumber(1)));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithDeclarationAndAssignment() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.VAR, Nodeops.createName("x"), null),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"), Nodeops.createNumber(1)));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithDeclarationAndCall() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.VAR, Nodeops.createName("x"), null),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"),
            Nodeops.createCall(Nodeops.createName("myFunction"), Nodeops.createArray(Nodeops.createNumber(1),
                Nodeops.createNumber(2)))));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild(), checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProcess_StatementWithDeclarationAndAssignmentAndCall() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.VAR, Nodeops.createName("x"), null),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"),
            Nodeops.createCall(Nodeops.createName("myFunction"), Nodeops.createArray(Nodeops.createNumber(1),
                Nodeops.createNumber(2)))));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild(), checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProcess_StatementWithDeclarationAndMultipleAssignments() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.VAR, Nodeops.createName("x"), null),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("z"), Nodeops.createNumber(1)));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcess_StatementWithDeclarationAndAssignmentAndCallAndAssignment() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.VAR, Nodeops.createName("x"), null),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"),
            Nodeops.createCall(Nodeops.createName("myFunction"), Nodeops.createArray(Nodeops.createNumber(1),
                Nodeops.createNumber(2)))),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("z"), Nodeops.createNumber(1)));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild(), checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProcess_StatementWithDeclarationAndMultipleAssignmentsAndCall() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.VAR, Nodeops.createName("x"), null),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("z"),
            Nodeops.createCall(Nodeops.createName("myFunction"), Nodeops.createArray(Nodeops.createNumber(1),
                Nodeops.createNumber(2)))));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild(), checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProcess_StatementWithDeclarationAndMultipleAssignmentsAndMultipleCalls() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.VAR, Nodeops.createName("x"), null),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("y"), Nodeops.createNumber(1)),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("z"),
            Nodeops.createCall(Nodeops.createName("myFunction1"), Nodeops.createArray(Nodeops.createNumber(1),
                Nodeops.createNumber(2)))),
        Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("w"),
            Nodeops.createCall(Nodeops.createName("myFunction2"), Nodeops.createArray(Nodeops.createNumber(1),
                Nodeops.createNumber(2)))));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild(), checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"),
        Nodeops.createBinaryOp(Token.PLUS, Nodeops.createName("a"), Nodeops.createName("b"))));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    checkSideEffects.protectSideEffects();
    assertEquals(0, checkSideEffects.problemNodes.size());
    Node xProtector = checkSideEffects.problemNodes.get(0);
    Node x = xProtector.getFirstChild();
    Node replacement = xProtector.getLastChild();
    assertEquals(Nodeops.createCall(Nodeops.createName("JSCOMPILER_PRESERVE"), null), replacement);
    assertEquals(Nodeops.createName("x"), x);
    assertEquals(Nodeops.createCall(Nodeops.createName("JSCOMPILER_PRESERVE"), Nodeops.createName("x")),
        replacement.getFirstChild());
  }

  @org.junit.Test
  public void testStripProtection() {
    Node root = Nodeops.createBlock(Nodeops.createStatement(Token.ASSIGN, Nodeops.createName("x"),
        Nodeops.createCall(Nodeops.createName("JSCOMPILER_PRESERVE"), Nodeops.createName("a"))));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    Node xProtector = checkSideEffects.problemNodes.get(0);
    Node stripper = new StripProtection(compiler);
    stripper.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
    assertEquals(Nodeops.createName("a"), xProtector.getFirstChild().getFirstChild());
  }
}