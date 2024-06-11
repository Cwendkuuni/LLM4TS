import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(10);
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new MockEvalContext();
        
        Object result = coreOperation.computeValue(context);
        
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(10);
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        String symbol = coreOperation.getSymbol();
        
        assertEquals("<=", symbol);
    }

    @Test
    public void testComputeValueEqualValues() {
        Expression arg1 = new MockExpression(15);
        Expression arg2 = new MockExpression(15);
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new MockEvalContext();
        
        Object result = coreOperation.computeValue(context);
        
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueNotLessThanOrEqual() {
        Expression arg1 = new MockExpression(20);
        Expression arg2 = new MockExpression(10);
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new MockEvalContext();
        
        Object result = coreOperation.computeValue(context);
        
        assertFalse((Boolean) result);
    }

}

class MockExpression implements Expression {

    private Object value;

    public MockExpression(Object value) {
        this.value = value;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return value;
    }

}

class MockEvalContext implements EvalContext {

    @Override
    public Object getSingleNode() {
        return null;
    }

    @Override
    public Object getContextNodePointer() {
        return null;
    }

    @Override
    public Object getValuePointer() {
        return null;
    }

    @Override
    public Object getValue(String xpath) {
        return null;
    }

}