import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {
    private CoreOperationLessThanOrEqual operation;

    @Before
    public void setUp() {
        operation = new CoreOperationLessThanOrEqual(new Expression() {

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public String getSymbol() {
                return "";
            }
        }, new Expression() {

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public String getSymbol() {
                return "";
            }
        });
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        Assert.assertEquals("<=", symbol);
    }
}