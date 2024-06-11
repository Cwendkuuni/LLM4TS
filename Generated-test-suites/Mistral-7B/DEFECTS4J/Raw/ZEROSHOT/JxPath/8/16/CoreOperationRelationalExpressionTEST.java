import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @org.junit.Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(1));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThan(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(1));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThan(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression1 = new CoreOperationGreaterThan(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        CoreOperationRelationalExpression expression2 = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        int precedence1 = expression1.getPrecedence();
        int precedence2 = expression2.getPrecedence();
        assertEquals(3, precedence1);
        assertEquals(3, precedence2);

        CoreOperationAdd expression3 = new CoreOperationAdd(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        int precedence3 = expression3.getPrecedence();
        assertEquals(2, precedence3);
    }

    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        boolean isSymmetric = expression.isSymmetric();
        assertFalse(isSymmetric);

        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        isSymmetric = expression.isSymmetric();
        assertFalse(isSymmetric);

        expression = new CoreOperationLessThan(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        isSymmetric = expression.isSymmetric();
        assertFalse(isSymmetric);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        isSymmetric = expression.isSymmetric();
        assertFalse(isSymmetric);
    }

    @org.junit.Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanRelation(true);
        int compare = 1;
        boolean result = expression.evaluateCompare(compare);
        assertTrue(result);

        expression = new CoreOperationGreaterThanOrEqualRelation(true);
        compare = 0;
        result = expression.evaluateCompare(compare);
        assertTrue(result);

        expression = new CoreOperationGreaterThanRelation(false);
        compare = 1;
        result = expression.evaluateCompare(compare);
        assertFalse(result);

        expression = new CoreOperationGreaterThanOrEqualRelation(false);
        compare = 0;
        result = expression.evaluateCompare(compare);
        assertFalse(result);

        expression = new CoreOperationLessThanRelation(true);
        compare = -1;
        result = expression.evaluateCompare(compare);
        assertTrue(result);

        expression = new CoreOperationLessThanOrEqualRelation(true);
        compare = -1;
        result = expression.evaluateCompare(compare);
        assertTrue(result);

        expression = new CoreOperationLessThanRelation(false);
        compare = -1;
        result = expression.evaluateCompare(compare);
        assertFalse(result);

        expression = new CoreOperationLessThanOrEqualRelation(false);
        compare = 0;
        result = expression.evaluateCompare(compare);
        assertTrue(result);
    }

    @org.junit.Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        Object left = new Integer(1);
        Object right = new Integer(2);
        boolean result = expression.compute(left, right);
        assertTrue(result);

        left = new Integer(1);
        right = new Integer(1);
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(1));
        result = expression.compute(left, right);
        assertTrue(result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationLessThan(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(1));
        result = expression.compute(left, right);
        assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(2));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationLessThan(new CoreExpressionLiteral(1), new CoreExpressionLiteral(1));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(1), new CoreExpressionLiteral(1));
        result = expression.compute(left, right);
        assertTrue(result);

        expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(new Integer(1)), new CoreExpressionLiteral(new Integer(2)));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(new Integer(1)), new CoreExpressionLiteral(new Integer(2)));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationLessThan(new CoreExpressionLiteral(new Integer(1)), new CoreExpressionLiteral(new Integer(2)));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(new Integer(1)), new CoreExpressionLiteral(new Integer(2)));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreExpressionLessThanOrEqual(new CoreExpressionIterator(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(2)); }}), new CoreExpressionLiteral(new Integer(2)));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionIterator(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(1)); }}), new CoreExpressionLiteral(new Integer(2)));
        result = expression.compute(left, right);
        assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(new Integer(1)), new CoreExpressionIterator(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(2)); }}));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(new Integer(2)), new CoreExpressionIterator(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(1)); }}));
        result = expression.compute(left, right);
        assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionIterator(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(1)); }}), new CoreExpressionLiteral(new Integer(1)));
        result = expression.compute(left, right);
        assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionIterator(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(1)); }}), new CoreExpressionLiteral(new Integer(2)));
        result = expression.compute(left, right);
        assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionIterator(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(2)); }}), new CoreExpressionLiteral(new Integer(2)));
        result = expression.compute(left, right);
        assertTrue(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionIterator(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(2)); }}), new CoreExpressionLiteral(new Integer(1)));
        result = expression.compute(left, right);
        assertFalse(result);

        expression = new CoreOperationLessThanOrEqual(new CoreExpressionIterator(new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(2)); }}), new CoreExpressionLiteral(new Integer(1)));
        result = expression.compute(left, right);
        assertFalse(result);
    }

    @org.junit.Test
    public void testReduce() {
        Object o = new Integer(1);
        Object reduced = new CoreOperationRelationalExpression().reduce(o);
        assertEquals(o, reduced);

        o = new SelfContext();
        reduced = new CoreOperationRelationalExpression().reduce(o);
        assertEquals(o, reduced);

        o = new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(1)); }};
        reduced = new CoreOperationRelationalExpression().reduce(o);
        assertEquals(o.iterator(), reduced);

        o = new InitialContext();
        reduced = new CoreOperationRelationalExpression().reduce(o);
        assertEquals(o, reduced);
    }

    @org.junit.Test
    public void testContainsMatch() {
        Collection<Object> collection = new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(2)); }};
        Iterator<Object> iterator = collection.iterator();
        Object value = new Integer(1);
        boolean result = new CoreOperationRelationalExpression().containsMatch(iterator, value);
        assertTrue(result);

        result = new CoreOperationRelationalExpression().containsMatch(iterator, new Integer(3));
        assertFalse(result);
    }

    @org.junit.Test
    public void testFindMatch() {
        Collection<Object> leftCollection = new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(1)); add(new Integer(2)); }};
        Collection<Object> rightCollection = new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(1)); }};
        Iterator<Object> leftIterator = leftCollection.iterator();
        Iterator<Object> rightIterator = rightCollection.iterator();
        boolean result = new CoreOperationRelationalExpression().findMatch(leftIterator, rightIterator);
        assertTrue(result);

        leftCollection = new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(1)); add(new Integer(2)); }};
        rightCollection = new ArrayList<Object>() {{ add(new Integer(1)); add(new Integer(2)); }};
        leftIterator = leftCollection.iterator();
        rightIterator = rightCollection.iterator();
        result = new CoreOperationRelationalExpression().findMatch(leftIterator, rightIterator);
        assertFalse(result);
    }
}