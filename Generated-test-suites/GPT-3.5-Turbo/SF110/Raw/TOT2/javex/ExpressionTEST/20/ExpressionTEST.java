import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression exp = new Expression("test");
        assertNotNull(exp.getExternalFunctions());
    }
    
    @Test
    public void testExpressionConstructor() {
        Expression exp = new Expression("test");
        assertNotNull(exp);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression exp = new Expression("test");
        assertTrue(exp.isReservedFunction("and"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression exp = new Expression("test");
        assertTrue(exp.isLogicalFunction("and"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression exp = new Expression("test");
        assertTrue(exp.isMathFunction("multiply"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression exp = new Expression("test");
        assertTrue(exp.isDateFunction("dateadd"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression exp = new Expression("test");
        assertTrue(exp.isStringFunction("tolower"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression exp = new Expression("test");
        assertNull(exp.getExternalFunction("external"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression exp = new Expression("test");
        assertFalse(exp.isExternalFunction("external"));
    }
    
    @Test
    public void testGetFunction() {
        Expression exp = new Expression("test");
        assertNotNull(exp.getFunction("and"));
    }
    
    @Test
    public void testIsFunction() {
        Expression exp = new Expression("test");
        assertTrue(exp.isFunction("and"));
    }
    
    @Test
    public void testPrepare() {
        Expression exp = new Expression("test");
        exp.prepare();
        assertTrue(exp.prepared);
    }
    
    @Test
    public void testEvaluate() {
        Expression exp = new Expression("test");
        assertThrows(Exception.class, () -> exp.evaluate());
    }
}