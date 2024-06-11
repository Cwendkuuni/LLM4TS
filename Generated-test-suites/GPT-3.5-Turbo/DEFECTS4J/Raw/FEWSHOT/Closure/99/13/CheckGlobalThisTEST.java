package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        //Case 1: should not traverse function annotated with @constructor
        JSDocInfo jsDoc1 = new JSDocInfoBuilder().recordConstructor().build();
        Node function1 = new Node(Token.FUNCTION);
        function1.setFirstChild(new Node(Token.NAME));
        function1.getFirstChild().setJSDocInfo(jsDoc1);
        function1.setType(Token.FUNCTION);
        CheckGlobalThis checkGlobalThis1 = new CheckGlobalThis(null, null);
        NodeTraversal tr1 = new NodeTraversal(null, checkGlobalThis1);
        assertTrue(!checkGlobalThis1.shouldTraverse(tr1, function1, null));
        
        //Case 2: should not traverse function annotated with @this
        JSDocInfoBuilder builder2 = new JSDocInfoBuilder();
        builder2.recordThisType();
        JSDocInfo jsDoc2 = builder2.build();
        Node function2 = new Node(Token.FUNCTION);
        function2.setFirstChild(new Node(Token.NAME));
        function2.getFirstChild().setJSDocInfo(jsDoc2);
        function2.setType(Token.FUNCTION);
        CheckGlobalThis checkGlobalThis2 = new CheckGlobalThis(null, null);
        NodeTraversal tr2 = new NodeTraversal(null, checkGlobalThis2);
        assertTrue(!checkGlobalThis2.shouldTraverse(tr2, function2, null));
        
        //Case 3: should not traverse function with no logical place to put @this annotation
        JSDocInfoBuilder builder3 = new JSDocInfoBuilder();
        JSDocInfo jsDoc3 = builder3.build();
        Node function3 = new Node(Token.FUNCTION);
        function3.setFirstChild(new Node(Token.NAME));
        function3.getFirstChild().setJSDocInfo(jsDoc3);
        function3.setType(Token.FUNCTION);
        CheckGlobalThis checkGlobalThis3 = new CheckGlobalThis(null, null);
        NodeTraversal tr3 = new NodeTraversal(null, checkGlobalThis3);
        assertTrue(!checkGlobalThis3.shouldTraverse(tr3, function3, null));
        
        //Case 4: should traverse function with logical place to put @this annotation
        JSDocInfoBuilder builder4 = new JSDocInfoBuilder();
        JSDocInfo jsDoc4 = builder4.build();
        Node function4 = new Node(Token.FUNCTION);
        function4.setFirstChild(new Node(Token.NAME));
        function4.getFirstChild().setJSDocInfo(jsDoc4);
        function4.setType(Token.FUNCTION);
        Node parent4 = new Node(Token.BLOCK);
        parent4.addChildToBack(function4);
        CheckGlobalThis checkGlobalThis4 = new CheckGlobalThis(null, null);
        NodeTraversal tr4 = new NodeTraversal(null, checkGlobalThis4);
        assertTrue(checkGlobalThis4.shouldTraverse(tr4, function4, parent4));
        
        //Case 5: should traverse function assigned to a variable
        Node name5 = new Node(Token.NAME);
        Node function5 = new Node(Token.FUNCTION);
        function5.setFirstChild(name5);
        function5.setType(Token.FUNCTION);
        Node assign5 = new Node(Token.ASSIGN);
        assign5.addChildToBack(name5);
        assign5.addChildToBack(function5);
        CheckGlobalThis checkGlobalThis5 = new CheckGlobalThis(null, null);
        NodeTraversal tr5 = new NodeTraversal(null, checkGlobalThis5);
        assertTrue(checkGlobalThis5.shouldTraverse(tr5, function5, assign5));
    }

    @Test
    public void testVisit() {
        //Case 1: n is a THIS node and should report it
        Node n1 = new Node(Token.THIS);
        Node parent1 = new Node(Token.GETPROP);
        CheckGlobalThis checkGlobalThis1 = new CheckGlobalThis(null, null);
        checkGlobalThis1.assignLhsChild = null;
        checkGlobalThis1.visit(null, n1, parent1);
        
        //Case 2: n is a THIS node and should not report it
        Node n2 = new Node(Token.THIS);
        Node parent2 = new Node(Token.CALL);
        CheckGlobalThis checkGlobalThis2 = new CheckGlobalThis(null, null);
        checkGlobalThis2.assignLhsChild = null;
        checkGlobalThis2.visit(null, n2, parent2);
        
        //Case 3: n is the same as assignLhsChild and should always report it
        Node n3 = new Node(Token.NAME);
        Node parent3 = new Node(Token.ASSIGN);
        CheckGlobalThis checkGlobalThis3 = new CheckGlobalThis(null, null);
        checkGlobalThis3.assignLhsChild = n3;
        checkGlobalThis3.visit(null, n3, parent3);
        assertNull(checkGlobalThis3.assignLhsChild);
    }
    
    @Test
    public void testGetFunctionJsDocInfo() {
        //Case 1: jsDoc is set on the node itself
        Node node1 = new Node(Token.FUNCTION);
        JSDocInfoBuilder builder1 = new JSDocInfoBuilder();
        builder1.recordConstructor();
        JSDocInfo jsDoc1 = builder1.build();
        node1.setJSDocInfo(jsDoc1);
        CheckGlobalThis checkGlobalThis1 = new CheckGlobalThis(null, null);
        assertEquals(jsDoc1, checkGlobalThis1.getFunctionJsDocInfo(node1));
        
        //Case 2: jsDoc is set on the parent
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.NAME);
        parent2.setFirstChild(node2);
        JSDocInfoBuilder builder2 = new JSDocInfoBuilder();
        builder2.recordConstructor();
        JSDocInfo jsDoc2 = builder2.build();
        parent2.setJSDocInfo(jsDoc2);
        CheckGlobalThis checkGlobalThis2 = new CheckGlobalThis(null, null);
        assertEquals(jsDoc2, checkGlobalThis2.getFunctionJsDocInfo(node2));
        
        //Case 3: jsDoc is set on the grandparent
        Node node3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.NAME);
        parent3.setFirstChild(node3);
        Node grandparent3 = new Node(Token.VAR);
        grandparent3.setFirstChild(parent3);
        JSDocInfoBuilder builder3 = new JSDocInfoBuilder();
        builder3.recordConstructor();
        JSDocInfo jsDoc3 = builder3.build();
        grandparent3.setJSDocInfo(jsDoc3);
        CheckGlobalThis checkGlobalThis3 = new CheckGlobalThis(null, null);
        assertEquals(jsDoc3, checkGlobalThis3.getFunctionJsDocInfo(node3));
    }
}