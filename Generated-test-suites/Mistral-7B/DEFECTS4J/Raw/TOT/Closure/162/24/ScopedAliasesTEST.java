import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ErrorCollector;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  dom.createElement(DIV); " +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler();
    compiler.init(errorCollector, jsCode, options);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, null);

    Node rootNode = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);
    assertFalse("Should not have errors.", traversal.hasErrors());

    // Check aliases were applied in the correct order.
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitions.size());
    assertEquals("DIV", aliasDefinitions.get(0).getQualifiedName());

    // Check alias usages were replaced with the correct aliases.
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertTrue(aliasUsage instanceof AliasedNode);
    Node aliasReference = ((AliasedNode) aliasUsage).aliasReference;
    assertEquals("dom.TagName.DIV", aliasReference.getQualifiedName());
    assertEquals("DIV", aliasReference.getString());

    // Check goog.scope call was removed.
    Node googScopeCall = NodeUtil.findFirstChildOfType(rootNode, Token.CALL);
    assertNull("Should not exist.", googScopeCall);

    // Check goog.dom.createElement was transformed correctly.
    Node createElementCall = NodeUtil.findFirstChildOfType(rootNode, Node.CALL);
    assertNotNull("Should exist.", createElementCall);
    assertEquals("goog.dom.createElement", createElementCall.getQualifiedName());
  }

  @Test
  public void testScopedAliasesWithBadGoogScopeCall() {
    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.scope(function() {" +
        "    dom.createElement(DIV); " +
        "  }); " +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler();
    compiler.init(errorCollector, jsCode, options);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, null);

    Node rootNode = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);
    assertTrue("Should have errors.", traversal.hasErrors());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", error.getName());
  }

  @Test
  public void testScopedAliasesWithDuplicateAliases() {
    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.scope(function() {" +
        "    var DIV = 1; " +
        "  }); " +
        "  dom.createElement(DIV); " +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler();
    compiler.init(errorCollector, jsCode, options);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, null);

    Node rootNode = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);
    assertTrue("Should have errors.", traversal.hasErrors());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getName());
  }

  @Test
  public void testScopedAliasesWithNonAliasLocal() {
    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  var div = document.createElement(DIV); " +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler();
    compiler.init(errorCollector, jsCode, options);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, null);

    Node rootNode = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);
    assertTrue("Should have errors.", traversal.hasErrors());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", error.getName());
  }

  @Test
  public void testScopedAliasesWithBadParameterFunction() {
    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.scope(function(x) {" +
        "    dom.createElement(DIV); " +
        "  }); " +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler();
    compiler.init(errorCollector, jsCode, options);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, null);

    Node rootNode = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);
    assertTrue("Should have errors.", traversal.hasErrors());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", error.getName());
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.scope(function() {" +
        "    return; " +
        "  }); " +
        "  dom.createElement(DIV); " +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler();
    compiler.init(errorCollector, jsCode, options);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, null);

    Node rootNode = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);
    assertTrue("Should have errors.", traversal.hasErrors());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", error.getName());
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.scope(function() {" +
        "    throw new Error(); " +
        "  }); " +
        "  dom.createElement(DIV); " +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    ErrorCollector errorCollector = new ErrorCollector();
    Compiler compiler = new Compiler();
    compiler.init(errorCollector, jsCode, options);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, null);

    Node rootNode = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);
    assertTrue("Should have errors.", traversal.hasErrors());

    DiagnosticType error = traversal.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", error.getName());
  }

  private static class NoOpAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public void logAliasTransformation(String fileName, SourcePosition position) {
      // Do nothing.
    }
  }
}