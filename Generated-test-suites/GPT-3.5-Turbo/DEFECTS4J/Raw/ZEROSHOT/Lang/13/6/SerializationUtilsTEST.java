import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test case 1: Clone a null object
        Object object1 = null;
        Object clone1 = SerializationUtils.clone(object1);
        Assert.assertEquals(null, clone1);

        // Test case 2: Clone an object
        SerializableClass object2 = new SerializableClass("Test", 10);
        SerializableClass clone2 = SerializationUtils.clone(object2);
        Assert.assertEquals(object2, clone2);
        Assert.assertNotSame(object2, clone2);
    }

    @Test
    public void testSerialize() throws IOException {
        // Test case 1: Serialize a null object
        Object object1 = null;
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        SerializationUtils.serialize(object1, outputStream1);
        byte[] bytes1 = outputStream1.toByteArray();
        Assert.assertEquals(0, bytes1.length);

        // Test case 2: Serialize an object
        SerializableClass object2 = new SerializableClass("Test", 10);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        SerializationUtils.serialize(object2, outputStream2);
        byte[] bytes2 = outputStream2.toByteArray();
        Assert.assertNotEquals(0, bytes2.length);
    }

    @Test
    public void testDeserialize() throws IOException {
        // Test case 1: Deserialize an empty byte array
        byte[] bytes1 = new byte[0];
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(bytes1);
        Object object1 = SerializationUtils.deserialize(inputStream1);
        Assert.assertEquals(null, object1);

        // Test case 2: Deserialize a serialized object
        SerializableClass object2 = new SerializableClass("Test", 10);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(outputStream2);
        objectOutputStream2.writeObject(object2);
        byte[] bytes2 = outputStream2.toByteArray();
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(bytes2);
        Object object22 = SerializationUtils.deserialize(inputStream2);
        Assert.assertEquals(object2, object22);
    }

    private static class SerializableClass implements Serializable {
        private String name;
        private int value;

        public SerializableClass(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

}