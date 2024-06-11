import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Unit tests for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {
      @Override
      public void transform(SourcePosition position, String oldName,
          String newName) {
        System.out.println("Alias: " + oldName + " -> " + newName);
      }
    });
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    dom.createElement(DIV); " +
        "  }); " +
        "});";

    // When
    Node root = Parser.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(SCOPING_METHOD_NAME, scopeCall.getFirstChild().getString());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasedNode aliasNode = (AliasedNode) aliasUsages.get(0);
    Node aliasDefinition = aliasNode.getAliasDefinition();
    assertEquals("dom", aliasNode.getAliasReference().getString());
    assertEquals("goog.dom", aliasDefinition.getString());

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    Node aliasDefinitionNode = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("dom", aliasDefinitionNode.getString());
  }

  @Test
  public void testScopedAliasesWithError() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {
      @Override
      public void transform(SourcePosition position, String oldName,
          String newName) {
        throw new RuntimeException("Unexpected alias transformation");
      }
    });
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    dom.createElement(DIV); " +
        "  }); " +
        "});";

    // When & Then
    expectException(RuntimeException.class, () -> {
      Node root = Parser.parse(sourceCode);
      scopedAliases.process(null, root);
    });
  }

  @Test
  public void testScopedAliasesWithEmptyBlock() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "goog.scope(function() {})";

    // When
    Node root = Parser.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(SCOPING_METHOD_NAME, scopeCall.getFirstChild().getString());
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());
  }

  @Test
  public void testScopedAliasesWithNonAliasVariable() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "goog.scope(function() {" +
        "  var x = 1;" +
        "});";

    // When
    Node root = Parser.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(SCOPING_METHOD_NAME, scopeCall.getFirstChild().getString());
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitions.size());

    // Verify no error was reported
    assertEquals(0, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasesWithMissingScopeCall() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "" +
        "var x = 1;";

    // When
    Node root = Parser.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(0, scopeCalls.size());
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitions.size());

    // Verify no error was reported
    assertEquals(0, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasesWithNestedScopeCalls() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var x = 1;" +
        "  })" +
        "});";

    // When
    Node root = Parser.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node innerScopeCall = scopeCalls.get(0);
    Node outerScopeCall = innerScopeCall.getParent();
    assertEquals(SCOPING_METHOD_NAME, outerScopeCall.getFirstChild().getString());
    assertEquals(SCOPING_METHOD_NAME, innerScopeCall.getFirstChild().getString());
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(0, aliasUsages.size());
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitions.size());

    // Verify no error was reported
    assertEquals(0, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasesWithMultipleAliases() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var a = 1, b = 2;" +
        "});";

    // When
    Node root = Parser.parse(sourceCode);
    scopedAliases.process(null, root);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(SCOPING_METHOD_NAME, scopeCall.getFirstChild().getString());
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    List<Node> aliasDefinitionA = Lists.newArrayList(aliasDefinitions.get(0));
    List<Node> aliasDefinitionB = Lists.newArrayList(aliasDefinitions.get(1));
    assertEquals("a", aliasDefinitionA.get(0).getString());
    assertEquals("a", aliasDefinitionA.get(1).getString());
    assertEquals("b", aliasDefinitionB.get(0).getString());
    assertEquals("b", aliasDefinitionB.get(1).getString());

    // Verify no error was reported
    assertEquals(0, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasesWithAliasesRedefined() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var a = 1;" +
        "  goog.scope(function() {" +
        "    var a = 2;" +
        "  })" +
        "});";

    // When & Then
    expectException(AssertionError.class, () -> {
      Node root = Parser.parse(sourceCode);
      scopedAliases.process(null, root);
    });
  }

  @Test
  public void testScopedAliasesWithAliasesUsingReturn() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  return goog.dom;" +
        "});";

    // When & Then
    expectException(GOOG_SCOPE_USES_RETURN.getType(), () -> {
      Node root = Parser.parse(sourceCode);
      scopedAliases.process(null, root);
    });
  }

  @Test
  public void testScopedAliasesWithAliasesUsingThis() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  this.x = 1;" +
        "});";

    // When & Then
    expectException(GOOG_SCOPE_REFERENCES_THIS.getType(), () -> {
      Node root = Parser.parse(sourceCode);
      scopedAliases.process(null, root);
    });
  }

  @Test
  public void testScopedAliasesWithAliasesUsingThrow() {
    // Given
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation() {});
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, options);

    String sourceCode = "" +
        "goog.scope(function() {" +
        "  throw new Error();" +
        "});";

    // When & Then
    expectException(GOOG_SCOPE_USES_THROW.getType(), () -> {
      Node root = Parser.parse(sourceCode);
      scopedAliases.process(null, root);
    });
  }

  private void expectException(Class<?> exceptionType, Runnable runnable) {
    try {
      runnable.run();
      fail("Expected exception of type " + exceptionType.getName());
    } catch (RuntimeException e) {
      if (exceptionType.isAssignableFrom(e.getClass())) {
        // Success
      } else {
        fail("Unexpected exception: " + e.getClass().getName());
      }
    }
  }
}