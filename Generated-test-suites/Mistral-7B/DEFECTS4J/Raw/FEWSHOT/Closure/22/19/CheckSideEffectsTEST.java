package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is the name of the AbstractCompiler instance in your test setup
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);

    // Sample code for testing
    Node root = Node.newBlock("sampleCode");
    Node assignment1 = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    Node assignment2 = Node.newAssignment(Node.newName("y"), Node.newString("test"));
    Node expression = Node.newCall(Node.newName("foo"), Node.newList());
    Node statement1 = Node.newExpressionStatement(assignment1);
    Node statement2 = Node.newExpressionStatement(assignment2);
    Node statement3 = Node.newExpressionStatement(expression);
    root.addChildrenToBack(statement1, statement2, statement3);

    checkSideEffects.process(null, root);

    // Verify that no warnings were generated for the sample code
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for useless code
    root = Node.newBlock("uselessCode");
    Node statement4 = Node.newExpressionStatement(Node.newAssignment(Node.newName("x"), Node.newAssignment(Node.newName("x"), Node.newNumber(0))));
    Node statement5 = Node.newExpressionStatement(Node.newAssignment(Node.newName("x"), Node.newNumber(1)));
    root.addChildrenToBack(statement4, statement5);

    checkSideEffects.process(null, root);

    // Verify that a warning was generated for the useless code
    assertTrue(checkSideEffects.problemNodes.size() > 0);

    // Test for side-effect free function calls
    root = Node.newBlock("sideEffectFreeFunctionCalls");
    Node functionCall1 = Node.newCall(Node.newName("someFunction"), Node.newList());
    Node functionCall2 = Node.newCall(Node.newName("someFunction"), Node.newList());
    root.addChildrenToBack(functionCall1, functionCall2);

    checkSideEffects.process(null, root);

    // Verify that no warnings were generated for side-effect free function calls
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is the name of the AbstractCompiler instance in your test setup
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    // Sample code for testing
    Node root = Node.newBlock("sampleCode");
    Node assignment1 = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    Node assignment2 = Node.newAssignment(Node.newName("y"), Node.newString("test"));
    Node expression = Node.newCall(Node.newName("foo"), Node.newList());
    Node statement1 = Node.newExpressionStatement(assignment1);
    Node statement2 = Node.newExpressionStatement(assignment2);
    Node statement3 = Node.newExpressionStatement(expression);
    root.addChildrenToBack(statement1, statement2, statement3);

    checkSideEffects.process(null, root);

    // Verify that no warnings were generated for the sample code
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for protected side-effect free statements
    checkSideEffects.protectSideEffects();

    // Verify that protected side-effect free statements have been replaced with calls to the PROTECTOR_FN extern
    Node protectedRoot = checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(checkSideEffects.compiler);
    assertTrue(protectedRoot.getChildCount() > 0);
    Node protectedFunction = protectedRoot.getFirstChild();
    assertTrue(protectedFunction.isFunctionDeclaration());
    Node protectedFunctionBody = protectedFunction.getLastChild();
    assertTrue(protectedFunctionBody.getChildCount() > 0);
    Node protectedFunctionCall = protectedFunctionBody.getChild(0);
    assertTrue(protectedFunctionCall.isCall());
    Node protectedFunctionName = protectedFunctionCall.getFirstChild();
    assertTrue(protectedFunctionName.isName());
    assertTrue(protectedFunctionName.getString().equals(CheckSideEffects.PROTECTOR_FN));
  }

  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is the name of the AbstractCompiler instance in your test setup
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    // Sample code for testing
    Node root = Node.newBlock("protectedCode");
    Node protectedFunction = Node.newFunctionDeclaration(Node.newName("someProtectedFunction"), Node.newList(), Node.newList(), Node.newBlock("protectedCode"));
    Node statement = Node.newExpressionStatement(Node.newCall(Node.newName("CheckSideEffects").getChild(1), Node.newList(Node.newName("PROTECTOR_FN"))));
    protectedFunction.getBody().addChildrenToBack(statement);
    root.addChildrenToBack(protectedFunction);

    checkSideEffects.process(null, root);

    // Verify that no warnings were generated for the protected code
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for stripping protection
    StripProtection stripProtection = new StripProtection(checkSideEffects.compiler);
    stripProtection.process(null, root);

    // Verify that protected side-effect free statements have been removed
    Node strippedRoot = checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(checkSideEffects.compiler);
    assertTrue(protectedFunction.getChildCount() == 0);
  }
}