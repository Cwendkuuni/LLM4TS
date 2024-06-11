
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    // Test Data
    private static final String TEST_STRING = "test";
    private static final Integer TEST_INTEGER = 123;
    private static final Map<String, Object> TEST_MAP = new HashMap<String, Object>() {
        {
            put("key1", "value1");
            put("key2", 123);
        }
    };

    @Test
    public void testClone_NullObject_ReturnNull() {
        // Arrange
        Object object = null;

        // Act
        Object clonedObject = SerializationUtils.clone(object);

        // Assert
        Assert.assertNull(clonedObject);
    }

    @Test
    public void testClone_SerializableObject_ReturnClone() {
        // Arrange
        SerializableObject object = new SerializableObject(TEST_STRING, TEST_INTEGER);

        // Act
        SerializableObject clonedObject = SerializationUtils.clone(object);

        // Assert
        Assert.assertNotSame(object, clonedObject);
        Assert.assertEquals(object.getStringValue(), clonedObject.getStringValue());
        Assert.assertEquals(object.getIntegerValue(), clonedObject.getIntegerValue());
    }

    @Test
    public void testSerialize_DeserializeObject_ReturnSameObject() {
        // Arrange
        SerializableObject object = new SerializableObject(TEST_STRING, TEST_INTEGER);

        // Act
        byte[] serializedData = SerializationUtils.serialize(object);
        SerializableObject deserializedObject = (SerializableObject) SerializationUtils.deserialize(serializedData);

        // Assert
        Assert.assertNotSame(object, deserializedObject);
        Assert.assertEquals(object.getStringValue(), deserializedObject.getStringValue());
        Assert.assertEquals(object.getIntegerValue(), deserializedObject.getIntegerValue());
    }

    @Test
    public void testSerialize_NullObject_ReturnEmptyArray() {
        // Arrange
        Object object = null;

        // Act
        byte[] serializedData = SerializationUtils.serialize(object);

        // Assert
        Assert.assertNotNull(serializedData);
        Assert.assertEquals(0, serializedData.length);
    }

    @Test
    public void testDeserialize_EmptyArray_ReturnNull() {
        // Arrange
        byte[] serializedData = new byte[0];

        // Act
        Object deserializedObject = SerializationUtils.deserialize(serializedData);

        // Assert
        Assert.assertNull(deserializedObject);
    }

    @Test(expected = SerializationException.class)
    public void testDeserialize_InvalidData_ThrowSerializationException() {
        // Arrange
        byte[] serializedData = "invalidData".getBytes();

        // Act
        SerializationUtils.deserialize(serializedData);

        // Assert
        // SerializationException is expected
    }

    @Test
    public void testClassLoaderAwareObjectInputStream_ResolveClass_Success() throws IOException, ClassNotFoundException {
        // Arrange
        String className = "org.apache.commons.lang3.SerializationUtilsTest$SerializableObject";
        ClassLoaderAwareObjectInputStream stream = new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(new byte[0]), getClass().getClassLoader());

        // Act
        Class<?> resolvedClass = stream.resolveClass(new CustomObjectStreamClass(className));

        // Assert
        Assert.assertNotNull(resolvedClass);
        Assert.assertEquals(className, resolvedClass.getName());
    }

    @Test
    public void testClassLoaderAwareObjectInputStream_ResolveClass_ThrowClassNotFoundException() throws IOException {
        // Arrange
        String className = "org.apache.commons.lang3.SerializationUtilsTest$NonExistentClass";
        ClassLoaderAwareObjectInputStream stream = new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(new byte[0]), getClass().getClassLoader());

        // Act
        try {
            stream.resolveClass(new CustomObjectStreamClass(className));
            Assert.fail("Expected ClassNotFoundException to be thrown");
        } catch (ClassNotFoundException ex) {
            // Assert
            Assert.assertEquals(className, ex.getMessage());
        }
    }

    // Serializable Object for Testing
    private static class SerializableObject implements java.io.Serializable {
        private String stringValue;
        private Integer integerValue;

        public SerializableObject(String stringValue, Integer integerValue) {
            this.stringValue = stringValue;
            this.integerValue = integerValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public Integer getIntegerValue() {
            return integerValue;
        }
    }

    // Custom ObjectStreamClass
    private static class CustomObjectStreamClass implements java.io.ObjectStreamClass {
        private String name;

        public CustomObjectStreamClass(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSerialVersionUID() {
            return 0;
        }

        // Other methods are not implemented for testing purposes
    }
}
