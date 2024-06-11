import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import java.util.Arrays;

public class EqualityTest {

    // Test case for areEqual method
    @Test
    public void testAreEqual() {
        // Typical use case
        assert Equality.areEqual(5, 5);

        // Edge cases
        assert Equality.areEqual(null, null) == true;
        assert Equality.areEqual(10, null) == false;

        // Error scenarios
        assert Equality.areEqual("Hello", "World") == false;
        assert Equality.areEqual(null, "Hello") == false;
    }

    // Test case for areArraysEqual method
    @Test
    public void testAreArraysEqual() {
        // Typical use case
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {1, 2, 3};
        assert Equality.areArraysEqual(arr1, arr2);

        // Edge cases
        Integer[] arr3 = {1, 2};
        Integer[] arr4 = {1, 2, 3};
        assert Equality.areArraysEqual(arr3, arr4) == false;

        Integer[] arr5 = {};
        Integer[] arr6 = {};
        assert Equality.areArraysEqual(arr5, arr6);

        // Error scenarios
        String[] arr7 = {"Hello", "World"};
        String[] arr8 = {"Hello"};
        assert Equality.areArraysEqual(arr7, arr8) == false;
    }

    // Test case for areArrayLengthsEqual method
    @Test
    public void testAreArrayLengthsEqual() {
        // Typical use case
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {4, 5, 6};
        assert Equality.areArrayLengthsEqual(arr1, arr2) == true;

        // Edge cases
        Integer[] arr3 = {1, 2};
        Integer[] arr4 = {1, 2, 3};
        assert Equality.areArrayLengthsEqual(arr3, arr4) == false;

        Integer[] arr5 = {};
        Integer[] arr6 = {};
        assert Equality.areArrayLengthsEqual(arr5, arr6) == true;

        // Error scenarios
        String[] arr7 = {"Hello", "World"};
        String[] arr8 = {"Hello"};
        assert Equality.areArrayLengthsEqual(arr7, arr8) == false;
    }

    // Test case for areArrayElementsEqual method
    @Test
    public void testAreArrayElementsEqual() {
        // Typical use case
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {1, 2, 3};
        assert Equality.areArrayElementsEqual(arr1, arr2);

        // Edge cases
        Integer[] arr3 = {1, 2};
        Integer[] arr4 = {1, 2, 3};
        assert Equality.areArrayElementsEqual(arr3, arr4) == false;

        Integer[] arr5 = {};
        Integer[] arr6 = {};
        assert Equality.areArrayElementsEqual(arr5, arr6);

        // Error scenarios
        String[] arr7 = {"Hello", "World"};
        String[] arr8 = {"Hello"};
        assert Equality.areArrayElementsEqual(arr7, arr8) == false;
    }

    // Test case for isArray method
    @Test
    public void testIsArray() {
        // Typical use case
        Integer[] arr1 = {1, 2, 3};
        assert Equality.isArray(arr1);

        // Edge cases
        Integer num = 5;
        assert Equality.isArray(num) == false;

        // Error scenarios
        assert Equality.isArray(null) == false;
    }
}