package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testScopedAliases_ValidScenario() {
    // Given
    Node externs = Node.createTextNode("");
    Node root = Node.createTextNode("foo() {}");

    // When
    scopedAliases.process(externs, root);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    // Assert
    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());
  }

  @Test
  public void testScopedAliases_MultipleAliases_ValidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function() {" +
        "var a = 1; " +
        "var b = 2; " +
        "})");
    Node root = Node.createTextNode("goog.scope(function() {" +
        "var a = goog.a; " +
        "var b = goog.b; " +
        "})");

    // When
    scopedAliases.process(externs, root);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    // Assert
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(2, aliasUsages.size());

    // Verify aliasDefinitionsInOrder
    AliasDefinition firstAliasDefinition =
        (AliasDefinition) aliasDefinitionsInOrder.get(0);
    assertEquals("a", firstAliasDefinition.getName());
    assertEquals("com.google.javascript.jscomp.ScopedAliases$1",
        firstAliasDefinition.getAlias());

    AliasDefinition secondAliasDefinition =
        (AliasDefinition) aliasDefinitionsInOrder.get(1);
    assertEquals("b", secondAliasDefinition.getName());
    assertEquals("com.google.javascript.jscomp.ScopedAliases$2",
        secondAliasDefinition.getAlias());

    // Verify scopeCalls
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getString());

    // Verify aliasUsages
    AliasUsage firstAliasUsage = aliasUsages.get(0);
    assertEquals(firstAliasDefinition, firstAliasUsage.getAliasedNode());

    AliasUsage secondAliasUsage = aliasUsages.get(1);
    assertEquals(secondAliasDefinition, secondAliasUsage.getAliasedNode());
  }

  @Test
  public void testScopedAliases_ImproperUsage_InvalidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function() {" +
        "goog.scope(function() {}); " +
        "});");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      scopedAliases.process(externs, Node.createTextNode(""));
    });

    // Verify diagnostics were reported
    ArgumentCaptor<Diagnostic> diagnosticCaptor =
        ArgumentCaptor.forClass(Diagnostic.class);
    List<Diagnostic> diagnostics = when(compiler.getDiagnostics())
        .thenReturn(Collections.emptyList())
        .thenCallRealMethod()
        .thenReturn(Collections.singletonList(diagnosticCaptor.capture()))
        .getValue();

    Diagnostic diagnostic = diagnosticCaptor.getValue();
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostic.getType());
    assertEquals("The call to goog.scope must be alone in a single statement.",
        diagnostic.getMessage(null));
  }

  @Test
  public void testScopedAliases_NonAnonymousFunction_InvalidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function goo() {})");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      scopedAliases.process(externs, Node.createTextNode(""));
    });

    // Verify diagnostics were reported
    ArgumentCaptor<Diagnostic> diagnosticCaptor =
        ArgumentCaptor.forClass(Diagnostic.class);
    List<Diagnostic> diagnostics = when(compiler.getDiagnostics())
        .thenReturn(Collections.emptyList())
        .thenCallRealMethod()
        .thenReturn(Collections.singletonList(diagnosticCaptor.capture()))
        .getValue();

    Diagnostic diagnostic = diagnosticCaptor.getValue();
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostic.getType());
    assertEquals("The call to goog.scope must be alone in a single statement.",
        diagnostic.getMessage(null));
  }

  @Test
  public void testScopedAliases_NonFunctionParameter_InvalidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function() { goo = 1 })");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      scopedAliases.process(externs, Node.createTextNode(""));
    });

    // Verify diagnostics were reported
    ArgumentCaptor<Diagnostic> diagnosticCaptor =
        ArgumentCaptor.forClass(Diagnostic.class);
    List<Diagnostic> diagnostics = when(compiler.getDiagnostics())
        .thenReturn(Collections.emptyList())
        .thenCallRealMethod()
        .thenReturn(Collections.singletonList(diagnosticCaptor.capture()))
        .getValue();

    Diagnostic diagnostic = diagnosticCaptor.getValue();
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostic.getType());
    assertEquals("The call to goog.scope must take only a single parameter.  It " +
        "must be an anonymous function that itself takes no parameters.",
        diagnostic.getMessage(null));
  }

  @Test
  public void testScopedAliases_AliasesSelf_InvalidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function() {" +
        "var a = function() { return this; }; " +
        "goog.scope(function() { a = 1; }); " +
        "})");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      scopedAliases.process(externs, Node.createTextNode(""));
    });

    // Verify diagnostics were reported
    ArgumentCaptor<Diagnostic> diagnosticCaptor =
        ArgumentCaptor.forClass(Diagnostic.class);
    List<Diagnostic> diagnostics = when(compiler.getDiagnostics())
        .thenReturn(Collections.emptyList())
        .thenCallRealMethod()
        .thenReturn(Collections.singletonList(diagnosticCaptor.capture()))
        .getValue();

    Diagnostic diagnostic = diagnosticCaptor.getValue();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnostic.getType());
    assertEquals("The body of a goog.scope function cannot reference 'this'.",
        diagnostic.getMessage(null));
  }

  @Test
  public void testScopedAliases_Returns_InvalidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function() {" +
        "return 1; " +
        "})");

    // When & Then
    assertFalse(scopedAliases.process(externs, Node.createTextNode("")));

    // Verify no diagnostics were reported
    List<Diagnostic> diagnostics = when(compiler.getDiagnostics())
        .thenReturn(Collections.emptyList())
        .thenCallRealMethod()
        .thenReturn(Collections.emptyList())
        .getValue();

    // Verify no aliases were applied
    ArgumentCaptor<Node> aliasDefinitionCaptor =
        ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<AliasUsage> aliasUsageCaptor =
        ArgumentCaptor.forClass(AliasUsage.class);
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
  }

  @Test
  public void testScopedAliases_Throws_InvalidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function() {" +
        "throw new Error('Test Error'); " +
        "})");

    // When & Then
    assertFalse(scopedAliases.process(externs, Node.createTextNode("")));

    // Verify no diagnostics were reported
    List<Diagnostic> diagnostics = when(compiler.getDiagnostics())
        .thenReturn(Collections.emptyList())
        .thenCallRealMethod()
        .thenReturn(Collections.emptyList())
        .getValue();

    // Verify no aliases were applied
    ArgumentCaptor<Node> aliasDefinitionCaptor =
        ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<AliasUsage> aliasUsageCaptor =
        ArgumentCaptor.forClass(AliasUsage.class);
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());
  }

  @Test
  public void testScopedAliases_AliasRedefined_InvalidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function() {" +
        "var a = 1; " +
        "goog.scope(function() { var a = 2; }); " +
        "})");

    // When & Then
    ArgumentCaptor<Diagnostic> diagnosticCaptor =
        ArgumentCaptor.forClass(Diagnostic.class);
    List<Diagnostic> diagnostics = when(compiler.getDiagnostics())
        .thenReturn(Collections.emptyList())
        .thenCallRealMethod()
        .thenReturn(Collections.singletonList(diagnosticCaptor.capture()))
        .getValue();

    Diagnostic diagnostic = diagnosticCaptor.getValue();
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostic.getType());
    assertEquals("The alias a is assigned a value more than once.",
        diagnostic.getMessage(null));
  }

  @Test
  public void testScopedAliases_AliasNonLocal_InvalidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function() {" +
        "var a = 1; " +
        "var b = goog.a; " +
        "})");

    // When & Then
    ArgumentCaptor<Diagnostic> diagnosticCaptor =
        ArgumentCaptor.forClass(Diagnostic.class);
    List<Diagnostic> diagnostics = when(compiler.getDiagnostics())
        .thenReturn(Collections.emptyList())
        .thenCallRealMethod()
        .thenReturn(Collections.singletonList(diagnosticCaptor.capture()))
        .getValue();

    Diagnostic diagnostic = diagnosticCaptor.getValue();
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, diagnostic.getType());
    assertEquals("The local variable a is in a goog.scope and is not an alias.",
        diagnostic.getMessage(null));
  }

  @Test
  public void testScopedAliases_AliasWithType_ValidScenario() {
    // Given
    Node externs = Node.createTextNode("goog.scope(function() {" +
        "var a: string = 1;" +
        "goog.scope(function() {" +
        "var a: number = 2;" +
        "})" +
        "})");

    // When
    scopedAliases.process(externs, Node.createTextNode(""));

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    // Assert
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(2, aliasUsages.size());

    // Verify aliasDefinitionsInOrder
    AliasDefinition firstAliasDefinition =
        (AliasDefinition) aliasDefinitionsInOrder.get(0);
    assertEquals("a", firstAliasDefinition.getName());
    assertEquals("com.google.javascript.jscomp.ScopedAliases$1",
        firstAliasDefinition.getAlias());
    assertEquals("string", firstAliasDefinition.getAliasType());

    AliasDefinition secondAliasDefinition =
        (AliasDefinition) aliasDefinitionsInOrder.get(1);
    assertEquals("a", secondAliasDefinition.getName());
    assertEquals("com.google.javascript.jscomp.ScopedAliases$2",
        secondAliasDefinition.getAlias());
    assertEquals("number", secondAliasDefinition.getAliasType());

    // Verify scopeCalls
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getString());

    // Verify aliasUsages
    AliasUsage firstAliasUsage = aliasUsages.get(0);
    assertEquals(firstAliasDefinition, firstAliasUsage.getAliasedNode());

    AliasUsage secondAliasUsage = aliasUsages.get(1);
    assertEquals(secondAliasDefinition, secondAliasUsage.getAliasedNode());
  }

  private static class AliasDefinition {
    private final String name;
    private final String alias;
    private final String aliasType;

    AliasDefinition(String name, String alias, String aliasType) {
      this.name = name;
      this.alias = alias;
      this.aliasType = aliasType;
    }

    String getName() {
      return name;
    }

    String getAlias() {
      return alias;
    }

    String getAliasType() {
      return aliasType;
    }
  }
}