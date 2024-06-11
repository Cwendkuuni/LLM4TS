package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.javascript.jscomp.CheckGlobalThis;

public class CheckGlobalThisTest {

    @Mock
    private AbstractCompiler compiler;

    @Mock
    private CheckLevel level;

    @InjectMocks
    private CheckGlobalThis checkGlobalThis;

    @Captor
    private ArgumentCaptor<NodeTraversal> nodeTraversalCaptor;

    @Captor
    private ArgumentCaptor<Node> nodeCaptor;

    @Captor
    private ArgumentCaptor<Node> parentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShouldTraverse_prototypeMethod() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.FUNCTION, new Node(Token.NAME));
        parent.addChildToFront(n);
        parent.getFirstChild().addChildToFront(new Node("prototype"));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_constructorFunction() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(n);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_thisAnnotation() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThis();
        n.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_logicalPlace() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_assignment() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node("a"));
        parent.addChildToBack(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_rightSideNoAssignment() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node("a"));
        parent.addChildToBack(new Node("b"));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_getProperty_prototype() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(n);
        n.addChildToFront(new Node(Token.NAME));
        n.getFirstChild().addChildToFront(new Node("prototype"));
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_this() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        JSError jsError = new JSError(CheckGlobalThis.GLOBAL_THIS);
        checkGlobalThis.visit(t, n, parent);
        verify(compiler).report(eq(jsError));
    }

    @Test
    public void testVisit_assignLhsChild() {
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node("a"));
        parent.addChildToBack(n);
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.getAssignLhsChild());
    }

    @Test
    public void testGetFunctionJsDocInfo_hasJsDoc() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_hasParentJsDoc() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_hasVarJsDoc() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        gramps.addChildToFront(parent);
        parent.addChildToFront(n);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

}