package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(context));

        coreOperationLessThan = new CoreOperationLessThan(new CoreConstantExpression(1.0), new CoreConstantExpression(1.0));
        assertEquals(Boolean.TRUE, coreOperationLessThan.computeValue(context));

        coreOperationLessThan = new CoreOperationLessThan(new CoreConstantExpression(1.0), new CoreConstantExpression(1.0));
        assertEquals(Boolean.TRUE, coreOperationLessThan.computeValue(context));

        coreOperationLessThan = new CoreOperationLessThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NaN));
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(context));

        coreOperationLessThan = new CoreOperationLessThan(new CoreConstantExpression(Double.POSITIVE_INFINITY), new CoreConstantExpression(Double.NaN));
        assertEquals(Boolean.TRUE, coreOperationLessThan.computeValue(context));

        coreOperationLessThan = new CoreOperationLessThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.POSITIVE_INFINITY));
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(context));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanWithNonNumericArguments() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreStringConstantExpression("1"), new CoreStringConstantExpression("2"));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanWithNonNumericArguments2() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNodeTestExpression(), new CoreNodeTestExpression());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithNullArguments() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new CoreNullExpression(), new CoreNullExpression());
    }
}