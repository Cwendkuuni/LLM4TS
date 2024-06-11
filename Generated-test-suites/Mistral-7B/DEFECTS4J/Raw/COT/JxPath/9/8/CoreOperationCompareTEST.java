import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        boolean isSymmetric = coreOperationCompare.isSymmetric();
        Assert.assertTrue(isSymmetric);
    }

    @Test
    public void testEqual_sameObjects() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Object l = new Object();
        Object r = l;
        boolean result = coreOperationCompare.equal(context, null, null, l, r);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_differentObjects() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Object l = new Object();
        Object r = new Object();
        boolean result = coreOperationCompare.equal(context, null, null, l, r);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_booleanTrueTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        boolean l = true;
        boolean r = true;
        boolean result = coreOperationCompare.equal(context, null, null, l, r);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_booleanFalseFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        boolean l = false;
        boolean r = false;
        boolean result = coreOperationCompare.equal(context, null, null, l, r);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_booleanTrueFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        boolean l = true;
        boolean r = false;
        boolean result = coreOperationCompare.equal(context, null, null, l, r);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_numberEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Number l = 1.0;
        Number r = 1.0;
        boolean result = coreOperationCompare.equal(context, null, null, l, r);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_numberNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Number l = 1.0;
        Number r = 2.0;
        boolean result = coreOperationCompare.equal(context, null, null, l, r);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_stringEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        String l = "test";
        String r = "test";
        boolean result = coreOperationCompare.equal(context, null, null, l, r);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_stringNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        String l = "test";
        String r = "test2";
        boolean result = coreOperationCompare.equal(context, null, null, l, r);
        Assert.assertFalse(result);
    }

    @Test
    public void testContains_emptyCollection_valuePresent() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection collection = new HashSet();
        Object value = new Object();
        boolean result = coreOperationCompare.contains(context, collection.iterator(), value);
        Assert.assertFalse(result);
    }

    @Test
    public void testContains_emptyCollection_valueAbsent() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection collection = new HashSet();
        Object value = new Object();
        boolean result = coreOperationCompare.contains(context, collection.iterator(), value);
        Assert.assertFalse(result);
    }

    @Test
    public void testContains_collectionWithOneValue_valuePresent() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection collection = new HashSet();
        collection.add(new Object());
        Object value = new Object();
        boolean result = coreOperationCompare.contains(context, collection.iterator(), value);
        Assert.assertTrue(result);
    }

    @Test
    public void testContains_collectionWithOneValue_valueAbsent() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection collection = new HashSet();
        collection.add(new Object());
        Object value = new Object();
        boolean result = coreOperationCompare.contains(context, collection.iterator(), value);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch_emptySet_valuePresent() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection collection = new HashSet();
        Object value = new Object();
        boolean result = coreOperationCompare.findMatch(collection.iterator(), collection.iterator());
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch_emptySet_valueAbsent() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection collection = new HashSet();
        Object value = new Object();
        boolean result = coreOperationCompare.findMatch(collection.iterator(), collection.iterator());
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch_twoSetsWithIdenticalValues_valuePresent() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection set1 = new HashSet();
        Collection set2 = new HashSet();
        set1.add(new Object());
        set2.add(new Object());
        boolean result = coreOperationCompare.findMatch(set1.iterator(), set2.iterator());
        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch_twoSetsWithIdenticalValues_valueAbsent() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection set1 = new HashSet();
        Collection set2 = new HashSet();
        set1.add(new Object());
        set2.add(new Object());
        Object value = new Object();
        boolean result = coreOperationCompare.findMatch(set1.iterator(), set2.iterator());
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMatch_nonIterableObjects_left() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Object l = new Object();
        Collection collection = new HashSet();
        boolean result = coreOperationCompare.findMatch(collection.iterator(), () -> l);
        Assert.fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMatch_nonIterableObjects_right() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection collection = new HashSet();
        Object r = new Object();
        boolean result = coreOperationCompare.findMatch(collection.iterator(), () -> r);
        Assert.fail("IllegalArgumentException expected");
    }

    @Test(expected = NullPointerException.class)
    public void testFindMatch_nullIterator() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection collection = new HashSet();
        boolean result = coreOperationCompare.findMatch(null, collection.iterator());
        Assert.fail("NullPointerException expected");
    }

    @Test(expected = NullPointerException.class)
    public void testFindMatch_nullIterator2() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Collection collection = new HashSet();
        boolean result = coreOperationCompare.findMatch(collection.iterator(), null);
        Assert.fail("NullPointerException expected");
    }
}