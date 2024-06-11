import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void equal_shouldReturnTrue_whenObjectsAreEqual() {
        // Arrange
        Object a = new Object();
        Object b = a;

        // Act
        boolean result = Objects.equal(a, b);

        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equal_shouldReturnFalse_whenObjectsAreNotEqual() {
        // Arrange
        Object a = new Object();
        Object b = new Object();

        // Act
        boolean result = Objects.equal(a, b);

        // Assert
        assertFalse(result);
    }
    
    @Test
    public void equal_shouldReturnTrue_whenNullObjectsArePassed() {
        // Act
        boolean result = Objects.equal(null, null);

        // Assert
        assertTrue(result);
    }
    
    @Test
    public void hashCode_shouldReturnHashCode_whenObjectsAreNotNull() {
        // Arrange
        Object[] objects = {1, "test", 3.14};

        // Act
        int hashCode = Objects.hashCode(objects);

        // Assert
        assertNotNull(hashCode);
    }
    
    @Test
    public void hashCode_shouldReturnZero_whenObjectsIsNull() {
        // Act
        int hashCode = Objects.hashCode(null);

        // Assert
        assertEquals(0, hashCode);
    }
    
    @Test
    public void toStringHelper_shouldCreateToStringRepresentationOfObject() {
        // Arrange
        Object obj = new Object();

        // Act
        Objects.ToStringHelper helper = Objects.toStringHelper(obj);

        // Assert
        assertNotNull(helper);
    }
    
    @Test
    public void firstNonNull_shouldReturnFirstObject_whenBothAreNonNull() {
        // Arrange
        String first = "first";
        String second = "second";

        // Act
        String result = Objects.firstNonNull(first, second);

        // Assert
        assertEquals(first, result);
    }
    
    @Test
    public void firstNonNull_shouldReturnSecondObject_whenFirstIsNull() {
        // Arrange
        String first = null;
        String second = "second";

        // Act
        String result = Objects.firstNonNull(first, second);

        // Assert
        assertEquals(second, result);
    }
    
    @Test
    public void firstNonNull_shouldReturnFirstObject_whenSecondIsNull() {
        // Arrange
        String first = "first";
        String second = null;

        // Act
        String result = Objects.firstNonNull(first, second);

        // Assert
        assertEquals(first, result);
    }
    
    @Test
    public void toString_shouldReturnNonNullStringRepresentation() {
        // Arrange
        Object obj = new Object();

        // Act
        String result = Objects.toString(obj);

        // Assert
        assertNotNull(result);
    }

}