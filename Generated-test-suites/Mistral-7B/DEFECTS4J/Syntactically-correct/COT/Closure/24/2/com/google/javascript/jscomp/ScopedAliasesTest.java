import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.createCompilerError;
import static com.google.javascript.jscomp.Diagnostics.createSourceFile;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.rhino.Token.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.TokenFactory;
import com.google.javascript.rhino.traverse.NodeTraversal;

public class ScopedAliasesTest {
  private Compiler compiler;
  private ErrorReporter errorReporter;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = new Compiler(null);
    errorReporter = compiler.getErrorReporter();
  }

  @Test
  public void testProcess() {
    String sourceCode =
        "goog.scope(function() {" +
            "  function test() {}" +
            "  goog.dom.createElement('DIV');" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    compiler.compile(ImmutableList.of(sourceFile), null, null, null);
    List<Node> ast = compiler.getSourceRoots().get(0).getChildNodes();

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, ast.get(0));

    assertTrue("No errors reported", errorReporter.getNumErrors() == 0);
  }

  @Test
  public void testProcessWithPhantomError() {
    String sourceCode =
        "goog.scope(function() {" +
            "  throw new Error('Test error');" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    compiler.compile(ImmutableList.of(sourceFile), null, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, astGetFirstChild(ast.get(0)));

    DiagnosticType errorType = createCompilerError(
        "JSC_GOOG_SCOPE_USES_THROW", null, null, null);
    assertEquals("One error reported", 1, errorReporter.getNumErrors());
    assertEquals("Error type", errorType.getName(),
        errorReporter.getError(0).getType().getName());
  }

  @Test
  public void testProcessWithImproperCall() {
    String sourceCode =
        "goog.scope(function() {" +
            "  function test() {" +
            "    function nestedTest() {}" +
            "  }" +
            "  goog.dom.createElement('DIV');" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    compiler.compile(ImmutableList.of(sourceFile), null, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, astGetFirstChild(ast.get(0)));

    DiagnosticType errorType = GOOG_SCOPE_USED_IMPROPERLY;
    assertEquals("One error reported", 1, errorReporter.getNumErrors());
    assertEquals("Error type", errorType.getName(),
        errorReporter.getError(0).getType().getName());
  }

  @Test
  public void testProcessWithBadParameters() {
    String sourceCode =
        "goog.scope(function(param1, param2) {" +
            "  goog.dom.createElement('DIV');" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    compiler.compile(ImmutableList.of(sourceFile), null, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, astGetFirstChild(ast.get(0)));

    DiagnosticType errorType = GOOG_SCOPE_HAS_BAD_PARAMETERS;
    assertEquals("One error reported", 1, errorReporter.getNumErrors());
    assertEquals("Error type", errorType.getName(),
        errorReporter.getError(0).getType().getName());
  }

  @Test
  public void testProcessWithThisReference() {
    String sourceCode =
        "goog.scope(function() {" +
            "  this.test = function() {};" +
            "  goog.dom.createElement('DIV');" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    compiler.compile(ImmutableList.of(sourceFile), null, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, astGetFirstChild(ast.get(0)));

    DiagnosticType errorType = GOOG_SCOPE_REFERENCES_THIS;
    assertEquals("One error reported", 1, errorReporter.getNumErrors());
    assertEquals("Error type", errorType.getName(),
        errorReporter.getError(0).getType().getName());
  }

  @Test
  public void testProcessWithReturnStatement() {
    String sourceCode =
        "goog.scope(function() {" +
            "  return; " +
            "  goog.dom.createElement('DIV');" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    compiler.compile(ImmutableList.of(sourceFile), null, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, astGetFirstChild(ast.get(0)));

    DiagnosticType errorType = GOOG_SCOPE_USES_RETURN;
    assertEquals("One error reported", 1, errorReporter.getNumErrors());
    assertEquals("Error type", errorType.getName(),
        errorReporter.getError(0).getType().getName());
  }

  @Test
  public void testProcessWithAliasDefinition() {
    String sourceCode =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    compiler.compile(ImmutableList.of(sourceFile), null, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, astGetFirstChild(ast.get(0)));

    assertTrue("No errors reported", errorReporter.getNumErrors() == 0);
  }

  @Test
  public void testProcessWithAliasRedefinition() {
    String sourceCode =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  var DIV = function() {};" +
            "  goog.dom.createElement(DIV);" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    compiler.compile(ImmutableList.of(sourceFile), null, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, astGetFirstChild(ast.get(0)));

    DiagnosticType errorType = GOOG_SCOPE_ALIAS_REDEFINED;
    assertEquals("One error reported", 1, errorReporter.getNumErrors());
    assertEquals("Error type", errorType.getName(),
        errorReporter.getError(0).getType().getName());
  }

  @Test
  public void testProcessWithNonAliasLocalVariable() {
    String sourceCode =
        "goog.scope(function() {" +
            "  var test = 'test';" +
            "  goog.dom.createElement('DIV');" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    compiler.compile(ImmutableList.of(sourceFile), null, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, astGetFirstChild(ast.get(0)));

    DiagnosticType errorType = GOOG_SCOPE_NON_ALIAS_LOCAL;
    assertEquals("One error reported", 1, errorReporter.getNumErrors());
    assertEquals("Error type", errorType.getName(),
        errorReporter.getError(0).getType().getName());
  }

  @Test
  public void testHotSwapScript() {
    String sourceCode =
        "goog.scope(function() {" +
            "  goog.dom.createElement('DIV');" +
            "});";

    SourceFile sourceFile = createSourceFile("TestScopedAliases.js", sourceCode);
    Node rootNode = astGetFirstChild(sourceFile.getAst());

    ScopedAliases scopedAliases = new ScopedAliases(
        compiler, null, null);
    scopedAliases.process(null, rootNode);

    assertTrue("No errors reported", errorReporter.getNumErrors() == 0);

    String updatedSourceCode =
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  goog.dom.createElement(dom.TagName.DIV);" +
            "});";

    Node updatedRootNode = parseCompilationUnit(updatedSourceCode);

    NodeTraversal traversal = new NodeTraversal(compiler, updatedRootNode, null);
    traversal.setScopedCallback(scopedAliases.new Traversal());
    NodeTraversal.traverse(compiler, updatedRootNode, traversal);

    assertTrue("No errors reported", errorReporter.getNumErrors() == 0);
  }
}