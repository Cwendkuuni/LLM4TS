package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checkGlobalThis;
    private NodeTraversal.Callback callback;
    private Node n;
    private Node parent;
    private NodeTraversal t;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
        callback = checkGlobalThis;
        n = mock(Node.class);
        parent = mock(Node.class);
        t = mock(NodeTraversal.class);
    }

    @Test
    public void testShouldTraverse_withAssignmentExpression() {
        when(n.getType()).thenReturn(Token.ASSIGN);
        when(n.getFirstChild()).thenReturn(mock(Node.class));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withAssignmentExpressionToPrototypeProperty() {
        Node lhs = mock(Node.class);
        when(n.getType()).thenReturn(Token.GETPROP);
        when(n.getLastChild()).thenReturn(mock(Node.class));
        when(n.getLastChild().getString()).thenReturn("prototype");
        when(n.getFirstChild()).thenReturn(lhs);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withAssignmentExpressionToPrototypeSubproperty() {
        Node lhs = mock(Node.class);
        when(n.getType()).thenReturn(Token.GETPROP);
        when(n.getFirstChild()).thenReturn(lhs);
        when(lhs.getQualifiedName()).thenReturn("test.prototype.sub");
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionNode() {
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(false);
        when(jsDoc.hasThisType()).thenReturn(false);
        when(jsDoc.isOverride()).thenReturn(false);
        when(parent.getType()).thenReturn(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withFunctionNodeConstructor() {
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(true);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionNodeThisType() {
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.hasThisType()).thenReturn(true);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionNodeOverride() {
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isOverride()).thenReturn(true);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionNodeInvalidParent() {
        JSDocInfo jsDoc = mock(JSDocInfo.class);
        when(n.getType()).thenReturn(Token.FUNCTION);
        when(n.getJSDocInfo()).thenReturn(jsDoc);
        when(jsDoc.isConstructor()).thenReturn(false);
        when(jsDoc.hasThisType()).thenReturn(false);
        when(jsDoc.isOverride()).thenReturn(false);
        when(parent.getType()).thenReturn(Token.NOT);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testVisit_withThisNode() {
        checkGlobalThis.visit(t, n, parent);

        verify(compiler, times(1)).report(any());
    }

    @Test
    public void testVisit_withAssignLhsChild() {
        checkGlobalThis.visit(t, n, parent);

        assertNull(checkGlobalThis.assignLhsChild);
    }
}