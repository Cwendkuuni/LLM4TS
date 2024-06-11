
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);

        context.setValue("arg1", 5);
        context.setValue("arg2", 3);
        operation.setArgs(new Expression[] { new CoreVariableReference("arg1"), new CoreVariableReference("arg2") });
        assertEquals(true, operation.computeValue(context));

        context.setValue("arg1", 3);
        context.setValue("arg2", 5);
        operation.setArgs(new Expression[] { new CoreVariableReference("arg1"), new CoreVariableReference("arg2") });
        assertEquals(false, operation.computeValue(context));

        context.setValue("arg1", 5);
        context.setValue("arg2", 5);
        operation.setArgs(new Expression[] { new CoreVariableReference("arg1"), new CoreVariableReference("arg2") });
        assertEquals(true, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        assertEquals(">=", operation.getSymbol());
    }
}
