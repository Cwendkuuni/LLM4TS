import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectStreamClass;

public class SerializationUtilsTest {

    @Test
    public void testCloneNullObject() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testCloneDeepCloneObjects() {
        // Test deep cloning of objects
        Person originalPerson = new Person("John", 30);
        Person clonedPerson = SerializationUtils.clone(originalPerson);

        assertNotSame(originalPerson, clonedPerson);
        assertEquals(originalPerson.getName(), clonedPerson.getName());
        assertEquals(originalPerson.getAge(), clonedPerson.getAge());
    }

    @Test
    public void testSerializeNullObject() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, outputStream);

        byte[] result = outputStream.toByteArray();
        assertEquals(0, result.length);
    }

    @Test
    public void testSerializeObject() {
        // Test serialization of an object
        Person person = new Person("Alice", 25);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(person, outputStream);

        byte[] result = outputStream.toByteArray();
        assertTrue(result.length > 0);
    }

    @Test
    public void testDeserializeObject() {
        // Test deserialization of an object
        Person originalPerson = new Person("Bob", 35);
        byte[] objectData = SerializationUtils.serialize(originalPerson);

        Person deserializedPerson = (Person) SerializationUtils.deserialize(objectData);
        assertNotNull(deserializedPerson);
        assertEquals(originalPerson.getName(), deserializedPerson.getName());
        assertEquals(originalPerson.getAge(), deserializedPerson.getAge());
    }

    @Test
    public void testDeserializeNullData() {
        assertNull(SerializationUtils.deserialize((byte[]) null));
    }

    @Test
    public void testDeserializeNullInputStream() {
        try {
            SerializationUtils.deserialize((ByteArrayInputStream) null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public class Person implements Serializable {
        private String name;
        private int age;

        // Constructor, getters, setters

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Person person = (Person) obj;
            return age == person.age && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }
}