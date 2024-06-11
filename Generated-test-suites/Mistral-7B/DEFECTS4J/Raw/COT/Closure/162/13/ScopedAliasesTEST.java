package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    

    // Prepare test data
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test empty case
    List<Node> rootNodes = Lists.newArrayList();
    scopedAliases.process(null, rootNodes.get(0));
    assertFalse(scopedAliases.hasErrors());

    // Test invalid goog.scope usage
    Node invalidGoogScopeNode = new Node(Token.CALL, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        new Node(Token.IDENTIFIER, "scope", null),
        new Node(Token.FUNCTION, null, new Node[0])
    });
    Node invalidGoogScopeParentNode = new Node(Token.ASSIGN, null, new Node[] {
        new Node(Token.IDENTIFIER, "result", null),
        invalidGoogScopeNode
    });
    List<Node> invalidGoogScopeRootNodes = Lists.newArrayList();
    invalidGoogScopeRootNodes.add(invalidGoogScopeParentNode);
    scopedAliases.process(null, invalidGoogScopeRootNodes.get(0));
    assertTrue(scopedAliases.hasErrors());

    // Test invalid goog.scope parameters
    Node invalidGoogScopeFunctionNode = new Node(Token.FUNCTION, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        new Node(Token.IDENTIFIER, "scope", null),
        new Node(Token.BLOCK, null, new Node[0])
    });
    Node invalidGoogScopeCallNode = new Node(Token.CALL, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        invalidGoogScopeFunctionNode
    });
    List<Node> invalidGoogScopeRootNodes = Lists.newArrayList();
    invalidGoogScopeRootNodes.add(invalidGoogScopeCallNode);
    scopedAliases.process(null, invalidGoogScopeRootNodes.get(0));
    assertTrue(scopedAliases.hasErrors());

    // Test invalid goog.scope body reference to 'this'
    Node invalidThisReferenceNode = new Node(Token.IDENTIFIER, "this", null);
    Node invalidGoogScopeBodyNode = new Node(Token.ASSIGN, null, new Node[] {
        new Node(Token.IDENTIFIER, "result", null),
        new Node(Token.IDENTIFIER, "x", null),
        invalidThisReferenceNode
    });
    Node invalidGoogScopeFunctionNode = new Node(Token.FUNCTION, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        new Node(Token.IDENTIFIER, "scope", null),
        new Node(Token.BLOCK, null, new Node[] { invalidGoogScopeBodyNode })
    });
    Node invalidGoogScopeCallNode = new Node(Token.CALL, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        invalidGoogScopeFunctionNode
    });
    List<Node> invalidGoogScopeRootNodes = Lists.newArrayList();
    invalidGoogScopeRootNodes.add(invalidGoogScopeCallNode);
    scopedAliases.process(null, invalidGoogScopeRootNodes.get(0));
    assertTrue(scopedAliases.hasErrors());

    // Test invalid goog.scope body use of 'return'
    Node invalidReturnNode = new Node(Token.RETURN, null, new Node[0]);
    Node invalidGoogScopeBodyNode = new Node(Token.ASSIGN, null, new Node[] {
        new Node(Token.IDENTIFIER, "result", null),
        new Node(Token.IDENTIFIER, "x", null),
        invalidReturnNode
    });
    Node invalidGoogScopeFunctionNode = new Node(Token.FUNCTION, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        new Node(Token.IDENTIFIER, "scope", null),
        new Node(Token.BLOCK, null, new Node[] { invalidGoogScopeBodyNode })
    });
    Node invalidGoogScopeCallNode = new Node(Token.CALL, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        invalidGoogScopeFunctionNode
    });
    List<Node> invalidGoogScopeRootNodes = Lists.newArrayList();
    invalidGoogScopeRootNodes.add(invalidGoogScopeCallNode);
    scopedAliases.process(null, invalidGoogScopeRootNodes.get(0));
    assertTrue(scopedAliases.hasErrors());

    // Test invalid goog.scope body use of 'throw'
    Node invalidThrowNode = new Node(Token.THROW, null, null);
    Node invalidGoogScopeBodyNode = new Node(Token.ASSIGN, null, new Node[] {
        new Node(Token.IDENTIFIER, "result", null),
        new Node(Token.IDENTIFIER, "x", null),
        invalidThrowNode
    });
    Node invalidGoogScopeFunctionNode = new Node(Token.FUNCTION, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        new Node(Token.IDENTIFIER, "scope", null),
        new Node(Token.BLOCK, null, new Node[] { invalidGoogScopeBodyNode })
    });
    Node invalidGoogScopeCallNode = new Node(Token.CALL, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        invalidGoogScopeFunctionNode
    });
    List<Node> invalidGoogScopeRootNodes = Lists.newArrayList();
    invalidGoogScopeRootNodes.add(invalidGoogScopeCallNode);
    scopedAliases.process(null, invalidGoogScopeRootNodes.get(0));
    assertTrue(scopedAliases.hasErrors());

    // Test alias redefinition
    Node aliasRedefinitionNode = new Node(Token.ASSIGN, null, new Node[] {
        new Node(Token.IDENTIFIER, "alias", null),
        new Node(Token.IDENTIFIER, "alias", null)
    });
    Node googScopeNode = new Node(Token.CALL, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        new Node(Token.FUNCTION, null, new Node[] {
            new Node(Token.IDENTIFIER, "scope", null),
            new Node(Token.BLOCK, null, new Node[] { aliasRedefinitionNode })
        })
    });
    List<Node> aliasRedefinitionRootNodes = Lists.newArrayList();
    aliasRedefinitionRootNodes.add(googScopeNode);
    scopedAliases.process(null, aliasRedefinitionRootNodes.get(0));
    assertTrue(scopedAliases.hasErrors());

    // Test alias usage of non-alias local variable
    Node aliasOfNonAliasLocalNode = new Node(Token.ASSIGN, null, new Node[] {
        new Node(Token.IDENTIFIER, "alias", null),
        new Node(Token.IDENTIFIER, "nonAliasLocal", null)
    });
    Node googScopeNode = new Node(Token.CALL, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        new Node(Token.FUNCTION, null, new Node[] {
            new Node(Token.IDENTIFIER, "scope", null),
            new Node(Token.BLOCK, null, new Node[] { aliasOfNonAliasLocalNode })
        })
    });
    List<Node> aliasOfNonAliasLocalRootNodes = Lists.newArrayList();
    aliasOfNonAliasLocalRootNodes.add(googScopeNode);
    scopedAliases.process(null, aliasOfNonAliasLocalRootNodes.get(0));
    assertTrue(scopedAliases.hasErrors());

    // Test alias usage
    Node aliasNode = new Node(Token.IDENTIFIER, "alias", null);
    Node googScopeNode = new Node(Token.CALL, null, new Node[] {
        new Node(Token.IDENTIFIER, "goog", null),
        new Node(Token.FUNCTION, null, new Node[] {
            new Node(Token.IDENTIFIER, "scope", null),
            new Node(Token.BLOCK, null, new Node[] { new Node(Token.ASSIGN, null, new Node[] {
                aliasNode,
                new Node(Token.IDENTIFIER, "aliasValue", null)
            }) })
        })
    });
    List<Node> aliasUsageRootNodes = Lists.newArrayList();
    aliasUsageRootNodes.add(googScopeNode);
    scopedAliases.process(null, aliasUsageRootNodes.get(0));
    assertFalse(scopedAliases.hasErrors());

    // Test alias transformation
    AliasTransformation aliasTransformation = new AliasTransformation();
    assertEquals("goog.scope", aliasTransformation.getName());
    assertEquals("goog", aliasTransformation.getAlias());
    assertEquals("goog.dom.createElement", aliasTransformation.getAliasFor("dom.createElement"));
    assertEquals("goog.dom.TagName.DIV", aliasTransformation.getAliasFor("dom.TagName.DIV"));

    
  }
}