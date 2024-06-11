package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Serializable obj = new SerializableObject();
        Serializable cloneObj = SerializationUtils.clone(obj);
        
        assertEquals(obj, cloneObj);
    }
    
    @Test
    public void testSerialize() {
        Serializable obj = new SerializableObject();
        byte[] serializedObj = SerializationUtils.serialize(obj);
        
        assertNotNull(serializedObj);
    }
    
    @Test
    public void testDeserialize() {
        Serializable obj = new SerializableObject();
        byte[] serializedObj = SerializationUtils.serialize(obj);
        
        Serializable deserializedObj = SerializationUtils.deserialize(serializedObj);
        
        assertEquals(obj, deserializedObj);
    }
    
    @Test
    public void testDeserializeWithInputStream() throws IOException {
        Serializable obj = new SerializableObject();
        byte[] serializedObj = SerializationUtils.serialize(obj);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        Serializable deserializedObj = SerializationUtils.deserialize(bais);
        
        assertEquals(obj, deserializedObj);
    }

    @Test
    public void testDeserializeWithArray() {
        Serializable obj = new SerializableObject();
        byte[] serializedObj = SerializationUtils.serialize(obj);
        
        Serializable deserializedObj = SerializationUtils.deserialize(serializedObj);
        
        assertEquals(obj, deserializedObj);
    }
    
    //Helper class for testing serialization/deserialization
    private static final class SerializableObject implements Serializable {

        private static final long serialVersionUID = 1L;
        
        private String name;
        private int age;
        
        public SerializableObject() {
            this.name = "John Doe";
            this.age = 30;
        }
        
        public String getName() {
            return name;
        }
        
        public int getAge() {
            return age;
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
            
            return this.name.equals(other.getName()) && this.age == other.getAge();
        }
        
        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + name.hashCode();
            result = 31 * result + age;
            return result;
        }
    }
}