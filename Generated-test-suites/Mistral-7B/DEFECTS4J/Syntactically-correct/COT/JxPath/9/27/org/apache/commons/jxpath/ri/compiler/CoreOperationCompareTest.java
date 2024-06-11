import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Assert.assertNotNull(coreOperationCompare);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        int precedence = coreOperationCompare.getPrecedence();
        Assert.assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean symmetric = coreOperationCompare.isSymmetric();
        Assert.assertTrue(symmetric);
    }

    @Test
    public void testEqualnullContext() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(null));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualSelfContext() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(new SelfContext()), new ConstantExpression(new SelfContext()));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualInitialContext() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(new InitialContext()), new ConstantExpression(new InitialContext()));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualBooleanTrueTrue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(true), new ConstantExpression(true));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualBooleanTrueFalse() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(true), new ConstantExpression(false));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualBooleanFalseTrue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(false), new ConstantExpression(true));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualBooleanFalseFalse() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(false), new ConstantExpression(false));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualNumberZeroZero() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(0.0), new ConstantExpression(0.0));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualNumberZeroOne() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(0.0), new ConstantExpression(1.0));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualNumberOneZero() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(1.0), new ConstantExpression(0.0));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualNumberNaN() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualNumberNaNNotNaN() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(Double.NaN), new ConstantExpression(0.0));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualStringEmptyStringEmptyString() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(""), new ConstantExpression(""));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualStringEmptyStringNonEmptyString() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(""), new ConstantExpression("Hello World"));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualStringNonEmptyStringEmptyString() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression("Hello World"), new ConstantExpression(""));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualStringNonEmptyStringNonEmptyString() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression("Hello World"), new ConstantExpression("Hello World"));
        boolean result = coreOperationCompare.equal(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testContainsIteratorsEqual() {
        EvalContext context = new EvalContext();
        Object[] array1 = { 1, 2, 3 };
        Object[] array2 = { 1, 2, 3 };
        Iterator it1 = Iterators.asJavaIterator(new IteratorChain(Iterators.forArray(array1)));
        Iterator it2 = Iterators.asJavaIterator(new IteratorChain(Iterators.forArray(array2)));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(it1), new ConstantExpression(it2));
        boolean result = coreOperationCompare.contains(context, null, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testContainsIteratorsNotEqual() {
        EvalContext context = new EvalContext();
        Object[] array1 = { 1, 2, 3 };
        Object[] array2 = { 3, 2, 1 };
        Iterator it1 = Iterators.asJavaIterator(new IteratorChain(Iterators.forArray(array1)));
        Iterator it2 = Iterators.asJavaIterator(new IteratorChain(Iterators.forArray(array2)));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(it1), new ConstantExpression(it2));
        boolean result = coreOperationCompare.contains(context, null, null);
        Assert.assertFalse(result);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNull() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(null), new ConstantExpression(null));
        coreOperationCompare.equal(context, null, null);
    }

    @Test(expected = AssertionError.class)
    public void testFindMatchNull() {
        EvalContext context = new EvalContext();
        Object[] array1 = { 1, 2, 3 };
        Object[] array2 = null;
        Iterator it1 = Iterators.asJavaIterator(new IteratorChain(Iterators.forArray(array1)));
        Iterator it2 = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(it1), new ConstantExpression(it2));
        coreOperationCompare.findMatch(context, null, null);
    }

}