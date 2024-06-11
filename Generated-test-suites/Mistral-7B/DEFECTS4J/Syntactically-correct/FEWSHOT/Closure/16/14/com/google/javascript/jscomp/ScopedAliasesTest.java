package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    preprocessorSymbolTable = null;
    transformationHandler = new TestAliasTransformationHandler();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess() {
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  var y = goog.y;\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "  }).createElement(goog.dom.TagName.DIV);\n" +
        "});");
    scopedAliases.process(null, root);
    Node expected = Node.parse("goog.x; // from first goog.scope\n" +
        "goog.y; // from first goog.scope\n" +
        "goog.dom.createElement(goog.dom.TagName.DIV); // from second goog.scope");
    assertSame(expected, root);
  }

  @Test
  public void testHotSwapScript() {
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "  }).createElement(goog.dom.TagName.DIV);\n" +
        "});");
    scopedAliases.hotSwapScript(root, null);
    Node expected = Node.parse("goog.x; // from first goog.scope\n" +
        "goog.dom.createElement(goog.dom.TagName.DIV); // from second goog.scope");
    assertSame(expected, root);
  }

  @Test
  public void testStripLeadingAndTrailingQuotes() {
    Traversal traversal = new Traversal();
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var foo = 'foo';\n" +
        "  var bar = 'bar';\n" +
        "  var baz = ''foo''bar'';\n" +
        "});");
    scopedAliases.process(null, root);
    NodeTraversal.traverse(compiler, root, traversal);
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    for (Node aliasDefinition : aliasDefinitionsInOrder) {
      assertSame(aliasDefinition, traversal.getAliasUsages().get(0).getAliasDefinition());
      assertEquals("foo", Util.stripLeadingAndTrailingQuotes(aliasDefinition.getString()));
    }
  }

  @Test
  public void testStripLeadingHyphens() {
    Traversal traversal = new Traversal();
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var --foo = 1;\n" +
        "  var --bar = 2;\n" +
        "  var --baz-- = 3;\n" +
        "});");
    scopedAliases.process(null, root);
    NodeTraversal.traverse(compiler, root, traversal);
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    for (Node aliasDefinition : aliasDefinitionsInOrder) {
      assertEquals("foo", Util.stripLeadingHyphens(aliasDefinition.getString()));
    }
  }

  @Test
  public void testAliasUsage() {
    Traversal traversal = new Traversal();
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "  }).createElement(goog.dom.TagName.DIV);\n" +
        "});");
    scopedAliases.process(null, root);
    NodeTraversal.traverse(compiler, root, traversal);
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        Node aliasDefinition = ((AliasedNode) aliasUsage).getAliasDefinition();
        assertEquals("x", aliasDefinition.getString());
        assertEquals(root.getChildAtIndex(1), aliasUsage.getAliasReference());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        Node aliasReference = ((AliasedTypeNode) aliasUsage).getTypeReference();
        assertEquals("goog.x", aliasReference.getString());
      }
    }
  }

  @Test
  public void testAliasDefinitionOrdering() {
    Traversal traversal = new Traversal();
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  var y = goog.y;\n" +
        "  var z = goog.z;\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "    var y = 2;\n" +
        "    var z = 3;\n" +
        "  }).createElement(goog.dom.TagName.DIV);\n" +
        "  goog.scope(function() {\n" +
        "    var y = 4;\n" +
        "  }).createElement(goog.dom.TagName.DIV);\n" +
        "  goog.scope(function() {\n" +
        "    var x = 5;\n" +
        "  }).createElement(goog.dom.TagName.DIV);\n" +
        "});");
    scopedAliases.process(null, root);
    NodeTraversal.traverse(compiler, root, traversal);
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> expected = Lists.newArrayList(
        Node.parse("x"),
        Node.parse("y"),
        Node.parse("z"),
        Node.parse("goog.x"),
        Node.parse("goog.y"),
        Node.parse("goog.z")
    );
    for (int i = 0; i < aliasDefinitionsInOrder.size(); i++) {
      Node aliasDefinition = aliasDefinitionsInOrder.get(i);
      Node expectedDefinition = expected.get(i);
      assertEquals(expectedDefinition, aliasDefinition);
    }
  }

  @Test
  public void testNoAliasDefinition() {
    Traversal traversal = new Traversal();
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  goog.scope(function() {\n" +
        "    var y = 1;\n" +
        "  }).createElement(goog.dom.TagName.DIV);\n" +
        "});");
    scopedAliases.process(null, root);
    NodeTraversal.traverse(compiler, root, traversal);
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        Node aliasDefinition = ((AliasedNode) aliasUsage).getAliasDefinition();
        assertNull(aliasDefinition);
      }
    }
  }

  @Test
  public void testErrorConditions() {
    Node root = Node.parse("goog.scope(function() {\n" +
        "  this.x = 1;\n" +
        "  return;\n" +
        "});");
    Traversal traversal = new Traversal();
    scopedAliases.process(null, root);
    NodeTraversal.traverse(compiler, root, traversal);
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnosticCount());
    List<Node> diagnostics = traversal.getDiagnostics();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", diagnostics.get(0).getType().name());
  }

  @Test
  public void testApplyAliases() {
    Traversal traversal = new Traversal();
    Node root = Node.parse("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "  }).createElement(goog.dom.TagName.DIV);\n" +
        "});");
    scopedAliases.process(null, root);
    NodeTraversal.traverse(compiler, root, traversal);
    for (AliasUsage aliasUsage : traversal.getAliasUsages()) {
      if (aliasUsage instanceof AliasedNode) {
        Node aliasDefinition = ((AliasedNode) aliasUsage).getAliasDefinition();
        Node aliasReference = ((AliasedNode) aliasUsage).getAliasReference();
        assertEquals("x", aliasDefinition.getString());
        assertEquals(aliasReference, aliasUsage.applyAlias());
      }
    }
  }

  @Test
  public void testCollapseScopes() {
    Traversal traversal = new Traversal();
    Node root = Node.parse("goog.scope(function() {\n" +
        "  goog.scope(function() {\n" +
        "    goog.scope(function() {\n" +
        "      goog.scope(function() {\n" +
        "        var x = 1;\n" +
        "      }).createElement(goog.dom.TagName.DIV);\n" +
        "    }).createElement(goog.dom.TagName.DIV);\n" +
        "  }).createElement(goog.dom.TagName.DIV);\n" +
        "});");
    scopedAliases.process(null, root);
    NodeTraversal.traverse(compiler, root, traversal);
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(3, scopeCalls.size());
    Node expected = Node.parse("goog.scope(function() {\n" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "});");
    for (Node scopeCall : scopeCalls) {
      assertEquals(expected, scopeCall.getParent());
    }
  }

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public void report(NodeTraversal t, Node n, DiagnosticType error, String... arguments) {
      // Do nothing.
    }
  }

  private static class TestAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public AliasTransformation logAliasTransformation(
        String fileName, SourcePosition position) {
      // Do nothing.
      return null;
    }
  }
}