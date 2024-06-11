
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(expression1, expression2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 is less than arg2
        Expression expression1 = new Expression();
        expression1.setValue(5);
        
        Expression expression2 = new Expression();
        expression2.setValue(10);
        
        assertTrue(lessThanOrEqual.computeValue(context));
        
        // Test when arg1 is equal to arg2
        Expression expression3 = new Expression();
        expression3.setValue(15);
        
        Expression expression4 = new Expression();
        expression4.setValue(15);
        
        assertTrue(lessThanOrEqual.computeValue(context));
        
        // Test when arg1 is greater than arg2
        Expression expression5 = new Expression();
        expression5.setValue(20);
        
        Expression expression6 = new Expression();
        expression6.setValue(18);
        
        assertFalse(lessThanOrEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(expression1, expression2);
        
        assertEquals("<=", lessThanOrEqual.getSymbol());
    }
}
