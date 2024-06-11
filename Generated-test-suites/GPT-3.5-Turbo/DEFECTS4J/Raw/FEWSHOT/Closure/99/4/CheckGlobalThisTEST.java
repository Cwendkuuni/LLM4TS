package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checkGlobalThis;
    private NodeTraversal nodeTraversal;

    @Before
    public void setUp() {
        compiler = Mockito.mock(AbstractCompiler.class);
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
        nodeTraversal = Mockito.mock(NodeTraversal.class);
    }

    @Test
    public void testShouldTraverse_withFunctionConstructorAndThisAnnotation_ReturnsFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        Mockito.when(parent.getJSDocInfo()).thenReturn(jsDocInfo);

        assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
    }

    @Test
    public void testShouldTraverse_withFunctionThisAnnotation_ReturnsFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        Mockito.when(parent.getJSDocInfo()).thenReturn(jsDocInfo);

        assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
    }

    @Test
    public void testShouldTraverse_withFunctionOverrideAnnotation_ReturnsFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        Mockito.when(parent.getJSDocInfo()).thenReturn(jsDocInfo);

        assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
    }

    @Test
    public void testShouldTraverse_withNonGlobalFunction_ReturnsFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        Node grandParent = Mockito.mock(Node.class);
        Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);
        Mockito.when(grandParent.getType()).thenReturn(Token.STATIC_MEMBER_FUNCTION_DEF);
        Mockito.when(parent.getParent()).thenReturn(grandParent);

        assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
    }

    @Test
    public void testShouldTraverse_withAssignParentAndNoGetPropChild_ReturnsTrue() {
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        Node grandParent = Mockito.mock(Node.class);
        Mockito.when(parent.getType()).thenReturn(Token.ASSIGN);
        Mockito.when(grandParent.getType()).thenReturn(Token.BLOCK);
        Mockito.when(parent.getParent()).thenReturn(grandParent);

        assertTrue(checkGlobalThis.shouldTraverse(t, functionNode, parent));
    }

    @Test
    public void testShouldTraverse_withAssignParentAndPrototypePropChild_ReturnsFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        Mockito.when(parent.getType()).thenReturn(Token.ASSIGN);
        Mockito.when(parent.getFirstChild()).thenReturn(lhs);

        assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
    }

    @Test
    public void testShouldTraverse_withAssignParentAndPrototypeInQualifiedName_ReturnsFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "a"));
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        Mockito.when(parent.getType()).thenReturn(Token.ASSIGN);
        Mockito.when(parent.getFirstChild()).thenReturn(lhs);

        assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
    }

    @Test
    public void testShouldTraverse_withParentNotAssign_ReturnsTrue() {
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(parent.getType()).thenReturn(Token.BLOCK);

        assertTrue(checkGlobalThis.shouldTraverse(t, functionNode, parent));
    }

    @Test
    public void testShouldTraverse_withNoParent_ReturnsTrue() {
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

        assertTrue(checkGlobalThis.shouldTraverse(t, functionNode, null));
    }

    @Test
    public void testVisit_withThisTokenAndShouldReportThis_ReturnsDiagnosticType() {
        Node thisNode = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(parent.isGetProp()).thenReturn(true);
        boolean shouldReportThis = true;

        checkGlobalThis.visit(t, thisNode, parent);
        // Add assertions here
    }

    @Test
    public void testVisit_withThisTokenAndNotShouldReportThis_DoesNotReturnDiagnosticType() {
        Node thisNode = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);
        boolean shouldReportThis = false;

        checkGlobalThis.visit(t, thisNode, parent);
        // Add assertions here
    }

    @Test
    public void testVisit_withNonThisToken_DoesNotReturnDiagnosticType() {
        Node nonThisNode = new Node(Token.GETPROP);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = Mockito.mock(Node.class);

        checkGlobalThis.visit(t, nonThisNode, parent);
        // Add assertions here
    }
}