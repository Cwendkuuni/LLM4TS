package com.google.javascript.jscomp.tests;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopeCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Map;

/**
 * Tests for the {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;
  private Scope.Builder scopeBuilder;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopeBuilder = Scope.newBuilder();
  }

  @Test
  public void testProcessWithNoAliases() {
    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, null);
    assertThat(traversal.getAliasDefinitionsInOrder()).isEmpty();
    assertThat(traversal.getScopeCalls()).isEmpty();
  }

  @Test
  public void testProcessWithAlias() {
    // Setup the test case.
    Var aliasVar = scopeBuilder.addVar("alias", null).build();
    Scope scope = scopeBuilder.build();
    Node aliasNode = new Node(Token.NAME, "alias");
    Node aliasReferenceNode = new Node(Token.NAME, "alias");
    Node aliasDefinitionNode = new Node(Token.ASSIGN, null, aliasNode, aliasReferenceNode);
    Node root = new Node(Token.BLOCK, null, aliasDefinitionNode);

    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, scope, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that the alias was defined and used correctly.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertThat(aliasDefinitionsInOrder).containsExactly(aliasDefinitionNode);
    assertThat(aliasUsages).hasSize(1);
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertThat(aliasUsage).isInstanceOf(AliasedNode.class);
    AliasedNode aliasedNode = (AliasedNode) aliasUsage;
    assertThat(aliasedNode.aliasDefinition).isSameInstanceAs(aliasDefinitionNode);
    assertThat(aliasedNode.aliasReference).isSameInstanceAs(aliasReferenceNode);
    assertThat(scopeCalls).isEmpty();
  }

  @Test
  public void testProcessWithAliasAndScopeCall() {
    // Setup the test case.
    Var aliasVar = scopeBuilder.addVar("alias", null).build();
    Scope scope = scopeBuilder.build();
    Node aliasNode = new Node(Token.NAME, "alias");
    Node aliasDefinitionNode = new Node(Token.ASSIGN, null, aliasNode, aliasNode);
    Node scopeCallNode = new Node(Token.CALL, null, new Node(Token.NAME, "goog.scope"), aliasDefinitionNode);
    Node root = new Node(Token.BLOCK, null, scopeCallNode);

    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, scope, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that the alias was defined and used correctly.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertThat(aliasDefinitionsInOrder).containsExactly(aliasDefinitionNode);
    assertThat(aliasUsages).hasSize(1);
    AliasUsage aliasUsage = aliasUsages.get(0);
    assertThat(aliasUsage).isInstanceOf(AliasedNode.class);
    AliasedNode aliasedNode = (AliasedNode) aliasUsage;
    assertThat(aliasedNode.aliasDefinition).isSameInstanceAs(aliasDefinitionNode);
    assertThat(aliasedNode.aliasReference).isSameInstanceAs(aliasNode);
    assertThat(scopeCalls).hasSize(1);

    // Verify that the scope call was removed.
    ArgumentCaptor<List<Node>> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    when(compiler.getRewrittenNodes(nodeCaptor.capture())).thenAnswer(new Answer<Node>() {
      @Override
      public Node answer(InvocationOnMock invocation) throws Throwable {
        List<Node> nodes = nodeCaptor.getValue();
        Node scopeCallNodeIn = nodes.get(0);
        assertThat(scopeCallNodeIn).isNull();
        return null;
      }
    });
    when(transformationHandler.logAliasTransformation(
        anyString(), any(SourcePosition.class)))
        .thenReturn(new AliasTransformation());
    scopedAliases.hotSwapScript(null, root);
  }

  @Test
  public void testProcessWithImproperlyUsedGoogScope() {
    Node root = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"),
        new Node(Token.CALL, null, new Node(Token.NAME, "goog.scope"),
            new Node(Token.BLOCK, null, new Node(Token.RETURN, null, new Node(Token.NAME, "b")))));

    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that an error was reported.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(aliasUsages).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(traversal.hasErrors()).isTrue();
  }

  @Test
  public void testProcessWithMultipleAliases() {
    Var aliasVar1 = scopeBuilder.addVar("alias1", null).build();
    Var aliasVar2 = scopeBuilder.addVar("alias2", null).build();
    Scope scope = scopeBuilder.build();
    Node aliasNode1 = new Node(Token.NAME, "alias1");
    Node aliasDefinitionNode1 = new Node(Token.ASSIGN, null, aliasNode1, aliasNode1);
    Node aliasNode2 = new Node(Token.NAME, "alias2");
    Node aliasDefinitionNode2 = new Node(Token.ASSIGN, null, aliasNode2, aliasNode2);
    Node root = new Node(Token.BLOCK, null, aliasDefinitionNode1, aliasDefinitionNode2);

    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, scope, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that both aliases were defined and used correctly.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertThat(aliasDefinitionsInOrder).containsExactly(aliasDefinitionNode1, aliasDefinitionNode2);
    assertThat(aliasUsages).hasSize(2);
    for (int i = 0; i < aliasUsages.size(); i++) {
      AliasUsage aliasUsage = aliasUsages.get(i);
      assertThat(aliasUsage).isInstanceOf(AliasedNode.class);
      AliasedNode aliasedNode = (AliasedNode) aliasUsage;
      switch (i) {
        case 0:
          assertThat(aliasedNode.aliasDefinition).isSameInstanceAs(aliasDefinitionNode1);
          break;
        case 1:
          assertThat(aliasedNode.aliasDefinition).isSameInstanceAs(aliasDefinitionNode2);
          break;
        default:
          throw new AssertionError();
      }
    }
    assertThat(scopeCalls).isEmpty();
  }

  @Test
  public void testProcessWithAliasReferenceThis() {
    Var aliasVar = scopeBuilder.addVar("alias", null).build();
    Scope scope = scopeBuilder.build();
    Node aliasNode = new Node(Token.NAME, "alias");
    Node aliasDefinitionNode = new Node(Token.ASSIGN, null, aliasNode, aliasNode);
    Node root = new Node(Token.BLOCK, null, new Node(Token.THIS, null), aliasDefinitionNode);

    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, scope, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that an error was reported.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(aliasUsages).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(traversal.hasErrors()).isTrue();
    assertThat(traversal.getErrorMessages())
        .containsExactly(GOOG_SCOPE_REFERENCES_THIS);
  }

  @Test
  public void testProcessWithAliasReturn() {
    Var aliasVar = scopeBuilder.addVar("alias", null).build();
    Scope scope = scopeBuilder.build();
    Node aliasNode = new Node(Token.NAME, "alias");
    Node aliasDefinitionNode = new Node(Token.ASSIGN, null, aliasNode, aliasNode);
    Node root = new Node(Token.BLOCK, null, new Node(Token.RETURN, null, aliasNode));

    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, scope, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that an error was reported.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(aliasUsages).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(traversal.hasErrors()).isTrue();
    assertThat(traversal.getErrorMessages())
        .containsExactly(GOOG_SCOPE_USES_RETURN);
  }

  @Test
  public void testProcessWithAliasThrow() {
    Var aliasVar = scopeBuilder.addVar("alias", null).build();
    Scope scope = scopeBuilder.build();
    Node aliasNode = new Node(Token.NAME, "alias");
    Node aliasDefinitionNode = new Node(Token.ASSIGN, null, aliasNode, aliasNode);
    Node root = new Node(Token.BLOCK, null, new Node(Token.THROW, null));

    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, scope, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that an error was reported.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(aliasUsages).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(traversal.hasErrors()).isTrue();
    assertThat(traversal.getErrorMessages())
        .containsExactly(GOOG_SCOPE_USES_THROW);
  }

  @Test
  public void testProcessWithAliasRedefinition() {
    Var aliasVar1 = scopeBuilder.addVar("alias", null).build();
    Scope scope = scopeBuilder.build();
    Node aliasNode1 = new Node(Token.NAME, "alias");
    Node aliasDefinitionNode1 = new Node(Token.ASSIGN, null, aliasNode1, aliasNode1);
    Node aliasNode2 = new Node(Token.NAME, "alias");
    Node aliasDefinitionNode2 = new Node(Token.ASSIGN, null, aliasNode2, aliasNode2);
    Node root = new Node(Token.BLOCK, null, aliasDefinitionNode1, aliasDefinitionNode2);

    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, scope, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that an error was reported.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(aliasUsages).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(traversal.hasErrors()).isTrue();
    assertThat(traversal.getErrorMessages())
        .containsExactly(GOOG_SCOPE_ALIAS_REDEFINED, "alias");
  }

  @Test
  public void testProcessWithNonAliasLocal() {
    Var aliasVar = scopeBuilder.addVar("alias", null).build();
    Var localVar = scopeBuilder.addVar("local", null).build();
    Scope scope = scopeBuilder.build();
    Node aliasNode = new Node(Token.NAME, "alias");
    Node aliasDefinitionNode = new Node(Token.ASSIGN, null, aliasNode, aliasNode);
    Node localNode = new Node(Token.NAME, "local");
    Node root = new Node(Token.BLOCK, null, aliasDefinitionNode, localNode);

    Traversal traversal = newTraversal();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, scope, transformationHandler);
    scopedAliases.process(null, root);

    // Verify that an error was reported.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertThat(aliasDefinitionsInOrder).isEmpty();
    assertThat(aliasUsages).isEmpty();
    assertThat(scopeCalls).isEmpty();
    assertThat(traversal.hasErrors()).isTrue();
    assertThat(traversal.getErrorMessages())
        .containsExactly(GOOG_SCOPE_NON_ALIAS_LOCAL, "local");
  }

  private Traversal newTraversal() {
    Traversal traversal = new Traversal();
    traversal.setCallback(new ScopeCallback() {});
    return traversal;
  }
}