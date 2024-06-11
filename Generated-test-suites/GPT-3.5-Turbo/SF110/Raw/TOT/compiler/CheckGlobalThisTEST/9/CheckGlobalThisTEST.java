import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    functionNode.setJSDocInfo(jsDoc);

    boolean shouldTraverse = traversal.shouldTraverse(null, functionNode, null);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    functionNode.setJSDocInfo(jsDoc);

    boolean shouldTraverse = traversal.shouldTraverse(null, functionNode, null);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    functionNode.setJSDocInfo(jsDoc);

    boolean shouldTraverse = traversal.shouldTraverse(null, functionNode, null);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_FunctionWithFunctionName_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node functionNameNode = new Node(Token.NAME, "functionName");
    functionNode.addChildToFront(functionNameNode);

    boolean shouldTraverse = traversal.shouldTraverse(null, functionNode, null);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_FunctionNotInBlockOrScript_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.AND);
    parent.addChildToFront(functionNode);

    boolean shouldTraverse = traversal.shouldTraverse(null, functionNode, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_FunctionInVarAssignment_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.VAR);
    parent.addChildToFront(functionNode);

    boolean shouldTraverse = traversal.shouldTraverse(null, functionNode, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_FunctionInObjLitKeyAssignment_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING_KEY);
    parent.addChildToFront(functionNode);

    boolean shouldTraverse = traversal.shouldTraverse(null, functionNode, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testShouldTraverse_FunctionInObjectLiteralProtoAssignment_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    Node grandparent = new Node(Token.OBJECTLIT);
    grandparent.setJSDocInfo(new JSDocInfo());
    grandparent.getJSDocInfo().recordLendsName("parent.prototype");
    parent.addChildToFront(new Node(Token.THIS));
    parent.addChildToBack(new Node(Token.STRING, "prototype"));
    parent.addChildToBack(functionNode);

    boolean shouldTraverse = traversal.shouldTraverse(null, functionNode, parent);

    Assert.assertFalse(shouldTraverse);
  }

  @Test
  public void testVisit_GlobalThisEncountered_CompilerReportsError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(functionNode);

    checkGlobalThis.visit(traversal, functionNode, parent);

    DiagnosticType reportedError = compiler.getReportedErrors().get(0).getType();

    Assert.assertEquals(CheckGlobalThis.GLOBAL_THIS, reportedError);
  }

  @Test
  public void testVisit_NotGlobalThisEncountered_CompilerDoesNotReportError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal.Callback callback = checkGlobalThis;
    NodeTraversal traversal = new NodeTraversal(callback);

    Node functionNode = new Node(Token.STRING, "someString");
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(functionNode);

    checkGlobalThis.visit(traversal, functionNode, parent);

    Assert.assertTrue(compiler.getReportedErrors().isEmpty());
  }

  @Test
  public void testShouldReportThis_LeftSideOfAssign_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node leftSideNode = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = leftSideNode;

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(leftSideNode);

    Assert.assertTrue(shouldReportThis);
  }

  @Test
  public void testShouldReportThis_PropertyAccess_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node functionNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(functionNode);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(functionNode);

    Assert.assertTrue(shouldReportThis);
  }

  @Test
  public void testShouldReportThis_NotLeftSideOfAssignOrPropertyAccess_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    Node functionNode = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);
    parent.addChildToFront(functionNode);

    boolean shouldReportThis = checkGlobalThis.shouldReportThis(functionNode);

    Assert.assertFalse(shouldReportThis);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJSDoc_ReturnsJSDocInfo() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    functionNode.setJSDocInfo(jsDoc);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    Assert.assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ChildOfAssignmentWithJSDoc_ReturnsJSDocInfo() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    Node assignmentNode = new Node(Token.ASSIGN, functionNode);
    assignmentNode.setJSDocInfo(jsDoc);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    Assert.assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_VarWithJSDoc_ReturnsJSDocInfo() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    Node nameNode = new Node(Token.NAME, "variable");
    Node varNode = new Node(Token.VAR, nameNode);
    varNode.setJSDocInfo(jsDoc);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    Assert.assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_VarWithAssignmentAndJSDoc_ReturnsJSDocInfo() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    Node nameNode = new Node(Token.NAME, "variable");
    Node assignmentNode = new Node(Token.ASSIGN, nameNode, functionNode);
    Node varNode = new Node(Token.VAR, assignmentNode);
    varNode.setJSDocInfo(jsDoc);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    Assert.assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_NotFound_ReturnsNull() {
    Node functionNode = new Node(Token.FUNCTION);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    Assert.assertNull(result);
  }
}