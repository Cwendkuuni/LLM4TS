package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ScopedAliasesTest {
    @Test
    public void testScopedAliasesProcess() {
        AbstractCompiler compiler = new CompilerOptions.NullCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
        AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
            @Override
            public AliasTransformation logAliasTransformation(
                    String sourceFileName, SourcePosition position) {
                AliasTransformation aliasTransformation = new AliasTransformation();
                aliasTransformation.setSourceFileName(sourceFileName);
                aliasTransformation.setPosition(position);
                return aliasTransformation;
            }
        };

        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

        Node rootNode = Node.createTree("(function() {" +
                "  var dom = goog.dom;" +
                "  var DIV = dom.TagName.DIV;" +
                "  goog.scope(function() {" +
                "    var foo = 'foo';" +
                "    goog.dom.createElement(DIV);" +
                "  });" +
                "  goog.dom.createElement(DIV);" +
                "})()");

        scopedAliases.process(null, rootNode);

        assertFalse(scopedAliases.hasErrors());

        List<Node> scopeCalls = scopedAliases.getScopeCalls();
        assertEquals(1, scopeCalls.size());
        Node scopeCallNode = scopeCalls.get(0);
        assertEquals(Token.CALL, scopeCallNode.getType());
        assertTrue(SCOPING_METHOD_NAME.equals(scopeCallNode.getFirstChild().getQualifiedName()));

        List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        assertEquals(1, aliasDefinitionsInOrder.size());
        Node aliasDefinitionNode = aliasDefinitionsInOrder.get(0);
        assertEquals(Token.VAR, aliasDefinitionNode.getType());
        assertEquals("foo", aliasDefinitionNode.getFirstChild().getString());
        assertEquals(Token.ASSIGN, aliasDefinitionNode.getLastChild().getType());

        List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
        assertEquals(2, aliasUsages.size());
        AliasUsage aliasUsage1 = aliasUsages.get(0);
        assertTrue(aliasUsage1 instanceof AliasedNode);
        AliasedNode aliasNode1 = (AliasedNode) aliasUsage1;
        assertEquals(Token.VAR, aliasNode1.aliasReference.getType());
        assertEquals("dom", aliasNode1.aliasReference.getString());
        assertEquals(Token.NAME, aliasNode1.aliasDefinition.getType());
        assertEquals("goog.dom", aliasNode1.aliasDefinition.getString());

        AliasUsage aliasUsage2 = aliasUsages.get(1);
        assertTrue(aliasUsage2 instanceof AliasedTypeNode);
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage2;
        assertEquals(Token.STRING, aliasedTypeNode.aliasReference.getType());
        assertEquals("DIV", aliasedTypeNode.aliasReference.getString());
        assertEquals("goog.dom.TagName.DIV", aliasedTypeNode.correctedType);
    }

    @Test
    public void testScopedAliasesProcessWithErrors() {
        AbstractCompiler compiler = new CompilerOptions.NullCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
        AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {
            @Override
            public AliasTransformation logAliasTransformation(
                    String sourceFileName, SourcePosition position) {
                throw new RuntimeException("Unexpected error");
            }
        };

        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

        Node rootNode = Node.createTree("(function() {" +
                "  var dom = goog.dom;" +
                "  var DIV = dom.TagName.DIV;" +
                "  goog.scope(function() {" +
                "    var foo = 'foo';" +
                "    var bar = 42;" + // This should cause an error
                "  });" +
                "  goog.dom.createElement(DIV);" +
                "})()");

        scopedAliases.process(null, rootNode);

        assertTrue(scopedAliases.hasErrors());
    }
}