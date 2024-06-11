import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_WhenBothNull_ReturnsTrue() {
        // Arrange
        Object o1 = null;
        Object o2 = null;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_WhenOneNull_ReturnsFalse() {
        // Arrange
        Object o1 = null;
        Object o2 = new Object();

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_WhenNotArray_WithEqualObjects_ReturnsTrue() {
        // Arrange
        Object o1 = new Integer(5);
        Object o2 = new Integer(5);

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_WhenNotArray_WithDifferentObjects_ReturnsFalse() {
        // Arrange
        Object o1 = new Integer(5);
        Object o2 = new Integer(10);

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_WhenArraysWithDifferentElements_ReturnsFalse() {
        // Arrange
        Object o1 = new Integer[]{1, 2, 3};
        Object o2 = new Integer[]{3, 2, 1};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_WhenArraysWithSameElements_ReturnsTrue() {
        // Arrange
        Object o1 = new Integer[]{1, 2, 3};
        Object o2 = new Integer[]{1, 2, 3};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_WhenArraysWithDifferentLengths_ReturnsFalse() {
        // Arrange
        Object o1 = new Integer[]{1, 2, 3};
        Object o2 = new Integer[]{1, 2, 3, 4};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArrayLengthsEqual_WhenArraysWithSameLength_ReturnsTrue() {
        // Arrange
        Object o1 = new Integer[]{1, 2, 3};
        Object o2 = new Integer[]{3, 2, 1};

        // Act
        boolean result = Equality.areArrayLengthsEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_WhenArraysWithDifferentLengths_ReturnsFalse() {
        // Arrange
        Object o1 = new Integer[]{1, 2, 3};
        Object o2 = new Integer[]{1, 2, 3, 4};

        // Act
        boolean result = Equality.areArrayLengthsEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_WhenArraysWithDifferentElements_ReturnsFalse() {
        // Arrange
        Object o1 = new Integer[]{1, 2, 3};
        Object o2 = new Integer[]{3, 2, 1};

        // Act
        boolean result = Equality.areArrayElementsEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_WhenArraysWithSameElements_ReturnsTrue() {
        // Arrange
        Object o1 = new Integer[]{1, 2, 3};
        Object o2 = new Integer[]{1, 2, 3};

        // Act
        boolean result = Equality.areArrayElementsEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testAreArrayElementsEqual_WhenOneArrayIsNull_ThrowsNullPointerException() {
        // Arrange
        Object o1 = new Integer[]{1, 2, 3};
        Object o2 = null;

        // Act
        Equality.areArrayElementsEqual(o1, o2);

        // Assert (Expected exception)
    }

    @Test
    public void testIsArray_WhenObjectIsArray_ReturnsTrue() {
        // Arrange
        Object o = new Integer[]{1, 2, 3};

        // Act
        boolean result = Equality.isArray(o);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsArray_WhenObjectIsNotArray_ReturnsFalse() {
        // Arrange
        Object o = new Integer(5);

        // Act
        boolean result = Equality.isArray(o);

        // Assert
        assertFalse(result);
    }

}