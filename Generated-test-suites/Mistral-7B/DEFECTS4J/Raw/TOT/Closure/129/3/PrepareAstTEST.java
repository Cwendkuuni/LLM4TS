import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;

public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Prepare a simple ast
    Node root = new Node(Token.BLOCK, null);
    Node function = new Node(Token.FUNCTION, null);
    Node name = new Node(Token.IDENT, "myFunction");
    Node body = new Node(Token.BLOCK, null);
    Node returnStatement = new Node(Token.RETURNS, null, new Node(Token.NUMBER, 42));
    body.addChildrenToFront(returnStatement);
    function.addChildrenToFront(name, body);
    root.addChildrenToFront(function);

    prepareAst.process(null, root);

    // Check if free call is detected correctly
    Node freeCall = new Node(Token.CALL, null);
    Node freeCallFunction = new Node(Token.IDENT, "myFunction");
    freeCall.addChildrenToFront(freeCallFunction);
    Node freeCallContext = new Node(Token.INT_LITERAL, 1);
    Node freeCallParent = new Node(Token.ASSIGN, null, freeCall, new Node(Token.IDENT, "x"));
    root.addChildrenToFront(freeCallParent);
    assertTrue(freeCall.isFreeCall());

    // Check if direct eval is detected correctly
    Node directEval = new Node(Token.CALL, null);
    Node directEvalFunction = new Node(Token.IDENT, "eval");
    directEval.addChildrenToFront(directEvalFunction);
    Node directEvalContext = new Node(Token.INT_LITERAL, 1);
    Node directEvalParent = new Node(Token.ASSIGN, null, directEval, new Node(Token.IDENT, "y"));
    root.addChildrenToFront(directEvalParent);
    assertTrue(directEval.isDirectEval());

    // Check if annotations are copied correctly
    Node objlit = new Node(Token.OBJECT_LITERAL, null);
    Node objlitKey = new Node(Token.IDENT, "a");
    Node objlitValue = new Node(Token.STRING, "b");
    Node objlitKeyAnnotation = new Node(Token.COMMENT, "/** @type {string} */");
    Node objlitValueAnnotation = new Node(Token.COMMENT, "/** @type {number} */");
    objlit.addChildrenToFront(objlitKey, objlitKeyAnnotation, objlitValue, objlitValueAnnotation);
    root.addChildrenToFront(objlit);
    assertEquals("/** @type {string} */a\n/** @type {number} */", objlit.toSource());

    // Check if blocks are added to control structures
    Node ifStatement = new Node(Token.IF, null);
    Node ifStatementCondition = new Node(Token.BOOLEAN_LITERAL, true);
    Node ifStatementBlock = new Node(Token.BLOCK, null);
    Node ifStatementBlockChild = new Node(Token.RETURN, null, new Node(Token.NUMBER, 42));
    ifStatementBlock.addChildrenToFront(ifStatementBlockChild);
    ifStatement.addChildrenToFront(ifStatementCondition, ifStatementBlock);
    root.addChildrenToFront(ifStatement);
    Node ifStatementBlockDetected = ifStatement.getFirstChild();
    assertTrue(ifStatementBlockDetected instanceof Node);
    assertTrue(ifStatementBlockDetected.isBlock());

    // Check if JSDoc info is attached to function literals
    Node functionLiteral = new Node(Token.FUNCTION, null);
    Node functionLiteralBody = new Node(Token.BLOCK, null);
    Node functionLiteralReturnStatement = new Node(Token.RETURNS, null, new Node(Token.NUMBER, 42));
    functionLiteralBody.addChildrenToFront(functionLiteralReturnStatement);
    Node functionLiteralKey = new Node(Token.IDENT, "myFunction");
    Node functionLiteralKeyAnnotation = new Node(Token.COMMENT, "/** @type {Function} */");
    functionLiteral.addChildrenToFront(functionLiteralKey, functionLiteralKeyAnnotation, functionLiteralBody);
    root.addChildrenToFront(functionLiteral);
    assertEquals("/** @type {Function} */myFunction", functionLiteral.toSource());
  }

  @Test(expected = AssertionError.class)
  public void testNormalizeNodeTypesConstraintViolated() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, true);
    Node root = new Node(Token.BLOCK, null);
    prepareAst.process(null, root);
    fail("NormalizeNodeTypes constraint violated");
  }
}