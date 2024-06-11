import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Literal(5);
        Expression arg2 = new Literal(10);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = lessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithEqualValues() {
        Expression arg1 = new Literal(5);
        Expression arg2 = new Literal(5);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = lessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithGreaterValue() {
        Expression arg1 = new Literal(10);
        Expression arg2 = new Literal(5);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = lessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Literal(5);
        Expression arg2 = new Literal(10);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        String symbol = lessThanOrEqual.getSymbol();
        assertEquals("<=", symbol);
    }
}