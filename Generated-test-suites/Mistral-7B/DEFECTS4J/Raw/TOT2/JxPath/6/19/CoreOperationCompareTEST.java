import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_TwoNullValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreOperation(new ConstantExpression(null)),
            new CoreOperation(new ConstantExpression(null))
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));
    }

    @Test
    public void testEqual_TwoBooleanValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreOperation(new ConstantExpression(true)),
            new CoreOperation(new ConstantExpression(true))
        );
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        coreOperationCompare = new CoreOperationCompare(
            new CoreOperation(new ConstantExpression(false)),
            new CoreOperation(new ConstantExpression(false))
        );
        assertTrue(coreOperationCompare.evaluateToBoolean(context));

        coreOperationCompare = new CoreOperationCompare(
            new CoreOperation(new ConstantExpression(true)),
            new CoreOperation(new ConstantExpression(false))
        );
        assertFalse(coreOperationCompare.evaluateToBoolean(context));
    }

    // Test other data types and edge cases here...
}