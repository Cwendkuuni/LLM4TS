import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Pointer;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setPosition(1);
        context.setSize(10);
        
        // Test when arg1 and arg2 are equal
        arg1.setValue(5);
        arg2.setValue(5);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        // Test when arg1 and arg2 are not equal
        arg2.setValue(10);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        // Test when arg1 is a pointer and arg2 is a value
        arg1.setValue(new Pointer(5));
        arg2.setValue(5);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        // Test when arg1 and arg2 are boolean values
        arg1.setValue(true);
        arg2.setValue(true);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        arg2.setValue(false);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        // Test when arg1 and arg2 are number values
        arg1.setValue(5.0);
        arg2.setValue(5);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        arg2.setValue(10.0);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        // Test when arg1 and arg2 are string values
        arg1.setValue("abc");
        arg2.setValue("abc");
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        arg2.setValue("def");
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // Test when the iterator is empty
        Iterator iterator = new ArrayList<>().iterator();
        assertFalse(coreOperationCompare.contains(iterator, "value"));

        // Test when the iterator contains the value
        Collection collection = new ArrayList<>();
        collection.add("value");
        iterator = collection.iterator();
        assertTrue(coreOperationCompare.contains(iterator, "value"));

        // Test when the iterator does not contain the value
        iterator = collection.iterator();
        assertFalse(coreOperationCompare.contains(iterator, "otherValue"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // Test when both iterators are empty
        Iterator left = new ArrayList<>().iterator();
        Iterator right = new ArrayList<>().iterator();
        assertFalse(coreOperationCompare.findMatch(left, right));

        // Test when only one iterator is empty
        Collection collection = new ArrayList<>();
        collection.add("value");
        left = collection.iterator();
        right = new ArrayList<>().iterator();
        assertFalse(coreOperationCompare.findMatch(left, right));

        left = new ArrayList<>().iterator();
        collection = new ArrayList<>();
        collection.add("value");
        right = collection.iterator();
        assertFalse(coreOperationCompare.findMatch(left, right));

        // Test when both iterators contain matching values
        collection = new ArrayList<>();
        collection.add("value");
        left = collection.iterator();
        collection = new ArrayList<>();
        collection.add("value");
        right = collection.iterator();
        assertTrue(coreOperationCompare.findMatch(left, right));

        // Test when both iterators contain non-matching values
        collection = new ArrayList<>();
        collection.add("value1");
        left = collection.iterator();
        collection = new ArrayList<>();
        collection.add("value2");
        right = collection.iterator();
        assertFalse(coreOperationCompare.findMatch(left, right));
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // Test when both values are pointers and equal
        Pointer pointer1 = new Pointer(5);
        assertEquals(coreOperationCompare.equal(pointer1, new Pointer(5)), true);

        // Test when both values are pointers but not equal
        assertEquals(coreOperationCompare.equal(pointer1, new Pointer(10)), false);

        // Test when one value is a pointer and the other is a value
        assertEquals(coreOperationCompare.equal(pointer1, 5), false);
        assertEquals(coreOperationCompare.equal(5, pointer1), false);

        // Test when both values are boolean
        assertEquals(coreOperationCompare.equal(true, true), true);
        assertEquals(coreOperationCompare.equal(true, false), false);
        assertEquals(coreOperationCompare.equal(false, false), true);

        // Test when both values are number
        assertEquals(coreOperationCompare.equal(5.0, 5), true);
        assertEquals(coreOperationCompare.equal(5.0, 10.0), false);

        // Test when both values are string
        assertEquals(coreOperationCompare.equal("abc", "abc"), true);
        assertEquals(coreOperationCompare.equal("abc", "def"), false);

        // Test when one value is null
        assertEquals(coreOperationCompare.equal(null, null), true);
        assertEquals(coreOperationCompare.equal(null, 5), false);
        assertEquals(coreOperationCompare.equal(5, null), false);

        // Test when both values are null
        assertEquals(coreOperationCompare.equal(null, null), true);
    }
}