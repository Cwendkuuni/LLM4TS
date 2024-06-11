
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new LiteralExpression(5), new LiteralExpression(10));
        EvalContext context = new EvalContext();
        Boolean result = operation.computeValue(context);
        assertFalse(result);
    }
    
    @Test
    public void testComputeValue_Equals() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new LiteralExpression(10), new LiteralExpression(10));
        EvalContext context = new EvalContext();
        Boolean result = operation.computeValue(context);
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new LiteralExpression(15), new LiteralExpression(10));
        EvalContext context = new EvalContext();
        Boolean result = operation.computeValue(context);
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_FloatValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new LiteralExpression(10.5f), new LiteralExpression(10.2f));
        EvalContext context = new EvalContext();
        Boolean result = operation.computeValue(context);
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_InvalidArgument() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new LiteralExpression("abc"), new LiteralExpression(10));
        EvalContext context = new EvalContext();
        Boolean result = operation.computeValue(context);
        assertFalse(result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullArguments() {
        new CoreOperationGreaterThanOrEqual(null, null);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new LiteralExpression(5), new LiteralExpression(10));
        assertEquals(">=", operation.getSymbol());
    }

}
