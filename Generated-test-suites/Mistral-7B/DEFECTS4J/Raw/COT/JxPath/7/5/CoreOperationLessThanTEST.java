import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CoreOperationLessThanTest {

    private CoreOperationLessThan operation;
    private EvalContext context;
    private Expression arg1;
    private Expression arg2;
    private Object expectedResult;

    public CoreOperationLessThanTest(Expression arg1, Expression arg2, Object expectedResult) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters(name = "{index}: test({0}, {1}) = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new LiteralExpression(4.0), new LiteralExpression(5.0), Boolean.FALSE},
                {new LiteralExpression(5.0), new LiteralExpression(4.0), Boolean.TRUE},
                {new LiteralExpression(4.0), new LiteralExpression(4.0), Boolean.FALSE},
                {new LiteralExpression(Double.NaN), new LiteralExpression(Double.NaN), Boolean.FALSE},
                {new VariableExpression("x"), new LiteralExpression(5.0), new Expression[]{new VariableExpression("x")}, Boolean.FALSE},
                {new LiteralExpression(5.0), new VariableExpression("x"), new Expression[]{new VariableExpression("x")}, Boolean.TRUE},
                {new VariableExpression("x"), new VariableExpression("x"), new Expression[]{new VariableExpression("x")}, Boolean.FALSE},
                {new LiteralExpression(4.0), new LiteralExpression(null), Boolean.TRUE},
                {new LiteralExpression(null), new LiteralExpression(4.0), Boolean.FALSE},
                {new LiteralExpression(null), new LiteralExpression(null), null}
        });
    }

    @Before
    public void setup() {
        context = new EvalContext();
        operation = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        Object result = operation.computeValue(context);
        if (expectedResult instanceof Boolean) {
            assertEquals("Expected: " + expectedResult + ", but was: " + result, expectedResult, result);
        } else {
            assertNotNull("Expected non-null value: " + expectedResult, result);
            if (expectedResult instanceof InfoSet) {
                assertNull("Expected null InfoSet, but got: " + result, ((InfoSet) result).getDocument());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new CoreOperationLessThan(null, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor2() {
        new CoreOperationLessThan(arg1, null);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertTrue("Expected symbol '<'", symbol.equals("<"));
    }
}