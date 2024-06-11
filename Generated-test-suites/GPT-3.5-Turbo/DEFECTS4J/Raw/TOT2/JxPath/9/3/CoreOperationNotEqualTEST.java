import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_NotEqual() {
        Expression arg1 = new Variable();
        arg1.setValue("value1");
        
        Expression arg2 = new Constant();
        arg2.setValue("value2");
        
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Variable();
        Expression arg2 = new Constant();
        
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        assertEquals("!=", operation.getSymbol());
    }
}