import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, null);
        EvalContext context = new EvalContext();

        // Test when values are equal
        operation.args[0] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        operation.args[1] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        assertTrue((Boolean)operation.computeValue(context));

        // Test when first value is less than second value
        operation.args[0] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        operation.args[1] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        assertTrue((Boolean)operation.computeValue(context));

        // Test when first value is greater than second value
        operation.args[0] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 7;
            }
        };
        operation.args[1] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        assertFalse((Boolean)operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, null);
        assertEquals("<=", operation.getSymbol());
    }
}