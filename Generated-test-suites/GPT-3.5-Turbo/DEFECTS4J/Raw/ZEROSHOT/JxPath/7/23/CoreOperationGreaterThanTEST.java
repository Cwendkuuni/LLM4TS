import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        context.setContextNode(new Node());
        context.setContextPosition(1);
        context.setContextSize(2);
        
        double l = InfoSetUtil.doubleValue(2.5);
        double r = InfoSetUtil.doubleValue(1.5);
        Object expected = Boolean.TRUE;
        Object result = op.computeValue(context);
        assertEquals(expected, result);
        
        l = InfoSetUtil.doubleValue(1.5);
        r = InfoSetUtil.doubleValue(2.5);
        expected = Boolean.FALSE;
        result = op.computeValue(context);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        String expected = ">";
        String result = op.getSymbol();
        assertEquals(expected, result);
    }
}