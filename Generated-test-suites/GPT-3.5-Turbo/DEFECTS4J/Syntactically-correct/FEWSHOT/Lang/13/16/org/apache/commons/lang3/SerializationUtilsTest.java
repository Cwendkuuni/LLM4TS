import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        // Test with null object
        assertNull(SerializationUtils.clone(null));

        // Test with Serializable objects
        SerializableObject obj1 = new SerializableObject("test", 10);
        SerializableObject obj2 = SerializationUtils.clone(obj1);
        assertEquals(obj1, obj2);

        // Test with non-Serializable object
        NonSerializableObject obj3 = new NonSerializableObject("test");
        try {
            SerializationUtils.clone(obj3);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSerializeByteArray() {
        // Test with null object
        assertNull(SerializationUtils.serialize(null));

        // Test with Serializable objects
        SerializableObject obj1 = new SerializableObject("test", 10);
        byte[] data = SerializationUtils.serialize(obj1);
        SerializableObject obj2 = (SerializableObject) SerializationUtils.deserialize(data);
        assertEquals(obj1, obj2);

        // Test with non-Serializable object
        NonSerializableObject obj3 = new NonSerializableObject("test");
        try {
            SerializationUtils.serialize(obj3);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSerializeOutputStream() {
        // Test with null object
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, baos1);
        assertArrayEquals(new byte[0], baos1.toByteArray());

        // Test with Serializable objects
        SerializableObject obj1 = new SerializableObject("test", 10);
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj1, baos2);
        SerializableObject obj2 = (SerializableObject) SerializationUtils.deserialize(new ByteArrayInputStream(baos2.toByteArray()));
        assertEquals(obj1, obj2);

        // Test with non-Serializable object
        NonSerializableObject obj3 = new NonSerializableObject("test");
        try {
            SerializationUtils.serialize(obj3, new ByteArrayOutputStream());
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @Test
    public void testDeserializeInputStream() {
        // Test with null input stream
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test with Serializable objects
        SerializableObject obj1 = new SerializableObject("test", 10);
        byte[] data = SerializationUtils.serialize(obj1);
        SerializableObject obj2 = (SerializableObject) SerializationUtils.deserialize(new ByteArrayInputStream(data));
        assertEquals(obj1, obj2);

        // Test with non-Serializable object
        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[10]));
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @Test
    public void testDeserializeByteArray() {
        // Test with null byte array
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test with Serializable objects
        SerializableObject obj1 = new SerializableObject("test", 10);
        byte[] data = SerializationUtils.serialize(obj1);
        SerializableObject obj2 = (SerializableObject) SerializationUtils.deserialize(data);
        assertEquals(obj1, obj2);

        // Test with non-Serializable object
        try {
            SerializationUtils.deserialize(new byte[0]);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    private class SerializableObject implements Serializable {
        private String name;
        private int value;

        public SerializableObject(String name, int value) {
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

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SerializableObject other = (SerializableObject) obj;
            return Objects.equals(name, other.name) &&
                    value == other.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }
    }

    private class NonSerializableObject {
        private String name;

        public NonSerializableObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            NonSerializableObject other = (NonSerializableObject) obj;
            return Objects.equals(name, other.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}