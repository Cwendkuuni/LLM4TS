package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new TestCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createTestNode("test.js");
    scopedAliases.process(null, root);

    // Check no errors were reported
    assertTrue(compiler.getDiagnostics().isEmpty());

    // Check alias definitions were identified and applied
    List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    List<Node> expectedAliasDefinitions = Lists.newArrayList(
        root.getChildAtIndex(1),
        root.getChildAtIndex(2)
    );
    assertEquals(expectedAliasDefinitions, aliasDefinitionsInOrder);

    // Check alias usages were identified and replaced
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    List<AliasUsage> expectedAliasUsages = Lists.newArrayList(
        new AliasedNode(root.getChildAtIndex(3), root.getChildAtIndex(4)),
        new AliasedNode(root.getChildAtIndex(5), root.getChildAtIndex(6))
    );
    assertEquals(expectedAliasUsages, aliasUsages);

    // Check scope calls were identified and removed
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<Node> expectedScopeCalls = Lists.newArrayList(root.getChildAtIndex(0));
    assertEquals(expectedScopeCalls, scopeCalls);
  }

  @Test
  public void testProcessErrors() {
    AbstractCompiler compiler = new TestCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = createTestNodeWithErrors("testWithErrors.js");
    scopedAliases.process(null, root);

    // Check errors were reported
    List<DiagnosticType> diagnostics = compiler.getDiagnostics();
    List<DiagnosticType> expectedDiagnostics = Lists.newArrayList(
        GOOG_SCOPE_USED_IMPROPERLY,
        GOOG_SCOPE_HAS_BAD_PARAMETERS,
        GOOG_SCOPE_REFERENCES_THIS,
        GOOG_SCOPE_USES_RETURN,
        GOOG_SCOPE_USES_THROW,
        GOOG_SCOPE_ALIAS_REDEFINED,
        GOOG_SCOPE_NON_ALIAS_LOCAL
    );
    assertEquals(expectedDiagnostics, diagnostics);
  }

  private Node createTestNode(String fileName) {
    Node root = new Node_();

    Node googScopeCall = new CallNode(
        new QualifiedNameNode("goog", "scope"),
        null,
        new ArrayNode(new Node[] {new FunctionDeclarationNode(null, null, null, null)}),
        null,
        null,
        new SourcePosition(0, 0, fileName, 0, 0)
    );

    root.addChild(googScopeCall);

    Node aliasDefinition1 = new VarNode(
        "goog",
        null,
        new IdentifierNode("goog"),
        null,
        null,
        null,
        new ArrayNode(new Node[] {}),
        null,
        new SourcePosition(1, 0, fileName, 1, 0)
    );

    Node aliasDefinition2 = new VarNode(
        "dom",
        null,
        new IdentifierNode("dom"),
        null,
        null,
        null,
        new ArrayNode(new Node[] {}),
        null,
        new SourcePosition(2, 0, fileName, 2, 0)
    );

    root.addChild(aliasDefinition1);
    root.addChild(aliasDefinition2);

    Node functionExpression = new FunctionExpressionNode(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        new ArrayNode(new Node[] {}),
        null,
        new SourcePosition(3, 0, fileName, 3, 0)
    );

    root.addChild(functionExpression);

    Node aliasUsage1 = new IdentifierNode("goog");
    Node aliasUsage2 = new IdentifierNode("dom");

    functionExpression.addChild(aliasUsage1);
    functionExpression.addChild(new BinaryOperatorNode(Token.OP_DOT, aliasUsage1, new IdentifierNode("dom")));
    functionExpression.addChild(aliasUsage2);

    root.addChild(new ReturnStatementNode(new ArrayNode(new Node[] {functionExpression})));

    return root;
  }

  private Node createTestNodeWithErrors(String fileName) {
    Node root = new Node_();

    Node googScopeCall = new CallNode(
        new QualifiedNameNode("goog", "scope"),
        null,
        new ArrayNode(new Node[] {new FunctionDeclarationNode(null, null, null, null)}),
        null,
        null,
        new SourcePosition(0, 0, fileName, 0, 0)
    );

    root.addChild(googScopeCall);

    Node aliasDefinition1 = new VarNode(
        "goog",
        null,
        new IdentifierNode("goog"),
        null,
        null,
        null,
        new ArrayNode(new Node[] {}),
        null,
        new SourcePosition(1, 0, fileName, 1, 0)
    );

    root.addChild(aliasDefinition1);

    Node aliasDefinition2 = new VarNode(
        "dom",
        null,
        new IdentifierNode("dom"),
        null,
        null,
        null,
        new ArrayNode(new Node[] {}),
        null,
        new SourcePosition(2, 0, fileName, 2, 0)
    );

    root.addChild(aliasDefinition2);

    Node functionExpression = new FunctionExpressionNode(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        new ArrayNode(new Node[] {}),
        null,
        new SourcePosition(3, 0, fileName, 3, 0)
    );

    root.addChild(functionExpression);

    Node aliasUsage1 = new IdentifierNode("goog");
    Node aliasUsage2 = new IdentifierNode("dom");
    Node thisNode = new IdentifierNode("this");
    Node returnNode = new ReturnStatementNode(new ArrayNode(new Node[] {}));
    Node throwNode = new ThrowStatementNode(new ObjectLiteralNode(new Node[] {}));

    // This node should cause "GOOG_SCOPE_USES_THIS" error
    functionExpression.addChild(new SetPropertyNode(aliasUsage1, thisNode));

    // This node should cause "GOOG_SCOPE_USES_RETURN" error
    functionExpression.addChild(returnNode);

    // This node should cause "GOOG_SCOPE_USES_THROW" error
    functionExpression.addChild(throwNode);

    // This node should cause "GOOG_SCOPE_REFERENCES_THIS" error
    functionExpression.addChild(new IdentifierNode("this"));

    // This node should cause "GOOG_SCOPE_ALIAS_REDEFINED" error
    functionExpression.addChild(aliasDefinition1);

    // This node should cause "GOOG_SCOPE_NON_ALIAS_LOCAL" error
    functionExpression.addChild(new VarNode("local", null, new IdentifierNode("local"), null, null, null, new ArrayNode(new Node[] {}), null, new SourcePosition(5, 0, fileName, 5, 0)));

    root.addChild(new ReturnStatementNode(new ArrayNode(new Node[] {functionExpression})));

    return root;
  }
}