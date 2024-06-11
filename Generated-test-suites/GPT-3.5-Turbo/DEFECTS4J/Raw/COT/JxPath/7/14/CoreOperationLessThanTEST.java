import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Test case 1: arg1 < arg2
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        arg1.setValue(5);
        arg2.setValue(10);
        assertEquals(Boolean.TRUE, lessThan.computeValue(context));

        // Test case 2: arg1 >= arg2
        context = new EvalContext();
        arg1.setValue(10);
        arg2.setValue(5);
        assertEquals(Boolean.FALSE, lessThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        assertEquals("<", lessThan.getSymbol());
    }

    @Test
    public void testNullArguments() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        assertNull(lessThan.computeValue(context));
    }

    @Test
    public void testArgument1Null() {
        Expression arg1 = null;
        Expression arg2 = new Expression();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        assertNull(lessThan.computeValue(context));
    }

    @Test
    public void testArgument2Null() {
        Expression arg1 = new Expression();
        Expression arg2 = null;
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        assertNull(lessThan.computeValue(context));
    }

    @Test
    public void testSameObject() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg1);

        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, lessThan.computeValue(context));
    }

    @Test(expected = Exception.class)
    public void testComputeValueException() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                throw new Exception("Exception");
            }
        };
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        lessThan.computeValue(context);
    }
}