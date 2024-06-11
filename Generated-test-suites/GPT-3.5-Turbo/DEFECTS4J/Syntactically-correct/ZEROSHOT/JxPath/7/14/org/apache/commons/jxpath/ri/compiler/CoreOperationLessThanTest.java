import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_WhenArg1IsLessThanArg2_ReturnsTrue() {
        CoreOperationLessThan coreOp = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext evalContext = new EvalContext();
        double arg1Value = 5.0;
        double arg2Value = 10.0;
        evalContext.setNodeSet(new double[]{arg1Value, arg2Value});
        Object result = coreOp.computeValue(evalContext);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_WhenArg1IsEqualToArg2_ReturnsFalse() {
        CoreOperationLessThan coreOp = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext evalContext = new EvalContext();
        double arg1Value = 5.0;
        double arg2Value = 5.0;
        evalContext.setNodeSet(new double[]{arg1Value, arg2Value});
        Object result = coreOp.computeValue(evalContext);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_WhenArg1IsGreaterThanArg2_ReturnsFalse() {
        CoreOperationLessThan coreOp = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext evalContext = new EvalContext();
        double arg1Value = 10.0;
        double arg2Value = 5.0;
        evalContext.setNodeSet(new double[]{arg1Value, arg2Value});
        Object result = coreOp.computeValue(evalContext);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_ReturnsLessThanSymbol() {
        CoreOperationLessThan coreOp = new CoreOperationLessThan(new Expression(), new Expression());
        String symbol = coreOp.getSymbol();
        assertEquals("<", symbol);
    }
}