import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private Compiler compiler;
  private AliasTransformationHandler handler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    handler = new DummyAliasTransformationHandler();
    options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    options.setAliasTransformationHandler(handler);
  }

  @Test
  public void testBasicScopedAlias() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  goog.dom.createElement('DIV');" +
        "});";

    Node root = Node.parse(input);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    assertEquals("goog.dom.createElement", traversal.getAliasDefinitionsInOrder().get(0).getString());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("dom", aliasUsage.getFirstChild().getString());
    assertEquals("goog.dom", aliasDefinition.getString());
  }

  @Test
  public void testMultipleScopedAliases() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node root = Node.parse(input);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(2, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(2, traversal.getAliasUsages().size());

    assertEquals("goog.dom.createElement", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.dom.TagName", traversal.getAliasDefinitionsInOrder().get(1).getString());

    Node aliasUsageDom = traversal.getAliasUsages().get(0);
    Node aliasDefinitionDom = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("dom", aliasUsageDom.getFirstChild().getString());
    assertEquals("goog.dom", aliasDefinitionDom.getString());

    Node aliasUsageDiv = traversal.getAliasUsages().get(1);
    Node aliasDefinitionDiv = traversal.getAliasDefinitionsInOrder().get(1);
    assertEquals("DIV", aliasUsageDiv.getFirstChild().getString());
    assertEquals("goog.dom.TagName", aliasDefinitionDiv.getString());
  }

  @Test
  public void testImproperlyUsedGoogScope() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});var i = 0;" +
        "goog.scope(function() {" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node root = Node.parse(input);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(2, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(2, traversal.getScopeCalls().size());
    assertEquals(2, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Diagnostics diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getType().name());
  }

  @Test
  public void testScopeWithBadParameters() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "});goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "});";

    Node root = Node.parse(input);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(2, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Diagnostics diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", diagnostic.getType().name());
  }

  @Test
  public void testScopeWithThisReference() {
    String input = "goog.scope(function() {" +
        "  this.dom = goog.dom;" +
        "  goog.dom.createElement('DIV');" +
        "});";

    Node root = Node.parse(input);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Diagnostics diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", diagnostic.getType().name());
  }

  @Test
  public void testScopeWithReturnStatement() {
    String input = "goog.scope(function() {" +
        "  return goog.dom.createElement('DIV');" +
        "});";

    Node root = Node.parse(input);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Diagnostics diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostic.getType().name());
  }

  @Test
  public void testScopeWithThrowStatement() {
    String input = "goog.scope(function() {" +
        "  throw new Error('Test error');" +
        "});";

    Node root = Node.parse(input);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Diagnostics diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostic.getType().name());
  }

  @Test
  public void testAliasRedefined() {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = goog.dom;" +
        "  goog.dom.createElement('DIV');" +
        "});";

    Node root = Node.parse(input);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Diagnostics diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostic.getType().name());
  }

  @Test
  public void testAliasNonLocalVariable() {
    String input = "goog.scope(function() {" +
        "  var i = 0;" +
        "  var dom = goog.dom;" +
        "  goog.dom.createElement('DIV');" +
        "});";

    Node root = Node.parse(input);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());

    assertTrue(traversal.hasErrors());
    List<Diagnostics> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Diagnostics diagnostic = diagnostics.get(0);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnostic.getType().name());
  }

  private static class DummyAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public AliasTransformation logAliasTransformation(String fileName, SourcePosition position) {
      return null;
    }
  }
}