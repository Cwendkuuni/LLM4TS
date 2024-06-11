import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Diagnostics;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CheckLevel.WARNING, DiagnosticType.JSC_USED_GLOBAL_THIS);
    Checker checker = new Checker(compiler, options);

    Node sourceNode = new Node("function evil() { this.useful = undefined; }");
    Diagnostics diagnostics = checker.check(sourceNode, null);
    assertTrue(diagnostics.getNumErrors() == 1);

    sourceNode = new Node("function safePrototype() { this.prototype.useful = undefined; }");
    diagnostics = checker.check(sourceNode, null);
    assertFalse(diagnostics.getNumErrors() > 0);

    sourceNode = new Node("function safeFunction() { this.useful = undefined; }");
    diagnostics = checker.newCheck(new CheckGlobalThis(compiler, WARNING)).check(sourceNode, null);
    assertFalse(diagnostics.getNumErrors() > 0);

    sourceNode = new Node("function constructor() { this.useful = undefined; }");
    diagnostics = checker.check(sourceNode, null);
    assertFalse(diagnostics.getNumErrors() > 0);

    sourceNode = new Node("function @constructor() { this.useful = undefined; }");
    diagnostics = checker.check(sourceNode, null);
    assertFalse(diagnostics.getNumErrors() > 0);

    sourceNode = new Node("function safeFunctionWithThis() { @this this.useful = undefined; }");
    diagnostics = checker.newCheck(new CheckGlobalThis(compiler, WARNING)).check(sourceNode, null);
    assertFalse(diagnostics.getNumErrors() > 0);

    sourceNode = new Node("function safeFunctionWithThisAnnotation() { @this this.useful = undefined; }");
    diagnostics = checker.check(sourceNode, null);
    assertFalse(diagnostics.getNumErrors() > 0);
  }
}