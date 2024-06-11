import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        
        assertNotNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("test");
        
        assertEquals("test", expression.expression);
        assertNotNull(expression.stack);
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("invalid"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression();
        
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("abs"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression();
        
        assertTrue(expression.isMathFunction("sqrt"));
        assertFalse(expression.isMathFunction("isEmpty"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("isNull"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        
        assertTrue(expression.isStringFunction("toLowerCase"));
        assertFalse(expression.isStringFunction("greaterThan"));
    }
    
    // Add more test cases based on identified edge cases and exceptions
    
}