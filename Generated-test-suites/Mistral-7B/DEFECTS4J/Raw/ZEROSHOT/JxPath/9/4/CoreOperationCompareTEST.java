package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InitialContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompareTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * JUnit test file for CoreOperationCompare class.
 */
public class CoreOperationCompareTest extends CoreOperationTest {

    @Override
    protected Expression createExpression() {
        Expression arg1 = createExpression("arg1");
        Expression arg2 = createExpression("arg2");
        return new CoreOperationCompare(arg1, arg2);
    }

    @Override
    protected void testEquals(Expression expression) {
        CoreOperationCompare coreOperationCompare = (CoreOperationCompare) expression;
        Expression arg1 = coreOperationCompare.getArguments()[0];
        Expression arg2 = coreOperationCompare.getArguments()[1];

        // Test equal method with same expressions
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        // Test equal method with different expressions of the same value
        Expression arg1Copy = createExpression(arg1.getValue(context));
        Expression arg2Copy = createExpression(arg2.getValue(context));
        assertTrue(coreOperationCompare.equal(context, arg1Copy, arg2Copy));

        // Test equal method with different expressions of different values
        Object value1 = arg1.getValue(context);
        Object value2 = arg2.getValue(context);
        if (!value1.equals(value2)) {
            assertFalse(coreOperationCompare.equal(context, arg1, arg2));
        }

        // Test equal method with different expressions of different types
        if (value1 instanceof Number && value2 instanceof String) {
            assertFalse(coreOperationCompare.equal(context, arg1, arg2));
        }

        // Test equal method with pointers
        Pointer pointer1 = new Pointer(new Object(), new Object());
        Pointer pointer2 = new Pointer(new Object(), new Object());
        Pointer pointer3 = new Pointer(new Object(), new Object());
        ContextWrapper contextWrapper = new ContextWrapper();
        contextWrapper.setVariable("pointer1", pointer1);
        contextWrapper.setVariable("pointer2", pointer2);
        contextWrapper.setVariable("pointer3", pointer3);
        EvalContext contextWrapperEvalContext = new EvalContext(contextWrapper);
        coreOperationCompare = (CoreOperationCompare) expression.compile(contextWrapperEvalContext);
        assertFalse(coreOperationCompare.equal(contextWrapperEvalContext, arg1, arg2));
        assertTrue(coreOperationCompare.equal(contextWrapperEvalContext, arg1, arg1));
        assertTrue(coreOperationCompare.equal(contextWrapperEvalContext, arg2, arg2));
        assertTrue(coreOperationCompare.equal(contextWrapperEvalContext, arg1, pointer1));
        assertTrue(coreOperationCompare.equal(contextWrapperEvalContext, pointer1, arg1));
        assertTrue(coreOperationCompare.equal(contextWrapperEvalContext, pointer1, pointer1));

        // Test equal method with initial contexts
        InitialContext initialContext1 = new InitialContext();
        InitialContext initialContext2 = new InitialContext();
        coreOperationCompare = (CoreOperationCompare) expression.compile(initialContext1);
        assertFalse(coreOperationCompare.equal(initialContext1, arg1, arg2));
        assertTrue(coreOperationCompare.equal(initialContext1, arg1, arg1));
        assertTrue(coreOperationCompare.equal(initialContext1, arg2, arg2));

        // Test equal method with self contexts
        SelfContext selfContext1 = new SelfContext(new Object(), null);
        SelfContext selfContext2 = new SelfContext(new Object(), null);
        coreOperationCompare = (CoreOperationCompare) expression.compile(selfContext1);
        assertFalse(coreOperationCompare.equal(selfContext1, arg1, arg2));
        assertTrue(coreOperationCompare.equal(selfContext1, arg1, arg1));
        assertTrue(coreOperationCompare.equal(selfContext1, arg2, arg2));
    }

    @Override
    protected void testNotEquals(Expression expression) {
        CoreOperationCompare coreOperationCompare = (CoreOperationCompare) expression;
        Expression arg1 = coreOperationCompare.getArguments()[0];
        Expression arg2 = coreOperationCompare.getArguments()[1];

        // Test not equal method with same expressions
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.notEqual(context, arg1, arg2));

        // Test not equal method with different expressions of the same value
        Expression arg1Copy = createExpression(arg1.getValue(context));
        Expression arg2Copy = createExpression(arg2.getValue(context));
        assertFalse(coreOperationCompare.notEqual(context, arg1Copy, arg2Copy));

        // Test not equal method with different expressions of different values
        Object value1 = arg1.getValue(context);
        Object value2 = arg2.getValue(context);
        if (value1.equals(value2)) {
            fail("Expected not equal but got equal");
        }
        assertTrue(coreOperationCompare.notEqual(context, arg1, arg2));

        // Test not equal method with different expressions of different types
        if (value1 instanceof Number && value2 instanceof String) {
            assertTrue(coreOperationCompare.notEqual(context, arg1, arg2));
        }

        // Test not equal method with pointers
        Pointer pointer1 = new Pointer(new Object(), new Object());
        Pointer pointer2 = new Pointer(new Object(), new Object());
        ContextWrapper contextWrapper = new ContextWrapper();
        contextWrapper.setVariable("pointer1", pointer1);
        contextWrapper.setVariable("pointer2", pointer2);
        EvalContext contextWrapperEvalContext = new EvalContext(contextWrapper);
        coreOperationCompare = (CoreOperationCompare) expression.compile(contextWrapperEvalContext);
        assertFalse(coreOperationCompare.notEqual(contextWrapperEvalContext, arg1, arg2));
        assertFalse(coreOperationCompare.notEqual(contextWrapperEvalContext, arg1, pointer1));
        assertFalse(coreOperationCompare.notEqual(contextWrapperEvalContext, pointer1, arg1));
        assertFalse(coreOperationCompare.notEqual(contextWrapperEvalContext, pointer1, pointer1));
        assertTrue(coreOperationCompare.notEqual(contextWrapperEvalContext, arg1, arg2));
        assertTrue(coreOperationCompare.notEqual(contextWrapperEvalContext, arg2, arg1));

        // Test not equal method with initial contexts
        InitialContext initialContext1 = new InitialContext();
        InitialContext initialContext2 = new InitialContext();
        coreOperationCompare = (CoreOperationCompare) expression.compile(initialContext1);
        assertFalse(coreOperationCompare.notEqual(initialContext1, arg1, arg2));

        // Test not equal method with self contexts
        SelfContext selfContext1 = new SelfContext(new Object(), null);
        SelfContext selfContext2 = new SelfContext(new Object(), null);
        coreOperationCompare = (CoreOperationCompare) expression.compile(selfContext1);
        assertFalse(coreOperationCompare.notEqual(selfContext1, arg1, arg2));
        assertFalse(coreOperationCompare.notEqual(selfContext1, arg1, selfContext1));
        assertFalse(coreOperationCompare.notEqual(selfContext1, selfContext1, arg1));
        assertFalse(coreOperationCompare.notEqual(selfContext1, selfContext1, selfContext1));
        assertTrue(coreOperationCompare.notEqual(selfContext1, arg1, arg2));
        assertTrue(coreOperationCompare.notEqual(selfContext1, arg2, arg1));
    }

    @Override
    protected void testIsSymmetric() {
        assertTrue(CoreOperationCompare.class.isInstance(new CoreOperationCompare(new CoreOperationCompare(null, null), null))
                .isSymmetric());
    }

    @Override
    protected void testGetPrecedence() {
        assertEquals(2, CoreOperationCompare.class.isInstance(new CoreOperationCompare(null, null)).getPrecedence());
    }

    @Override
    protected void testCompoundExpression() {
        Expression expression = createExpression();
        Collection<Expression> compoundExpressions = new ArrayList<>();
        compoundExpressions.add(expression);
        compoundExpressions.add(expression);
        EvalContext context = new EvalContext();
        Expression result = expression.compile(context).compile(context);
        Object value = result.getValue(context);
        assertTrue(value instanceof Boolean);
        assertTrue(((Boolean) value).booleanValue());
    }

    @Override
    protected void testCompareCollections() {
        Collection<Object> leftCollection = new ArrayList<>();
        Collection<Object> rightCollection = new ArrayList<>();

        // Test equal method with equal collections
        leftCollection.add(1);
        leftCollection.add(2);
        rightCollection.add(1);
        rightCollection.add(2);
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = (CoreOperationCompare) createExpression().compile(context);
        assertTrue(coreOperationCompare.equal(context, createExpression("leftCollection"), createExpression("rightCollection")));

        // Test equal method with non-equal collections
        leftCollection.add(1);
        leftCollection.add(2);
        rightCollection.add(1);
        rightCollection.add(3);
        assertFalse(coreOperationCompare.equal(context, createExpression("leftCollection"), createExpression("rightCollection")));

        // Test contains method with collection as left argument
        leftCollection.clear();
        leftCollection.add(1);
        leftCollection.add(2);
        Object value1 = new Integer(1);
        Object value2 = new Integer(2);
        rightCollection.add(value1);
        rightCollection.add(value2);
        rightCollection.add(new Integer(3));
        assertTrue(coreOperationCompare.contains(leftCollection.iterator(), value1));
        assertTrue(coreOperationCompare.contains(leftCollection.iterator(), value2));
        assertFalse(coreOperationCompare.contains(leftCollection.iterator(), new Integer(3)));
        assertFalse(coreOperationCompare.contains(leftCollection.iterator(), new Integer(4)));

        // Test contains method with collection as right argument
        Collection<Object> rightCollectionCopy = new ArrayList<>();
        rightCollectionCopy.addAll(rightCollection);
        assertTrue(coreOperationCompare.contains(rightCollection.iterator(), value1));
        assertTrue(coreOperationCompare.contains(rightCollection.iterator(), value2));
        assertFalse(coreOperationCompare.contains(rightCollection.iterator(), new Integer(3)));
        assertFalse(coreOperationCompare.contains(rightCollection.iterator(), new Integer(4)));

        // Test findMatch method with iterators
        Iterator<Object> leftIterator = leftCollection.iterator();
        Iterator<Object> rightIterator = rightCollection.iterator();
        HashSet<Object> leftSet = new HashSet<>();
        while (leftIterator.hasNext()) {
            leftSet.add(leftIterator.next());
        }
        assertTrue(coreOperationCompare.findMatch(leftIterator, rightIterator));
        assertTrue(coreOperationCompare.findMatch(rightIterator, leftIterator));
        assertFalse(coreOperationCompare.findMatch(leftIterator, rightIterator));
        assertFalse(coreOperationCompare.findMatch(rightIterator, new ArrayList<>(leftCollection).iterator()));

        // Test findMatch method with different collections
        Collection<Object> leftCollection2 = new ArrayList<>();
        leftCollection2.add(new Integer(4));
        rightCollectionCopy.clear();
        rightCollectionCopy.addAll(rightCollection);
        assertFalse(coreOperationCompare.findMatch(leftIterator, rightIterator));
        assertFalse(coreOperationCompare.findMatch(leftIterator, new ArrayList<>(rightCollection).iterator()));
        assertFalse(coreOperationCompare.findMatch(rightIterator, leftIterator));
        assertFalse(coreOperationCompare.findMatch(rightIterator, new ArrayList<>(leftCollection).iterator()));
    }
}