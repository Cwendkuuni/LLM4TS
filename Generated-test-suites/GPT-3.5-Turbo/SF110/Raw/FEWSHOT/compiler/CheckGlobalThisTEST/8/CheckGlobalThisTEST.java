package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CheckGlobalThisTest {

    @Test
    public void testShouldReportThis() {
        Node n1 = new Node(Token.THIS);
        Node n2 = new Node(Token.GETPROP);
        Node n3 = new Node(Token.NAME);
        Node n4 = new Node(Token.FUNCTION);
        Node n5 = new Node(Token.ASSIGN);
        Node n6 = new Node(Token.VAR);

        AssignLhsChild = null;
        assertFalse(shouldReportThis(n1));

        AssignLhsChild = n2;
        assertTrue(shouldReportThis(n1));

        AssignLhsChild = n3;
        assertFalse(shouldReportThis(n1));

        AssignLhsChild = n4;
        assertTrue(shouldReportThis(n1));

        AssignLhsChild = n5;
        assertTrue(shouldReportThis(n1));

        AssignLhsChild = n6;
        assertFalse(shouldReportThis(n1));
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        Node n1 = new Node(Token.THIS);
        Node n2 = new Node(Token.GETPROP);
        Node n3 = new Node(Token.NAME);
        Node n4 = new Node(Token.FUNCTION);
        Node n5 = new Node(Token.ASSIGN);
        Node n6 = new Node(Token.VAR);

        assertEquals(null, getFunctionJsDocInfo(n1));

        assertEquals(null, getFunctionJsDocInfo(n2));

        assertEquals(null, getFunctionJsDocInfo(n3));

        assertEquals(null, getFunctionJsDocInfo(n4));

        assertEquals(null, getFunctionJsDocInfo(n5));

        assertEquals(null, getFunctionJsDocInfo(n6));

        Node n7 = new Node(Token.STRING);
        Node n8 = new Node(Token.BLOCK);
        Node n9 = new Node(Token.SCRIPT);
        Node n10 = new Node(Token.NUMBER);

        n7.putProp(Node.JSDOC_INFO_PROP, "JSDocInfo");
        assertEquals("JSDocInfo", getFunctionJsDocInfo(n7));

        n8.putProp(Node.JSDOC_INFO_PROP, "JSDocInfo");
        assertEquals("JSDocInfo", getFunctionJsDocInfo(n8));

        n9.putProp(Node.JSDOC_INFO_PROP, "JSDocInfo");
        assertEquals("JSDocInfo", getFunctionJsDocInfo(n9));

        n10.putProp(Node.JSDOC_INFO_PROP, "JSDocInfo");
        assertEquals("JSDocInfo", getFunctionJsDocInfo(n10));
    }
}