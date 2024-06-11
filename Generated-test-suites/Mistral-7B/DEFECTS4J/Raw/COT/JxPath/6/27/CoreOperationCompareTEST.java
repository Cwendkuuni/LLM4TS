import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.jxpath.ri.axes.AxisNames.CHILD;
import static org.apache.commons.jxpath.ri.axes.AxisNames.SELF;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression arg1 = new Expression(new Constant(1));
        Expression arg2 = new Expression(new Constant(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Assert.assertEquals(2, coreOperationCompare.getOperands().length);
    }

    @Test
    public void testEqualMethod_whenBothObjectsAreSame_thenReturnTrue() {
        EvalContext context = new EvalContext();
        Expression left = new Variable("left");
        Expression right = new Variable("right");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        Object result = coreOperationCompare.evaluate(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertTrue((Boolean) result);

        context.setVariable("left", new Constant(1));
        context.setVariable("right", new Constant(1));
        result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);

        context.setVariable("left", new String("test"));
        context.setVariable("right", new String("test"));
        result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testEqualMethod_whenObjectsAreDifferent_thenReturnFalse() {
        EvalContext context = new EvalContext();
        Expression left = new Variable("left");
        Expression right = new Variable("right");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        context.setVariable("left", new Constant(1));
        context.setVariable("right", new Constant(2));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);

        context.setVariable("left", new String("test"));
        context.setVariable("right", new String("test1"));
        result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);

        context.setVariable("left", new Pointer(Arrays.asList(new String[]{"a", "b"})));
        context.setVariable("right", new Pointer(Arrays.asList(new String[]{"c", "d"})));
        result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testEqualMethod_whenLeftIsCollectionAndRightIsSingleElement_thenReturnTrueIfElementsAreEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Variable("left");
        Expression right = new Variable("right");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        context.setVariable("left", new IteratorIterator(Arrays.asList(1, 2, 3).iterator()));
        context.setVariable("right", new Constant(2));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);

        context.setVariable("left", new IteratorIterator(Arrays.asList(1, 2, 3).iterator()));
        context.setVariable("right", new IteratorIterator(Arrays.asList(2).iterator()));
        result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);

        context.setVariable("left", new IteratorIterator(Arrays.asList(1, 2, 3).iterator()));
        context.setVariable("right", new IteratorIterator(Arrays.asList(2, 3).iterator()));
        result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);

        context.setVariable("left", new IteratorIterator(Arrays.asList(1, 2, 3).iterator()));
        context.setVariable("right", new IteratorIterator(Arrays.asList(1, 2, 3).iterator()));
        result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testEqualMethod_whenLeftIsSingleElementAndRightIsCollection_thenReturnTrueIfElementsAreEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Variable("left");
        Expression right = new Variable("right");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        context.setVariable("left", new Constant(2));
        context.setVariable("right", new IteratorIterator(Arrays.asList(2).iterator()));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);

        context.setVariable("left", new Constant(2));
        context.setVariable("right", new IteratorIterator(Arrays.asList(1, 2, 3).iterator()));
        result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);

        context.setVariable("left", new IteratorIterator(Arrays.asList(1, 2, 3).iterator()));
        context.setVariable("right", new Constant(2));
        result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);

        context.setVariable("left", new IteratorIterator(Arrays.asList(1, 2, 3).iterator()));
        context.setVariable("right", new IteratorIterator(Arrays.asList(1, 2, 3).iterator()));
        result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testEqualMethod_whenBothAreCollections_thenReturnTrueIfAllElementsInLeftArePresentInRight() {
        EvalContext context = new EvalContext();
        Expression left = new Variable("left");
        Expression right = new Variable("right");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        List<Object> leftCollection = Arrays.asList(1, 2, 3);
        List<Object> rightCollection = Arrays.asList(1, 2, 3, 4);
        context.setVariable("left", new IteratorIterator(leftCollection.iterator()));
        context.setVariable("right", new IteratorIterator(rightCollection.iterator()));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);

        leftCollection = Arrays.asList(1, 2, 3);
        rightCollection = Arrays.asList(1, 2);
        context.setVariable("left", new IteratorIterator(leftCollection.iterator()));
        context.setVariable("right", new IteratorIterator(rightCollection.iterator()));
        result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);

        leftCollection = Arrays.asList(1, 2, 3);
        rightCollection = Arrays.asList(1, 2, 3, 4, 5);
        context.setVariable("left", new IteratorIterator(leftCollection.iterator()));
        context.setVariable("right", new IteratorIterator(rightCollection.iterator()));
        result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testEqualMethod_whenLeftIsCollectionAndRightIsEmpty_thenReturnFalse() {
        EvalContext context = new EvalContext();
        Expression left = new Variable("left");
        Expression right = new Variable("right");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        List<Object> leftCollection = Collections.singletonList(1);
        context.setVariable("left", new IteratorIterator(leftCollection.iterator()));
        context.setVariable("right", new Constant(Collections.emptyList()));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualMethod_whenLeftIsCollectionAndRightIsNotIterable_thenThrowIllegalArgumentException() {
        EvalContext context = new EvalContext();
        Expression left = new Variable("left");
        Expression right = new Constant(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        List<Object> leftCollection = Arrays.asList(1, 2, 3);
        context.setVariable("left", new IteratorIterator(leftCollection.iterator()));
        coreOperationCompare.evaluate(context);
    }

    @Test
    public void testContainsMethod_whenIteratorContainsElement_thenReturnTrue() {
        EvalContext context = new EvalContext();
        Expression it = new Variable("it");
        Expression value = new Variable("value");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(it, value);

        Iterator<Object> itterator = Arrays.asList(1, 2, 3).iterator();
        context.setVariable("it", new IteratorIterator(itterator));
        context.setVariable("value", new Constant(2));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testContainsMethod_whenIteratorDoesNotContainElement_thenReturnFalse() {
        EvalContext context = new EvalContext();
        Expression it = new Variable("it");
        Expression value = new Variable("value");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(it, value);

        Iterator<Object> itterator = Arrays.asList(1, 2, 3).iterator();
        context.setVariable("it", new IteratorIterator(itterator));
        context.setVariable("value", new Constant(4));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testFindMatchMethod_whenLeftIteratorContainsElementOfRightIterator_thenReturnTrue() {
        EvalContext context = new EvalContext();
        Expression lit = new Variable("lit");
        Expression rit = new Variable("rit");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        Iterator<Object> leftIterator = Arrays.asList(1, 2, 3).iterator();
        context.setVariable("lit", new IteratorIterator(leftIterator));
        Iterator<Object> rightIterator = Arrays.asList(2, 3, 4).iterator();
        context.setVariable("rit", new IteratorIterator(rightIterator));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testFindMatchMethod_whenLeftIteratorDoesNotContainAnyElementOfRightIterator_thenReturnFalse() {
        EvalContext context = new EvalContext();
        Expression lit = new Variable("lit");
        Expression rit = new Variable("rit");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        Iterator<Object> leftIterator = Arrays.asList(1, 2, 3).iterator();
        context.setVariable("lit", new IteratorIterator(leftIterator));
        Iterator<Object> rightIterator = Arrays.asList(4, 5, 6).iterator();
        context.setVariable("rit", new IteratorIterator(rightIterator));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testFindMatchMethod_whenLeftIteratorContainsAnElementWhichIsSubsetOfAnElementInRightIterator_thenReturnTrue() {
        EvalContext context = new EvalContext();
        Expression lit = new Variable("lit");
        Expression rit = new Variable("rit");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        Iterator<List<Object>> leftIterator = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4)).iterator();
        context.setVariable("lit", new IteratorIterator(leftIterator));
        Iterator<Object> rightIterator = Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6)).iterator();
        context.setVariable("rit", new IteratorIterator(rightIterator));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testFindMatchMethod_whenLeftIteratorContainsAnElementWhichIsSuperSetOfAnElementInRightIterator_thenReturnTrue() {
        EvalContext context = new EvalContext();
        Expression lit = new Variable("lit");
        Expression rit = new Variable("rit");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        Iterator<List<Object>> leftIterator = Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(3, 4)).iterator();
        context.setVariable("lit", new IteratorIterator(leftIterator));
        Iterator<Object> rightIterator = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4)).iterator();
        context.setVariable("rit", new IteratorIterator(rightIterator));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testFindMatchMethod_whenLeftIteratorDoesNotContainAnyElementWhichIsSubsetOfOrSuperSetOfAnElementInRightIterator_thenReturnFalse() {
        EvalContext context = new EvalContext();
        Expression lit = new Variable("lit");
        Expression rit = new Variable("rit");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        Iterator<List<Object>> leftIterator = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4)).iterator();
        context.setVariable("lit", new IteratorIterator(leftIterator));
        Iterator<Object> rightIterator = Arrays.asList(Arrays.asList(5, 6), Arrays.asList(7, 8)).iterator();
        context.setVariable("rit", new IteratorIterator(rightIterator));
        Object result = coreOperationCompare.evaluate(context);
        Assert.assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMatchMethod_whenLeftIteratorIsEmpty_thenThrowIllegalArgumentException() {
        EvalContext context = new EvalContext();
        Expression lit = new Variable("lit");
        Expression rit = new Variable("rit");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(lit, rit);

        Iterator<Object> leftIterator = Collections.emptyIterator();
        context.setVariable("lit", new IteratorIterator(leftIterator));
        Iterator<Object> rightIterator = Arrays.asList(1, 2, 3).iterator();
        context.setVariable("rit", new IteratorIterator(rightIterator));
        coreOperationCompare.evaluate(context);
    }
}