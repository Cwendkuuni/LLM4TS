import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToFront(new Node(Token.BLOCK));
    forInNode.addChildToFront(new Node(Token.VAR));
    assertTrue(NodeUtil.isForIn(forInNode));

    Node nonForInNode = new Node(Token.FOR);
    nonForInNode.addChildToFront(new Node(Token.BLOCK));
    assertFalse(NodeUtil.isForIn(nonForInNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(new Node(Token.EXPR_RESULT));
    scriptNode.addChildToBack(blockNode);
    assertTrue(NodeUtil.isStatement(blockNode));

    Node labelNode = new Node(Token.LABEL);
    labelNode.addChildToBack(new Node(Token.BLOCK));
    assertTrue(NodeUtil.isStatement(labelNode));

    Node nonStatementNode = new Node(Token.EXPR_RESULT);
    assertFalse(NodeUtil.isStatement(nonStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentScriptNode = new Node(Token.SCRIPT);
    parentScriptNode.addChildToBack(functionNode);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node parentBlockNode = new Node(Token.BLOCK);
    parentBlockNode.addChildToBack(functionNode);
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));

    Node nonFunctionNode = new Node(Token.VAR);
    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node parentFunctionNode = new Node(Token.FUNCTION);
    parentFunctionNode.addChildToBack(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node nonHoistedFunctionNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionNode));

    Node nonFunctionNode = new Node(Token.VAR);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonFunctionNode));
  }

}