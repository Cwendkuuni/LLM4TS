package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

public class SyntacticScopeCreatorTest {
    private AbstractCompiler compiler;
    private String sourceName;
    private Scope scope;
    private Node node;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        sourceName = null;
        scope = mock(Scope.class);
        node = mock(Node.class);
    }

    @Test
    public void testCreateScope_WithNullParent() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        when(node.getType()).thenReturn(Token.FUNCTION);
        when(node.getProp(Node.SOURCENAME_PROP)).thenReturn(sourceName);

        Scope actualScope = creator.createScope(node, null);

        assertEquals(scope, actualScope);
        verify(scope).setParent(null);
        verify(scope).setRootNode(node);
        verify(compiler).report(JSError.make(sourceName, node, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, "arguments", "??"));
    }

    @Test
    public void testCreateScope_WithNonNullParent() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        when(node.getType()).thenReturn(Token.FUNCTION);
        when(node.getProp(Node.SOURCENAME_PROP)).thenReturn(sourceName);

        Scope actualScope = creator.createScope(node, scope);

        assertEquals(scope, actualScope);
        verify(scope).setParent(scope);
        verify(scope).setRootNode(node);
        verify(compiler).report(JSError.make(sourceName, node, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, "arguments", "??"));
    }

    @Test
    public void testScanRoot_NodeTypeIsFunction() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node fnNameNode = mock(Node.class);
        Node args = mock(Node.class);
        Node body = mock(Node.class);
        when(node.getType()).thenReturn(Token.FUNCTION);
        when(node.getFirstChild()).thenReturn(fnNameNode);
        when(fnNameNode.getNext()).thenReturn(args);
        when(args.getNext()).thenReturn(body);
        when(fnNameNode.getString()).thenReturn("fnName");
        when(args.getType()).thenReturn(Token.LP);

        creator.scanRoot(node, scope);

        verify(scope).declare("fnName", fnNameNode, null, null);
        verify(compiler).report(JSError.make(sourceName, node, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, "arguments", "??"));
    }

    @Test
    public void testScanRoot_NodeTypeIsNotFunction() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        when(node.getType()).thenReturn(Token.SCRIPT);

        creator.scanRoot(node, scope);

        verify(scope, never()).declare(anyString(), any(), any(), any());
        verify(compiler, never()).report(any(JSError.class));
    }

    @Test
    public void testScanVars_NodeTypeIsVar() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node child = mock(Node.class);
        Node next = mock(Node.class);
        when(node.getType()).thenReturn(Token.VAR);
        when(node.getFirstChild()).thenReturn(child);
        when(child.getNext()).thenReturn(next);
        when(child.getType()).thenReturn(Token.NAME);
        when(child.getString()).thenReturn("name");
        when(scope.getParent()).thenReturn(null);

        creator.scanVars(node, null);

        verify(scope).declare("name", child, null, null);
        verify(compiler).report(JSError.make(sourceName, node, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, "arguments", "??"));
    }

    @Test
    public void testScanVars_NodeTypeIsFunctionWithFunctionExpression() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node fnNameNode = mock(Node.class);
        when(node.getType()).thenReturn(Token.FUNCTION);
        when(node.getFirstChild()).thenReturn(fnNameNode);
        when(fnNameNode.getString()).thenReturn("fnName");
        when(NodeUtil.isFunctionExpression(node)).thenReturn(true);

        creator.scanVars(node, null);

        verify(scope, never()).declare(anyString(), any(), any(), any());
        verify(compiler, never()).report(any(JSError.class));
    }

    @Test
    public void testScanVars_NodeTypeIsFunctionWithNonEmptyFunctionName() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node fnNameNode = mock(Node.class);
        when(node.getType()).thenReturn(Token.FUNCTION);
        when(node.getFirstChild()).thenReturn(fnNameNode);
        when(fnNameNode.getString()).thenReturn("fnName");
        when(NodeUtil.isFunctionExpression(node)).thenReturn(false);

        creator.scanVars(node, null);

        verify(scope).declare("fnName", fnNameNode, null, null);
        verify(compiler).report(JSError.make(sourceName, node, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, "arguments", "??"));
    }

    @Test
    public void testScanVars_NodeTypeIsCatch() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node var = mock(Node.class);
        Node block = mock(Node.class);
        when(node.getType()).thenReturn(Token.CATCH);
        when(node.getChildCount()).thenReturn(2);
        when(node.getFirstChild()).thenReturn(var);
        when(var.getNext()).thenReturn(block);
        when(var.getType()).thenReturn(Token.NAME);
        when(var.getString()).thenReturn("var");

        creator.scanVars(node, null);

        verify(scope).declare("var", var, null, null);
        verify(compiler).report(JSError.make(sourceName, node, SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, "arguments", "??"));
    }

    @Test
    public void testScanVars_NodeTypeIsNotVarFunctionOrCatch() {
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        when(node.getType()).thenReturn(Token.CALL);

        creator.scanVars(node, null);

        verify(scope, never()).declare(anyString(), any(), any(), any());
        verify(compiler, never()).report(any(JSError.class));
    }
}