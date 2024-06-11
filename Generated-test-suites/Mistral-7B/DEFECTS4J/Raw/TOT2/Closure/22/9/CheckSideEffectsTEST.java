import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createCall;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createFunctionCall;
import static com.google.javascript.rhino.NodeFactory.createBinaryOperator;
import static com.google.javascript.rhino.NodeFactory.createNameExpression;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createLiteral;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createQualifiedName;
import static com.google.javascript.rhino.NodeFactory.createIfStatement;
import static com.google.javascript.rhino.NodeFactory.createReturnStatement;
import static com.google.javascript.rhino.NodeFactory.createStatement;
import static com.google.javascript.rhino.NodeFactory.createPostfixOperator;
import static com.google.javascript.rhino.NodeFactory.createBooleanLiteral;
import static com.google.javascript.rhino.NodeFactory.createLiteralNumber;
import static com.google.javascript.rhino.NodeFactory.createIdentificator;
import static com.google.javascript.rhino.NodeFactory.createIdentifierExpression;
import static com.google.javascript.rhino.NodeFactory.createVariable;
import static com.google.javascript.rhino.NodeFactory.createVariableStatement;

import org.junit.Test;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ScriptContext;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.SourceFile;

public class CheckSideEffectsTest {
  @Test
  public void testCheckSideEffects() {
    
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, level, true);

    SourceFile sourceFile = new SourceFile("test.js", "test");
    Node root = sourceFile.getOrCreateRootNode();

    // Basic functionality test
    Node statement = createStatement(createBinaryOperator(
        createNameExpression("x"), Token.OP_ASSIGN, createName("foo")));
    Node context = createBlock(statement);
    checkSideEffects.process(sourceFile, context);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Edge cases and exception handling scenarios

    // Test for missing '+' sign before continued string
    statement = createStatement(createString("this string is ")
        .concat(createString("continued on the next line but you forgot the +")));
    context = createBlock(statement);
    checkSideEffects.process(sourceFile, context);
    assertEquals(1, checkSideEffects.problemNodes.size());
    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(Token.TOKEN_STRING, problemNode.getType());
    assertFalse(problemNode.isStatement());

    // Test for empty statement
    statement = createStatement(createCommaOperator(createName("x")));
    context = createBlock(statement);
    checkSideEffects.process(sourceFile, context);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for expression statement
    statement = createStatement(createFunctionCall(createName("eval"), createArray()));
    context = createBlock(statement);
    checkSideEffects.process(sourceFile, context);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for statement inside for loop
    statement = createStatement(createName("x"));
    Node forNode = createForStatement(createName("i"), createLiteralNumber(0),
        createLiteralNumber(1), createStatementList(statement));
    context = createBlock(forNode);
    checkSideEffects.process(sourceFile, context);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for statement inside if statement
    statement = createStatement(createName("x"));
    Node ifNode = createIfStatement(createName("condition"), createStatementList(statement), null);
    context = createBlock(ifNode);
    checkSideEffects.process(sourceFile, context);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for statement inside switch statement
    statement = createStatement(createName("x"));
    Node switchNode = createSwitchStatement(createNameExpression("x"), null,
        createStatementList(statement));
    context = createBlock(switchNode);
    checkSideEffects.process(sourceFile, context);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for statement inside try-catch block
    Node tryNode = createTryStatement(createBlock(createStatement(createName("x"))), null);
    Node catchNode = createCatchBlock(createIdentifier("e"), createBlock(), null);
    Node finallyNode = createFinallyBlock(createBlock(), null);
    Node statementBlock = createBlock(createStatement(createName("x")));
    Node contextNode = createBlock(createStatement(tryNode));
    Node rootNode = createBlock(createStatement(catchNode), createStatement(finallyNode), contextNode);
    checkSideEffects.process(sourceFile, rootNode);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for statement inside with statement
    statement = createStatement(createName("x"));
    Node withNode = createWithStatement(createNameExpression("obj"), createBlock(statement));
    context = createBlock(withNode);
    checkSideEffects.process(sourceFile, context);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for statement inside do-while loop
    statement = createStatement(createName("x"));
    Node doWhileNode = createDoWhileStatement(createName("condition"), createStatementList(statement));
    context = createBlock(doWhileNode);
    checkSideEffects.process(sourceFile, context);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test for statement inside while loop
    statement = createStatement(createName("x"));
    Node whileNode = createWhileStatement(createName("condition"), createStatementList(statement));
    context = createBlock(whileNode);
    checkSideEffects.process(sourceFile, context);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    
  }
}