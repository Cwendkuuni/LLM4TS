package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionArgs;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionType;
import org.apache.commons.jxpath.ri.model.NodeInfo;
import org.apache.commons.jxpath.ri.model.Value;
import org.apache.commons.jxpath.ri.model.ValueFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * CoreOperationRelationalExpressionTest is a JUnit 4 test case file for the CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    // Test methods for public abstract CoreOperationRelationalExpression constructor

    /**
     * Test the CoreOperationRelationalExpression constructor with valid arguments.
     */
    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression arg1 = new CoreOperationLiteralExpression(new ValueFactory().createValue(1.0));
        Expression arg2 = new CoreOperationLiteralExpression(new ValueFactory().createValue(2.0));
        CoreOperationRelationalExpression expected = new CoreOperationRelationalExpressionFactory()
                .newExpression(CoreOperationRelationalExpressionType.GT);
        expected.setLeft(arg1);
        expected.setRight(arg2);
        CoreOperationRelationalExpression actual = new CoreOperationRelationalExpression(new CoreOperationRelationalExpressionArgs[]{new CoreOperationRelationalExpressionArgs(CoreOperationRelationalExpressionType.GT, arg1, arg2)});
        assertEquals(expected, actual);
    }

    // Test methods for public final Object computeValue(EvalContext context)

    /**
     * Test the computeValue method with valid arguments and expected result of true.
     */
    @Test
    public void testComputeValueTrue() {
        Expression arg1 = new CoreOperationLiteralExpression(new ValueFactory().createValue(2.0));
        Expression arg2 = new CoreOperationLiteralExpression(new ValueFactory().createValue(2.0));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .newExpression(CoreOperationRelationalExpressionType.EQ);
        expression.setLeft(arg1);
        expression.setRight(arg2);
        Object result = expression.computeValue(new EvalContext(new NodeInfo[0]));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test the computeValue method with valid arguments and expected result of false.
     */
    @Test
    public void testComputeValueFalse() {
        Expression arg1 = new CoreOperationLiteralExpression(new ValueFactory().createValue(1.0));
        Expression arg2 = new CoreOperationLiteralExpression(new ValueFactory().createValue(2.0));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .newExpression(CoreOperationRelationalExpressionType.GT);
        expression.setLeft(arg1);
        expression.setRight(arg2);
        Object result = expression.computeValue(new EvalContext(new NodeInfo[0]));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    // Test methods for protected final int getPrecedence()

    /**
     * Test the getPrecedence method with a valid CoreOperationRelationalExpression and expected precedence value.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .newExpression(CoreOperationRelationalExpressionType.GT);
        int expected = RELATIONAL_EXPR_PRECEDENCE;
        int actual = expression.getPrecedence();
        assertEquals(expected, actual);
    }

    // Test methods for protected final boolean isSymmetric()

    /**
     * Test the isSymmetric method with a valid CoreOperationRelationalExpression and expected result of false.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .newExpression(CoreOperationRelationalExpressionType.GT);
        boolean expected = false;
        boolean actual = expression.isSymmetric();
        assertEquals(expected, actual);
    }

    // Test methods for protected abstract boolean evaluateCompare(int compare)

    /**
     * Test the evaluateCompare method with a valid compare value and expected result of true.
     */
    @Test
    public void testEvaluateCompareTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression();
        boolean expected = true;
        boolean actual = expression.evaluateCompare(0);
        assertEquals(expected, actual);
    }

    /**
     * Test the evaluateCompare method with a valid compare value and expected result of false.
     */
    @Test
    public void testEvaluateCompareFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression();
        boolean expected = false;
        boolean actual = expression.evaluateCompare(-1);
        assertEquals(expected, actual);
    }

    // Test methods for private boolean compute(Object left, Object right)

    /**
     * Test the compute method with valid arguments, no NaN values, and expected result of true.
     */
    @Test
    public void testComputeTrue() {
        Expression arg1 = new CoreOperationLiteralExpression(new ValueFactory().createValue(2.0));
        Expression arg2 = new CoreOperationLiteralExpression(new ValueFactory().createValue(2.0));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .newExpression(CoreOperationRelationalExpressionType.EQ);
        expression.setLeft(arg1);
        expression.setRight(arg2);
        Object result = expression.compute(arg1.compute(new EvalContext(new NodeInfo[0])), arg2.compute(new EvalContext(new NodeInfo[0])));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test the compute method with valid arguments, no NaN values, and expected result of false.
     */
    @Test
    public void testComputeFalse() {
        Expression arg1 = new CoreOperationLiteralExpression(new ValueFactory().createValue(1.0));
        Expression arg2 = new CoreOperationLiteralExpression(new ValueFactory().createValue(2.0));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .newExpression(CoreOperationRelationalExpressionType.GT);
        expression.setLeft(arg1);
        expression.setRight(arg2);
        Object result = expression.compute(arg1.compute(new EvalContext(new NodeInfo[0])), arg2.compute(new EvalContext(new NodeInfo[0])));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    // Test methods for private Object reduce(Object o)

    /**
     * Test the reduce method with a valid SelfContext and expected result of the NodeInfo associated with the SelfContext.
     */
    @Test
    public void testReduceSelfContext() {
        Expression selfContext = new SelfContext();
        Object expected = new NodeInfo(new Object());
        Object actual = new CoreOperationRelationalExpression(new CoreOperationRelationalExpressionArgs[0])
                .reduce(selfContext);
        assertEquals(expected, actual);
    }

    /**
     * Test the reduce method with a valid Collection and expected result of an Iterator for the Collection.
     */
    @Test
    public void testReduceCollection() {
        Collection<Object> collection = new ArrayList<>();
        collection.add(new ValueFactory().createValue(1.0));
        Object expected = collection.iterator();
        Object actual = new CoreOperationRelationalExpression(new CoreOperationRelationalExpressionArgs[0])
                .reduce(collection);
        assertEquals(expected.getClass(), actual.getClass());
        assertTrue(actual instanceof Iterator);
    }

    // Test methods for private boolean findMatch(Iterator lit, Iterator rit)

    /**
     * Test the findMatch method with valid Iterators and expected result of true.
     */
    @Test
    public void testFindMatchTrue() {
        Iterator<Object> leftIterator = new ArrayList<Object>(){{add(new ValueFactory().createValue(1.0));}}.iterator();
        Iterator<Object> rightIterator = new ArrayList<Object>(){{add(new ValueFactory().createValue(1.0));}}.iterator();
        boolean expected = true;
        boolean actual = new CoreOperationRelationalExpression(new CoreOperationRelationalExpressionArgs[0])
                .findMatch(leftIterator, rightIterator);
        assertEquals(expected, actual);
    }

    /**
     * Test the findMatch method with valid Iterators and expected result of false.
     */
    @Test
    public void testFindMatchFalse() {
        Iterator<Object> leftIterator = new ArrayList<Object>(){{add(new ValueFactory().createValue(1.0));}}.iterator();
        Iterator<Object> rightIterator = new ArrayList<Object>(){{add(new ValueFactory().createValue(2.0));}}.iterator();
        boolean expected = false;
        boolean actual = new CoreOperationRelationalExpression(new CoreOperationRelationalExpressionArgs[0])
                .findMatch(leftIterator, rightIterator);
        assertEquals(expected, actual);
    }

    // Test methods for private boolean containsMatch(Iterator it, Object value)

    /**
     * Test the containsMatch method with a valid Iterator and a valid Object and expected result of true.
     */
    @Test
    public void testContainsMatchTrue() {
        Iterator<Object> iterator = new ArrayList<Object>(){{add(new ValueFactory().createValue(1.0)); add(new ValueFactory().createValue(1.0));}}.iterator();
        Object value = new ValueFactory().createValue(1.0);
        boolean expected = true;
        boolean actual = new CoreOperationRelationalExpression(new CoreOperationRelationalExpressionArgs[0])
                .containsMatch(iterator, value);
        assertEquals(expected, actual);
    }

    /**
     * Test the containsMatch method with a valid Iterator and a invalid Object and expected result of false.
     */
    @Test
    public void testContainsMatchFalse() {
        Iterator<Object> iterator = new ArrayList<Object>(){{add(new ValueFactory().createValue(1.0));}}.iterator();
        Object value = new ValueFactory().createValue(2.0);
        boolean expected = false;
        boolean actual = new CoreOperationRelationalExpression(new CoreOperationRelationalExpressionArgs[0])
                .containsMatch(iterator, value);
        assertEquals(expected, actual);
    }

    // Test methods for private boolean compute(Object left, Object right) with NaN values

    /**
     * Test the compute method with valid NaN values and expected result of false.
     */
    @Test
    public void testComputeNaN() {
        Expression arg1 = new CoreOperationLiteralExpression(new ValueFactory().createValue(Double.NaN));
        Expression arg2 = new CoreOperationLiteralExpression(new ValueFactory().createValue(Double.NaN));
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .newExpression(CoreOperationRelationalExpressionType.EQ);
        expression.setLeft(arg1);
        expression.setRight(arg2);
        Object result = expression.compute(arg1.compute(new EvalContext(new NodeInfo[0])), arg2.compute(new EvalContext(new NodeInfo[0])));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    // Test methods for private boolean evaluateCompare(int compare) with negative compare values

    /**
     * Test the evaluateCompare method with a negative compare value and expected result of false.
     */
    @Test(expected = AssertionError.class)
    public void testEvaluateCompareNegativeCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression();
        boolean actual = expression.evaluateCompare(-1);
        fail("evaluateCompare should throw an AssertionError for negative compare value");
    }
}