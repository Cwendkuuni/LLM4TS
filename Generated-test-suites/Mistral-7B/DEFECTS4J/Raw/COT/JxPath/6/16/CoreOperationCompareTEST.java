package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest {

    private CoreOperationCompare comp;
    private EvalContext ctx;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        Expression arg1Value = new LiteralExpression(new Integer(10));
        Expression arg2Value = new LiteralExpression(new Integer(10));
        arg1 = new VariableExpression("arg1", new CoreOperation(new Expression[] { arg1Value }));
        arg2 = new VariableExpression("arg2", new CoreOperation(new Expression[] { arg2Value }));
        comp = new CoreOperationCompare(arg1, arg2);
        ctx = new EvalContext(new InitialContext(new Object[] {}), new SelfContext(new Object[] {}));
    }

    @Test
    public void testEqual() {
        Object l = comp.equal(ctx, arg1, arg2);
        assertTrue("Method equal should return true when comparing equal values", l instanceof Boolean && InfoSetUtil.booleanValue(l));
    }

    @Test
    public void testNotEqual() {
        Object l = comp.equal(ctx, arg1, arg2);
        assertFalse("Method equal should return false when comparing not equal values", l instanceof Boolean && InfoSetUtil.booleanValue(l));

        Object l1 = comp.equal(ctx, arg1, arg1);
        assertTrue("Method equal should return true when comparing the same value", l1 instanceof Boolean && InfoSetUtil.booleanValue(l1));
    }

    @Test
    public void testCompareNulls() {
        Object l = comp.equal(ctx, new LiteralExpression(null), new LiteralExpression(null));
        assertTrue("Method equal should return true when comparing null values", l instanceof Boolean && InfoSetUtil.booleanValue(l));

        Object l1 = comp.equal(ctx, new LiteralExpression(null), new LiteralExpression(new Integer(10)));
        assertFalse("Method equal should return false when comparing null and non-null values", l1 instanceof Boolean && InfoSetUtil.booleanValue(l1));
    }

    @Test
    public void testCompareCollections() {
        Collection<Integer> coll1 = new ArrayList<>();
        coll1.add(1);
        coll1.add(2);
        Collection<Integer> coll2 = new ArrayList<>();
        coll2.add(1);
        coll2.add(2);

        Object l = comp.equal(ctx, new VariableExpression("coll1", new CoreOperation(new Expression[] { new LiteralExpression(coll1), new LiteralExpression("size") })),
            new VariableExpression("coll2", new CoreOperation(new Expression[] { new LiteralExpression(coll2), new LiteralExpression("size") })));
        assertTrue("Method equal should return true when comparing equal collections", l instanceof Boolean && InfoSetUtil.booleanValue(l));

        Collection<Integer> coll3 = new ArrayList<>();
        coll3.add(1);
        coll3.add(3);
        Object l1 = comp.equal(ctx, new VariableExpression("coll1", new CoreOperation(new Expression[] { new LiteralExpression(coll1), new LiteralExpression("size") })),
            new VariableExpression("coll3", new CoreOperation(new Expression[] { new LiteralExpression(coll3), new LiteralExpression("size") })));
        assertFalse("Method equal should return false when comparing not equal collections", l1 instanceof Boolean && InfoSetUtil.booleanValue(l1));
    }

    @Test
    public void testCompareIterators() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Iterator<Integer> it = list.iterator();

        Object l = comp.equal(ctx, new VariableExpression("it", new CoreOperation(new Expression[] { new LiteralExpression(it), new LiteralExpression("hasNext") })),
            new VariableExpression("it", new CoreOperation(new Expression[] { new LiteralExpression(it), new LiteralExpression("hasNext") })));
        assertTrue("Method equal should return true when comparing equal iterators", l instanceof Boolean && InfoSetUtil.booleanValue(l));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareDifferentTypes() {
        Object l = comp.equal(ctx, arg1, new LiteralExpression("Hello World"));
        assertNull("Method equal should return null when comparing different types", l);
    }

    @Test
    public void testFindMatch() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);

        HashSet<Integer> set1 = new HashSet<>(list1);
        HashSet<Integer> set2 = new HashSet<>(list2);

        Object l = comp.findMatch(set1.iterator(), set2.iterator());
        assertTrue("Method findMatch should return true when finding a match", l instanceof Boolean && InfoSetUtil.booleanValue(l));
    }

    @Test
    public void testFindNoMatch() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 4, 5);

        HashSet<Integer> set1 = new HashSet<>(list1);
        HashSet<Integer> set2 = new HashSet<>(list2);

        Object l = comp.findMatch(set1.iterator(), set2.iterator());
        assertFalse("Method findMatch should return false when no match found", l instanceof Boolean && InfoSetUtil.booleanValue(l));
    }

    @Test
    public void testContains() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Iterator<Integer> it = list.iterator();

        Object l = comp.contains(it, new LiteralExpression(1));
        assertTrue("Method contains should return true when finding a match", l instanceof Boolean && InfoSetUtil.booleanValue(l));
    }

    @Test
    public void testContainsNoMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Iterator<Integer> it = list.iterator();

        Object l = comp.contains(it, new LiteralExpression(4));
        assertFalse("Method contains should return false when no match found", l instanceof Boolean && InfoSetUtil.booleanValue(l));
    }
}