import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  
  @Test
  public void testScopedAliases() {
    // Given: A simple valid scoped alias test case
    String testCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new AliasTransformationHandler() {});

    Compiler compiler = new Compiler();
    compiler.init(options);
    Node root = compiler.parseTree(testCode);

    HotSwapCompilerPass processor = new ScopedAliases(compiler, null, null);
    processor.process(null, root);

    // Then: The processed code should not contain any errors
    assertEquals("Expected no errors, but found " + processor.getDiagnostics().size() + " errors.", 0, processor.getDiagnostics().size());

    // And: The processed code should have applied the alias
    Node processedCode = processor.getProcessedSource();
    assertEquals("Expected processed code to be: goog.dom.createElement(goog.dom.TagName.DIV)", "goog.dom.createElement(goog.dom.TagName.DIV)", processedCode.toSource());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    // Given: A scoped alias test case with errors
    String testCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  this.x = 5;" + // This should cause a GOOG_SCOPE_REFERENCES_THIS error
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new AliasTransformationHandler() {});

    Compiler compiler = new Compiler();
    compiler.init(options);
    Node root = compiler.parseTree(testCode);

    HotSwapCompilerPass processor = new ScopedAliases(compiler, null, null);
    processor.process(null, root);

    // Then: The processed code should have errors
    List<DiagnosticType> diagnostics = processor.getDiagnostics();
    assertEquals("Expected 1 error, but found " + diagnostics.size() + " errors.", 1, diagnostics.size());

    DiagnosticType error = diagnostics.get(0);
    assertEquals("Expected error message: The body of a goog.scope function cannot reference 'this'", GOOG_SCOPE_REFERENCES_THIS, error);

    // And: The processed code should not have applied the alias
    Node processedCode = processor.getProcessedSource();
    assertFalse("Expected processed code to not contain: goog.dom.createElement(goog.dom.TagName.DIV)", processedCode.toSource().contains("goog.dom.TagName.DIV"));
  }

  @Test
  public void testScopedAliasesWithMultipleAliases() {
    // Given: A scoped alias test case with multiple aliases
    String testCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var x = goog.x;" +
        "  var y = goog.y;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new AliasTransformationHandler() {});

    Compiler compiler = new Compiler();
    compiler.init(options);
    Node root = compiler.parseTree(testCode);

    HotSwapCompilerPass processor = new ScopedAliases(compiler, null, null);
    processor.process(null, root);

    // Then: The processed code should have errors
    List<DiagnosticType> diagnostics = processor.getDiagnostics();
    assertEquals("Expected 1 error, but found " + diagnostics.size() + " errors.", 1, diagnostics.size());

    DiagnosticType error = diagnostics.get(0);
    assertEquals("Expected error message: The alias x is assigned a value more than once", GOOG_SCOPE_ALIAS_REDEFINED, error);

    // And: The processed code should not have applied the alias
    Node processedCode = processor.getProcessedSource();
    assertFalse("Expected processed code to not contain: goog.x", processedCode.toSource().contains("goog.x"));
    assertFalse("Expected processed code to not contain: goog.y", processedCode.toSource().contains("goog.y"));
  }

  @Test
  public void testScopedAliasesWithNamespaceShadows() {
    // Given: A scoped alias test case with namespace shadows
    String testCode = "goog.scope(function() {" +
        "  var goog = {" +
        "    dom: {" +
        "      TagName: {" +
        "        DIV: 42" +
        "      }" +
        "    }" +
        "  };" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new AliasTransformationHandler() {});

    Compiler compiler = new Compiler();
    compiler.init(options);
    Node root = compiler.parseTree(testCode);

    HotSwapCompilerPass processor = new ScopedAliases(compiler, null, null);
    processor.process(null, root);

    // Then: The processed code should have errors
    List<DiagnosticType> diagnostics = processor.getDiagnostics();
    assertEquals("Expected 1 error, but found " + diagnostics.size() + " errors.", 1, diagnostics.size());

    DiagnosticType error = diagnostics.get(0);
    assertEquals("Expected error message: The alias goog.dom is assigned a value more than once", GOOG_SCOPE_ALIAS_REDEFINED, error);

    // And: The processed code should have renamed the shadowed namespace
    Node processedCode = processor.getProcessedSource();
    assertTrue("Expected processed code to contain: goog_dom", processedCode.toSource().contains("goog_dom"));
  }
}