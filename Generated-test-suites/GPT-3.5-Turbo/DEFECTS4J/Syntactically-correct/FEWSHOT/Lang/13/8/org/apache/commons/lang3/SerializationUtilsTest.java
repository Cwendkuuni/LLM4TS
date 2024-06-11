package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {
    
    // clone()
    @Test
    public void testClone() {
        TestObject obj1 = new TestObject(1, "test");
        TestObject obj2 = SerializationUtils.clone(obj1);
        assertNotSame(obj1, obj2);
        assertEquals(obj1, obj2);
    }
    
    // serialize()
    @Test
    public void testSerialize() {
        TestObject obj = new TestObject(1, "test");
        byte[] data = SerializationUtils.serialize(obj);
        assertNotNull(data);
        assertTrue(data.length > 0);
    }
    
    // serialize() with outputStream
    @Test
    public void testSerializeWithOutputStream() {
        TestObject obj = new TestObject(1, "test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] data = baos.toByteArray();
        assertNotNull(data);
        assertTrue(data.length > 0);
    }
    
    // deserialize() with inputStream
    @Test
    public void testDeserializeWithInputStream() {
        TestObject obj = new TestObject(1, "test");
        byte[] data = SerializationUtils.serialize(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        TestObject deserializedObj = (TestObject) SerializationUtils.deserialize(bais);
        assertNotNull(deserializedObj);
        assertEquals(obj, deserializedObj);
    }
    
    // deserialize() with byte array
    @Test
    public void testDeserializeWithByteArray() {
        TestObject obj = new TestObject(1, "test");
        byte[] data = SerializationUtils.serialize(obj);
        TestObject deserializedObj = (TestObject) SerializationUtils.deserialize(data);
        assertNotNull(deserializedObj);
        assertEquals(obj, deserializedObj);
    }
    
    // ClassLoaderAwareObjectInputStream
    @Test
    public void testClassLoaderAwareObjectInputStream() throws Exception {
        InputStream in = null;
        SerializationUtils.ClassLoaderAwareObjectInputStream objectInputStream = new SerializationUtils.ClassLoaderAwareObjectInputStream(in, getClass().getClassLoader());
        assertNotNull(objectInputStream);
    }
    
    // Internal class - TestObject
    private static class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private int id;
        private String name;
        
        public TestObject(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
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
            return id == that.id && name.equals(that.name);
        }
        
        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + name.hashCode();
            return result;
        }
    }

}