import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        
        CoreOperationGreaterThan testObject = new CoreOperationGreaterThan(arg1, arg2);
        
        Object result = testObject.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan testObject = new CoreOperationGreaterThan(null, null);
        
        String symbol = testObject.getSymbol();
        
        assertEquals(">", symbol);
    }
}