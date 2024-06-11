import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;
import static com.google.javascript.rhino.Node.*;
import static com.google.javascript.rhino.SourcePosition.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ScopedAliasesTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  @InjectMocks
  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testScopedAliases_noAlias() {
    // Given
    Node root = createNode("function main() {}", "main.js");

    // When
    scopedAliases.process(null, root);

    // Then
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
  }

  @Test
  public void testScopedAliases_simpleAlias() {
    // Given
    Node root = createNode(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "})()", "main.js");

    // When
    scopedAliases.process(null, root);

    // Then
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.VAR, aliasDefinition.getType());
    assertEquals("dom", aliasDefinition.getString());
    assertEquals(Token.FUNCTION, aliasDefinition.getFirstChild().getType());
    assertEquals("goog.dom", aliasDefinition.getFirstChild().getQualifiedName());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    AliasUsage aliasUsage = aliasUsages.get(0);
    Node aliasReference = aliasUsage.getAliasReference();
    assertEquals(Token.NAME, aliasReference.getType());
    assertEquals("dom", aliasReference.getString());
    Node aliasDefinitionInUse = aliasUsage.getAliasDefinition();
    assertEquals(aliasDefinition, aliasDefinitionInUse);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());
    assertEquals(1, scopeCall.getChildCount());
    assertEquals(Token.FUNCTION, scopeCall.getChildAtIndex(0).getType());
  }

  @Test
  public void testScopedAliases_aliasRedefinition() {
    // Given
    Node root = createNode(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var dom = goog.dom;" +
        "})()", "main.js");

    // When & Then
    assertFalse(scopedAliases.process(null, root));
    assertFalse(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());

    List<DiagnosticType> diagnostics = scopedAliases.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnostic.getType());
  }

  @Test
  public void testScopedAliases_invalidScopeCall() {
    // Given
    Node root = createNode(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  goog.scope(function() {});" +
        "})()", "main.js");

    // When & Then
    assertFalse(scopedAliases.process(null, root));
    assertFalse(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());

    List<DiagnosticType> diagnostics = scopedAliases.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnostic.getType());
  }

  @Test
  public void testScopedAliases_invalidScopeCallWithParameters() {
    // Given
    Node root = createNode(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  goog.scope(function(x) {}, 1);" +
        "})()", "main.js");

    // When & Then
    assertFalse(scopedAliases.process(null, root));
    assertFalse(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());

    List<DiagnosticType> diagnostics = scopedAliases.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnostic.getType());
  }

  @Test
  public void testScopedAliases_invalidScopeCallWithReturn() {
    // Given
    Node root = createNode(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  return goog.dom.createElement(DIV);" +
        "})()", "main.js");

    // When & Then
    assertFalse(scopedAliases.process(null, root));
    assertFalse(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());

    List<DiagnosticType> diagnostics = scopedAliases.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnostic.getType());
  }

  @Test
  public void testScopedAliases_invalidScopeCallWithThrow() {
    // Given
    Node root = createNode(
        "goog.scope(function() {" +
        "  try {" +
        "    goog.scope(function() {});" +
        "  } catch (e) {" +
        "    throw e;" +
        "  }" +
        "})()", "main.js");

    // When & Then
    assertFalse(scopedAliases.process(null, root));
    assertFalse(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());

    List<DiagnosticType> diagnostics = scopedAliases.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(GOOG_SCOPE_USES_THROW, diagnostic.getType());
  }

  @Test
  public void testScopedAliases_aliasReferenceInsideScope() {
    // Given
    Node root = createNode(
        "goog.scope(function() {" +
        "  function test() {" +
        "    var dom = goog.dom;" +
        "    goog.scope(function() {" +
        "      var dom = goog.dom;" +
        "    });" +
        "    goog.dom.createElement(DIV);" +
        "  }" +
        "})()", "main.js");

    // When & Then
    assertTrue(scopedAliases.process(null, root));
    assertFalse(scopedAliases.hasErrors());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    Node scopeCall = scopeCalls.get(0);
    assertEquals(Token.CALL, scopeCall.getType());
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());
    assertEquals(1, scopeCall.getChildCount());
    assertEquals(Token.FUNCTION, scopeCall.getChildAtIndex(0).getType());

    List<DiagnosticType> diagnostics = scopedAliases.getDiagnostics();
    assertEquals(0, diagnostics.size());
  }

  @Test
  public void testScopedAliases_multipleAliases() {
    // Given
    Node root = createNode(
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var link = goog.dom.createElement(DIV);" +
        "})()", "main.js");

    // When
    scopedAliases.process(null, root);

    // Then
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    Node aliasDefinitionDom = aliasDefinitionsInOrder.get(0);
    assertEquals(Token.VAR, aliasDefinitionDom.getType());
    assertEquals("dom", aliasDefinitionDom.getString());
    assertEquals(Token.FUNCTION, aliasDefinitionDom.getFirstChild().getType());
    assertEquals("goog.dom", aliasDefinitionDom.getFirstChild().getQualifiedName());

    Node aliasDefinitionDiv = aliasDefinitionsInOrder.get(1);
    assertEquals(Token.VAR, aliasDefinitionDiv.getType());
    assertEquals("DIV", aliasDefinitionDiv.getString());
    assertEquals(Token.NAME, aliasDefinitionDiv.getFirstChild().getType());
    assertEquals("TagName", aliasDefinitionDiv.getFirstChild().getString());
    assertEquals(Token.DOT, aliasDefinitionDiv.getFirstChild().getLastChild().getType());
    assertEquals("DIV", aliasDefinitionDiv.getFirstChild().getLastChild().getString());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    AliasUsage aliasUsageDom = aliasUsages.get(0);
    Node aliasReferenceDom = aliasUsageDom.getAliasReference();
    assertEquals(Token.NAME, aliasReferenceDom.getType());
    assertEquals("dom", aliasReferenceDom.getString());
    Node aliasDefinitionDomInUse = aliasUsageDom.getAliasDefinition();
    assertEquals(aliasDefinitionDom, aliasDefinitionDomInUse);

    AliasUsage aliasUsageDiv = aliasUsages.get(1);
    Node aliasReferenceDiv = aliasUsageDiv.getAliasReference();
    assertEquals(Token.NAME, aliasReferenceDiv.getType());
    assertEquals("DIV", aliasReferenceDiv.getString());
    Node aliasDefinitionDivInUse = aliasUsageDiv.getAliasDefinition();
    assertEquals(aliasDefinitionDiv, aliasDefinitionDivInUse);
  }

  private Node createNode(String code, String fileName) {
    Node root = compiler.parseTree(code, fileName, null, false);
    Mockito.when(compiler.getSourceFileName(root)).thenReturn(fileName);
    return root;
  }
}