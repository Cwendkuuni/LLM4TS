
package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
    }

    @Test
    public void testIsContextDependent() {
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        assertNull(expression.computeValue(null));
    }

    @Test
    public void testCompute() {
        assertNull(expression.compute(null));
    }

    @Test
    public void testIterate() {
        assertNull(expression.iterate(null));
    }

    @Test
    public void testIteratePointers() {
        assertNull(expression.iteratePointers(null));
    }
}

