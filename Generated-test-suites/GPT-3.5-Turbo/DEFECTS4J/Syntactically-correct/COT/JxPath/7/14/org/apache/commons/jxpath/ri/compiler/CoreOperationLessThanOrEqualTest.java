import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        
        EvalContext context = new EvalContext();
        context.setDocumentRoot(new Object()); // Set the document root for testing
        
        Double value1 = 10.0;
        Double value2 = 20.0;
        
        // Test when value1 is less than or equal to value2
        context.setNodeSet(new Double[] {value1});
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test when value1 is greater than value2
        context.setNodeSet(new Double[] {value2});
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        assertEquals("<=", operation.getSymbol());
    }
}