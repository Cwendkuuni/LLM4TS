package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        Map<String, String> original = new HashMap<>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        
        Map<String, String> cloned = SerializationUtils.clone(original);
        
        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals(original, cloned);
    }
    
    @Test
    public void testSerializeAndDeserialize() {
        TestObject obj = new TestObject(1, "Test");
        
        byte[] data = SerializationUtils.serialize(obj);
        assertNotNull(data);
        assertTrue(data.length > 0);
        
        TestObject deserialized = (TestObject) SerializationUtils.deserialize(data);
        assertNotNull(deserialized);
        assertEquals(obj, deserialized);
    }
    
    @Test
    public void testSerialize() {
        TestObject obj = new TestObject(1, "Test");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        
        byte[] data = baos.toByteArray();
        assertNotNull(data);
        assertTrue(data.length > 0);
    }
    
    @Test
    public void testDeserialize() {
        byte[] data = new byte[] { 0, 1, 2, 3, 4 };
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        
        Object deserialized = SerializationUtils.deserialize(bais);
        
        assertNotNull(deserialized);
        assertEquals(data, deserialized);
    }
    
    static class TestObject implements Serializable {
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
            TestObject other = (TestObject) obj;
            return id == other.id && name.equals(other.name);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
        
        @Override
        public String toString() {
            return "TestObject [id=" + id + ", name=" + name + "]";
        }
    }
}