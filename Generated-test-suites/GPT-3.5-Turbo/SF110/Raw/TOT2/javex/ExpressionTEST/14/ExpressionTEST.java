import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test expression");
        
        assertNotNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testConstructor() {
        Expression expression = new Expression("test expression");
        
        assertNotNull(expression);
        assertEquals("test expression", expression.expression);
        assertFalse(expression.prepared);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("test"));
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
        
        assertTrue(expression.isMathFunction("divide"));
        assertFalse(expression.isMathFunction("and"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("length"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        
        assertTrue(expression.isStringFunction("toUpperCase"));
        assertFalse(expression.isStringFunction("power"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        
        assertFalse(expression.isExternalFunction("test"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        
        assertTrue(expression.isFunction("abs"));
        assertFalse(expression.isFunction("test"));
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression("2 + 3");
        expression.prepare();
        
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression("2 + 3");
        expression.tokenize();
        
        assertNotNull(expression.getStackCopy());
    }
    
    @Test
    public void testEvaluate() throws Exception {
        // Testing the abstract method evaluate() is challenging without specific implementation.
        // Here, we can test a dummy scenario that should result in an exception.
        Expression expression = new Expression("2 + x");
        
        assertThrows(Exception.class, () -> expression.evaluate());
    }
}