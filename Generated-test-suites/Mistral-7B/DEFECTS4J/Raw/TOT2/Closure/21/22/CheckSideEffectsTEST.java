import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;

public class CheckSideEffectsTest {
  @Test
  public void testCheckSideEffects() {
    // Basic functionality test
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, WARNING, false);
    Node testNode = IR.literal(1);
    checkSideEffects.process(null, testNode);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    // Edge case test with void statements
    Node voidStatementNode = IR.semicolon();
    checkSideEffects.process(null, voidStatementNode);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Exception handling test with JSDocInfo
    Node jsDocInfoNode = IR.literal("this is a JSDoc comment");
    jsDocInfoNode.setJSDocInfo(new JSDocInfoBuilder().build(jsDocInfoNode));
    checkSideEffects.process(null, jsDocInfoNode);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Exception handling test with simple operators
    Node addNode = IR.binaryOp(Token.PLUS, IR.literal(1), IR.literal(1));
    checkSideEffects.process(null, addNode);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    // Exception handling test with FOR statement
    Node forStatementNode = IR.forInit(IR.name("i"), IR.literal(0), IR.literal(1), IR.literal(1));
    Node forUpdateNode = IR.statement(IR.assignment(IR.name("i"), IR.literal(1)));
    Node forBodyNode = IR.statement(IR.assignment(IR.name("i"), IR.literal(2)));
    Node forStatement = IR.statementList(forInitNode, forStatementNode, forUpdateNode, forBodyNode);
    Node blockNode = IR.block(forStatement);
    checkSideEffects.process(null, blockNode);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
}