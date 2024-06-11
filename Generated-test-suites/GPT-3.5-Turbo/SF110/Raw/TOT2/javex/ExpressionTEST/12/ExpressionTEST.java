import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class ExpressionTest {
    
    @Test
    public void testGetExternalFunctions() {
        Expression expr = new Expression("test");
        List<String> externalFunctions = new ArrayList<>();
        externalFunctions.add("function1");
        externalFunctions.add("function2");
        expr.externalFunctions = externalFunctions;
        
        assertEquals(externalFunctions, expr.getExternalFunctions());
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expr = new Expression("test");
        
        assertTrue(expr.isReservedFunction("and"));
        assertFalse(expr.isReservedFunction("randomFunction"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expr = new Expression("test");
        
        assertTrue(expr.isLogicalFunction("and"));
        assertFalse(expr.isLogicalFunction("substring"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expr = new Expression("test");
        
        assertTrue(expr.isMathFunction("power"));
        assertFalse(expr.isMathFunction("contains"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expr = new Expression("test");
        
        assertTrue(expr.isDateFunction("dateAdd"));
        assertFalse(expr.isDateFunction("substring"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expr = new Expression("test");
        
        assertTrue(expr.isStringFunction("length"));
        assertFalse(expr.isStringFunction("equals"));
    }
    
    // Add more test cases for other methods
    
}