import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    
    private CoreOperationLessThanOrEqual operation;
    
    @Before
    public void setUp() {
        operation = new CoreOperationLessThanOrEqual(null, null);
    }
    
    @Test
    public void testComputeValue_lessThanOrEqual() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new ConstantString("5");
        Expression arg2 = new ConstantString("10");
        
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_notLessThanOrEqual() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new ConstantString("10");
        Expression arg2 = new ConstantString("5");
        
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_equalValues() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new ConstantString("5");
        Expression arg2 = new ConstantString("5");
        
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }
}