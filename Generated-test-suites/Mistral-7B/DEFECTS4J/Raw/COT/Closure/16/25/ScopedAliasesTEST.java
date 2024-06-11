import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {
  /** The mock compiler. */
  private AbstractCompiler compiler;

  /** The mock preprocessor symbol table. */
  private PreprocessorSymbolTable preprocessorSymbolTable;

  /** The mock alias transformation handler. */
  private AliasTransformationHandler transformationHandler;

  /** The instance of the ScopedAliases under test. */
  private ScopedAliases scopedAliases;

  /** The diagnostic type for goog.scope used improperly error. */
  private static final DiagnosticType GOOG_SCOPE_USED_IMPROPERLY = DiagnosticType.error(
      "JSC_GOOG_SCOPE_USED_IMPROPERLY",
      "The call to goog.scope must be alone in a single statement.");

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    MockitoAnnotations.initMocks(this);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_EmptyScript() {
    Node root = Node.createTree();
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_ValidScopeCall() {
    String code = "goog.scope(function() {\n" +
        "  var x = goog.foo;\n" +
        "});";

    Node root = Node.getRootNode(code);
    scopedAliases.process(null, root);
    assertFalse(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
  }

  @Test
  public void testProcess_InvalidScopeCall_MultipleStatements() {
    String code = "goog.scope(function() {\n" +
        "  var x = goog.foo;\n" +
        "  goog.scope(function() {\n" +
        "  });\n" +
        "});";

    Node root = Node.getRootNode(code);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, scopedAliases.getDiagnostics().get(0).getType());
  }

  @Test
  public void testProcess_InvalidScopeCall_WrongParameters() {
    String code = "goog.scope(\n" +
        "  function(a) {\n" +
        "  });";

    Node root = Node.getRootNode(code);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, scopedAliases.getDiagnostics().get(0).getType());
  }

  @Test
  public void testProcess_InvalidScopeCall_ThrowsException() {
    String code = "goog.scope(function() {\n" +
        "  throw new Error('Exception');\n" +
        "});";

    Node root = Node.getRootNode(code);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_USES_THROW, scopedAliases.getDiagnostics().get(0).getType());
  }

  @Test
  public void testProcess_InvalidScopeCall_RefersToThis() {
    String code = "goog.scope(function() {\n" +
        "  this.x = goog.foo;\n" +
        "});";

    Node root = Node.getRootNode(code);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, scopedAliases.getDiagnostics().get(0).getType());
  }

  @Test
  public void testProcess_InvalidScopeCall_UsesReturn() {
    String code = "goog.scope(function() {\n" +
        "  return 'Hello World';\n" +
        "});";

    Node root = Node.getRootNode(code);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_USES_RETURN, scopedAliases.getDiagnostics().get(0).getType());
  }

  @Test
  public void testProcess_AliasRedefined() {
    String code = "goog.scope(function() {\n" +
        "  var x = goog.foo;\n" +
        "  var x = goog.bar;\n" +
        "});";

    Node root = Node.getRootNode(code);
    AliasTransformation aliasTransformation = mock(AliasTransformation.class);
    when(transformationHandler.logAliasTransformation(
        anyString(), any(SourcePosition.class)))
        .thenReturn(aliasTransformation);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, scopedAliases.getDiagnostics().get(0).getType());
  }

  @Test
  public void testProcess_NonAliasLocal() {
    String code = "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "});";

    Node root = Node.getRootNode(code);
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, scopedAliases.getDiagnostics().get(0).getType());
  }

  @Test
  public void testHotSwapScript() {
    String code = "goog.scope(function() {\n" +
        "  var x = goog.foo;\n" +
        "});";

    Node root = Node.getRootNode(code);
    Node originalRoot = Node.getRootNode(code);
    scopedAliases.hotSwapScript(root, originalRoot);
    assertFalse(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
  }

  @Test
  public void testHotSwapScript_WithErrors() {
    String code = "goog.scope(function() {\n" +
        "  goog.scope(function() {});\n" +
        "});";

    Node root = Node.getRootNode(code);
    Node originalRoot = Node.getRootNode(code);
    scopedAliases.hotSwapScript(root, originalRoot);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, scopedAliases.getDiagnostics().get(0).getType());
  }
}