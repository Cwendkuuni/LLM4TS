import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class SerializationUtilsTest {

    private SerializationUtils serializationUtils;

    @Before
    public void setUp() {
        serializationUtils = new SerializationUtils();
    }

    @Test
    public void testCloneWithNullObject() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerializeWithNullObject() {
        try {
            SerializationUtils.serialize(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testSerializeAndDeserializeObject() {
        SerializableObject obj = new SerializableObject("TestObject");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);

        byte[] serializedData = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        SerializableObject deserializedObj = (SerializableObject) SerializationUtils.deserialize(bais);

        assertEquals(obj, deserializedObj);
    }

    @Test
    public void testDeserializeWithNullInputStream() {
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testDeserializeWithInvalidSerializedData() {
        byte[] invalidSerializedData = {0x01, 0x02, 0x03};
        ByteArrayInputStream bais = new ByteArrayInputStream(invalidSerializedData);

        try {
            SerializationUtils.deserialize(bais);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // success
        }
    }

    // Test for custom ClassLoaderAwareObjectInputStream is omitted to avoid extra dependencies

    private static class SerializableObject implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        private String value;

        public SerializableObject(String value) {
            this.value = value;
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
            return value.equals(that.value);
        }

        @Override 
        public int hashCode() {
            return value.hashCode();
        }
    }
}