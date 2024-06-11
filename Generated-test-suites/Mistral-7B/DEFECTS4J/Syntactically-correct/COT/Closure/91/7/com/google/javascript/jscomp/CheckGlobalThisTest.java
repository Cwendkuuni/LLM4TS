import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private NodeTraversal traversal;

  @org.junit.Before
  public void setup() {
    this.compiler = new Compiler();
    this.level = WARNING;
    this.traversal = new NodeTraversal(this.compiler.getRootNode(), this);
  }

  @org.junit.Test
  public void testShouldTraverseFunction() {
    Node node = new Node(Token.FUNCTION, null);
    assertTrue(new CheckGlobalThis(compiler, level).shouldTraverse(traversal, node, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseConstructor() {
    Node node = new Node(Token.FUNCTION, null);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    node.setJSDocInfo(jsDoc);
    assertFalse(new CheckGlobalThis(compiler, level).shouldTraverse(traversal, node, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithAnnotation() {
    Node node = new Node(Token.FUNCTION, null);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType("MyType");
    node.setJSDocInfo(jsDoc);
    assertFalse(new CheckGlobalThis(compiler, level).shouldTraverse(traversal, node, null));
  }

  @org.junit.Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node node = new Node(Token.FUNCTION, null);
    assertFalse(new CheckGlobalThis(compiler, level).shouldTraverse(traversal, node, null));
    node.setParent(new Node(Token.ASSIGN, null));
    assertTrue(new CheckGlobalThis(compiler, level).shouldTraverse(traversal, node, null));
  }

  @org.junit.Test
  public void testShouldNotTraversePrototypeProperty() {
    Node node = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));
    node.setFirstChild(lhs);
    Node rhs = new Node(Token.IDENT, "obj");
    node.setLastChild(rhs);
    Node func = new Node(Token.FUNCTION, null);
    lhs.setParent(func);
    rhs.setParent(func);
    assertTrue(new CheckGlobalThis(compiler, level).shouldTraverse(traversal, func, node));
    assertFalse(new CheckGlobalThis(compiler, level).shouldTraverse(traversal, func, lhs));
  }

  @org.junit.Test
  public void testShouldNotTraversePrototypeSubProperty() {
    Node node = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.GETPROP, null, new Node(Token.STRING, "prototype"));
    lhs = new Node(Token.GETPROP, null, lhs);
    node.setFirstChild(lhs);
    Node rhs = new Node(Token.IDENT, "obj");
    node.setLastChild(rhs);
    Node func = new Node(Token.FUNCTION, null);
    lhs.setParent(func);
    rhs.setParent(func);
    assertTrue(new CheckGlobalThis(compiler, level).shouldTraverse(traversal, func, node));
    assertFalse(new CheckGlobalThis(compiler, level).shouldTraverse(traversal, func, lhs));
  }

  @org.junit.Test
  public void testShouldTraverseFunctionWithLhsChild() {
    Node node = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.IDENT, "assignLhsChild");
    Node rhs = new Node(Token.FUNCTION, null);
    node.setFirstChild(lhs);
    node.setLastChild(rhs);
    lhs.setParent(node);
    rhs.setParent(node);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, rhs, node));
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node node = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.THIS, null);
    Node rhs = new Node(Token.IDENT, "rightSide");
    node.setFirstChild(lhs);
    node.setLastChild(rhs);
    lhs.setParent(node);
    rhs.setParent(node);
    new CheckGlobalThis(compiler, level).visit(traversal, node, null);
    assertTrue(compiler.hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS"));
  }

  @org.junit.Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node node = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.IDENT, "lhs");
    Node rhs = new Node(Token.THIS, null);
    node.setFirstChild(lhs);
    node.setLastChild(rhs);
    lhs.setParent(node);
    rhs.setParent(node);
    new CheckGlobalThis(compiler, level).visit(traversal, node, null);
    assertFalse(compiler.hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS"));
  }

  @org.junit.Test
  public void testShouldReportThisOnPropertyAccess() {
    Node node = new Node(Token.GETPROP, null, new Node(Token.THIS, null));
    new CheckGlobalThis(compiler, level).visit(traversal, node, null);
    assertTrue(compiler.hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS"));
  }

  @org.junit.Test
  public void testShouldNotReportThisOnPropertyAccessViaAnotherProperty() {
    Node node = new Node(Token.GETPROP, null, new Node(Token.THIS, null));
    node.setLastChild(new Node(Token.GETPROP, null, new Node(Token.STRING, "property")));
    new CheckGlobalThis(compiler, level).visit(traversal, node, null);
    assertFalse(compiler.hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS"));
  }

  @org.junit.Test
  public void testShouldNotReportThisOnPropertyAccessViaAnotherPropertyWithTrailingComma() {
    Node node = new Node(Token.GETPROP, null, new Node(Token.THIS, null));
    node.setLastChild(new Node(Token.GETPROP, null, new Node(Token.STRING, "property"), new Node(Token.COMMA, null)));
    new CheckGlobalThis(compiler, level).visit(traversal, node, null);
    assertFalse(compiler.hasError(DiagnosticType.warning, "JSC_USED_GLOBAL_THIS"));
  }

  @org.junit.Test
  public void testGetFunctionJsDocInfo() {
    Node node = new Node(Token.FUNCTION, null);
    JSDocInfo result = new CheckGlobalThis().getFunctionJsDocInfo(traversal, node, null);
    assertNull(result);

    node.setParent(new Node(Token.NAME, "funcName"));
    result = new CheckGlobalThis().getFunctionJsDocInfo(traversal, node, null);
    assertNull(result);

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    node.setJSDocInfo(jsDoc);
    assertNull(new CheckGlobalThis().getFunctionJsDocInfo(traversal, node, null));

    jsDoc.setThisType("MyType");
    node.setJSDocInfo(jsDoc);
    assertNull(new CheckGlobalThis().getFunctionJsDocInfo(traversal, node, null));

    jsDoc.setInterface(true);
    node.setJSDocInfo(jsDoc);
    assertNull(new CheckGlobalThis().getFunctionJsDocInfo(traversal, node, null));

    jsDoc.setOverride(true);
    node.setJSDocInfo(jsDoc);
    assertNull(new CheckGlobalThis().getFunctionJsDocInfo(traversal, node, null));

    jsDoc = new JSDocInfo();
    node.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, new CheckGlobalThis().getFunctionJsDocInfo(traversal, node, null));
  }
}