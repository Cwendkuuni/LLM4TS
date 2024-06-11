package com.google.javascript.jscomp;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CheckGlobalThisTest {
  
    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setup() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @After
    public void teardown() {
        compiler = null;
        level = null;
        checkGlobalThis = null;
    }

    @Test
    public void testShouldTraverseFunctionWithoutAnnotationBlockReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverseFunctionWithoutAnnotationScriptReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverseFunctionWithoutAnnotationNameReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverseFunctionWithoutAnnotationAssignReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverseFunctionWithoutAnnotationStringReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverseFunctionWithoutAnnotationNumberReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldReportThisOnLeftSideOfAssignAssignLhsChildNotNullReturnsTrue() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.assignLhsChild = new Node(Token.THIS);

        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldReportThisWithPropertyAccessParentIsPropertyAccessReturnsTrue() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(true, result);
    }


    @Test
    public void testShouldReportThisWithPropertyAccessParentIsNotPropertyAccessReturnsFalse() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);

        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(false, result);
    }
}