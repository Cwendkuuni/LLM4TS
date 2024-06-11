import static org.junit.Assert.*;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testCloneTypicalCase() {
        // typical case with a non-null Serializable object
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        
        Map<String, Integer> clone = SerializationUtils.clone(map);
        
        assertEquals(map, clone);
        assertNotSame(map, clone);
    }
    
    @Test
    public void testCloneEdgeCase() {
        // edge case with a null Serializable object
        assertNull(SerializationUtils.clone(null));
    }
    
    @Test(expected = SerializationException.class)
    public void testCloneErrorScenario() {
        // error scenario with a non-Serializable object
        SerializationUtils.clone(new Object());
    }
    
    @Test
    public void testSerializeTypicalCase() {
        // typical case with a non-null Serializable object
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        
        byte[] serialized = SerializationUtils.serialize(map);
        
        assertNotNull(serialized);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeEdgeCase() {
        // edge case with a null Serializable object
        SerializationUtils.serialize(null, System.out);
    }
    
    @Test(expected = SerializationException.class)
    public void testSerializeErrorScenario() {
        // error scenario with a non-Serializable object
        SerializationUtils.serialize(new Object(), System.out);
    }
    
    @Test
    public void testDeserializeTypicalCase() {
        // typical case with a non-null serialized object
        byte[] serialized = new byte[10];
        
        Object deserialized = SerializationUtils.deserialize(serialized);
        
        assertNotNull(deserialized);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeEdgeCase() {
        // edge case with a null serialized object
        SerializationUtils.deserialize(null);
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserializeErrorScenario() {
        // error scenario with an invalid serialized object
        byte[] serialized = new byte[]{0, 1, 2};
        
        SerializationUtils.deserialize(serialized);
    }
}