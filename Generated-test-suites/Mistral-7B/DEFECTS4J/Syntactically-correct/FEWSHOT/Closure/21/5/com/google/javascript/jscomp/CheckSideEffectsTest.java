package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeOperations;

import java.util.List;

/**
 * Test cases for CheckSideEffects class.
 */
public class CheckSideEffectsTest {

  private static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";

  @Test
  public void testCheckSideEffects() {
    Compiler compiler = mock(Compiler.class);
    Checker checker = mock(Checker.class);
    when(compiler.getContext()).thenReturn(checker);
    AbstractCompiler abstractCompiler = new CompilerPassRegistry(compiler);

    CheckSideEffects checkSideEffects = new CheckSideEffects(abstractCompiler, CheckLevel.WARNING, false);

    SourceFile sourceFile = mock(SourceFile.class);
    Node rootNode = IR.block(null, null);

    NodeTraversal nodeTraversal = new NodeTraversal(abstractCompiler, rootNode, null);
    nodeTraversal.traverse(checkSideEffects);

    Node voidNode = IR.emptyStatement(null);
    Node commaNode = IR.comma(null);
    Node commaNode1 = IR.comma(null);
    Node commaNode2 = IR.comma(null);
    Node stringNode1 = IR.stringLiteral("this string is ");
    Node stringNode2 = IR.stringLiteral("continued on the next line but you forgot the +");
    Node equalsNode = IR.binaryOperator(Token.EQUALS, null, IR.name("foo"), IR.call(IR.name("foo"), null));
    Node semicolonNode1 = IR.semicolon(null);
    Node semicolonNode2 = IR.semicolon(null);
    Node statementNode1 = IR.assign(IR.name("x"), equalsNode);
    Node statementNode2 = IR.expressionStatement(IR.call(IR.name("foo"), null));
    Node statementNode3 = IR.expressionStatement(IR.call(IR.name("foo"), null));
    Node statementNode4 = IR.expressionStatement(IR.call(IR.name("foo"), null));
    Node statementNode5 = IR.expressionStatement(IR.call(IR.name("foo"), null));
    Node blockNode = IR.block(null, Lists.newArrayList(statementNode1, statementNode2, statementNode3, statementNode4, statementNode5));

    Node simpleOpNode1 = IR.binaryOperator(Token.PLUS, null, stringNode1, stringNode2);
    Node simpleOpNode2 = IR.binaryOperator(Token.MINUS, null, IR.name("x"), IR.number(1));

    Node ifConditionNode = IR.binaryOperator(Token.LOGICAL_AND, null,
        IR.isTypeOf(IR.name("x"), IR.stringType()), IR.isTypeOf(IR.name("x"), IR.numberType()));
    Node ifStatementNode = IR.ifStatement(ifConditionNode, blockNode, null);

    Node forStatementNode = IR.forStatement(null, IR.name("i"), IR.number(1), IR.number(1), IR.number(1), IR.block(null, Lists.newArrayList(
        IR.assign(IR.name("i"), IR.add(IR.name("i"), IR.number(1))),
        IR.expressionStatement(IR.call(IR.name("console.log"), null, IR.stringLiteral("i")))
    )));

    Node methodCallNode = IR.call(IR.name("foo"), null);
    Node methodCallStatementNode = IR.expressionStatement(methodCallNode);

    Node voidExpressionNode = IR.block(null, null);
    Node emptyStatementNode = IR.emptyStatement(null);

    rootNode.addChildrenToBack(voidNode);
    rootNode.addChildrenToBack(commaNode);
    commaNode.addChildrenToBack(commaNode1);
    commaNode.addChildrenToBack(commaNode2);
    commaNode1.addChildrenToBack(voidNode);
    commaNode1.addChildrenToBack(stringNode1);
    commaNode2.addChildrenToBack(equalsNode);
    commaNode2.addChildrenToBack(semicolonNode1);
    commaNode2.addChildrenToBack(semicolonNode2);
    rootNode.addChildrenToBack(statementNode1);
    rootNode.addChildrenToBack(statementNode2);
    rootNode.addChildrenToBack(statementNode3);
    rootNode.addChildrenToBack(statementNode4);
    rootNode.addChildrenToBack(statementNode5);
    rootNode.addChildrenToBack(simpleOpNode1);
    rootNode.addChildrenToBack(simpleOpNode2);
    rootNode.addChildrenToBack(ifStatementNode);
    rootNode.addChildrenToBack(forStatementNode);
    rootNode.addChildrenToBack(methodCallStatementNode);

    nodeTraversal.traverse(checkSideEffects);

    List<Node> problemNodes = checkSideEffects.problemNodes;

    // Test void statement and comma statements.
    assertTrue(problemNodes.isEmpty());

    // Test non-side effect statements with no side effect expressions.
    Node rootNode1 = IR.block(null, null);
    rootNode1.addChildrenToBack(simpleOpNode1);
    rootNode1.addChildrenToBack(simpleOpNode2);
    nodeTraversal = new NodeTraversal(abstractCompiler, rootNode1, null);
    nodeTraversal.traverse(checkSideEffects);
    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    // Test non-side effect statements with side effect expressions.
    Node rootNode2 = IR.block(null, null);
    rootNode2.addChildrenToBack(stringNode1);
    rootNode2.addChildrenToBack(equalsNode);
    nodeTraversal = new NodeTraversal(abstractCompiler, rootNode2, null);
    nodeTraversal.traverse(checkSideEffects);
    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 1);
    assertEquals(equalsNode, problemNodes.get(0));

    // Test side effect statements.
    Node rootNode3 = IR.block(null, null);
    rootNode3.addChildrenToBack(statementNode1);
    nodeTraversal = new NodeTraversal(abstractCompiler, rootNode3, null);
    nodeTraversal.traverse(checkSideEffects);
    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 5);
    for (Node problemNode : problemNodes) {
      if (problemNode == statementNode1) continue;
      assertNotNull(problemNode);
    }

    // Test side effect statements with side effect free statements.
    rootNode3 = IR.block(null, null);
    rootNode3.addChildrenToBack(statementNode1);
    rootNode3.addChildrenToBack(simpleOpNode1);
    nodeTraversal = new NodeTraversal(abstractCompiler, rootNode3, null);
    nodeTraversal.traverse(checkSideEffects);
    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 4);
    for (Node problemNode : problemNodes) {
      if (problemNode == simpleOpNode1) continue;
      assertNotNull(problemNode);
    }

    // Test if statement with non-side effect statements.
    Node rootNode4 = IR.block(null, null);
    rootNode4.addChildrenToBack(ifStatementNode);
    nodeTraversal = new NodeTraversal(abstractCompiler, rootNode4, null);
    nodeTraversal.traverse(checkSideEffects);
    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    // Test if statement with side effect statements.
    rootNode4 = IR.block(null, null);
    rootNode4.addChildrenToBack(ifStatementNode);
    rootNode4.addChildrenToBack(statementNode3);
    nodeTraversal = new NodeTraversal(abstractCompiler, rootNode4, null);
    nodeTraversal.traverse(checkSideEffects);
    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.size() == 5);
    for (Node problemNode : problemNodes) {
      if (problemNode == statementNode3) continue;
      assertNotNull(problemNode);
    }

    // Test for statement.
    rootNode4 = IR.block(null, null);
    rootNode4.addChildrenToBack(forStatementNode);
    nodeTraversal = new NodeTraversal(abstractCompiler, rootNode4, null);
    nodeTraversal.traverse(checkSideEffects);
    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());

    // Test method call statement.
    rootNode4 = IR.block(null, null);
    rootNode4.addChildrenToBack(methodCallStatementNode);
    nodeTraversal = new NodeTraversal(abstractCompiler, rootNode4, null);
    nodeTraversal.traverse(checkSideEffects);
    problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    Compiler compiler = mock(Compiler.class);
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    Node rootNode = IR.block(null, null);
    Node sideEffectFreeNode = IR.binaryOperator(Token.MINUS, null, IR.name("x"), IR.number(1));
    rootNode.addChildrenToBack(sideEffectFreeNode);

    checkSideEffects.protectSideEffects();
    Node protectedNode = checkSideEffects.problemNodes.get(0);
    Node replacementNode = protectedNode.getLastChild();
    assertEquals("JSCOMPILER_PRESERVE.call(JSCOMPILER_PRESERVE, " + protectedNode + ")",
        replacementNode.toString());

    Node callerNode = IR.call(IR.name("JSCOMPILER_PRESERVE"), null, protectedNode);
    assertEquals(sideEffectFreeNode, callerNode.getFirstChild());
    assertEquals(replacementNode, callerNode.getLastChild());

    Node rootNode1 = IR.block(null, null);
    rootNode1.addChildrenToBack(sideEffectFreeNode);
    rootNode1.addChildrenToBack(IR.name("console.log"));
    rootNode1.addChildrenToBack(IR.stringLiteral("sideEffectFreeNode"));
    rootNode1.addChildrenToBack(IR.call(IR.name("JSCOMPILER_PRESERVE"), null, sideEffectFreeNode));

    checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    checkSideEffects.protectSideEffects();

    Node protectedSideEffectFreeNode = checkSideEffects.problemNodes.get(0);
    Node protectedCallerNode = protectedSideEffectFreeNode.getLastChild();
    assertEquals("JSCOMPILER_PRESERVE.call(JSCOMPILER_PRESERVE, " + protectedSideEffectFreeNode + ")",
        protectedCallerNode.toString());

    Node replacementSideEffectFreeNode = protectedCallerNode.getFirstChild();
    assertEquals(sideEffectFreeNode, replacementSideEffectFreeNode);

    Node protectedConsoleLogNode = protectedCallerNode.getNext();
    assertEquals("console.log", protectedConsoleLogNode.toString());

    Node protectedStringLiteralNode = protectedConsoleLogNode.getNext();
    assertEquals("sideEffectFreeNode", protectedStringLiteralNode.toString());
  }

  @Test
  public void testStripProtection() {
    Compiler compiler = mock(Compiler.class);
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    StripProtection stripProtection = new StripProtection(compiler);

    Node rootNode = IR.block(null, null);
    Node sideEffectFreeNode = IR.binaryOperator(Token.MINUS, null, IR.name("x"), IR.number(1));
    rootNode.addChildrenToBack(IR.call(IR.name("JSCOMPILER_PRESERVE"), null, sideEffectFreeNode));

    CheckSideEffects testCheckSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    testCheckSideEffects.protectSideEffects();

    Node protectedNode = testCheckSideEffects.problemNodes.get(0);
    Node replacementNode = protectedNode.getLastChild();

    stripProtection.process(null, rootNode);
    assertTrue(testCheckSideEffects.problemNodes.isEmpty());
    assertFalse(rootNode.hasChild(protectedNode));
    assertFalse(rootNode.hasChild(replacementNode));
  }
}