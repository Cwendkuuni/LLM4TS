import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs_ReturnsTrue() {
        // Arrange
        Object obj1 = null;
        Object obj2 = null;

        // Act
        boolean result = Equality.areEqual(obj1, obj2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_OneNullInput_ReturnsFalse() {
        // Arrange
        Object obj1 = new Object();
        Object obj2 = null;

        // Act
        boolean result = Equality.areEqual(obj1, obj2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_BothArraysOfDifferentLength_ReturnsFalse() {
        // Arrange
        int[] arr1 = {1, 2};
        int[] arr2 = {1, 2, 3};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_BothArraysOfSameLengthButDifferentElements_ReturnsFalse() {
        // Arrange
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_BothArraysOfSameLengthAndSameElements_ReturnsTrue() {
        // Arrange
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        // Act
        boolean result = Equality.areEqual(arr1, arr2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreEqual_ObjectsWithDifferentValues_ReturnsFalse() {
        // Arrange
        Integer obj1 = new Integer(5);
        Integer obj2 = new Integer(10);

        // Act
        boolean result = Equality.areEqual(obj1, obj2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreEqual_EqualObjects_ReturnsTrue() {
        // Arrange
        Integer obj1 = new Integer(5);
        Integer obj2 = new Integer(5);

        // Act
        boolean result = Equality.areEqual(obj1, obj2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreArraysEqual_BothArraysOfDifferentLength_ReturnsFalse() {
        // Arrange
        int[] arr1 = {1, 2};
        int[] arr2 = {1, 2, 3};

        // Act
        boolean result = Equality.areArrayLengthsEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_BothArraysOfSameLengthButDifferentElements_ReturnsFalse() {
        // Arrange
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};

        // Act
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_BothArraysOfSameLengthAndSameElements_ReturnsTrue() {
        // Arrange
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        // Act
        boolean result = Equality.areArrayElementsEqual(arr1, arr2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArray_NonArrayObject_ReturnsFalse() {
        // Arrange
        Object obj = new Object();

        // Act
        boolean result = Equality.isArray(obj);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsArray_ArrayObject_ReturnsTrue() {
        // Arrange
        int[] arr = {1, 2, 3};

        // Act
        boolean result = Equality.isArray(arr);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreArraysEqual_NullInputs_ReturnsTrue() {
        // Arrange
        int[] arr1 = null;
        int[] arr2 = null;

        // Act
        boolean result = Equality.areArraysEqual(arr1, arr2);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testAreArraysEqual_OneNullInput_ReturnsFalse() {
        // Arrange
        int[] arr1 = null;
        int[] arr2 = {1, 2, 3};

        // Act
        boolean result = Equality.areArraysEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_BothArraysOfDifferentLength_ReturnsFalse() {
        // Arrange
        int[] arr1 = {1, 2};
        int[] arr2 = {1, 2, 3};

        // Act
        boolean result = Equality.areArraysEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_BothArraysOfSameLengthButDifferentElements_ReturnsFalse() {
        // Arrange
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};

        // Act
        boolean result = Equality.areArraysEqual(arr1, arr2);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_BothArraysOfSameLengthAndSameElements_ReturnsTrue() {
        // Arrange
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        // Act
        boolean result = Equality.areArraysEqual(arr1, arr2);

        // Assert
        Assert.assertTrue(result);
    }

}