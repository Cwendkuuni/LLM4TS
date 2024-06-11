import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertNotNull(operation);
        assertEquals(arg1, operation.getArgs()[0]);
        assertEquals(arg2, operation.getArgs()[1]);
    }
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        // Test case 1: Arguments are numbers
        arg1.setValue(10);
        arg2.setValue(5);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Test case 2: Arguments are equal
        arg1.setValue(10);
        arg2.setValue(10);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test case 3: Arguments are not numbers
        arg1.setValue("10");
        arg2.setValue("5");
        // Handle exception or update method to return false
        
        // Add more test cases as needed
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertEquals("<=", operation.getSymbol());
    }

}