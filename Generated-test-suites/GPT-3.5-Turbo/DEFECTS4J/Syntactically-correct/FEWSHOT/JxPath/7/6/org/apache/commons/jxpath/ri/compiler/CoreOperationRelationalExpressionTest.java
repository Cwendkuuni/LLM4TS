
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected final int getPrecedence() {
                return 3;
            }

            @Override
            protected final boolean isSymmetric() {
                return false;
            }
        };

        int result = expression.getPrecedence();

        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected final int getPrecedence() {
                return 3;
            }

            @Override
            protected final boolean isSymmetric() {
                return false;
            }
        };

        boolean result = expression.isSymmetric();

        assertEquals(false, result);
    }
}
