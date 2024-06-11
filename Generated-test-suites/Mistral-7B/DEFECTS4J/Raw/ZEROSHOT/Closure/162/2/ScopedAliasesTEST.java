import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "});";

    Node root = AbstractCompiler.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);
    assertFalse("There should be no errors.", scopedAliases.hasErrors());
    assertEquals("The number of alias definitions should be 1.", 1,
        scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals("The alias definition should be 'dom'.", "dom",
        scopedAliases.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("The number of alias usage should be 1.", 1,
        scopedAliases.getAliasUsages().size());
    assertEquals("The alias usage should be 'dom'.", "dom",
        scopedAliases.getAliasUsages().get(0).aliasReference.getString());
    assertEquals("The alias definition for 'dom' should be 'goog.dom'.",
        "goog.dom",
        ((AliasedNode) scopedAliases.getAliasUsages().get(0)).aliasDefinition
            .getQualifiedName());
    assertEquals("The number of scope calls should be 1.", 1,
        scopedAliases.getScopeCalls().size());
    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    assertEquals("The scope call should be 'goog.scope'.", "goog.scope",
        scopeCall.getFirstChild().getString());

    // Re-parse to check no errors.
    Node rootAfterPass = AbstractCompiler.parse(sourceCode, "test.js", null);
    scopedAliases.hotSwapScript(rootAfterPass, root);
    assertFalse("There should be no errors.", scopedAliases.hasErrors());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "  return 1;" +
        "});";

    Node root = AbstractCompiler.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);
    assertTrue("There should be an error.", scopedAliases.hasErrors());
    assertEquals("The number of errors should be 1.", 1,
        scopedAliases.getDiagnostics().length);
    DiagnosticType error = scopedAliases.getDiagnostics()[0].getType();
    assertEquals("The error should be GOOG_SCOPE_USES_RETURN.",
        DiagnosticType.JSC_GOOG_SCOPE_USES_RETURN, error);
  }

  @Test
  public void testScopedAliasesWithBadScopeCall() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  goog.scope(function() {" +
        "    var DIV = dom.TagName.DIV; " +
        "    goog.dom.createElement(DIV); " +
        "  });" +
        "});";

    Node root = AbstractCompiler.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);
    assertTrue("There should be an error.", scopedAliases.hasErrors());
    assertEquals("The number of errors should be 1.", 1,
        scopedAliases.getDiagnostics().length);
    DiagnosticType error = scopedAliases.getDiagnostics()[0].getType();
    assertEquals("The error should be GOOG_SCOPE_USED_IMPROPERLY.",
        DiagnosticType.JSC_GOOG_SCOPE_USED_IMPROPERLY, error);
  }

  @Test
  public void testScopedAliasesWithThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  this.dom = dom;" +
        "});";

    Node root = AbstractCompiler.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);
    assertTrue("There should be an error.", scopedAliases.hasErrors());
    assertEquals("The number of errors should be 1.", 1,
        scopedAliases.getDiagnostics().length);
    DiagnosticType error = scopedAliases.getDiagnostics()[0].getType();
    assertEquals("The error should be GOOG_SCOPE_REFERENCES_THIS.",
        DiagnosticType.JSC_GOOG_SCOPE_REFERENCES_THIS, error);
  }

  @Test
  public void testScopedAliasesWithAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var googDom = goog.dom;" +
        "  goog.dom = googDom;" +
        "});";

    Node root = AbstractCompiler.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);
    assertTrue("There should be an error.", scopedAliases.hasErrors());
    assertEquals("The number of errors should be 1.", 1,
        scopedAliases.getDiagnostics().length);
    DiagnosticType error = scopedAliases.getDiagnostics()[0].getType();
    assertEquals("The error should be GOOG_SCOPE_ALIAS_REDEFINED.",
        DiagnosticType.JSC_GOOG_SCOPE_ALIAS_REDEFINED, error);
  }

  @Test
  public void testScopedAliasesWithNonAliasLocal() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var element = document.createElement('DIV');" +
        "});";

    Node root = AbstractCompiler.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);
    assertTrue("There should be an error.", scopedAliases.hasErrors());
    assertEquals("The number of errors should be 1.", 1,
        scopedAliases.getDiagnostics().length);
    DiagnosticType error = scopedAliases.getDiagnostics()[0].getType();
    assertEquals("The error should be GOOG_SCOPE_NON_ALIAS_LOCAL.",
        DiagnosticType.JSC_GOOG_SCOPE_NON_ALIAS_LOCAL, error);
  }

  @Test
  public void testScopedAliasesWithMultipleAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    HotSwapCompilerPass scopedAliases =
        new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var googDom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});";

    Node root = AbstractCompiler.parse(sourceCode, "test.js", null);
    scopedAliases.process(null, root);
    assertFalse("There should be no errors.", scopedAliases.hasErrors());
    assertEquals("The number of alias definitions should be 2.", 2,
        scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals("The first alias should be 'dom'.", "dom",
        scopedAliases.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("The second alias should be 'goog.dom'.",
        "goog.dom",
        scopedAliases.getAliasDefinitionsInOrder().get(1).getString());
  }
}