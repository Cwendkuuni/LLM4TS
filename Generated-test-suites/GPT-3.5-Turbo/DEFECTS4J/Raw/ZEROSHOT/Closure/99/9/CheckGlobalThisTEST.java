import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractCompiler;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }
  
  @Test
  public void testShouldTraverse_functionWithConstructorAnnotation_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    n.setJSDocInfo(jsDocInfo);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }
  
  @Test
  public void testShouldTraverse_functionWithThisAnnotation_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setThisType(new Node());
    n.setJSDocInfo(jsDocInfo);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }
  
  @Test
  public void testShouldTraverse_functionWithOverrideAnnotation_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride(true);
    n.setJSDocInfo(jsDocInfo);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, null));
  }
  
  @Test
  public void testShouldTraverse_functionInsideBlock_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    n.setParent(parent);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_functionInsideScript_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_functionInsideName_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    n.setParent(parent);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_functionInsideAssign_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    n.setParent(parent);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_otherNode_returnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.STRING);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, null));
  }
  
  @Test
  public void testVisit_thisNode_shouldReportThis() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.STRING);
    diagnosticType.setReported(true);
    checkGlobalThis.visit(t, n, parent);
    assertTrue(diagnosticType.isReported());
  }
  
  @Test
  public void testVisit_thisNode_shouldNotReportThis() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NUMBER);
    diagnosticType.setReported(true);
    checkGlobalThis.visit(t, n, parent);
    assertFalse(diagnosticType.isReported());
  }
  
  @Test
  public void testVisit_assignLhsChild_shouldSetAssignLhsChildToNull() {
    NodeTraversal t = new NodeTraversal();
    Node assignLhsChild = new Node(Token.STRING);
    checkGlobalThis.assignLhsChild = assignLhsChild;
    checkGlobalThis.visit(t, null, assignLhsChild);
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_functionHasJSDocInfo_shouldReturnJsDocInfo() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    functionNode.setJSDocInfo(jsDocInfo);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_functionParentIsNameNodeWithJSDocInfo_shouldReturnJsDocInfo() {
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME);
    JSDocInfo jsDocInfo = new JSDocInfo();
    nameNode.setJSDocInfo(jsDocInfo);
    functionNode.setParent(nameNode);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_functionParentIsAssignNodeAndParentOfAssignNodeIsVarNodeWithJSDocInfo_shouldReturnJsDocInfo() {
    Node functionNode = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN);
    Node varNode = new Node(Token.VAR);
    JSDocInfo jsDocInfo = new JSDocInfo();
    varNode.setJSDocInfo(jsDocInfo);
    assignNode.setParent(varNode);
    functionNode.setParent(assignNode);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_functionParentIsAssignNodeWithJSDocInfo_shouldReturnJsDocInfo() {
    Node functionNode = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN);
    JSDocInfo jsDocInfo = new JSDocInfo();
    assignNode.setJSDocInfo(jsDocInfo);
    functionNode.setParent(assignNode);
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_functionAndParentNodesDoNotHaveJSDocInfo_shouldReturnNull() {
    Node functionNode = new Node(Token.FUNCTION);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
}