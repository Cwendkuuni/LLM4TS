import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual operation;

    @Before
    public void setup() {
        Expression arg1 = new ExpressionTest();
        Expression arg2 = new ExpressionTest();
        operation = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void computeValue_NotEqual_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Object arg1 = "test1";
        Object arg2 = "test2";
        context.addVariable("arg1", arg1);
        context.addVariable("arg2", arg2);

        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void computeValue_Equal_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Object arg1 = "test";
        Object arg2 = "test";
        context.addVariable("arg1", arg1);
        context.addVariable("arg2", arg2);

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test(expected = NullPointerException.class)
    public void computeValue_Arg1Null_ThrowsNullPointerException() {
        EvalContext context = new EvalContext();
        Object arg2 = "test";
        context.addVariable("arg2", arg2);

        operation.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void computeValue_Arg2Null_ThrowsNullPointerException() {
        EvalContext context = new EvalContext();
        Object arg1 = "test";
        context.addVariable("arg1", arg1);

        operation.computeValue(context);
    }

    @Test
    public void getSymbol_ReturnsNotEqualSymbol() {
        String symbol = operation.getSymbol();

        assertEquals("!=", symbol);
    }

    private class ExpressionTest extends Expression {

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}