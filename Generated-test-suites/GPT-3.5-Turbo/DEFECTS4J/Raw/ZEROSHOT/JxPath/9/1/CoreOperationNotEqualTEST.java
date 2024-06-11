import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual operation;

    @Before
    public void setUp() {
        operation = new CoreOperationNotEqual(new Expression(), new Expression());
    }

    @Test
    public void testComputeValue_notEqual_returnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("variable", "value");

        Expression arg1 = new Expression();
        arg1.setVariable("variable");

        Expression arg2 = new Expression();
        arg2.setConstant("differentValue");

        operation = new CoreOperationNotEqual(arg1, arg2);

        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_equal_returnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("variable", "value");

        Expression arg1 = new Expression();
        arg1.setVariable("variable");

        Expression arg2 = new Expression();
        arg2.setConstant("value");

        operation = new CoreOperationNotEqual(arg1, arg2);

        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol_returnsNotEqualSymbol() {
        assertEquals("!=", operation.getSymbol());
    }
}