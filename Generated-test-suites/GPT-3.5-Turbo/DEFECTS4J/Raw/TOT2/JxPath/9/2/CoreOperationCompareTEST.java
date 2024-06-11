import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();

        // Test equal values
        assertTrue(coreOperationCompare.equal(context, left, right));

        // Test unequal values
        assertFalse(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return null;
            }
        };
        Object value = new Object();

        // Test when iterator is empty
        assertFalse(coreOperationCompare.contains(it, value));

        // Test when iterator contains value
        it = new Iterator() {
            private boolean hasReturned = false;
            @Override
            public boolean hasNext() {
                return !hasReturned;
            }
            @Override
            public Object next() {
                hasReturned = true;
                return value;
            }
        };
        assertTrue(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return null;
            }
        };
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return null;
            }
        };

        // Test when both iterators are empty
        assertFalse(coreOperationCompare.findMatch(lit, rit));

        // Test when both iterators contain matching values
        lit = new Iterator() {
            private int count = 0;
            @Override
            public boolean hasNext() {
                return count < 1;
            }
            @Override
            public Object next() {
                count++;
                return new Object();
            }
        };
        rit = new Iterator() {
            private int count = 0;
            @Override
            public boolean hasNext() {
                return count < 1;
            }
            @Override
            public Object next() {
                count++;
                return new Object();
            }
        };
        assertTrue(coreOperationCompare.findMatch(lit, rit));
    }

    // Add more test cases for other methods if needed
}