import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_functionWithConstructorAnnotation_false() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckGlobalThis.CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    node.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_functionWithThisAnnotation_false() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckGlobalThis.CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    node.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_functionWithNoLogicalPlaceForThisAnnotation_false() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckGlobalThis.CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_functionWithNonGlobalContext_false() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckGlobalThis.CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_functionWithAssignmentToPrototype_false() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckGlobalThis.CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    Node lhs = new Node(Token.GETPROP);
    Node prototype = new Node(Token.STRING);
    prototype.setString("prototype");
    lhs.addChildToBack(prototype);
    node.addChildToFront(lhs);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(lhs.cloneTree());
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    Assert.assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_nonFunctionNode_true() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckGlobalThis.CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testVisit_thisNodeWithNoParentNode_reportError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckGlobalThis.CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = null;
    checkGlobalThis.visit(traversal, node, parent);
  }
  
  @Test
  public void testVisit_thisNodeWithParentNodeNotGetProperty_reportError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckGlobalThis.CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.visit(traversal, node, parent);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_functionWithNoJsDocInfo_returnNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckGlobalThis.CheckLevel.WARNING);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    Assert.assertNull(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_functionWithJsDocInfo_returnJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckGlobalThis.CheckLevel.WARNING);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    node.setJSDocInfo(jsDocInfo);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    Assert.assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_functionWithParentNameNodeAndNoParentJsDocInfo_returnJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckGlobalThis.CheckLevel.WARNING);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(null);
    parent.addChildToFront(node);
    parent.addChildToBack(new Node(Token.NULL));
    jsDocInfo.setConstructor(true);
    node.setJSDocInfo(jsDocInfo);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    Assert.assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_functionWithGrandparentVarNode_returnJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckGlobalThis.CheckLevel.WARNING);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    Node parent = new Node(Token.NAME);
    parent.addChildToFront(node);
    parent.addChildToBack(new Node(Token.NULL));
    Node grandparent = new Node(Token.VAR);
    grandparent.setJSDocInfo(jsDocInfo);
    grandparent.addChildToFront(parent);
    jsDocInfo.setConstructor(true);
    node.setJSDocInfo(null);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    Assert.assertEquals(jsDocInfo, result);
  }

}