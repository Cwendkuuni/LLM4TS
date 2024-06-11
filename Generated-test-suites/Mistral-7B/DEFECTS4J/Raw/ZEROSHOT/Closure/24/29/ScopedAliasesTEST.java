package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.jscomp.parser.jst.JSTNode;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new NoOpCompiler(); // Replace with real compiler
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    JSTNode rootNode = createTestNode();
    Node root = compiler.parseTree(rootNode);
    scopedAliases.process(null, root);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    Node googScopeCall = findNodeByName(root, "goog.scope");
    assertFalse(googScopeCall.isCall());

    Node invalidGoogScopeCall = createInvalidGoogScopeCall();
    scopedAliases.process(null, root);
    Node invalidGoogScopeCallNode = findNodeByName(root, "goog.scope");
    assertFalse(invalidGoogScopeCall.isCall());
    assertFalse(invalidGoogScopeCallNode.isCall());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    Node validGoogScopeCall = createValidGoogScopeCall();
    scopedAliases.process(null, root);
    Node validGoogScopeCallNode = findNodeByName(root, "goog.scope");
    assertTrue(validGoogScopeCall.isCall());
    assertTrue(validGoogScopeCallNode.isCall());
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    Node invalidAliasDefinition = createInvalidAliasDefinition();
    Node invalidAliasUsage = createInvalidAliasUsage();
    scopedAliases.process(null, root);
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    Node validAliasDefinition = createValidAliasDefinition();
    Node validAliasUsage = createValidAliasUsage();
    scopedAliases.process(null, root);
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    Node invalidAliasRedefinition = createInvalidAliasRedefinition();
    scopedAliases.process(null, root);
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    Node invalidThisReference = createInvalidThisReference();
    scopedAliases.process(null, root);
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());

    Node invalidReturnStatement = createInvalidReturnStatement();
    scopedAliases.process(null, root);
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
  }

  private JSTNode createTestNode() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newExpressionStatement(
                JSTNode.newCallExpression(
                    JSTNode.newIdentifier("goog"),
                    JSTNode.newIdentifier("scope"),
                    JSTNode.newFunctionExpression(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        JSTNode.newBlockStatement(
                            JSTNode.newStatementList(
                                JSTNode.newExpressionStatement(
                                    JSTNode.newCallExpression(
                                        JSTNode.newIdentifier("goog"),
                                        JSTNode.newIdentifier("dom"),
                                        JSTNode.newMemberExpression(
                                            JSTNode.newThisExpression(),
                                            JSTNode.newIdentifier("createElement")),
                                        JSTNode.newNewArrayExpression(
                                            JSTNode.newLiteral(Node.DIV))),
                                    null)
                            )
                        )
                    )
                )
            )
        )
    );
  }

  private Node findNodeByName(Node root, String name) {
    for (Node child = root; child != null; child = child.getNext()) {
      if (child.isIdentifier() && child.getString().equals(name)) {
        return child;
      }
    }
    return null;
  }

  private Node createInvalidGoogScopeCall() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newExpressionStatement(
                JSTNode.newCallExpression(
                    null, // Replace with valid "goog.scope" node
                    JSTNode.newIdentifier("goog"),
                    JSTNode.newIdentifier("scope"),
                    JSTNode.newFunctionExpression(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        JSTNode.newBlockStatement(
                            JSTNode.newStatementList(
                                JSTNode.newExpressionStatement(
                                    JSTNode.newCallExpression(
                                        null, // Replace with valid call node
                                        JSTNode.newIdentifier("goog"),
                                        JSTNode.newIdentifier("scope"),
                                        null
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    );
  }

  private Node createValidGoogScopeCall() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newExpressionStatement(
                JSTNode.newCallExpression(
                    JSTNode.newIdentifier("goog"),
                    JSTNode.newIdentifier("scope"),
                    JSTNode.newFunctionExpression(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        JSTNode.newBlockStatement(
                            JSTNode.newStatementList()
                        )
                    )
                )
            )
        )
    );
  }

  private Node createInvalidAliasDefinition() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newVariableDeclarationList(
                JSTNode.newVarDeclaratorList(
                    JSTNode.newVarDeclarator(
                        JSTNode.newIdentifier("foo"),
                        JSTNode.newCallExpression(
                            null, // Replace with valid "goog.scope" node
                            JSTNode.newIdentifier("goog"),
                            JSTNode.newIdentifier("scope"),
                            null
                        )
                    )
                )
            )
        )
    );
  }

  private Node createValidAliasDefinition() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newVariableDeclarationList(
                JSTNode.newVarDeclaratorList(
                    JSTNode.newVarDeclarator(
                        JSTNode.newIdentifier("bar"),
                        JSTNode.newCallExpression(
                            JSTNode.newIdentifier("goog"),
                            JSTNode.newIdentifier("scope"),
                            JSTNode.newFunctionExpression(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                JSTNode.newBlockStatement(
                                    JSTNode.newStatementList(
                                        JSTNode.newAssignmentStatement(
                                            JSTNode.newVariableReference("bar"),
                                            JSTNode.newIdentifier("baz")
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    );
  }

  private Node createInvalidAliasUsage() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newExpressionStatement(
                JSTNode.newCallExpression(
                    null, // Replace with valid expression node
                    JSTNode.newIdentifier("foo")
                )
            )
        )
    );
  }

  private Node createValidAliasUsage() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newExpressionStatement(
                JSTNode.newCallExpression(
                    JSTNode.newIdentifier("bar")
                )
            )
        )
    );
  }

  private Node createInvalidAliasRedefinition() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newVariableDeclarationList(
                JSTNode.newVarDeclaratorList(
                    JSTNode.newVarDeclarator(
                        JSTNode.newIdentifier("foo"),
                        JSTNode.newCallExpression(
                            null, // Replace with valid "goog.scope" node
                            JSTNode.newIdentifier("goog"),
                            JSTNode.newIdentifier("scope"),
                            null
                        )
                    ),
                    JSTNode.newVarDeclarator(
                        JSTNode.newIdentifier("foo"),
                        JSTNode.newLiteral(42)
                    )
                )
            )
        )
    );
  }

  private Node createInvalidThisReference() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newExpressionStatement(
                JSTNode.newCallExpression(
                    null, // Replace with valid "goog.scope" node
                    JSTNode.newIdentifier("goog"),
                    JSTNode.newIdentifier("scope"),
                    null
                ),
                JSTNode.newMemberExpression(
                    JSTNode.newThisExpression(),
                    JSTNode.newIdentifier("toString")
                )
            )
        )
    );
  }

  private Node createInvalidReturnStatement() {
    return JSTNode.newProgram(
        JSTNode.newStatementList(
            JSTNode.newExpressionStatement(
                JSTNode.newCallExpression(
                    null, // Replace with valid "goog.scope" node
                    JSTNode.newIdentifier("goog"),
                    JSTNode.newIdentifier("scope"),
                    null
                ),
                JSTNode.newReturnStatement(JSTNode.newNullLiteral())
            )
        )
    );
  }
}