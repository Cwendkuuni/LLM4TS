package com.google.javascript.jscomp;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checkGlobalThis;

    @Test
    public void testShouldTraverse_WithConstructorFunctionAndThisAnnotation_ReturnFalse() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);

        boolean result = checkGlobalThis.shouldTraverse(t, n, null);

        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_WithFunctionAndThisAnnotation_ReturnFalse() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setThisType(new JSTypeExpression(null));
        n.setJSDocInfo(jsDoc);

        boolean result = checkGlobalThis.shouldTraverse(t, n, null);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_WithNestedAssignments_ReturnFalse() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.NAME)));
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        parent.getLastChild().addChildToFront(n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_WithAssignmentToPrototypeProperty_ReturnFalse() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.NAME, "prototype")));
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        parent.getLastChild().addChildToFront(n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_WithAssignmentToSubproperty_ReturnFalse() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.NAME, "prototype.property")));
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        parent.getLastChild().addChildToFront(n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_WithLeftSideAssignment_ReturnTrue() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        parent.getLastChild().addChildToFront(n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_WithOtherCase_ReturnTrue() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(new Node(Token.NAME));
        parent.getLastChild().addChildToFront(n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertEquals(true, result);
    }

    @Test
    public void testVisit_WithThisAndShouldReportThis_ReturnsGlobalThisError() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);

        checkGlobalThis.visit(t, n, parent);

        DiagnosticType error = CheckGlobalThis.GLOBAL_THIS;

        assertEquals(true, compiler.getErrorCount() == 1 && compiler.getErrors().get(0).getType().equals(error));
    }

    @Test
    public void testVisit_WithAssignLhsChildNotNull_AssignsNullToAssignLhsChild() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);

        checkGlobalThis.shouldTraverse(t, n, parent);
        checkGlobalThis.visit(t, n, parent);

        assertEquals(null, checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testVisit_WithAssignLhsChildNull_DoesNotAssignNullToAssignLhsChild() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(null, checkGlobalThis.assignLhsChild);
    }

    private JSDocInfo getFunctionJsDocInfo(Node n) {
        JSDocInfo jsDoc = n.getJSDocInfo();
        Node parent = n.getParent();
        if (jsDoc == null) {
            int parentType = parent.getType();
            if (parentType == Token.NAME || parentType == Token.ASSIGN) {
                jsDoc = parent.getJSDocInfo();
                if (jsDoc == null && parentType == Token.NAME) {
                    Node gramps = parent.getParent();
                    if (gramps.getType() == Token.VAR) {
                        jsDoc = gramps.getJSDocInfo();
                    }
                }
            }
        }
        return jsDoc;
    }

}