import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        Expression arg1 = new ExpressionImpl();
        Expression arg2 = new ExpressionImpl();
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueWithEqualArgs() {
        EvalContext context = new EvalContextImpl();
        context.setVariable("arg1", "value");
        context.setVariable("arg2", "value");
        
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValueWithNotEqualArgs() {
        EvalContext context = new EvalContextImpl();
        context.setVariable("arg1", "value1");
        context.setVariable("arg2", "value2");
        
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValueWithNullArgs() {
        EvalContext context = new EvalContextImpl();
        context.setVariable("arg1", null);
        context.setVariable("arg2", null);
        
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValueWithOneNullArg() {
        EvalContext context = new EvalContextImpl();
        context.setVariable("arg1", "value1");
        context.setVariable("arg2", null);
        
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        EvalContext context = new EvalContextImpl();
        context.setVariable("arg1", "5");
        context.setVariable("arg2", 5);
        
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testComputeValueWithEmptyString() {
        EvalContext context = new EvalContextImpl();
        context.setVariable("arg1", "");
        context.setVariable("arg2", "");
        
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}