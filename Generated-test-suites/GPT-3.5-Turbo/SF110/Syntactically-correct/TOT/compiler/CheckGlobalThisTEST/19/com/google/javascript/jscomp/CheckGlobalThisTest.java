import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void shouldTraverse_GlobalContext_ReturnsTrue() {
    Node node = mock(Node.class);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, null);
    assertTrue(result);
    verify(node).isFunction();
  }

  @Test
  public void shouldTraverse_Constructor_ReturnsFalse() {
    Node node = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.isFunction()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(true);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, null);
    assertFalse(result);
    verify(node).getJSDocInfo();
    verify(jsDoc).isConstructor();
  }

  @Test
  public void shouldTraverse_ThisAnnotation_ReturnsFalse() {
    Node node = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.isFunction()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(false);
    when(jsDoc.hasThisType()).thenReturn(true);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, null);
    assertFalse(result);
    verify(node).getJSDocInfo();
    verify(jsDoc).isConstructor();
    verify(jsDoc).hasThisType();
  }

  @Test
  public void shouldTraverse_OverrideAnnotation_ReturnsFalse() {
    Node node = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.isFunction()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(jsDoc.isConstructor()).thenReturn(false);
    when(jsDoc.hasThisType()).thenReturn(false);
    when(jsDoc.isOverride()).thenReturn(true);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, null);
    assertFalse(result);
    verify(node).getJSDocInfo();
    verify(jsDoc).isConstructor();
    verify(jsDoc).hasThisType();
    verify(jsDoc).isOverride();
  }

  @Test
  public void shouldTraverse_NotTraversable_ReturnsFalse() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.isFunction()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    assertFalse(result);
    verify(node).getJSDocInfo();
    verify(parent).getType();
  }

  @Test
  public void shouldTraverse_NotObjectLitKey_ReturnsTrue() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.isFunction()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    assertTrue(result);
    verify(node).getJSDocInfo();
    verify(parent).getType();
  }

  @Test
  public void shouldTraverse_NotObjectLitKeyWithLendedPrototype_ReturnsFalse() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Node grandparent = mock(Node.class);
    Node llhs = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    JSDocInfo maybeLends = mock(JSDocInfo.class);
    when(node.isFunction()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.STRING_KEY);
    when(parent.getParent()).thenReturn(grandparent);
    when(grandparent.getJSDocInfo()).thenReturn(maybeLends);
    when(maybeLends.getLendsName()).thenReturn("Lends.prototype");
    when(parent.getFirstChild()).thenReturn(llhs);
    when(llhs.isGetProp()).thenReturn(true);
    when(llhs.getLastChild()).thenReturn(mock(Node.class));
    when(llhs.getLastChild().getString()).thenReturn("prototype");
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    assertFalse(result);
    verify(node).getJSDocInfo();
    verify(parent).getType();
    verify(parent).getParent();
    verify(grandparent).getJSDocInfo();
    verify(maybeLends).getLendsName();
    verify(llhs).isGetProp();
    verify(llhs).getLastChild();
  }

  @Test
  public void shouldTraverse_Traversable_ReturnsTrue() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Node grandparent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.isFunction()).thenReturn(true);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.NAME);
    when(parent.getParent()).thenReturn(grandparent);
    when(grandparent.getJSDocInfo()).thenReturn(null);
    boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
    assertTrue(result);
    verify(node).getJSDocInfo();
    verify(parent).getType();
    verify(parent).getParent();
    verify(grandparent).getJSDocInfo();
  }

  @Test
  public void visit_ThisOnLeftSideOfAssign_Reported() {
    Node node = Node.newString(Token.THIS, "this");
    Node parent = mock(Node.class);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    checkGlobalThis.assignLhsChild = node;
    checkGlobalThis.visit(traversal, node, parent);
    verify(compiler).report();
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void visit_ThisPropertyAccess_Reported() {
    Node node = Node.newString(Token.THIS, "this");
    Node parent = Node.newString(Token.GETPROP, "this.property");
    checkGlobalThis.assignLhsChild = null;
    checkGlobalThis.visit(traversal, node, parent);
    verify(compiler).report();
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void visit_This_NotReported() {
    Node node = Node.newString(Token.THIS, "this");
    Node parent = mock(Node.class);
    when(parent.getType()).thenReturn(Token.NAME);
    checkGlobalThis.assignLhsChild = null;
    checkGlobalThis.visit(traversal, node, parent);
    verify(compiler, never()).report();
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void shouldReportThis_AssignLhsChildNotNull_ReturnsTrue() {
    Node node = Node.newString(Token.THIS, "this");
    checkGlobalThis.assignLhsChild = node;
    boolean result = checkGlobalThis.shouldReportThis(node);
    assertTrue(result);
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void shouldReportThis_PropertyAccess_ReturnsTrue() {
    Node node = Node.newString(Token.THIS, "this");
    Node parent = Node.newString(Token.GETPROP, "this.property");
    checkGlobalThis.assignLhsChild = null;
    boolean result = checkGlobalThis.shouldReportThis(node);
    assertTrue(result);
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void shouldReportThis_NotPropertyAccess_ReturnsFalse() {
    Node node = Node.newString(Token.THIS, "this");
    checkGlobalThis.assignLhsChild = null;
    boolean result = checkGlobalThis.shouldReportThis(node);
    assertFalse(result);
    verify(compiler, never()).report();
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void getFunctionJsDocInfo_HasJsDocInfo_ReturnsJsDocInfo() {
    Node node = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    assertEquals(jsDoc, result);
    verify(node).getJSDocInfo();
  }

  @Test
  public void getFunctionJsDocInfo_NameTypeParent_ReturnsJsDocInfo() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    JSDocInfo parentJsDoc = mock(JSDocInfo.class);
    when(node.getJSDocInfo()).thenReturn(null);
    when(node.getParent()).thenReturn(parent);
    when(parent.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.NAME);
    when(parent.getParent()).thenReturn(mock(Node.class));
    when(parent.getParent().isVar()).thenReturn(true);
    when(parent.getParent().getJSDocInfo()).thenReturn(parentJsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    assertEquals(parentJsDoc, result);
    verify(node).getJSDocInfo();
    verify(node).getParent();
    verify(parent).getJSDocInfo();
    verify(parent).getType();
    verify(parent).getParent();
    verify(parent.getParent()).isVar();
    verify(parent.getParent()).getJSDocInfo();
  }

  @Test
  public void getFunctionJsDocInfo_AssignTypeParent_ReturnsJsDocInfo() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(node.getJSDocInfo()).thenReturn(null);
    when(node.getParent()).thenReturn(parent);
    when(parent.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    assertEquals(jsDoc, result);
    verify(node).getJSDocInfo();
    verify(node).getParent();
    verify(parent).getJSDocInfo();
    verify(parent).getType();
  }

  @Test
  public void getFunctionJsDocInfo_ParentType_ReturnsNull() {
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    when(node.getJSDocInfo()).thenReturn(null);
    when(node.getParent()).thenReturn(parent);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    assertNull(result);
    verify(node).getJSDocInfo();
    verify(node).getParent();
    verify(parent).getType();
  }
}