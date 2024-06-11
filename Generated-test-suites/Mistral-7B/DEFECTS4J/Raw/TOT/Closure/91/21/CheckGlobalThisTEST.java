import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    String jsCode = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING));

    Node root = checker.parseTree(jsCode);
    NodeTraversal traverser = new NodeTraversal(root);
    checker.process(root, traverser);

    int numberOfDiagnostics = compiler.getDiagnostics().size();
    assertEquals("There should be a warning", 1, numberOfDiagnostics);

    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("The warning type should be JSC_USED_GLOBAL_THIS",
        DiagnosticType.WARNING, diagnostic.getType());
    assertEquals("The warning message should be 'dangerous use of the global 'this' object'",
        "dangerous use of the global 'this' object", diagnostic.getMessage(null));
  }

  @org.junit.Test
  public void testCheckGlobalThis_Constructor() {
    String jsCode = "function EvilConstructor() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING));

    Node root = checker.parseTree(jsCode);
    NodeTraversal traverser = new NodeTraversal(root);
    checker.process(root, traverser);

    int numberOfDiagnostics = compiler.getDiagnostics().size();
    assertEquals("There should be no warning", 0, numberOfDiagnostics);
  }

  @org.junit.Test
  public void testCheckGlobalThis_PrototypeMethod() {
    String jsCode = "function EvilObject() { this.useful = undefined; } EvilObject.prototype.evil = function() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING));

    Node root = checker.parseTree(jsCode);
    NodeTraversal traverser = new NodeTraversal(root);
    checker.process(root, traverser);

    int numberOfDiagnostics = compiler.getDiagnostics().size();
    assertEquals("There should be no warning", 0, numberOfDiagnostics);
  }

  @org.junit.Test
  public void testCheckGlobalThis_OverriddenMethod() {
    String jsCode = "function EvilObject() {} EvilObject.prototype.evil = function() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING));

    Node root = checker.parseTree(jsCode);
    NodeTraversal traverser = new NodeTraversal(root);
    checker.process(root, traverser);

    int numberOfDiagnostics = compiler.getDiagnostics().size();
    assertEquals("There should be no warning", 0, numberOfDiagnostics);
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithoutAnnotation() {
    String jsCode = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING));

    Node root = checker.parseTree(jsCode);
    NodeTraversal traverser = new NodeTraversal(root);
    checker.process(root, traverser);

    int numberOfDiagnostics = compiler.getDiagnostics().size();
    assertEquals("There should be a warning", 1, numberOfDiagnostics);
  }

  @org.junit.Test
  public void testCheckGlobalThis_Assignment() {
    String jsCode = "var a = function() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING));

    Node root = checker.parseTree(jsCode);
    NodeTraversal traverser = new NodeTraversal(root);
    checker.process(root, traverser);

    int numberOfDiagnostics = compiler.getDiagnostics().size();
    assertEquals("There should be no warning", 0, numberOfDiagnostics);
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    String jsCode = "function @this evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING));

    Node root = checker.parseTree(jsCode);
    NodeTraversal traverser = new NodeTraversal(root);
    checker.process(root, traverser);

    int numberOfDiagnostics = compiler.getDiagnostics().size();
    assertEquals("There should be no warning", 0, numberOfDiagnostics);
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionInsideObjectLiteral() {
    String jsCode = "var obj = { evil: function() { this.useful = undefined; } }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING));

    Node root = checker.parseTree(jsCode);
    NodeTraversal traverser = new NodeTraversal(root);
    checker.process(root, traverser);

    int numberOfDiagnostics = compiler.getDiagnostics().size();
    assertEquals("There should be no warning", 0, numberOfDiagnostics);
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionInsidePrototype() {
    String jsCode = "var obj = {}; obj.prototype.evil = function() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler();
    Checker checker = new Checker(compiler, new CheckGlobalThis(compiler, WARNING));

    Node root = checker.parseTree(jsCode);
    NodeTraversal traverser = new NodeTraversal(root);
    checker.process(root, traverser);

    int numberOfDiagnostics = compiler.getDiagnostics().size();
    assertEquals("There should be no warning", 0, numberOfDiagnostics);
  }
}