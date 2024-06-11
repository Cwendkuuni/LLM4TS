import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue1() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setNodeSet(nodeSet);
        Object result = expression.computeValue(context);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testComputeValue2() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setNodeSet(nodeSet);
        Object result = expression.computeValue(context);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testComputeValue3() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setNodeSet(nodeSet);
        Object result = expression.computeValue(context);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = expression.getSymbol();
        assertEquals("<=", symbol);
    }

}