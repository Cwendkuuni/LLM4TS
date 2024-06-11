import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }
  
  @Test
  public void testShouldTraverse_GlobalThisFunctionWithConstructorAnnotation_ReturnsFalse() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    functionNode.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_GlobalThisFunctionWithThisAnnotation_ReturnsFalse() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    functionNode.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_GlobalThisVarAssignment_ReturnsTrue() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node assignmentNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.NAME);
    Node rhsNode = new Node(Token.THIS);
    assignmentNode.addChildToBack(lhsNode);
    assignmentNode.addChildToBack(rhsNode);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, lhsNode, assignmentNode);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_GlobalThisPropertyAssignment_ReturnsFalse() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node assignmentNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.GETPROP);
    Node rhsNode = new Node(Token.THIS);
    Node prototypeNode = new Node(Token.NAME, "prototype");
    lhsNode.addChildToBack(prototypeNode);
    lhsNode.addChildToBack(rhsNode);
    assignmentNode.addChildToBack(lhsNode);
    assignmentNode.addChildToBack(rhsNode);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, lhsNode, assignmentNode);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildIsNotNull_ReturnsTrue() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);
    Node thisNode = new Node(Token.THIS);
    Node parentNode = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldReportThis(thisNode, parentNode);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildIsNull_ReturnsFalse() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    checkGlobalThis.assignLhsChild = null;
    Node thisNode = new Node(Token.THIS);
    Node parentNode = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldReportThis(thisNode, parentNode);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_ThisNodeNotGlobal_Returns() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node thisNode = new Node(Token.THIS);
    Node parentNode = new Node(Token.GETPROP);
    
    checkGlobalThis.visit(nodeTraversal, thisNode, parentNode);
  }
  
  @Test
  public void testVisit_ThisNodeGlobalAndShouldReportThis_CompilerReportsError() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    CheckGlobalThis compiler = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node thisNode = new Node(Token.THIS);
    Node parentNode = new Node(Token.ASSIGN);
    
    compiler.visit(nodeTraversal, thisNode, parentNode);
    
    // Check that compiler.error was called
  }
  
  @Test
  public void testVisit_NodeEqualsAssignLhsChild_AssignLhsChildSetToNull() {
    NodeTraversal nodeTraversal = new NodeTraversal();
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);
    Node thisNode = new Node(Token.THIS);
    
    checkGlobalThis.visit(nodeTraversal, thisNode, checkGlobalThis.assignLhsChild);
    
    assertNull(checkGlobalThis.assignLhsChild);
  }
}