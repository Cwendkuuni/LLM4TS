import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopeCall;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Test class for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private Traversal traversal;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    transformationHandler = Mockito.mock(AliasTransformationHandler.class);
    traversal = new Traversal(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess_NoAliases() {
    Node root = Mockito.mock(Node.class);

    traversal.process(null, root);

    verify(compiler, never()).reportCodeChange();
  }

  @Test
  public void testProcess_SingleAlias() {
    Node root = Mockito.mock(Node.class);
    Node aliasDefinition = Mockito.mock(Node.class);
    Node aliasUsage = Mockito.mock(Node.class);

    when(root.getFirstChild()).thenReturn(aliasDefinition);
    when(aliasDefinition.getLastChild()).thenReturn(aliasUsage);

    when(aliasDefinition.getLastChild().getQualifiedName()).thenReturn("dom");
    when(aliasDefinition.getParent().getLastChild().getQualifiedName()).thenReturn("goog.dom");

    traversal.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(0, scopeCalls.size());
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());

    Node expectedAliasDefinition = new Node("Name", "goog.dom");
    Node expectedAliasUsage = new Node("Name", "dom");

    verify(compiler, never()).reportCodeChange();

    verify(aliasDefinition).detachFromParent();
    verify(aliasUsage).applyAlias();
    assertEquals(expectedAliasDefinition, aliasDefinitionsInOrder.get(0));
    assertEquals(expectedAliasUsage, aliasUsages.get(0));
  }

  @Test
  public void testProcess_MultipleAliases() {
    Node root = Mockito.mock(Node.class);
    Node aliasDefinition1 = Mockito.mock(Node.class);
    Node aliasDefinition2 = Mockito.mock(Node.class);
    Node aliasUsage1 = Mockito.mock(Node.class);
    Node aliasUsage2 = Mockito.mock(Node.class);

    when(root.getFirstChild()).thenReturn(aliasDefinition1);
    when(aliasDefinition1.getLastChild()).thenReturn(aliasUsage1);
    when(aliasDefinition1.getLastChild().getQualifiedName()).thenReturn("dom");
    when(aliasDefinition1.getParent().getLastChild().getQualifiedName()).thenReturn("goog.dom");

    when(root.getLastChild()).thenReturn(aliasDefinition2);
    when(aliasDefinition2.getLastChild()).thenReturn(aliasUsage2);
    when(aliasDefinition2.getLastChild().getQualifiedName()).thenReturn("element");
    when(aliasDefinition2.getParent().getLastChild().getQualifiedName()).thenReturn("goog.element");

    traversal.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(0, scopeCalls.size());
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(2, aliasUsages.size());

    Node expectedAliasDefinition1 = new Node("Name", "goog.dom");
    Node expectedAliasDefinition2 = new Node("Name", "goog.element");
    Node expectedAliasUsage1 = new Node("Name", "dom");
    Node expectedAliasUsage2 = new Node("Name", "element");

    verify(compiler, never()).reportCodeChange();

    verify(aliasDefinition1).detachFromParent();
    verify(aliasUsage1).applyAlias();
    assertEquals(expectedAliasDefinition1, aliasDefinitionsInOrder.get(0));
    assertEquals(expectedAliasUsage1, aliasUsages.get(0));

    verify(aliasDefinition2).detachFromParent();
    verify(aliasUsage2).applyAlias();
    assertEquals(expectedAliasDefinition2, aliasDefinitionsInOrder.get(1));
    assertEquals(expectedAliasUsage2, aliasUsages.get(1));
  }

  @Test(expected = AssertionError.class)
  public void testProcess_InvalidScopeCall() {
    Node root = Mockito.mock(Node.class);
    Node scopeCall = Mockito.mock(Node.class);

    when(root.getFirstChild()).thenReturn(scopeCall);

    when(scopeCall.getLastChild().getQualifiedName()).thenReturn("goog.scope");
    when(scopeCall.getLastChild().getLastChild().getQualifiedName()).thenReturn("invalid");

    traversal.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();

    assertEquals(1, scopeCalls.size());

    verify(compiler).report(ArgumentMatchers.isA(DiagnosticType.class),
        any(Node.class), any(String[].class));
  }

  @Test(expected = AssertionError.class)
  public void testProcess_InvalidAliasDefinition() {
    Node root = Mockito.mock(Node.class);
    Node aliasDefinition = Mockito.mock(Node.class);

    when(root.getFirstChild()).thenReturn(aliasDefinition);

    when(aliasDefinition.getLastChild()).thenReturn(Mockito.mock(Node.class));

    traversal.process(null, root);

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();

    assertEquals(0, aliasDefinitionsInOrder.size());

    verify(compiler).report(ArgumentMatchers.isA(DiagnosticType.class),
        any(Node.class), any(String[].class));
  }

  @Test(expected = AssertionError.class)
  public void testProcess_InvalidAliasUsage() {
    Node root = Mockito.mock(Node.class);
    Node aliasUsage = Mockito.mock(Node.class);

    traversal.process(null, root);

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(0, aliasUsages.size());

    verify(compiler).report(ArgumentMatchers.isA(DiagnosticType.class),
        any(Node.class), any(String[].class));
  }

  @Test(expected = AssertionError.class)
  public void testProcess_MultipleAliasesWithSameName() {
    Node root = Mockito.mock(Node.class);
    Node aliasDefinition1 = Mockito.mock(Node.class);
    Node aliasDefinition2 = Mockito.mock(Node.class);
    Node aliasUsage1 = Mockito.mock(Node.class);
    Node aliasUsage2 = Mockito.mock(Node.class);

    when(root.getFirstChild()).thenReturn(aliasDefinition1);
    when(aliasDefinition1.getLastChild()).thenReturn(aliasUsage1);
    when(aliasDefinition1.getLastChild().getQualifiedName()).thenReturn("dom");
    when(aliasDefinition1.getParent().getLastChild().getQualifiedName()).thenReturn("goog.dom");

    when(root.getLastChild()).thenReturn(aliasDefinition2);
    when(aliasDefinition2.getLastChild()).thenReturn(aliasUsage2);
    when(aliasDefinition2.getLastChild().getQualifiedName()).thenReturn("dom");
    when(aliasDefinition2.getParent().getLastChild().getQualifiedName()).thenReturn("goog.dom");

    traversal.process(null, root);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(0, scopeCalls.size());
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());

    verify(compiler).report(ArgumentMatchers.isA(DiagnosticType.class),
        any(Node.class), ArgumentMatchers.arrayContaining(new String[]{"dom"}));
  }

  @Test
  public void testHotSwapScript() {
    Node root = Mockito.mock(Node.class);
    Node aliasDefinition = Mockito.mock(Node.class);
    Node aliasUsage = Mockito.mock(Node.class);

    when(root.getFirstChild()).thenReturn(aliasDefinition);
    when(aliasDefinition.getLastChild()).thenReturn(aliasUsage);

    when(aliasDefinition.getLastChild().getQualifiedName()).thenReturn("dom");
    when(aliasDefinition.getParent().getLastChild().getQualifiedName()).thenReturn("goog.dom");

    traversal.hotSwapScript(root, null);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(0, scopeCalls.size());
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());

    Node expectedAliasDefinition = new Node("Name", "goog.dom");
    Node expectedAliasUsage = new Node("Name", "dom");

    verify(compiler).reportCodeChange();

    verify(aliasDefinition).detachFromParent();
    verify(aliasUsage).applyAlias();
    assertEquals(expectedAliasDefinition, aliasDefinitionsInOrder.get(0));
    assertEquals(expectedAliasUsage, aliasUsages.get(0));
  }

  @Test
  public void testHotSwapScript_NoChanges() {
    Node root = Mockito.mock(Node.class);

    traversal.hotSwapScript(root, null);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(0, scopeCalls.size());
    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, aliasUsages.size());

    verify(compiler, never()).reportCodeChange();
  }

  @Test
  public void testHotSwapScript_MultipleChanges() {
    Node root = Mockito.mock(Node.class);
    Node aliasDefinition1 = Mockito.mock(Node.class);
    Node aliasDefinition2 = Mockito.mock(Node.class);
    Node aliasUsage1 = Mockito.mock(Node.class);
    Node aliasUsage2 = Mockito.mock(Node.class);

    when(root.getFirstChild()).thenReturn(aliasDefinition1);
    when(aliasDefinition1.getLastChild()).thenReturn(aliasUsage1);
    when(aliasDefinition1.getLastChild().getQualifiedName()).thenReturn("dom1");
    when(aliasDefinition1.getParent().getLastChild().getQualifiedName()).thenReturn("goog.dom1");

    when(root.getLastChild()).thenReturn(aliasDefinition2);
    when(aliasDefinition2.getLastChild()).thenReturn(aliasUsage2);
    when(aliasDefinition2.getLastChild().getQualifiedName()).thenReturn("dom2");
    when(aliasDefinition2.getParent().getLastChild().getQualifiedName()).thenReturn("goog.dom2");

    traversal.hotSwapScript(root, null);

    List<ScopeCall> scopeCalls = traversal.getScopeCalls();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    assertEquals(0, scopeCalls.size());
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals(2, aliasUsages.size());

    Node expectedAliasDefinition1 = new Node("Name", "goog.dom1");
    Node expectedAliasDefinition2 = new Node("Name", "goog.dom2");
    Node expectedAliasUsage1 = new Node("Name", "dom1");
    Node expectedAliasUsage2 = new Node("Name", "dom2");

    verify(compiler).reportCodeChange();

    verify(aliasDefinition1).detachFromParent();
    verify(aliasUsage1).applyAlias();
    assertEquals(expectedAliasDefinition1, aliasDefinitionsInOrder.get(0));
    assertEquals(expectedAliasUsage1, aliasUsages.get(0));

    verify(aliasDefinition2).detachFromParent();
    verify(aliasUsage2).applyAlias();
    assertEquals(expectedAliasDefinition2, aliasDefinitionsInOrder.get(1));
    assertEquals(expectedAliasUsage2, aliasUsages.get(1));
  }

  @Test
  public void testApplyAlias() {
    Node aliasDefinition = Mockito.mock(Node.class);
    Node aliasUsage = Mockito.mock(Node.class);

    when(aliasDefinition.getQualifiedName()).thenReturn("goog.dom.DOM");
    when(aliasUsage.getQualifiedName()).thenReturn("dom");

    traversal.applyAlias(aliasDefinition, aliasUsage);

    verify(aliasUsage).replaceChildren(aliasDefinition);
  }

  @Test
  public void testRemoveAliasDefinition() {
    Node aliasDefinition = Mockito.mock(Node.class);

    when(aliasDefinition.getParent().isVar()).thenReturn(true);
    when(aliasDefinition.getParent().hasOneChild()).thenReturn(true);

    traversal.removeAliasDefinition(aliasDefinition);

    verify(aliasDefinition).detachFromParent();
  }

  @Test
  public void testRemoveAliasDefinition_NotVar() {
    Node aliasDefinition = Mockito.mock(Node.class);

    traversal.removeAliasDefinition(aliasDefinition);

    verify(aliasDefinition, never()).detachFromParent();
  }
}