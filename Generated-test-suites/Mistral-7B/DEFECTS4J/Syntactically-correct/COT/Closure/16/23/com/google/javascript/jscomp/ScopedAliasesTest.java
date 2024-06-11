import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private ScopedAliases scopedAliases;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    options = new CompilerOptions();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String sourceFileName, SourcePosition<AliasTransformation> position) {
        return new AliasTransformation() {};
      }
    });
  }

  @Test
  public void testValidScopedAlias() {
    // Given
    Node rootNode = createNode(
        "goog.scope(function() {",
        "  var x = goog.x;",
        "});");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertThat(aliasDefinition.getString()).isEqualTo("x = goog.x;");
    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertThat(aliasUsage.getParent().getParent().getLastChild().getString())
        .isEqualTo("x");
  }

  @Test
  public void testMultipleScopedAliases() {
    // Given
    Node rootNode = createNode(
        "goog.scope(function() {",
        "  var x = goog.x;",
        "  var y = goog.y;",
        "});");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = traversal.getAliasUsages();

    // Check order and values
    Node xDefinition = aliasDefinitions.get(0);
    Node xUsage = aliasUsages.get(0);
    assertThat(xDefinition.getString()).isEqualTo("x = goog.x;");
    assertThat(xUsage.getParent().getParent().getLastChild().getString())
        .isEqualTo("x");

    Node yDefinition = aliasDefinitions.get(1);
    Node yUsage = aliasUsages.get(1);
    assertThat(yDefinition.getString()).isEqualTo("y = goog.y;");
    assertThat(yUsage.getParent().getParent().getLastChild().getString())
        .isEqualTo("y");
  }

  @Test
  public void testImproperUsageOfGoogScope() {
    // Given
    Node rootNode = createNode(
        "goog.scope(function() {",
        "  var x = function() {};",
        "});");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> errors = traversal.getErrors();
    assertThat(errors).hasSize(1);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String[]> messageArgumentCaptor =
        ArgumentCaptor.forClass(String[].class);

    when(compiler.report(scopedAliases.makeError(eq(traversal), any(Node.class),
        eq(diagTypeArgumentCaptor.capture()), any(String[].class))))
        .thenReturn(null);

    scopedAliases.process(null, rootNode);

    // Verify
    Node errorNode = traversal.getErrors().get(0);
    assertThat(diagTypeArgumentCaptor.getValue()).isEqualTo(GOOG_SCOPE_USED_IMPROPERLY);
    String[] message = messageArgumentCaptor.getValue();
    assertThat(message[0]).isEqualTo("goog.scope must be alone in a single statement.");
  }

  @Test
  public void testGoogScopeWithBadParameters() {
    // Given
    Node rootNode = createNode(
        "goog.scope(function(a) {",
        "});");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> errors = traversal.getErrors();
    assertThat(errors).hasSize(1);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String[]> messageArgumentCaptor =
        ArgumentCaptor.forClass(String[].class);

    when(compiler.report(scopedAliases.makeError(eq(traversal), any(Node.class),
        eq(diagTypeArgumentCaptor.capture()), any(String[].class))))
        .thenReturn(null);

    scopedAliases.process(null, rootNode);

    // Verify
    Node errorNode = traversal.getErrors().get(0);
    assertThat(diagTypeArgumentCaptor.getValue()).isEqualTo(GOOG_SCOPE_HAS_BAD_PARAMETERS);
    String[] message = messageArgumentCaptor.getValue();
    assertThat(message[0]).isEqualTo("The call to goog.scope must take only a single parameter. It must "
        + "be an anonymous function that itself takes no parameters.");
  }

  @Test
  public void testGoogScopeReferencesThis() {
    // Given
    Node rootNode = createNode(
        "goog.scope(function() {",
        "  this.x = 1;",
        "});");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> errors = traversal.getErrors();
    assertThat(errors).hasSize(1);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String[]> messageArgumentCaptor =
        ArgumentCaptor.forClass(String[].class);

    when(compiler.report(scopedAliases.makeError(eq(traversal), any(Node.class),
        eq(diagTypeArgumentCaptor.capture()), any(String[].class))))
        .thenReturn(null);

    scopedAliases.process(null, rootNode);

    // Verify
    Node errorNode = traversal.getErrors().get(0);
    assertThat(diagTypeArgumentCaptor.getValue()).isEqualTo(GOOG_SCOPE_REFERENCES_THIS);
    String[] message = messageArgumentCaptor.getValue();
    assertThat(message[0]).isEqualTo("The body of a goog.scope function cannot reference 'this'.");
  }

  @Test
  public void testGoogScopeUsesReturn() {
    // Given
    Node rootNode = createNode(
        "goog.scope(function() {",
        "  return 1;",
        "});");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> errors = traversal.getErrors();
    assertThat(errors).hasSize(1);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String[]> messageArgumentCaptor =
        ArgumentCaptor.forClass(String[].class);

    when(compiler.report(scopedAliases.makeError(eq(traversal), any(Node.class),
        eq(diagTypeArgumentCaptor.capture()), any(String[].class))))
        .thenReturn(null);

    scopedAliases.process(null, rootNode);

    // Verify
    Node errorNode = traversal.getErrors().get(0);
    assertThat(diagTypeArgumentCaptor.getValue()).isEqualTo(GOOG_SCOPE_USES_RETURN);
    String[] message = messageArgumentCaptor.getValue();
    assertThat(message[0]).isEqualTo("The body of a goog.scope function cannot use 'return'.");
  }

  @Test
  public void testGoogScopeUsesThrow() {
    // Given
    Node rootNode = createNode(
        "goog.scope(function() {",
        "  throw new Error('Error occurred');",
        "});");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> errors = traversal.getErrors();
    assertThat(errors).hasSize(1);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String[]> messageArgumentCaptor =
        ArgumentCaptor.forClass(String[].class);

    when(compiler.report(scopedAliases.makeError(eq(traversal), any(Node.class),
        eq(diagTypeArgumentCaptor.capture()), any(String[].class))))
        .thenReturn(null);

    scopedAliases.process(null, rootNode);

    // Verify
    Node errorNode = traversal.getErrors().get(0);
    assertThat(diagTypeArgumentCaptor.getValue()).isEqualTo(GOOG_SCOPE_USES_THROW);
    String[] message = messageArgumentCaptor.getValue();
    assertThat(message[0]).isEqualTo("The body of a goog.scope function cannot use 'throw'.");
  }

  @Test
  public void testAliasRedefined() {
    // Given
    Node rootNode = createNode(
        "goog.scope(function() {",
        "  var x = goog.x;",
        "  var x = 1;",
        "});");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> errors = traversal.getErrors();
    assertThat(errors).hasSize(1);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String[]> messageArgumentCaptor =
        ArgumentCaptor.forClass(String[].class);

    when(compiler.report(scopedAliases.makeError(eq(traversal), any(Node.class),
        eq(diagTypeArgumentCaptor.capture()), any(String[].class))))
        .thenReturn(null);

    scopedAliases.process(null, rootNode);

    // Verify
    Node errorNode = traversal.getErrors().get(0);
    assertThat(diagTypeArgumentCaptor.getValue()).isEqualTo(GOOG_SCOPE_ALIAS_REDEFINED);
    String[] message = messageArgumentCaptor.getValue();
    assertThat(message[0]).isEqualTo("The alias x is assigned a value more than once.");
  }

  @Test
  public void testNonAliasLocalInScopedBlock() {
    // Given
    Node rootNode = createNode(
        "goog.scope(function() {",
        "  var x = 1;",
        "});");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Traversal traversal = scopedAliases.getTraversal();
    List<Node> errors = traversal.getErrors();
    assertThat(errors).hasSize(1);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String[]> messageArgumentCaptor =
        ArgumentCaptor.forClass(String[].class);

    when(compiler.report(scopedAliases.makeError(eq(traversal), any(Node.class),
        eq(diagTypeArgumentCaptor.capture()), any(String[].class))))
        .thenReturn(null);

    scopedAliases.process(null, rootNode);

    // Verify
    Node errorNode = traversal.getErrors().get(0);
    assertThat(diagTypeArgumentCaptor.getValue()).isEqualTo(GOOG_SCOPE_NON_ALIAS_LOCAL);
    String[] message = messageArgumentCaptor.getValue();
    assertThat(message[0]).isEqualTo("The local variable x is in a goog.scope and is not an alias.");
  }

  private Node createNode(String... nodes) {
    Node rootNode = new Node() {};
    for (String nodeStr : nodes) {
      Node node = new Node() {};
      node.setString(nodeStr);
      rootNode.addChild(node);
    }
    return rootNode;
  }

  private DiagnosticType GOOG_SCOPE_USED_IMPROPERLY = new DiagnosticType() {};
  private DiagnosticType GOOG_SCOPE_HAS_BAD_PARAMETERS = new DiagnosticType() {};
  private DiagnosticType GOOG_SCOPE_REFERENCES_THIS = new DiagnosticType() {};
  private DiagnosticType GOOG_SCOPE_USES_RETURN = new DiagnosticType() {};
  private DiagnosticType GOOG_SCOPE_USES_THROW = new DiagnosticType() {};
  private DiagnosticType GOOG_SCOPE_ALIAS_REDEFINED = new DiagnosticType() {};
  private DiagnosticType GOOG_SCOPE_NON_ALIAS_LOCAL = new DiagnosticType() {};
}