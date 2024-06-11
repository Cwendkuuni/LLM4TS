
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer() {
            public String asPath() {
                return null;
            }

            public int compareTo(Pointer pointer) {
                return 0;
            }

            public int hashCode() {
                return 0;
            }

            public Object getNode() {
                return null;
            }

            public Object getBaseValue() {
                return null;
            }

            public Object getImmediateNode() {
                return null;
            }

            public Pointer getParent() {
                return null;
            }

            public String getValueString() {
                return null;
            }

            public boolean isLeaf() {
                return false;
            }

            public Iterator<? extends Pointer> childIterator() {
                return Collections.emptyIterator();
            }

            public boolean setValue(Object value) {
                return false;
            }

            public void remove() {

            }
        });

        Expression left = new Expression();
        Expression right = new Expression();

        // Test when left and right are both null
        assertTrue(coreOperationCompare.equal(context, left, right));

        // Test when left is null and right is non-null
        right = new Expression() {
            public Object compute(EvalContext ec) {
                return "test";
            }
        };
        assertFalse(coreOperationCompare.equal(context, left, right));

        // Test when right is null and left is non-null
        left = new Expression() {
            public Object compute(EvalContext ec) {
                return "test";
            }
        };
        assertFalse(coreOperationCompare.equal(context, left, right));

        // Test when left and right are not null and are equal
        right = new Expression() {
            public Object compute(EvalContext ec) {
                return "test";
            }
        };
        assertTrue(coreOperationCompare.equal(context, left, right));

        // Test when left and right are not null and are not equal
        right = new Expression() {
            public Object compute(EvalContext ec) {
                return "not_test";
            }
        };
        assertFalse(coreOperationCompare.equal(context, left, right));

        // Test when left and right are Iterators
        left = new Expression() {
            public Object compute(EvalContext ec) {
                return Arrays.asList("test", "not_test", "another_test").iterator();
            }
        };
        right = new Expression() {
            public Object compute(EvalContext ec) {
                return Arrays.asList("test", "another_test").iterator();
            }
        };
        assertTrue(coreOperationCompare.equal(context, left, right));

        left = new Expression() {
            public Object compute(EvalContext ec) {
                return Arrays.asList("test", "not_test", "another_test").iterator();
            }
        };
        right = new Expression() {
            public Object compute(EvalContext ec) {
                return Arrays.asList("not_test", "another_test").iterator();
            }
        };
        assertFalse(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        Iterator<Object> iterator = Arrays.asList("test", "not_test", "another_test").iterator();
        assertTrue(coreOperationCompare.contains(iterator, "test"));
        assertFalse(coreOperationCompare.contains(iterator, "not_present"));
    }

    @Test
    public void testFindMatch() {
        Iterator<Object> leftIterator = Arrays.asList("test", "not_test", "another_test").iterator();
        Iterator<Object> rightIterator = Arrays.asList("test", "another_test").iterator();
        assertTrue(coreOperationCompare.findMatch(leftIterator, rightIterator));

        leftIterator = Arrays.asList("test", "not_test", "another_test").iterator();
        rightIterator = Arrays.asList("not_test", "another_test").iterator();
        assertFalse(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testEqualValues() {
        Object l = "test";
        Object r = "test";
        assertTrue(coreOperationCompare.equal(l, r));

        l = "test";
        r = "not_test";
        assertFalse(coreOperationCompare.equal(l, r));

        l = 1;
        r = 1;
        assertTrue(coreOperationCompare.equal(l, r));

        l = 1;
        r = 1.0;
        assertTrue(coreOperationCompare.equal(l, r));

        l = "1";
        r = "1.0";
        assertFalse(coreOperationCompare.equal(l, r));

        l = true;
        r = true;
        assertTrue(coreOperationCompare.equal(l, r));

        l = true;
        r = false;
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Pointer() {
            public String asPath() {
                return null;
            }

            public int compareTo(Pointer pointer) {
                return 0;
            }

            public int hashCode() {
                return 0;
            }

            public Object getNode() {
                return null;
            }

            public Object getBaseValue() {
                return null;
            }

            public Object getImmediateNode() {
                return null;
            }

            public Pointer getParent() {
                return null;
            }

            public String getValueString() {
                return null;
            }

            public boolean isLeaf() {
                return false;
            }

            public Iterator<? extends Pointer> childIterator() {
                return Collections.emptyIterator();
            }

            public boolean setValue(Object value) {
                return false;
            }

            public void remove() {

            }
        };
        r = new Pointer() {
            public String asPath() {
                return null;
            }

            public int compareTo(Pointer pointer) {
                return 0;
            }

            public int hashCode() {
                return 0;
            }

            public Object getNode() {
                return null;
            }

            public Object getBaseValue() {
                return null;
            }

            public Object getImmediateNode() {
                return null;
            }

            public Pointer getParent() {
                return null;
            }

            public String getValueString() {
                return null;
            }

            public boolean isLeaf() {
                return false;
            }

            public Iterator<? extends Pointer> childIterator() {
                return Collections.emptyIterator();
            }

            public boolean setValue(Object value) {
                return false;
            }

            public void remove() {

            }
        };
        assertTrue(coreOperationCompare.equal(l, r));

        l = "test";
        r = new Pointer() {
            public String asPath() {
                return null;
            }

            public int compareTo(Pointer pointer) {
                return 0;
            }

            public int hashCode() {
                return 0;
            }

            public Object getNode() {
                return null;
            }

            public Object getBaseValue() {
                return null;
            }

            public Object getImmediateNode() {
                return null;
            }

            public Pointer getParent() {
                return null;
            }

            public String getValueString() {
                return null;
            }

            public boolean isLeaf() {
                return false;
            }

            public Iterator<? extends Pointer> childIterator() {
                return Collections.emptyIterator();
            }

            public boolean setValue(Object value) {
                return false;
            }

            public void remove() {

            }
        };
        assertFalse(coreOperationCompare.equal(l, r));

        l = 1;
        r = new Pointer() {
            public String asPath() {
                return null;
            }

            public int compareTo(Pointer pointer) {
                return 0;
            }

            public int hashCode() {
                return 0;
            }

            public Object getNode() {
                return null;
            }

            public Object getBaseValue() {
                return null;
            }

            public Object getImmediateNode() {
                return null;
            }

            public Pointer getParent() {
                return null;
            }

            public String getValueString() {
                return null;
            }

            public boolean isLeaf() {
                return false;
            }

            public Iterator<? extends Pointer> childIterator() {
                return Collections.emptyIterator();
            }

            public boolean setValue(Object value) {
                return false;
            }

            public void remove() {

            }
        };
        assertTrue(coreOperationCompare.equal(l, r));

        l = new Pointer() {
            public String asPath() {
                return null;
            }

            public int compareTo(Pointer pointer) {
                return 0;
            }

            public int hashCode() {
                return 0;
            }

            public Object getNode() {
                return null;
            }

            public Object getBaseValue() {
                return null;
            }

            public Object getImmediateNode() {
                return null;
            }

            public Pointer getParent() {
                return null;
            }

            public String getValueString() {
                return null;
            }

            public boolean isLeaf() {
                return false;
            }

            public Iterator<? extends Pointer> childIterator() {
                return Collections.emptyIterator();
            }

            public boolean setValue(Object value) {
                return false;
            }

            public void remove() {

            }
        };
        r = new Pointer() {
            public String asPath() {
                return null;
            }

            public int compareTo(Pointer pointer) {
                return 0;
            }

            public int hashCode() {
                return 0;
            }

            public Object getNode() {
                return null;
            }

            public Object getBaseValue() {
                return null;
            }

            public Object getImmediateNode() {
                return null;
            }

            public Pointer getParent() {
                return null;
            }

            public String getValueString() {
                return null;
            }

            public boolean isLeaf() {
                return false;
            }

            public Iterator<? extends Pointer> childIterator() {
                return Collections.emptyIterator();
            }

            public boolean setValue(Object value) {
                return false;
            }

            public void remove() {

            }
        };
        assertTrue(coreOperationCompare.equal(l, r));

        l = null;
        r = null;
        assertTrue(coreOperationCompare.equal(l, r));
    }
}
