import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression(new Object() {});
        Expression arg2 = new Expression(new Object() {});
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Assert.assertNotNull(coreOperationCompare);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new Object() {}), new Expression(new Object() {}));
        int precedence = coreOperationCompare.getPrecedence();
        Assert.assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new Object() {}), new Expression(new Object() {}));
        boolean isSymmetric = coreOperationCompare.isSymmetric();
        Assert.assertTrue(isSymmetric);
    }

    @Test
    public void testEqualNullContext() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new Object() {}), new Expression(new Object() {}));
        Assert.assertFalse(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testEqualSameObjects() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new Object() {}), new Expression(new Object() {}));
        Object l = new Object();
        Object r = l;
        Assert.assertTrue(coreOperationCompare.equal(context, new Expression(l), new Expression(r)));
    }

    @Test
    public void testEqualBooleanValues() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(Boolean.TRUE), new Expression(Boolean.TRUE));
        Assert.assertTrue(coreOperationCompare.equal(context, new Expression(Boolean.TRUE), new Expression(Boolean.TRUE)));
        CoreOperationCompare coreOperationCompare2 = new CoreOperationCompare(new Expression(Boolean.FALSE), new Expression(Boolean.TRUE));
        Assert.assertFalse(coreOperationCompare2.equal(context, new Expression(Boolean.FALSE), new Expression(Boolean.TRUE)));
    }

    @Test
    public void testEqualDifferentDataTypes() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(Integer.valueOf(1)), new Expression(Boolean.TRUE));
        Assert.assertFalse(coreOperationCompare.equal(context, new Expression(Integer.valueOf(1)), new Expression(Boolean.TRUE)));
    }

    @Test
    public void testEqualNullValue() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(Integer.valueOf(1)), new Expression(null));
        Assert.assertFalse(coreOperationCompare.equal(context, new Expression(Integer.valueOf(1)), new Expression(null)));
    }

    @Test
    public void testEqualNaN() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(Double.NaN), new Expression(Double.NaN));
        Assert.assertTrue(coreOperationCompare.equal(context, new Expression(Double.NaN), new Expression(Double.NaN)));
    }

    @Test
    public void testEqualStringValues() {
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression("String1"), new Expression("String1"));
        Assert.assertTrue(coreOperationCompare.equal(context, new Expression("String1"), new Expression("String1")));
        CoreOperationCompare coreOperationCompare2 = new CoreOperationCompare(new Expression("String1"), new Expression("String2"));
        Assert.assertFalse(coreOperationCompare2.equal(context, new Expression("String1"), new Expression("String2")));
    }

    @Test
    public void testEqualIteratorsWithMatchingElements() {
        EvalContext context = new EvalContext();
        Collection collection1 = new ArrayList();
        collection1.add(new Object());
        collection1.add(new Object());
        Collection collection2 = new ArrayList();
        collection2.add(new Object());
        collection2.add(new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(collection1.iterator()), new Expression(collection2.iterator()));
        Iterator iterator1 = collection1.iterator();
        Iterator iterator2 = collection2.iterator();
        Assert.assertTrue(coreOperationCompare.equal(context, new Expression(iterator1), new Expression(iterator2)));
        while (iterator1.hasNext()) {
            iterator1.next();
            iterator2.next();
        }
    }

    @Test
    public void testEqualIteratorsWithoutMatchingElements() {
        EvalContext context = new EvalContext();
        Collection collection1 = new ArrayList();
        collection1.add(new Object());
        Collection collection2 = new ArrayList();
        collection2.add(new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(collection1.iterator()), new Expression(collection2.iterator()));
        Iterator iterator1 = collection1.iterator();
        Iterator iterator2 = collection2.iterator();
        Assert.assertFalse(coreOperationCompare.equal(context, new Expression(iterator1), new Expression(iterator2)));
    }

    @Test
    public void testContainsNullIterator() {
        EvalContext context = new EvalContext();
        Iterator nullIterator = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(new ArrayList().iterator()), nullIterator);
        Assert.assertFalse(coreOperationCompare.contains(nullIterator, new Object()));
    }

    @Test
    public void testContainsMatchingElements() {
        EvalContext context = new EvalContext();
        Collection collection = new ArrayList();
        collection.add(new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(collection.iterator()), new Expression(new Object()));
        Iterator iterator = collection.iterator();
        Assert.assertTrue(coreOperationCompare.contains(iterator, new Object()));
    }

    @Test
    public void testFindMatchNullIterator1() {
        EvalContext context = new EvalContext();
        Iterator nullIterator1 = null;
        Iterator iterator2 = new ArrayList().iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(nullIterator1, iterator2);
        Assert.assertFalse(coreOperationCompare.findMatch(nullIterator1, iterator2));
    }

    @Test
    public void testFindMatchNullIterator2() {
        EvalContext context = new EvalContext();
        Iterator iterator1 = new ArrayList().iterator();
        Iterator nullIterator2 = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(iterator1, nullIterator2);
        Assert.assertFalse(coreOperationCompare.findMatch(iterator1, nullIterator2));
    }

    @Test
    public void testFindMatchMatchingElements() {
        EvalContext context = new EvalContext();
        Collection collection1 = new ArrayList();
        collection1.add(new Object());
        collection1.add(new Object());
        Collection collection2 = new ArrayList();
        collection2.add(new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(collection1.iterator(), collection2.iterator());
        Assert.assertTrue(coreOperationCompare.findMatch(collection1.iterator(), collection2.iterator()));
    }

    @Test
    public void testFindMatchNoMatchingElements() {
        EvalContext context = new EvalContext();
        Collection collection1 = new ArrayList();
        collection1.add(new Object());
        collection1.add(new Object());
        Collection collection2 = new ArrayList();
        collection2.add(new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(collection1.iterator(), collection2.iterator());
        Assert.assertFalse(coreOperationCompare.findMatch(collection1.iterator(), collection2.iterator()));
    }

}