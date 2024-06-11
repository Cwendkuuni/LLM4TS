import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class SerializationUtilsTest {

    private static final String TEST_STRING = "This is a test string";

    // Test clone method
    @Test
    public void testClone() {
        TestClass obj = new TestClass(TEST_STRING);
        TestClass clonedObj = SerializationUtils.clone(obj);
        Assert.assertNotSame(obj, clonedObj);
        Assert.assertEquals(obj.getData(), clonedObj.getData());
    }

    // Test serialize method
    @Test
    public void testSerialize() {
        TestClass obj = new TestClass(TEST_STRING);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
      
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            TestClass deserializedObj = (TestClass) ois.readObject();
            Assert.assertEquals(obj.getData(), deserializedObj.getData());
        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("An exception occurred during serialization/deserialization");
        }
    }

    // Test deserialize method
    @Test
    public void testDeserialize() {
        TestClass obj = new TestClass(TEST_STRING);
        byte[] serializedData = serialize(obj);
        TestClass deserializedObj = (TestClass) deserialize(serializedData);
        Assert.assertEquals(obj.getData(), deserializedObj.getData());
    }

    // Helper method to serialize an object
    private byte[] serialize(Serializable obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            Assert.fail("An exception occurred during serialization");
            return null;
        }
    }

    // Helper method to deserialize an object
    private Object deserialize(byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("An exception occurred during deserialization");
            return null;
        }
    }

    // TestClass for serialization test
    private static class TestClass implements Serializable {
        private String data;

        public TestClass(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            byte[] encryptedData = encryptData(data.getBytes(StandardCharsets.UTF_8));
            out.writeObject(encryptedData);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            byte[] encryptedData = (byte[]) in.readObject();
            this.data = new String(decryptData(encryptedData), StandardCharsets.UTF_8);
        }

        private byte[] encryptData(byte[] data) {
            // Implement encryption logic
            return data;
        }

        private byte[] decryptData(byte[] encryptedData) {
            // Implement decryption logic
            return encryptedData;
        }
    }
}