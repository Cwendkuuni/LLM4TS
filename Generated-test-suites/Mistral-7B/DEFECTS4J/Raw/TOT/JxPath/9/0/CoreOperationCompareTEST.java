import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axis.Axis;
import org.apache.commons.jxpath.ri.axis.AxisCall;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.eval.Expression;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoreOperationCompareTest {

    private JXPathContext context;
    private CoreOperationCompare compare;

    @Before
    public void setUp() {
        context = JXPathContext.newContext(new Object[]{"apple", 1, 2, "orange", new Integer[0]});
        compare = new CoreOperationCompare(
            new ExpressionImpl("1"),
            new ExpressionImpl("1")
        );
    }

    @Test
    public void testBooleanEqual() {
        Expression left = new Variable("booleanTrue");
        Expression right = new Variable("booleanTrue");
        CoreOperationCompare compareBoolean = new CoreOperationCompare(left, right);
        boolean result = compareBoolean.compute(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testBooleanNotEqual() {
        Expression left = new Variable("booleanTrue");
        Expression right = new Variable("booleanFalse");
        CoreOperationCompare compareBoolean = new CoreOperationCompare(left, right);
        boolean result = compareBoolean.compute(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testBooleanEqualNull() {
        Expression left = new Variable("booleanTrue");
        Expression right = new Variable("null");
        CoreOperationCompare compareBoolean = new CoreOperationCompare(left, right);
        boolean result = compareBoolean.compute(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testBooleanNotEqualNull() {
        Expression left = new Variable("null");
        Expression right = new Variable("booleanTrue");
        CoreOperationCompare compareBoolean = new CoreOperationCompare(left, right);
        boolean result = compareBoolean.compute(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testNumberEqual() {
        Expression left = new Variable("numberOne");
        Expression right = new Variable("numberOne");
        CoreOperationCompare compareNumber = new CoreOperationCompare(left, right);
        double result = compareNumber.compute(context);
        Assert.assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testNumberNotEqual() {
        Expression left = new Variable("numberOne");
        Expression right = new Variable("numberTwo");
        CoreOperationCompare compareNumber = new CoreOperationCompare(left, right);
        double result = compareNumber.compute(context);
        Assert.assertNotEquals(1.0, result, 0.0);
    }

    @Test
    public void testStringEqual() {
        Expression left = new Variable("stringApple");
        Expression right = new Variable("stringApple");
        CoreOperationCompare compareString = new CoreOperationCompare(left, right);
        String result = (String) compareString.compute(context);
        Assert.assertEquals("apple", result);
    }

    @Test
    public void testStringNotEqual() {
        Expression left = new Variable("stringApple");
        Expression right = new Variable("stringOrange");
        CoreOperationCompare compareString = new CoreOperationCompare(left, right);
        String result = (String) compareString.compute(context);
        Assert.assertNotEquals("apple", result);
    }

    @Test
    public void testCollectionEqual() {
        Expression left = new Variable("collectionApple");
        Expression right = new Variable("collectionApple");
        CoreOperationCompare compareCollection = new CoreOperationCompare(left, right);
        Iterator leftIterator = (Iterator) compareCollection.compute(context);
        Iterator rightIterator = (Iterator) compareCollection.compute(context);
        Assert.assertTrue(Iterators.areEqual(leftIterator, rightIterator));
    }

    @Test
    public void testCollectionNotEqual() {
        Expression left = new Variable("collectionApple");
        Expression right = new Variable("collectionOrange");
        CoreOperationCompare compareCollection = new CoreOperationCompare(left, right);
        Iterator leftIterator = (Iterator) compareCollection.compute(context);
        Iterator rightIterator = (Iterator) compareCollection.compute(context);
        Assert.assertFalse(Iterators.areEqual(leftIterator, rightIterator));
    }

    @Test
    public void testCollectionContain() {
        Expression left = new Variable("collectionApple");
        Expression right = new Variable("numberOne");
        CoreOperationCompare compareCollection = new CoreOperationCompare(left, right);
        Iterator leftIterator = (Iterator) compareCollection.compute(context);
        boolean result = false;
        if (leftIterator.hasNext()) {
            Object next = leftIterator.next();
            if (next instanceof Number && ((Number) next).intValue() == 1) {
                result = true;
            }
        }
        Assert.assertFalse(result);
    }

    @Test
    public void testCollectionContainWithMatchingValue() {
        Expression left = new Variable("collectionApple");
        Expression right = new Variable("stringApple");
        CoreOperationCompare compareCollection = new CoreOperationCompare(left, right);
        Iterator leftIterator = (Iterator) compareCollection.compute(context);
        boolean result = false;
        if (leftIterator.hasNext()) {
            Object next = leftIterator.next();
            if (next instanceof String && next.equals("apple")) {
                result = true;
            }
        }
        Assert.assertTrue(result);
    }

    @Test
    public void testSelfContextEqual() {
        EvalContext selfContext = new EvalContext();
        Axis axis = Axis.SELF;
        AxisCall selfAxisCall = new AxisCall(axis);
        selfContext.setAxis(selfAxisCall);
        selfContext.setCurrentNode(new Pointer(context, 1));
        Expression left = new Variable("selfContext");
        Expression right = new Variable("selfContext");
        CoreOperationCompare compareSelfContext = new CoreOperationCompare(left, right);
        Assert.assertTrue(compareSelfContext.compute(selfContext));
    }

    @Test
    public void testSelfContextNotEqual() {
        EvalContext selfContext1 = new EvalContext();
        EvalContext selfContext2 = new EvalContext();
        Axis axis = Axis.SELF;
        AxisCall selfAxisCall1 = new AxisCall(axis);
        AxisCall selfAxisCall2 = new AxisCall(axis);
        selfContext1.setAxis(selfAxisCall1);
        selfContext1.setCurrentNode(new Pointer(context, 1));
        selfContext2.setAxis(selfAxisCall2);
        selfContext2.setCurrentNode(new Pointer(context, 2));
        Expression left = new Variable("selfContext1");
        Expression right = new Variable("selfContext2");
        CoreOperationCompare compareSelfContext = new CoreOperationCompare(left, right);
        Assert.assertFalse(compareSelfContext.compute(selfContext1));
    }

    @Test
    public void testInitialContextEqual() {
        EvalContext initialContext = new EvalContext();
        Expression left = new Variable("initialContext");
        Expression right = new Variable("initialContext");
        CoreOperationCompare compareInitialContext = new CoreOperationCompare(left, right);
        Assert.assertTrue(compareInitialContext.compute(initialContext));
    }

    @Test
    public void testInitialContextNotEqual() {
        EvalContext initialContext1 = new EvalContext();
        EvalContext initialContext2 = new EvalContext();
        Expression left = new Variable("initialContext1");
        Expression right = new Variable("initialContext2");
        CoreOperationCompare compareInitialContext = new CoreOperationCompare(left, right);
        Assert.assertFalse(compareInitialContext.compute(initialContext1));
    }
}