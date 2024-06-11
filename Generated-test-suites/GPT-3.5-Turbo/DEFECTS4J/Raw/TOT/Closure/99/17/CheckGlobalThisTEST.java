import org.junit.Test;
import org.junit.Assert;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionNotConstructorOrThisAnnotated_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(null);

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, null);
  
    Assert.assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    JSDocInfo jsDoc = JSDocInfo.builder()
        .setConstructor(true)
        .build();

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, null);
  
    Assert.assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionThisAnnotated_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    JSDocInfo jsDoc = JSDocInfo.builder()
        .setHasThisType(true)
        .build();

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(jsDoc);

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, null);
  
    Assert.assertFalse(result);
  }

  @Test
  public void testVisit_ThisAndShouldReportThis_ReturnsError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    Node thisNode = new Node(Token.THIS);

    checkGlobalThis.visit(t, thisNode, null);

    Assert.assertEquals(1, compiler.getErrorCount());
  }

  @Test
  public void testVisit_AssignLhsChildNotNull_SetToNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    Node assignNode = new Node(Token.ASSIGN);
    Node lhsChildNode = new Node(Token.NAME, "a");
    Node rhsChildNode = new Node(Token.THIS);
    assignNode.addChildToBack(lhsChildNode);
    assignNode.addChildToBack(rhsChildNode);

    Node thisNode = new Node(Token.THIS);

    checkGlobalThis.visit(t, thisNode, assignNode);

    Assert.assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_AssignLhsChildNull_DoNothing() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    Node assignNode = new Node(Token.ASSIGN);
    Node lhsChildNode = new Node(Token.NAME, "a");
    Node rhsChildNode = new Node(Token.THIS);
    assignNode.addChildToBack(lhsChildNode);
    assignNode.addChildToBack(rhsChildNode);

    Node thisNode = new Node(Token.THIS);

    checkGlobalThis.visit(t, thisNode, null);

    Assert.assertEquals(lhsChildNode, checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testShouldReportThis_NotAssignLhsChildNotNull_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(new Node(Token.NAME, "a"));
    parent.addChildToBack(new Node(Token.STRING, "prototype"));

    boolean result = checkGlobalThis.shouldReportThis(thisNode, parent);

    Assert.assertFalse(result);
  }

  @Test
  public void testShouldReportThis_NotAssignLhsChildNotNull_andParentIsGet_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(new Node(Token.NAME, "a"));
    parent.addChildToBack(new Node(Token.STRING, "property"));

    boolean result = checkGlobalThis.shouldReportThis(thisNode, parent);

    Assert.assertTrue(result);
  }

  @Test
  public void testShouldReportThis_NotAssignLhsChildNotNull_andParentIsNotGet_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);

    boolean result = checkGlobalThis.shouldReportThis(thisNode, parent);

    Assert.assertFalse(result);
  }

  @Test
  public void testShouldReportThis_AssignLhsChildNotNull_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    Node thisNode = new Node(Token.THIS);

    boolean result = checkGlobalThis.shouldReportThis(thisNode, null);

    Assert.assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ParentIsNull_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    Node thisNode = new Node(Token.THIS);

    boolean result = checkGlobalThis.shouldReportThis(thisNode, null);

    Assert.assertFalse(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_JSDocInfoNotNull_ReturnsJSDocInfo() {
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = JSDocInfo.builder()
        .setHasThisType(true)
        .build();
    node.setJSDocInfo(jsDoc);

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);

    Assert.assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_JSDocInfoNullAndParentName_GetParentJSDocInfo() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo parentJsDoc = JSDocInfo.builder()
        .setHasThisType(true)
        .build();
    parent.setJSDocInfo(parentJsDoc);
    node.setParent(parent);

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);

    Assert.assertEquals(parentJsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_JSDocInfoNullAndParentNameAndGrampsVar_GetGrampsJSDocInfo() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    JSDocInfo grampsJsDoc = JSDocInfo.builder()
        .setHasThisType(true)
        .build();
    gramps.setJSDocInfo(grampsJsDoc);
    parent.setParent(gramps);
    node.setParent(parent);

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);

    Assert.assertEquals(grampsJsDoc, result);
  }

}