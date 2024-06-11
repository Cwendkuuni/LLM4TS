import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  public CheckGlobalThisTest() {
    this.checkGlobalThis = new CheckGlobalThis(new DefaultCompiler());
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForGlobalContext() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForBlockContext() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.BLOCK);
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForScriptContext() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.SCRIPT);
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForNameContext() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForAssignContext() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.ASSIGN);
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForObjectLiteralKeysContext() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.STRING_KEY);
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForConstructorFunctionWithJsDoc() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForInterfaceFunctionWithJsDoc() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setInterface(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionWithThisTypeJsDoc() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionWithOverrideJsDoc() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForPrototypePropertyAssignment() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.ASSIGN);
    Node parent = new Node();
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.THIS));
    lhs.addChildToBack(new Node(Token.STRING, "prototype"));
    n.addChildToBack(lhs);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void visit_shouldNotReportThis() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    checkGlobalThis.visit(t, n, parent);
    // TODO: Write appropriate assertions
  }

  @Test
  public void visit_shouldReportThis() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.visit(t, n, parent);
    // TODO: Write appropriate assertions
  }

  @Test
  public void visit_shouldResetAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.visit(t, n, parent);
    // TODO: Write appropriate assertions
  }

  @Test
  public void shouldReportThis_shouldReturnTrueForAssignLhsChildNotNull() {
    Node n = new Node();
    checkGlobalThis.assignLhsChild = n;
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }

  @Test
  public void shouldReportThis_shouldReturnTrueForGetParentIsGetTrue() {
    Node n = new Node();
    Node parent = new Node();
    parent.setIsGet(true);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }

  @Test
  public void shouldReportThis_shouldReturnFalseForGetParentIsGetFalse() {
    Node n = new Node();
    Node parent = new Node();
    parent.setIsGet(false);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertFalse(result);
  }

  @Test
  public void getFunctionJsDocInfo_shouldReturnNullForFunctionWithJsDoc() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(result);
  }

  @Test
  public void getFunctionJsDocInfo_shouldReturnParentJsDocForNameWithJsDoc() {
    Node n = new Node(Token.NAME);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

  @Test
  public void getFunctionJsDocInfo_shouldReturnParentParentJsDocForNameWithVarJsDoc() {
    Node n = new Node(Token.NAME);
    Node parent = new Node();
    parent.setType(Token.VAR);
    Node grandParent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    grandParent.setJSDocInfo(jsDoc);
    parent.setParent(grandParent);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

  @Test
  public void getFunctionJsDocInfo_shouldReturnParentJsDocForAssignWithNameWithJsDoc() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

  @Test
  public void getFunctionJsDocInfo_shouldReturnParentParentJsDocForAssignWithNameWithVarJsDoc() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    Node grandParent = new Node();
    grandParent.setType(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    grandParent.setJSDocInfo(jsDoc);
    parent.setParent(grandParent);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }
}