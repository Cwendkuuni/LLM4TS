import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }
  
  @Test
  public void testShouldReportThis_withAssignmentToPrototype_shouldReturnFalse() {
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.NAME));
    parent.addChildToFront(new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.STRING, "prototype")));
    node.addChildToFront(parent);
    
    assertFalse(checkGlobalThis.shouldReportThis(node));
  }
  
  @Test
  public void testShouldReportThis_withThisOnLeftSideOfAssign_shouldReturnTrue() {
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.THIS));
    node.addChildToFront(parent);
    
    assertTrue(checkGlobalThis.shouldReportThis(node));
  }
  
  @Test
  public void testShouldReportThis_withThisAsPropertyAccess_shouldReturnTrue() {
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.THIS));
    node.addChildToFront(parent);
    
    assertTrue(checkGlobalThis.shouldReportThis(node));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withoutJsDocInfo_shouldReturnNull() {
    Node node = new Node(Token.FUNCTION);
    
    assertNull(checkGlobalThis.getFunctionJsDocInfo(node));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withJsDocInfoOnFunction_shouldReturnJsDocInfo() {
    Node node = new Node(Token.FUNCTION);
    node.setJSDocInfo(new JSDocInfo());
    
    assertNotNull(checkGlobalThis.getFunctionJsDocInfo(node));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withJsDocInfoOnName_shouldReturnJsDocInfo() {
    Node node = new Node(Token.NAME);
    node.setJSDocInfo(new JSDocInfo());
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(new Node(Token.NAME));
    parent.addChildToFront(node);
    
    assertNotNull(checkGlobalThis.getFunctionJsDocInfo(node));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withJsDocInfoOnVar_shouldReturnJsDocInfo() {
    Node node = new Node(Token.NAME);
    node.setString("x");
    node.setJSDocInfo(new JSDocInfo());
    Node parent = new Node(Token.VAR);
    parent.addChildToBack(node);
    
    assertNotNull(checkGlobalThis.getFunctionJsDocInfo(node));
  }
  
  @Test
  public void testVisit_withThisAndShouldReportThisTrue_shouldReportError() {
    NodeTraversal t = new NodeTraversal();
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.THIS));
    node.addChildToFront(parent);
    
    checkGlobalThis.visit(t, node, null);
    
    assertTrue(t.getErrorReports().size() > 0);
  }
  
  @Test
  public void testVisit_withAssignLhsChildNotNull_shouldSetAssignLhsChildToNull() {
    NodeTraversal t = new NodeTraversal();
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.THIS);
    node.addChildToFront(parent);
    
    assertNull(checkGlobalThis.assignLhsChild);
    
    checkGlobalThis.visit(t, node, null);
    
    assertNull(checkGlobalThis.assignLhsChild);
  }
}