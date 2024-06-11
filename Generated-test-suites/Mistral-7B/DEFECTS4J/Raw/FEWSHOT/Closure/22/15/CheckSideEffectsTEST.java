package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.CheckSideEffects.PROTECTOR_FN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.util.List;
import java.util.stream.Stream;

public class CheckSideEffectsTest {
  private static final DiagnosticType USELESS_CODE_ERROR = DiagnosticType.warning("JSC_USELESS_CODE", "Suspicious code. {0}");

  private AbstractCompiler compiler;
  private Checker checker;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new Checker(compiler, new Diagnostics(), new NodeTraversal(compiler, new AbstractPostOrderCallback[0]));
    nodeFactory = new NodeFactory();
  }

  @org.junit.Test
  public void testVisitVoidNode() {
    Node node = nodeFactory.createVoidNode();
    Node parent = nodeFactory.createExpressionStatement(node);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);

    checkSideEffects.visit(new NodeTraversal(compiler, null), node, parent);

    assertNull(checkSideEffects.problemNodes);
  }

  @org.junit.Test
  public void testVisitForStatementWithoutInitializer() {
    Node forStatement = nodeFactory.createForStatement(null, null, null, null, null, null);
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);

    checkSideEffects.visit(new NodeTraversal(compiler, null), forStatement, parent);

    assertNull(checkSideEffects.problemNodes);
  }

  @org.junit.Test
  public void testVisitStatementWithSideEffect() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createName("foo").call(null, nodeFactory.createNumber(42))));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);

    checkSideEffects.visit(new NodeTraversal(compiler, null), statement, parent);

    assertNull(checkSideEffects.problemNodes);
  }

  @org.junit.Test
  public void testVisitStatementWithSimpleOperator() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createName("x"), nodeFactory.createName("y")));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);

    checkSideEffects.visit(new NodeTraversal(compiler, null), statement, parent);

    assertNull(checkSideEffects.problemNodes);
  }

  @org.junit.Test
  public void testVisitStatementWithSideEffectAndJSDocInfo() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createName("foo").call(null, nodeFactory.createString("hello"))));
    statement.setJSDocInfo(new JSDocInfoBuilder().build(statement));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);

    checkSideEffects.visit(new NodeTraversal(compiler, null), statement, parent);

    assertNull(checkSideEffects.problemNodes);
  }

  @org.junit.Test
  public void testVisitStatementWithSideEffectAndExpressionResult() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createName("foo").call(null, nodeFactory.createNumber(42))));
    Node expressionResult = nodeFactory.createExpressionStatement(statement);
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);

    checkSideEffects.visit(new NodeTraversal(compiler, null), expressionResult, parent);

    assertNull(checkSideEffects.problemNodes);
  }

  @org.junit.Test
  public void testVisitStatementWithSideEffectButNotRemoved() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createName("foo").call(null, nodeFactory.createNumber(42))));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);

    checkSideEffects.visit(new NodeTraversal(compiler, null), statement, parent);

    assertNull(checkSideEffects.problemNodes);
  }

  @org.junit.Test
  public void testVisitStatementWithNoSideEffectButNotProtected() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createName("x"), nodeFactory.createName("y")));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);

    checkSideEffects.visit(new NodeTraversal(compiler, null), statement, parent);

    assertNull(checkSideEffects.problemNodes);
  }

  @org.junit.Test
  public void testVisitStatementWithNoSideEffectButProtected() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createName("x"), nodeFactory.createName("y")));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);

    when(compiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput());

    checkSideEffects.visit(new NodeTraversal(compiler, null), statement, parent);

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    checkSideEffects.protectSideEffects();

    Node externFunction = checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(compiler).getFirstChildRecursive();
    List<Node> protectorFnChildren = Lists.newArrayList(externFunction.getChildren());

    assertEquals(1, protectorFnChildren.size());
    assertEquals(externFunction.getType(), Token.FUNCTION);
    assertEquals(1, externFunction.getChildCount());
    assertEquals(externFunction.getChild(0).getType(), Token.VAR);

    CheckSideEffects newCheckSideEffects = new CheckSideEffects(compiler, WARNING, true);
    newCheckSideEffects.process(compiler.getSynthesizedExternsInput().getAstRoot(compiler), null);
    newCheckSideEffects.stripProtections();

    assertEquals(0, newCheckSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitStatementWithNoSideEffectButProtectedAndNotRemoved() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createName("x"), nodeFactory.createName("y")));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);

    when(compiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput());

    checkSideEffects.visit(new NodeTraversal(compiler, null), statement, parent);

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    checkSideEffects.protectSideEffects();

    Node externFunction = checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(compiler).getFirstChildRecursive();
    List<Node> protectorFnChildren = Lists.newArrayList(externFunction.getChildren());

    assertEquals(1, protectorFnChildren.size());
    assertEquals(externFunction.getType(), Token.FUNCTION);
    assertEquals(1, externFunction.getChildCount());
    assertEquals(externFunction.getChild(0).getType(), Token.VAR);

    checkSideEffects.problemNodes.clear();
    checkSideEffects.stripProtections();

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitStatementWithNoSideEffectButNotProtectedAndRemoved() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createName("x"), nodeFactory.createName("y")));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);

    checkSideEffects.process(compiler.getSynthesizedExternsInput().getAstRoot(compiler), null);
    checkSideEffects.stripProtections();

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitStatementWithNoSideEffectButProtectedAndRemoved() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createName("x"), nodeFactory.createName("y")));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);

    when(compiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput());

    checkSideEffects.visit(new NodeTraversal(compiler, null), statement, parent);

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    checkSideEffects.protectSideEffects();

    Node externFunction = checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(compiler).getFirstChildRecursive();
    List<Node> protectorFnChildren = Lists.newArrayList(externFunction.getChildren());

    assertEquals(1, protectorFnChildren.size());
    assertEquals(externFunction.getType(), Token.FUNCTION);
    assertEquals(1, externFunction.getChildCount());
    assertEquals(externFunction.getChild(0).getType(), Token.VAR);

    checkSideEffects.problemNodes.clear();
    checkSideEffects.stripProtections();

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitStatementWithNoSideEffectButProtectedAndNotRemovedButJSDocInfo() {
    Node statement = nodeFactory.createExpressionStatement(
        nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createName("x"), nodeFactory.createName("y")));
    statement.setJSDocInfo(new JSDocInfoBuilder().build(statement));
    Node parent = nodeFactory.createBlock(null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);

    when(compiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput());

    checkSideEffects.visit(new NodeTraversal(compiler, null), statement, parent);

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    checkSideEffects.protectSideEffects();

    Node externFunction = checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(compiler).getFirstChildRecursive();
    List<Node> protectorFnChildren = Lists.newArrayList(externFunction.getChildren());

    assertEquals(1, protectorFnChildren.size());
    assertEquals(externFunction.getType(), Token.FUNCTION);
    assertEquals(1, externFunction.getChildCount());
    assertEquals(externFunction.getChild(0).getType(), Token.VAR);

    checkSideEffects.problemNodes.clear();
    checkSideEffects.stripProtections();

    assertEquals(0, checkSideEffects.problemNodes.size());
  }
}