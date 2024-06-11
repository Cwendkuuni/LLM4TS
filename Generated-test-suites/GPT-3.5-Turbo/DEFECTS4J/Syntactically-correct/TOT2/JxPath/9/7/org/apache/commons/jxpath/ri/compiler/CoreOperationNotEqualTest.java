import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        assertNotNull(coreOperationNotEqual);
        assertEquals(arg1, coreOperationNotEqual.args[0]);
        assertEquals(arg2, coreOperationNotEqual.args[1]);
    }

    @Test
    public void testComputeValueNotEqual() {
        EvalContext context = new MockEvalContext();
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        context.setValue(null, "test1", "value1");
        context.setValue(null, "test2", "value2");
        
        Object result = coreOperationNotEqual.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueEqual() {
        EvalContext context = new MockEvalContext();
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        context.setValue(null, "test1", "value3");
        context.setValue(null, "test2", "value3");
        
        Object result = coreOperationNotEqual.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    // Additional edge cases and exception handling scenarios tests can be added here

}