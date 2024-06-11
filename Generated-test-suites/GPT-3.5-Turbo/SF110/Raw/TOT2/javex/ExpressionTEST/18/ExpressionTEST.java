import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression exp = new Expression("test expression");
        assertNotNull(exp.getExternalFunctions());
    }
    
    @Test
    public void testExpressionConstructor() {
        Expression exp = new Expression("test expression");
        assertNotNull(exp);
        assertEquals("test expression", exp.expression);
        assertNotNull(exp.stack);
        assertNotNull(exp.getExternalFunctions());
        assertFalse(exp.prepared);
    }
    
    // Add more test cases for other methods in the Expression class
    
}