package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Nodes;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private NodeTraversal traverser;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    traverser = new NodeTraversal(compiler, null);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node globalContext = Nodes.script();
    assertThat(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.any(Node.class), globalContext)).isTrue();
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    Node nonGlobalContext = Nodes.function("evil");
    Node parentOfNonGlobalContext = Nodes.block();
    parentOfNonGlobalContext.addChildToStatementList(nonGlobalContext);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfNonGlobalContext);

    assertThat(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.any(Node.class), globalContext)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutJSDocInfo() {
    Node functionWithoutJsDocInfo = Nodes.function("evil");
    Node parentOfFunctionWithoutJsDocInfo = Nodes.block();
    parentOfFunctionWithoutJsDocInfo.addChildToStatementList(functionWithoutJsDocInfo);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfFunctionWithoutJsDocInfo);

    assertThat(checker.shouldTraverse(traverser, functionWithoutJsDocInfo, globalContext)).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    JSDocInfo constructorAnnotation = new JSDocInfo("constructor", ImmutableList.of());
    Node constructorFunction = Nodes.function("Constructor", constructorAnnotation);
    Node parentOfConstructorFunction = Nodes.block();
    parentOfConstructorFunction.addChildToStatementList(constructorFunction);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfConstructorFunction);

    assertThat(checker.shouldTraverse(traverser, constructorFunction, globalContext)).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    JSDocInfo thisAnnotation = new JSDocInfo("this", ImmutableList.of());
    Node thisFunction = Nodes.function("thisFunction", thisAnnotation);
    Node parentOfThisFunction = Nodes.block();
    parentOfThisFunction.addChildToStatementList(thisFunction);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfThisFunction);

    assertThat(checker.shouldTraverse(traverser, thisFunction, globalContext)).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionWithNoLogicalPlaceForThisAnnotation = Nodes.function("noLogicalPlaceForThisAnnotation");
    Node parentOfFunctionWithNoLogicalPlaceForThisAnnotation = Nodes.block();
    parentOfFunctionWithNoLogicalPlaceForThisAnnotation.addChildToStatementList(functionWithNoLogicalPlaceForThisAnnotation);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfFunctionWithNoLogicalPlaceForThisAnnotation);

    assertThat(checker.shouldTraverse(traverser, functionWithNoLogicalPlaceForThisAnnotation, globalContext)).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAssignment() {
    Node prototypePropertyAssignment = Nodes.assign(Nodes.name("prototype"), Nodes.thisKeyword());

    Node functionWithPrototypePropertyAssignment = Nodes.function("functionWithPrototypePropertyAssignment");
    functionWithPrototypePropertyAssignment.addChildToStatementList(prototypePropertyAssignment);

    Node parentOfFunctionWithPrototypePropertyAssignment = Nodes.block();
    parentOfFunctionWithPrototypePropertyAssignment.addChildToStatementList(functionWithPrototypePropertyAssignment);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfFunctionWithPrototypePropertyAssignment);

    assertThat(checker.shouldTraverse(traverser, functionWithPrototypePropertyAssignment, globalContext)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyGet() {
    Node prototypePropertyGet = Nodes.getProp(Nodes.name("prototype"));

    Node functionWithPrototypePropertyGet = Nodes.function("functionWithPrototypePropertyGet");
    functionWithPrototypePropertyGet.addChildToStatementList(prototypePropertyGet);

    Node parentOfFunctionWithPrototypePropertyGet = Nodes.block();
    parentOfFunctionWithPrototypePropertyGet.addChildToStatementList(functionWithPrototypePropertyGet);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfFunctionWithPrototypePropertyGet);

    assertThat(checker.shouldTraverse(traverser, functionWithPrototypePropertyGet, globalContext)).isFalse();
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignment = Nodes.assign(Nodes.name("this"), Nodes.thisKeyword());

    Node parentOfAssignment = Nodes.block();
    parentOfAssignment.addChildToStatementList(assignment);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfAssignment);

    checker.traverse(traverser, globalContext, null);

    Node thisKeywordOnLeftSideOfAssignment = traverser.getCurrentNode();
    assertThat(thisKeywordOnLeftSideOfAssignment).isNotNull();
    assertThat(thisKeywordOnLeftSideOfAssignment.getType()).isEqualTo(Token.THIS);

    compiler.assertNoErrors();
    assertThat(compiler.getDiagnostics()).isEmpty();
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfPropertyAccess() {
    Node propertyAccess = Nodes.getProp(Nodes.name("this"), Nodes.name("property"));

    Node parentOfPropertyAccess = Nodes.block();
    parentOfPropertyAccess.addChildToStatementList(propertyAccess);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfPropertyAccess);

    checker.traverse(traverser, globalContext, null);

    Node thisKeywordOnLeftSideOfPropertyAccess = traverser.getCurrentNode();
    assertThat(thisKeywordOnLeftSideOfPropertyAccess).isNotNull();
    assertThat(thisKeywordOnLeftSideOfPropertyAccess.getType()).isEqualTo(Token.THIS);

    compiler.assertNoErrors();
    assertThat(compiler.getDiagnostics()).isEmpty();
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node assignment = Nodes.assign(Nodes.name("this"), Nodes.literal("foo"));

    Node parentOfAssignment = Nodes.block();
    parentOfAssignment.addChildToStatementList(assignment);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfAssignment);

    checker.traverse(traverser, globalContext, null);

    Node thisKeywordOnRightSideOfAssignment = traverser.getCurrentNode();
    assertThat(thisKeywordOnRightSideOfAssignment).isNull();

    compiler.assertNoErrors();
    assertThat(compiler.getDiagnostics()).isEmpty();
  }

  @Test
  public void testShouldReportThisKeywordOnRightSideOfPropertyAccess() {
    Node propertyAccess = Nodes.getProp(Nodes.name("this"), Nodes.name("property"));

    Node rhsOfAssignment = Nodes.thisKeyword();
    Node assignment = Nodes.assign(Nodes.name("this"), propertyAccess);
    assignment.setRhs(rhsOfAssignment);

    Node parentOfAssignment = Nodes.block();
    parentOfAssignment.addChildToStatementList(assignment);

    Node globalContext = Nodes.script();
    globalContext.addChildToStatementList(parentOfAssignment);

    checker.traverse(traverser, globalContext, null);

    Node thisKeywordOnRightSideOfPropertyAccess = traverser.getCurrentNode();
    assertThat(thisKeywordOnRightSideOfPropertyAccess).isNull();

    compiler.assertNoErrors();
    assertThat(compiler.getDiagnostics()).isEmpty();
  }
}