import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operation;

    private EvalContext context;

    @Before
    public void setUp() {
        operation = new CoreOperationGreaterThan(new Expression() {
             public Object computeValue(EvalContext context) {
                 return 5;
             }
        }, new Expression() {
             public Object computeValue(EvalContext context) {
                 return 3;
             }
        });
        context = new EvalContext();
    }

    @Test
    public void testComputeValue() {
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }

    @Test
    public void testComputeValueWithNegativeNumbers() {
        operation = new CoreOperationGreaterThan(new Expression() {
             public Object computeValue(EvalContext context) {
                 return -5;
             }
        }, new Expression() {
             public Object computeValue(EvalContext context) {
                 return -3;
             }
        });

        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

}