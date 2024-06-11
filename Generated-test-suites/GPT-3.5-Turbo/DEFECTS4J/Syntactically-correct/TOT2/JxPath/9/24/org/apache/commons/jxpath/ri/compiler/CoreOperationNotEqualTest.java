import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_notEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(5), new Expression(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_equal() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(5), new Expression(5));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(5), new Expression(10));
        assertEquals("!=", operation.getSymbol());
    }
}