package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        // Test with a function annotated with @constructor
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.NAME);
        JSDocInfo jsDoc1 = new JSDocInfo();
        jsDoc1.setConstructor();
        n1.setJSDocInfo(jsDoc1);
        boolean result1 = checkGlobalThis.shouldTraverse(t1, n1, parent1);
        assertEquals(false, result1);

        // Test with a function annotated with @this
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.NAME);
        JSDocInfo jsDoc2 = new JSDocInfo();
        jsDoc2.addType(new JSTypeExpression(new Node(Token.THIS), sourceName));
        n2.setJSDocInfo(jsDoc2);
        boolean result2 = checkGlobalThis.shouldTraverse(t2, n2, parent2);
        assertEquals(false, result2);

        // Test with a function with no logical place to put a @this annotation
        NodeTraversal t3 = new NodeTraversal();
        Node n3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.VAR);
        boolean result3 = checkGlobalThis.shouldTraverse(t3, n3, parent3);
        assertEquals(false, result3);

        // Test with a function that is not a constructor or have any special annotations
        NodeTraversal t4 = new NodeTraversal();
        Node n4 = new Node(Token.FUNCTION);
        Node parent4 = new Node(Token.SCRIPT);
        boolean result4 = checkGlobalThis.shouldTraverse(t4, n4, parent4);
        assertEquals(true, result4);
    }

    @Test
    public void testVisit() {
        // Test with a THIS on the left side of an assignment
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.THIS);
        Node parent1 = new Node(Token.ASSIGN);
        checkGlobalThis.visit(t1, n1, parent1);
        // Check if error is reported

        // Test with a THIS on the right side of an assignment
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.THIS);
        Node parent2 = new Node(Token.GETPROP);
        checkGlobalThis.visit(t2, n2, parent2);
        // Check if error is reported
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        // Test with a function that has JSDoc info
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        n1.setJSDocInfo(jsDoc1);
        JSDocInfo result1 = checkGlobalThis.getFunctionJsDocInfo(n1);
        assertEquals(jsDoc1, result1);

        // Test with a function that does not have JSDoc info, but has a parent with JSDoc info
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.NAME);
        JSDocInfo jsDoc2 = new JSDocInfo();
        parent2.setJSDocInfo(jsDoc2);
        n2.setParent(parent2);
        JSDocInfo result2 = checkGlobalThis.getFunctionJsDocInfo(n2);
        assertEquals(jsDoc2, result2);

        // Test with a function that does not have JSDoc info, and has a parent without JSDoc info
        NodeTraversal t3 = new NodeTraversal();
        Node n3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.VAR);
        n3.setParent(parent3);
        JSDocInfo result3 = checkGlobalThis.getFunctionJsDocInfo(n3);
        assertEquals(null, result3);
    }

}