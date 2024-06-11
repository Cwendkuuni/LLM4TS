import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        MyClass obj1 = new MyClass("Object 1", 10);
        MyClass cloneObj1 = null;
        try {
            cloneObj1 = (MyClass) SerializationUtils.clone(obj1);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(obj1.getName(), cloneObj1.getName());
        Assert.assertEquals(obj1.getValue(), cloneObj1.getValue());

        // Edge case: object is null
        MyClass obj2 = null;
        MyClass cloneObj2 = null;
        try {
            cloneObj2 = (MyClass) SerializationUtils.clone(obj2);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        Assert.assertNull(cloneObj2);
    }

    @Test
    public void testSerialize() {
        // Typical use case
        MyClass obj1 = new MyClass("Object 1", 10);
        byte[] serializedObj1 = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SerializationUtils.serialize(obj1, baos);
            serializedObj1 = baos.toByteArray();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(serializedObj1);

        // Edge case: object is null
        MyClass obj2 = null;
        byte[] serializedObj2 = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SerializationUtils.serialize(obj2, baos);
            serializedObj2 = baos.toByteArray();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(serializedObj2);
        Assert.assertEquals(0, serializedObj2.length);
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        MyClass obj1 = new MyClass("Object 1", 10);
        byte[] serializedObj1 = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(obj1);
            out.close();
            serializedObj1 = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object deserializedObj1 = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObj1));
        Assert.assertEquals(obj1.getName(), ((MyClass) deserializedObj1).getName());
        Assert.assertEquals(obj1.getValue(), ((MyClass) deserializedObj1).getValue());

        // Edge case: object is null
        byte[] serializedObj2 = null;
        Object deserializedObj2 = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObj2));
        Assert.assertNull(deserializedObj2);
    }

    private static class MyClass implements Serializable {
        private String name;
        private int value;

        public MyClass(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}