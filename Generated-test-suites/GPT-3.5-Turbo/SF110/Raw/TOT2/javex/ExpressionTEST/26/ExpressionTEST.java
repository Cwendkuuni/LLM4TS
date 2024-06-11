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
        expression.doEvaluationStack();
        // Add test cases here for doEvaluationStack method
    }
    
    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("and"));
        assertNull(Expression.getReservedFunctions("random"));
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("random"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("random"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isMathFunction("sqrt"));
        assertFalse(expression.isMathFunction("random"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("random"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isStringFunction("startsWith"));
        assertFalse(expression.isStringFunction("random"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("test");
        assertNotNull(expression.getExternalFunction("startsWith"));
        assertNull(expression.getExternalFunction("random"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isExternalFunction("startsWith"));
        assertFalse(expression.isExternalFunction("random"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression("test");
        assertNotNull(expression.getFunction("startsWith"));
        assertNull(expression.getFunction("random"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression("test");
        assertTrue(expression.isFunction("startsWith"));
        assertFalse(expression.isFunction("random"));
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression("test");
        expression.prepare();
        // Add test cases here for prepare method
    }
    
    @Test
    public void testEvaluate() {
        Expression expression = new Expression("test");
        try {
            assertNotNull(expression.evaluate());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression("test");
        expression.prepare();
        // Add test cases here for tokenize method
    }
}