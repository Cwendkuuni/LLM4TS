import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(null, null);
  }
  
  @Test
  public void testShouldTraverse_notGlobalFunctionAnnotation_noTraverse() {
    NodeTraversal t = null;
    Node n = createFunctionNode(false, false);
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_notGlobalFunctionAnnotation_withTraverse() {
    NodeTraversal t = null;
    Node n = createFunctionNode(false, false);
    Node parent = createAssignNode();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_constructorFunctionAnnotation_noTraverse() {
    NodeTraversal t = null;
    Node n = createFunctionNode(true, false);
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_constructorFunctionAnnotation_withTraverse() {
    NodeTraversal t = null;
    Node n = createFunctionNode(true, false);
    Node parent = createAssignNode();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_thisFunctionAnnotation_noTraverse() {
    NodeTraversal t = null;
    Node n = createFunctionNode(false, true);
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_thisFunctionAnnotation_withTraverse() {
    NodeTraversal t = null;
    Node n = createFunctionNode(false, true);
    Node parent = createAssignNode();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_notFunctionType_noTraverse() {
    NodeTraversal t = null;
    Node n = createSimpleNode();
    Node parent = createAssignNode();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_notAssignmentNode_traverse() {
    NodeTraversal t = null;
    Node n = createSimpleNode();
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_leftSideOfAssignment_assignLhsChild() {
    NodeTraversal t = null;
    Node n = createSimpleNode();
    Node parent = createAssignNode();
    
    checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertEquals(n, checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testShouldTraverse_rightSideOfAssignmentNotPrototypeProperty_traverse() {
    NodeTraversal t = null;
    Node n = createSimpleNode();
    Node parent = createRightSideOfAssignNode();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  // other test cases for visit() method, shouldReportThis() method, and getFunctionJsDocInfo() method
  
  private Node createFunctionNode(boolean isConstructor, boolean hasThisType) {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfoBuilder jsDocBuilder = new JSDocInfoBuilder();
    if(isConstructor) {
      jsDocBuilder.recordConstructor();
    }
    if(hasThisType) {
      jsDocBuilder.recordThisType();
    }
    JSDocInfo jsDoc = jsDocBuilder.build();
    functionNode.setJSDocInfo(jsDoc);
    return functionNode;
  }
  
  private Node createAssignNode() {
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(createSimpleNode());
    assignNode.addChildToBack(createRightSideOfAssignNode());
    return assignNode;
  }
  
  private Node createSimpleNode() {
    return new Node(Token.NAME);
  }
  
  private Node createRightSideOfAssignNode() {
    Node getPropNode = new Node(Token.GETPROP);
    Node childNode = new Node(Token.NAME, "prototype");
    getPropNode.addChildToBack(childNode);
    return getPropNode;
  }
  
}