
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    // Typical use case: for-in loop
    Node forInLoop = new Node(Token.FOR);
    Node varNode = new Node(Token.VAR);
    Node nameNode = new Node(Token.NAME);
    forInLoop.addChildToBack(varNode);
    forInLoop.addChildToBack(nameNode);
    forInLoop.addChildToBack(new Node(Token.TRUE));
    
    boolean result = NodeUtil.isForIn(forInLoop);
    
    assertTrue(result);
    
    // Edge case: Not a for-in loop
    Node otherLoop = new Node(Token.FOR);
    otherLoop.addChildToBack(new Node(Token.NAME));
    otherLoop.addChildToBack(new Node(Token.NULL));
    
    result = NodeUtil.isForIn(otherLoop);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement() {
    // Typical use case: An if statement
    Node ifStatement = new Node(Token.IF);
    
    boolean result = NodeUtil.isStatement(ifStatement);
    
    assertTrue(result);
    
    // Edge case: A function expression
    Node functionExpr = new Node(Token.FUNCTION);
    result = NodeUtil.isStatement(functionExpr);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case: A function declaration in the root scope
    Node functionDecl = new Node(Token.FUNCTION);
    
    boolean result = NodeUtil.isFunctionDeclaration(functionDecl);
    
    assertTrue(result);
    
    // Edge case: A function expression
    Node functionExpr = new Node(Token.FUNCTION);
    Node varNode = new Node(Token.VAR);
    varNode.addChildToFront(new Node(Token.NAME));
    varNode.addChildToFront(functionExpr);
    
    result = NodeUtil.isFunctionDeclaration(varNode);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case: A hoisted function declaration in the script
    Node functionDecl = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    functionDecl.setParent(scriptNode);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(functionDecl);
    
    assertTrue(result);
    
    // Edge case: A function declaration in a nested function
    Node functionDecl2 = new Node(Token.FUNCTION);
    Node parentFunction = new Node(Token.FUNCTION);
    parentFunction.addChildToBack(functionDecl2);
    
    result = NodeUtil.isHoistedFunctionDeclaration(functionDecl2);
    
    assertFalse(result);
  }
  
}
