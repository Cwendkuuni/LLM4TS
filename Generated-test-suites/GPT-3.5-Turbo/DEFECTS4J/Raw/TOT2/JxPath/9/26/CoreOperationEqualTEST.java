import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testConstructor() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expression1, expression2);
        assertNotNull(coreOperationEqual);
    }

    @Test
    public void testComputeValue_equalValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expression1, expression2);
        EvalContext context = new EvalContext();
        context.setContextNodePointer(new Pointer());
        context.setContextPosition(1);
        context.setContextSize(1);
        
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValue_unequalValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expression1, expression2);
        EvalContext context = new EvalContext();
        context.setContextNodePointer(new Pointer());
        context.setContextPosition(1);
        context.setContextSize(1);
        
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expression1, expression2);
        
        assertEquals("=", coreOperationEqual.getSymbol());
    }
    
    // Edge Cases and Exception Handling Scenarios
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullExpression1() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, expression2);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullExpression2() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expression1, null);
    }

    @Test
    public void testComputeValue_nullContext() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expression1, expression2);
        
        assertNull(coreOperationEqual.computeValue(null));
    }

}