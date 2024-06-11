import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SerializationUtilsTest {

    // Clone test
     @Test
     public void testClone() throws SerializationException {
         HashMap<String, String> originalMap = new HashMap<>();
         originalMap.put("one", "one");
         originalMap.put("two", "two");

         HashMap<String, String> clonedMap = SerializationUtils.clone(originalMap);

         assertNotNull(clonedMap);
         assertEquals(originalMap, clonedMap);
     }

     // Serialize test
     @Test
     public void testSerialize() throws SerializationException, IOException {
         HashMap<String, String> originalMap = new HashMap<>();
         originalMap.put("one", "one");
         originalMap.put("two", "two");

         byte[] serializedMap = SerializationUtils.serialize(originalMap);

         ByteArrayInputStream bais = new ByteArrayInputStream(serializedMap);
         ObjectInputStream ois = new ObjectInputStream(bais);
         HashMap<String, String> deserializedMap = (HashMap<String, String>) ois.readObject();

         assertNotNull(deserializedMap);
         assertEquals(originalMap, deserializedMap);
     }

     // Deserialize test
     @Test
     public void testDeserialize() throws SerializationException, IOException, ClassNotFoundException {
         byte[] serializedMapBytes = SerializationUtils.serialize(new HashMap<>());

         ByteArrayInputStream bais = new ByteArrayInputStream(serializedMapBytes);
         ObjectInputStream ois = new ObjectInputStream(bais);
         Object readObject = ois.readObject();

         HashMap<String, String> deserializedMap = (HashMap<String, String>) readObject;
         assertNotNull(deserializedMap);
         assertEquals(0, deserializedMap.size());
     }

     // Test exception handling during cloning
     @Test(expected = SerializationException.class)
     public void testCloneException() throws SerializationException {
         SerializationUtils.clone(null);
     }

     // Test exception handling during serialization
     @Test(expected = SerializationException.class)
     public void testSerializeException() throws SerializationException, IOException {
         SerializationUtils.serialize(null);
     }
}