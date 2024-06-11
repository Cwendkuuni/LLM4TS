import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {
    
    // Test case for clone method
    @Test
    public void testClone() {
        // Typical use case
        Person person = new Person("John", 25);
        Person clonedPerson = SerializationUtils.clone(person);
        assertEquals(person.getName(), clonedPerson.getName());
        assertEquals(person.getAge(), clonedPerson.getAge());
        
        // Edge case: cloning null object
        Person nullPerson = null;
        Person clonedNullPerson = SerializationUtils.clone(nullPerson);
        assertNull(clonedNullPerson);
        
        // Edge case: cloning empty object
        Person emptyPerson = new Person("", 0);
        Person clonedEmptyPerson = SerializationUtils.clone(emptyPerson);
        assertEquals(emptyPerson.getName(), clonedEmptyPerson.getName());
        assertEquals(emptyPerson.getAge(), clonedEmptyPerson.getAge());
        
        // Error scenario: cloning non-serializable object
        NonSerializableObject nonSerializableObject = new NonSerializableObject();
        assertThrows(SerializationException.class, () -> {
            SerializationUtils.clone(nonSerializableObject);
        });
    }
    
    // Test case for serialize (object, outputStream) method
    @Test
    public void testSerializeWithOutputStream() {
        // Typical use case
        Person person = new Person("Jane", 30);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(person, outputStream);
        byte[] serializedData = outputStream.toByteArray();
        assertNotNull(serializedData);
        
        // Error scenario: passing null outputStream
        Person nullPerson = new Person("Null", 0);
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.serialize(nullPerson, null);
        });
    }
    
    // Test case for serialize (object) method
    @Test
    public void testSerialize() {
        // Typical use case
        Car car = new Car("Toyota", 2020);
        byte[] serializedData = SerializationUtils.serialize(car);
        assertNotNull(serializedData);

        // Error scenario: passing null object
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.serialize(null);
        });
    }
    
    // Test case for deserialize (inputStream) method
    @Test
    public void testDeserializeWithInputStream() {
        // Typical use case
        byte[] serializedData = new byte[] {1, 2, 3, 4};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
        assertNotNull(deserializedObject);
        
        // Error scenario: passing null inputStream
        byte[] nullSerializedData = new byte[] {4, 3, 2, 1};
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.deserialize(null);
        });
    }
    
    // Test case for deserialize (objectData) method
    @Test
    public void testDeserialize() {
        // Typical use case
        byte[] serializedData = new byte[] {4, 3, 2, 1};
        Object deserializedObject = SerializationUtils.deserialize(serializedData);
        assertNotNull(deserializedObject);
        
        // Error scenario: passing null objectData
        byte[] nullSerializedData = new byte[] {1, 2, 3, 4};
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.deserialize(null);
        });
    }

}