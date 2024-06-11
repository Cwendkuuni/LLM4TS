import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    private CoreOperationGreaterThanOrEqual coreOp;
    
    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }
    
    @Test
    public void testComputeValueGreaterThanOrEqualTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 3);
        Object result = coreOp.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValueGreaterThanOrEqualFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 3);
        context.setVariable("arg2", 5);
        Object result = coreOp.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = coreOp.getSymbol();
        assertEquals(">=", symbol);
    }
    
    @After
    public void tearDown() {
        coreOp = null;
    }
}