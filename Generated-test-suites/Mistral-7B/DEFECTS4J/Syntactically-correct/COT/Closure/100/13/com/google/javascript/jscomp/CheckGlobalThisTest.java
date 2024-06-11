import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler() {}, checker);

    Node root = createNode("function unsafeFunction() { this.property = 'global'; }");

    traversal.traverse(root);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, checker.compiler.getDiagnostics().get(0).getType());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.compiler.getDiagnostics().get(0).getCode());
  }

  @Test
  public void testCheckGlobalThisConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node root = createNode("function MyConstructor() { this.property = 'constructor'; }");

    traversal.traverse(root);

    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisPrototype() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node root = createNode("var MyObject = function() {}; MyObject.prototype.property = 'prototype';");

    traversal.traverse(root);

    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisNestedAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node root = createNode("var a = function() { this.property = 'nested'; }; a.property = 'parent';");

    traversal.traverse(root);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals(DiagnosticType.WARNING, checker.compiler.getDiagnostics().get(0).getType());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.compiler.getDiagnostics().get(0).getCode());
  }

  @Test
  public void testCheckGlobalThisAnnotatedFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node root = createNode("@this function unsafeFunction() { this.property = 'annotated'; }");

    traversal.traverse(root);

    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  @Test
  public void testCheckGlobalThisShouldNotTraverseFunctionsWithoutThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    Node root = createNode("function safeFunction() {}");

    traversal.traverse(root);

    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  private Node createNode(String code) {
    RhinoCompiler compiler = new RhinoCompiler();
    Node root = compiler.parse(code, null, null);
    return root;
  }

  private NodeTraversal traversal = null;
}