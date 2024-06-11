package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        assertNotNull(new CoreOperationRelationalExpression(new Expression[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionConstructorNullArguments() {
        new CoreOperationRelationalExpression(null);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertTrue(expression.getPrecedence() == 3);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }
}