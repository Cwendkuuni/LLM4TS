import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects_ReturnsTrue() {
        // Arrange
        Object o1 = null;
        Object o2 = null;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_OneNullObject_ReturnsFalse() {
        // Arrange
        Object o1 = new Object();
        Object o2 = null;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_SameObject_ReturnsTrue() {
        // Arrange
        Object o1 = new Object();
        Object o2 = o1;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_EqualObjects_ReturnsTrue() {
        // Arrange
        Object o1 = new Object();
        Object o2 = new Object();

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_DifferentObjects_ReturnsFalse() {
        // Arrange
        Object o1 = new Object();
        Object o2 = new Object();

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArraysEqual_ReturnsTrue() {
        // Arrange
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};

        // Act
        boolean result = Equality.areEqual(array1, array2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_ArraysNotEqual_ReturnsFalse() {
        // Arrange
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};

        // Act
        boolean result = Equality.areEqual(array1, array2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArraysWithDifferentLengths_ReturnsFalse() {
        // Arrange
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};

        // Act
        boolean result = Equality.areEqual(array1, array2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ObjectAndArray_ReturnsFalse() {
        // Arrange
        Object object = new Object();
        int[] array = {1, 2, 3};

        // Act
        boolean result = Equality.areEqual(object, array);

        // Assert
        assertFalse(result);
    }
}