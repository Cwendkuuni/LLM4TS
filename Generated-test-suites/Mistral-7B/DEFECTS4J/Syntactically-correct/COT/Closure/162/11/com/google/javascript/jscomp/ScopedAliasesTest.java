import static com.google.common.truth.Truth.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TypeChecker.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

/**
 * Test for ScopedAliases class.
 */
public class ScopedAliasesTest {
  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.getPreprocessorSymbolTable()).thenReturn(preprocessorSymbolTable);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_NoScopedAlias_NoError() {
    Node root = Node.newTextNode("// No scoped alias.");

    scopedAliases.process(null, root);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testProcess_EmptyExterns_NoError() {
    Node root = Node.newTextNode("// Empty externs.");

    scopedAliases.process(Node.newObjectLiteralNode(), root);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testProcess_WithScopedAlias_NoError() {
    Node root = Node.newTextNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});");

    scopedAliases.process(Node.newObjectLiteralNode(), root);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testProcess_WithInvalidScopedAlias_Error() {
    Node root = Node.newTextNode("goog.scope(function() {" +
        "goog.scope(function() {});" +
        "});");

    doThrow(IllegalArgumentException.class)
        .when(compiler)
        .report(any(), eq(GOOG_SCOPE_USED_IMPROPERLY));

    scopedAliases.process(Node.newObjectLiteralNode(), root);

    verify(compiler).report(eq(root), eq(GOOG_SCOPE_USED_IMPROPERLY));
  }

  @Test
  public void testProcess_WithScopedAliasWithBadParameter_Error() {
    Node root = Node.newTextNode("goog.scope(function(x) {" +
        "goog.scope(function() {});" +
        "});");

    doThrow(IllegalArgumentException.class)
        .when(compiler)
        .report(any(), eq(GOOG_SCOPE_HAS_BAD_PARAMETERS));

    scopedAliases.process(Node.newObjectLiteralNode(), root);

    verify(compiler).report(eq(root), eq(GOOG_SCOPE_HAS_BAD_PARAMETERS));
  }

  @Test
  public void testProcess_WithScopedAliasWithNonAnonymousFunction_Error() {
    Node root = Node.newTextNode("goog.scope(function foo() {" +
        "goog.scope(function() {});" +
        "});");

    doThrow(IllegalArgumentException.class)
        .when(compiler)
        .report(any(), eq(GOOG_SCOPE_REFERENCES_THIS));

    scopedAliases.process(Node.newObjectLiteralNode(), root);

    verify(compiler).report(eq(root), eq(GOOG_SCOPE_REFERENCES_THIS));
  }

  @Test
  public void testProcess_WithScopedAliasWithReturnStatement_Error() {
    Node root = Node.newTextNode("goog.scope(function() {" +
        "return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});");

    doThrow(IllegalStateException.class)
        .when(compiler)
        .reportCodeChange();

    doThrow(IllegalArgumentException.class)
        .when(compiler)
        .report(any(), eq(GOOG_SCOPE_USES_RETURN));

    scopedAliases.process(Node.newObjectLiteralNode(), root);

    verify(compiler).reportCodeChange();
    verify(compiler).report(eq(root), eq(GOOG_SCOPE_USES_RETURN));
  }

  @Test
  public void testProcess_WithScopedAliasWithThrowStatement_Error() {
    Node root = Node.newTextNode("goog.scope(function() {" +
        "throw new Error();" +
        "});");

    doThrow(IllegalStateException.class)
        .when(compiler)
        .reportCodeChange();

    doThrow(IllegalArgumentException.class)
        .when(compiler)
        .report(any(), eq(GOOG_SCOPE_USES_THROW));

    scopedAliases.process(Node.newObjectLiteralNode(), root);

    verify(compiler).reportCodeChange();
    verify(compiler).report(eq(root), eq(GOOG_SCOPE_USES_THROW));
  }

  @Test
  public void testProcess_WithScopedAliasWithAliasRedefined_Error() {
    Node root = Node.newTextNode("goog.scope(function() {" +
        "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = goog.dom.TagName.DIV;" +
        "goog.dom.createElement(DIV);" +
        "var DIV = 1;" +
        "});" +
        "});");

    doThrow(IllegalArgumentException.class)
        .when(compiler)
        .report(any(), eq(GOOG_SCOPE_ALIAS_REDEFINED));

    scopedAliases.process(Node.newObjectLiteralNode(), root);

    verify(compiler).report(eq(root), eq(GOOG_SCOPE_ALIAS_REDEFINED));
  }

  @Test
  public void testProcess_WithScopedAliasWithNonAliasLocal_Error() {
    Node root = Node.newTextNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var foo = 1;" +
        "goog.scope(function() {" +
        "goog.scope.get('dom');" +
        "});" +
        "});");

    doThrow(IllegalArgumentException.class)
        .when(compiler)
        .report(any(), eq(GOOG_SCOPE_NON_ALIAS_LOCAL));

    scopedAliases.process(Node.newObjectLiteralNode(), root);

    verify(compiler).report(eq(root), eq(GOOG_SCOPE_NON_ALIAS_LOCAL));
  }

  @Test
  public void testHotSwapScript_NoChange_NoError() {
    Node root = Node.newTextNode("// No change.");

    scopedAliases.hotSwapScript(root, null);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testHotSwapScript_WithChange_ApplyAliases() {
    Node root = Node.newTextNode("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope.get('dom').createElement(goog.dom.TagName.DIV);" +
        "});");

    Node expectedRoot = Node.newTextNode("goog.dom.createElement(goog.dom.TagName.DIV);");

    doReturn(new AliasTransformation())
        .when(transformationHandler)
        .logAliasTransformation(eq("srcFile"), any());

    scopedAliases.hotSwapScript(root, null);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Node aliasUsage = aliasUsages.get(0);
    Node scopeCall = scopeCalls.get(0);

    assertEquals("dom", aliasDefinition.getString());
    assertEquals(Type.VAR, aliasDefinition.getType());

    assertEquals("goog.dom", aliasUsage.getAliasedNode().getString());
    assertEquals("goog.dom.TagName.DIV", aliasUsage.getAliasedTypeNode().getCorrectedType());

    assertEquals("goog.scope.get", scopeCall.getString());
    assertEquals(Type.CALL, scopeCall.getType());

    assertEquals(expectedRoot, root);
  }
}