import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_globalContext_returnTrue(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_notGlobalContext_returnFalse(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node(Node.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_nonFunction_returnTrue(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node(Node.BLOCK);
    Node grandParent = new Node(Node.SCRIPT);
    parent.setParent(grandParent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_jsDocConstructor_returnFalse(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_jsDocThis_returnFalse(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(new Node());
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_jsDocOverride_returnFalse(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_parentIsVar_returnTrue(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    Node grandParent = new Node(Node.VAR);
    parent.setParent(grandParent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_objectLiteralKeys_returnTrue(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    Node grandParent = new Node();
    grandParent.setJSDocInfo(new JSDocInfo());
    parent.setParent(grandParent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(true, result);
  }

  @Test
  public void testVisit_thisOnLeftSideOfAssign_reportGlobalThis(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Node.THIS);
    Node parent = new Node(Node.ASSIGN);
    n.setParent(parent);
    checkGlobalThis.assignLhsChild = n;
    checkGlobalThis.visit(t, n, parent);
    assertEquals(DiagnosticType.JSC_USED_GLOBAL_THIS, compiler.getLastReportedError().getType());
  }

  @Test
  public void testVisit_thisWithPropertyAccess_reportGlobalThis(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Node.THIS);
    Node parent = new Node(Node.GETPROP);
    n.setParent(parent);
    checkGlobalThis.visit(t, n, parent);
    assertEquals(DiagnosticType.JSC_USED_GLOBAL_THIS, compiler.getLastReportedError().getType());
  }

  @Test
  public void testShouldReportThis_thisWithPropertyAccess_returnTrue(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = new Node(Node.THIS);
    Node parent = new Node(Node.GETPROP);
    n.setParent(parent);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertEquals(true, result);
  }

  @Test
  public void testShouldReportThis_thisOnLeftSideOfAssign_returnTrue(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = new Node(Node.THIS);
    Node parent = new Node(Node.ASSIGN);
    n.setParent(parent);
    checkGlobalThis.assignLhsChild = n;
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertEquals(true, result);
  }

  @Test
  public void testShouldReportThis_notGlobalThis_returnFalse(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = new Node(Node.NAME);
    Node parent = new Node(Node.ASSIGN);
    n.setParent(parent);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertEquals(false, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_noJsDoc_returnNull(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = new Node();
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(null, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_jsDocPassedAsParent_returnNotNull(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = new Node();
    Node parent = new Node(Node.NAME);
    parent.setJSDocInfo(new JSDocInfo());
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_jsDocPassedAsVar_returnNotNull(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = new Node();
    Node parent = new Node(Node.VAR);
    parent.setJSDocInfo(new JSDocInfo());
    Node grandParent = new Node();
    parent.setParent(grandParent);
    grandParent.addChildToFront(parent);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_jsDocPassedAsAssign_returnNotNull(){
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node n = new Node();
    Node parent = new Node(Node.ASSIGN);
    parent.setJSDocInfo(new JSDocInfo());
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNotNull(result);
  }
}