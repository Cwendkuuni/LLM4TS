
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    private Expression arg1 = new Expression() {
        @Override
        public Object computeValue(EvalContext context) {
            return 5;
        }
    };

    private Expression arg2 = new Expression() {
        @Override
        public Object computeValue(EvalContext context) {
            return 3;
        }
    };

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(Boolean.TRUE, op.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", op.getSymbol());
    }
}

