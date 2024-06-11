package org.apache.commons.lang3;

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
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test object to clone
        TestObject original = new TestObject("foo", 10);

        // Clone object using SerializationUtils.clone()
        TestObject cloned = SerializationUtils.clone(original);

        // Assert that the cloned object is equal to the original
        assertEquals(original, cloned);
    }

    @Test
    public void testSerialize() {
        // Test object to serialize
        TestObject object = new TestObject("foo", 10);

        // Serialize object using SerializationUtils.serialize()
        byte[] serialized = SerializationUtils.serialize(object);

        // Deserialize serialized object using ObjectInputStream
        TestObject deserialized = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
            ObjectInputStream ois = new ObjectInputStream(bais);
            deserialized = (TestObject) ois.readObject();
            ois.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Assert that the deserialized object is equal to the original
        assertEquals(object, deserialized);
    }

    @Test
    public void testDeserialize() {
        // Test object to deserialize
        TestObject object = new TestObject("foo", 10);

        // Serialize object using ObjectOutputStream
        byte[] serialized = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
            serialized = baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Deserialize serialized object using SerializationUtils.deserialize()
        TestObject deserialized = (TestObject) SerializationUtils.deserialize(serialized);

        // Assert that the deserialized object is equal to the original
        assertEquals(object, deserialized);
    }
    
    // TestObject class for testing serialization
    private static class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int age;
        
        public TestObject(String name, int age) {
            this.name = name;
            this.age = age;
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
            TestObject other = (TestObject) obj;
            return name.equals(other.name) && age == other.age;
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