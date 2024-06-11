package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;
    private CheckGlobalThis globalThisChecker;
    private NodeTraversal.Callback callback;

    @Before
    public void setUp() {
        compiler = new MockAbstractCompiler();
        globalThisChecker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        callback = globalThisChecker;
    }

    @Test
    public void testShouldTraverse_ShouldTraverseIfFunctionIsNotConstructorAndDoesNotHaveThisTypeOrOverrideAnnotation_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        assertTrue(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldNotTraverseIfFunctionIsConstructor_ReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        node.setJSDocInfo(new JSDocInfo().setConstructor(true));
        assertFalse(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldNotTraverseIfFunctionHasThisTypeAnnotation_ReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        node.setJSDocInfo(new JSDocInfo().recordThisType());
        assertFalse(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldNotTraverseIfFunctionHasOverrideAnnotation_ReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        node.setJSDocInfo(new JSDocInfo().setOverride(true));
        assertFalse(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldTraverseIfFunctionHasNoLogicalPlaceForThisAnnotation_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        assertTrue(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldTraverseIfParentIsBlock_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        assertTrue(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldTraverseIfParentIsScript_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        assertTrue(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldTraverseIfParentIsName_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        assertTrue(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldTraverseIfParentIsAssign_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        assertTrue(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldTraverseIfParentIsString_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);
        assertTrue(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldTraverseIfParentIsNumber_ReturnsTrue() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);
        assertTrue(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldNotTraverseIfParentIsGetAndLastChildOfStringIsPrototype_ReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GETPROP);
        Node stringNode = new Node(Token.STRING, "prototype");
        parent.addChildToBack(stringNode);
        assertFalse(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testShouldTraverse_ShouldNotTraverseIfParentIsGetAndLastChildOfFirstChildOfStringIsPrototype_ReturnsFalse() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GETPROP);
        Node firstChild = new Node(Token.STRING, "foo");
        Node stringNode = new Node(Token.STRING, "prototype");
        parent.addChildToBack(firstChild);
        firstChild.addChildToBack(stringNode);
        assertFalse(globalThisChecker.shouldTraverse(new MockNodeTraversal(), node, parent));
    }

    @Test
    public void testVisit_ShouldReportErrorIfNodeIsGlobalThis() {
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        MockNodeTraversal traversal = new MockNodeTraversal();
        globalThisChecker.visit(traversal, node, parent);
        assertEquals(1, traversal.getErrorCount());
        assertEquals("dangerous use of the global 'this' object", traversal.getLastError().description);
    }

    @Test
    public void testVisit_ShouldNotReportErrorIfNodeIsNotGlobalThis() {
        Node node = new Node(Token.NAME, "foo");
        Node parent = new Node(Token.GETPROP);
        MockNodeTraversal traversal = new MockNodeTraversal();
        globalThisChecker.visit(traversal, node, parent);
        assertEquals(0, traversal.getErrorCount());
    }

    private static class MockAbstractCompiler extends AbstractCompiler {
        @Override
        public void report(JSError error) {
            // Mock implementation
        }
    }

    private static class MockNodeTraversal extends NodeTraversal {

        private int errorCount;
        private JSError lastError;

        MockNodeTraversal() {
            super(new AbstractCompiler() {
                @Override
                public void reportChangeToEnclosingScope(Node n) {
                    // Mock implementation
                }
            });
        }

        @Override
        public void reportError(JSError error) {
            errorCount++;
            lastError = error;
        }

        public int getErrorCount() {
            return errorCount;
        }

        public JSError getLastError() {
            return lastError;
        }
    }

}