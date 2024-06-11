import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual operationEqual;

    @Before
    public void setup() {
        operationEqual = new CoreOperationEqual(new ExpressionMock(), new ExpressionMock());
    }

    @Test
    public void testComputeValue_equalArguments_returnsTrue() {
        EvalContext context = new EvalContext();
        Object result = operationEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_notEqualArguments_returnsFalse() {
        EvalContext context = new EvalContext();
        operationEqual = new CoreOperationEqual(new ExpressionMock("test1"), new ExpressionMock("test2"));
        Object result = operationEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol_returnsEqualSymbol() {
        String symbol = operationEqual.getSymbol();
        assertEquals("=", symbol);
    }

    private class ExpressionMock extends Expression {

        private String value;

        public ExpressionMock() {
            this.value = "test";
        }

        public ExpressionMock(String value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}