package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String sourceCode = "goog.scope(function() {\n" +
      "  var dom = goog.dom;\n" +
      "  var DIV = dom.TagName.DIV;\n" +
      "  dom.createElement(DIV);\n" +
      "});";

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    when(transformationHandler.logAliasTransformation(anyString(), any(SourcePosition.class)))
        .thenReturn(new AliasTransformation("goog.dom", "goog"));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.parse(sourceCode);

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    // There should be one alias definition.
    assertThat(aliasDefinitionsInOrder).hasSize(1);
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertThat(aliasDefinition.getLastChild().getString()).isEqualTo("DOM");

    // There should be one scope call.
    Node scopeCall = scopeCalls.get(0);
    assertThat(scopeCall.getLastChild().getLastChild().getQualifiedName())
        .isEqualTo("goog.scope");

    // There should be one alias usage.
    Node aliasUsage = aliasUsages.get(0);
    Node aliasedNode = aliasUsage.applyAlias();
    assertThat(aliasedNode.getQualifiedName()).isEqualTo("goog.dom");

    // The scope call should have been removed.
    assertThat(scopeCalls).isEmpty();

    // The alias definition should have been replaced with the alias.
    Node aliasDefinitionParent = aliasDefinition.getParent();
    Node aliasReplacement = aliasDefinitionParent.getFirstChild();
    Node aliasedNodeParent = aliasedNode.getParent();
    Node aliasReplacementParent = aliasedNodeParent.getParent();
    Node originalNode = aliasReplacementParent.getLastChild();

    assertThat(aliasDefinitionParent.getChildCount()).isEqualTo(1);
    assertThat(aliasReplacementParent.getChildCount()).isEqualTo(1);
    assertThat(originalNode).isSameInstanceAs(aliasReplacement);
    assertThat(aliasReplacementParent).isSameInstanceAs(originalNode.getParent());

    // The scoped alias should have been collapsed.
    Node scopedAliasedNode = aliasDefinitionParent.getLastChild();
    Node scopedAliasedNodeParent = scopedAliasedNode.getParent();
    Node scopedAliasedNodeLastChild = scopedAliasedNode.getLastChild();

    Node scopedAliasedNodeNewParent = scopedAliasedNodeLastChild.getParent();
    Node scopedAliasedNodeNewLastChild = scopedAliasedNodeNewParent.getLastChild();

    assertThat(scopedAliasedNodeLastChild.getParent()).isSameInstanceAs(scopedAliasedNodeNewParent);
    assertThat(scopedAliasedNodeNewLastChild).isSameInstanceAs(scopedAliasedNodeLastChild);

    // Check the transformed AST.
    Node transformedRoot = scopedAliases.getTransformedRoot();
    Node transformedAliasDefinition = transformedRoot.getLastChild();
    Node transformedAliasedNode = transformedAliasDefinition.getLastChild();

    assertThat(transformedAliasDefinition.getLastChild().getString()).isEqualTo("DOM");
    assertThat(transformedAliasedNode.getQualifiedName()).isEqualTo("goog.dom");

    // The transformed AST should not contain the original scope call.
    Node transformedScopeCall = NodeUtil.findNode(transformedRoot, "goog.scope");
    assertThat(transformedScopeCall).isNull();
  }
}