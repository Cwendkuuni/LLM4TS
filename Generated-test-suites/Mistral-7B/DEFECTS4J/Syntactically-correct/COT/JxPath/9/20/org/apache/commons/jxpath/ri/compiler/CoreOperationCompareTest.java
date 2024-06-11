import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest extends CoreOperationTest {

    @Test
    public void testCompareBooleanValues() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(true),
                new CoreConstantExpression(true));
        assertTrue(compare.compute(context, args));

        compare = new CoreOperationCompare(
                new CoreConstantExpression(true),
                new CoreConstantExpression(false));
        assertFalse(compare.compute(context, args));
    }

    @Test
    public void testCompareNumbers() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(1.0),
                new CoreConstantExpression(1.0));
        assertTrue(compare.compute(context, args));

        compare = new CoreOperationCompare(
                new CoreConstantExpression(1.0),
                new CoreConstantExpression(2.0));
        assertFalse(compare.compute(context, args));

        compare = new CoreOperationCompare(
                new CoreConstantExpression(Double.NaN),
                new CoreConstantExpression(Double.NaN));
        assertTrue(compare.compute(context, args));
    }

    @Test
    public void testCompareStrings() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression("foo"),
                new CoreConstantExpression("foo"));
        assertTrue(compare.compute(context, args));

        compare = new CoreOperationCompare(
                new CoreConstantExpression("foo"),
                new CoreConstantExpression("bar"));
        assertFalse(compare.compute(context, args));
    }

    @Test
    public void testCompareIteratorContainingSameValue() {
        Collection coll = new HashSet();
        coll.add(new Integer(1));
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(new Integer(1)),
                new CoreConstantExpression(coll.iterator()));
        assertTrue(compare.compute(context, args));
    }

    @Test
    public void testCompareIteratorContainingDifferentValues() {
        Collection coll = new HashSet();
        coll.add(new Integer(1));
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(new Integer(1)),
                new CoreConstantExpression(coll.iterator()));
        assertFalse(compare.compute(context, args));

        coll.add(new Integer(2));
        compare = new CoreOperationCompare(
                new CoreConstantExpression(new Integer(1)),
                new CoreConstantExpression(coll.iterator()));
        assertFalse(compare.compute(context, args));
    }

    @Test
    public void testCompareIteratorsWithCommonElement() {
        Collection coll1 = new HashSet();
        coll1.add(new Integer(1));
        Collection coll2 = new HashSet();
        coll2.add(new Integer(1));
        coll2.add(new Integer(2));
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(new Integer(1)),
                new CoreConstantExpression(coll2.iterator()));
        assertTrue(compare.compute(context, args));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareNullValues() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(null),
                new CoreConstantExpression(null));
        compare.compute(context, args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareNullAndNotNullValues() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(null),
                new CoreConstantExpression(new Integer(1)));
        compare.compute(context, args);
    }

    @Test
    public void testComparePointerAndNumber() {
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(new Integer(1)),
                new CoreConstantExpression(context.newVariable("x")));
        Pointer x = context.newVariable("x", new Integer(1));
        args = new Expression[]{new CoreVariableExpression("x"), new CoreConstantExpression(new Integer(1))};
        assertTrue(compare.compute(context, args));
    }

    @Test
    public void testComparePointerAndPointer() {
        Pointer p1 = context.newSingleNodePointer("1");
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(p1),
                new CoreConstantExpression(p1));
        assertTrue(compare.compute(context, args));

        Pointer p2 = context.newSingleNodePointer("2");
        compare = new CoreOperationCompare(
                new CoreConstantExpression(p1),
                new CoreConstantExpression(p2));
        assertFalse(compare.compute(context, args));
    }

    @Test
    public void testComparePointerAndString() {
        Pointer p = context.newSingleNodePointer("foo");
        CoreOperationCompare compare = new CoreOperationCompare(
                new CoreConstantExpression(p),
                new CoreConstantExpression("foo"));
        assertTrue(compare.compute(context, args));
    }
}