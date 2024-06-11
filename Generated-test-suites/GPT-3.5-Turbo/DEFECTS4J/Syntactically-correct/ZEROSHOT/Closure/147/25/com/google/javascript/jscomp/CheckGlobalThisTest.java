import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionConstructor() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionThisAnnotation() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNoLogicalPlaceForThisAnnotation_BlockParent() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNoLogicalPlaceForThisAnnotation_ScriptParent() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.SCRIPT);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNoLogicalPlaceForThisAnnotation_NameParent() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.NAME);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNoLogicalPlaceForThisAnnotation_AssignParent() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.ASSIGN);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_PrototypeAssignment() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.STRING);
    Node parent = new Node();
    parent.setType(Token.GETPROP);
    parent.addChildToFront(n);
    Node grandparent = new Node();
    grandparent.setType(Token.ASSIGN);
    grandparent.addChildToFront(parent);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, grandparent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_PrototypeAssignment_Subproperty() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.STRING);
    Node parent = new Node();
    parent.setType(Token.GETPROP);
    parent.addChildToFront(n);
    Node llhs = new Node();
    llhs.setType(Token.GETPROP);
    llhs.addChildToFront(parent);
    Node grandparent = new Node();
    grandparent.setType(Token.ASSIGN);
    grandparent.addChildToFront(llhs);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(t, n, grandparent);
    assertFalse(result);
  }

  @Test
  public void testVisit_GlobalThis() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.THIS);
    Node parent = new Node();
    parent.setType(Token.NAME);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.visit(t, n, parent);
  }

  @Test
  public void testVisit_AssignLhsChildNotNull() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.STRING);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.visit(t, n, null);
  }

  @Test
  public void testVisit_AssignLhsChildNull() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.setType(Token.STRING);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.visit(t, n, n);
  }

  @Test
  public void testGetFunctionJsDocInfo_WithJSDoc() {
    Node n = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.getFunctionJsDocInfo(n);
  }

  @Test
  public void testGetFunctionJsDocInfo_WithoutJSDoc_NameParent() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.NAME);
    n.setParent(parent);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.getFunctionJsDocInfo(n);
  }

  @Test
  public void testGetFunctionJsDocInfo_WithoutJSDoc_AssignParent() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.ASSIGN);
    n.setParent(parent);
    Node gramps = new Node();
    gramps.setType(Token.VAR);
    parent.setParent(gramps);
    JSDocInfo jsDoc = new JSDocInfo();
    gramps.setJSDocInfo(jsDoc);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.getFunctionJsDocInfo(n);
  }
}