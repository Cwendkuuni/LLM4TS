import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_StringComparison() {
        CoreOperationCompare operation = new CoreOperationCompare();

        String string1 = "hello";
        String string2 = "hello";
        String string3 = "world";

        assertTrue(operation.equal(string1, string2)); // Typical use case - equal strings
        assertFalse(operation.equal(string1, string3)); // Typical use case - different strings
    }

    @Test
    public void testEqual_IntegerComparison() {
        CoreOperationCompare operation = new CoreOperationCompare();

        Integer int1 = 5;
        Integer int2 = 5;
        Integer int3 = 10;

        assertTrue(operation.equal(int1, int2)); // Typical use case - equal integers
        assertFalse(operation.equal(int1, int3)); // Typical use case - different integers
    }

    @Test
    public void testEqual_NullComparison() {
        CoreOperationCompare operation = new CoreOperationCompare();

        Object object1 = null;
        Object object2 = null;
        String string = "hello";

        assertTrue(operation.equal(object1, object2)); // Typical use case - both objects are null
        assertFalse(operation.equal(object1, string)); // Typical use case - one object is null and the other is not
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare();

        int[] array = {1, 2, 3, 4, 5};
        int value1 = 3;
        int value2 = 6;

        assertTrue(operation.contains(array, value1)); // Typical use case - value is present in the array
        assertFalse(operation.contains(array, value2)); // Typical use case - value is not present in the array
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare();

        int[] array1 = {1, 2, 3, 4, 5};
        int[] array2 = {6, 7, 8, 9, 10};

        assertTrue(operation.findMatch(array1, array2)); // Typical use case - at least one value is common between the two arrays
    }

    @Test
    public void testFindMatch_NoMatch() {
        CoreOperationCompare operation = new CoreOperationCompare();

        int[] array1 = {1, 2, 3, 4, 5};
        int[] array2 = {6, 7, 8, 9, 10};

        assertFalse(operation.findMatch(array1, array2)); // Typical use case - no values are common between the two arrays
    }

    @Test
    public void testEqual_IntegerPointerComparison() {
        CoreOperationCompare operation = new CoreOperationCompare();

        IntegerPointer pointer1 = new IntegerPointer(5);
        IntegerPointer pointer2 = new IntegerPointer(5);
        IntegerPointer pointer3 = new IntegerPointer(10);

        assertTrue(operation.equal(pointer1, pointer2)); // Typical use case - equal pointers
        assertFalse(operation.equal(pointer1, pointer3)); // Typical use case - different pointers
        assertTrue(operation.equal(pointer1, 5)); // Typical use case - pointer value is compared with an integer
    }

    @Test
    public void testEqual_BooleanComparison() {
        CoreOperationCompare operation = new CoreOperationCompare();

        Boolean bool1 = true;
        Boolean bool2 = true;
        Boolean bool3 = false;

        assertTrue(operation.equal(bool1, bool2)); // Typical use case - equal booleans
        assertFalse(operation.equal(bool1, bool3)); // Typical use case - different booleans
    }

}