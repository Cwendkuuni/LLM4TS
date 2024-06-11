package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.commons.lang3.SerializationUtils;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        TestClass obj = new TestClass("Test");
        TestClass clonedObj = SerializationUtils.clone(obj);

        assertNotSame(obj, clonedObj);
        assertEquals(obj.getName(), clonedObj.getName());
    }

    @Test
    public void testSerializeDeserialize() {
        TestClass obj = new TestClass("Test");
        byte[] serializedObj = SerializationUtils.serialize(obj);
        TestClass deserializedObj = (TestClass) SerializationUtils.deserialize(serializedObj);

        assertNotSame(obj, deserializedObj);
        assertEquals(obj.getName(), deserializedObj.getName());
    }

    private static class TestClass implements Serializable {
        private String name;

        public TestClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}