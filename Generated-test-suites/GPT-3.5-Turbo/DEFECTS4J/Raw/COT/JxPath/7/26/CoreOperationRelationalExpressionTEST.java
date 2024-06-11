
package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        // Create instance of CoreOperationRelationalExpression
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
        };
        
        // Test the value of getPrecedence method
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        // Create instance of CoreOperationRelationalExpression
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
        };
        
        // Test the value of isSymmetric method
        assertFalse(expression.isSymmetric());
    }
}

