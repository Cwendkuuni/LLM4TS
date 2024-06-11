
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Array;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        // Test case 1: o1 and o2 are both null
        Object o1 = null;
        Object o2 = null;
        boolean expected1 = true;
        boolean actual1 = Equality.areEqual(o1, o2);
        assertEquals(expected1, actual1);

        // Test case 2: o1 is null, o2 is not null
        Object o3 = null;
        Object o4 = new Object();
        boolean expected2 = false;
        boolean actual2 = Equality.areEqual(o3, o4);
        assertEquals(expected2, actual2);

        // Test case 3: o1 is not null, o2 is null
        Object o5 = new Object();
        Object o6 = null;
        boolean expected3 = false;
        boolean actual3 = Equality.areEqual(o5, o6);
        assertEquals(expected3, actual3);

        // Test case 4: o1 and o2 are the same object reference
        Object o7 = new Object();
        Object o8 = o7;
        boolean expected4 = true;
        boolean actual4 = Equality.areEqual(o7, o8);
        assertEquals(expected4, actual4);

        // Test case 5: o1 and o2 are different object references
        Object o9 = new Object();
        Object o10 = new Object();
        boolean expected5 = false;
        boolean actual5 = Equality.areEqual(o9, o10);
        assertEquals(expected5, actual5);

        // Test case 6: o1 and o2 are arrays with equal elements
        Object o11 = new int[]{1, 2, 3};
        Object o12 = new int[]{1, 2, 3};
        boolean expected6 = true;
        boolean actual6 = Equality.areEqual(o11, o12);
        assertEquals(expected6, actual6);

        // Test case 7: o1 and o2 are arrays with different elements
        Object o13 = new int[]{1, 2, 3};
        Object o14 = new int[]{1, 2, 4};
        boolean expected7 = false;
        boolean actual7 = Equality.areEqual(o13, o14);
        assertEquals(expected7, actual7);
    }

    @Test
    public void testAreArraysEqual() {
        // Test case 1: o1 and o2 have equal lengths and elements
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3};
        boolean expected1 = true;
        boolean actual1 = Equality.areArraysEqual(o1, o2);
        assertEquals(expected1, actual1);

        // Test case 2: o1 and o2 have different lengths
        Object o3 = new int[]{1, 2, 3};
        Object o4 = new int[]{1, 2, 3, 4};
        boolean expected2 = false;
        boolean actual2 = Equality.areArraysEqual(o3, o4);
        assertEquals(expected2, actual2);

        // Test case 3: o1 and o2 have equal lengths but different elements
        Object o5 = new int[]{1, 2, 3};
        Object o6 = new int[]{1, 2, 4};
        boolean expected3 = false;
        boolean actual3 = Equality.areArraysEqual(o5, o6);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testAreArrayLengthsEqual() {
        // Test case 1: o1 and o2 have equal lengths
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3};
        boolean expected1 = true;
        boolean actual1 = Equality.areArrayLengthsEqual(o1, o2);
        assertEquals(expected1, actual1);

        // Test case 2: o1 and o2 have different lengths
        Object o3 = new int[]{1, 2, 3};
        Object o4 = new int[]{1, 2, 3, 4};
        boolean expected2 = false;
        boolean actual2 = Equality.areArrayLengthsEqual(o3, o4);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testAreArrayElementsEqual() {
        // Test case 1: o1 and o2 have equal elements
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3};
        boolean expected1 = true;
        boolean actual1 = Equality.areArrayElementsEqual(o1, o2);
        assertEquals(expected1, actual1);

        // Test case 2: o1 and o2 have different elements
        Object o3 = new int[]{1, 2, 3};
        Object o4 = new int[]{1, 2, 4};
        boolean expected2 = false;
        boolean actual2 = Equality.areArrayElementsEqual(o3, o4);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testIsArray() {
        // Test case 1: object is an array
        Object o1 = new int[]{1, 2, 3};
        boolean expected1 = true;
        boolean actual1 = Equality.isArray(o1);
        assertEquals(expected1, actual1);

        // Test case 2: object is not an array
        Object o2 = new Object();
        boolean expected2 = false;
        boolean actual2 = Equality.isArray(o2);
        assertEquals(expected2, actual2);
    }
}
