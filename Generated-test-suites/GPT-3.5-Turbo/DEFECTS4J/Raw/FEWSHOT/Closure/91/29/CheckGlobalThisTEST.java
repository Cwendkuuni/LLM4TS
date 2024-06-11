package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CheckGlobalThisTest {

    @Mock
    private AbstractCompiler compiler;

    @Mock
    private CheckLevel level;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = new CheckLevel();
    }

    @Test
    public void testShouldTraverse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withFunctionType() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndConstructorAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        n.setJSDocInfo(jsDocInfo);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        n.setJSDocInfo(jsDocInfo);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndNoLogicalPlaceForThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        n.setJSDocInfo(jsDocInfo);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndInsideBlock() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndInsideScript() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndInsideName() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndInsideAssign() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndStringKey() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_withFunctionTypeAndNumberKey() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testVisit_withThisNodeAndShouldReportThis_true() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        checkGlobalThis.visit(t, n, parent);
    }

    @Test
    public void testVisit_withAssignLhsChild_notNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        checkGlobalThis.visit(t, n, parent);

        assertEquals(null, checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        Node n = new Node();
        JSDocInfo jsDocInfo = new JSDocInfo();
        n.setJSDocInfo(jsDocInfo);
        Node parent = new Node(Token.NAME);
        Node gramps = new Node(Token.VAR);
        parent.setParent(gramps);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withParentTypeAssign() {
        Node n = new Node();
        Node lhs = new Node();
        lhs.setToken(Token.NAME);
        n.setParent(lhs);
        Node parent = new Node(Token.ASSIGN);
        parent.setFirstChild(lhs);
        JSDocInfo jsDocInfo = new JSDocInfo();
        lhs.setJSDocInfo(jsDocInfo);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withParentTypeAssignAndParentHasJSDocInfo() {
        Node n = new Node();
        Node lhs = new Node();
        lhs.setToken(Token.NAME);
        n.setParent(lhs);
        Node parent = new Node(Token.ASSIGN);
        parent.setFirstChild(lhs);
        JSDocInfo jsDocInfo = new JSDocInfo();
        parent.setJSDocInfo(jsDocInfo);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withParentTypeVarAndGrampsIsVar() {
        Node n = new Node();
        JSDocInfo jsDocInfo = new JSDocInfo();
        Node parent = new Node(Token.NAME);
        Node gramps = new Node(Token.VAR);
        gramps.setJSDocInfo(jsDocInfo);
        parent.setParent(gramps);
        n.setParent(parent);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDocInfo, result);
    }
}