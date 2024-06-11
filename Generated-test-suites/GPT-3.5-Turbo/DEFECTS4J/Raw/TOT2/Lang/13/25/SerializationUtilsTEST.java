import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        SampleObject obj = new SampleObject("Test");
        SampleObject cloned = SerializationUtils.clone(obj);
        assertNotSame(obj, cloned);
        assertEquals(obj.getValue(), cloned.getValue());

        // Edge case
        assertNull(SerializationUtils.clone(null));

        // Error scenario
        try {
            SerializationUtils.clone(new Object());
            fail("SerializationException expected");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testSerialize() {
        // Typical use case
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SampleObject obj = new SampleObject("Test");
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();

        // Edge case
        assertEquals(22, serializedData.length);

        // Error scenario
        try {
            SerializationUtils.serialize(obj, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        SampleObject obj = new SampleObject("Test");
        byte[] serializedData = SerializationUtils.serialize(obj);
        SampleObject deserialized = (SampleObject) SerializationUtils.deserialize(serializedData);
        assertEquals(obj.getValue(), deserialized.getValue());

        // Edge case
        try {
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Error scenario
        byte[] invalidData = new byte[5];
        try {
            SerializationUtils.deserialize(invalidData);
            fail("SerializationException expected");
        } catch (SerializationException e) {
            // Expected
        }
    }

    private static class SampleObject implements Serializable {

        private static final long serialVersionUID = 1L;
        private String value;

        public SampleObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}