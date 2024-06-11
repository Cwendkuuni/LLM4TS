package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse_invalidContext_notTraversed() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

        // Function is a constructor
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.CLASS));
        assertEquals(false, shouldTraverse);
        
        // Function has @this annotation
        shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.EMPTY));
        assertEquals(false, shouldTraverse);
        
        // Function has @override annotation
        shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.EMPTY));
        assertEquals(false, shouldTraverse);
        
        // Function is not associated with a @this annotation and has logical place to put one
        shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.EMPTY));
        assertEquals(false, shouldTraverse);
        
        // Parent is not a valid context for a function
        shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.EMPTY));
        assertEquals(false, shouldTraverse);
    }

    @Test
    public void testShouldTraverse_validContext_traversed() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

        // Valid context for a function, should traverse
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.BLOCK));
        assertEquals(true, shouldTraverse);
        
        shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.SCRIPT));
        assertEquals(true, shouldTraverse);
        
        shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.NAME));
        assertEquals(true, shouldTraverse);
        
        shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.ASSIGN));
        assertEquals(true, shouldTraverse);
        
        shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.STRING));
        assertEquals(true, shouldTraverse);
        
        shouldTraverse = checkGlobalThis.shouldTraverse(null, createNode(Token.FUNCTION), createNode(Token.NUMBER));
        assertEquals(true, shouldTraverse);
    }

    @Test
    public void testShouldTraverse_isAssignLeftSide_traversed() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node assignParent = createNode(Token.ASSIGN);
        Node lhsNode = createNode(Token.NAME);

        // LHS is traversed always
        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, lhsNode, assignParent);
        assertEquals(true, shouldTraverse);
    }

    @Test
    public void testShouldTraverse_isAssignRightSide_notTraversed() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node assignParent = createNode(Token.ASSIGN);
        Node rhsNode = createNode(Token.NAME);

        // RHS is not traversed if it's an assignment to a prototype property or subproperty
        assignParent.addChildToBack(createNode(Token.GETPROP));
        assignParent.addChildToBack(rhsNode);

        boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, rhsNode, assignParent);
        assertEquals(false, shouldTraverse);
    }

    @Test
    public void testVisit_this_shouldReport() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node thisNode = createNode(Token.THIS);

        // Should report if assignLhsChild is not null
        checkGlobalThis.assignLhsChild = createNode(Token.NAME);
        checkGlobalThis.visit(null, thisNode, null);
        //Assert that report was made

        // Should report if this is on the left side of a property access
        Node parent = createNode(Token.GETPROP);
        parent.addChildToBack(thisNode);
        checkGlobalThis.assignLhsChild = null;
        checkGlobalThis.visit(null, thisNode, parent);
        //Assert that report was made
    }

    @Test
    public void testVisit_this_shouldNotReport() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node thisNode = createNode(Token.THIS);

        // Should not report if assignLhsChild is null and this is not on the left side of a property access
        checkGlobalThis.assignLhsChild = null;
        checkGlobalThis.visit(null, thisNode, null);
        //Assert that report was not made

        Node parent = createNode(Token.GETPROP);

        // Should not report if this is not on the left side of a property access
        parent.addChildToBack(createNode(Token.NAME));
        parent.addChildToBack(thisNode);
        checkGlobalThis.assignLhsChild = null;
        checkGlobalThis.visit(null, thisNode, parent);
        //Assert that report was not made

        // Should not report if this is not the node being traversed
        Node n = createNode(Token.ADD);
        n.addChildToBack(createNode(Token.THIS));
        checkGlobalThis.assignLhsChild = null;
        checkGlobalThis.visit(null, n, null);
        //Assert that report was not made
    }

    private Node createNode(int type) {
        return new Node(type);
    }
}