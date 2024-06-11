import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

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
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(compiler.report(NodeTraversal.makeError(
        Node.createTree("testNode"), DiagnosticType.error,
        new String[]{"message"}))).thenReturn(null);

    when(preprocessorSymbolTable.addReference(Node.createTree("testNode")))
        .thenReturn(null);

    when(transformationHandler.logAliasTransformation(
        "testFile.js", SourcePosition.newSourcePosition(0, 0)))
        .thenReturn(new AliasTransformation());
  }

  @Test
  public void testProcess_NoAliasUsagesOrDefinitionsOrScopeCalls() {
    Node root = Node.createTree("testRoot");
    scopedAliases.process(null, root);

    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testProcess_SingleAliasUsage() {
    Node root = Node.createTree("goog.scope(function() { var x = goog.y; })");
    Node aliasDefinition = Node.createTree("goog.y = 1;");
    scopedAliases.transformationHandler = transformationHandler;
    scopedAliases.preprocessorSymbolTable = preprocessorSymbolTable;

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.compile(Node.createTree("testFile.js"), root)).thenReturn(traversal);

    NodeTraversal.traverse(compiler, root, scopedAliases);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertFalse(scopedAliases.hasErrors());
    assertSame(aliasDefinition, aliasDefinitionsInOrder.get(0));
    assertSame(root.getChildAtIndex(0), aliasUsages.get(0).aliasReference);
  }

  @Test
  public void testProcess_MultipleAliasUsages() {
    Node root = Node.createTree(
        "goog.scope(function() {" +
            "  var x = goog.y;" +
            "  var z = goog.z;" +
            "})");
    Node aliasDefinitionX = Node.createTree("goog.y = 1;");
    Node aliasDefinitionZ = Node.createTree("goog.z = 2;");

    scopedAliases.transformationHandler = transformationHandler;
    scopedAliases.preprocessorSymbolTable = preprocessorSymbolTable;

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.compile(Node.createTree("testFile.js"), root)).thenReturn(traversal);

    NodeTraversal.traverse(compiler, root, scopedAliases);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    assertFalse(scopedAliases.hasErrors());
    assertSame(aliasDefinitionX, aliasDefinitionsInOrder.get(0));
    assertSame(aliasDefinitionZ, aliasDefinitionsInOrder.get(1));
    assertSame(root.getChildAtIndex(0), aliasUsages.get(0).aliasReference);
    assertSame(root.getChildAtIndex(2), aliasUsages.get(1).aliasReference);
  }

  @Test
  public void testProcess_SingleScopeCall() {
    Node root = Node.createTree(
        "goog.scope(function() {" +
            "  goog.scope(function() {});" +
            "})");
    Node scopeCall = root.getChildAtIndex(1);

    scopedAliases.transformationHandler = transformationHandler;
    scopedAliases.preprocessorSymbolTable = preprocessorSymbolTable;

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.compile(Node.createTree("testFile.js"), root)).thenReturn(traversal);

    NodeTraversal.traverse(compiler, root, scopedAliases);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertFalse(scopedAliases.hasErrors());
    assertSame(scopeCall, scopeCalls.get(0));
    assertNull(aliasDefinitionsInOrder);
    assertNull(aliasUsages);
  }

  @Test
  public void testProcess_MultipleScopeCalls() {
    Node root = Node.createTree(
        "goog.scope(function() {" +
            "  goog.scope(function() {});" +
            "  goog.scope(function() {});" +
            "})");

    scopedAliases.transformationHandler = transformationHandler;
    scopedAliases.preprocessorSymbolTable = preprocessorSymbolTable;

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.compile(Node.createTree("testFile.js"), root)).thenReturn(traversal);

    NodeTraversal.traverse(compiler, root, scopedAliases);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();

    assertFalse(scopedAliases.hasErrors());
    assertSame(root.getChildAtIndex(1), scopeCalls.get(0));
    assertSame(root.getChildAtIndex(3), scopeCalls.get(1));
    assertNull(aliasDefinitionsInOrder);
    assertNull(aliasUsages);
  }
}