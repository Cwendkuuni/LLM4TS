import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;

/**
 * Tests for ScopedAliases class.
 */
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private AliasTransformationHandler transformationHandler;
  private Diagnostics diagnostics;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    options = new CompilerOptions() {};
    transformationHandler = new NoOpAliasTransformationHandler();
    diagnostics = new Diagnostics();
  }

  @Test
  public void testScopedAliases() {
    // Test a valid goog.scope block
    String code = "goog.scope(function() {" +
        "var x = goog.x;" +
        "goog.x = 10;" +
        "});";

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, textToNode(code));

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(ImmutableList.of(
        GOOG_SCOPE_USED_IMPROPERLY,
        GOOG_SCOPE_ALIAS_REDEFINED
    ), traversal.getErrors());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    // Test a goog.scope block with errors
    code = "goog.scope(function() {" +
        "goog.scope(function() {});" + // Nested goog.scope
        "var x = this;" +
        "return;" +
        "goog.x = 10;" +
        "});";

    scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, textToNode(code));

    traversal = scopedAliases.getTraversal();
    assertEquals(ImmutableList.of(
        GOOG_SCOPE_USED_IMPROPERLY,
        GOOG_SCOPE_REFERENCES_THIS,
        GOOG_SCOPE_USES_RETURN,
        GOOG_SCOPE_ALIAS_REDEFINED
    ), traversal.getErrors());
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());
  }

  @Test
  public void testScopedAliasesWithAliasTransformation() {
    // Test a valid goog.scope block with alias transformation
    String code = "goog.scope(function() {" +
        "var goog = {};" +
        "goog.x = 1;" +
        "goog.scope(function() {" +
        "var x = goog.x;" +
        "goog.x = 10;" +
        "});" +
        "goog.scope(function() {" +
        "var y = goog.x;" +
        "goog.x = 20;" +
        "});" +
        "});";

    AliasTransformation aliasTransformation = new AliasTransformation(
        compilationUnit(),
        ImmutableList.of(new AliasMapping("goog.x", "x")),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );

    transformationHandler = new TransformationHandlerWithAliasTransformation(aliasTransformation);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, textToNode(code));

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(ImmutableList.of(), traversal.getErrors());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(2, traversal.getAliasUsages().size());

    assertEquals("x", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.x", traversal.getAliasUsages().get(0).getAliasDefinition().getString());
    assertEquals("x", traversal.getAliasUsages().get(1).getAliasReference().getString());

    Node scopeCallNode = traversal.getScopeCalls().get(0);
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getString());
    assertEquals(1, scopeCallNode.getChildCount());
  }

  private interface NoOpAliasTransformationHandler extends AliasTransformationHandler {

    @Override
    void logAliasTransformation(
        String fileName, SourcePosition position,
        String fromQualifiedName, String toQualifiedName) {}
  }

  private static class TransformationHandlerWithAliasTransformation
      extends NoOpAliasTransformationHandler {

    private final AliasTransformation aliasTransformation;

    TransformationHandlerWithAliasTransformation(AliasTransformation aliasTransformation) {
      this.aliasTransformation = aliasTransformation;
    }

    @Override
    void logAliasTransformation(
        String fileName, SourcePosition position,
        String fromQualifiedName, String toQualifiedName) {
      aliasTransformation.logAliasTransformation(fileName, position, fromQualifiedName, toQualifiedName);
    }
  }
}