import org.junit.Test;
import org.apache.commons.lang3.*;
import static org.junit.Assert.*;

public class SerializationUtilsTest {
  
  @Test
  public void testClone() {
    // Test cloning a null object
    assertNull(SerializationUtils.clone(null));
    
    // Test cloning a Serializable object
    Person person = new Person("John", 25);
    Person clone = SerializationUtils.clone(person);
    assertEquals(person.getName(), clone.getName());
    assertEquals(person.getAge(), clone.getAge());
    
    // Test cloning an object that is not Serializable
    Map<String, String> nonSerializableMap = new HashMap<>();
    nonSerializableMap.put("key1", "value1");
    nonSerializableMap.put("key2", "value2");
    assertNull(SerializationUtils.clone(nonSerializableMap));
  }

  @Test
  public void testSerialize() {
    // Test serializing an object to an OutputStream
    Person person = new Person("John", 25);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    SerializationUtils.serialize(person, baos);
    byte[] objectData = baos.toByteArray();
    
    // Test serializing an object to a byte array
    byte[] expectedData = SerializationUtils.serialize(person);
    assertArrayEquals(expectedData, objectData);
  }

  @Test
  public void testDeserialize() {
    // Test deserializing an object from an InputStream
    byte[] objectData = {1, 2, 3, 4};
    ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
    Object deserializedObject = SerializationUtils.deserialize(bais);

    // Test deserializing an object from a byte array
    Object expectedObject = SerializationUtils.deserialize(objectData);
    assertEquals(expectedObject, deserializedObject);
  }

  private static class Person implements Serializable {
    private String name;
    private int age;

    public Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }
  }
}