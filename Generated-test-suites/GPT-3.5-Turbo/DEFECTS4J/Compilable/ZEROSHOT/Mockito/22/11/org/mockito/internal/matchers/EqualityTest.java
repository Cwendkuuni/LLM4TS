package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        // Arrange
        Object o1 = null;
        Object o2 = null;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_OneNullInput() {
        // Arrange
        Object o1 = new Object();
        Object o2 = null;

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothArrays() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 2, 3};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_OneArrayAndOneObject() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new Object();

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothEqualObjects() {
        // Arrange
        Object o1 = new String("abc");
        Object o2 = new String("abc");

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_DifferentObjects() {
        // Arrange
        Object o1 = new String("abc");
        Object o2 = new String("def");

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_EqualArrayLengths() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {4, 5, 6};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_DifferentArrayLengths() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 2};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreEqual_EqualArrayElements() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 2, 3};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreEqual_DifferentArrayElements() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 4, 3};

        // Act
        boolean result = Equality.areEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArrayEqual_NullInputs() {
        // Arrange
        Object o1 = null;
        Object o2 = null;

        // Act
        boolean result = Equality.areArraysEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreArrayEqual_DifferentArrayLengths() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 2};

        // Act
        boolean result = Equality.areArraysEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArrayEqual_EqualArrayElements() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 2, 3};

        // Act
        boolean result = Equality.areArraysEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreArrayEqual_DifferentArrayElements() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 4, 3};

        // Act
        boolean result = Equality.areArraysEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArrayLengthsEqual_EqualArrayLengths() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {4, 5, 6};

        // Act
        boolean result = Equality.areArrayLengthsEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_DifferentArrayLengths() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 2};

        // Act
        boolean result = Equality.areArrayLengthsEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_EqualArrayElements() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 2, 3};

        // Act
        boolean result = Equality.areArrayElementsEqual(o1, o2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAreArrayElementsEqual_DifferentArrayElements() {
        // Arrange
        Object o1 = new int[] {1, 2, 3};
        Object o2 = new int[] {1, 4, 3};

        // Act
        boolean result = Equality.areArrayElementsEqual(o1, o2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsArray_ArrayObject() {
        // Arrange
        Object o = new int[] {1, 2, 3};

        // Act
        boolean result = Equality.isArray(o);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsArray_NonArrayObject() {
        // Arrange
        Object o = new Object();

        // Act
        boolean result = Equality.isArray(o);

        // Assert
        assertFalse(result);
    }
}