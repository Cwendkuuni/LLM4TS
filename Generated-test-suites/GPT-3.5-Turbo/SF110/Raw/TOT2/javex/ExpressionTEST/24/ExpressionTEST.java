import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        assertNotNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("test");
        expression.prepare();
        // Add test cases to verify the evaluation stack logic
    }
    
    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("+"));
        // Add more test cases to check if reserved functions are returned correctly
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isReservedFunction("and"));
        // Add more test cases for reserved function
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isLogicalFunction("and"));
        // Add more test cases for logical function
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isMathFunction("sqrt"));
        // Add more test cases for math function
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isDateFunction("dateAdd"));
        // Add more test cases for date function
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isStringFunction("toLowerCase"));
        // Add more test cases for string function
    }
    
    // Add more test cases for remaining methods
    
    @Test
    public void testEvaluate() {
        Expression expression = new Expression("test");
        try {
            assertNotNull(expression.evaluate());
        } catch (Exception e) {
            fail("Exception thrown while evaluating expression");
        }
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression("test");
        expression.prepare();
        assertTrue(expression.isPrepared());
    }
    
    // Add more test cases for other methods
    
}