package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);

        // Test with function that is constructor
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        Node parent = new Node();
        parent.setType(Token.NAME);
        assertFalse(checker.shouldTraverse(t, n, parent));

        // Test with function that has @this annotation
        jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        assertFalse(checker.shouldTraverse(t, n, parent));

        // Test with function that has @override annotation
        jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        assertFalse(checker.shouldTraverse(t, n, parent));

        // Test with function that is interface
        jsDoc = new JSDocInfo();
        jsDoc.setInterface(true);
        n.setJSDocInfo(jsDoc);
        assertFalse(checker.shouldTraverse(t, n, parent));

        // Test with function that is not constructor and does not have annotations
        jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        parent.setType(Token.SCRIPT);
        assertTrue(checker.shouldTraverse(t, n, parent));

        // Test with function that is not constructor and has annotations, but parent type is not one of the expected types
        JSDocInfo parentJsDoc = new JSDocInfo();
        parent.setJSDocInfo(parentJsDoc);
        parent.setType(Token.ADD);
        assertFalse(checker.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token. THIS);

        // Test with assignLhsChild not null
        checker.assignLhsChild = new Node(Token.ASSIGN);
        Node parent = new Node();
        checker.visit(t, n, parent);
        assertNull(checker.assignLhsChild);

        // Test with assignLhsChild null and parent node is not a property access
        checker.assignLhsChild = null;
        parent = new Node();
        checker.visit(t, n, parent);
        assertNull(checker.assignLhsChild);

        // Test with assignLhsChild null and parent node is property access
        checker.assignLhsChild = null;
        parent = new Node(Token.GETPROP);
        checker.visit(t, n, parent);
        assertNull(checker.assignLhsChild);

        // Test with assignLhsChild null and parent node is property access and lhs is GETPROP
        Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, new Node(Token.NAME, new Node(Token.GETPROP))));
        parent = new Node(Token.GETPROP, lhs);
        checker.visit(t, n, parent);
        assertNull(checker.assignLhsChild);
    }

    @Test
    public void testShouldReportThis() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        
        // Test with assignLhsChild not null
        checker.assignLhsChild = new Node(Token.ASSIGN);
        assertTrue(checker.shouldReportThis(n, parent));

        // Test with assignLhsChild null and parent node is not a property access
        checker.assignLhsChild = null;
        assertFalse(checker.shouldReportThis(n, parent));

        // Test with assignLhsChild null and parent node is a property access
        checker.assignLhsChild = null;
        parent = new Node(Token.GETPROP);
        assertTrue(checker.shouldReportThis(n, parent));
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checker = new CheckGlobalThis(null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();

        // Test with JSDocInfo present in the function node
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        assertEquals(jsDoc, checker.getFunctionJsDocInfo(n));

        // Test with JSDocInfo not present in the function node and JSDocInfo present in the parent node of function node which is NAME type
        parent.setJSDocInfo(jsDoc);
        parent.setType(Token.NAME);
        assertEquals(jsDoc, checker.getFunctionJsDocInfo(n));

        // Test with JSDocInfo not present in the function node and JSDocInfo present in the parent node of function node which is ASSIGN type and grand parent is VAR type
        Node grandParent = new Node();
        grandParent.setJSDocInfo(jsDoc);
        grandParent.setType(Token.VAR);
        parent = new Node();
        parent.setParent(grandParent);
        parent.setType(Token.ASSIGN);
        assertEquals(jsDoc, checker.getFunctionJsDocInfo(n));

        // Test with JSDocInfo not present in the function node and JSDocInfo present in the parent node of function node which is ASSIGN type and grand parent is not VAR type
        grandParent.setJSDocInfo(null);
        grandParent.setType(Token.SCRIPT);
        assertEquals(null, checker.getFunctionJsDocInfo(n));
    }
}