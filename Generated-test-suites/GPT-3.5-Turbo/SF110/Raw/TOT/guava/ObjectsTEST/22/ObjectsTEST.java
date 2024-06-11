
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    // Test cases for equal() method

    @Test
    public void testEqual_WhenObjectsAreEqual_ReturnTrue() {
        // Arrange
        Object a = "abc";
        Object b = new String("abc");

        // Act
        boolean result = Objects.equal(a, b);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEqual_WhenObjectsAreNotEqual_ReturnFalse() {
        // Arrange
        Object a = "abc";
        Object b = new String("def");

        // Act
        boolean result = Objects.equal(a, b);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testEqual_WhenBothObjectsAreNull_ReturnTrue() {
        // Arrange
        Object a = null;
        Object b = null;

        // Act
        boolean result = Objects.equal(a, b);

        // Assert
        assertTrue(result);
    }

    // Test cases for hashCode() method

    @Test
    public void testHashCode_WhenNoObjects_ReturnZero() {
        // Arrange
        Object[] objects = {};

        // Act
        int result = Objects.hashCode(objects);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testHashCode_WhenSingleObject_ReturnCorrectHashCode() {
        // Arrange
        Object[] objects = { "abc" };

        // Act
        int result = Objects.hashCode(objects);

        // Assert
        assertEquals("abc".hashCode(), result);
    }

    @Test
    public void testHashCode_WhenMultipleObjects_ReturnCorrectHashCode() {
        // Arrange
        Object[] objects = { "abc", 123, true };

        // Act
        int result = Objects.hashCode(objects);

        // Assert
        int expected = ("abc".hashCode() ^ Integer.hashCode(123) ^ Boolean.hashCode(true));
        assertEquals(expected, result);
    }

    // Test cases for toStringHelper() method

    @Test
    public void testToStringHelper_WhenGivenObject_ReturnToStringHelperWithClassName() {
        // Arrange
        Object object = new Object();

        // Act
        Objects.ToStringHelper result = Objects.toStringHelper(object);

        // Assert
        assertEquals(object.getClass().getSimpleName(), result.toString());
    }

    @Test
    public void testToStringHelper_WhenGivenClass_ReturnToStringHelperWithClassName() {
        // Arrange
        Class<?> clazz = String.class;

        // Act
        Objects.ToStringHelper result = Objects.toStringHelper(clazz);

        // Assert
        assertEquals(clazz.getSimpleName(), result.toString());
    }

    @Test
    public void testToStringHelper_WhenGivenClassName_ReturnToStringHelperWithClassName() {
        // Arrange
        String className = "TestClassName";

        // Act
        Objects.ToStringHelper result = Objects.toStringHelper(className);

        // Assert
        assertEquals(className, result.toString());
    }

    // Test cases for firstNonNull() method

    @Test
    public void testFirstNonNull_WhenFirstObjectIsNull_ReturnSecondObject() {
        // Arrange
        Object first = null;
        Object second = "abc";

        // Act
        Object result = Objects.firstNonNull(first, second);

        // Assert
        assertEquals(second, result);
    }

    @Test
    public void testFirstNonNull_WhenFirstObjectIsNotNull_ReturnFirstObject() {
        // Arrange
        Object first = "abc";
        Object second = "def";

        // Act
        Object result = Objects.firstNonNull(first, second);

        // Assert
        assertEquals(first, result);
    }
}

