package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        // Test with Boolean values
        CoreOperationEqual coreOperationEqual1 = new CoreOperationEqual(Boolean.TRUE, Boolean.TRUE);
        EvalContext context = new EvalContext();
        assertEquals("Boolean true equals to Boolean true", Boolean.TRUE, coreOperationEqual1.computeValue(context));

        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(Boolean.FALSE, Boolean.FALSE);
        assertEquals("Boolean false equals to Boolean false", Boolean.TRUE, coreOperationEqual2.computeValue(context));

        CoreOperationEqual coreOperationEqual3 = new CoreOperationEqual(Boolean.TRUE, Boolean.FALSE);
        assertEquals("Boolean true does not equal to Boolean false", Boolean.FALSE, coreOperationEqual3.computeValue(context));

        // Test with Number values
        CoreOperationEqual coreOperationEqual4 = new CoreOperationEqual(5, 5);
        assertEquals("Number 5 equals to Number 5", Boolean.TRUE, coreOperationEqual4.computeValue(context));

        CoreOperationEqual coreOperationEqual5 = new CoreOperationEqual(5, 6);
        assertEquals("Number 5 does not equal to Number 6", Boolean.FALSE, coreOperationEqual5.computeValue(context));

        // Test with String values
        CoreOperationEqual coreOperationEqual6 = new CoreOperationEqual("Apache Commons", "Apache Commons");
        assertEquals("String 'Apache Commons' equals to String 'Apache Commons'", Boolean.TRUE, coreOperationEqual6.computeValue(context));

        CoreOperationEqual coreOperationEqual7 = new CoreOperationEqual("Java", "Commons");
        assertEquals("String 'Java' does not equal to String 'Commons'", Boolean.FALSE, coreOperationEqual7.computeValue(context));

        // Test with different classes
        CoreOperationEqual coreOperationEqual8 = new CoreOperationEqual(new Integer(5), new String("5"));
        assertEquals("Integer 5 does not equal to String '5'", Boolean.FALSE, coreOperationEqual8.computeValue(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualWithInvalidArguments() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, Boolean.TRUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualWithInvalidArguments2() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(Boolean.TRUE, null);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Integer(5), new Integer(5));
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("Symbol for '=' operation", "=", symbol);
    }
}