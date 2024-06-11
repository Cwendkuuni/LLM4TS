import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoreOperationRelationalExpressionTest {

    @ParameterizedTest
    @MethodSource("dataProviderForTestComputeValue")
    public void testComputeValue(Object left, Object right, Boolean expected) {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(new Integer(5)), new CoreExpressionLiteral(new Integer(3)));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertEquals(expected, result);
        assertEquals(expected, expression.computeValue(context, left, right));
    }

    @ParameterizedTest
    @MethodSource("dataProviderForTestGetPrecedence")
    public void testGetPrecedence(int expected, CoreOperationRelationalExpression expression) {
        assertEquals(expected, expression.getPrecedence());
    }

    @ParameterizedTest
    @MethodSource("dataProviderForTestIsSymmetric")
    public void testIsSymmetric(boolean expected, CoreOperationRelationalExpression expression) {
        assertEquals(expected, expression.isSymmetric());
    }

    @ParameterizedTest
    @MethodSource("dataProviderForTestEvaluateCompare")
    public void testEvaluateCompare(int compare, CoreOperationRelationalExpression expression, Boolean expected) {
        boolean result = expression.evaluateCompare(compare);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("dataProviderForTestCompute")
    public void testCompute(Object left, Object right, Boolean expected) {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(new Integer(5)), new CoreExpressionLiteral(new Integer(3)));
        EvalContext context = new EvalContext();

        Boolean result = (Boolean) expression.compute(context, left, right);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("dataProviderForTestReduce")
    public void testReduce(Object input, Object expected) {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(new Integer(5)), new CoreExpressionLiteral(new Integer(3)));

        Object result = expression.reduce(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("dataProviderForTestFindMatch")
    public void testFindMatch(Iterator<Object> iterator1, Iterator<Object> iterator2, Boolean expected) {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(new Integer(5)), new CoreExpressionLiteral(new Integer(3)));

        Boolean result = expression.findMatch(iterator1, iterator2);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("dataProviderForTestContainsMatch")
    public void testContainsMatch(Iterator<Object> iterator, Object value, Boolean expected) {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(new Integer(5)), new CoreExpressionLiteral(new Integer(3)));

        Boolean result = expression.containsMatch(iterator, value);

        assertEquals(expected, result);
    }

    private static Object[][] dataProviderForTestComputeValue() {
        return new Object[][]{
                {new Integer(5), new Integer(3), Boolean.TRUE},
                {new Integer(5), new Integer(5), Boolean.TRUE},
                {new Integer(5), new Integer(6), Boolean.FALSE},
                {new Integer(5), new Integer(4), Boolean.FALSE},
                {new Integer(5), new Integer(5.0), Boolean.TRUE},
                {new Integer(5), new Double(3.5), Boolean.FALSE},
                {new Integer(5), new Double(5.0), Boolean.TRUE},
                {new Integer(5), new Double(4.5), Boolean.FALSE},
                {new Double(5.5), new Integer(3), Boolean.FALSE},
                {new Double(5.5), new Integer(5), Boolean.TRUE},
                {new Double(5.5), new Integer(6), Boolean.FALSE},
                {new Double(5.5), new Integer(4), Boolean.FALSE},
                {new Double(5.5), new Double(3.5), Boolean.FALSE},
                {new Double(5.5), new Double(5.0), Boolean.TRUE},
                {new Double(5.5), new Double(4.5), Boolean.FALSE}
        };
    }

    private static int[][] dataProviderForTestGetPrecedence() {
        return new int[][]{
                {3, CoreOperationRelationalExpression.class},
                {2, CoreOperationAddSubtract.class},
                {1, CoreOperationMultDiv.class},
                {0, CoreOperationUnary.class}
        };
    }

    private static Boolean[][] dataProviderForTestIsSymmetric() {
        return new Boolean[][]{
                {false, CoreOperationGreaterThan.class},
                {false, CoreOperationGreaterThanOrEqual.class},
                {true, CoreOperationEqual.class},
                {true, CoreOperationNotEqual.class},
                {false, CoreOperationLessThan.class},
                {false, CoreOperationLessThanOrEqual.class}
        };
    }

    private static int[][] dataProviderForTestEvaluateCompare() {
        return new int[][]{
                {0, new CoreOperationGreaterThan().getClass(), Boolean.TRUE},
                {-1, new CoreOperationLessThan().getClass(), Boolean.TRUE},
                {0, new CoreOperationGreaterThanOrEqual().getClass(), Boolean.TRUE},
                {0, new CoreOperationLessThanOrEqual().getClass(), Boolean.TRUE},
                {1, new CoreOperationGreaterThan().getClass(), Boolean.FALSE},
                {-1, new CoreOperationLessThan().getClass(), Boolean.FALSE},
                {0, new CoreOperationLessThanOrEqual().getClass(), Boolean.FALSE},
                {0, new CoreOperationGreaterThanOrEqual().getClass(), Boolean.FALSE}
        };
    }

    private static Object[][] dataProviderForTestCompute() {
        return new Object[][]{
                {new Integer(5), new Integer(3), Boolean.TRUE},
                {new Integer(5), new Integer(5), Boolean.TRUE},
                {new Integer(5), new Integer(6), Boolean.FALSE},
                {new Integer(5), new Integer(4), Boolean.FALSE},
                {new Integer(5), new Integer(5.0), Boolean.TRUE},
                {new Integer(5), new Double(3.5), Boolean.FALSE},
                {new Integer(5), new Double(5.0), Boolean.TRUE},
                {new Integer(5), new Double(4.5), Boolean.FALSE},
                {new Double(5.5), new Integer(3), Boolean.FALSE},
                {new Double(5.5), new Integer(5), Boolean.TRUE},
                {new Double(5.5), new Integer(6), Boolean.FALSE},
                {new Double(5.5), new Integer(4), Boolean.FALSE},
                {new Double(5.5), new Double(3.5), Boolean.FALSE},
                {new Double(5.5), new Double(5.0), Boolean.TRUE},
                {new Double(5.5), new Double(4.5), Boolean.FALSE}
        };
    }

    private static Object[][] dataProviderForTestReduce() {
        return new Object[][]{
                {new Integer(5), new InitialContext()},
                {new Integer(5), new SelfContext()},
                {new Integer(5), new ArrayList<>().iterator()},
                {new Integer(5), new HashSet<>().iterator()},
                {new Double(5.5), new InitialContext()},
                {new Double(5.5), new SelfContext()},
                {new Double(5.5), new ArrayList<>().iterator()},
                {new Double(5.5), new HashSet<>().iterator()}
        };
    }

    private static Object[][] dataProviderForTestFindMatch() {
        Object[][] data = {
                {new Object[]{1, 2, 3, 4, 5}, new Object[]{3, 4, 5}, Boolean.TRUE},
                {new Object[]{1, 2, 3, 4, 5}, new Object[]{3, 4, 6}, Boolean.FALSE},
                {new Object[]{1.1, 2.2, 3.3, 4.4, 5.5}, new Object[]{3.3, 4.4, 5.5}, Boolean.TRUE},
                {new Object[]{1.1, 2.2, 3.3, 4.4, 5.5}, new Object[]{3.3, 4.4, 6.6}, Boolean.FALSE}
        };

        return data;
    }

    private static Object[][] dataProviderForTestContainsMatch() {
        Object[][] data = {
                {new Object[]{1, 2, 3, 4, 5}, new Integer(3), Boolean.TRUE},
                {new Object[]{1, 2, 3, 4, 5}, new Integer(6), Boolean.FALSE},
                {new Object[]{1.1, 2.2, 3.3, 4.4, 5.5}, new Double(3.3), Boolean.TRUE},
                {new Object[]{1.1, 2.2, 3.3, 4.4, 5.5}, new Double(6.6), Boolean.FALSE}
        };

        return data;
    }
}

class CoreOperationGreaterThan extends CoreOperationRelationalExpression {

    public CoreOperationGreaterThan(Expression left, Expression right) {
        super(new Expression[]{left, right});
    }

    @Override
    protected boolean evaluateCompare(int compare) {
        return compare > 0;
    }
}

class CoreOperationGreaterThanOrEqual extends CoreOperationRelationalExpression {

    public CoreOperationGreaterThanOrEqual(Expression left, Expression right) {
        super(new Expression[]{left, right});
    }

    @Override
    protected boolean evaluateCompare(int compare) {
        return compare >= 0;
    }
}