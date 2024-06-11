package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.JXPathTestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreBoolean(false), new CoreBoolean(false));
        EvalContext context = JXPathTestCase.createContext(null);

        assertEquals(Boolean.TRUE, operation.computeValue(context));
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreInteger(5), new CoreInteger(3));

        assertEquals(">=", operation.getSymbol());
    }
}