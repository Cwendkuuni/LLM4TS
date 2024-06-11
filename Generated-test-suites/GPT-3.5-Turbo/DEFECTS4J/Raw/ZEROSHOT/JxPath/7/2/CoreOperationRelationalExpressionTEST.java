
package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testGetPrecedence() {
        Expression[] args = {new CoreOperation()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {new CoreOperation()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean isSymmetric = expression.isSymmetric();
        assertFalse(isSymmetric);
    }
}
