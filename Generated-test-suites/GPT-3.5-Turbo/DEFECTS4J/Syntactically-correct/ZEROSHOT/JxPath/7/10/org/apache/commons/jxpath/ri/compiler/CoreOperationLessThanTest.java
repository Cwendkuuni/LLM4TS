import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan operation;

    @Before
    public void setup() {
        operation = new CoreOperationLessThan(new Expression(), new Expression());
    }

    @Test
    public void testComputeValue_lessThan_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        double l = 2.5;
        double r = 3.5;

        operation.args[0] = l;
        operation.args[1] = r;

        Object result = operation.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_notLessThan_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        double l = 3.5;
        double r = 2.5;

        operation.args[0] = l;
        operation.args[1] = r;

        Object result = operation.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_returnsLessThanSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}