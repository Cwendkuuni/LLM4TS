import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    private CoreOperationNotEqual coreOperationNotEqual;
    
    @Before
    public void setUp() {
        coreOperationNotEqual = new CoreOperationNotEqual(null, null);
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test when args are equal
        Expression[] args1 = new Expression[2];
        args1[0] = new Expression();
        args1[1] = new Expression();
        coreOperationNotEqual.setArgs(args1);
        Boolean result1 = coreOperationNotEqual.computeValue(context);
        assertFalse(result1);
        
        // Test when args are not equal
        Expression[] args2 = new Expression[2];
        args2[0] = new Expression();
        args2[1] = new Expression();
        args2[1].setValue("test");
        coreOperationNotEqual.setArgs(args2);
        Boolean result2 = coreOperationNotEqual.computeValue(context);
        assertTrue(result2);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }
}