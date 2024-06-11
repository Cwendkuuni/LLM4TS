
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue_typicalCase() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression("hello"), new Expression("hello"));
        EvalContext context = new EvalContext();
        assertTrue(equal.computeValue(context));
    }
    
    @Test
    public void testComputeValue_differentValues() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression("hello"), new Expression("world"));
        EvalContext context = new EvalContext();
        assertFalse(equal.computeValue(context));
    }
    
    @Test
    public void testComputeValue_nullValues() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression(null), new Expression(null));
        EvalContext context = new EvalContext();
        assertTrue(equal.computeValue(context));
    }
    
    @Test
    public void testComputeValue_typicalCaseWithNumbers() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression(5), new Expression(5));
        EvalContext context = new EvalContext();
        assertTrue(equal.computeValue(context));
    }
    
    @Test
    public void testComputeValue_edgeCaseWithNumbers() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression(0), new Expression(-0));
        EvalContext context = new EvalContext();
        assertTrue(equal.computeValue(context));
    }
    
    @Test
    public void testComputeValue_typicalCaseWithBoolean() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression(true), new Expression(true));
        EvalContext context = new EvalContext();
        assertTrue(equal.computeValue(context));
    }
    
    @Test
    public void testComputeValue_edgeCaseWithBoolean() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression(false), new Expression(false));
        EvalContext context = new EvalContext();
        assertTrue(equal.computeValue(context));
    }
    
    @Test
    public void testComputeValue_typicalCaseWithObjects() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression(new Object()), new Expression(new Object()));
        EvalContext context = new EvalContext();
        assertFalse(equal.computeValue(context));
    }
    
    @Test
    public void testComputeValue_nullObject() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression(null), new Expression(new Object()));
        EvalContext context = new EvalContext();
        assertFalse(equal.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual equal = new CoreOperationEqual(new Expression("hello"), new Expression("world"));
        assertEquals("=", equal.getSymbol());
    }
}
