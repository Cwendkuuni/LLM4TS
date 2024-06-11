
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test if a given node represents a FOR-IN loop
    Node forNode = new Node(Token.FOR);
    forNode.addChildToBack(new Node(Token.EMPTY));
    forNode.addChildToBack(new Node(Token.EMPTY));
    forNode.addChildToBack(new Node(Token.EMPTY));
  
    assertTrue(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsNotForIn() {
    // Test if a given node is not a FOR-IN loop
    Node otherNode = new Node(Token.EMPTY);
  
    assertFalse(NodeUtil.isForIn(otherNode));
  }

  @Test
  public void testIsStatement() {
    // Test if a given node is used as a statement
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL);
  
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
  }

  @Test
  public void testIsNotStatement() {
    // Test if a given node is not used as a statement
    Node otherNode = new Node(Token.EMPTY);
  
    assertFalse(NodeUtil.isStatement(otherNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test if a given node is a function declaration
    Node functionNode = new Node(Token.FUNCTION);
  
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    // Test if a given node is not a function declaration
    Node otherNode = new Node(Token.EMPTY);
  
    assertFalse(NodeUtil.isFunctionDeclaration(otherNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test if a given node is a hoisted function declaration
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(functionNode);
  
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    // Test if a given node is not a hoisted function declaration
    Node functionNode = new Node(Token.FUNCTION);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(functionNode);
  
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}