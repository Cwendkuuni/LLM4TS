import org.apache.commons.jxpath.ri.Compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    
    private CoreOperationGreaterThan operation;
    private EvalContext context;
    
    @Before
    public void setup() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
    }
    
    @Test
    public void testComputeValue_returnsTrue_whenArg1IsGreaterThanArg2() {
        context.set(arg1, "5");
        context.set(arg2, "3");
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValue_returnsFalse_whenArg1IsLessThanArg2() {
        context.set(arg1, "3");
        context.set(arg2, "5");
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValue_returnsFalse_whenArg1IsEqualToArg2() {
        context.set(arg1, "5");
        context.set(arg2, "5");
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol_returnsGreaterThanSymbol() {
        assertEquals(">", operation.getSymbol());
    }
    
    @Test
    public void testGetSymbol_notEqualsToLessThanSymbol() {
        assertNotEquals("<", operation.getSymbol());
    }
    
    @Test
    public void testGetSymbol_notEqualsToEqualToSymbol() {
        assertNotEquals("==", operation.getSymbol());
    }
}