package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.test.TestSupport;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(TestSupport.createNumberValue(1), TestSupport.createNumberValue(1));
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue("Expected true but was false", result instanceof Boolean && ((Boolean) result).booleanValue());

        coreOperationEqual = new CoreOperationEqual(TestSupport.createStringValue("foo"), TestSupport.createStringValue("foo"));
        result = coreOperationEqual.computeValue(context);
        assertTrue("Expected true but was false", result instanceof Boolean && ((Boolean) result).booleanValue());

        coreOperationEqual = new CoreOperationEqual(TestSupport.createNumberValue(1), TestSupport.createNumberValue(2));
        result = coreOperationEqual.computeValue(context);
        assertFalse("Expected false but was true", result instanceof Boolean && ((Boolean) result).booleanValue());

        coreOperationEqual = new CoreOperationEqual(TestSupport.createStringValue("foo"), TestSupport.createStringValue("bar"));
        result = coreOperationEqual.computeValue(context);
        assertFalse("Expected false but was true", result instanceof Boolean && ((Boolean) result).booleanValue());
    }
}