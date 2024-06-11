import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {
  
  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }
  
  @Test
  public void testShouldTraverse_GlobalContext_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.SCRIPT);
    Node parent = null;
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionConstructor_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = null;
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().recordConstructor().build());
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionWithThisTypeAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = null;
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().createThisType().build());
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = null;
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().recordOverride().build());
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionWithBlockParent_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionWithScriptParent_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionWithNameParent_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionWithAssignParent_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testVisit_ThisNode_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.SCRIPT);
    assertFalse(checkGlobalThis.visit(t, n, parent));
  }
  
  @Test
  public void testVisit_ThisNodeWithParentPropertyAccess_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    assertTrue(checkGlobalThis.visit(t, n, parent));
  }
  
  @Test
  public void testVisit_ThisNodeWithParentAssign_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    assertTrue(checkGlobalThis.visit(t, n, parent));
  }
  
  @Test
  public void testVisit_AssignLhsChildNotNull_ResetsAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.SCRIPT);
    checkGlobalThis.assignLhsChild = n;
    checkGlobalThis.visit(t, n, parent);
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJsDocInfo_ReturnsJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNotNull(jsDocInfo);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithParentName_ReturnsParentJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
    n.setParent(parent);
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNotNull(jsDocInfo);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithParentAssign_ReturnsParentJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
    n.setParent(parent);
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNotNull(jsDocInfo);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithGrampsVar_ReturnsGrampsJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    gramps.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
    parent.setParent(gramps);
    n.setParent(parent);
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNotNull(jsDocInfo);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDocInfo_ReturnsNull() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(jsDocInfo);
  }
  
}