package com.google.javascript.jscomp;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_withFunctionConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);

        boolean result = checker.shouldTraverse(t, n, null);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_withFunctionThisAnnotation() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType(new JSTypeExpression(new Node(Token.STRING), "<this>", false));
        n.setJSDocInfo(jsDoc);

        boolean result = checker.shouldTraverse(t, n, null);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_withFunctionNoLogicalPlaceForThisAnnotation() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        n.setParent(parent);

        boolean result = checker.shouldTraverse(t, n, parent);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_withFunctionInBlock() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(new Node(Token.VAR));
        n.setParent(parent);

        boolean result = checker.shouldTraverse(t, n, parent);

        Assert.assertTrue(result);
    }

    @Test
    public void testShouldTraverse_withFunctionInScript() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        boolean result = checker.shouldTraverse(t, n, parent);

        Assert.assertTrue(result);
    }

    @Test
    public void testShouldTraverse_withFunctionInName() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);

        boolean result = checker.shouldTraverse(t, n, parent);

        Assert.assertTrue(result);
    }

    @Test
    public void testShouldTraverse_withAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.ASSIGN);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);

        boolean result = checker.shouldTraverse(t, n, parent);

        Assert.assertFalse(result);
    }

    @Test
    public void testShouldTraverse_withNestedAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.ASSIGN);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.NAME));
        parent.addChildToFront(n);
        n.setParent(parent);

        boolean result = checker.shouldTraverse(t, n, parent);

        Assert.assertTrue(result);
    }

    @Test
    public void testVisit_withGlobalThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);

        checker.visit(t, n, parent);
    }

    @Test
    public void testVisit_withAssignLhsChildNotNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal(compiler, checker);

        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);
        checker.setAssignLhsChild(new Node(Token.THIS));

        checker.visit(t, n, parent);
    }

    @Test
    public void testShouldReportThis_withAssignLhsChildNotNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        boolean result = checker.shouldReportThis(new Node(Token.THIS), new Node(Token.NAME));

        Assert.assertTrue(result);
    }

    @Test
    public void testShouldReportThis_withParentGetProperty() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        boolean result = checker.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP));

        Assert.assertTrue(result);
    }

    @Test
    public void testShouldReportThis_withParentAssign() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        boolean result = checker.shouldReportThis(new Node(Token.THIS), new Node(Token.ASSIGN));

        Assert.assertTrue(result);
    }
}