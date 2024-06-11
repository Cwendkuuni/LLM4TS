
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.*;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(3);
        
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(3);
        
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">", symbol);
    }
}

class MockExpression extends Expression {
    private Object value;
    
    public MockExpression(Object value) {
        this.value = value;
    }
    
    public Object computeValue(EvalContext context) {
        return value;
    }
}
