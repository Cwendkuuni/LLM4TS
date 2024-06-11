import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Test case 1: arg1 < arg2
        CoreOperationLessThan obj = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setNodeSet(nodeSet);
        Object result = obj.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test case 2: arg1 >= arg2
        CoreOperationLessThan obj = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setNodeSet(nodeSet);
        Object result = obj.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}