import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical case
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToFront(new Node(Token.VAR));
    forInNode.addChildToBack(new Node(Token.EMPTY));
    forInNode.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(forInNode));

    // Edge case: Empty for-in loop
    Node emptyForInNode = new Node(Token.FOR);
    emptyForInNode.addChildToFront(new Node(Token.VAR));
    emptyForInNode.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(emptyForInNode));

    // Edge case: Missing one child node
    Node invalidForInNode = new Node(Token.FOR);
    invalidForInNode.addChildToFront(new Node(Token.VAR));
    assertFalse(NodeUtil.isForIn(invalidForInNode));

    // Edge case: Missing two child nodes
    Node incompleteForInNode = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(incompleteForInNode));

    // Error scenario: Invalid node type
    Node invalidNodeTypeNode = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(invalidNodeTypeNode));
  }

  @Test
  public void testIsStatement() {
    // Typical case
    Node statementNode = new Node(Token.SCRIPT);
    Node childNode = new Node();
    statementNode.addChildToBack(childNode);
    assertTrue(NodeUtil.isStatement(childNode));

    // Edge case: Empty statement
    Node emptyStatementNode = new Node(Token.SCRIPT);
    assertFalse(NodeUtil.isStatement(emptyStatementNode));

    // Error scenario: Invalid parent node type
    Node invalidParentNode = new Node(Token.VAR);
    assertFalse(NodeUtil.isStatement(invalidParentNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical case
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node parentBlockNode = new Node(Token.BLOCK);
    Node parentScriptNode = new Node(Token.SCRIPT);
    parentScriptNode.addChildToBack(parentBlockNode);
    parentBlockNode.addChildToBack(functionDeclarationNode);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    // Edge case: Empty function declaration
    Node emptyFunctionDeclarationNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(emptyFunctionDeclarationNode));

    // Edge case: Function declaration not part of a statement
    Node functionDeclarationNotInStatementNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(functionDeclarationNotInStatementNode));

    // Error scenario: Invalid node type
    Node invalidNodeTypeNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(invalidNodeTypeNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical case
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    Node functionParentNode = new Node(Token.FUNCTION);
    Node scriptParentNode = new Node(Token.SCRIPT);
    scriptParentNode.addChildToBack(functionParentNode);
    functionParentNode.addChildToBack(hoistedFunctionDeclarationNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    // Edge case: Hoisted function declaration in the script root
    Node hoistedFunctionInScriptNode = new Node(Token.FUNCTION);
    Node scriptRootNode = new Node(Token.SCRIPT);
    scriptRootNode.addChildToBack(hoistedFunctionInScriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionInScriptNode));

    // Edge case: Function declaration not hoisted
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    // Error scenario: Invalid parent node type
    Node invalidParentNode = new Node(Token.IMPORT);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(invalidParentNode));
  }

}