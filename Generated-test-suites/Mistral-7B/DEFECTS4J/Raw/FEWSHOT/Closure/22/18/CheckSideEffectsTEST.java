package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    // Test empty case
    CheckSideEffects checker = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = null;
    checker.process(null, root);

    // Test void statement
    CheckSideEffects checkerVoid = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode("{}");
    checkerVoid.process(null, root);

    // Test expression result
    CheckSideEffects checkerExprResult = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode("function foo() { return 1; }");
    checkerExprResult.process(null, root);

    // Test block
    CheckSideEffects checkerBlock = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode("{}");
    checkerBlock.process(null, root);

    // Test call with no arguments
    CheckSideEffects checkerCallNoArg = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode("function foo() {} foo();");
    checkerCallNoArg.process(null, root);

    // Test call with arguments
    CheckSideEffects checkerCallArg = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode("function foo(x) {} foo(1);");
    checkerCallArg.process(null, root);

    // Test simple operator
    CheckSideEffects checkerSimpleOp = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode("var x = 1 + 1;");
    checkerSimpleOp.process(null, root);

    // Test may have side effects
    CheckSideEffects checkerMayHaveSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode("function foo() { document.write('Hello World'); } foo();");
    checkerMayHaveSideEffects.process(null, root);

    // Test assignment
    CheckSideEffects checkerAssignment = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode("var x = 1; x = 2;");
    checkerAssignment.process(null, root);

    // Test qualifed name with JSDoc info
    CheckSideEffects checkerQualifiedName = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode(
        "/**\n" +
        " * @constructor\n" +
        " */\n" +
        "function Foo() {\n" +
        " this.x = 1;\n" +
        "}\n" +
        "new Foo();");
    checkerQualifiedName.process(null, root);

    // Test expression result used
    root = createNode("var x = 1 + 1;");
    CheckSideEffects checkerExpressionResultUsed = new CheckSideEffects(null, CheckLevel.WARNING, false);
    checkerExpressionResultUsed.process(null, root);
    assertTrue(checkerExpressionResultUsed.problemNodes.isEmpty());

    // Test simple operator used
    root = createNode("var x = 1 + 1;");
    CheckSideEffects checkerSimpleOperatorUsed = new CheckSideEffects(null, CheckLevel.WARNING, false);
    checkerSimpleOperatorUsed.process(null, root);
    assertTrue(checkerSimpleOperatorUsed.problemNodes.isEmpty());

    // Test side effect free code protection
    CheckSideEffects checkerProtectSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    root = createNode("function foo() { return 1 + 1; } foo();");
    checkerProtectSideEffects.process(null, root);
    assertFalse(checkerProtectSideEffects.problemNodes.isEmpty());

    // Test strip protection
    CheckSideEffects checkerStripProtection = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = createNode(
        "function JSCompiler_PROTECTOR_FN(x) {\n" +
        " return x;\n" +
        "}\n" +
        "JSCompiler_PROTECTOR_FN(1);");
    checkerStripProtection.process(null, root);
    assertTrue(checkerStripProtection.problemNodes.isEmpty());
  }

  private Node createNode(String ast) {
    return new Node(Token.parse(ast));
  }
}