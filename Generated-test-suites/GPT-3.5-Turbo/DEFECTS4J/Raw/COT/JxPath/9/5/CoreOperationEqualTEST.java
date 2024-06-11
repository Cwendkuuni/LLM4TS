import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {
   
    @Test
    public void testComputeValue_equal(){
        EvalContext context = new EvalContext();
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression(), new Expression());
        Object result = operationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
   
    @Test
    public void testComputeValue_notEqual(){
        EvalContext context = new EvalContext();
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression(), new Expression());
        Object result = operationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
   
    @Test
    public void testGetSymbol(){
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression(), new Expression());
        String symbol = operationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}