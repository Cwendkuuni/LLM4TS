import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return false;
            }
        };
    }

    @Test
    public void testGetPrecedence() {
        int precedence = coreOperationCompare.getPrecedence();
        assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = coreOperationCompare.isSymmetric();
        assertTrue(symmetric);
    }

    @Test
    public void testContains() {
        Collection<Object> collection = new HashSet<>();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        Iterator<Object> iterator = collection.iterator();
        boolean contains = coreOperationCompare.contains(iterator, 2);
        assertTrue(contains);
    }

    @Test
    public void testFindMatch() {
        Collection<Object> leftCollection = new HashSet<>();
        leftCollection.add(1);
        leftCollection.add(2);
        leftCollection.add(3);

        Collection<Object> rightCollection = new HashSet<>();
        rightCollection.add(4);
        rightCollection.add(5);
        rightCollection.add(2);

        Iterator<Object> leftIterator = leftCollection.iterator();
        Iterator<Object> rightIterator = rightCollection.iterator();

        boolean match = coreOperationCompare.findMatch(leftIterator, rightIterator);
        assertTrue(match);
    }

    @Test
    public void testEqualWithPointers() {
        Pointer pointer1 = new Pointer(null);
        Pointer pointer2 = new Pointer(null);
        boolean equal = coreOperationCompare.equal(pointer1, pointer2);
        assertTrue(equal);
    }

    @Test
    public void testEqualWithNumbers() {
        boolean equal = coreOperationCompare.equal(1, 1);
        assertTrue(equal);
    }

    @Test
    public void testEqualWithBooleans() {
        boolean equal = coreOperationCompare.equal(true, true);
        assertTrue(equal);
    }

    @Test
    public void testEqualWithStrings() {
        boolean equal = coreOperationCompare.equal("hello", "hello");
        assertTrue(equal);
    }

    @Test
    public void testEqualWithNull() {
        boolean equal = coreOperationCompare.equal(null, null);
        assertTrue(equal);
    }

    @Test
    public void testEqualWithDifferentObjects() {
        boolean equal = coreOperationCompare.equal("hello", 123);
        assertFalse(equal);
    }

    @Test
    public void testEqualWithDifferentStrings() {
        boolean equal = coreOperationCompare.equal("hello", "world");
        assertFalse(equal);
    }

    @Test
    public void testEqualWithDifferentNumbers() {
        boolean equal = coreOperationCompare.equal(1, 2);
        assertFalse(equal);
    }

    @Test
    public void testEqualWithDifferentBooleans() {
        boolean equal = coreOperationCompare.equal(true, false);
        assertFalse(equal);
    }

    @Test
    public void testEqualWithPointerAndObject() {
        Pointer pointer = new Pointer(null);
        boolean equal = coreOperationCompare.equal(pointer, 1);
        assertFalse(equal);
    }
}