import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess() {
    // Given
    Node root = createTestNode();

    // When
    scopedAliases.process(null, root);

    // Then
    verifyNoErrors();
  }

  @Test
  public void testHotSwapScript() {
    // Given
    Node root = createTestNode();

    // When
    scopedAliases.hotSwapScript(root, null);

    // Then
    verifyNoErrors();
  }

  @Test
  public void testApplyAlias() {
    // Given
    Node aliasReference = createAliasReferenceNode();
    Node aliasDefinition = createAliasDefinitionNode("dom", "goog.dom");

    AliasUsage aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);

    // When
    aliasUsage.applyAlias();

    // Then
    assertEquals("goog.dom", aliasReference.getString());
  }

  @Test
  public void testApplyAliasType() {
    // Given
    Node aliasReference = createAliasReferenceTypeNode("goog.dom.TagName.DIV");
    String correctedType = "com.google.javascript.rhino.Node.TAG_DIV";

    AliasUsage aliasUsage = new ScopedAliases.AliasedTypeNode(aliasReference, correctedType);

    // When
    aliasUsage.applyAlias();

    // Then
    assertEquals(correctedType, aliasReference.getString());
  }

  @Test(expected = AssertionError.class)
  public void testApplyAliasTwice() {
    // Given
    Node aliasReference = createAliasReferenceNode();
    Node aliasDefinition = createAliasDefinitionNode("dom", "goog.dom");

    AliasUsage aliasUsage = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);
    aliasUsage.applyAlias();

    // When
    aliasUsage.applyAlias();

    // Then
    fail("Alias should not be applied twice.");
  }

  @Test
  public void testAliasRedefined() {
    // Given
    Node aliasReference1 = createAliasReferenceNode();
    Node aliasDefinition1 = createAliasDefinitionNode("dom1", "goog.dom1");

    AliasUsage aliasUsage1 = new ScopedAliases.AliasedNode(aliasReference1, aliasDefinition1);

    Node aliasReference2 = createAliasReferenceNode();
    Node aliasDefinition2 = createAliasDefinitionNode("dom1", "goog.dom2");

    AliasUsage aliasUsage2 = new ScopedAliases.AliasedNode(aliasReference2, aliasDefinition2);

    // When
    List<Node> aliasDefinitionsInOrder = Lists.newArrayList(aliasDefinition1, aliasDefinition2);
    scopedAliases.getAliasDefinitionsInOrder().clear();
    scopedAliases.getAliasDefinitionsInOrder().addAll(aliasDefinitionsInOrder);

    // Then
    compileAndExpectError(GOOG_SCOPE_ALIAS_REDEFINED, "dom1");
  }

  @Test
  public void testNonAliasLocalVariable() {
    // Given
    Node aliasReference = createAliasReferenceNode();
    Node aliasDefinition = createAliasDefinitionNode("dom", "goog.dom");

    Node localVariable = createVariableNode("local");

    Node root = createRootNode(aliasReference, localVariable);

    // When
    List<Node> aliasDefinitionsInOrder = Lists.newArrayList(aliasDefinition);
    scopedAliases.getAliasDefinitionsInOrder().clear();
    scopedAliases.getAliasDefinitionsInOrder().addAll(aliasDefinitionsInOrder);

    // Then
    compileAndExpectError(GOOG_SCOPE_NON_ALIAS_LOCAL, "local");
  }

  @Test
  public void testScopeCallWithArguments() {
    // Given
    Node scopeCall = createScopeCallNodeWithArguments("func", Node.newList(createNumberNode(1), createNumberNode(2)));

    // When
    List<Node> scopeCalls = Lists.newArrayList(scopeCall);
    scopedAliases.getScopeCalls().clear();
    scopedAliases.getScopeCalls().addAll(scopeCalls);

    // Then
    verifyNoErrors();
  }

  @Test
  public void testScopeCallWithoutArguments() {
    // Given
    Node scopeCall = createScopeCallNode();

    // When
    List<Node> scopeCalls = Lists.newArrayList(scopeCall);
    scopedAliases.getScopeCalls().clear();
    scopedAliases.getScopeCalls().addAll(scopeCalls);

    // Then
    verifyNoErrors();
  }

  @Test
  public void testScopeCallWithInvalidFunction() {
    // Given
    Node scopeCall = createNode("{ invalidFunction: function() {} }");

    // When
    List<Node> scopeCalls = Lists.newArrayList(scopeCall);
    scopedAliases.getScopeCalls().clear();
    scopedAliases.getScopeCalls().addAll(scopeCalls);

    // Then
    compileAndExpectError(GOOG_SCOPE_USED_IMPROPERLY);
  }

  @Test
  public void testScopeCallWithInvalidParameter() {
    // Given
    Node scopeCall = createScopeCallNodeWithArguments("func", Node.newList(createInvalidNode()));

    // When
    List<Node> scopeCalls = Lists.newArrayList(scopeCall);
    scopedAliases.getScopeCalls().clear();
    scopedAliases.getScopeCalls().addAll(scopeCalls);

    // Then
    compileAndExpectError(GOOG_SCOPE_HAS_BAD_PARAMETERS);
  }

  @Test
  public void testScopeCallWithReferenceToThis() {
    // Given
    Node scopeCall = createScopeCallNode();
    Node thisReference = createNode("this");
    Node referenceToThis = createReferenceNode(thisReference);

    Node root = createRootNode(referenceToThis);

    // When
    List<Node> scopeCalls = Lists.newArrayList(scopeCall);
    scopedAliases.getScopeCalls().clear();
    scopedAliases.getScopeCalls().addAll(scopeCalls);

    // Then
    compileAndExpectError(GOOG_SCOPE_REFERENCES_THIS, "this");
  }

  @Test
  public void testScopeCallWithReturnStatement() {
    // Given
    Node scopeCall = createScopeCallNode();
    Node returnStatement = createReturnStatementNode();

    Node root = createRootNode(returnStatement);

    // When
    List<Node> scopeCalls = Lists.newArrayList(scopeCall);
    scopedAliases.getScopeCalls().clear();
    scopedAliases.getScopeCalls().addAll(scopeCalls);

    // Then
    compileAndExpectError(GOOG_SCOPE_USES_RETURN);
  }

  @Test
  public void testScopeCallWithThrowStatement() {
    // Given
    Node scopeCall = createScopeCallNode();
    Node throwStatement = createThrowStatementNode();

    Node root = createRootNode(throwStatement);

    // When
    List<Node> scopeCalls = Lists.newArrayList(scopeCall);
    scopedAliases.getScopeCalls().clear();
    scopedAliases.getScopeCalls().addAll(scopeCalls);

    // Then
    compileAndExpectError(GOOG_SCOPE_USES_THROW);
  }

  private Node createTestNode() {
    return NodeOperations.parseJson("{}");
  }

  private Node createAliasReferenceNode() {
    return createNameNode("aliasReference");
  }

  private Node createAliasDefinitionNode(String name, String value) {
    return NodeOperations.parseJson("{\"name\":\"" + name + "\",\"value\":{" + value + "}}");
  }

  private Node createVariableNode(String name) {
    return NodeOperations.parseJson("{\"type\":\"VariableDeclaration\",\"kind\":\"var\",\"name\":\"" + name + "\",\"value\":null,\"initializer\":null}");
  }

  private Node createScopeCallNode() {
    return NodeOperations.parseJson("goog.scope(function() {})");
  }

  private Node createScopeCallNodeWithArguments(String name, NodeList arguments) {
    return NodeOperations.parseJson("goog.scope(function(" + name + ") {" + arguments.toString() + "})");
  }

  private Node createInvalidNode() {
    return NodeOperations.parseJson("invalid");
  }

  private Node createNameNode(String name) {
    return NodeOperations.parseJson("{" + name + "}");
  }

  private Node createReferenceNode(Node reference) {
    return NodeOperations.parseJson("&" + reference.toString());
  }

  private Node createReturnStatementNode() {
    return NodeOperations.parseJson("return;");
  }

  private Node createThrowStatementNode() {
    return NodeOperations.parseJson("throw new Error(\"Test error\");");
  }

  private Node createRootNode(Node... nodes) {
    return NodeOperations.parseJson("{" + String.join("", Arrays.asList(Arrays.toString(nodes))) + "}");
  }

  private void compileAndExpectError(DiagnosticType errorType, String... messageParts) {
    Node root = createTestNode();
    for (Node n : scopedAliases.getAliasDefinitionsInOrder()) {
      root = NodeOperations.appendChild(root, n);
    }
    for (Node n : scopedAliases.getScopeCalls()) {
      root = NodeOperations.appendChild(root, n);
    }
    for (AliasUsage aliasUsage : scopedAliases.getAliasUsages()) {
      root = NodeOperations.appendChild(root, aliasUsage.aliasReference);
    }

    compiler.process(null, root);

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticInfo diagnostic = compiler.getDiagnostics().get(0);
    assertNotNull(diagnostic);
    assertEquals(errorType, diagnostic.getType());
    assertArrayEquals(messageParts, diagnostic.getMessageArguments());
  }

  private void verifyNoErrors() {
    assertEquals(0, compiler.getDiagnostics().size());
  }
}