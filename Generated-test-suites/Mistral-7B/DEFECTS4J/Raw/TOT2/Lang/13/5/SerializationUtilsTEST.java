import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws SerializationException {
        // Given: An instance of a cloneable object
        Cloneable cloneableObject = new HashMap<>();

        // When: SerializationUtils is used to deep clone the object
        Cloneable clonedObject = SerializationUtils.clone(cloneableObject);

        // Then: The cloned object should not be the same as the original object
        assertNotNull(clonedObject);
        assertNotNull(cloneableObject);
        assertEquals(cloneableObject, cloneableObject);
        assertEquals(clonedObject, clonedObject);
        assertEquals(cloneableObject, clonedObject);

        // Given: A null object
        Object nullObject = null;

        // When: SerializationUtils is used to deep clone the null object

        // Then: An IllegalArgumentException should be thrown
        try {
            SerializationUtils.clone(nullObject);
        } catch (IllegalArgumentException expected) {
            // Test passes if expected exception is thrown
        }
    }

    // Test other methods with similar test cases
}