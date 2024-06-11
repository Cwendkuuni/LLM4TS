package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
    private AbstractCompiler compiler;
    private SyntacticScopeCreator.RedeclarationHandler redeclarationHandler;
    private SyntacticScopeCreator syntacticScopeCreator;
    private Scope scope;
    private String sourceName;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        redeclarationHandler = mock(SyntacticScopeCreator.RedeclarationHandler.class);
        syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
        scope = mock(Scope.class);
    }

    @Test
    public void testCreateScope_withNonNullParent_returnsScope() {
        Node n = mock(Node.class);
        Scope parent = mock(Scope.class);
        when(parent.getParent()).thenReturn(null);
        when(parent, n).thenReturn(scope);
        sourceName = "";

        Scope result = syntacticScopeCreator.createScope(n, parent);

        assertEquals(scope, result);
        assertNull(sourceName);
        verify(redeclarationHandler).onRedeclaration(
            scope, name, n, parent, null, n);
        verifyNoMoreInteractions(redeclarationHandler);
        verify(scope).declare(name, n, (JSType)null, null);
        verifyNoMoreInteractions(scope);
    }

    @Test
    public void testCreateScope_withNullParent_returnsScope() {
        Node n = mock(Node.class);
        scope = new Scope(n, compiler);
        sourceName = "";

        Scope result = syntacticScopeCreator.createScope(n, null);

        assertEquals(scope, result);
        assertNull(sourceName);
        verify(redeclarationHandler).onRedeclaration(
            scope, name, n, parent, null, n);
        verifyNoMoreInteractions(redeclarationHandler);
        verify(scope).declare(name, n, (JSType)null, null);
        verifyNoMoreInteractions(scope);
    }

    @Test
    public void testScanRoot_withFunctionNode_executesCorrectly() {
        Node n = mock(Node.class);
        Node fnNameNode = mock(Node.class);
        Node args = mock(Node.class);
        Node body = mock(Node.class);
        String fnName = "";
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getProp(Node.SOURCENAME_PROP)).thenReturn("");
        when(n.getFirstChild()).thenReturn(fnNameNode);
        when(fnNameNode.getNext()).thenReturn(args);
        when(args.getNext()).thenReturn(body);
        when(fnNameNode.getString()).thenReturn(fnName);
        when(scope.getParent()).thenReturn(null);
        sourceName = "";

        syntacticScopeCreator.scanRoot(n, null);

        verify(scope).declare(fnName, fnNameNode, (JSType)null, null);
        verifyNoMoreInteractions(scope);
        verify(redeclarationHandler).onRedeclaration(
            scope, name, n, parent, null, n);
        verifyNoMoreInteractions(redeclarationHandler);
        verify(redeclarationHandler).onRedeclaration(
            scope, name, n, parent, null, n);
        verifyNoMoreInteractions(redeclarationHandler);
        verify(redeclarationHandler).onRedeclaration(
            scope, name, n, parent, null, n);
        verifyNoMoreInteractions(redeclarationHandler);
        verify(scope).declare(name, child, (JSType)null, null);
        verify(scope, times(3)).declare(var.getString(), var, (JSType)null, null);
        verifyNoMoreInteractions(scope);
    }

    @Test
    public void testScanRoot_withVarNode_executesCorrectly() {
        Node n = mock(Node.class);
        Node child = mock(Node.class);
        String name = "";
        when(n.getType()).thenReturn(Token.VAR);
        when(child.getNext()).thenReturn(null);
        when(child.getType()).thenReturn(Token.NAME);
        when(child.getString()).thenReturn(name);

        syntacticScopeCreator.scanRoot(n, null);

        verify(redeclarationHandler).onRedeclaration(
            scope, name, n, parent, null, n);
        verifyNoMoreInteractions(redeclarationHandler);
        verify(scope).declare(name, child, (JSType)null, null);
        verify(redeclarationHandler).onRedeclaration(
            scope, name, n, parent, null, n);
        verifyNoMoreInteractions(redeclarationHandler);
    }

    @Test
    public void testScanRoot_withFunctionExpressionNode_executesCorrectly() {
        Node n = mock(Node.class);
        String fnName = "";
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getFirstChild().getString()).thenReturn(fnName);
        when(scope.isLocal()).thenReturn(true);
        sourceName = "";

        syntacticScopeCreator.scanRoot(n, null);

        verify(scope).declare(fnName, n.getFirstChild(), (JSType)null, null);
    }

    @Test
    public void testScanRoot_withCatchNode_executesCorrectly() {
        Node n = mock(Node.class);
        Node var = mock(Node.class);
        Node block = mock(Node.class);
        String name = "";
        when(n.getType()).thenReturn(Token.CATCH);
        when(n.getFirstChild()).thenReturn(var);
        when(var.getNext()).thenReturn(block);
        when(n.getChildCount()).thenReturn(2);
        when(var.getType()).thenReturn(Token.NAME);
        when(var.getString()).thenReturn(name);

        syntacticScopeCreator.scanRoot(n, null);

        verify(redeclarationHandler).onRedeclaration(
            scope, name, n, parent, null, n);
        verifyNoMoreInteractions(redeclarationHandler);
        verify(scope).declare(name, var, (JSType)null, null);
    }

    @Test
    public void testScanRoot_withScriptNode_executesCorrectly() {
        Node n = mock(Node.class);
        sourceName = "";

        syntacticScopeCreator.scanRoot(n, null);

        assertNull(sourceName);
    }

    @Test
    public void testScanRoot_withStatementBlockNode_executesCorrectly() {
        Node n = mock(Node.class);
        Node child = mock(Node.class);
        when(NodeUtil.isStatementBlock(n)).thenReturn(true);
        when(child.getNext()).thenReturn(null);
        when(child.getType()).thenReturn(Token.NAME);

        syntacticScopeCreator.scanRoot(n, null);

        verify(redeclarationHandler).onRedeclaration(
            scope, name, n, parent, null, n);
        verifyNoMoreInteractions(redeclarationHandler);
        verify(scope).declare(name, child, (JSType)null, null);
    }
}