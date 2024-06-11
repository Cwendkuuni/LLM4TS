import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis_SafeUses() {
    String jsCode = "function foo() { return this.x; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node root = new Node(Token.SCRIPT, jsCode);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traverser = new NodeTraversal(root, checker);
    traverser.traverse(root);
    assertEquals("No warnings expected", 0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_Constructor() {
    String jsCode = "function Foo() { this.x = 42; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node root = new Node(Token.SCRIPT, jsCode);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traverser = new NodeTraversal(root, checker);
    traverser.traverse(root);
    assertEquals("One warning expected", 1, compiler.getDiagnostics().size());
    DiagnosticType warning = compiler.getDiagnostics().get(0).getType();
    assertTrue("Warning should be of type JSC_USED_GLOBAL_THIS", warning == DiagnosticType.warning(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }

  @org.junit.Test
  public void testCheckGlobalThis_PrototypeProperty() {
    String jsCode = "var a = { x: 42 }; function foo() { return a.prototype.x; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node root = new Node(Token.SCRIPT, jsCode);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traverser = new NodeTraversal(root, checker);
    traverser.traverse(root);
    assertEquals("No warnings expected", 0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThis_PrototypeSubProperty() {
    String jsCode = "var a = { x: { y: 42 } }; function foo() { return a.x.prototype.y; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node root = new Node(Token.SCRIPT, jsCode);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traverser = new NodeTraversal(root, checker);
    traverser.traverse(root);
    assertEquals("One warning expected", 1, compiler.getDiagnostics().size());
    DiagnosticType warning = compiler.getDiagnostics().get(0).getType();
    assertTrue("Warning should be of type JSC_USED_GLOBAL_THIS", warning == DiagnosticType.warning(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    String jsCode = "function foo() { this.x = 42; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Node root = new Node(Token.SCRIPT, jsCode);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, ERROR);
    NodeTraversal traverser = new NodeTraversal(root, checker);
    traverser.traverse(root);
    assertEquals("One error expected", 1, compiler.getDiagnostics().size());
    DiagnosticType error = compiler.getDiagnostics().get(0).getType();
    assertTrue("Error should be of type JSC_USED_GLOBAL_THIS", error == DiagnosticType.error(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }
}