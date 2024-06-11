
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_greaterThan() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(6), new Constant(5));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValue_lessThan() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(6));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValue_equal() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValue_notNumber() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant("5"), new Constant(5));
        EvalContext context = new EvalContext();
        try {
            operation.computeValue(context);
            fail("Expected ClassCastException");
        } catch (ClassCastException ex) {
            // Expected
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(6));
        assertEquals(">=", operation.getSymbol());
    }

}
