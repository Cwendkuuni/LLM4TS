import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.Iterators;
import org.apache.commons.jxpath.ri.iter.SequenceIterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest.createExpression;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_boolean_boolean() {
        Expression expression = createExpression("true > false");
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_boolean_boolean_false() {
        Expression expression = createExpression("false > true");
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_double_double() {
        Expression expression = createExpression("3.5 > 2.5");
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("numbers", Arrays.asList(3.5, 2.5));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_double_double_false() {
        Expression expression = createExpression("2.5 > 3.5");
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("numbers", Arrays.asList(2.5, 3.5));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_double_double_equals() {
        Expression expression = createExpression("2.5 > 2.5");
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("numbers", Arrays.asList(2.5, 2.5));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_double_double_notNaN() {
        Expression expression = createExpression("NaN > 2.5");
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("numbers", Collections.singletonList(Double.NaN));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_double_double_NaN() {
        Expression expression = createExpression("2.5 > NaN");
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("numbers", Arrays.asList(2.5, Double.NaN));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_iterator_iterator() {
        Expression expression = createExpression("(1, 2, 3) > (2, 3, 4)");
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("iter1", Iterators.iter(Arrays.asList(1, 2, 3).iterator()));
        context.setNode("iter2", Iterators.iter(Arrays.asList(2, 3, 4).iterator()));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_iterator_iterator_match() {
        Expression expression = createExpression("(1, 2, 3) > (2, 3)");
        EvalContext context = new EvalContext(new InfoSet());
        SequenceIterator iter1 = Iterators.iter(Arrays.asList(1, 2, 3).iterator());
        SequenceIterator iter2 = Iterators.iter(Arrays.asList(2, 3).iterator());
        context.setNode("iter1", iter1);
        context.setNode("iter2", iter2);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_iterator_iterator_containsMatch() {
        Expression expression = createExpression("(1, 2, 3) > 2");
        EvalContext context = new EvalContext(new InfoSet());
        SequenceIterator iter = Iterators.iter(Arrays.asList(1, 2, 3).iterator());
        context.setNode("iter", iter);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_collection_collection() {
        Expression expression = createExpression("[1, 2, 3] > [2, 3, 4]");
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("collection1", Arrays.asList(1, 2, 3));
        context.setNode("collection2", Arrays.asList(2, 3, 4));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_collection_collection_match() {
        Expression expression = createExpression("[1, 2, 3] > [2, 3]");
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("collection1", Arrays.asList(1, 2, 3));
        context.setNode("collection2", Arrays.asList(2, 3));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_collection_collection_containsMatch() {
        Expression expression = createExpression("[1, 2, 3] > 2");
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("collection", Arrays.asList(1, 2, 3));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @ParameterizedTest(name = "{0} > {1}")
    @MethodSource("iteratorsProvider")
    public void testComputeValue_iterator_iterator(Iterator<Object> iter1, Iterator<Object> iter2) {
        Expression expression = createExpression("{0} > {1}", iter1, iter2);
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("iter1", iter1);
        context.setNode("iter2", iter2);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        if (iter1.hasNext() && iter2.hasNext()) {
            Object left = iter1.next();
            Object right = iter2.next();
            int compare = Integer.compare(InfoSetUtil.intValue(left), InfoSetUtil.intValue(right));
            assertEquals(evaluateCompare(compare), result);
        } else {
            assertFalse(result instanceof Boolean);
        }
    }

    @ParameterizedTest(name = "{0} > {1}")
    @MethodSource("iteratorsProvider")
    public void testComputeValue_iterator_boolean(Iterator<Object> iter, boolean value) {
        Expression expression = createExpression("{0} > {1}", iter, value);
        EvalContext context = new EvalContext(new InfoSet());
        context.setNode("iter", iter);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(Boolean.FALSE.equals(result) ^ value);
    }

    private static Object[][] iteratorsProvider() {
        return new Object[][]{
                {Iterators.iter(Arrays.asList(1, 2, 3).iterator()), Iterators.iter(Arrays.asList(2, 3).iterator())},
                {Iterators.iter(Arrays.asList(1, 2, 3).iterator()), 2},
                {Iterators.iter(Arrays.asList(2, 3).iterator()), 1},
                {Iterators.iter(Arrays.asList(1, 2).iterator()), 3},
                {Iterators.iter(Arrays.asList(3).iterator()), 1},
                {Iterators.iter(Arrays.asList(1).iterator()), 2},
                {null, 1},
                {1, null}
        };
    }

    private static boolean evaluateCompare(int compare) {
        return compare > 0;
    }

    private static Expression createExpression(String expression) {
        return createExpression(expression, new Expression[0]);
    }

    private static Expression createExpression(String expression, Expression... args) {
        return new CoreOperationRelationalExpression(args) {
            @Override
            protected String expressionString() {
                return expression;
            }
        };
    }
}