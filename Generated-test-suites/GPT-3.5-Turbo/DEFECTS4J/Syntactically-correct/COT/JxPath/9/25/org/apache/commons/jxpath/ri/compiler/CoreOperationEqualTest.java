import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        
        // Test when both arguments are null
        assertNull(operation.computeValue(null));
        
        // Test when both arguments are equal
        Expression arg1 = new CoreValue("test");
        Expression arg2 = new CoreValue("test");
        assertTrue((Boolean) operation.computeValue(null));
        
        // Test when both arguments are not equal
        Expression arg1 = new CoreValue("test1");
        Expression arg2 = new CoreValue("test2");
        assertFalse((Boolean) operation.computeValue(null));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(null, null);
        assertEquals("=", operation.getSymbol());
    }
}