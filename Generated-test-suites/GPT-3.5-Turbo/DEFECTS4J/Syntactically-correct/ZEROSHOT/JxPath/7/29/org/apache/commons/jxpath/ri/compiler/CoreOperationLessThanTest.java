import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan operation;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testComputeValueLessThan() {
        EvalContext context = new EvalContext();
        context.setValue(1, "a");
        context.setValue(2, "b");
        Object result = operation.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValueGreaterThan() {
        EvalContext context = new EvalContext();
        context.setValue(1, "b");
        context.setValue(2, "a");
        Object result = operation.computeValue(context);
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValueEqual() {
        EvalContext context = new EvalContext();
        context.setValue(1, "a");
        context.setValue(2, "a");
        Object result = operation.computeValue(context);
        assertFalse((boolean) result);
    }
}