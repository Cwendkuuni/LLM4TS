import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual coreOp = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        // Test when arg1 is less than arg2
        arg1.setValue(10);
        arg2.setValue(20);
        Assert.assertEquals(Boolean.TRUE, coreOp.computeValue(context));
        
        // Test when arg1 is equal to arg2
        arg1.setValue(10);
        arg2.setValue(10);
        Assert.assertEquals(Boolean.TRUE, coreOp.computeValue(context));
        
        // Test when arg1 is greater than arg2
        arg1.setValue(20);
        arg2.setValue(10);
        Assert.assertEquals(Boolean.FALSE, coreOp.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual coreOp = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        Assert.assertEquals("<=", coreOp.getSymbol());
    }
}

class Expression {
    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object computeValue(EvalContext context) {
        return value;
    }
}