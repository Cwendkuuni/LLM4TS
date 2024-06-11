import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

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
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_rootNodeIsEmpty_noErrorsReported() {
    Node root = Node.nullLiteral();
    scopedAliases.process(null, root);
    assertFalse(compiler.hasErrors());
  }

  @Test
  public void testProcess_rootNodeWithGoogScope_aliasesAppliedAndErrorsReported() {
    Node root = getNode("goog.scope(function() { var x = goog.x; });");
    scopedAliases.process(null, root);

    ArgumentCaptor<Node> aliasDefinitionCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<AliasTransformation> transformationCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);

    List<Node> aliasDefinitions = transformationHandler.getAliasDefinitions();
    assertTrue(aliasDefinitions.size() > 0);
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals("x", aliasDefinition.getFirstChild().getString());
    assertEquals("goog.x", aliasDefinition.getLastChild().getString());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertTrue(aliasUsages.size() > 0);
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertEquals("goog.x", aliasUsage.getClass().getSimpleName());
    ((AliasedNode) aliasUsage).applyAlias();

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<DiagnosticType> errorTypeCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<Object[]> errorArgumentsCaptor =
        ArgumentCaptor.forClass(Object[].class);

    when(compiler.report(ArgumentCaptor.isA(NodeTraversal.class),
        ArgumentCaptor.isA(Node.class), ArgumentCaptor.isA(DiagnosticType.class),
        (Object[]) ArgumentCaptor.isA(Object[].class))).thenReturn(null);

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_ALIAS_REDEFINED, ArgumentCaptor.isA(String.class)))
        .thenReturn(null);

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_NON_ALIAS_LOCAL, ArgumentCaptor.isA(String.class)))
        .thenReturn(null);

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_USES_RETURN)).thenReturn(null);

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_REFERENCES_THIS)).thenReturn(null);

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_USES_THROW)).thenReturn(null);

    Node rootAfterAliasApplication = root.cloneTree();
    when(compiler.reportCodeChange()).thenReturn(null);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertTrue(scopeCalls.size() > 0);
    assertEquals("goog.scope", scopeCalls.get(0).getFirstChild().getQualifiedName());

    when(preprocessorSymbolTable.addReference(ArgumentCaptor.isA(Node.class)))
        .thenReturn(null);

    scopedAliases.process(null, root);

    when(compiler.report(ArgumentCaptor.isA(NodeTraversal.class),
        ArgumentCaptor.isA(Node.class), ArgumentCaptor.isA(DiagnosticType.class),
        (Object[]) ArgumentCaptor.isA(Object[].class))).thenThrow(RuntimeException.class);

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_USES_RETURN)).thenReturn(new DiagnosticPosition(rootAfterAliasApplication, 1, 1));

    scopedAliases.process(null, root);

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_REFERENCES_THIS)).thenReturn(new DiagnosticPosition(rootAfterAliasApplication, 1, 5));

    scopedAliases.process(null, root);

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_USES_THROW)).thenReturn(new DiagnosticPosition(rootAfterAliasApplication, 1, 8));

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_ALIAS_REDEFINED, ArgumentCaptor.isA(String.class)))
        .thenReturn(new DiagnosticPosition(rootAfterAliasApplication, 1, 11));

    when(compiler.report(mock(NodeTraversal.class), ArgumentCaptor.isA(Node.class),
        GOOG_SCOPE_NON_ALIAS_LOCAL, ArgumentCaptor.isA(String.class)))
        .thenReturn(new DiagnosticPosition(rootAfterAliasApplication, 1, 15));

    when(compiler.reportCodeChange()).thenReturn(new DiagnosticPosition(root, 1, 1));

    Node rootAfterErrors = rootAfterAliasApplication.cloneTree();

    scopedAliases.process(null, root);

    when(compiler.report(ArgumentCaptor.isA(NodeTraversal.class),
        ArgumentCaptor.isA(Node.class), ArgumentCaptor.isA(DiagnosticType.class),
        (Object[]) ArgumentCaptor.isA(Object[].class)))
        .thenThrow(RuntimeException.class);

    scopedAliases.process(null, rootAfterErrors);

    when(compiler.report(ArgumentCaptor.isA(NodeTraversal.class),
        ArgumentCaptor.isA(Node.class), GOOG_SCOPE_USED_IMPROPERLY,
        (Object[]) ArgumentCaptor.isA(Object[].class)))
        .thenReturn(new DiagnosticPosition(rootAfterErrors, 1, 1));

    scopedAliases.process(null, rootAfterErrors);

    when(compiler.report(mock(NodeTraversal.class),
        ArgumentCaptor.isA(Node.class), GOOG_SCOPE_HAS_BAD_PARAMETERS))
        .thenReturn(new DiagnosticPosition(rootAfterErrors, 1, 5));

    scopedAliases.process(null, rootAfterErrors);

    Node expectedResult = getNode("goog.x;");
    assertEquals(expectedResult, rootAfterErrors);
  }

  @Test
  public void testProcess_rootNodeContainsFunctionWithMultipleStatements_googScopeIsIgnored() {
    Node root = getNode("goog.scope(function() { var x = goog.x; goog.x = 10; });");
    scopedAliases.process(null, root);
    assertFalse(compiler.hasErrors());
    assertEquals("goog.x;", root.toString());
  }

  @Test
  public void testProcess_rootNodeContainsFunctionWithMultipleStatements_googScopeIsNotIgnored() {
    Node root = getNode("goog.scope(function() { var x = goog.x; goog.scope(function() { goog.x = 10; }); });");
    scopedAliases.process(null, root);
    assertFalse(compiler.hasErrors());
    assertEquals("goog.x;", root.toString());
  }

  @Test
  public void testProcess_rootNodeContainsFunctionWithMultipleStatements_googScopeContainsGoogScope_recursionLimitation() {
    Node root = getNode("goog.scope(function() { var x = goog.x; goog.scope(function() { goog.scope(function() { goog.x = 10; }); }); });");
    scopedAliases.process(null, root);
    assertTrue(compiler.hasErrors());
  }

  @Test
  public void testProcess_rootNodeContainsFunctionWithMultipleStatements_googScopeContainsReturnStatement_returnStatementIsRemoved() {
    Node root = getNode("goog.scope(function() { var x = goog.x; return goog.x; });");
    scopedAliases.process(null, root);
    assertFalse(compiler.hasErrors());
    assertEquals("goog.x;", root.toString());
  }

  @Test
  public void testProcess_rootNodeContainsFunctionWithMultipleStatements_googScopeContainsThrowStatement_throwStatementIsRemoved() {
    Node root = getNode("goog.scope(function() { var x = goog.x; throw new Error(); });");
    scopedAliases.process(null, root);
    assertFalse(compiler.hasErrors());
    assertEquals("goog.x;", root.toString());
  }

  @Test
  public void testProcess_rootNodeContainsFunctionWithMultipleStatements_googScopeContainsThisKeyword_thisKeywordIsReplacedWithWindow() {
    Node root = getNode("goog.scope(function() { this.x = 10; });");
    scopedAliases.process(null, root);
    assertFalse(compiler.hasErrors());
    assertEquals("window.x = 10;", root.toString());
  }

  private Node getNode(String code) {
    Node node = new Node();
    node.setCode(code);
    node = NodeUtil.parseTree(compiler, node, new SourcePosition(null, null, 1, 1));
    return node;
  }
}