package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.IteratorSupport;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationGt(new CoreStringValue("1"), new CoreStringValue("2"));
        EvalContext context = new EvalContext(new InfoSetSupport(), new IteratorSupport());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGt(new CoreStringValue("2"), new CoreStringValue("1"));
        context = new EvalContext(new InfoSetSupport(), new IteratorSupport());
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expr = new CoreOperationLt(new CoreNumberValue(1.5), new CoreNumberValue(1.5));
        context = new EvalContext(new InfoSetSupport(), new IteratorSupport());
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationLt(new CoreNumberValue(1.5), new CoreNumberValue(2.5));
        context = new EvalContext(new InfoSetSupport(), new IteratorSupport());
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGt(new CoreStringValue("1"), new CoreStringValue("2"));
        int precedence = expr.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGt(new CoreStringValue("1"), new CoreStringValue("2"));
        boolean symmetric = expr.isSymmetric();
        assertFalse(symmetric);

        expr = new CoreOperationLt(new CoreStringValue("1"), new CoreStringValue("2"));
        symmetric = expr.isSymmetric();
        assertFalse(symmetric);

        expr = new CoreOperationGt(new CoreStringValue("2"), new CoreStringValue("1"));
        symmetric = expr.isSymmetric();
        assertFalse(symmetric);

        expr = new CoreOperationLt(new CoreStringValue("2"), new CoreStringValue("1"));
        symmetric = expr.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new CoreOperationGt(new CoreStringValue("1"), new CoreStringValue("2"));
        boolean result = expr.evaluateCompare(0);
        assertFalse(result);

        result = expr.evaluateCompare(-1);
        assertTrue(result);

        expr = new CoreOperationLt(new CoreStringValue("1"), new CoreStringValue("2"));
        result = expr.evaluateCompare(0);
        assertFalse(result);

        result = expr.evaluateCompare(1);
        assertFalse(result);

        result = expr.evaluateCompare(-1);
        assertTrue(result);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expr = new CoreOperationGt(new CoreNumberValue(1), new CoreNumberValue(2));
        Object left = expr.args[0].computeValue(new EvalContext(new InfoSetSupport(), new IteratorSupport()));
        Object right = expr.args[1].computeValue(new EvalContext(new InfoSetSupport(), new IteratorSupport()));
        Object result = expr.compute(left, right);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGt(new CoreNumberValue(2), new CoreNumberValue(1));
        left = expr.args[0].computeValue(new EvalContext(new InfoSetSupport(), new IteratorSupport()));
        right = expr.args[1].computeValue(new EvalContext(new InfoSetSupport(), new IteratorSupport()));
        result = expr.compute(left, right);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expr = new CoreOperationLt(new CoreNumberValue(1), new CoreNumberValue(2));
        left = expr.args[0].computeValue(new EvalContext(new InfoSetSupport(), new IteratorSupport()));
        right = expr.args[1].computeValue(new EvalContext(new InfoSetSupport(), new IteratorSupport()));
        result = expr.compute(left, right);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expr = new CoreOperationLt(new CoreNumberValue(2), new CoreNumberValue(1));
        left = expr.args[0].computeValue(new EvalContext(new InfoSetSupport(), new IteratorSupport()));
        right = expr.args[1].computeValue(new EvalContext(new InfoSetSupport(), new IteratorSupport()));
        result = expr.compute(left, right);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        Collections.shuffle(Arrays.asList(new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)));
        Iterator leftIter = Arrays.asList(new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)).iterator();
        Iterator rightIter = Arrays.asList(new CoreNumberValue(2), new CoreNumberValue(1), new CoreNumberValue(3)).iterator();
        expr = new CoreOperationGt(leftIter, rightIter);
        result = expr.compute(leftIter, rightIter);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expr = new CoreOperationLt(leftIter, rightIter);
        result = expr.compute(leftIter, rightIter);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testReduce() {
        Object obj = new CoreNumberValue(1);
        Object result = expr.reduce(obj);
        assertEquals(obj, result);

        obj = new CoreStringValue("test");
        result = expr.reduce(obj);
        assertEquals(obj, result);

        obj = new CoreList(new Object[]{new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)});
        result = expr.reduce(obj);
        assertTrue(result instanceof Iterator);
        Iterator iterator = (Iterator) result;
       assertTrue(iterator.hasNext());
        assertEquals(new CoreNumberValue(1), iterator.next());
    }

    @Test
    public void testContainsMatch() {
        Iterator leftIter = Arrays.asList(new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)).iterator();
        Object value = new CoreNumberValue(2);
        boolean result = expr.containsMatch(leftIter, value);
        assertTrue(result);

        leftIter = Arrays.asList(new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)).iterator();
        value = new CoreNumberValue(4);
        result = expr.containsMatch(leftIter, value);
        assertFalse(result);

        Iterator rightIter = Arrays.asList(new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)).iterator();
        value = new CoreNumberValue(2);
        result = expr.containsMatch(rightIter, value);
        assertTrue(result);

        rightIter = Arrays.asList(new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)).iterator();
        value = new CoreNumberValue(4);
        result = expr.containsMatch(rightIter, value);
        assertFalse(result);

        leftIter = Arrays.asList(new CoreStringValue("test1"), new CoreStringValue("test2"), new CoreStringValue("test3")).iterator();
        value = new CoreStringValue("test2");
        result = expr.containsMatch(leftIter, value);
        assertTrue(result);

        leftIter = Arrays.asList(new CoreStringValue("test1"), new CoreStringValue("test2"), new CoreStringValue("test3")).iterator();
        value = new CoreStringValue("test4");
        result = expr.containsMatch(leftIter, value);
        assertFalse(result);

        rightIter = Arrays.asList(new CoreStringValue("test1"), new CoreStringValue("test2"), new CoreStringValue("test3")).iterator();
        value = new CoreStringValue("test2");
        result = expr.containsMatch(rightIter, value);
        assertTrue(result);

        rightIter = Arrays.asList(new CoreStringValue("test1"), new CoreStringValue("test2"), new CoreStringValue("test3")).iterator();
        value = new CoreStringValue("test4");
        result = expr.containsMatch(rightIter, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        HashSet leftSet = new HashSet<>();
        leftSet.add(new CoreNumberValue(1));
        leftSet.add(new CoreNumberValue(2));
        leftSet.add(new CoreNumberValue(3));
        Iterator leftIter = leftSet.iterator();

        Iterator rightIter = Arrays.asList(new CoreNumberValue(2), new CoreNumberValue(1), new CoreNumberValue(3)).iterator();
        boolean result = expr.findMatch(leftIter, rightIter);
        assertFalse(result);

        leftIter = Arrays.asList(new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)).iterator();
        result = expr.findMatch(leftIter, rightIter);
        assertFalse(result);

        leftIter = Arrays.asList(new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)).iterator();
        HashSet rightSet = new HashSet();
        rightSet.add(new CoreNumberValue(2));
        rightSet.add(new CoreNumberValue(1));
        rightSet.add(new CoreNumberValue(3));
        rightIter = rightSet.iterator();
        result = expr.findMatch(leftIter, rightIter);
        assertTrue(result);

        leftIter = new SingletonIterator(new CoreNumberValue(1));
        rightIter = Arrays.asList(new CoreNumberValue(2), new CoreNumberValue(1), new CoreNumberValue(3)).iterator();
        result = expr.findMatch(leftIter, rightIter);
        assertFalse(result);

        leftIter = Arrays.asList(new CoreNumberValue(1), new CoreNumberValue(2), new CoreNumberValue(3)).iterator();
        rightIter = new SingletonIterator(new CoreNumberValue(2));
        result = expr.findMatch(leftIter, rightIter);
        assertTrue(result);

        leftIter = Arrays.asList(new CoreStringValue("test1"), new CoreStringValue("test2"), new CoreStringValue("test3")).iterator();
        HashSet rightSet2 = new HashSet();
        rightSet2.add(new CoreStringValue("test2"));
        rightSet2.add(new CoreStringValue("test1"));
        rightSet2.add(new CoreStringValue("test3"));
        rightIter = rightSet2.iterator();
        result = expr.findMatch(leftIter, rightIter);
        assertTrue(result);

        leftIter = Arrays.asList(new CoreStringValue("test1"), new CoreStringValue("test2"), new CoreStringValue("test3")).iterator();
        rightIter = new SingletonIterator(new CoreStringValue("test2"));
        result = expr.findMatch(leftIter, rightIter);
        assertTrue(result);
    }
}