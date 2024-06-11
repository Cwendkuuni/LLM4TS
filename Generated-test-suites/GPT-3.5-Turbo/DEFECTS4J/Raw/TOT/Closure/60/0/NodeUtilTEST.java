
import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalUseCase() {
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToBack(new Node(Token.NAME));
    forInNode.addChildToBack(new Node(Token.NAME));
    forInNode.addChildToBack(new Node(Token.NAME));

    assertTrue(NodeUtil.isForIn(forInNode));
  }

  @Test
  public void testIsForIn_emptyNode() {
    Node emptyNode = new Node(Token.FOR);

    assertFalse(NodeUtil.isForIn(emptyNode));
  }

  @Test
  public void testIsStatement_scriptNode() {
    Node scriptNode = new Node(Token.FUNCTION, new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isStatement(scriptNode));
  }

  @Test
  public void testIsStatement_blockNode() {
    Node blockNode = new Node(Token.FUNCTION, new Node(Token.BLOCK));

    assertTrue(NodeUtil.isStatement(blockNode));
  }

  @Test
  public void testIsStatement_labelNode() {
    Node labelNode = new Node(Token.FUNCTION, new Node(Token.LABEL));

    assertTrue(NodeUtil.isStatement(labelNode));
  }

  @Test
  public void testIsStatement_nonStatements() {
    Node nonStatementNode1 = new Node(Token.FUNCTION, new Node(Token.CONST));
    Node nonStatementNode2 = new Node(Token.FUNCTION, new Node(Token.EXPR_RESULT));

    assertFalse(NodeUtil.isStatement(nonStatementNode1));
    assertFalse(NodeUtil.isStatement(nonStatementNode2));
  }

  @Test
  public void testIsFunctionDeclaration_typicalUseCase() {
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    functionDeclarationNode.addChildToBack(new Node(Token.NAME));
    functionDeclarationNode.addChildToBack(new Node(Token.PARAM_LIST));
    functionDeclarationNode.addChildToBack(new Node(Token.BLOCK));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsFunctionDeclaration_nonFunctionDeclaration() {
    Node nonFunctionDeclarationNode = new Node(Token.NAME);

    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typicalUseCase() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));
    hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.PARAM_LIST));
    hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.BLOCK));

    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(hoistedFunctionDeclarationNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_nonHoistedFunctionDeclaration() {
    Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    nonHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));
    nonHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.PARAM_LIST));
    nonHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.BLOCK));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }
}

