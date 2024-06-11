import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValueEquals() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object value1 = new Object();
        Object value2 = value1;
        context.setNodeSet(new Object[] { value1, value2 });
        
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValueNotEquals() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object value1 = new Object();
        Object value2 = new Object();
        context.setNodeSet(new Object[] { value1, value2 });
        
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }
}