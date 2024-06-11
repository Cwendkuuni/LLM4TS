package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNode(new Node());
        context.setExpressionContext(new ExpressionContext());

        // Test case 1
        double value1 = 5.0;
        double value2 = 10.0;
        lessThan.getArgs()[0].computeValue(context);

        // Test case 2
        double value1 = 10.0;
        double value2 = 5.0;
        lessThan.getArgs()[1].computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        assertEquals("<", lessThan.getSymbol());
    }
}