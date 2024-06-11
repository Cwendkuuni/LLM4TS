import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Test typical use case
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        arg1.setValue(5);
        arg2.setValue(10);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test when arg1 and arg2 are equal
        arg1.setValue(10);
        arg2.setValue(10);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test when arg1 is greater than arg2
        arg1.setValue(15);
        arg2.setValue(10);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        // Test when arg1 is less than arg2
        arg1.setValue(5);
        arg2.setValue(10);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test when arg1 and arg2 are negative values
        arg1.setValue(-5);
        arg2.setValue(-10);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        // Test when arg1 is negative and arg2 is positive
        arg1.setValue(-10);
        arg2.setValue(5);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test when arg1 is positive and arg2 is negative
        arg1.setValue(10);
        arg2.setValue(-5);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertEquals("<=", expression.getSymbol());
    }
}