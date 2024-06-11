import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };

        EvalContext context = new EvalContext();
        assertTrue(operation.equal(context, null, null));
        assertTrue(operation.equal(context, new Object(), new Object()));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean contains(Iterator it, Object value) {
                return true;
            }
        };

        Iterator iterator = new Iterator() {
            public boolean hasNext() {
                return true;
            }

            public Object next() {
                return new Object();
            }
        };

        assertTrue(operation.contains(iterator, new Object()));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean findMatch(Iterator lit, Iterator rit) {
                return true;
            }
        };

        Iterator leftIterator = new Iterator() {
            public boolean hasNext() {
                return true;
            }

            public Object next() {
                return new Object();
            }
        };

        Iterator rightIterator = new Iterator() {
            public boolean hasNext() {
                return true;
            }

            public Object next() {
                return new Object();
            }
        };

        assertTrue(operation.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testEqualObject() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };

        assertTrue(operation.equal(new Object(), new Object()));
        assertTrue(operation.equal("test", "test"));
    }
}