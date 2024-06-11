import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for ScopedAliases.
 */
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
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_NoAliases() {
    Node root = Mockito.mock(Node.class);
    Node externs = Mockito.mock(Node.class);
    scopedAliases.process(externs, root);
    Mockito.verify(compiler, Mockito.never()).reportCodeChange();
  }

  @Test
  public void testProcess_WithAliases() {
    Node root = createNode("goog.scope(function() { var x = goog.x; })");
    Node externs = Mockito.mock(Node.class);
    scopedAliases.process(externs, root);

    Traversal traversal = Mockito.mock(Traversal.class);
    NodeTraversal.traverse(compiler, root, traversal);
    when(traversal.getAliasDefinitionsInOrder()).thenReturn(Lists.newArrayList(
        createNode("var x"),
        createNode("var goog.x")
    ));
    when(traversal.getScopeCalls()).thenReturn(Lists.newArrayList(createNode("goog.scope")));

    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
  }

  @Test
  public void testHotSwapScript_NoChanges() {
    Node root = Mockito.mock(Node.class);
    Node originalRoot = Mockito.mock(Node.class);
    scopedAliases.hotSwapScript(root, originalRoot);

    Mockito.verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testHotSwapScript_WithChanges() {
    Node root = createNode("goog.scope(function() { var x = goog.x; })");
    Node originalRoot = Mockito.mock(Node.class);
    scopedAliases.hotSwapScript(root, originalRoot);

    Traversal traversal = Mockito.mock(Traversal.class);
    NodeTraversal.traverse(compiler, root, traversal);
    when(traversal.getAliasDefinitionsInOrder()).thenReturn(Lists.newArrayList(
        createNode("var x"),
        createNode("var goog.x")
    ));
    when(traversal.getScopeCalls()).thenReturn(Lists.newArrayList(createNode("goog.scope")));

    when(compiler.getOptions()).thenReturn(new CompilerOptions(AliasTransformation.ALLOW_GOOG_SCOPE));
    when(transformationHandler.logAliasTransformation(
        ArgumentMatchers.eq("test.js"),
        ArgumentMatchers.any(SourcePosition.class)
    )).thenReturn(Mockito.mock(AliasTransformation.class));

    scopedAliases.hotSwapScript(root, originalRoot);

    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    Mockito.verify(transformationHandler, Mockito.times(1)).logAliasTransformation(
        ArgumentMatchers.eq("test.js"), Mockito.any());
  }

  @Test(expected = AssertionError.class)
  public void testHotSwapScript_WithErrors() {
    Node root = createNode("goog.scope(function() { return goog.x; })");
    Node originalRoot = Mockito.mock(Node.class);
    scopedAliases.hotSwapScript(root, originalRoot);

    Traversal traversal = Mockito.mock(Traversal.class);
    NodeTraversal.traverse(compiler, root, traversal);
    when(traversal.getAliasDefinitionsInOrder()).thenReturn(Lists.newArrayList(
        createNode("var x"),
        createNode("var goog.x")
    ));
    when(traversal.getScopeCalls()).thenReturn(Lists.newArrayList(createNode("goog.scope")));

    // This is just a mock error message, you can replace it with actual
    // error messages that may occur during testing.
    when(compiler.report(ArgumentMatchers.any())).thenThrow(new AssertionError("Test error"));

    scopedAliases.hotSwapScript(root, originalRoot);
  }

  private Node createNode(String code) {
    Node node = Node.createTree(code);
    return node;
  }
}