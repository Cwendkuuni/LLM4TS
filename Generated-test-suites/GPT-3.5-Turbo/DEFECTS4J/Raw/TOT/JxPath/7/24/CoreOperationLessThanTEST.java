import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan operation;

    @Before
    public void setUp() {
        operation = new CoreOperationLessThan(new MockExpression(2), new MockExpression(3));
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new MockEvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("<", operation.getSymbol());
    }

    @Test
    public void testComputeValue_withFloatingPointNumbers() {
        operation = new CoreOperationLessThan(new MockExpression(2.5), new MockExpression(3.5));
        EvalContext context = new MockEvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_withNegativeNumbers() {
        operation = new CoreOperationLessThan(new MockExpression(-2), new MockExpression(-1));
        EvalContext context = new MockEvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_withZeroValue() {
        operation = new CoreOperationLessThan(new MockExpression(0), new MockExpression(1));
        EvalContext context = new MockEvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_withEqualNumbers() {
        operation = new CoreOperationLessThan(new MockExpression(2), new MockExpression(2));
        EvalContext context = new MockEvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_withFirstArgumentLarger() {
        operation = new CoreOperationLessThan(new MockExpression(3), new MockExpression(2));
        EvalContext context = new MockEvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_withInvalidContext() {
        operation = new CoreOperationLessThan(new MockExpression(2), new MockExpression(3));
        EvalContext context = new MockEvalContext();
        context.setErrorState(true);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_withOneInvalidArgument() {
        operation = new CoreOperationLessThan(new MockExpression(2), new MockExpression("invalid"));
        EvalContext context = new MockEvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
}