
package org.apache.commons.lang3;

import static org.junit.Assert.*;

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

import org.junit.Before;
import org.junit.Test;

public class SerializationUtilsTest {

    private static final String TEST_STRING = "test";
    private static final int TEST_INT = 42;

    private TestObject testObject;

    @Before
    public void setUp() {
        testObject = new TestObject(TEST_STRING, TEST_INT);
    }

    @Test
    public void testClone() {
        TestObject clonedObject = SerializationUtils.clone(testObject);
        assertNotSame(testObject, clonedObject);
        assertEquals(testObject.getStringValue(), clonedObject.getStringValue());
        assertEquals(testObject.getIntValue(), clonedObject.getIntValue());
    }

    @Test
    public void testSerializeDeserialize() {
        byte[] serializedData = SerializationUtils.serialize(testObject);
        TestObject deserializedObject = SerializationUtils.deserialize(serializedData);
        assertNotSame(testObject, deserializedObject);
        assertEquals(testObject.getStringValue(), deserializedObject.getStringValue());
        assertEquals(testObject.getIntValue(), deserializedObject.getIntValue());
    }

    private static class TestObject implements Serializable {

        private static final long serialVersionUID = 1L;

        private String stringValue;
        private int intValue;

        public TestObject(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(stringValue);
            out.writeInt(intValue);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            stringValue = (String) in.readObject();
            intValue = in.readInt();
        }

    }

}

