import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(15);
        Expression arg2 = new Constant(5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        String symbol = operation.getSymbol();

        assertEquals("<=", symbol);
    }
  
    @Test
    public void testComputeValueWithNegativeNumbers() {
        Expression arg1 = new Constant(-5);
        Expression arg2 = new Constant(-10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithEqualNumbers() {
        Expression arg1 = new Constant(20);
        Expression arg2 = new Constant(20);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

}