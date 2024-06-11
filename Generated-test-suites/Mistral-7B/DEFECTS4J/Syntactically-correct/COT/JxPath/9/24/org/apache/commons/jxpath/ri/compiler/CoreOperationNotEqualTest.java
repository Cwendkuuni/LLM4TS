import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationCompare;
import org.apache.commons.jxpath.ri.core.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_differentObjects() {
        Expression arg1 = new CoreConstantExpression(new Integer(1));
        Expression arg2 = new CoreConstantExpression(new Integer(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_sameObjects() {
        Expression arg1 = new CoreConstantExpression(new Integer(1));
        Expression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValue_typeMismatch() {
        Expression arg1 = new CoreConstantExpression(new Integer(1));
        Expression arg2 = new CoreStringConstantExpression("true");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreConstantExpression(new Integer(1)), new CoreConstantExpression(new Integer(2)));
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }
}