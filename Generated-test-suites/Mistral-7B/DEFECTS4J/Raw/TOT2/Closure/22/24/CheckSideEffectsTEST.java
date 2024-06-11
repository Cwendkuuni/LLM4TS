import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

public class CheckSideEffectsTest {

  @org.junit.Test
  public void testCheckSideEffects() {
    // Given
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    Node root = new Node(Token.BLOCK);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    // When
    checkSideEffects.process(null, root);

    // Then
    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertEquals(0, problemNodes.size());

    // Test for a basic use case
    Node basicUseCase = new Node(Token.NAME, "x");
    Node assignStatement = new Node(Token.ASSIGN, basicUseCase, new Node(Token.NAME, "foo"));
    Node statementBlock = new Node(Token.BLOCK, assignStatement);
    NodeTraversal.traverse(compiler, statementBlock, checkSideEffects);
    checkSideEffects.process(null, statementBlock);
    assertEquals(0, problemNodes.size());

    // Test for a edge case with a simple operator
    Node edgeCaseWithSimpleOperator = new Node(Token.NAME, "x");
    Node addOperator = new Node(Token.PLUS, new Node(Token.NAME, "y"), edgeCaseWithSimpleOperator);
    Node statementBlockWithSimpleOperator = new Node(Token.BLOCK, addOperator);
    NodeTraversal.traverse(compiler, statementBlockWithSimpleOperator, checkSideEffects);
    checkSideEffects.process(null, statementBlockWithSimpleOperator);
    assertEquals(0, problemNodes.size());

    // Test for an error scenario with a void statement
    Node errorScenarioWithVoidStatement = new Node(Token.VOID);
    Node statementBlockWithVoidStatement = new Node(Token.BLOCK, errorScenarioWithVoidStatement);
    NodeTraversal.traverse(compiler, statementBlockWithVoidStatement, checkSideEffects);
    checkSideEffects.process(null, statementBlockWithVoidStatement);
    assertEquals(0, problemNodes.size());

    // Test for an error scenario with a missing '+' sign
    Node errorScenarioWithMissingPlusSign = new Node(Token.STRING_LITERAL, "this string is ");
    Node nextNode = new Node(Token.NAME, "continued");
    Node missingPlusSign = new Node(Token.NAME, "on");
    Node nextNextNode = new Node(Token.NAME, "the next line");
    Node statementBlockWithMissingPlusSign = new Node(Token.BLOCK, errorScenarioWithMissingPlusSign, nextNode, missingPlusSign, nextNextNode);
    NodeTraversal.traverse(compiler, statementBlockWithMissingPlusSign, checkSideEffects);
    checkSideEffects.process(null, statementBlockWithMissingPlusSign);
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    String expectedMessage = "Is there a missing '+' on the previous line?";
    assertEquals(expectedMessage, problemNode.getErrorMessage());

    // Test for an error scenario with a stray semicolon
    Node errorScenarioWithStraySemicolon = new Node(Token.SEMICOLON);
    Node statementBlockWithStraySemicolon = new Node(Token.BLOCK, errorScenarioWithStraySemicolon);
    NodeTraversal.traverse(compiler, statementBlockWithStraySemicolon, checkSideEffects);
    checkSideEffects.process(null, statementBlockWithStraySemicolon);
    assertEquals(0, problemNodes.size());

    // Test for an error scenario with an eval statement
    Node errorScenarioWithEvalStatement = new Node(Token.NAME, "eval");
    Node statementBlockWithEvalStatement = new Node(Token.BLOCK, errorScenarioWithEvalStatement);
    NodeTraversal.traverse(compiler, statementBlockWithEvalStatement, checkSideEffects);
    checkSideEffects.process(null, statementBlockWithEvalStatement);
    assertEquals(0, problemNodes.size());
  }
}