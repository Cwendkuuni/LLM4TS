package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_functionWithConstructorAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        parent.setType(Token.FUNCTION);
        n.setParent(parent);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        n.setJSDocInfo(jsDoc);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_functionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        parent.setType(Token.FUNCTION);
        n.setParent(parent);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType(new Node(Token.STRING));
        n.setJSDocInfo(jsDoc);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_assignWithObjectLiteralKey() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        parent.setType(Token.STRING_KEY);
        Node gramps = new Node();
        gramps.setJSDocInfo(new JSDocInfo());
        gramps.getJSDocInfo().setLendsName("test.prototype");
        parent.setParent(gramps);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_functionWithNormalParent() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node();
        parent.setType(Token.NAME);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_functionWithAssignParent() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node();
        parent.setType(Token.ASSIGN);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_functionWithPrototypeParent() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        n.setType(Token.FUNCTION);
        Node parent = new Node();
        parent.setType(Token.GETPROP);
        Node gramps = new Node();
        gramps.setJSDocInfo(new JSDocInfo());
        gramps.getJSDocInfo().setLendsName("test.prototype");
        parent.setParent(gramps);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testVisit_thisNode_shouldReportThis() {
        NodeTraversal t = new NodeTraversal();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        Node n = new Node(Token.THIS);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertTrue(result);
    }

    @Test
    public void testVisit_thisNode_shouldNotReportThis() {
        NodeTraversal t = new NodeTraversal();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        Node n = new Node(Token.THIS);
        checkGlobalThis.assignLhsChild = new Node();

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertFalse(result);
    }

    @Test
    public void testVisit_nonThisNode_shouldNotReportThis() {
        NodeTraversal t = new NodeTraversal();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        Node n = new Node(Token.OTHER);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertFalse(result);
    }

    @Test
    public void testShouldReportThis_thisNodeWithPropertyAccess_shouldReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        n.setParent(parent);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_thisNodeWithoutPropertyAccess_shouldNotReportThis() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        Node n = new Node(Token.THIS);

        boolean result = checkGlobalThis.shouldReportThis(n);

        assertFalse(result);
    }

    @Test
    public void testVisit_thisNode_shouldReportGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        checkGlobalThis.assignLhsChild = null;

        checkGlobalThis.visit(t, n, parent);

        assertEquals(1, t.getErrors().size());
        assertEquals(DiagnosticType.warning("JSC_USED_GLOBAL_THIS"), t.getErrors().get(0).getType());
    }

    @Test
    public void testVisit_nonThisNode_shouldNotReportGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.OTHER);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

        checkGlobalThis.visit(t, n, parent);

        assertEquals(0, t.getErrors().size());
    }

    @Test
    public void testVisit_thisNodeWithAssignLhsChild_shouldNotReportGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        checkGlobalThis.assignLhsChild = new Node();

        checkGlobalThis.visit(t, n, parent);

        assertEquals(0, t.getErrors().size());
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithJSDocInfo_returnsJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithoutJSDocInfoButAssignChildWithJSDoc_returnsAssignParentJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(parent.getJSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithoutJSDocInfoAndAssignChildWithoutJSDocButVarGrandparentWithJSDoc_returnsVarGrandparentJSDocInfo() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        Node gramps = new Node(Token.VAR);
        gramps.setJSDocInfo(new JSDocInfo());
        parent.setParent(gramps);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertEquals(gramps.getJSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_functionWithoutJSDocInfoAndAssignChildWithoutJSDocAndNonVarGrandparent_returnsNull() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        Node gramps = new Node(Token.OTHER);
        parent.setParent(gramps);
        n.setParent(parent);

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

        assertNull(result);
    }
}