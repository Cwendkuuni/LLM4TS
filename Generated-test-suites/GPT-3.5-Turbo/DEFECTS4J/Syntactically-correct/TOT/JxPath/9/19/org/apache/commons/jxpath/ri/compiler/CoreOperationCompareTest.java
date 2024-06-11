import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private CoreOperationCompare operation;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = new ExpressionStub();
        arg2 = new ExpressionStub();
        operation = new CoreOperationCompare(arg1, arg2);
        context = new EvalContext();
    }

    @Test
    public void testEqual_BothNull_ReturnsTrue() {
        Object result = operation.equal(context, arg1, arg2);
        assertNull(arg1.computeCalledWith); // verify that arg1.compute() was called
        assertNull(arg2.computeCalledWith); // verify that arg2.compute() was called
        assertTrue((boolean) result);
    }

    @Test
    public void testEqual_BothNonNullEqual_ReturnsTrue() {
        arg1.computeResult = "value";
        arg2.computeResult = "value";

        Object result = operation.equal(context, arg1, arg2);

        assertEquals("value", arg1.computeCalledWith); // verify that arg1.compute() was called
        assertEquals("value", arg2.computeCalledWith); // verify that arg2.compute() was called
        assertTrue((boolean) result);
    }

    @Test
    public void testEqual_BothNonNullNotEqual_ReturnsFalse() {
        arg1.computeResult = "value1";
        arg2.computeResult = "value2";

        Object result = operation.equal(context, arg1, arg2);

        assertEquals("value1", arg1.computeCalledWith); // verify that arg1.compute() was called
        assertEquals("value2", arg2.computeCalledWith); // verify that arg2.compute() was called
        assertFalse((boolean) result);
    }

    @Test
    public void testEqual_LeftNull_ReturnsFalse() {
        arg2.computeResult = "value";

        Object result = operation.equal(context, arg1, arg2);

        assertNull(arg1.computeCalledWith); // verify that arg1.compute() was not called
        assertEquals("value", arg2.computeCalledWith); // verify that arg2.compute() was called
        assertFalse((boolean) result);
    }

    @Test
    public void testEqual_RightNull_ReturnsFalse() {
        arg1.computeResult = "value";

        Object result = operation.equal(context, arg1, arg2);

        assertEquals("value", arg1.computeCalledWith); // verify that arg1.compute() was called
        assertNull(arg2.computeCalledWith); // verify that arg2.compute() was not called
        assertFalse((boolean) result);
    }

    // Add more test cases for other methods

}

class ExpressionStub extends Expression {
    public Object computeCalledWith;
    public Object computeResult;

    @Override
    public Object compute(EvalContext context) {
        computeCalledWith = context;
        return computeResult;
    }
}