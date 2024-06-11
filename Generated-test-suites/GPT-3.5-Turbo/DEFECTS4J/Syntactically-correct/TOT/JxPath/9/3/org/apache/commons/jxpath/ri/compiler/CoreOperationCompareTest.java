
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        CoreOperationCompare operationCompare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean isSymmetric() {
                return true;
            }
        };

        // Typical test case - when both values are same
        // l and r both are null
        Assert.assertTrue(operationCompare.equal(context, null, null));

        // AtomicInteger is not equal to null
        Assert.assertFalse(operationCompare.equal(context, new AtomicInteger(5), null));

        // Different instances of the same class - should not be equal
        Assert.assertFalse(operationCompare.equal(context, new Object(), new Object()));
        
        // Different instances of the same class with same values - should be equal
        Assert.assertTrue(operationCompare.equal(context, new Integer(10), new Integer(10)));

        // Collection with same elements - should be equal
        List<String> list1 = Arrays.asList("A", "B", "C");
        List<String> list2 = Arrays.asList("C", "B", "A");
        Assert.assertTrue(operationCompare.equal(context, list1, list2));

        // Different collections - should not be equal
        List<String> list3 = Arrays.asList("A", "B", "C");
        List<String> list4 = Arrays.asList("D", "E", "F");
        Assert.assertFalse(operationCompare.equal(context, list3, list4));

        // Collection and Iterator with same elements - should be equal
        Iterator<String> iterator1 = list1.iterator();
        Assert.assertTrue(operationCompare.equal(context, list1, iterator1));

        // Collection and Iterator with different elements - should not be equal
        List<String> list5 = Arrays.asList("D", "E", "F");
        Iterator<String> iterator2 = list5.iterator();
        Assert.assertFalse(operationCompare.equal(context, list3, iterator2));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operationCompare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean isSymmetric() {
                return true;
            }

            @Override
            protected boolean equal(Object l, Object r) {
                return false;
            }
        };

        // Iterator contains the value - should return true
        List<String> list = Arrays.asList("A", "B", "C");
        Iterator<String> iterator = list.iterator();
        String value = "B";
        Assert.assertTrue(operationCompare.contains(iterator, value));

        // Iterator does not contain the value - should return false
        String value2 = "D";
        Assert.assertFalse(operationCompare.contains(iterator, value2));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operationCompare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean isSymmetric() {
                return true;
            }
        };

        // Match found in both iterators - should return true
        List<String> list1 = Arrays.asList("A", "B", "C");
        List<String> list2 = Arrays.asList("C", "B", "A");
        Iterator<String> iterator1 = list1.iterator();
        Iterator<String> iterator2 = list2.iterator();
        Assert.assertTrue(operationCompare.findMatch(iterator1, iterator2));

        // Match not found in any iterator - should return false
        List<String> list3 = Arrays.asList("A", "B", "C");
        List<String> list4 = Arrays.asList("D", "E", "F");
        Iterator<String> iterator3 = list3.iterator();
        Iterator<String> iterator4 = list4.iterator();
        Assert.assertFalse(operationCompare.findMatch(iterator3, iterator4));
    }

    @Test
    public void testEqualForObject() {
        EvalContext context = new EvalContext();
        CoreOperationCompare operationCompare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean isSymmetric() {
                return true;
            }

            @Override
            protected boolean equal(Object l, Object r) {
                return false;
            }
        };

        // Comparing two Pointers which are equal
        Assert.assertTrue(operationCompare.equal(context, new CoreOperationCompareTest.Pointer(1), new CoreOperationCompareTest.Pointer(1)));

        // Comparing two Pointers which are not equal
        Assert.assertFalse(operationCompare.equal(context, new CoreOperationCompareTest.Pointer(1), new CoreOperationCompareTest.Pointer(2)));

        // Comparing Object and Pointer which are equal
        Assert.assertTrue(operationCompare.equal(context, new Object(), new CoreOperationCompareTest.Pointer(1)));

        // Comparing Object and Pointer which are not equal
        Assert.assertFalse(operationCompare.equal(context, new Object(), new CoreOperationCompareTest.Pointer(2)));
    }

    private static class Pointer {
        private int value;

        Pointer(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }
    }

}
