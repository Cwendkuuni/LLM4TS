import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckSideEffectsTest {

 private @Mock AbstractCompiler compiler;
 private CheckSideEffects checkSideEffects;
 private NodeFactory nodeFactory;
 private Node rootNode;
 private CompilerInput compilerInput;
 private CompilerPassManager passManager;

 @Before
 public void setUp() {
 nodeFactory = NodeFactory.getInitial();
 rootNode = nodeFactory.parseTree("{}");
 checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
 compilerInput = new CompilerInput("test", rootNode);
 passManager = new CompilerPassManager(compilerInput, compiler);
 }

 @Test
 public void testVisitSimpleExpression() {
 Node expressionNode = nodeFactory.literal(123);
 Node parentNode = nodeFactory.exprStatement(expressionNode);
 rootNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), parentNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitVoidStatement() {
 Node voidNode = nodeFactory.voidStatement();
 Node parentNode = nodeFactory.exprStatement(voidNode);
 rootNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), parentNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitCallStatementWithSideEffect() {
 Node callNode = nodeFactory.callExpression("alert", Lists.newArrayList(nodeFactory.literal("Hello World")));
 Node parentNode = nodeFactory.exprStatement(callNode);
 rootNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), parentNode, rootNode);
 // A warning should be issued
 assertTrue(!checkSideEffects.problemNodes.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testVisitCallStatementWithEmptyArgumentList() {
 Node callNode = nodeFactory.callExpression("alert", Lists.newArrayList());
 Node parentNode = nodeFactory.exprStatement(callNode);
 rootNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), parentNode, rootNode);
 }

 @Test
 public void testVisitForStatement() {
 Node forNode = nodeFactory.forStatement(
 nodeFactory.identifier("i"),
 nodeFactory.numberLiteral(1),
 nodeFactory.numberLiteral(5),
 nodeFactory.block(nodeFactory.statement(nodeFactory.returnStatement(nodeFactory.literal(123)))));
 rootNode.addChildToBack(forNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), forNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitBlockStatementWithAssignment() {
 Node assignmentNode = nodeFactory.assignment(nodeFactory.identifier("x"), nodeFactory.literal(123));
 Node blockNode = nodeFactory.block(assignmentNode);
 rootNode.addChildToBack(blockNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), blockNode, rootNode);
 // A warning should be issued
 assertTrue(!checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitBlockStatementWithExpressionResult() {
 Node expressionResultNode = nodeFactory.exprStatement(nodeFactory.returnStatement(nodeFactory.literal(123)));
 Node blockNode = nodeFactory.block(expressionResultNode);
 rootNode.addChildToBack(blockNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), blockNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitBlockStatementWithCallExpression() {
 Node callNode = nodeFactory.callExpression("print", Lists.newArrayList(nodeFactory.literal("Hello World")));
 Node blockNode = nodeFactory.block(callNode);
 rootNode.addChildToBack(blockNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), blockNode, rootNode);
 // A warning should be issued
 assertTrue(!checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitVariableDeclarationStatement() {
 Node variableDeclarationNode = nodeFactory.variableDeclarationStatement(nodeFactory.identifier("x"), nodeFactory.literal(123));
 Node parentNode = nodeFactory.stmtList(variableDeclarationNode);
 rootNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), parentNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitForInStatement() {
 Node forInNode = nodeFactory.forInStatement(
 nodeFactory.variableDeclarationList(nodeFactory.variableDeclarationStatement(nodeFactory.identifier("i"), null), null),
 nodeFactory.callExpression("Object.keys", Lists.newArrayList(nodeFactory.memberExpression(nodeFactory.identifier("obj"), null))),
 nodeFactory.statement(nodeFactory.assignment(nodeFactory.identifier("x"), nodeFactory.identifier("i"))));
 rootNode.addChildToBack(forInNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), forInNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitIfStatement() {
 Node ifNode = nodeFactory.ifStatement(nodeFactory.expression(nodeFactory.booleanLiteral(true)), nodeFactory.block(nodeFactory.returnStatement(nodeFactory.literal(123))), null);
 rootNode.addChildToBack(ifNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), ifNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitTryCatchStatement() {
 Node tryNode = nodeFactory.tryStatement(
 nodeFactory.block(nodeFactory.expressionStatement(nodeFactory.callExpression("alert", Lists.newArrayList(nodeFactory.literal("Hello World"))))),
 nodeFactory.block(nodeFactory.variableDeclarationStatement(nodeFactory.variableDeclarationList(nodeFactory.variableDeclarationStatement(nodeFactory.identifier("e"), null), null), null),
 nodeFactory.returnStatement(nodeFactory.literal(123))));
 rootNode.addChildToBack(tryNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), tryNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitIdentifier() {
 Node identifierNode = nodeFactory.identifier("foo");
 Node parentNode = nodeFactory.exprStatement(identifierNode);
 rootNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), parentNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitQualifiedName() {
 Node qualifiedNameNode = nodeFactory.qualifiedName(nodeFactory.name("Math"), nodeFactory.name("sqrt"));
 Node parentNode = nodeFactory.exprStatement(qualifiedNameNode);
 rootNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), parentNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testVisitStatementWithJSDocInfo() {
 Node jsdocInfoStatement = nodeFactory.jsDocInfoStatement(nodeFactory.createJSDocInfo("@return {foo: 123}"));
 Node parentNode = nodeFactory.stmtList(jsdocInfoStatement);
 rootNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), parentNode, rootNode);
 }

 @Test
 public void testVisitCallExpressionWithJSDocInfo() {
 Node jsdocInfoCall = nodeFactory.callExpression("print", Lists.newArrayList(nodeFactory.literal("Hello World"), nodeFactory.createJSDocInfo("@param {string} s")));
 Node parentNode = nodeFactory.stmtList(jsdocInfoCall);
 rootNode.addChildToBack(parentNode);
 checkSideEffects.visit(NodeTraversal.create(passManager, rootNode, null), parentNode, rootNode);
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testProcess() {
 passManager.runPasses();
 checkSideEffects.protectSideEffects();
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testProcessWithProtectedSideEffectFreeCode() {
 Node sideEffectFreeCode = nodeFactory.expressionStatement(nodeFactory.assignment(nodeFactory.identifier("x"), nodeFactory.literal(123)));
 rootNode.addChildToBack(sideEffectFreeCode);
 checkSideEffects.process(compilerInput, rootNode);
 checkSideEffects.protectSideEffects();
 // No warning should be issued
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testProtectSideEffects() {
 Node sideEffectFreeCode = nodeFactory.expressionStatement(nodeFactory.assignment(nodeFactory.identifier("x"), nodeFactory.literal(123)));
 rootNode.addChildToBack(sideEffectFreeCode);
 checkSideEffects.process(compilerInput, rootNode);
 checkSideEffects.protectSideEffects();
 Node protectedSideEffectFreeCode = checkSideEffects.problemNodes.get(0);
 assertEquals(Token.CALL, protectedSideEffectFreeCode.getType());
 }

 @Test
 public void testProcessWithProtectedSideEffectFreeCodeAndCallStatementWithSideEffect() {
 Node sideEffectFreeCode = nodeFactory.expressionStatement(nodeFactory.assignment(nodeFactory.identifier("x"), nodeFactory.literal(123)));
 Node callStatementWithSideEffect = nodeFactory.callExpression("alert", Lists.newArrayList(nodeFactory.literal("Hello World")));
 rootNode.addChildToBack(sideEffectFreeCode);
 rootNode.addChildToBack(callStatementWithSideEffect);
 checkSideEffects.process(compilerInput, rootNode);
 checkSideEffects.protectSideEffects();
 Node protectedSideEffectFreeCode = checkSideEffects.problemNodes.get(0);
 assertEquals(Token.CALL, protectedSideEffectFreeCode.getType());
 Node callStatementWithProtectedSideEffect = checkSideEffects.problemNodes.get(1);
 assertEquals(Token.CALL, callStatementWithProtectedSideEffect.getType());
 }

 @Test
 public void testProtectSideEffectsWithEmptyCodeChangeList() {
 checkSideEffects.protectSideEffects();
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testProtectSideEffectsWithNonEmptyCodeChangeList() {
 checkSideEffects.problemNodes.add(nodeFactory.literal(123));
 checkSideEffects.protectSideEffects();
 assertEquals(1, checkSideEffects.problemNodes.size());
 }

 @Test
 public void testStripProtection() {
 passManager.addPass(new CheckSideEffects(compiler, WARNING, false));
 passManager.addPass(new CheckSideEffects.StripProtection(compiler));
 rootNode.addChildToBack(nodeFactory.callExpression("JSCOMPILER_PRESERVE", Lists.newArrayList(nodeFactory.literal("x"))));
 passManager.runPasses();
 Node strippedNode = checkSideEffects.problemNodes.get(0);
 assertEquals(Token.CALL, strippedNode.getType());
 assertEquals("JSCOMPILER_PRESERVE", strippedNode.getFirstChild().getString());
 }
}