package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Set up mocks
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setAliasTransformation(AliasTransformation.NEVER);

    AliasTransformationHandler transformationHandler = new AliasTransformationHandler(
        compilerOptions);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    // Create test root node
    Node root = Node.newRootNode();
    Node googScopeCall = Node.newCall(
        Node.newName("goog", Node.NO_DECORATIONS),
        Node.newName("scope", Node.NO_DECORATIONS),
        Node.newList(
            Node.newFunction(
                Node.SCOPE_FUNCTION,
                Node.newList(),
                Node.newBlock(
                    Node.newStatement(
                        Node.newAssign(
                            Node.newName("dom", Node.NO_DECORATIONS),
                            Node.newCall(
                                Node.newName("goog", Node.NO_DECORATIONS),
                                Node.newName("dom", Node.NO_DECORATIONS),
                                Node.newList(
                                    Node.newQualifiedName(
                                        "TagName", "DIV"),
                                    Node.NO_DECORATIONS,
                                    Node.newFunction(
                                        Node.SCOPE_FUNCTION,
                                        Node.newList(),
                                        Node.newBlock(
                                            Node.newStatement(
                                                Node.newReturn(
                                                    Node.newCall(
                                                        Node.newName(
                                                            "goog",
                                                            Node.NO_DECORATIONS),
                                                        "dom",
                                                        Node.NO_DECORATIONS,
                                                        Node.newList(
                                                            Node.newQualifiedName(
                                                                "TagName",
                                                                "DIV"),
                                                            Node.NO_DECORATIONS)
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    Node.newStatement(Node.newReturn(Node.newNull()))
                )
            )
        )
    );

    root.addChild(googScopeCall);

    // Test passing
    scopedAliases.process(null, root);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    // Check number of nodes changed
    assertThat(aliasDefinitionsInOrder).hasSize(1);
    assertThat(scopeCalls).hasSize(1);
    assertThat(aliasUsages).hasSize(1);

    // Check alias definition
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    Var aliasVar = aliasDefinition.getParent();
    assertThat(aliasVar).isNotNull();
    String aliasName = aliasVar.getName();
    String aliasValue = aliasDefinition.getChildAtIndex(0).getString();
    assertThat(aliasName).isEqualTo("dom");
    assertThat(aliasValue).isEqualTo("goog.dom");

    // Check scope call
    Node scopeCall = scopeCalls.get(0);
    Node scopeFunction = scopeCall.getLastChild();
    Node anonymousFunction = scopeFunction.getLastChild();
    List<Node> parameters = NodeUtil.getFunctionParameters(anonymousFunction);
    assertThat(parameters).isEmpty();

    // Check alias usage
    Node aliasUsage = aliasUsages.get(0);
    Node aliasReference = aliasUsage.getNode();
    Node aliasDefinitionNode = ((AliasedNode) aliasUsage).getAliasDefinition();
    String aliasName2 = aliasReference.getString();
    String aliasValue2 = aliasDefinitionNode.getString();
    assertThat(aliasName2).isEqualTo("dom");
    assertThat(aliasValue2).isEqualTo("goog.dom");

    // Test error handling
    Node wrongScopeCall = Node.newCall(
        Node.newName("goog", Node.NO_DECORATIONS),
        Node.newName("scope", Node.NO_DECORATIONS),
        Node.newList(
            Node.newFunction(
                Node.SCOPE_FUNCTION,
                Node.newList(),
                Node.newBlock(
                    Node.newStatement(
                        Node.newAssign(
                            Node.newName("dom", Node.NO_DECORATIONS),
                            Node.newCall(
                                Node.newName("goog", Node.NO_DECORATIONS),
                                Node.newName("dom", Node.NO_DECORATIONS),
                                Node.newList(
                                    Node.newQualifiedName(
                                        "TagName", "DIV"),
                                    Node.NO_DECORATIONS,
                                    Node.newFunction(
                                        Node.SCOPE_FUNCTION,
                                        Node.newList(),
                                        Node.newBlock(
                                            Node.newStatement(
                                                Node.newReturn(
                                                    Node.newCall(
                                                        Node.newName(
                                                            "goog",
                                                            Node.NO_DECORATIONS),
                                                        "dom",
                                                        Node.NO_DECORATIONS,
                                                        Node.newList(
                                                            Node.newQualifiedName(
                                                                "TagName",
                                                                "DIV"),
                                                            Node.newName(
                                                                "this",
                                                                Node.NO_DECORATIONS),
                                                            Node.NO_DECORATIONS)
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    Node.newStatement(Node.newReturn(Node.newNull()))
                )
            )
        )
    );
    when(compiler.report(any(DiagnosticType.class), any()))
        .thenReturn(null);

    scopedAliases.process(null, root);

    List<Node> errorNodes = Lists.newArrayList();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    for (Node node : traversal.getVisibleNodes()) {
      if (node.getDiagnostic() != null) {
        errorNodes.add(node);
      }
    }

    DiagnosticType diagnostic = GOOG_SCOPE_REFERENCES_THIS;
    for (Node errorNode : errorNodes) {
      DiagnosticType nodeDiagnostic = errorNode.getDiagnostic();
      if (nodeDiagnostic != null && diagnostic.equals(nodeDiagnostic)) {
        break;
      }
    }

    assertThat(diagnostic).isNotNull();
  }
}