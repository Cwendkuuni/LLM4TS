import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual tester;
    
    @Before
    public void setUp() {
        tester = new CoreOperationLessThanOrEqual(null, null);
    }
    
    // Test case for constructor
    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        tester = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertArrayEquals(new Expression[] {arg1, arg2}, tester.getArgs());
    }
    
    // Test case for computeValue() method - Typical use case
    @Test
    public void testComputeValue_Typical() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", 5);
        context.setVariable("var2", 10);
        
        tester = new CoreOperationLessThanOrEqual(new Expression("$var1"), new Expression("$var2"));
        
        assertEquals(Boolean.TRUE, tester.computeValue(context));
    }
    
    // Test case for computeValue() method - Edge case
    @Test
    public void testComputeValue_Edge() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", -10);
        context.setVariable("var2", -10);
        
        tester = new CoreOperationLessThanOrEqual(new Expression("$var1"), new Expression("$var2"));
        
        assertEquals(Boolean.TRUE, tester.computeValue(context));
    }
    
    // Test case for computeValue() method - Error scenario
    @Test(expected = NullPointerException.class)
    public void testComputeValue_Error() {
        tester.computeValue(null);
    }
    
    // Test case for getSymbol() method
    @Test
    public void testGetSymbol() {
        assertEquals("<=", tester.getSymbol());
    }
}