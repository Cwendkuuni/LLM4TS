package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        // Create a test object
        TestObject obj = new TestObject();
        obj.setStringField("Test");
        obj.setIntField(123);
        obj.setDoubleField(3.14);

        // Clone the object
        TestObject clone = SerializationUtils.clone(obj);

        // Verify that the clone is equal to the original object
        assertEquals(obj.getStringField(), clone.getStringField());
        assertEquals(obj.getIntField(), clone.getIntField());
        assertEquals(obj.getDoubleField(), clone.getDoubleField(), 0.001);
    }

    @Test
    public void testSerializeAndDeserialize() {
        // Create a test object
        TestObject obj = new TestObject();
        obj.setStringField("Test");
        obj.setIntField(123);
        obj.setDoubleField(3.14);

        // Serialize the object
        byte[] serialized = SerializationUtils.serialize(obj);

        // Deserialize the object
        TestObject deserialized = (TestObject) SerializationUtils.deserialize(serialized);

        // Verify that the deserialized object is equal to the original object
        assertEquals(obj.getStringField(), deserialized.getStringField());
        assertEquals(obj.getIntField(), deserialized.getIntField());
        assertEquals(obj.getDoubleField(), deserialized.getDoubleField(), 0.001);
    }

    // Inner class representing a test object
    private static class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;

        private String stringField;
        private int intField;
        private double doubleField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public void setIntField(int intField) {
            this.intField = intField;
        }

        public double getDoubleField() {
            return doubleField;
        }

        public void setDoubleField(double doubleField) {
            this.doubleField = doubleField;
        }
    }
}