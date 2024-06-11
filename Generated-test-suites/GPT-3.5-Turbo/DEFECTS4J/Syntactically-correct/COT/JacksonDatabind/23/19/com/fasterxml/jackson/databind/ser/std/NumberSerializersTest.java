import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        // Arrange
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();

        // Act
        NumberSerializers.addAll(allDeserializers);

        // Assert
        assertEquals(6, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }
    
    @Test
    public void testAddAllWithEmptyMap() {
        // Arrange
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
    
        // Act
        NumberSerializers.addAll(allDeserializers);
    
        // Assert
        assertEquals(6, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }
    
}