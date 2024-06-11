
package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test 1: n.getType() is Token.FUNCTION and n is a constructor
        NodeTraversal t1 = new NodeTraversal(compiler, checkGlobalThis);
        Node n1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfoBuilder().recordConstructor().build();
        n1.setJSDocInfo(jsDoc1);
        assertFalse(checkGlobalThis.shouldTraverse(t1, n1, null));

        // Test 2: n.getType() is Token.FUNCTION and n has @this annotation
        NodeTraversal t2 = new NodeTraversal(compiler, checkGlobalThis);
        Node n2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc2 = new JSDocInfoBuilder().recordThisType().build();
        n2.setJSDocInfo(jsDoc2);
        assertFalse(checkGlobalThis.shouldTraverse(t2, n2, null));

        // Test 3: n.getType() is Token.FUNCTION and n has @override annotation
        NodeTraversal t3 = new NodeTraversal(compiler, checkGlobalThis);
        Node n3 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc3 = new JSDocInfoBuilder().recordOverride().build();
        n3.setJSDocInfo(jsDoc3);
        assertFalse(checkGlobalThis.shouldTraverse(t3, n3, null));

        // Test 4: n.getType() is Token.FUNCTION and parent is not Token.BLOCK, Token.SCRIPT, Token.NAME, or Token.ASSIGN
        NodeTraversal t4 = new NodeTraversal(compiler, checkGlobalThis);
        Node n4 = new Node(Token.FUNCTION);
        Node parent4 = new Node(Token.NOT);
        assertFalse(checkGlobalThis.shouldTraverse(t4, n4, parent4));

        // Test 5: parent is null and n.getType() is not Token.ASSIGN
        NodeTraversal t5 = new NodeTraversal(compiler, checkGlobalThis);
        Node n5 = new Node(Token.FUNCTION);
        assertNull(n5.getParent());
        assertFalse(checkGlobalThis.shouldTraverse(t5, n5, null));

        // Test 6: parent is not null and parent.getType() is not Token.ASSIGN
        NodeTraversal t6 = new NodeTraversal(compiler, checkGlobalThis);
        Node n6 = new Node(Token.FUNCTION);
        Node parent6 = new Node(Token.NOT);
        assertFalse(checkGlobalThis.shouldTraverse(t6, n6, parent6));

        // Test 7: parent is not null, parent.getType() is Token.ASSIGN, n != lhs, lhs is GETPROP, and lhs property is "prototype"
        NodeTraversal t7 = new NodeTraversal(compiler, checkGlobalThis);
        Node n7 = new Node(Token.FUNCTION);
        Node parent7 = new Node(Token.ASSIGN);
        Node lhs7 = new Node(Token.GETPROP);
        lhs7.addChildToBack(new Node(Token.STRING, "prototype"));
        parent7.addChildToBack(lhs7);
        assertFalse(checkGlobalThis.shouldTraverse(t7, n7, parent7));

        // Test 8: parent is not null, parent.getType() is Token.ASSIGN, n != lhs, lhs is GETPROP and lhs contains ".prototype."
        NodeTraversal t8 = new NodeTraversal(compiler, checkGlobalThis);
        Node n8 = new Node(Token.FUNCTION);
        Node parent8 = new Node(Token.ASSIGN);
        Node lhs8 = new Node(Token.GETPROP);
        lhs8.addChildToBack(new Node(Token.STRING, "foo.prototype.bar"));
        parent8.addChildToBack(lhs8);
        assertFalse(checkGlobalThis.shouldTraverse(t8, n8, parent8));

        // Test 9: parent is not null, parent.getType() is Token.ASSIGN and n == lhs
        NodeTraversal t9 = new NodeTraversal(compiler, checkGlobalThis);
        Node n9 = new Node(Token.FUNCTION);
        Node parent9 = new Node(Token.ASSIGN);
        Node lhs9 = new Node(Token.NAME, "x");
        parent9.addChildToBack(lhs9);
        assertFalse(checkGlobalThis.shouldTraverse(t9, n9, parent9));
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test 1: n.getType() is Token.THIS, shouldReportThis() returns true
        NodeTraversal t1 = new NodeTraversal(compiler, checkGlobalThis);
        Node n1 = new Node(Token.THIS);
        Node parent1 = new Node(Token.GETPROP);
        assertTrue(checkGlobalThis.shouldReportThis(n1, parent1));

        // Test 2: n.getType() is Token.THIS, shouldReportThis() returns false
        NodeTraversal t2 = new NodeTraversal(compiler, checkGlobalThis);
        Node n2 = new Node(Token.THIS);
        Node parent2 = new Node(Token.NOT);
        assertFalse(checkGlobalThis.shouldReportThis(n2, parent2));

        // Test 3: n != assignLhsChild
        NodeTraversal t3 = new NodeTraversal(compiler, checkGlobalThis);
        Node n3 = new Node(Token.NAME, "x");
        Node parent3 = new Node(Token.NAME, "y");
        checkGlobalThis.visit(t3, n3, parent3);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        // Test 1: n.getJSDocInfo() is not null
        Node n1 = new Node(Token.NAME, "x");
        JSDocInfo jsDoc1 = new JSDocInfoBuilder().build();
        n1.setJSDocInfo(jsDoc1);
        assertEquals(jsDoc1, checkGlobalThis.getFunctionJsDocInfo(n1));

        // Test 2: parent.getType() is Token.NAME and parent.getJSDocInfo() is not null
        Node n2 = new Node(Token.NAME, "x");
        Node parent2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc2 = new JSDocInfoBuilder().build();
        parent2.setJSDocInfo(jsDoc2);
        parent2.addChildToFront(n2);
        assertEquals(jsDoc2, checkGlobalThis.getFunctionJsDocInfo(n2));

        // Test 3: parent.getType() is Token.ASSIGN and parent.getJSDocInfo() is null
        Node n3 = new Node(Token.NAME, "x");
        Node parent3 = new Node(Token.ASSIGN);
        parent3.addChildToFront(n3);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n3));

        // Test 4: parent.getType() is Token.NAME and parent.getParent().getType() is Token.VAR
        Node n4 = new Node(Token.NAME, "x");
        Node parent4 = new Node(Token.NAME, "y");
        parent4.setParent(new Node(Token.VAR));
        parent4.addChildToFront(n4);
        assertNull(checkGlobalThis.getFunctionJsDocInfo(n4));

    }

}
