import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setUp() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5; // Typical use case
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10; // Typical use case
            }
        };

        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        assertTrue((Boolean) operation.computeValue(null)); // Typical use case

        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10; // Typical use case
            }
        };

        operation.getArguments()[0] = arg1;

        assertFalse((Boolean) operation.computeValue(null)); // Typical use case

        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5; // Typical use case
            }
        };

        operation.getArguments()[1] = arg2;

        assertTrue((Boolean) operation.computeValue(null)); // Typical use case

        // Edge case where args[0] and args[1] are equal
        Expression arg3 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5; // Edge case
            }
        };
        
        operation.getArguments()[0] = arg3;
        operation.getArguments()[1] = arg3;
        
        assertTrue((Boolean) operation.computeValue(null)); // Edge case

        // Edge case where args[0] is greater than args[1]
        Expression arg4 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 15; // Edge case
            }
        };

        operation.getArguments()[0] = arg4;

        assertFalse((Boolean) operation.computeValue(null)); // Edge case
    }

    @Test
    public void testGetSymbol() {
        assertEquals(">=", operation.getSymbol()); // Typical use case
    }
}