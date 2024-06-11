import org.junit.*;
import static org.junit.Assert.*;

public class ExpressionTest {
    
    // Test case for constructor
    @Test
    public void testConstructor() {
        Expression expression = new Expression("2+3");
        assertNotNull(expression);
    }
    
    // Test case for getExternalFunctions method
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression();
        assertNotNull(expression.getExternalFunctions());
    }
    
    // Test case for doEvaluationStack method
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("(2+3)*4");
        expression.prepare();
        expression.doEvaluationStack();
        // Asserts for evaluation stack
        
    }
    
    // Test case for getReservedFunctions method
    @Test
    public void testGetReservedFunctions() {
        Expression expression = new Expression();
        assertNotNull(expression.getReservedFunctions("and"));
    }
    
    // Test case for isReservedFunction method
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isReservedFunction("and"));
    }
    
    // Test case for isLogicalFunction method
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("startsWith"));
    }
    
    // Test case for isMathFunction method
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isMathFunction("abs"));
        assertFalse(expression.isMathFunction("startsWith"));
    }
    
    // Test case for isDateFunction method
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("startsWith"));
    }
    
    // Test case for isStringFunction method
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isStringFunction("substring"));
        assertFalse(expression.isStringFunction("startsWith"));
    }
    
    // Test case for getExternalFunction method
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression();
        assertNotNull(expression.getExternalFunction("substring"));
    }
    
    // Test case for isExternalFunction method
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isExternalFunction("substring"));
        assertFalse(expression.isExternalFunction("startsWith"));
    }
    
    // Test case for getFunction method
    @Test
    public void testGetFunction() {
        Expression expression = new Expression();
        assertNotNull(expression.getFunction("substring"));
    }
    
    // Test case for isFunction method
    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isFunction("substring"));
        assertFalse(expression.isFunction("startsWith"));
    }
    
    // Test case for prepare method
    @Test
    public void testPrepare() {
        Expression expression = new Expression("2+3");
        expression.prepare();
        assertTrue(expression.isPrepared());
    }
    
    // Test case for getStackCopy method
    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("2+3");
        expression.prepare();
        assertNotNull(expression.getStackCopy());
    }
    
    // Test case for evaluate method
    @Test
    public void testEvaluate() {
        Expression expression = new Expression("2+3");
        expression.prepare();
        try {
            assertNotNull(expression.evaluate());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }
    
    // Test case for tokenize method
    @Test
    public void testTokenize() {
        Expression expression = new Expression("2 + 3");
        expression.tokenize();
        // Asserts for tokenized stack
    }
    
    // Test case for complete flow
    @Test
    public void testCompleteFlow() {
        Expression expression = new Expression("(2+3)*4");
        expression.prepare();
        try {
            assertNotNull(expression.evaluate());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }
    
}