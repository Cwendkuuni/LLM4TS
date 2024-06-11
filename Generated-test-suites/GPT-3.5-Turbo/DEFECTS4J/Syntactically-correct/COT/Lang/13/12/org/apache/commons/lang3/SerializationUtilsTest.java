import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        MyObject obj = new MyObject();
        MyObject clone = SerializationUtils.clone(obj);
        assertNotSame(obj, clone);
    }

    @Test
    public void testSerialize() {
        MyObject obj = new MyObject();
        byte[] data = SerializationUtils.serialize(obj);
        assertNotNull(data);
    }

    @Test
    public void testDeserialize() {
        MyObject obj = new MyObject();
        byte[] data = SerializationUtils.serialize(obj);
        MyObject deserializeObj = (MyObject) SerializationUtils.deserialize(data);
        assertNotNull(deserializeObj);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() {
        assertNotNull(new SerializationUtils.ClassLoaderAwareObjectInputStream(null, null));
    }

    private static class MyObject implements Serializable {
        // Implementation of the class
    }

}