
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue_notEqual_returnsTrue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        EvalContext context = null;
        
        Object result = operation.computeValue(context);
        
        Assert.assertEquals(true, result);
    }
    
    @Test
    public void testGetSymbol_returnsNotEqualSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        
        String symbol = operation.getSymbol();
        
        Assert.assertEquals("!=", symbol);
    }
}
