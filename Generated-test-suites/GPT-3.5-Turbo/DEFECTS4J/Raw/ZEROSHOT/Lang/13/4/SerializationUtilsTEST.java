import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        
        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);
        
        assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testSerialize() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, baos);
        
        byte[] serializedBytes = baos.toByteArray();
        
        assertNotNull(serializedBytes);
        assertTrue(serializedBytes.length > 0);
    }

    @Test
    public void testDeserialize() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        
        byte[] serializedBytes = baos.toByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);
        
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(bais);
        
        assertEquals(map, deserializedMap);
    }

    @Test
    public void testDeserializeBytes() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        
        byte[] serializedBytes = baos.toByteArray();
        
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(serializedBytes);
        
        assertEquals(map, deserializedMap);
    }
}