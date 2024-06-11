import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueLessThan() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        double l = InfoSetUtil.doubleValue(expression.getArgs()[0].computeValue(context));
        double r = InfoSetUtil.doubleValue(expression.getArgs()[1].computeValue(context));
        assertEquals(l < r, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        assertEquals("<", expression.getSymbol());
    }
}