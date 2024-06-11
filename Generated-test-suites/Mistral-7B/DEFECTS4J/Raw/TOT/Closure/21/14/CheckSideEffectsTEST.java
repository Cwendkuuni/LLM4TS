package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

 private static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";

 @Test
 public void testCheckSideEffects() {
 CheckLevel level = CheckLevel.WARNING;
 AbstractCompiler compiler = new Compiler();
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);

 // Test empty statement
 Node root = IR.block();
 Node emptyStatement = IR.statement();
 root.addChildToBack(emptyStatement);
 checkSideEffects.process(null, root);
 assertEquals(0, checkSideEffects.problemNodes.size());

 // Test simple statement
 Node simpleStatement = IR.statement(IR.name("x").assign(IR.intLiteral(1)));
 root.addChildToBack(simpleStatement);
 checkSideEffects.process(null, root);
 assertEquals(1, checkSideEffects.problemNodes.size());
 assertNotNull(checkSideEffects.problemNodes.get(0).getJSDocInfo());

 // Test expression statement
 Node exprStatement = IR.expressionStatement(IR.name("x").assign(IR.intLiteral(1)));
 root.addChildToBack(exprStatement);
 checkSideEffects.process(null, root);
 assertEquals(1, checkSideEffects.problemNodes.size());
 assertNotNull(checkSideEffects.problemNodes.get(0).getJSDocInfo());

 // Test side-effect free expression
 Node sideEffectFreeExpression = IR.expression(IR.name("x").assign(IR.intLiteral(1)));
 Node sideEffectFreeStatement = IR.statement(sideEffectFreeExpression);
 root.addChildToBack(sideEffectFreeStatement);
 checkSideEffects.process(null, root);
 assertEquals(0, checkSideEffects.problemNodes.size());

 // Test statement with multiple expressions
 Node multipleExpressions = IR.block(
 IR.statement(IR.name("x").assign(IR.intLiteral(1))),
 IR.statement(IR.name("y").assign(IR.intLiteral(2)))
 );
 root.addChildToBack(multipleExpressions);
 checkSideEffects.process(null, root);
 assertEquals(0, checkSideEffects.problemNodes.size());

 // Test statement with assignments and operators
 Node assignmentOperator = IR.binaryOp(
 IR.name("x"),
 Token.ASSIGN,
 IR.name("y")
 );
 Node operatorExpression = IR.expression(assignmentOperator);
 Node operatorStatement = IR.statement(operatorExpression);
 Node statementWithAssignments = IR.block(
 IR.statement(IR.name("x").assign(IR.intLiteral(1))),
 operatorStatement
 );
 root.addChildToBack(statementWithAssignments);
 checkSideEffects.process(null, root);
 assertEquals(1, checkSideEffects.problemNodes.size());
 assertNotNull(checkSideEffects.problemNodes.get(0).getJSDocInfo());

 // Test statement with side-effect
 Node sideEffect = IR.statement(IR.expressionStatement(IR.call(IR.name("alert"), IR.stringLiteral("Hello"))));
 root.addChildToBack(sideEffect);
 checkSideEffects.process(null, root);
 assertEquals(1, checkSideEffects.problemNodes.size());
 assertNotNull(checkSideEffects.problemNodes.get(0).getJSDocInfo());

 // Test statement with side-effect and protectSideEffectFreeCode
 Node sideEffectFree = IR.statement(IR.name("x").assign(IR.intLiteral(1)));
 Node protectSideEffect = IR.expressionStatement(IR.call(IR.name("JSCOMPILER_PRESERVE"), sideEffectFree));
 Node protectSideEffectStatement = IR.statement(protectSideEffect);
 root.addChildToBack(protectSideEffectStatement);
 checkSideEffects.process(null, root);
 assertEquals(0, checkSideEffects.problemNodes.size());

 // Test statement with side-effect and protectSideEffectFreeCode in different scopes
 Node protectSideEffectInDifferentScope = IR.statement(IR.expressionStatement(IR.call(IR.name("JSCOMPILER_PRESERVE"), sideEffectFreeStatement)));
 root.addChildToBack(protectSideEffectInDifferentScope);
 checkSideEffects.process(null, root);
 assertEquals(0, checkSideEffects.problemNodes.size());

 // Test statement with side-effect and protectSideEffectFreeCode with JSDoc info
 Node jsdocInfo = new JSDocInfoBuilder()
 .build(IR.name("myFunction"));
 Node protectSideEffectWithJSDoc = IR.statement(IR.expressionStatement(IR.call(IR.name("JSCOMPILER_PRESERVE"), sideEffectFreeStatement)));
 protectSideEffectWithJSDoc.setJSDocInfo(jsdocInfo);
 root.addChildToBack(protectSideEffectWithJSDoc);
 checkSideEffects.process(null, root);
 assertEquals(0, checkSideEffects.problemNodes.size());

 // Test block with side-effect
 Node blockWithSideEffect = IR.block(sideEffect);
 root.addChildToBack(blockWithSideEffect);
 checkSideEffects.process(null, root);
 assertEquals(1, checkSideEffects.problemNodes.size());
 assertNotNull(checkSideEffects.problemNodes.get(0).getJSDocInfo());

 // Test block with side-effect and protectSideEffectFreeCode
 Node blockWithSideEffectAndProtect = IR.block(protectSideEffectStatement);
 root.addChildToBack(blockWithSideEffectAndProtect);
 checkSideEffects.process(null, root);
 assertEquals(0, checkSideEffects.problemNodes.size());

 // Test statement with side-effect and checkLevel set to ERROR
 CheckLevel levelError = CheckLevel.ERROR;
 CheckSideEffects checkSideEffectsError = new CheckSideEffects(compiler, levelError, true);
 root.addChildToBack(sideEffect);
 checkSideEffectsError.process(null, root);
 assertFalse("Expected an error, but got no errors", checkSideEffectsError.hasErrors());
 }

 @Test
 public void testStripProtection() {
 CheckLevel level = CheckLevel.ERROR;
 AbstractCompiler compiler = new Compiler();
 CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);
 StripProtection stripProtection = new StripProtection(compiler);

 // Test empty statement
 Node root = IR.block();
 Node emptyStatement = IR.statement();
 root.addChildToBack(emptyStatement);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(0, root.getChildCount());

 // Test simple statement
 Node simpleStatement = IR.statement(IR.name("x").assign(IR.intLiteral(1)));
 root.addChildToBack(simpleStatement);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.ASSIGN, root.getChild(0).getType());

 // Test expression statement
 Node exprStatement = IR.expressionStatement(IR.name("x").assign(IR.intLiteral(1)));
 root.addChildToBack(exprStatement);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.ASSIGN, root.getChild(0).getType());

 // Test side-effect free expression
 Node sideEffectFreeExpression = IR.expression(IR.name("x").assign(IR.intLiteral(1)));
 Node sideEffectFreeStatement = IR.statement(sideEffectFreeExpression);
 root.addChildToBack(sideEffectFreeStatement);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(0, root.getChildCount());

 // Test statement with multiple expressions
 Node multipleExpressions = IR.block(
 IR.statement(IR.name("x").assign(IR.intLiteral(1))),
 IR.statement(IR.name("y").assign(IR.intLiteral(2)))
 );
 root.addChildToBack(multipleExpressions);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(2, root.getChildCount());

 // Test statement with assignments and operators
 Node assignmentOperator = IR.binaryOp(
 IR.name("x"),
 Token.ASSIGN,
 IR.name("y")
 );
 Node operatorExpression = IR.expression(assignmentOperator);
 Node operatorStatement = IR.statement(operatorExpression);
 Node statementWithAssignments = IR.block(
 IR.statement(IR.name("x").assign(IR.intLiteral(1))),
 operatorStatement
 );
 root.addChildToBack(statementWithAssignments);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(2, root.getChildCount());

 // Test statement with side-effect
 Node sideEffect = IR.statement(IR.expressionStatement(IR.call(IR.name("alert"), IR.stringLiteral("Hello"))));
 root.addChildToBack(sideEffect);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.CALL, root.getChild(0).getType());

 // Test statement with side-effect and protectSideEffectFreeCode
 Node protectSideEffect = IR.expressionStatement(IR.call(IR.name("JSCOMPILER_PRESERVE"), sideEffectFreeStatement));
 Node protectSideEffectStatement = IR.statement(protectSideEffect);
 root.addChildToBack(protectSideEffectStatement);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.CALL, root.getChild(0).getType());

 // Test statement with side-effect and protectSideEffectFreeCode in different scopes
 Node protectSideEffectInDifferentScope = IR.statement(IR.expressionStatement(IR.call(IR.name("JSCOMPILER_PRESERVE"), sideEffectFreeStatement)));
 root.addChildToBack(protectSideEffectInDifferentScope);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.CALL, root.getChild(0).getType());

 // Test statement with side-effect and protectSideEffectFreeCode with JSDoc info
 Node jsdocInfo = new JSDocInfoBuilder()
 .build(IR.name("myFunction"));
 Node protectSideEffectWithJSDoc = IR.statement(IR.expressionStatement(IR.call(IR.name("JSCOMPILER_PRESERVE"), sideEffectFreeStatement)));
 protectSideEffectWithJSDoc.setJSDocInfo(jsdocInfo);
 root.addChildToBack(protectSideEffectWithJSDoc);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.CALL, root.getChild(0).getType());

 // Test block with side-effect
 Node blockWithSideEffect = IR.block(sideEffect);
 root.addChildToBack(blockWithSideEffect);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.BLOCK, root.getChild(0).getType());

 // Test block with side-effect and protectSideEffectFreeCode
 Node blockWithSideEffectAndProtect = IR.block(protectSideEffectStatement);
 root.addChildToBack(blockWithSideEffectAndProtect);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.BLOCK, root.getChild(0).getType());

 // Test block with side-effect and protectSideEffectFreeCode in different scopes
 Node protectSideEffectInDifferentScopeBlock = IR.block(protectSideEffectInDifferentScope);
 root.addChildToBack(protectSideEffectInDifferentScopeBlock);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.BLOCK, root.getChild(0).getType());

 // Test block with side-effect and protectSideEffectFreeCode with JSDoc info
 Node protectSideEffectWithJSDocBlock = IR.block(protectSideEffectWithJSDoc);
 root.addChildToBack(protectSideEffectWithJSDocBlock);
 checkSideEffects.process(null, root);
 stripProtection.process(null, root);
 assertEquals(1, root.getChildCount());
 assertEquals(Token.BLOCK, root.getChild(0).getType());
 }

 private static boolean hasErrors(Compiler compiler) {
 List<String> messages = Lists.newArrayList();
 compiler.getDiagnostics().accept(new DiagnosticVisitor() {
 @Override
 public void visit(DiagnosticType type, Node node, String message, int code, int column, int line, int sourceStart, int sourceEnd) {
 messages.add(message);
 }
 });
 return !messages.isEmpty();
 }
}