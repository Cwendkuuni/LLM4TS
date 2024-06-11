import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.test.BaseCompilerTestCase;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest extends BaseCompilerTestCase {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = newCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    compiler.addPhase(checker);
    compiler.setOption(CompilerOptions.WARNING_NON_OVERRIDDEN_THIS, CompilerOptions.ErrorLevel.ERROR);
  }

  @Test
  public void testNoGlobalThis() {
    String jsCode = "function a() {};";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertFalse("No global 'this' detected.", checker.hasDiagnostics());
  }

  @Test
  public void testGlobalThisPropertyAccess() {
    String jsCode = "function a() { this.x = 1; }";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue("Global 'this' detected.", checker.hasDiagnostics());
  }

  @Test
  public void testGlobalThisAssignment() {
    String jsCode = "var a = function() { this = {x: 1}; }";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertTrue("Global 'this' detected.", checker.hasDiagnostics());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String jsCode = "function @this MyClass() {}";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertFalse("No global 'this' detected.", checker.hasDiagnostics());
  }

  @Test
  public void testConstructorFunction() {
    String jsCode = "function MyClass() {}";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertFalse("No global 'this' detected.", checker.hasDiagnostics());
  }

  @Test
  public void testPrototypeProperty() {
    String jsCode = "var MyClass = function() {}; MyClass.prototype.x = function() {};";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertFalse("No global 'this' detected.", checker.hasDiagnostics());
  }

  @Test
  public void testAssignedToPrototype() {
    String jsCode = "function MyClass() {}; MyClass.prototype.a = this;";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertFalse("No global 'this' detected.", checker.hasDiagnostics());
  }

  @Test
  public void testNestedAssignment() {
    String jsCode = "(function() { var a = this; this.x = a; })();";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertFalse("No global 'this' detected.", checker.hasDiagnostics());
  }

  @Test
  public void testGetProperty() {
    String jsCode = "function a() { return this.x; }";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertFalse("No global 'this' detected.", checker.hasDiagnostics());
  }

  @Test
  public void testNestedGetProperty() {
    String jsCode = "function a() { return this.x.y; }";
    Node ast = parseJsCode(compiler, jsCode);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertFalse("No global 'this' detected.", checker.hasDiagnostics());
  }
}