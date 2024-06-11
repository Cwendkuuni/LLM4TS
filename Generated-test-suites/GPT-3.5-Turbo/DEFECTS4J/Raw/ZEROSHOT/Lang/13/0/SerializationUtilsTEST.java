import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // create a test object
        TestObject obj = new TestObject("test", 10);

        // clone the object using SerializationUtils
        TestObject clone = SerializationUtils.clone(obj);

        // assert that the cloned object is not null and is equal to the original object
        Assert.assertNotNull(clone);
        Assert.assertEquals(obj, clone);
    }

    @Test
    public void testSerialize() {
        // create a test object
        TestObject obj = new TestObject("test", 10);

        // serialize the object to a byte array
        byte[] serialized = SerializationUtils.serialize(obj);

        // assert that the serialized byte array is not null
        Assert.assertNotNull(serialized);
    }

    @Test
    public void testDeserialize() {
        // create a test object
        TestObject obj = new TestObject("test", 10);

        // serialize the object to a byte array
        byte[] serialized = SerializationUtils.serialize(obj);

        // deserialize the byte array to an object
        TestObject deserialized = (TestObject) SerializationUtils.deserialize(serialized);

        // assert that the deserialized object is not null and is equal to the original object
        Assert.assertNotNull(deserialized);
        Assert.assertEquals(obj, deserialized);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        // create a test object
        TestObject obj = new TestObject("test", 10);

        // serialize the object to a byte array
        byte[] serialized = SerializationUtils.serialize(obj);

        // create a ClassLoaderAwareObjectInputStream with a custom ClassLoader
        ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(serialized), this.getClass().getClassLoader());

        // deserialize the object using the ClassLoaderAwareObjectInputStream
        TestObject deserialized = (TestObject) in.readObject();

        // assert that the deserialized object is not null and is equal to the original object
        Assert.assertNotNull(deserialized);
        Assert.assertEquals(obj, deserialized);

        // close the input stream
        in.close();
    }

    static class TestObject implements Serializable {
        private String name;
        private int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestObject that = (TestObject) o;
            return value == that.value && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }
    }
}