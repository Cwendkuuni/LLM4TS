import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operationGreaterThan;

    @Before
    public void setup() {
        operationGreaterThan = new CoreOperationGreaterThan(new ExpressionStub(), new ExpressionStub());
    }

    // Test computeValue method

    @Test
    public void testComputeValueShouldReturnTrueWhenLeftValueIsGreaterThanRightValue() {
        EvalContext context = new EvalContextStub();
        ExpressionStub arg1 = new ExpressionStub();
        ExpressionStub arg2 = new ExpressionStub();
        arg1.setValue(10);
        arg2.setValue(5);
        operationGreaterThan.setArgs(new Expression[] {arg1, arg2});

        Object result = operationGreaterThan.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueShouldReturnFalseWhenLeftValueIsEqualOrLessThanRightValue() {
        EvalContext context = new EvalContextStub();
        ExpressionStub arg1 = new ExpressionStub();
        ExpressionStub arg2 = new ExpressionStub();
        arg1.setValue(5);
        arg2.setValue(10);
        operationGreaterThan.setArgs(new Expression[] {arg1, arg2});

        Object result = operationGreaterThan.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    // Test getSymbol method

    @Test
    public void testGetSymbolShouldReturnGreaterThanSymbol() {
        String symbol = operationGreaterThan.getSymbol();

        assertEquals(">", symbol);
    }

    // Test additional scenarios

    @Test(expected = NullPointerException.class)
    public void testComputeValueShouldThrowNullPointerExceptionWhenContextIsNull() {
        operationGreaterThan.computeValue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueShouldThrowNullPointerExceptionWhenArg1IsNull() {
        operationGreaterThan.setArgs(new Expression[] {null, new ExpressionStub()});
        operationGreaterThan.computeValue(new EvalContextStub());
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueShouldThrowNullPointerExceptionWhenArg2IsNull() {
        operationGreaterThan.setArgs(new Expression[] {new ExpressionStub(), null});
        operationGreaterThan.computeValue(new EvalContextStub());
    }

}