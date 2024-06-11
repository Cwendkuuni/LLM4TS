import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }
  
  @After
  public void tearDown() {
    checkGlobalThis = null;
    compiler = null;
  }
  
  @Test
  public void testShouldTraverse_returnsTrueForFunctionWithNoAnnotations() {
    Node functionNode = new Node(Token.FUNCTION);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, functionNode, null);
    boolean expectedResult = true;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForFunctionWithConstructorAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    functionNode.setJSDocInfo(jsDoc);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, functionNode, null);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForFunctionWithThisTypeAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType("MyObject");
    functionNode.setJSDocInfo(jsDoc);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, functionNode, null);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForFunctionWithOverrideAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    functionNode.setJSDocInfo(jsDoc);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, functionNode, null);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForFunctionInObjectLiteral() {
    Node functionNode = new Node(Token.FUNCTION);
    Node objectLiteralNode = new Node(Token.OBJECTLIT);
    objectLiteralNode.addChildToFront(functionNode);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, functionNode, objectLiteralNode);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForFunctionInObjectLiteralWithPrototypeProperty() {
    Node functionNode = new Node(Token.FUNCTION);
    Node objectLiteralNode = new Node(Token.OBJECTLIT);
    Node prototypePropertyNode = new Node(Token.STRING_KEY, "prototype");
    objectLiteralNode.addChildToFront(prototypePropertyNode);
    prototypePropertyNode.addChildToFront(functionNode);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, functionNode, prototypePropertyNode);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForFunctionInObjectLiteralWithPrototypePropertyAccess() {
    Node functionNode = new Node(Token.FUNCTION);
    Node objectLiteralNode = new Node(Token.OBJECTLIT);
    Node prototypePropertyAccessNode = new Node(Token.GETPROP);
    Node prototypeNode = new Node(Token.STRING, "prototype");
    prototypePropertyAccessNode.addChildToFront(prototypeNode);
    objectLiteralNode.addChildToFront(prototypePropertyAccessNode);
    prototypePropertyAccessNode.addChildToBack(functionNode);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, functionNode, prototypePropertyAccessNode);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForFunctionInObjectLiteralWithLendsName() {
    Node functionNode = new Node(Token.FUNCTION);
    Node objectLiteralNode = new Node(Token.OBJECTLIT);
    Node lendsNameNode = new Node(Token.BLOCK, "lendsName");
    Node lendsPrototypePropertyNode = new Node(Token.STRING_KEY, "prototype");
    lendsNameNode.addChildToBack(lendsPrototypePropertyNode);
    objectLiteralNode.addChildToFront(lendsNameNode);
    lendsPrototypePropertyNode.addChildToFront(functionNode);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, functionNode, lendsPrototypePropertyNode);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForRightSideOfAssignmentToPrototypeProperty() {
    Node assignmentNode = new Node(Token.ASSIGN);
    Node prototypePropertyNode = new Node(Token.GETPROP);
    Node prototypeNode = new Node(Token.STRING, "prototype");
    prototypePropertyNode.addChildToFront(prototypeNode);
    assignmentNode.addChildToBack(prototypePropertyNode);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, prototypePropertyNode, assignmentNode);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForRightSideOfAssignmentToPrototypePropertyAccess() {
    Node assignmentNode = new Node(Token.ASSIGN);
    Node prototypePropertyAccessNode = new Node(Token.GETPROP);
    Node prototypeNode = new Node(Token.STRING, "prototype");
    prototypePropertyAccessNode.addChildToFront(prototypeNode);
    assignmentNode.addChildToBack(prototypePropertyAccessNode);
    boolean actualResult = checkGlobalThis.shouldTraverse(null, prototypePropertyAccessNode, assignmentNode);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testVisit_reportsWarningForThisKeyword() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node thisNode = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    t.reportError(thisNode, "message");
    boolean actualResult = compiler.getErrorCount() == 1;
    boolean expectedResult = true;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testVisit_resetsAssignLhsChildToNullForThisNode() {
    checkGlobalThis.assignLhsChild = new Node(Token.THIS);
    Node thisNode = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    t.reportError(thisNode, "message");
    boolean actualResult = checkGlobalThis.assignLhsChild == null;
    boolean expectedResult = true;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldReportThis_returnsTrueForThisNodeOnLeftSideOfAssignment() {
    Node assignmentNode = new Node(Token.ASSIGN);
    Node thisNode = new Node(Token.THIS);
    assignmentNode.addChildToFront(thisNode);
    checkGlobalThis.assignLhsChild = thisNode;
    boolean actualResult = checkGlobalThis.shouldReportThis(thisNode);
    boolean expectedResult = true;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldReportThis_returnsTrueForThisNodeWithPropertyAccess() {
    Node propertyAccessNode = new Node(Token.GETPROP);
    Node thisNode = new Node(Token.THIS);
    propertyAccessNode.addChildToFront(thisNode);
    boolean actualResult = checkGlobalThis.shouldReportThis(thisNode);
    boolean expectedResult = true;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testShouldReportThis_returnsFalseForThisNodeWithoutPropertyAccess() {
    Node thisNode = new Node(Token.THIS);
    boolean actualResult = checkGlobalThis.shouldReportThis(thisNode);
    boolean expectedResult = false;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_returnsNullWhenNoJsDocInfoAvailable() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo actualResult = checkGlobalThis.getFunctionJsDocInfo(functionNode);
    JSDocInfo expectedResult = null;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_returnsJsDocInfoFromFunctionNode() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    functionNode.setJSDocInfo(jsDoc);
    JSDocInfo actualResult = checkGlobalThis.getFunctionJsDocInfo(functionNode);
    JSDocInfo expectedResult = jsDoc;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_returnsJsDocInfoFromParentNodeWithAssignType() {
    Node functionNode = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    assignNode.setJSDocInfo(jsDoc);
    assignNode.addChildToBack(functionNode);
    JSDocInfo actualResult = checkGlobalThis.getFunctionJsDocInfo(functionNode);
    JSDocInfo expectedResult = jsDoc;
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_returnsJsDocInfoFromParentNodeWithNameType() {
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    nameNode.setJSDocInfo(jsDoc);
    nameNode.addChildToBack(functionNode);
    JSDocInfo actualResult = checkGlobalThis.getFunctionJsDocInfo(functionNode);
    JSDocInfo expectedResult = jsDoc;
    assertEquals(expectedResult, actualResult);
  }

}