import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThanPositive() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(3.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EqualityTester tester = new EqualityTester();
        InfoSet infoSet = tester.createMockInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanZero() {
        Expression arg1 = new LiteralExpression(new Double(0.0));
        Expression arg2 = new LiteralExpression(new Double(1.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EqualityTester tester = new EqualityTester();
        InfoSet infoSet = tester.createMockInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new Double(1.0));
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGreaterThanNegative() {
        Expression arg1 = new LiteralExpression(new Double(-3.0));
        Expression arg2 = new LiteralExpression(new Double(-5.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EqualityTester tester = new EqualityTester();
        InfoSet infoSet = tester.createMockInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new Double(-3.0));
        arg2 = new LiteralExpression(new Double(-2.0));
        operation = new CoreOperationGreaterThan(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.MIN_VALUE, Double.MAX_VALUE})
    public void testGreaterThanMinMaxValue(Double value) {
        Expression arg1 = new LiteralExpression(value);
        Expression arg2 = new LiteralExpression(new Double(0.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EqualityTester tester = new EqualityTester();
        InfoSet infoSet = tester.createMockInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals((value > 0.0), result);
    }

    @Test
    public void testNullArgument() {
        Expression arg1 = new LiteralExpression((Object) null);
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EqualityTester tester = new EqualityTester();
        InfoSet infoSet = tester.createMockInfoSet();
        EvalContext context = new EvalContext(infoSet);
        try {
            operation.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMismatchedTypes() {
        Expression arg1 = new LiteralExpression(new Integer(5));
        Expression arg2 = new LiteralExpression(new Double(3.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EqualityTester tester = new EqualityTester();
        InfoSet infoSet = tester.createMockInfoSet();
        EvalContext context = new EvalContext(infoSet);
        try {
            operation.computeValue(context);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testInvalidExpression() {
        Expression arg1 = new InvalidExpression();
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EqualityTester tester = new EqualityTester();
        InfoSet infoSet = tester.createMockInfoSet();
        EvalContext context = new EvalContext(infoSet);
        try {
            operation.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}