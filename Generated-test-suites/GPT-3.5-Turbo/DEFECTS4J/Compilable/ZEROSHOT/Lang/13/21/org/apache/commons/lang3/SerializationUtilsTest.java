package org.apache.commons.lang3;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SerializationUtilsTest {
    
    @Test
    public void testClone() {
        // Test object
        TestObject testObject = new TestObject();
        testObject.setValue(10);
        
        // Clone object
        TestObject clonedObject = SerializationUtils.clone(testObject);
        
        // Check if cloned object is not null
        Assert.assertNotNull(clonedObject);
        
        // Check if cloned object is different from original object
        Assert.assertNotSame(testObject, clonedObject);
        
        // Check if cloned object has the same value as original object
        Assert.assertEquals(testObject.getValue(), clonedObject.getValue());
    }
    
    @Test
    public void testSerialize() {
        // Test object
        TestObject testObject = new TestObject();
        testObject.setValue(15);
        
        // Serialize object
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(testObject, outputStream);
        
        // Deserialize object
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TestObject deserializedObject = (TestObject) SerializationUtils.deserialize(inputStream);
        
        // Check if deserialized object is not null
        Assert.assertNotNull(deserializedObject);
        
        // Check if deserialized object has the same value as original object
        Assert.assertEquals(testObject.getValue(), deserializedObject.getValue());
    }
    
    @Test
    public void testDeserialize() {
        // Test object
        TestObject testObject = new TestObject();
        testObject.setValue(20);
        
        // Serialize object
        byte[] objectData = SerializationUtils.serialize(testObject);
        
        // Deserialize object
        TestObject deserializedObject = (TestObject) SerializationUtils.deserialize(objectData);
        
        // Check if deserialized object is not null
        Assert.assertNotNull(deserializedObject);
        
        // Check if deserialized object has the same value as original object
        Assert.assertEquals(testObject.getValue(), deserializedObject.getValue());
    }
    
    private static class TestObject implements Serializable {
        private int value;
        
        public int getValue() {
            return value;
        }
        
        public void setValue(int value) {
            this.value = value;
        }
    }
}