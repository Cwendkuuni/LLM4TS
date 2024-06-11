import org.junit.Test;
import org.mockito.internal.matchers.Equality;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_withNullObjects_shouldReturnTrue() {
        // Arrange
        Object o1 = null;
        Object o2 = null;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_withOneNullObject_shouldReturnFalse() {
        // Arrange
        Object o1 = null;
        Object o2 = new Object();

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_withTwoArrayObjects_shouldReturnTrue() {
        // Arrange
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_withDifferentArrayLengths_shouldReturnFalse() {
        // Arrange
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3, 4};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_withDifferentArrayElements_shouldReturnFalse() {
        // Arrange
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{4, 5, 6};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_withSameNonArrayObject_shouldReturnTrue() {
        // Arrange
        Object o1 = new Object();
        Object o2 = o1;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_withDifferentNonArrayObjects_shouldReturnFalse() {
        // Arrange
        Object o1 = new Object();
        Object o2 = new Object();

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_withSameNonArrayObject_shouldReturnTrue() {
        // Arrange
        Object o1 = new Object();
        Object o2 = o1;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_withDifferentNonArrayObjects_shouldReturnFalse() {
        // Arrange
        Object o1 = new Object();
        Object o2 = new Object();

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArraysEqual_withSameLengthArrays_shouldReturnTrue() {
        // Arrange
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3};

        // Act
        boolean result = Equality.areArraysEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreArraysEqual_withDifferentLengthArrays_shouldReturnFalse() {
        // Arrange
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3, 4};

        // Act
        boolean result = Equality.areArraysEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArrayLengthsEqual_withSameLengthArrays_shouldReturnTrue() {
        // Arrange
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3};

        // Act
        boolean result = Equality.areArrayLengthsEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_withDifferentLengthArrays_shouldReturnFalse() {
        // Arrange
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3, 4};

        // Act
        boolean result = Equality.areArrayLengthsEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_withSameElementsArrays_shouldReturnTrue() {
        // Arrange
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{1, 2, 3};

        // Act
        boolean result = Equality.areArrayElementsEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreArrayElementsEqual_withDifferentElementsArrays_shouldReturnFalse() {
        // Arrange
        Object o1 = new int[]{1, 2, 3};
        Object o2 = new int[]{4, 5, 6};

        // Act
        boolean result = Equality.areArrayElementsEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsArray_withArrayObject_shouldReturnTrue() {
        // Arrange
        Object o = new int[]{1, 2, 3};

        // Act
        boolean result = Equality.isArray(o);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsArray_withNonArrayObject_shouldReturnFalse() {
        // Arrange
        Object o = new Object();

        // Act
        boolean result = Equality.isArray(o);

        // Assert
        assertFalse(result);
    }
}