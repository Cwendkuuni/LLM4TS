import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression exp = new Expression("2+3");
        assertNull(exp.getExternalFunctions());
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression exp = new Expression("2+3");
        exp.prepare();
        assertNotNull(exp.stack);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression exp = new Expression("2+3");
        assertTrue(exp.isReservedFunction("+"));
        assertFalse(exp.isReservedFunction("*"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression exp = new Expression("and(2>3)");
        assertTrue(exp.isLogicalFunction("and"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression exp = new Expression("2*3");
        assertTrue(exp.isMathFunction("*"));
        assertFalse(exp.isMathFunction("sqrt"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression exp = new Expression("dateAdd(2021-12-01, 1)");
        assertTrue(exp.isDateFunction("dateAdd"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression exp = new Expression("contains('example', 'ex')");
        assertTrue(exp.isStringFunction("contains"));
        assertFalse(exp.isStringFunction("startsWith"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression exp = new Expression("2+3");
        assertNull(exp.getExternalFunction("abs"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression exp = new Expression("2+3");
        assertFalse(exp.isExternalFunction("abs"));
    }
    
    @Test
    public void testGetFunction() {
        Expression exp = new Expression("2+3");
        assertNotNull(exp.getFunction("+"));
    }
    
    @Test
    public void testIsFunction() {
        Expression exp = new Expression("2+3");
        assertTrue(exp.isFunction("+"));
        assertFalse(exp.isFunction("abc"));
    }
    
    @Test
    public void testPrepare() {
        Expression exp = new Expression("2+3");
        assertDoesNotThrow(() -> exp.prepare());
    }
    
    @Test
    public void testEvaluate() {
        Expression exp = new Expression("1+1");
        assertThrows(Exception.class, () -> exp.evaluate());
    }
}