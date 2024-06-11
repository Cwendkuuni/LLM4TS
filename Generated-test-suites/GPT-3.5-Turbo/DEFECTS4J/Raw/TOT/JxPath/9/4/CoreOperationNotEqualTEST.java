import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_EqualValues() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("A"), new Expression("A"));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_UnequalValues() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("A"), new Expression("B"));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_NullValues() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(null), new Expression(null));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_MixedValues() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("A"), new Expression(null));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_IncompatibleTypes() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression("1"), new Expression(1));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}