import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual operation;

    @Test
    public void testComputeValue_equalValues() {
        operation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(5));
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_notEqualValues() {
        operation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(10));
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetSymbol() {
        operation = new CoreOperationNotEqual(new MockExpression(5), new MockExpression(5));
        
        String symbol = operation.getSymbol();
        
        Assert.assertEquals("!=", symbol);
    }
}

class MockExpression extends Expression {

    private Object value;
    
    public MockExpression(Object value) {
        this.value = value;
    }
    
    @Override
    public Object computeValue(EvalContext context) {
        return value;
    }
}