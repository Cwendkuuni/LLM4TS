package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test basic functionality
    testBasicFunctionality();

    // Test error cases
    testErrorCases();
  }

  private void testBasicFunctionality() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mockHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = new Node(Token.SCRIPT, "test", null);
    root.addChild(new Node(Token.BLOCK, null, root));

    // Test with valid goog.scope block
    Node googScopeBlock = new Node(Token.CALL, null, root);
    googScopeBlock.addChild(new Node(Token.IDENT, "goog.scope", null, null));
    googScopeBlock.addChild(new Node(Token.OPEN_BRACE, null, null));
    Node aliasDefinition = new Node(Token.VAR, null, null);
    aliasDefinition.addChild(new Node(Token.IDENT, "dom", null, null));
    aliasDefinition.addChild(new Node(Token.ASSIGN, null, null));
    aliasDefinition.addChild(new Node(Token.IDENT, "goog.dom", null, null));
    googScopeBlock.addChild(aliasDefinition);
    Node statement = new Node(Token.EXPR_STATEMENT, null, null);
    statement.addChild(new Node(Token.CALL, null, null));
    statement.addChild(new Node(Token.IDENT, "dom.createElement", null, null));
    statement.addChild(new Node(Token.STRING, "DIV", null, null));
    googScopeBlock.addChild(statement);
    googScopeBlock.addChild(new Node(Token.CLOSE_BRACE, null, null));
    root.addChild(googScopeBlock);

    scopedAliases.process(null, root);

    // Check that the alias definition has been replaced
    assertTrue(root.getFirstChild().getLastChild().isExpressionNode());
    Node replacedAliasDefinition = root.getFirstChild().getLastChild();
    assertTrue(replacedAliasDefinition.getFirstChild().isCallNode());
    assertTrue(replacedAliasDefinition.getFirstChild().getFirstChild().isIdentifier());
    assertTrue(replacedAliasDefinition.getFirstChild().getFirstChild().getString().equals("goog.dom"));

    // Check that the alias usage has been replaced
    assertTrue(root.getChildAtIndex(1).isExpressionStatement());
    Node replacedAliasUsage = root.getChildAtIndex(1).getExpression();
    assertTrue(replacedAliasUsage.isCallNode());
    assertTrue(replacedAliasUsage.getFirstChild().isIdentifier());
    assertTrue(replacedAliasUsage.getFirstChild().getString().equals("dom.createElement"));

    // Test with multiple aliases in the same goog.scope block
    resetMocks(transformationHandler);

    root = new Node(Token.SCRIPT, "test", null);
    root.addChild(new Node(Token.BLOCK, null, root));

    googScopeBlock = new Node(Token.CALL, null, root);
    googScopeBlock.addChild(new Node(Token.IDENT, "goog.scope", null, null));
    googScopeBlock.addChild(new Node(Token.OPEN_BRACE, null, null));

    aliasDefinition = new Node(Token.VAR, null, null);
    aliasDefinition.addChild(new Node(Token.IDENT, "dom", null, null));
    aliasDefinition.addChild(new Node(Token.ASSIGN, null, null));
    aliasDefinition.addChild(new Node(Token.IDENT, "goog.dom", null, null));
    googScopeBlock.addChild(aliasDefinition);

    aliasDefinition = new Node(Token.VAR, null, null);
    aliasDefinition.addChild(new Node(Token.IDENT, "DIV", null, null));
    aliasDefinition.addChild(new Node(Token.ASSIGN, null, null));
    aliasDefinition.addChild(new Node(Token.IDENT, "goog.dom.TagName.DIV", null, null));
    googScopeBlock.addChild(aliasDefinition);

    statement = new Node(Token.EXPR_STATEMENT, null, null);
    statement.addChild(new Node(Token.CALL, null, null));
    statement.addChild(new Node(Token.IDENT, "dom.createElement", null, null));
    statement.addChild(new Node(Token.STRING, "DIV", null, null));
    googScopeBlock.addChild(statement);

    googScopeBlock.addChild(new Node(Token.CLOSE_BRACE, null, null));
    root.addChild(googScopeBlock);

    scopedAliases.process(null, root);

    // Check that both aliases have been replaced
    assertTrue(root.getFirstChild().getLastChild().isExpressionNode());
    replacedAliasDefinition = root.getFirstChild().getLastChild();
    assertTrue(replacedAliasDefinition.getChildCount() > 1);
    Node firstReplacedAliasDefinition = replacedAliasDefinition.getChildAtIndex(0);
    assertTrue(firstReplacedAliasDefinition.getFirstChild().isIdentifier());
    assertTrue(firstReplacedAliasDefinition.getFirstChild().getString().equals("goog.dom"));

    Node secondReplacedAliasDefinition = replacedAliasDefinition.getChildAtIndex(1);
    assertTrue(secondReplacedAliasDefinition.getFirstChild().isIdentifier());
    assertTrue(secondReplacedAliasDefinition.getFirstChild().getString().equals("goog.dom.TagName.DIV"));

    // Test with multiple goog.scope blocks within the same script
    resetMocks(transformationHandler);

    root = new Node(Token.SCRIPT, "test", null);
    root.addChild(new Node(Token.BLOCK, null, root));

    googScopeBlock = new Node(Token.CALL, null, root);
    googScopeBlock.addChild(new Node(Token.IDENT, "goog.scope", null, null));
    googScopeBlock.addChild(new Node(Token.OPEN_BRACE, null, null));

    aliasDefinition = new Node(Token.VAR, null, null);
    aliasDefinition.addChild(new Node(Token.IDENT, "dom", null, null));
    aliasDefinition.addChild(new Node(Token.ASSIGN, null, null));
    aliasDefinition.addChild(new Node(Token.IDENT, "goog.dom", null, null));
    googScopeBlock.addChild(aliasDefinition);

    statement = new Node(Token.EXPR_STATEMENT, null, null);
    statement.addChild(new Node(Token.CALL, null, null));
    statement.addChild(new Node(Token.IDENT, "dom.createElement", null, null));
    statement.addChild(new Node(Token.STRING, "DIV", null, null));
    googScopeBlock.addChild(statement);
    googScopeBlock.addChild(new Node(Token.CLOSE_BRACE, null, null));

    googScopeBlock = new Node(Token.CALL, null, root);
    googScopeBlock.addChild(new Node(Token.IDENT, "goog.scope", null, null));
    googScopeBlock.addChild(new Node(Token.OPEN_BRACE, null, null));

    aliasDefinition = new Node(Token.VAR, null, null);
    aliasDefinition.addChild(new Node(Token.IDENT, "div", null, null));
    aliasDefinition.addChild(new Node(Token.ASSIGN, null, null));
    aliasDefinition.addChild(new Node(Token.IDENT, "goog.dom.TagName.DIV", null, null));
    googScopeBlock.addChild(aliasDefinition);

    statement = new Node(Token.EXPR_STATEMENT, null, null);
    statement.addChild(new Node(Token.CALL, null, null));
    statement.addChild(new Node(Token.IDENT, "dom.createElement", null, null));
    statement.addChild(new Node(Token.STRING, "DIV", null, null));
    googScopeBlock.addChild(statement);

    googScopeBlock.addChild(new Node(Token.CLOSE_BRACE, null, null));
    root.addChild(googScopeBlock);
    root.addChild(googScopeBlock);

    scopedAliases.process(null, root);

    // Check that both sets of aliases have been replaced
    assertTrue(root.getChildAtIndex(0).getLastChild().isExpressionNode());
    replacedAliasDefinition = root.getChildAtIndex(0).getLastChild();
    assertTrue(replacedAliasDefinition.getChildCount() > 1);
    firstReplacedAliasDefinition = replacedAliasDefinition.getChildAtIndex(0);
    assertTrue(firstReplacedAliasDefinition.getFirstChild().isIdentifier());
    assertTrue(firstReplacedAliasDefinition.getFirstChild().getString().equals("goog.dom"));

    secondReplacedAliasDefinition = replacedAliasDefinition.getChildAtIndex(1);
    assertTrue(secondReplacedAliasDefinition.getFirstChild().isIdentifier());
    assertTrue(secondReplacedAliasDefinition.getFirstChild().getString().equals("goog.dom.TagName.DIV"));

    assertTrue(root.getChildAtIndex(1).getLastChild().isExpressionNode());
    replacedAliasDefinition = root.getChildAtIndex(1).getLastChild();
    assertTrue(replacedAliasDefinition.getChildCount() > 1);
    firstReplacedAliasDefinition = replacedAliasDefinition.getChildAtIndex(0);
    assertTrue(firstReplacedAliasDefinition.getFirstChild().isIdentifier());
    assertTrue(firstReplacedAliasDefinition.getFirstChild().getString().equals("goog.dom"));

    secondReplacedAliasDefinition = replacedAliasDefinition.getChildAtIndex(1);
    assertTrue(secondReplacedAliasDefinition.getFirstChild().isIdentifier());
    assertTrue(secondReplacedAliasDefinition.getFirstChild().getString().equals("goog.dom.TagName.DIV"));
  }

  private void testErrorCases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mockHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = new Node(Token.SCRIPT, "test", null);
    root.addChild(new Node(Token.BLOCK, null, root));

    // Test goog.scope used improperly
    Node improperlyUsedScopeBlock = new Node(Token.CALL, null, root);
    improperlyUsedScopeBlock.addChild(new Node(Token.IDENT, "goog.scope", null, null));
    Node improperlyUsedStatement = new Node(Token.EXPR_STATEMENT, null, null);
    improperlyUsedStatement.addChild(new Node(Token.CALL, null, null));
    improperlyUsedStatement.addChild(new Node(Token.IDENT, "dom.createElement", null, null));
    improperlyUsedStatement.addChild(new Node(Token.STRING, "DIV", null, null));
    improperlyUsedScopeBlock.addChild(improperlyUsedStatement);
    root.addChild(improperlyUsedScopeBlock);

    scopedAliases.process(null, root);

    // Check that an error has been reported
    assertTrue(compiler.getDiagnostics().hasError(GOOG_SCOPE_USED_IMPROPERLY));

    // Test goog.scope with multiple parameters
    Node multipleParameterScopeBlock = new Node(Token.CALL, null, root);
    multipleParameterScopeBlock.addChild(new Node(Token.IDENT, "goog.scope", null, null));
    multipleParameterScopeBlock.addChild(new Node(Token.OPEN_BRACE, null, null));
    Node multipleParameterStatement = new Node(Token.EXPR_STATEMENT, null, null);
    multipleParameterStatement.addChild(new Node(Token.CALL, null, null));
    multipleParameterStatement.addChild(new Node(Token.IDENT, "dom.createElement", null, null));
    multipleParameterStatement.addChild(new Node(Token.STRING, "DIV", null, null));
    multipleParameterScopeBlock.addChild(multipleParameterStatement);
    multipleParameterScopeBlock.addChild(new Node(Token.CLOSE_BRACE, null, null));
    root.addChild(multipleParameterScopeBlock);

    scopedAliases.process(null, root);

    // Check that an error has been reported
    assertTrue(compiler.getDiagnostics().hasError(GOOG_SCOPE_HAS_BAD_PARAMETERS));

    // Test goog.scope with a function body that references 'this'
    Node referenceThisScopeBlock = new Node(Token.CALL, null, root);
    referenceThisScopeBlock.addChild(new Node(Token.IDENT, "goog.scope", null, null));
    referenceThisScopeBlock.addChild(new Node(Token.OPEN_BRACE, null, null));
    Node referenceThisStatement = new Node(Token.EXPR_STATEMENT, null, null);
    referenceThisStatement.addChild(new Node(Token.IDENT, "this", null, null));
    referenceThisStatement.addChild(new Node(Token.DOT, null, null));
    referenceThisStatement.addChild(new Node(Token.IDENT, "createElement", null, null));
    referenceThisStatement.addChild(new Node(Token.STRING, "DIV", null, null));
    referenceThisScopeBlock.addChild(referenceThisStatement);
    referenceThisScopeBlock.addChild(new Node(Token.CLOSE_BRACE, null, null));
    root.addChild(referenceThisScopeBlock);

    scopedAliases.process(null, root);

    // Check that an error has been reported
    assertTrue(compiler.getDiagnostics().hasError(GOOG_SCOPE_REFERENCES_THIS));
  }

  private AliasTransformationHandler mockHandler() {
    AliasTransformationHandler handler = mock(AliasTransformationHandler.class);
    when(handler.logAliasTransformation(eq("test.js"), any(SourcePosition.class)))
        .thenReturn(new AliasTransformation("dom", "goog.dom"));
    when(handler.logAliasTransformation(eq("test.js"), any(SourcePosition.class)))
        .thenReturn(new AliasTransformation("DIV", "goog.dom.TagName.DIV"));
    return handler;
  }

  private void resetMocks(AliasTransformationHandler handler) {
    reset(handler);
  }

  @Nullable
  private DiagnosticType GOOG_SCOPE_USED_IMPROPERLY = DiagnosticType.error(
      "JSC_GOOG_SCOPE_USED_IMPROPERLY",
      "The call to goog.scope must be alone in a single statement.");

  @Nullable
  private DiagnosticType GOOG_SCOPE_HAS_BAD_PARAMETERS =
      DiagnosticType.error(
          "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
          "The call to goog.scope must take only a single parameter.  It must" +
              " be an anonymous function that itself takes no parameters.");

  @Nullable
  private DiagnosticType GOOG_SCOPE_REFERENCES_THIS = DiagnosticType.error(
      "JSC_GOOG_SCOPE_REFERENCES_THIS",
      "The body of a goog.scope function cannot reference 'this'.");
}