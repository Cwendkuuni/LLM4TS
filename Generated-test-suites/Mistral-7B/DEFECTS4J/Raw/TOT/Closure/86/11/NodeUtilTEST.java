import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  Node node, parentNode;

  @org.junit.Before
  public void setUp() {
    // Set up test nodes
    node = new Node();
    node.setType(Token.FOR);
    node.setChildCount(3);

    parentNode = new Node();
    parentNode.setType(Token.SCRIPT);
  }

  @org.junit.Test
  public void testIsForIn() {
    assertTrue(isForIn(node));
  }

  @org.junit.Test
  public void testIsForInWithInvalidNodeType() {
    Node invalidNode = new Node();
    invalidNode.setType(Token.IF);
    assertFalse(isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsForInWithInvalidChildCount() {
    Node invalidNode = new Node();
    invalidNode.setType(Token.FOR);
    invalidNode.setChildCount(2);
    assertFalse(isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    assertTrue(isStatement(node));
  }

  @org.junit.Test
  public void testIsStatementWithNullParentNode() {
    assertFalse(isStatement(node));
  }

  @org.junit.Test
  public void testIsStatementWithInvalidParentType() {
    parentNode.setType(Token.VAR);
    Node invalidNode = new Node();
    invalidNode.setType(Token.IF);
    assertFalse(isStatement(invalidNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);
    functionNode.setChildCount(1);
    Node nameNode = new Node();
    nameNode.setType(Token.IDENTIFIER);
    nameNode.setNodeValue("myFunction");
    functionNode.setFirstChild(nameNode);
    functionNode.addChild(new Node()); // function body

    assertTrue(isFunctionDeclaration(functionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationWithInvalidType() {
    Node invalidNode = new Node();
    invalidNode.setType(Token.OBJECT);
    assertFalse(isFunctionDeclaration(invalidNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationWithNoName() {
    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);
    functionNode.setChildCount(1);
    functionNode.addChild(new Node()); // function body

    assertFalse(isFunctionDeclaration(functionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node();
    scriptNode.setType(Token.SCRIPT);
    scriptNode.addChild(node);

    Node functionDeclaration = new Node();
    functionDeclaration.setType(Token.FUNCTION);
    functionDeclaration.setChildCount(1);
    Node nameNode = new Node();
    nameNode.setType(Token.IDENTIFIER);
    nameNode.setNodeValue("myFunction");
    functionDeclaration.setFirstChild(nameNode);
    functionDeclaration.addChild(new Node()); // function body

    scriptNode.addChild(functionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(functionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationWithNoParentScript() {
    Node functionDeclaration = new Node();
    functionDeclaration.setType(Token.FUNCTION);
    functionDeclaration.setChildCount(1);
    Node nameNode = new Node();
    nameNode.setType(Token.IDENTIFIER);
    nameNode.setNodeValue("myFunction");
    functionDeclaration.setFirstChild(nameNode);
    functionDeclaration.addChild(new Node()); // function body

    assertFalse(isHoistedFunctionDeclaration(functionDeclaration));
  }
}