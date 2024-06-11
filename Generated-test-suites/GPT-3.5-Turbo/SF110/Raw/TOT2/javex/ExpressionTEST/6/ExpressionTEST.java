import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class ExpressionTest {
    
    private Expression testExpression;
    
    @Before
    public void setUp() {
        testExpression = new Expression("2+3*(4-1)");
    }
    
    @Test
    public void testGetExternalFunctions() {
        assertNotNull(testExpression.getExternalFunctions());
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(testExpression);
    }
    
    @Test
    public void testDoEvaluationStack() {
        testExpression.prepare();
        // Add relevant assertions here after analyzing the method
    }
    
    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("abs"));
        assertNull(Expression.getReservedFunctions("invalid"));
    }
    
    @Test
    public void testIsReservedFunction() {
        assertTrue(testExpression.isReservedFunction("abs"));
        assertFalse(testExpression.isReservedFunction("invalid"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        assertTrue(testExpression.isLogicalFunction("and"));
        assertFalse(testExpression.isLogicalFunction("abs"));
    }
    
    @Test
    public void testIsMathFunction() {
        assertTrue(testExpression.isMathFunction("sqrt"));
        assertFalse(testExpression.isMathFunction("and"));
    }
    
    @Test
    public void testIsDateFunction() {
        assertTrue(testExpression.isDateFunction("dateadd"));
        assertFalse(testExpression.isDateFunction("and"));
    }
    
    @Test
    public void testIsStringFunction() {
        assertTrue(testExpression.isStringFunction("startswith"));
        assertFalse(testExpression.isStringFunction("abs"));
    }
    
    @Test
    public void testGetExternalFunction() {
        assertNotNull(testExpression.getExternalFunction("endswith"));
        assertNull(testExpression.getExternalFunction("invalid"));
    }
    
    @Test
    public void testIsExternalFunction() {
        assertTrue(testExpression.isExternalFunction("endswith"));
        assertFalse(testExpression.isExternalFunction("invalid"));
    }
    
    @Test
    public void testGetFunction() {
        assertNotNull(testExpression.getFunction("abs"));
        assertNull(testExpression.getFunction("invalid"));
    }
    
    @Test
    public void testIsFunction() {
        assertTrue(testExpression.isFunction("abs"));
        assertFalse(testExpression.isFunction("invalid"));
    }
    
    @Test
    public void testPrepare() {
        testExpression.prepare();
        // Add relevant assertions here after analyzing the method
    }
    
    @Test
    public void testEvaluate() throws Exception {
        assertNotNull(testExpression.evaluate());
    }
    
    @Test
    public void testStack() {
        Stack stack = new Stack();
        testExpression.stack("test", stack);
        assertEquals("test", stack.pop());
    }
    
    @Test
    public void testGetStackCopy() {
        assertNotNull(testExpression.getStackCopy());
    }
    
    // Add more test cases for the remaining methods
    
}