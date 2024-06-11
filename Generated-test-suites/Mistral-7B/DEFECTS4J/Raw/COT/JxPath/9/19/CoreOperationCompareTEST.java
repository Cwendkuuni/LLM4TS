import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression(new Constant(1));
        Expression arg2 = new Expression(new Constant(1));
        CoreOperationCompare cop = new CoreOperationCompare(arg1, arg2);
        Assert.assertEquals(2, cop.getPrecedence());
        Assert.assertTrue(cop.isSymmetric());
    }

    @Test
    public void testEqual_sameObjects() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant(1), new Constant(1));
        EvalContext context = new EvalContext();
        assertTrue(cop.equal(context, null, null));
        assertTrue(cop.equal(context, new Constant(1), new Constant(1)));
    }

    @Test
    public void testEqual_differentObjects() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant(1), new Constant(2));
        EvalContext context = new EvalContext();
        assertFalse(cop.equal(context, null, null));
        assertFalse(cop.equal(context, new Constant(1), new Constant(2)));
    }

    @Test
    public void testEqual_objectsWithSameValue() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant("abc"), new Constant("abc"));
        EvalContext context = new EvalContext();
        assertTrue(cop.equal(context, null, null));
        assertTrue(cop.equal(context, new Constant("abc"), new Constant("abc")));
    }

    @Test
    public void testEqual_objectsWithDifferentValues() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant("abc"), new Constant("def"));
        EvalContext context = new EvalContext();
        assertFalse(cop.equal(context, null, null));
        assertFalse(cop.equal(context, new Constant("abc"), new Constant("def")));
    }

    @Test
    public void testEqual_objectsWithNullValues() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant(null), new Constant(null));
        EvalContext context = new EvalContext();
        assertTrue(cop.equal(context, null, null));
        assertTrue(cop.equal(context, new Constant(null), new Constant(null)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqual_mismatchedTypes() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant(1), new Constant("abc"));
    }

    @Test
    public void testContains() {
        Iterator it = Collections.singletonList(new Constant(1)).iterator();
        CoreOperationCompare cop = new CoreOperationCompare(it, new Constant(1));
        assertTrue(cop.contains(it, new Constant(1)));
        assertFalse(cop.contains(it, new Constant(2)));
    }

    @Test
    public void testFindMatch() {
        Iterator lIt = Collections.singletonList(new Constant(1)).iterator();
        Iterator rIt = Collections.singletonList(new Constant(1)).iterator();
        CoreOperationCompare cop = new CoreOperationCompare(lIt, rIt);
        assertTrue(cop.findMatch(lIt, rIt));
    }

    @Test
    public void testFindMatch_noMatch() {
        Iterator lIt = Collections.singletonList(new Constant(1)).iterator();
        Iterator rIt = Collections.singletonList(new Constant(2)).iterator();
        CoreOperationCompare cop = new CoreOperationCompare(lIt, rIt);
        assertFalse(cop.findMatch(lIt, rIt));
    }

    @Test
    public void testFindMatch_iteratorsWithMultipleElements() {
        List list = Collections.singletonList(Collections.singletonList(new Constant(1)));
        Iterator lIt = list.iterator();
        Iterator rIt = Collections.singletonList(new Constant(1)).iterator();
        CoreOperationCompare cop = new CoreOperationCompare(lIt, rIt);
        assertTrue(cop.findMatch(lIt, rIt));
    }

    @Test
    public void testFindMatch_iteratorsWithNoMatchingElements() {
        List list = Collections.singletonList(Collections.singletonList(new Constant(1)));
        Iterator lIt = list.iterator();
        Iterator rIt = Collections.singletonList(new Constant(2)).iterator();
        CoreOperationCompare cop = new CoreOperationCompare(lIt, rIt);
        assertFalse(cop.findMatch(lIt, rIt));
    }

    @Test
    public void testFindMatch_iteratorsWithIteratorsAsElements() {
        List list = Collections.singletonList(Collections.singletonList(new IteratorTestObject()));
        Iterator lIt = list.iterator();
        Iterator rIt = Collections.singletonList(new IteratorTestObject()).iterator();
        CoreOperationCompare cop = new CoreOperationCompare(lIt, rIt);
        assertTrue(cop.findMatch(lIt, rIt));
    }

    @Test
    public void testFindMatch_iteratorsWithNoMatchingIterators() {
        List list = Collections.singletonList(Collections.singletonList(new IteratorTestObject()));
        Iterator lIt = list.iterator();
        Iterator rIt = Collections.singletonList(new IteratorTestObjectWithDifferentValue()).iterator();
        CoreOperationCompare cop = new CoreOperationCompare(lIt, rIt);
        assertFalse(cop.findMatch(lIt, rIt));
    }

    @Test
    public void testEqual_pointerValues() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant(new Pointer("a")), new Constant(new Pointer("a")));
        EvalContext context = new EvalContext();
        assertTrue(cop.equal(context, null, null));
        assertTrue(cop.equal(context, new Constant(new Pointer("a")), new Constant(new Pointer("a"))));
    }

    @Test
    public void testEqual_pointersWithDifferentValues() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant(new Pointer("a")), new Constant(new Pointer("b")));
        EvalContext context = new EvalContext();
        assertFalse(cop.equal(context, null, null));
        assertFalse(cop.equal(context, new Constant(new Pointer("a")), new Constant(new Pointer("b"))));
    }

    @Test
    public void testEqual_pointersWithNullValues() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant(new Pointer(null)), new Constant(new Pointer(null)));
        EvalContext context = new EvalContext();
        assertTrue(cop.equal(context, null, null));
        assertTrue(cop.equal(context, new Constant(new Pointer(null)), new Constant(new Pointer(null))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqual_pointersWithMismatchedTypes() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant(new Pointer(1)), new Constant(new String("abc")));
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare cop = new CoreOperationCompare(new Constant(1), new Constant(1));
        assertTrue(cop.isSymmetric());
    }

    private static class IteratorTestObject {
        @Override
        public boolean equals(Object o) {
            return true;
        }

        @Override
        public String toString() {
            return "1";
        }
    }

    private static class IteratorTestObjectWithDifferentValue extends IteratorTestObject {
        @Override
        public String toString() {
            return "2";
        }
    }

}