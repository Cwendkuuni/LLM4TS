
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        CoreOperationCompare instance = new CoreOperationCompare(null, null);
        
        // Test equal values
        assertTrue(instance.equal(null, null));
        assertTrue(instance.equal(true, true));
        assertTrue(instance.equal(false, false));
        assertTrue(instance.equal(0, 0));
        assertTrue(instance.equal(3.14, 3.14));
        assertTrue(instance.equal("hello", "hello"));
        
        // Test not equal values
        assertFalse(instance.equal(null, true));
        assertFalse(instance.equal(true, false));
        assertFalse(instance.equal(0, 1));
        assertFalse(instance.equal(3.14, 2.71));
        assertFalse(instance.equal("hello", "world"));
        
        // Test numeric values
        assertTrue(instance.equal(3, 3.0));
        assertTrue(instance.equal(3, 3L));
        assertFalse(instance.equal(3, 3.14));
        assertFalse(instance.equal(3, 4L));
    }
    
    @Test
    public void testContains() {
        CoreOperationCompare instance = new CoreOperationCompare(null, null);
        
        // Test contains method with iterator and object
        assertTrue(instance.contains(new Iterator() {
            int index;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public Object next() {
                return ++index;
            }
        }, 2));
        
        assertFalse(instance.contains(new Iterator() {
            int index;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public Object next() {
                return ++index;
            }
        }, 5));
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationCompare instance = new CoreOperationCompare(null, null);
        
        // Test findMatch method with two iterators
        assertTrue(instance.findMatch(new Iterator() {
            int index;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public Object next() {
                return ++index;
            }
        }, new Iterator() {
            int index;

            @Override
            public boolean hasNext() {
                return index < 5;
            }

            @Override
            public Object next() {
                return ++index;
            }
        }));
        
        assertFalse(instance.findMatch(new Iterator() {
            int index;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public Object next() {
                return ++index;
            }
        }, new Iterator() {
            int index;

            @Override
            public boolean hasNext() {
                return index < 5;
            }

            @Override
            public Object next() {
                return ++index + 10;
            }
        }));
    }
    
}
