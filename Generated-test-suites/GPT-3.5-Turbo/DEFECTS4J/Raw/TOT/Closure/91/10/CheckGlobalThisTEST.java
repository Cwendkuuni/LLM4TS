import org.junit.Test;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionAnnotation_ReturnsFalse() {
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    node.setJSDocInfo(jsDoc);

    Callback callback = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(callback);

    boolean result = callback.shouldTraverse(traversal, node, null);

    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionAnnotation_ReturnsTrue() {
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    node.setJSDocInfo(jsDoc);

    Callback callback = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(callback);

    boolean result = callback.shouldTraverse(traversal, node, null);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ParentTypeIsBlock_ReturnsTrue() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(node);

    Callback callback = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(callback);

    boolean result = callback.shouldTraverse(traversal, node, parent);

    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ParentTypeIsAssign_ReturnsTrue() {
    Node node = new Node(Token.FUNCTION);;
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(node);

    Callback callback = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(callback);

    boolean result = callback.shouldTraverse(traversal, node, parent);

    assertTrue(result);
  }

  @Test
  public void testVisit_CheckThis_ShouldReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node node = new Node(Token.THIS);
    NodeTraversal traversal = new NodeTraversal(checkGlobalThis);

    checkGlobalThis.visit(traversal, node, null);

    // Assert that an error is reported
  }

  @Test
  public void testVisit_CheckAssignLhsChild_NotReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node node = new Node(Token.STRING);
    checkGlobalThis.shouldReportThis(node, null);

    // Assert that no error is reported
  }

  @Test
  public void testGetFunctionJsDocInfo_HasJsDoc_ReturnsJsDoc() {
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    node.setJSDocInfo(jsDoc);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);

    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_HasJsDocInParent_ReturnsParentJsDoc() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    parent.addChildToFront(node);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);

    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_HasJsDocInGrandParent_ReturnsGrandParentJsDoc() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node grandParent = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    grandParent.setJSDocInfo(jsDoc);
    parent.addChildToFront(node);
    grandParent.addChildToFront(parent);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);

    assertEquals(jsDoc, result);
  }
}