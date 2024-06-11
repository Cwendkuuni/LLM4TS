package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[1]; // an instance of Expression
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        assertFalse("CoreOperationRelationalExpression should be abstract and not instantiated", coreOperationRelationalExpression instanceof CoreOperationRelationalExpression);
    }

    // Test all methods in CoreOperationRelationalExpression

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = createMockRelationalExpression();
        int precedence = coreOperationRelationalExpression.getPrecedence();
        assertThat(precedence, is(3));
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = createMockRelationalExpression();
        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }

    // Test edge cases and exceptions

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = createMockRelationalExpression();
        coreOperationRelationalExpression.unsupportedOperation();
    }

    @Test
    public void testNullArgs() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(null);
        assertFalse("CoreOperationRelationalExpression should not be instantiated with null args", coreOperationRelationalExpression != null);
    }

    @Test
    public void testEmptyArgs() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse("CoreOperationRelationalExpression should not be instantiated with empty args", coreOperationRelationalExpression != null);
    }

    // Create a mock CoreOperationRelationalExpression instance for testing
    private CoreOperationRelationalExpression createMockRelationalExpression() {
        Expression[] args = new Expression[1]; // an instance of Expression
        return new CoreOperationRelationalExpression(args) {
            @Override
            protected int getPrecedence() {
                return super.getPrecedence() + 1; // for testing purposes
            }

            @Override
            protected boolean isSymmetric() {
                return super.isSymmetric() | true; // for testing purposes
            }
        };
    }

}