import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;

    @Before
    public void setup() {
        deserializer = new StringCollectionDeserializer(null, null, null);
    }

    @Test
    public void testDeserialize_WithValidArray_ReturnsCollectionOfString() throws Exception {
        // Arrange
        String json = "[\"value1\", \"value2\", \"value3\"]";
        Collection<String> expected = Arrays.asList("value1", "value2", "value3");

        // Act
        Collection<String> result = deserializer.deserialize(json);

        // Assert
        assertNotNull(result);
        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_WithNull_ReturnsEmptyCollection() throws Exception {
        // Arrange
        String json = "null";
        Collection<String> expected = new ArrayList<>();

        // Act
        Collection<String> result = deserializer.deserialize(json);

        // Assert
        assertNotNull(result);
        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }

    @Test(expected = Exception.class)
    public void testDeserialize_WithInvalidFormat_ThrowsException() throws Exception {
        // Arrange
        String json = "[]";

        // Act
        deserializer.deserialize(json);

        // Assert (Exception expected)
    }
}