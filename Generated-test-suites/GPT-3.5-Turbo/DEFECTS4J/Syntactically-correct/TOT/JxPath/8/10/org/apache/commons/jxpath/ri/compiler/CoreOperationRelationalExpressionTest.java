
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression testClass;

    @Before
    public void setup() {
        testClass = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(null);
        context.setSingleNodePointer("1");
        evalContext.setSingleNodePointer("2");
        Assert.assertNotNull(computeValue(context));
        context.setSingleNodePointer(2);
        Assert.assertNull(computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Assert.assertEquals(3, testClass.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Assert.assertFalse(testClass.isSymmetric());
    }

    @Test
    public void testComputeWithLeftIteratorAndRightIterator() {
        Iterator leftIterator = createIterator(1, 2, 3);
        Iterator rightIterator = createIterator(2, 3, 4);
        Assert.assertTrue(compute(leftIterator, rightIterator));
        leftIterator = createIterator(1, 2, 3);
        rightIterator = createIterator(4, 5, 6);
        Assert.assertFalse(compute(leftIterator, rightIterator));
    }

    @Test
    public void testComputeWithLeftIteratorAndRightValue() {
        Iterator iterator = createIterator(1, 2, 3);
        Assert.assertFalse(compute(iterator, 4));
        iterator = createIterator(1, 2, 3);
        Assert.assertTrue(compute(iterator, 2));
    }

    @Test
    public void testComputeWithLeftValueAndRightIterator() {
        Iterator iterator = createIterator(1, 2, 3);
        Assert.assertFalse(compute(4, iterator));
        iterator = createIterator(1, 2, 3);
        Assert.assertTrue(compute(2, iterator));
    }

    @Test
    public void testComputeWithComparableLeftAndRightValues() {
        // Test equal values
        List<Integer> comparable = new ArrayList<>();
        comparable.add(1);
        comparable.add(1);
        Assert.assertTrue(compute(comparable.get(0), comparable.get(1)));

        // Test different values
        comparable.set(1, 2);
        Assert.assertFalse(compute(comparable.get(0), comparable.get(1)));

        // Test negative values
        comparable.set(1, -1);
        Assert.assertEquals(-1, computeValue(comparable).intValue());
    }

    @Test
    public void testReduceWithSelfContextObject() {
        SelfContext selfContext = new SelfContext(null, null);
        Assert.assertNull(reduce(selfContext));

        selfContext.setSingleNodePointer(10);
        Assert.assertEquals(10, reduce(selfContext));

        selfContext.reset();
        Collection<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        selfContext.setSingleNodePointer(collection);
        Assert.assertTrue(reduce(selfContext) instanceof Iterator);
    }

    @Test
    public void testReduceWithCollection() {
        Collection<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        Assert.assertTrue(reduce(collection) instanceof Iterator);
    }

    @Test
    public void testContainsMatchWithIterator() {
        Iterator iterator = createIterator(1, 2, 3);
        Assert.assertFalse(containsMatch(iterator, 4));
        iterator = createIterator(1, 2, 3);
        Assert.assertTrue(containsMatch(iterator, 2));
    }

    @Test
    public void testFindMatchWithIterators() {
        Iterator leftIterator = createIterator(1, 2, 3);
        Iterator rightIterator = createIterator(4, 5, 6);
        Assert.assertFalse(findMatch(leftIterator, rightIterator));
        rightIterator = createIterator(2, 3, 4);
        Assert.assertTrue(findMatch(leftIterator, rightIterator));
    }

    private Iterator createIterator(Object... values) {
        Collection<Object> collection = new HashSet<>();
        for (Object value : values) {
            collection.add(value);
        }
        return collection.iterator();
    }
}
