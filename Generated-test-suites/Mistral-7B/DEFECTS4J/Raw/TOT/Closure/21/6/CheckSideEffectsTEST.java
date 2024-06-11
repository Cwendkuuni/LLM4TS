package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckSideEffectsTest {

 private CheckSideEffects checkSideEffects;
 @Mock private AbstractCompiler compiler;
 @Mock private Node rootNode;

 @Before
 public void setUp() {
 checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 }

 @Test
 public void testVisitNonSideEffectStatement() {
 Node statementNode = createStatementNode("x = y + z");
 Node parentNode = createBlockNode();
 parentNode.addChildToBack(statementNode);
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(0, checkSideEffects.problemNodes.size());
 }

 @Test
 public void testVisitSideEffectStatement() {
 Node statementNode = createStatementNode("document.write('Hello World')");
 Node parentNode = createBlockNode();
 parentNode.addChildToBack(statementNode);
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(1, checkSideEffects.problemNodes.size());
 assertEquals(statementNode, checkSideEffects.problemNodes.get(0));
 }

 @Test
 public void testVisitVoidStatement() {
 Node statementNode = createStatementNode(";");
 Node parentNode = createBlockNode();
 parentNode.addChildToBack(statementNode);
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(0, checkSideEffects.problemNodes.size());
 }

 @Test
 public void testVisitForLoop() {
 Node forNode = createForLoopNode();
 Node statementNode = createStatementNode("x++");
 Node parentNode = forNode;
 parentNode.addChildToBack(statementNode);
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(0, checkSideEffects.problemNodes.size());
 }

 @Test
 public void testVisitStatementInsideBlock() {
 Node statementNode = createStatementNode("x = y + z");
 Node blockNode = createBlockNode();
 Node parentNode = blockNode;
 parentNode.addChildToBack(statementNode);
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(0, checkSideEffects.problemNodes.size());
 }

 @Test
 public void testVisitStatementInsideExpressionResult() {
 Node expressionResultNode = createExpressionResultNode();
 Node statementNode = createStatementNode("x++");
 Node parentNode = expressionResultNode;
 parentNode.addChildToBack(statementNode);
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(0, checkSideEffects.problemNodes.size());
 }

 @Test
 public void testVisitStatementInsideQualifiedName() {
 Node qualifiedNameNode = createQualifiedNameNode("MyClass.myMethod");
 Node statementNode = createStatementNode("x++");
 Node parentNode = qualifiedNameNode;
 parentNode.addChildToBack(statementNode);
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(0, checkSideEffects.problemNodes.size());
 }

 @Test
 public void testVisitStatementInsideComma() {
 Node commaNode = createCommaNode();
 Node statementNode1 = createStatementNode("x++");
 Node statementNode2 = createStatementNode("y++");
 Node parentNode = commaNode;
 parentNode.addChildrenToBack(Lists.newArrayList(statementNode1, statementNode2));
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(0, checkSideEffects.problemNodes.size());
 }

 @Test
 public void testVisitStatementInsideIfStatement() {
 Node ifStatementNode = createIfStatementNode();
 Node statementNode = createStatementNode("x++");
 Node parentNode = ifStatementNode;
 parentNode.addChildToBack(statementNode);
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(0, checkSideEffects.problemNodes.size());
 }

 @Test
 public void testProtectSideEffects() {
 checkSideEffects.process(null, rootNode);
 checkSideEffects.protectSideEffects();
 Node problemNode = createStatementNode("document.write('Hello World')");
 Node parentNode = createBlockNode();
 parentNode.addChildToBack(problemNode);
 checkSideEffects.visit(null, parentNode, rootNode);
 assertEquals(1, checkSideEffects.problemNodes.size());
 assertEquals(problemNode, checkSideEffects.problemNodes.get(0));
}

 private Node createStatementNode(String statement) {
 return IR.makeNode(compiler, statement, null, null, null);
}

 private Node createBlockNode() {
 return IR.block(null, null, null, null);
}

 private Node createForLoopNode() {
 Node forToken = IR.token(FOR, null, null, null);
 Node initialization = IR.makeNode(compiler, "i = 0", null, null, null);
 Node condition = IR.makeNode(compiler, "i < 10", null, null, null);
 Node increment = IR.makeNode(compiler, "i++", null, null, null);
 Node body = IR.makeNode(compiler, "x += i", null, null, null);
 Node forStatement = IR.makeNode(compiler, null, forToken, initialization, condition, increment, body, null);
 return forStatement;
}

 private Node createIfStatementNode() {
 Node ifToken = IR.token(IF, null, null, null);
 Node condition = IR.makeNode(compiler, "x > 0", null, null, null);
 Node thenBranch = IR.makeNode(compiler, "y++", null, null, null);
 Node elseBranch = IR.makeNode(compiler, null, null, null, null);
 Node ifStatement = IR.makeNode(compiler, null, ifToken, condition, thenBranch, elseBranch, null, null);
 return ifStatement;
}

 private Node createExpressionResultNode() {
 return IR.makeNode(compiler, null, EXPR_RESULT, null, null);
}

 private Node createQualifiedNameNode(String qualifiedName) {
 return IR.makeNode(compiler, qualifiedName, null, null, null);
}

 private Node createCommaNode() {
 return IR.token(COMMA, null, null, null);
}
}