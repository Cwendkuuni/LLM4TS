package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.AliasTransformation;
import org.apache.commons.cli.AliasTransformationHandler;
import org.apache.commons.cli.CompilationUnitTest;
import org.apache.commons.cli.ScopedAliases;
import org.apache.commons.cli.util.NodeUtil;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.diagnostics.DiagnosticType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScopedAliasesTest extends CompilationUnitTest {

    @Test
    public void testScopedAliases() {
        AbstractCompiler compiler = newNoTypeCheckCompiler();
        CompilerOptions options = newCompilerOptions();
        ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new TestAliasTransformationHandler());

        String sourceCode = "goog.scope(function() {" +
                "  var goog = goog || {};" +
                "  goog.dom = {};" +
                "  goog.dom.createElement = function(tagName) {" +
                "    return document.createElement(tagName);" +
                "  };" +
                "});";

        javascriptSourceRoot("TestSourceRoot.java");
        javascriptSource(sourceCode);
        compileWithErrors(options);

        List<DiagnosticType> diagnostics = getDiagnostics();
        assertEquals(0, diagnostics.size());

        Node rootNode = getCompilationUnit().getRootNode();
        assertFalse(rootNode.hasChild());
        Node googScopeCall = NodeUtil.findFirstDescendantByType(rootNode, Node.CALL);
        assertEquals("goog.scope", googScopeCall.getQualifiedName().getIdentifier());

        Node googDom = NodeUtil.findFirstDescendantByType(rootNode, Node.NAME, "goog.dom");
        assertEquals("goog.dom", googDom.getQualifiedName().getIdentifier());
        assertEquals("Document", NodeUtil.getFunctionName(googDom.getParent()));
        assertEquals("createElement", googDom.getLastChild().getIdentifier());

        Traversal traversal = new Traversal();
        NodeTraversal.traverse(scopedAliases, rootNode, traversal);
        assertFalse(traversal.hasErrors());
        assertEquals(1, traversal.getScopeCalls().size());
        assertEquals(2, traversal.getAliasDefinitionsInOrder().size());

        Node aliasDefinitionNode1 = traversal.getAliasDefinitionsInOrder().get(0);
        assertEquals("goog", aliasDefinitionNode1.getFirstChild());
        assertEquals("goog", aliasDefinitionNode1.getLastChild().getIdentifier());
        assertEquals("goog", aliasDefinitionNode1.getLastChild().getString());

        Node aliasDefinitionNode2 = traversal.getAliasDefinitionsInOrder().get(1);
        assertEquals("dom", aliasDefinitionNode2.getFirstChild());
        assertEquals("Document", aliasDefinitionNode2.getParent().getFirstChild());
        assertEquals("Document.prototype", aliasDefinitionNode2.getParent().getString());
        assertEquals("document.createElement", aliasDefinitionNode2.getLastChild().getIdentifier());
        assertEquals("createElement", aliasDefinitionNode2.getLastChild().getIdentifier());
        assertEquals("createElement", aliasDefinitionNode2.getLastChild().getString());
    }

    @Test
    public void testScopedAliasesWithNameShadowing() {
        AbstractCompiler compiler = newNoTypeCheckCompiler();
        CompilerOptions options = newCompilerOptions();
        ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new TestAliasTransformationHandler());

        String sourceCode = "goog.scope(function() {" +
                "  var goog = goog || {};" +
                "  goog.dom = {};" +
                "  goog.dom.createElement = function(tagName) {" +
                "    return document.createElement(tagName);" +
                "  };" +
                "  var goog2 = goog || {};" +
                "});";

        javascriptSourceRoot("TestSourceRoot.java");
        javascriptSource(sourceCode);
        compileWithErrors(options);

        List<DiagnosticType> diagnostics = getDiagnostics();
        assertEquals(1, diagnostics.size());
        assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", diagnostics.get(0).getType());

        Node rootNode = getCompilationUnit().getRootNode();
        assertFalse(rootNode.hasChild());
        Node googScopeCall = NodeUtil.findFirstDescendantByType(rootNode, Node.CALL);
        assertEquals("goog.scope", googScopeCall.getQualifiedName().getIdentifier());

        Node googDom = NodeUtil.findFirstDescendantByType(rootNode, Node.NAME, "goog.dom");
        assertEquals("goog.dom", googDom.getQualifiedName().getIdentifier());
        assertEquals("goog", googDom.getParent());

        Traversal traversal = new Traversal();
        NodeTraversal.traverse(scopedAliases, rootNode, traversal);
        assertFalse(traversal.hasErrors());
        assertEquals(1, traversal.getScopeCalls().size());
        assertEquals(2, traversal.getAliasDefinitionsInOrder().size());

        Node aliasDefinitionNode1 = traversal.getAliasDefinitionsInOrder().get(0);
        assertEquals("goog", aliasDefinitionNode1.getFirstChild());
        assertEquals("goog", aliasDefinitionNode1.getLastChild().getIdentifier());
        assertEquals("goog", aliasDefinitionNode1.getLastChild().getString());

        Node aliasDefinitionNode2 = traversal.getAliasDefinitionsInOrder().get(1);
        assertEquals("dom", aliasDefinitionNode2.getFirstChild());
        assertEquals("Document", aliasDefinitionNode2.getParent().getFirstChild());
        assertEquals("Document.prototype", aliasDefinitionNode2.getParent().getString());
        assertEquals("document.createElement", aliasDefinitionNode2.getLastChild().getIdentifier());
        assertEquals("createElement", aliasDefinitionNode2.getLastChild().getIdentifier());
        assertEquals("createElement", aliasDefinitionNode2.getLastChild().getString());
    }

    @Test
    public void testScopedAliasesWithError() {
        AbstractCompiler compiler = newNoTypeCheckCompiler();
        CompilerOptions options = newCompilerOptions();
        ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new TestAliasTransformationHandler());

        String sourceCode = "goog.scope(function() {" +
                "  var goog = goog || {};" +
                "  var x = goog.x;" +
                "});";

        javascriptSourceRoot("TestSourceRoot.java");
        javascriptSource(sourceCode);
        compileWithErrors(options);

        List<DiagnosticType> diagnostics = getDiagnostics();
        assertEquals(1, diagnostics.size());
        assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", diagnostics.get(0).getType());

        Node rootNode = getCompilationUnit().getRootNode();
        assertFalse(rootNode.hasChild());
        Node googScopeCall = NodeUtil.findFirstDescendantByType(rootNode, Node.CALL);
        assertEquals("goog.scope", googScopeCall.getQualifiedName().getIdentifier());

        Traversal traversal = new Traversal();
        NodeTraversal.traverse(scopedAliases, rootNode, traversal);
        assertFalse(traversal.hasErrors());
        assertEquals(1, traversal.getScopeCalls().size());
        assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    }

    @Test
    public void testScopedAliasesWithoutScopeCall() {
        AbstractCompiler compiler = newNoTypeCheckCompiler();
        CompilerOptions options = newCompilerOptions();
        ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new TestAliasTransformationHandler());

        String sourceCode = "goog.scope(function() {" +
                "  var goog = goog || {};" +
                "});";

        javascriptSourceRoot("TestSourceRoot.java");
        javascriptSource(sourceCode);
        compileWithErrors(options);

        List<DiagnosticType> diagnostics = getDiagnostics();
        assertEquals(1, diagnostics.size());
        assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostics.get(0).getType());

        Node rootNode = getCompilationUnit().getRootNode();
        assertFalse(rootNode.hasChild());
        assertNull(NodeUtil.findFirstDescendantByType(rootNode, Node.CALL, "goog.scope"));
    }

    @Test
    public void testScopedAliasesWithReturn() {
        AbstractCompiler compiler = newNoTypeCheckCompiler();
        CompilerOptions options = newCompilerOptions();
        ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new TestAliasTransformationHandler());

        String sourceCode = "goog.scope(function() {" +
                "  var goog = goog || {};" +
                "  return goog;" +
                "});";

        javascriptSourceRoot("TestSourceRoot.java");
        javascriptSource(sourceCode);
        compileWithErrors(options);

        List<DiagnosticType> diagnostics = getDiagnostics();
        assertEquals(1, diagnostics.size());
        assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostics.get(0).getType());

        Node rootNode = getCompilationUnit().getRootNode();
        assertFalse(rootNode.hasChild());
        Node googScopeCall = NodeUtil.findFirstDescendantByType(rootNode, Node.CALL);
        assertEquals("goog.scope", googScopeCall.getQualifiedName().getIdentifier());
    }

    @Test
    public void testScopedAliasesWithThrow() {
        AbstractCompiler compiler = newNoTypeCheckCompiler();
        CompilerOptions options = newCompilerOptions();
        ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new TestAliasTransformationHandler());

        String sourceCode = "goog.scope(function() {" +
                "  var goog = goog || {};" +
                "  throw new Error('Test');" +
                "});";

        javascriptSourceRoot("TestSourceRoot.java");
        javascriptSource(sourceCode);
        compileWithErrors(options);

        List<DiagnosticType> diagnostics = getDiagnostics();
        assertEquals(1, diagnostics.size());
        assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostics.get(0).getType());

        Node rootNode = getCompilationUnit().getRootNode();
        assertFalse(rootNode.hasChild());
        Node googScopeCall = NodeUtil.findFirstDescendantByType(rootNode, Node.CALL);
        assertEquals("goog.scope", googScopeCall.getQualifiedName().getIdentifier());
    }

    private static class TestAliasTransformationHandler implements AliasTransformationHandler {
        @Override
        public AliasTransformation logAliasTransformation(String sourceFileName, SourcePosition sourcePosition) {
            return new AliasTransformation("TestAliasTransformation");
        }
    }
}