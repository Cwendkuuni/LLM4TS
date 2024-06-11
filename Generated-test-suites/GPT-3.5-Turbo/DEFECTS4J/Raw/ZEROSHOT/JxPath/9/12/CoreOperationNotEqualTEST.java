import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        Object value = operation.computeValue(context);
        assertTrue((Boolean)value);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        assertEquals("!=", operation.getSymbol());
    }
    
    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(operation);
        assertEquals(arg1, operation.getArg1());
        assertEquals(arg2, operation.getArg2());
    }
    
    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        Object value = operation.computeValue(context);
        assertTrue((Boolean)value);
    }
}