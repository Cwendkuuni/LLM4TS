
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new ExpressionImpl(), new ExpressionImpl());

        EvalContext context = new EvalContext();

        // Test positive case
        context.setNodeSet(new Object[] { 5, 10 });
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        // Test negative case
        context.setNodeSet(new Object[] { 10, 5 });
        result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
}
