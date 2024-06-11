import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.IterateIter;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.core.Expression;
import org.apache.commons.jxpath.ri.core.NodeTest;
import org.apache.commons.jxpath.ri.iter.SingleNodeIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIter;
import org.apache.commons.jxpath.ri.iter.VectorIter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        // Test empty constructor
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertNotNull(expression);

        // Test constructor with arguments
        Expression left = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "1"));
        Expression right = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "2"));
        CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        assertNotNull(relationalExpression);
        assertEquals(CoreOperationRelationalExpression.class, relationalExpression.getClass());
    }

    @Test
    public void testComputeValue() {
        Expression left = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "1"));
        Expression right = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "2"));
        CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});

        EvaluationContext context = new EvaluationContext(Context.getRootNode());
        Object result = relationalExpression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        left = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "2"));
        right = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "2"));
        relationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        result = relationalExpression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression left = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "1"));
        Expression right = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "2"));
        CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        int precedence = relationalExpression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression left = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "1"));
        Expression right = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "2"));
        CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        assertFalse(relationalExpression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertTrue(relationalExpression.evaluateCompare(1));
        assertFalse(relationalExpression.evaluateCompare(0));
        assertFalse(relationalExpression.evaluateCompare(-1));
    }

    @Test
    public void testCompute() {
        Expression left = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "1"));
        Expression right = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "2"));
        CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});

        Object leftValue = left.computeValue(new EvaluationContext(Context.getRootNode()));
        Object rightValue = right.computeValue(new EvaluationContext(Context.getRootNode()));
        assertTrue(relationalExpression.compute(leftValue, rightValue));

        leftValue = new SingletonIter(Context.getRootNode(), "3");
        rightValue = new SingletonIter(Context.getRootNode(), "2");
        assertFalse(relationalExpression.compute(leftValue, rightValue));

        leftValue = new IterateIter(Context.getRootNode(), "1", "2", "3");
        rightValue = new IterateIter(Context.getRootNode(), "2", "3", "4");
        assertFalse(relationalExpression.compute(leftValue, rightValue));

        leftValue = new IterateIter(Context.getRootNode(), "1", "2", "3");
        rightValue = new VectorIter(Context.getRootNode(), new Object[]{"2", "1", "3"});
        assertFalse(relationalExpression.compute(leftValue, rightValue));

        leftValue = new SingletonIter(Context.getRootNode(), "1.0");
        rightValue = new SingletonIter(Context.getRootNode(), "2.0");
        assertFalse(relationalExpression.compute(leftValue, rightValue));

        leftValue = new SingletonIter(Context.getRootNode(), "1.0");
        rightValue = new SingletonIter(Context.getRootNode(), "1.0");
        assertTrue(relationalExpression.compute(leftValue, rightValue));
    }

    @Test
    public void testReduce() {
        Expression value = new NodeTest(NodeTest.IS_NUMBER, new SingleNodeIterator(Context.getRootNode(), "1"));
        Object reducedValue = ((CoreOperationRelationalExpression) new CoreOperationRelationalExpression(new Expression[]{value}) {
            @Override
            protected Object reduce(Object o) {
                return new SingleNodeIterator(Context.getRootNode(), "2");
            }
        }).reduce(value);
        assertNotNull(reducedValue);
        assertTrue(reducedValue instanceof SingleNodeIterator);
    }

    @Test
    public void testContainsMatch() {
        Expression it = new IterateIter(Context.getRootNode(), "1", "2", "3");
        Object value = new SingletonIter(Context.getRootNode(), "2");
        CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new Expression[]{it});
        assertTrue(relationalExpression.containsMatch(it.iterator(), value));

        it = new IterateIter(Context.getRootNode(), "1", "2", "3");
        value = new SingletonIter(Context.getRootNode(), "3");
        assertFalse(relationalExpression.containsMatch(it.iterator(), value));

        it = new IterateIter(Context.getRootNode(), "1", "2", "3");
        value = new SingletonIter(Context.getRootNode(), "1");
        assertTrue(relationalExpression.containsMatch(it.iterator(), value));

        it = new IterateIter(Context.getRootNode(), "1", "2", "3");
        value = new VectorIter(Context.getRootNode(), new Object[]{"2", "1", "3"});
        assertFalse(relationalExpression.containsMatch(it.iterator(), value));

        it = new IterateIter(Context.getRootNode(), "1", "2", "3");
        value = new SingletonIter(Context.getRootNode(), "2.0");
        assertFalse(relationalExpression.containsMatch(it.iterator(), value));

        it = new IterateIter(Context.getRootNode(), "1", "2", "3");
        value = new SingletonIter(Context.getRootNode(), "1.0");
        assertTrue(relationalExpression.containsMatch(it.iterator(), value));
    }

    @Test
    public void testFindMatch() {
        Expression leftIt = new IterateIter(Context.getRootNode(), "1", "2", "3");
        Expression rightIt = new IterateIter(Context.getRootNode(), "1", "2", "3");
        CoreOperationRelationalExpression relationalExpression = new CoreOperationRelationalExpression(new Expression[]{leftIt});
        assertTrue(relationalExpression.findMatch(leftIt.iterator(), rightIt.iterator()));

        leftIt = new IterateIter(Context.getRootNode(), "1", "2", "3");
        rightIt = new IterateIter(Context.getRootNode(), "1", "4", "3");
        assertFalse(relationalExpression.findMatch(leftIt.iterator(), rightIt.iterator()));

        leftIt = new IterateIter(Context.getRootNode(), "1", "2", "3");
        rightIt = new IterateIter(Context.getRootNode(), "1", "2", "3");
        assertTrue(relationalExpression.findMatch(leftIt.iterator(), rightIt.iterator()));

        leftIt = new IterateIter(Context.getRootNode(), "1", "2", "3");
        rightIt = new VectorIter(Context.getRootNode(), new Object[]{"1", "2", "3"});
        assertFalse(relationalExpression.findMatch(leftIt.iterator(), rightIt.iterator()));

        leftIt = new IterateIter(Context.getRootNode(), "1", "2", "3");
        rightIt = new SingletonIter(Context.getRootNode(), "2.0");
        assertFalse(relationalExpression.findMatch(leftIt.iterator(), rightIt.iterator()));

        leftIt = new IterateIter(Context.getRootNode(), "1", "2", "3");
        rightIt = new SingletonIter(Context.getRootNode(), "1.0");
        assertTrue(relationalExpression.findMatch(leftIt.iterator(), rightIt.iterator()));
    }
}