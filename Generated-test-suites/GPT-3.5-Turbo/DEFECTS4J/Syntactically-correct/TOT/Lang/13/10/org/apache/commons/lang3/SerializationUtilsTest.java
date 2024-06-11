import org.junit.Test;
import org.apache.commons.lang3.SerializationException;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone_withNullObject_returnsNull() {
        Object object = null;
        Object clonedObject = SerializationUtils.clone(object);
        assertNull(clonedObject);
    }

    @Test
    public void testClone_withSerializableObject_returnsClonedObject() {
        SerializableObject object = new SerializableObject("Hello", 5);
        SerializableObject clonedObject = SerializationUtils.clone(object);
        assertEquals(object, clonedObject);
        assertNotSame(object, clonedObject);
    }

    @Test(expected = SerializationException.class)
    public void testClone_withNonSerializableObject_throwsSerializationException() {
        NonSerializableObject object = new NonSerializableObject("Hello", 5);
        SerializationUtils.clone(object);
    }

    @Test
    public void testSerialize_withNullObject_throwsIllegalArgumentException() {
        Object object = null;
        try {
            SerializationUtils.serialize(object);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The OutputStream must not be null", e.getMessage());
        } catch (SerializationException e) {
            fail("Should throw IllegalArgumentException");
        }
    }

    @Test
    public void testSerialize_withSerializableObject_returnsByteArray() {
        SerializableObject object = new SerializableObject("Hello", 5);
        byte[] serializedData = SerializationUtils.serialize(object);
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }

    @Test(expected = SerializationException.class)
    public void testSerialize_withNonSerializableObject_throwsSerializationException() {
        NonSerializableObject object = new NonSerializableObject("Hello", 5);
        SerializationUtils.serialize(object);
    }

    @Test
    public void testDeserialize_withNullInputStream_throwsIllegalArgumentException() {
        try {
            SerializationUtils.deserialize(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The InputStream must not be null", e.getMessage());
        } catch (SerializationException e) {
            fail("Should throw IllegalArgumentException");
        }
    }

    @Test
    public void testDeserialize_withValidInput_returnsDeserializedObject() {
        SerializableObject object = new SerializableObject("Hello", 5);
        byte[] serializedData = SerializationUtils.serialize(object);
        SerializableObject deserializedObject = (SerializableObject) SerializationUtils.deserialize(serializedData);
        assertEquals(object, deserializedObject);
    }

    @Test(expected = SerializationException.class)
    public void testDeserialize_withInvalidInput_throwsSerializationException() {
        byte[] invalidData = new byte[]{1, 2, 3};
        SerializationUtils.deserialize(invalidData);
    }
}

class SerializableObject implements Serializable {
    private String message;
    private int value;

    public SerializableObject(String message, int value) {
        this.message = message;
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SerializableObject that = (SerializableObject) obj;
        return value == that.value && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + value;
        return result;
    }
}

class NonSerializableObject {
    private String message;
    private int value;

    public NonSerializableObject(String message, int value) {
        this.message = message;
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NonSerializableObject that = (NonSerializableObject) obj;
        return value == that.value && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + value;
        return result;
    }
}