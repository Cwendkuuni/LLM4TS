
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.SerializationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        // Test case 1: Clone null object
        Assert.assertNull(SerializationUtils.clone(null));

        // Test case 2: Clone non-null object
        SerializableObject input = new SerializableObject("Test", 123);
        SerializableObject output = SerializationUtils.clone(input);
        Assert.assertNotSame(input, output);
        Assert.assertEquals(input, output);
    }

    @Test
    public void testSerialize() {
        // Test case 1: Serialize null object
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, outputStream1);
        byte[] bytes1 = outputStream1.toByteArray();
        Assert.assertEquals(0, bytes1.length);

        // Test case 2: Serialize non-null object
        SerializableObject input = new SerializableObject("Test", 123);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        SerializationUtils.serialize(input, outputStream2);
        byte[] bytes2 = outputStream2.toByteArray();
        Assert.assertNotEquals(0, bytes2.length);
    }

    @Test
    public void testDeserialize() {
        // Test case 1: Deserialize null object
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(new byte[0]);
        Object object1 = SerializationUtils.deserialize(inputStream1);
        Assert.assertNull(object1);

        // Test case 2: Deserialize non-null object
        SerializableObject input = new SerializableObject("Test", 123);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(input, outputStream);
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(outputStream.toByteArray());
        Object object2 = SerializationUtils.deserialize(inputStream2);
        Assert.assertTrue(object2 instanceof SerializableObject);
        SerializableObject output = (SerializableObject) object2;
        Assert.assertEquals(input, output);
    }

    private static class SerializableObject implements Serializable {
        private String name;
        private int value;

        public SerializableObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
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
            SerializableObject other = (SerializableObject) obj;
            return value == other.value && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + value;
            return result;
        }
    }
}
