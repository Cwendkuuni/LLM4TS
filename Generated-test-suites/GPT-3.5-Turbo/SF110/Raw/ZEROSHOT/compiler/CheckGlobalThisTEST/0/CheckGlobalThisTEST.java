import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.SCRIPT)));
  }
  
  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.SCRIPT)));
  }
  
  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.SCRIPT)));
  }
  
  @Test
  public void testShouldTraverse_FunctionInNonGlobalContext() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.BLOCK)));
  }
  
  @Test
  public void testShouldTraverse_FunctionInObjectLiteralKey() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.STRING_KEY)));
  }
  
  @Test
  public void testShouldTraverse_FunctionInObjectLiteralKeyWithPrototypeAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.NAME)));
  }
  
  @Test
  public void testShouldTraverse_FunctionInObjectLiteralKeyWithNestedPrototypeAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.GETPROP)));
  }
  
  @Test
  public void testShouldTraverse_FunctionInAssignmentLhs() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertTrue(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.ASSIGN)));
  }
  
  @Test
  public void testShouldTraverse_FunctionInAssignmentRhs() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertFalse(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.GETPROP)));
  }
  
  @Test
  public void testVisit_ThisNodeWithAssignmentLhs() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS)));
  }
  
  @Test
  public void testVisit_ThisNodeWithGetPropParent() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS)));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    functionNode.setJSDocInfo(jsDocInfo);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc_AssignParentWithJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node assignNode = new Node(Token.ASSIGN);
    Node functionNode = new Node(Token.FUNCTION);
    assignNode.addChildToFront(functionNode);
    JSDocInfo jsDocInfo = new JSDocInfo();
    assignNode.setJSDocInfo(jsDocInfo);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc_NameParentWithJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node nameNode = new Node(Token.NAME);
    Node functionNode = new Node(Token.FUNCTION);
    nameNode.addChildToFront(functionNode);
    JSDocInfo jsDocInfo = new JSDocInfo();
    nameNode.setJSDocInfo(jsDocInfo);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc_NameParentWithVarParentWithJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node varNode = new Node(Token.VAR);
    Node nameNode = new Node(Token.NAME);
    Node functionNode = new Node(Token.FUNCTION);
    nameNode.addChildToFront(functionNode);
    varNode.addChildToFront(nameNode);
    JSDocInfo jsDocInfo = new JSDocInfo();
    varNode.setJSDocInfo(jsDocInfo);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node functionNode = new Node(Token.FUNCTION);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
}