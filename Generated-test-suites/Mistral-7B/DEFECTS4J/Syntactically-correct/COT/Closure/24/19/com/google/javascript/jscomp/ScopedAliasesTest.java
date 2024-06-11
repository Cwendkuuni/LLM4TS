package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    

    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.newObjectLiteral();
    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Node.newFunction(Node.newBlock(Node.newStatementList()))
    );
    root.addChild(googScopeCall);
    Node aliasDefinition = Node.newAssignment(
        Node.newQualifiedName("dom", "DIV"),
        Node.newIdentifier("DIV")
    );
    googScopeCall.addChild(aliasDefinition);
    Node aliasUsage = Node.newCall(
        Node.newQualifiedName("dom"),
        Node.newIdentifier("createElement"),
        Node.newQualifiedName("DIV")
    );
    root.addChild(aliasUsage);

    scopedAliases.process(Node.newObjectLiteral(), root);

    // Then
    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    assertThat(aliasDefinitionsInOrder).hasSize(1);
    assertThat(aliasDefinitionsInOrder.get(0)).isEqualTo(aliasDefinition);

    assertThat(scopeCalls).hasSize(1);
    assertThat(scopeCalls.get(0)).isEqualTo(googScopeCall);

    assertThat(aliasUsages).hasSize(1);
    assertThat(aliasUsages.get(0)).isInstanceOf(AliasedNode.class);
    AliasedNode aliasedNode = (AliasedNode) aliasUsages.get(0);
    assertThat(aliasedNode.aliasReference).isEqualTo(aliasUsage);
    assertThat(aliasedNode.aliasDefinition).isEqualTo(aliasDefinition);

    
  }

  @Test
  public void testScopedAliasesWithErrors() {
    

    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node root = Node.newObjectLiteral();
    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Node.newFunction(
            Node.newBlock(
                Node.newStatementList(
                    Node.newVariableDeclarationList(
                        Node.newVariableDeclaration("g", Node.newIdentifier("goog"))),
                    Node.newStatement(Node.newReturn(Node.newIdentifier("g")))
                )
            )
        )
    );
    root.addChild(googScopeCall);
    Node aliasDefinition = Node.newAssignment(
        Node.newQualifiedName("dom", "DIV"),
        Node.newIdentifier("DIV")
    );
    googScopeCall.addChild(aliasDefinition);
    Node aliasUsage = Node.newCall(
        Node.newQualifiedName("dom"),
        Node.newIdentifier("createElement"),
        Node.newQualifiedName("DIV")
    );
    root.addChild(aliasUsage);
    Node badParameterCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Node.newFunction(
            Node.newBlock(
                Node.newStatementList(
                    Node.newStatement(Node.newCall(
                        Node.newQualifiedName("goog", "scope"),
                        Node.newFunction(
                            Node.newBlock(
                                Node.newStatementList(
                                    Node.newStatement(Node.newCall(
                                        Node.newIdentifier("console"),
                                        Node.newIdentifier("log"),
                                        Node.newStringLiteral("Hello")))
                                )
                            )
                        )
                    ))
                )
            )
        )
    );
    root.addChild(badParameterCall);

    scopedAliases.process(Node.newObjectLiteral(), root);

    // Then
    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    Collection<DiagnosticType> diagnostics = compiler.getDiagnostics();

    assertThat(aliasDefinitionsInOrder).hasSize(1);
    assertThat(aliasDefinitionsInOrder.get(0)).isEqualTo(aliasDefinition);

    assertThat(scopeCalls).hasSize(2);
    assertThat(scopeCalls.get(0)).isEqualTo(googScopeCall);
    assertThat(scopeCalls.get(1)).isEqualTo(aliasDefinition.getParent());

    assertThat(aliasUsages).hasSize(2);
    assertThat(aliasUsages.get(0)).isInstanceOf(AliasedNode.class);
    AliasedNode aliasedNode = (AliasedNode) aliasUsages.get(0);
    assertThat(aliasedNode.aliasReference).isEqualTo(aliasUsage);
    assertThat(aliasedNode.aliasDefinition).isEqualTo(aliasDefinition);
    assertThat(aliasUsages.get(1)).isInstanceOf(AliasedNode.class);
    AliasedNode badParameterAliasedNode = (AliasedNode) aliasUsages.get(1);
    assertThat(badParameterAliasedNode.aliasReference).isEqualTo(badParameterCall);
    assertThat(badParameterAliasedNode.aliasDefinition).isNull();

    assertThat(diagnostics).hasSize(2);
    assertThat(diagnostics.iterator().next().getMessage()).isEqualTo(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS: The call to goog.scope must take only a single parameter.  It must " +
            "be an anonymous function that itself takes no parameters.");
    assertThat(diagnostics.iterator().next().getSourcePosition().getFileName())
        .isEqualTo("TestSourceFile.js");

    
  }
}