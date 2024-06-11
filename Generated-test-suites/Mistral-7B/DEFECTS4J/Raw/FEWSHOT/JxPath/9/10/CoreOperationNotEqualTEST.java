package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Value;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Constant(1), new Constant(2));
        EvalContext context = new EvalContext(new Object[]{}, new Object[]{});
        Value value = operation.computeValue(context);
        assertTrue(value.isBoolean() && value.getBooleanValue().booleanValue() == true);

        operation = new CoreOperationNotEqual(new Constant(2), new Constant(1));
        value = operation.computeValue(context);
        assertTrue(value.isBoolean() && value.getBooleanValue().booleanValue() == false);

        operation = new CoreOperationNotEqual(new Constant(1), new Constant(1));
        value = operation.computeValue(context);
        assertFalse(value.isBoolean() && value.getBooleanValue().booleanValue() == false);
    }
}