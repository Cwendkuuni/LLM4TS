import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private static final DiagnosticType GLOBAL_THIS = DiagnosticType.warning(
      "JSC_USED_GLOBAL_THIS",
      "dangerous use of the global 'this' object");

  @org.junit.Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);

    // Test valid uses of 'this'
    Node validFunction = createNode("function validFunction() {}", Token.FUNCTION);
    Node validPrototypeMethod = createNode("var obj = {}; obj.prototype.validPrototype = function validPrototype() {}",
        Token.VAR_DECL);
    Node validConstructor = createNode("function validConstructor() {}", Token.FUNCTION, "Constructor");
    Node validThisAnnotated = createNode("@this function thisAnnotated() {}", Token.FUNCTION);
    Node validLogicalPlace = createNode("function logicalPlace() {}", Token.FUNCTION);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    checker.shouldTraverse(traversal, validFunction, null);
    checker.shouldTraverse(traversal, validPrototypeMethod, null);
    checker.shouldTraverse(traversal, validConstructor, null);
    checker.shouldTraverse(traversal, validThisAnnotated, null);
    checker.shouldTraverse(traversal, validLogicalPlace, null);

    NodeTraversal.Callback callback = mock(Callback.class);
    when(callback.report).then(invocation -> {});

    when(traversal.getParent()).thenReturn(null);
    when(traversal.getCurrentNode()).thenReturn(validFunction);
    checker.visit(traversal, validFunction, null);
    checker.visit(traversal, validPrototypeMethod, null);
    checker.visit(traversal, validConstructor, null);
    checker.visit(traversal, validThisAnnotated, null);
    checker.visit(traversal, validLogicalPlace, null);

    // Test invalid uses of 'this'
    Node invalidAssignment = createNode("this = 1", Token.ASSIGN);
    Node invalidPropertyAccess = createNode("this.property = 1", Token.ASSIGN);
    Node invalidFunctionInBlock = createNode("function() { this.property = 1 }", Token.FUNCTION, null);
    Node invalidFunctionInScript = createNode("this.property = 1", Token.SCRIPT);

    checker.shouldTraverse(traversal, invalidAssignment, null);
    checker.shouldTraverse(traversal, invalidPropertyAccess, null);
    checker.shouldTraverse(traversal, invalidFunctionInBlock, null);
    checker.shouldTraverse(traversal, invalidFunctionInScript, null);

    when(traversal.getParent()).thenReturn(null);
    when(traversal.getCurrentNode()).thenReturn(invalidAssignment);
    checker.visit(traversal, invalidAssignment, null);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(GLOBAL_THIS, compiler.getDiagnostics().get(0).getType());

    when(traversal.getCurrentNode()).thenReturn(invalidPropertyAccess);
    checker.visit(traversal, invalidPropertyAccess, null);
    assertEquals(2, compiler.getDiagnostics().size());

    when(traversal.getCurrentNode()).thenReturn(invalidFunctionInBlock);
    checker.visit(traversal, invalidFunctionInBlock, null);
    assertEquals(3, compiler.getDiagnostics().size());

    when(traversal.getCurrentNode()).thenReturn(invalidFunctionInScript);
    checker.visit(traversal, invalidFunctionInScript, null);
    assertEquals(4, compiler.getDiagnostics().size());
  }

  private Node createNode(String code, int... types) {
    Node node = RhinoTestUtils.createJsNodeFromString(code);
    setNodeType(node, types);
    return node;
  }

  private void setNodeType(Node node, int... types) {
    if (types != null) {
      for (int type : types) {
        node.setType(type);
      }
    }
  }
}