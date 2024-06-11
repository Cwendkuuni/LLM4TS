import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        
        // Typical use case
        assertEquals(Boolean.TRUE, lessThan.computeValue(new EvalContext()));
        
        // Edge case
        assertEquals(Boolean.FALSE, lessThan.computeValue(new EvalContext()));
        
        // Error scenario
        assertEquals(null, lessThan.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(), new Expression());
        
        // Typical use case
        assertEquals("<", lessThan.getSymbol());
        
        // Edge case
        assertEquals(null, lessThan.getSymbol());
        
        // Error scenario
        assertEquals(">", lessThan.getSymbol());
    }
}