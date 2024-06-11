package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]);
        assertNotNull(coreOperationRelationalExpression);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]);
        int precedence = coreOperationRelationalExpression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]);
        boolean symmetric = coreOperationRelationalExpression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test(expected = AssertionError.class)
    public void testGetPrecedenceWhenNotSubclass() {
        CoreOperation coreOperation = new CoreOperation(new Expression[0]);
        coreOperation.getPrecedence();
    }

    @Test(expected = AssertionError.class)
    public void testIsSymmetricWhenNotSubclass() {
        CoreOperation coreOperation = new CoreOperation(new Expression[0]);
        coreOperation.isSymmetric();
    }

}