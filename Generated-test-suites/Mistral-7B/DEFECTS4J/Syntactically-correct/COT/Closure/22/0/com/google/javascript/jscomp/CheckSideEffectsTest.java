import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.token.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckSideEffectsTest {

  @Mock
  private AbstractCompiler compiler;

  @InjectMocks
  private CheckSideEffects checkSideEffects;

  @Test
  public void testProcess_emptyCode() {
    // Test case for an empty code block
    CompilerInput input = createCompilerInput();
    checkSideEffects.process(input, NodeFactory.createRootNode());
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_voidStatement() {
    // Test case for void statement
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createStatement(Token.VOID, null));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_comment() {
    // Test case for comment
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createComment("This is a comment"));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_assignment() {
    // Test case for simple assignment
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createAssignment(Token.ASSIGN, "x",
            NodeFactory.createLiteral(1)));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_assignmentWithSideEffect() {
    // Test case for assignment with side-effect
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createAssignment(Token.ASSIGN, "x",
            NodeFactory.createCall(
                NodeFactory.createName("console"), "log",
                NodeFactory.createString("Hello World"))));
    checkSideEffects.process(input, root);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
    assertEquals(1, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcess_simpleExpression() {
    // Test case for simple expression
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createExpressionStatement(
            NodeFactory.createBinaryOp(
                Token.PLUS, NodeFactory.createName("x"),
                NodeFactory.createLiteral(1))));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_complexExpression() {
    // Test case for complex expression
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createExpressionStatement(
            NodeFactory.createBinaryOp(
                Token.MINUS,
                NodeFactory.createBinaryOp(
                    Token.TIMES,
                    NodeFactory.createName("x"),
                    NodeFactory.createName("y")),
                NodeFactory.createLiteral(2))));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_conditionalStatement() {
    // Test case for conditional statement
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createIfStatement(
            NodeFactory.createBinaryOp(Token.LESS_THAN, NodeFactory.createName("x"), NodeFactory.createLiteral(5)),
            NodeFactory.createBlock(
                NodeFactory.createExpressionStatement(
                    NodeFactory.createAssignment(Token.ASSIGN, NodeFactory.createName("y"), NodeFactory.createLiteral(6)))),
            NodeFactory.createBlock()));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_loopStatement() {
    // Test case for loop statement
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createStatement(Token.FOR,
            NodeFactory.createName("i"),
            NodeFactory.createLiteral(0),
            NodeFactory.createLiteral(10),
            NodeFactory.createBlock(
                NodeFactory.createExpressionStatement(
                    NodeFactory.createAssignment(Token.ASSIGN, NodeFactory.createName("x"), NodeFactory.createName("i"))))));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_callStatement() {
    // Test case for call statement
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createExpressionStatement(
            NodeFactory.createCall(NodeFactory.createName("foo"), NodeFactory.createArray())));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_returnStatement() {
    // Test case for return statement
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createReturnStatement(NodeFactory.createLiteral(7)));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_functionDeclaration() {
    // Test case for function declaration
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createFunctionDeclaration(
            NodeFactory.createName("myFunction"),
            NodeFactory.createList(NodeFactory.createName("arg1")),
            NodeFactory.createBlock(
                NodeFactory.createReturnStatement(NodeFactory.createLiteral(7)))));
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_statementWithSideEffect() {
    // Test case for statement with side-effect
    CompilerInput input = createCompilerInput();
    Node root = NodeFactory.createBlock(
        NodeFactory.createStatement(Token.ASSIGN, "document.body.style.backgroundColor", NodeFactory.createString("#FFF")));
    checkSideEffects.process(input, root);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
    assertEquals(1, checkSideEffects.problemNodes.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_nullInput() {
    checkSideEffects.process(null, NodeFactory.createRootNode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_nullRoot() {
    checkSideEffects.process(createCompilerInput(), null);
  }

  @Test
  public void testProtectSideEffects() {
    // Test case for protecting side-effect free code
    CompilerInput input = createCompilerInput();
    checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    Node root = NodeFactory.createBlock(
        NodeFactory.createExpressionStatement(
            NodeFactory.createBinaryOp(Token.PLUS, NodeFactory.createName("x"), NodeFactory.createLiteral(1))));
    checkSideEffects.process(input, root);
    checkSideEffects.protectSideEffects();
    Node expected = NodeFactory.createCall(
        NodeFactory.createName("JSCOMPILER_PRESERVE"), NodeFactory.createArray());
    assertEquals(expected, root.getLastChild());
  }

  @Test
  public void testStripProtection() {
    // Test case for stripping side-effect protection
    CompilerInput input = createCompilerInput();
    checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    Node root = NodeFactory.createBlock(
        NodeFactory.createCall(
            NodeFactory.createName("JSCOMPILER_PRESERVE"), NodeFactory.createArray()));
    checkSideEffects.process(input, root);
    checkSideEffects.protectSideEffects();
    Node expected = NodeFactory.createExpressionStatement(
        NodeFactory.createBinaryOp(Token.PLUS, NodeFactory.createName("x"), NodeFactory.createLiteral(1)));
    NodeTraversal.traverse(compiler, root, new StripProtection(compiler));
    assertEquals(expected, root.getLastChild());
  }

  private CompilerInput createCompilerInput() {
    return new CompilerInput("test.js", NodeFactory.createRootNode());
  }
}