import static org.junit.Assert.*;

import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test with null object
        assertNull(SerializationUtils.clone(null));

        // Test with non-serializable object
        Object nonSerializableObject = new Object();
        try {
            SerializationUtils.clone(nonSerializableObject);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected exception
        }

        // Test with serializable object
        MyClass originalObject = new MyClass("Hello");
        MyClass clonedObject = SerializationUtils.clone(originalObject);
        assertEquals(originalObject.getText(), clonedObject.getText());
    }

    @Test
    public void testSerialize() throws IOException, ClassNotFoundException {
        // Test with null object
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, byteArrayOutputStream);
        byte[] serializedData = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        assertNull(SerializationUtils.deserialize(byteArrayInputStream));

        // Test with non-serializable object
        byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(new Object(), byteArrayOutputStream);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected exception
        }

        // Test with serializable object
        byteArrayOutputStream = new ByteArrayOutputStream();
        MyClass objectToSerialize = new MyClass("Hello");
        SerializationUtils.serialize(objectToSerialize, byteArrayOutputStream);
        byte[] serializedData2 = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(serializedData2);
        MyClass deserializedObject = (MyClass) SerializationUtils.deserialize(byteArrayInputStream2);
        assertEquals(objectToSerialize.getText(), deserializedObject.getText());
    }

    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        // Test with null input stream
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test with valid input stream
        MyClass objectToSerialize = new MyClass("Hello");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(objectToSerialize);
        byte[] serializedData = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        Object deserializedObject = SerializationUtils.deserialize(byteArrayInputStream);
        assertTrue(deserializedObject instanceof MyClass);
        assertEquals(objectToSerialize.getText(), ((MyClass) deserializedObject).getText());
    }

    private static class MyClass implements java.io.Serializable {
        private String text;

        public MyClass(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}