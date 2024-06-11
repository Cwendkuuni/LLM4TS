import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan coreOp = new CoreOperationGreaterThan(new Literal(5), new Literal(3));
        EvalContext context = new EvalContext();
        Object result = coreOp.computeValue(context);
        assertEquals(Boolean.TRUE, result); // Typical use case
        
        coreOp = new CoreOperationGreaterThan(new Literal(2.5), new Literal(2.5));
        result = coreOp.computeValue(context);
        assertEquals(Boolean.FALSE, result); // Edge case where values are equal
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOp = new CoreOperationGreaterThan(new Literal(5), new Literal(3));
        assertEquals(">", coreOp.getSymbol()); // Typical use case
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullArg1() {
        new CoreOperationGreaterThan(null, new Literal(3)); // Error scenario: passing null as arg1
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullArg2() {
        new CoreOperationGreaterThan(new Literal(5), null); // Error scenario: passing null as arg2
    }
    
    @Test
    public void testComputeValueEdgeCases() {
        CoreOperationGreaterThan coreOp = new CoreOperationGreaterThan(new Literal(Double.POSITIVE_INFINITY), new Literal(Double.NEGATIVE_INFINITY));
        EvalContext context = new EvalContext();
        Object result = coreOp.computeValue(context);
        assertEquals(Boolean.TRUE, result); // Edge case: comparing positive infinity with negative infinity
    }
}