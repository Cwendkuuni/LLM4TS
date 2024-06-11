import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(null, null);
  }
  
  @Test
  public void testShouldTraverse_withFunctionConstructor_noAnnotation_shouldReturnFalse() {
    assertFalse(checkGlobalThis.shouldTraverse(null, createFunctionNode(true, false, false), null));
  }
  
  @Test
  public void testShouldTraverse_withFunctionConstructor_withThisAnnotation_shouldReturnFalse() {
    assertFalse(checkGlobalThis.shouldTraverse(null, createFunctionNode(true, true, false), null));
  }
  
  @Test
  public void testShouldTraverse_withFunctionConstructor_withOverrideAnnotation_shouldReturnFalse() {
    assertFalse(checkGlobalThis.shouldTraverse(null, createFunctionNode(true, false, true), null));
  }
  
  @Test
  public void testShouldTraverse_withFunction_noAnnotation_noLogicalPlace_shouldReturnFalse() {
    assertFalse(checkGlobalThis.shouldTraverse(null, createFunctionNode(false, false, false), createParentNode(Token.BLOCK)));
  }
  
  @Test
  public void testShouldTraverse_withFunction_noAnnotation_withBlockParent_shouldReturnTrue() {
    assertTrue(checkGlobalThis.shouldTraverse(null, createFunctionNode(false, false, false), createParentNode(Token.BLOCK)));
  }
  
  @Test
  public void testShouldTraverse_withFunction_noAnnotation_withScriptParent_shouldReturnTrue() {
    assertTrue(checkGlobalThis.shouldTraverse(null, createFunctionNode(false, false, false), createParentNode(Token.SCRIPT)));
  }
  
  @Test
  public void testShouldTraverse_withFunction_noAnnotation_withNameParent_shouldReturnTrue() {
    assertTrue(checkGlobalThis.shouldTraverse(null, createFunctionNode(false, false, false), createParentNode(Token.NAME)));
  }
  
  @Test
  public void testShouldTraverse_withFunction_noAnnotation_withAssignParent_shouldReturnTrue() {
    assertTrue(checkGlobalThis.shouldTraverse(null, createFunctionNode(false, false, false), createParentNode(Token.ASSIGN)));
  }
  
  @Test
  public void testShouldTraverse_withParentNotNull_withAssignment_shouldReturnFalse() {
    assertFalse(checkGlobalThis.shouldTraverse(null, createNode(Token.THIS), createParentNodeWithAssignment()));
  }
  
  @Test
  public void testShouldTraverse_withParentNotNull_withPropertyAccess_shouldReturnTrue() {
    assertTrue(checkGlobalThis.shouldTraverse(null, createNode(Token.THIS), createParentNodeWithPropertyAccess()));
  }
  
  @Test
  public void testShouldReportThis_withAssignableLhsChild_shouldReturnTrue() {
    assertTrue(checkGlobalThis.shouldReportThis(createNode(Token.THIS), null));
  }
  
  @Test
  public void testShouldReportThis_withNonAssignableLhsChild_shouldReturnFalse() {
    assertFalse(checkGlobalThis.shouldReportThis(createNode(Token.THIS), createParentNode(Token.GETPROP)));
  }
  
  private Node createFunctionNode(boolean isConstructor, boolean hasThisType, boolean isOverride) {
    Node functionNode = new Node(Token.FUNCTION);
    if (isConstructor) {
      JSDocInfo jsDocInfo = new JSDocInfo();
      jsDocInfo.setConstructor(isConstructor);
      functionNode.setJSDocInfo(jsDocInfo);
    }
    if (hasThisType) {
      JSDocInfo jsDocInfo = new JSDocInfo();
      jsDocInfo.recordThisType();
      functionNode.setJSDocInfo(jsDocInfo);
    }
    if (isOverride) {
      JSDocInfo jsDocInfo = new JSDocInfo();
      jsDocInfo.setOverride(true);
      functionNode.setJSDocInfo(jsDocInfo);
    }
    return functionNode;
  }
  
  private Node createParentNode(int type) {
    return new Node(type);
  }
  
  private Node createNode(int type) {
    return new Node(type);
  }
  
  private Node createParentNodeWithAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.THIS);
    Node rhsNode = new Node(Token.NAME);
    assignNode.addChildToBack(lhsNode);
    assignNode.addChildToBack(rhsNode);
    return assignNode;
  }
  
  private Node createParentNodeWithPropertyAccess() {
    Node getpropNode = new Node(Token.GETPROP);
    Node lhsNode = new Node(Token.THIS);
    Node rhsNode = new Node(Token.NAME);
    getpropNode.addChildToBack(lhsNode);
    getpropNode.addChildToBack(rhsNode);
    return getpropNode;
  }
}