package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        // Test that shouldTraverse returns false for functions that are constructors
        // or have the @this or @override annotation
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        jsDoc1.setConstructor(true);
        n1.setJSDocInfo(jsDoc1);
        assertEquals(false, CheckGlobalThis.shouldTraverse(t1, n1, null));

        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc2 = new JSDocInfo();
        jsDoc2.addInterface();
        n2.setJSDocInfo(jsDoc2);
        assertEquals(false, CheckGlobalThis.shouldTraverse(t2, n2, null));

        NodeTraversal t3 = new NodeTraversal();
        Node n3 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc3 = new JSDocInfo();
        jsDoc3.recordThisType();
        n3.setJSDocInfo(jsDoc3);
        assertEquals(false, CheckGlobalThis.shouldTraverse(t3, n3, null));

        NodeTraversal t4 = new NodeTraversal();
        Node n4 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc4 = new JSDocInfo();
        jsDoc4.recordOverride();
        n4.setJSDocInfo(jsDoc4);
        assertEquals(false, CheckGlobalThis.shouldTraverse(t4, n4, null));

        // Test that shouldTraverse returns false for functions that are not able
        // to have a @this annotation
        NodeTraversal t5 = new NodeTraversal();
        Node n5 = new Node(Token.FUNCTION);
        Node parent5 = new Node(Token.BLOCK);
        Node grandparent5 = new Node(Token.IF, parent5);
        grandparent5.addChildToFront(n5);
        assertEquals(false, CheckGlobalThis.shouldTraverse(t5, n5, grandparent5));

        NodeTraversal t6 = new NodeTraversal();
        Node n6 = new Node(Token.FUNCTION);
        Node parent6 = new Node(Token.SCRIPT);
        parent6.addChildToFront(n6);
        assertEquals(false, CheckGlobalThis.shouldTraverse(t6, n6, parent6));

        NodeTraversal t7 = new NodeTraversal();
        Node n7 = new Node(Token.FUNCTION);
        Node parent7 = new Node(Token.NAME);
        parent7.addChildToFront(n7);
        assertEquals(false, CheckGlobalThis.shouldTraverse(t7, n7, parent7));

        NodeTraversal t8 = new NodeTraversal();
        Node n8 = new Node(Token.FUNCTION);
        Node parent8 = new Node(Token.ASSIGN);
        parent8.addChildToFront(n8);
        assertEquals(false, CheckGlobalThis.shouldTraverse(t8, n8, parent8));
    }

    @Test
    public void testVisit() {
        // Test that visit reports an error when a global this keyword is encountered
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.THIS);
        CheckGlobalThis.visit(t1, n1, null);
        assertEquals(1, t1.getErrors().size());

        // Test that visit does not report an error when a global this keyword is not encountered
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.NAME);
        CheckGlobalThis.visit(t2, n2, null);
        assertEquals(0, t2.getErrors().size());
    }
}