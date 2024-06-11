package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

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

public class ScopedAliasesTest {
  @Test
  public void testSimpleAlias() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String sourceFileName, SourcePosition sourcePosition) {
        return new AliasTransformation() {
          @Override
          public void applyTransformation(Node externs, Node root) {
            NodeUtil.tryMergeBlock(
                root.getLastChild().getLastChild());
          }

          @Override
          public String[] getAlternateNames() {
            return new String[] {"GOOG_SCOPE_ALIAS"};
          }

          @Override
          public String getName() {
            return "GOOG_SCOPE_ALIAS";
          }

          @Override
          public DiagnosticType getErrorType() {
            return null;
          }

          @Override
          public void reportError(NodeTraversal nt, DiagnosticType error,
              String... arguments) {
            // Do nothing.
          }
        };
      }
    };
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    Node root = new Node(Token.SCRIPT);
    Node googScope = new Node(Token.CALL, new Node(Token.NAME, "goog"),
        new Node(Token.FUNCTION, new Node(Token.NAME, "anonymous"),
            new Node[0]));
    Node aliasDefinition = new Node(Token.ASSIGN, new Node(Token.NAME, "dom"),
        new Node(Token.NAME, "goog.dom"));
    googScope.setParent(root);
    aliasDefinition.setParent(googScope);
    root.setParent(null);

    scopedAliases.process(null, root);

    assertEquals("goog.dom", root.getLastChild().getLastChild().getFirstChild()
        .getString());
    assertSame(googScope.getLastChild(), aliasDefinition);
    assertNull(googScope.getPreviousSibling());
  }

  @Test
  public void testMultipleAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String sourceFileName, SourcePosition sourcePosition) {
        return new AliasTransformation() {
          @Override
          public void applyTransformation(Node externs, Node root) {
            NodeUtil.tryMergeBlock(
                root.getLastChild().getLastChild());
          }

          @Override
          public String[] getAlternateNames() {
            return new String[] {"GOOG_SCOPE_ALIAS_1", "GOOG_SCOPE_ALIAS_2"};
          }

          @Override
          public String getName() {
            return "GOOG_SCOPE_ALIAS";
          }

          @Override
          public DiagnosticType getErrorType() {
            return null;
          }

          @Override
          public void reportError(NodeTraversal nt, DiagnosticType error,
              String... arguments) {
            // Do nothing.
          }
        };
      }
    };
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    Node root = new Node(Token.SCRIPT);
    Node googScope = new Node(Token.CALL, new Node(Token.NAME, "goog"),
        new Node(Token.FUNCTION, new Node(Token.NAME, "anonymous"),
            new Node[0]));
    Node aliasDefinition1 = new Node(Token.ASSIGN, new Node(Token.NAME, "dom"),
        new Node(Token.NAME, "goog.dom"));
    Node aliasDefinition2 = new Node(Token.ASSIGN, new Node(Token.NAME, "doc"),
        new Node(Token.NAME, "document"));
    googScope.setParent(root);
    aliasDefinition1.setParent(googScope);
    aliasDefinition2.setParent(googScope);
    root.setParent(null);

    scopedAliases.process(null, root);

    assertEquals("document", root.getLastChild().getLastChild().getLastChild()
        .getFirstChild().getString());
    assertEquals("goog.dom", root.getLastChild().getLastChild().getLastChild()
        .getLastChild().getFirstChild().getString());
  }

  @Test
  public void testErrorCases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(
          String sourceFileName, SourcePosition sourcePosition) {
        return null;
      }
    };
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    Node root = new Node(Token.SCRIPT);
    Node googScope = new Node(Token.CALL, new Node(Token.NAME, "goog"),
        new Node(Token.FUNCTION, new Node(Token.NAME, "anonymous"),
            new Node[0]));
    // goog.scope should not have any children except for the anonymous function.
    Node aliasDefinition = new Node(Token.ASSIGN, new Node(Token.NAME, "dom"),
        new Node(Token.NAME, "goog.dom"));
    // goog.scope should not have any siblings or previous siblings.
    Node extraChild = new Node(Token.NAME, "extra");
    Node extraSibling = new Node(Token.NAME, "extraSibling");
    Node extraPrevious = new Node(Token.NAME, "extraPrevious");
    googScope.setParent(root);
    aliasDefinition.setParent(googScope);
    extraChild.setParent(googScope);
    extraSibling.setParent(root);
    extraPrevious.setParent(googScope);
    root.setParent(null);

    scopedAliases.process(null, root);

    // TODO: Implement assertions for specific error messages.
  }
}