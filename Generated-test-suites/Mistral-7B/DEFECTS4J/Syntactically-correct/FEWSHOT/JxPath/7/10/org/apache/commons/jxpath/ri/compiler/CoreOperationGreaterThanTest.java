package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();

        // Test with double values
        Expression arg1 = new CoreNumber(new Double(5.0));
        Expression arg2 = new CoreNumber(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        arg1 = new CoreNumber(new Double(3.0));
        arg2 = new CoreNumber(new Double(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        arg1 = new CoreNumber(new Double(5.0));
        arg2 = new CoreNumber(new Double(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        // Test with negative double values
        arg1 = new CoreNumber(new Double(-5.0));
        arg2 = new CoreNumber(new Double(-3.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        arg1 = new CoreNumber(new Double(-3.0));
        arg2 = new CoreNumber(new Double(-5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        arg1 = new CoreNumber(new Double(-5.0));
        arg2 = new CoreNumber(new Double(-5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        // Test with InfoSet values
        arg1 = new CoreVariable("x");
        arg2 = new CoreNumber(new Double(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet varValue = new InfoSet();
        varValue.setDoubleValue("x", 3.0);
        context.setCurrentInfoSet(varValue);
        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        varValue.setDoubleValue("x", 5.0);
        context.setCurrentInfoSet(varValue);
        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        arg1 = new CoreNumber(new Double(5.0));
        arg2 = new CoreVariable("x");
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context.setCurrentInfoSet(infoSet);
        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        varValue.setDoubleValue("x", 3.0);
        context.setCurrentInfoSet(varValue);
        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        varValue.setDoubleValue("x", 5.0);
        context.setCurrentInfoSet(varValue);
        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));
    }
}