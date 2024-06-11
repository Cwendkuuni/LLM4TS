import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Typical use case: Comparing two integers
        int a = 5;
        int b = 10;
        Object[] args = new Object[]{a, b};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        assertEquals(true, expr.computeValue());
        
        // Typical use case: Comparing two strings
        String s1 = "abc";
        String s2 = "abcdef";
        args = new Object[]{s1, s2};
        expr = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(false, expr.computeValue());
        
        // Edge case: Comparing two equal values
        int x = 5;
        int y = 5;
        args = new Object[]{x, y};
        expr = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };
        assertEquals(true, expr.computeValue());
        
        // Edge case: Comparing two equal strings
        String str1 = "abc";
        String str2 = "abc";
        args = new Object[]{str1, str2};
        expr = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        assertEquals(true, expr.computeValue());
        
        // Error scenario: Comparing incompatible types
        Object obj1 = new Object();
        int num = 5;
        args = new Object[]{obj1, num};
        expr = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(false, expr.computeValue());
    }
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        
        assertEquals(3, expr.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        
        assertEquals(false, expr.isSymmetric());
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        
        // Collection to iterator
        Collection<Integer> collection = new ArrayList<Integer>();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        Object result = expr.reduce(collection);
        assertTrue(result instanceof Iterator);
        Iterator<Integer> iterator = (Iterator<Integer>) result;
        assertEquals((Integer)1, iterator.next());
        assertEquals((Integer)2, iterator.next());
        assertEquals((Integer)3, iterator.next());
        assertFalse(iterator.hasNext());
        
        // SelfContext to SingleNodePointer
        EvalContext selfContext = new SelfContext();
        selfContext.setSingleNodePointer(new SingleNodePointer(new Object()));
        result = expr.reduce(selfContext);
        assertTrue(result instanceof SingleNodePointer);
        
        // Object to Object
        Object object = new Object();
        result = expr.reduce(object);
        assertEquals(object, result);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        
        // Typical use case: Iterator contains matching value
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Integer> iterator = list.iterator();
        Object value = 2;
        assertEquals(true, expr.containsMatch(iterator, value));
        
        // Typical use case: Iterator does not contain matching value
        iterator = list.iterator();
        value = 4;
        assertEquals(false, expr.containsMatch(iterator, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        
        // Typical use case: Matching value found
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(4);
        list2.add(5);
        list2.add(2);
        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();
        assertEquals(true, expr.findMatch(iterator1, iterator2));
        
        // Typical use case: No matching value found
        list1 = new ArrayList<Integer>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list2 = new ArrayList<Integer>();
        list2.add(4);
        list2.add(5);
        list2.add(6);
        iterator1 = list1.iterator();
        iterator2 = list2.iterator();
        assertEquals(false, expr.findMatch(iterator1, iterator2));
    }

}