import org.junit.Test;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    // Test clone method
    @Test
    public void testClone() {
        // Create test object
        TestObject obj = new TestObject(10, "test");

        // Clone the object
        TestObject clonedObj = SerializationUtils.clone(obj);

        // Check if the cloned object is not null
        assertNotNull(clonedObj);

        // Check if the cloned object is equal to the original object
        assertEquals(obj, clonedObj);

        // Check if the cloned object is a different instance
        assertNotSame(obj, clonedObj);
    }

    // Test serialize method
    @Test
    public void testSerialize() {
        // Create test object
        TestObject obj = new TestObject(10, "test");

        // Serialize the object
        byte[] serializedObj = SerializationUtils.serialize(obj);

        // Check if the serialized object is not null
        assertNotNull(serializedObj);

        // Check if the serialized object has length greater than 0
        assertTrue(serializedObj.length > 0);
    }

    // Test deserialize method
    @Test
    public void testDeserialize() {
        // Create serialized object
        byte[] serializedObj = new byte[]{1, 2, 3, 4};

        // Deserialize the object
        Object obj = SerializationUtils.deserialize(serializedObj);

        // Check if the deserialized object is not null
        assertNotNull(obj);
    }

    // Test ClassLoaderAwareObjectInputStream resolveClass method
    @Test
    public void testResolveClass() throws Exception {
        // Create test object
        TestObject obj = new TestObject(10, "test");

        // Serialize the object
        byte[] serializedObj = SerializationUtils.serialize(obj);

        // Create ClassLoaderAwareObjectInputStream with null classLoader
        SerializationUtils.ClassLoaderAwareObjectInputStream in = 
            new SerializationUtils.ClassLoaderAwareObjectInputStream(
                new ByteArrayInputStream(serializedObj),
                null
            );

        // Deserialize the object
        Object deserializedObj = in.readObject();

        // Check if the deserialized object is not null
        assertNotNull(deserializedObj);

        // Check if the deserialized object is equal to the original object
        assertEquals(obj, deserializedObj);

        // Check if the deserialized object is a different instance
        assertNotSame(obj, deserializedObj);

        // Create ClassLoaderAwareObjectInputStream with specific classLoader
        ClassLoader classLoader = this.getClass().getClassLoader();
        in = new SerializationUtils.ClassLoaderAwareObjectInputStream(
            new ByteArrayInputStream(serializedObj),
            classLoader
        );

        // Deserialize the object
        Object resolvedObj = in.readObject();

        // Check if the resolved object is not null
        assertNotNull(resolvedObj);

        // Check if the resolved object is equal to the original object
        assertEquals(obj, resolvedObj);

        // Check if the resolved object is a different instance
        assertNotSame(obj, resolvedObj);

        // Check if the resolved object has the same class loader as the specified class loader
        assertSame(classLoader, resolvedObj.getClass().getClassLoader());
    }

    // Test IllegalArgumentException in serialize method with null outputStream
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeIllegalArgumentException() {
        // Create test object
        TestObject obj = new TestObject(10, "test");

        // Try to serialize the object with null outputStream
        SerializationUtils.serialize(obj, null);
    }

    // Test SerializationException in serialize method
    @Test(expected = SerializationException.class)
    public void testSerializeSerializationException() throws Exception {
        // Create test object
        TestObject obj = new TestObject(10, "test");

        // Create faulty outputStream that will throw IOException when writing object
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("IO Exception");
            }
        };

        // Try to serialize the object with faulty outputStream
        SerializationUtils.serialize(obj, outputStream);
    }

    // Test IllegalArgumentException in deserialize method with null inputStream
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeIllegalArgumentException() {
        // Try to deserialize with null inputStream
        SerializationUtils.deserialize(null);
    }

    // Test SerializationException in deserialize method
    @Test(expected = SerializationException.class)
    public void testDeserializeSerializationException() throws Exception {
        // Create faulty inputStream that will throw IOException when reading object
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("IO Exception");
            }
        };

        // Try to deserialize with faulty inputStream
        SerializationUtils.deserialize(inputStream);
    }

    // Test IllegalArgumentException in deserialize method with null objectData
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeIllegalArgumentException2() {
        // Try to deserialize with null objectData
        SerializationUtils.deserialize(null);
    }

    // Test SerializationException in deserialize method
    @Test(expected = SerializationException.class)
    public void testDeserializeSerializationException2() throws Exception {
        // Create test object
        TestObject obj = new TestObject(10, "test");

        // Serialize the object
        byte[] serializedObj = SerializationUtils.serialize(obj);

        // Create faulty objectData that is empty
        byte[] objectData = new byte[0];

        // Try to deserialize with faulty objectData
        SerializationUtils.deserialize(objectData);
    }

    // Helper class for testing
    private static class TestObject implements Serializable {
        private int intValue;
        private String stringValue;

        public TestObject(int intValue, String stringValue) {
            this.intValue = intValue;
            this.stringValue = stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TestObject that = (TestObject) obj;
            return intValue == that.intValue && stringValue.equals(that.stringValue);
        }

        @Override
        public int hashCode() {
            int result = intValue;
            result = 31 * result + stringValue.hashCode();
            return result;
        }
    }

}