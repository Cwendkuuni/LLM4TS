import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testConstructor() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNotNull(coreOperationNotEqual);
    }

    @Test
    public void testComputeValue_NotEqual() {
        EvalContext context = new EvalContext();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_Equal() {
        EvalContext context = new EvalContext();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        context.getExpressionContext().getContext().put("var1", "value");
        
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }
}