package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import org.junit.Test;

/**
 * Test for {@link ScopedAliases} class
 */
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    // Given
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName,
          SourcePosition position) {
        return new AliasTransformation() {
          @Override
          public void apply(Node root) {
            // Do nothing
          }

          @Override
          public void dispose() {
            // Do nothing
          }
        };
      }
    };
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    String testCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var element = dom.createElement(DIV);" +
        "});";

    // When
    Node root = NodeFactory.createCode(testCode);
    scopedAliases.process(null, root);

    // Then
    NodeElement elementNode = root.getFirstChild().getLastChild();
    Node tagNameNode = elementNode.getLastChild();

    // The 'DIV' alias should have been replaced with 'goog.dom.TagName.DIV'
    String tagName = tagNameNode.getString();
    assertTrue(tagName.equals("goog.dom.TagName.DIV"));

    // No errors should have been reported during the processing
    assertFalse(compiler.hasErrors());
  }

  @Test
  public void testScopedAliasesWithErrors() {
    // Given
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName,
          SourcePosition position) {
        return new AliasTransformation() {
          @Override
          public void apply(Node root) {
            // Do nothing
          }

          @Override
          public void dispose() {
            // Do nothing
          }
        };
      }
    };
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    String testCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  function DIV() {}" +
        "  var element = dom.createElement(DIV);" +
        "});";

    // When
    Node root = NodeFactory.createCode(testCode);
    scopedAliases.process(null, root);

    // Then
    // An error should have been reported during the processing
    assertTrue(compiler.hasErrors());

    // The 'DIV' alias should not have been replaced
    Node elementNode = root.getFirstChild().getLastChild();
    Node tagNameNode = elementNode.getLastChild();

    String tagName = tagNameNode.getString();
    assertFalse(tagName.startsWith("goog.dom."));
  }

  @Test
  public void testScopedAliasesWithNestedScopes() {
    // Given
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName,
          SourcePosition position) {
        return new AliasTransformation() {
          @Override
          public void apply(Node root) {
            // Do nothing
          }

          @Override
          public void dispose() {
            // Do nothing
          }
        };
      }
    };
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    String testCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  goog.scope(function() {" +
        "    var DIV = goog.dom.TagName.DIV;" +
        "  });" +
        "  var element = dom.createElement(DIV);" +
        "});";

    // When
    Node root = NodeFactory.createCode(testCode);
    scopedAliases.process(null, root);

    // Then
    // The 'DIV' alias should have been replaced with 'goog.dom.TagName.DIV'
    Node elementNode = root.getFirstChild().getLastChild();
    Node tagNameNode = elementNode.getLastChild();

    String tagName = tagNameNode.getString();
    assertTrue(tagName.equals("goog.dom.TagName.DIV"));

    // No errors should have been reported during the processing
    assertFalse(compiler.hasErrors());
  }

  @Test
  public void testScopedAliasesWithThis() {
    // Given
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName,
          SourcePosition position) {
        return new AliasTransformation() {
          @Override
          public void apply(Node root) {
            // Do nothing
          }

          @Override
          public void dispose() {
            // Do nothing
          }
        };
      }
    };
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    String testCode = "goog.scope(function() {" +
        "  this.dom = goog.dom;" +
        "});";

    // When
    Node root = NodeFactory.createCode(testCode);
    scopedAliases.process(null, root);

    // Then
    // A 'GOOG_SCOPE_REFERENCES_THIS' error should have been reported
    assertTrue(compiler.hasErrorMessages().get(0).getMessage()
        .equals("The body of a goog.scope function cannot reference 'this'."));
  }

  @Test
  public void testScopedAliasesWithReturn() {
    // Given
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName,
          SourcePosition position) {
        return new AliasTransformation() {
          @Override
          public void apply(Node root) {
            // Do nothing
          }

          @Override
          public void dispose() {
            // Do nothing
          }
        };
      }
    };
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    String testCode = "goog.scope(function() {" +
        "  return goog.dom.createElement('DIV');" +
        "});";

    // When
    Node root = NodeFactory.createCode(testCode);
    scopedAliases.process(null, root);

    // Then
    // A 'GOOG_SCOPE_USES_RETURN' error should have been reported
    assertTrue(compiler.hasErrorMessages().get(0).getMessage()
        .equals("The body of a goog.scope function cannot use 'return'."));
  }

  @Test
  public void testScopedAliasesWithThrow() {
    // Given
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName,
          SourcePosition position) {
        return new AliasTransformation() {
          @Override
          public void apply(Node root) {
            // Do nothing
          }

          @Override
          public void dispose() {
            // Do nothing
          }
        };
      }
    };
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    String testCode = "goog.scope(function() {" +
        "  throw new Error('test');" +
        "});";

    // When
    Node root = NodeFactory.createCode(testCode);
    scopedAliases.process(null, root);

    // Then
    // A 'GOOG_SCOPE_USES_THROW' error should have been reported
    assertTrue(compiler.hasErrorMessages().get(0).getMessage()
        .equals("The body of a goog.scope function cannot use 'throw'."));
  }
}