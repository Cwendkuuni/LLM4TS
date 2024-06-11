import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_Boolean_Boolean() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        Boolean expected = Boolean.FALSE;
        assertEquals(expected, result);

        arg1 = new Variable("true");
        arg2 = new Variable("true");
        result = coreOperationNotEqual.computeValue(new EvalContext());
        expected = Boolean.TRUE;
        assertEquals(expected, result);

        arg1 = new Variable("true");
        arg2 = new Variable("false");
        result = coreOperationNotEqual.computeValue(new EvalContext());
        expected = Boolean.FALSE;
        assertEquals(expected, result);

        arg1 = new Variable("false");
        arg2 = new Variable("true");
        result = coreOperationNotEqual.computeValue(new EvalContext());
        expected = Boolean.TRUE;
        assertEquals(expected, result);

        arg1 = new Variable("null");
        arg2 = new Variable("null");
        result = coreOperationNotEqual.computeValue(new EvalContext());
        expected = Boolean.TRUE;
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_IllegalArgumentException() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_IllegalArgumentException_ArgumentCount() {
        Expression arg1 = new Variable("arg1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_IllegalArgumentException_Arg1IsNull() {
        Expression arg1 = null;
        Expression arg2 = new Variable("arg2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_IllegalArgumentException_Arg2IsNull() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }
}