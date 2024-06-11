import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        arg1.setValue(5);
        arg2.setValue(10);
        assertTrue((Boolean)op.computeValue(context));
        
        arg1.setValue(10);
        arg2.setValue(5);
        assertFalse((Boolean)op.computeValue(context));
        
        arg1.setValue(5);
        arg2.setValue(5);
        assertTrue((Boolean)op.computeValue(context));
        
        arg1.setValue(10.5);
        arg2.setValue(10.5);
        assertTrue((Boolean)op.computeValue(context));
        
        arg1.setValue(5);
        arg2.setValue(5.5);
        assertTrue((Boolean)op.computeValue(context));
        
        arg1.setValue(5.5);
        arg2.setValue(5);
        assertFalse((Boolean)op.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertEquals("<=", op.getSymbol());
    }
}