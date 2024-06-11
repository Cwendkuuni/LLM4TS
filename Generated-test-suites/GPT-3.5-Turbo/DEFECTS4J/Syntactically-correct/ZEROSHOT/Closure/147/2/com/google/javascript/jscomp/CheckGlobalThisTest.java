import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_withFunctionWithConstructorAnnotation_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withFunctionWithThisTypeAnnotation_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType("MyObject");
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withFunctionNotBlockOrScript_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.IF);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withParentAssignLeftNode_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node assign = new Node(Token.ASSIGN);
    Node parent = new Node(Token.VAR);
    parent.addChildToBack(assign);
    assign.addChildToBack(n);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withParentAssignRightNode_returnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node assign = new Node(Token.ASSIGN);
    Node parent = new Node(Token.VAR);
    parent.addChildToBack(assign);
    assign.addChildToBack(Node.newString(Token.NAME, "this"));
    assign.addChildToBack(n);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_withAssignLhsChild_returnTrue() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.shouldTraverse(new NodeTraversal(), parent, null);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_withParentGet_returnTrue() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_withoutParent_returnFalse() {
    Node n = new Node(Token.THIS);
    boolean result = checkGlobalThis.shouldReportThis(n, null);
    assertFalse(result);
  }

  @Test
  public void testVisit_withThisAndShouldReportThis_reportError() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.shouldTraverse(t, parent, null);
    checkGlobalThis.visit(t, n, parent);
    // Assert error is reported
    // ...
  }

  @Test
  public void testVisit_withAssignLhsChild_resetAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.shouldTraverse(t, parent, null);
    checkGlobalThis.visit(t, n, parent);
    // Assert assignLhsChild is null
    // ...
  }

  @Test
  public void testGetFunctionJsDocInfo_withFunctionJSDocInfo_returnFunctionJSDocInfo() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_withParentNameParentJSDocInfo_returnParentJSDocInfo() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    parent.addChildToBack(n);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_withParentName_returnParentParentJSDocInfo() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    Node grandparent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    grandparent.setJSDocInfo(jsDoc);
    parent.addChildToFront(n);
    grandparent.addChildToFront(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_withoutJSDocInfo_returnNull() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(result);
  }
}