import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {

  private static final Compiler COMPILER = new Compiler();
  private static final CompilerOptions COMPILER_OPTIONS =
      new CompilerOptions(new AliasTransformationHandler() {});

  @Test
  public void testNoAlias() {
    Node root = Node.stringLiteral("goog.dom.createElement('div')");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());
  }

  @Test
  public void testAlias() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  var x = goog.x;" +
        "})");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals(TOKEN_NAME, aliasDefinition.getType());
    assertEquals("x", aliasDefinition.getString());
    assertEquals(Node.SCOPE, aliasDefinition.getParent().getType());
    Node aliasUsage = aliasDefinitions.get(1);
    assertEquals(TOKEN_NAME, aliasUsage.getType());
    assertEquals("x", aliasUsage.getString());
    assertTrue(aliasUsage.getParent().isVar());
    assertEquals(TOKEN_FUNCTION, aliasDefinition.getParent().getType());
  }

  @Test
  public void testBadAliasCall() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  goog.scope(function() {" +
        "  })");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    List<DiagnosticType> diagnostics = COMPILER_OPTIONS.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostics.get(0));
  }

  @Test
  public void testBadAliasParameters() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  goog.scope(function(x) {" +
        "  })");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    List<DiagnosticType> diagnostics = COMPILER_OPTIONS.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostics.get(0));
  }

  @Test
  public void testBadAliasSelfReference() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  var x = this;" +
        "})");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    List<DiagnosticType> diagnostics = COMPILER_OPTIONS.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostics.get(0));
  }

  @Test
  public void testBadAliasReturn() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  return goog.x;" +
        "})");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    List<DiagnosticType> diagnostics = COMPILER_OPTIONS.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostics.get(0));
  }

  @Test
  public void testBadAliasThrow() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  throw goog.x;" +
        "})");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    List<DiagnosticType> diagnostics = COMPILER_OPTIONS.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostics.get(0));
  }

  @Test
  public void testAliasRedefinition() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  var x = goog.x;" +
        "  var x = 'foo';" +
        "})");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    List<DiagnosticType> diagnostics = COMPILER_OPTIONS.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostics.get(0));
  }

  @Test
  public void testAliasNonLocal() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  var x = 1;" +
        "  goog.scope(function() {" +
        "    var x = 2;" +
        "  })" +
        "})");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    List<DiagnosticType> diagnostics = COMPILER_OPTIONS.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, diagnostics.get(0));
  }

  @Test
  public void testAliasNamespace() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var google = goog.goog;" +
        "  })" +
        "})");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    List<DiagnosticType> diagnostics = COMPILER_OPTIONS.getDiagnostics();
    assertEquals(0, diagnostics.size());

    Traversal traversal2 = new Traversal();
    Node root2 = Node.stringLiteral("goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var goog = {dom: {createElement: function() {}}}" +
        "  })" +
        "})");
    COMPILER.process(null, root2, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    diagnostics = COMPILER_OPTIONS.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testAliasWithType() {
    Node root = Node.stringLiteral("goog.scope(function() {" +
        "  class Foo {" +
        "    Bar bar" +
        "  }" +
        "  var foo = new Foo();" +
        "  goog.scope(function() {" +
        "    class Foo {" +
        "      Bar bar" +
        "    }" +
        "    var foo = new Foo();" +
        "  })" +
        "})");
    Traversal traversal = new Traversal();
    COMPILER.process(null, root, new ScopedAliases(COMPILER, null, COMPILER_OPTIONS));
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    List<Node> typeNodes = Lists.newArrayList();
    JSDocInfo info = root.getFirstChild().getFirstChild().getJSDocInfo();
    for (Node node : info.getTypeNodes()) {
      typeNodes.add(node);
    }
    for (Node node : typeNodes) {
      Traversal.TraversalCallback callback = new Traversal.TraversalCallback() {
        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
          if (n.getType() == TOKEN_NAME && n.getString().equals("Foo")) {
            Node aliasDefinition = null;
            for (Node aliasDefinitionCandidate : traversal.getAliasDefinitionsInOrder()) {
              if (aliasDefinitionCandidate.isFunction() &&
                  aliasDefinitionCandidate.getFirstChild().getString().equals("Foo")) {
                aliasDefinition = aliasDefinitionCandidate;
                break;
              }
            }
            assertNotNull(aliasDefinition);
            Node aliasUsage = null;
            for (Node aliasUsageCandidate : traversal.getAliasUsages()) {
              if (aliasUsageCandidate.isCall() &&
                  aliasUsageCandidate.getFirstChild().isQualifiedName() &&
                  aliasUsageCandidate.getLastChild().isFunction() &&
                  aliasUsageCandidate.getLastChild().getFirstChild().getString().equals("Foo")) {
                aliasUsage = aliasUsageCandidate;
                break;
              }
            }
            assertNotNull(aliasUsage);
            assertNotNull(aliasDefinition);
            assertEquals(aliasDefinition, aliasUsage.getParent().getParent());
            assertEquals(aliasDefinition, aliasUsage.getParent());
            assertEquals(aliasDefinition, aliasUsage.getLastChild().getParent());
            assertEquals(aliasDefinition, aliasUsage.getLastChild());
          }
        }
      };
      NodeTraversal.traverse(COMPILER, root, callback);
    }
  }
}