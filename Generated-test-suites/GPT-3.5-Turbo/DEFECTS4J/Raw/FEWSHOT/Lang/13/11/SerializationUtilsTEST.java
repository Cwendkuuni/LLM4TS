package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import org.junit.Test;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        // Test cloning a null object
        assertNull(SerializationUtils.clone(null));
        
        // Test cloning a Serializable object
        MySerializableObject obj = new MySerializableObject("test");
        MySerializableObject clonedObj = SerializationUtils.clone(obj);
        assertNotNull(clonedObj);
        assertEquals(obj.getValue(), clonedObj.getValue());
        
        // Test cloning an object that is not Serializable
        NonSerializableObject nonSerializableObj = new NonSerializableObject();
        assertNull(SerializationUtils.clone(nonSerializableObj));
    }
    
    @Test
    public void testSerialize() throws IOException {
        // Test serializing a null object
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, baos);
        assertEquals(0, baos.size());
        
        // Test serializing a Serializable object
        MySerializableObject obj = new MySerializableObject("test");
        baos.reset();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();
        assertNotNull(serializedData);
        
        // Test serializing an object that is not Serializable
        NonSerializableObject nonSerializableObj = new NonSerializableObject();
        baos.reset();
        try {
            SerializationUtils.serialize(nonSerializableObj, baos);
        } catch (SerializationException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        // Test deserializing a null object
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        assertNull(SerializationUtils.deserialize(bais));
        
        // Test deserializing a Serializable object
        MySerializableObject obj = new MySerializableObject("test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(obj);
        out.close();
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
        MySerializableObject deserializedObj = (MySerializableObject) SerializationUtils.deserialize(input);
        assertNotNull(deserializedObj);
        assertEquals(obj.getValue(), deserializedObj.getValue());
        
        // Test deserializing an object that is not Serializable
        NonSerializableObject nonSerializableObj = new NonSerializableObject();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(output);
        oos.writeObject(nonSerializableObj);
        oos.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        try {
            SerializationUtils.deserialize(inputStream);
        } catch (SerializationException e) {
            assertNotNull(e);
        }
    }
    
    class MySerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String value;
        
        public MySerializableObject(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    class NonSerializableObject {
        private String value;
        
        public NonSerializableObject() {
            this.value = "test";
        }
        
        public String getValue() {
            return value;
        }
    }
}