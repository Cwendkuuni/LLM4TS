import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testCloneNonNullObject() {
        Object obj = new Object();
        Object clonedObj = SerializationUtils.clone(obj);
        assertNotSame(obj, clonedObj);
    }

    @Test
    public void testCloneNullObject() {
        Object obj = null;
        Object clonedObj = SerializationUtils.clone(obj);
        assertNull(clonedObj);
    }

    @Test
    public void testSerializeNonNullObject() {
        Object obj = new Object();
        byte[] objectData = SerializationUtils.serialize(obj);
        assertNotNull(objectData);
    }

    @Test
    public void testSerializeNullObject() {
        Object obj = null;
        byte[] objectData = SerializationUtils.serialize(obj);
        assertNull(objectData);
    }

    @Test
    public void testDeserializeNonNullObject() {
        byte[] objectData = "serialized object data".getBytes();
        Object obj = SerializationUtils.deserialize(objectData);
        assertNotNull(obj);
    }

    @Test
    public void testDeserializeNullObject() {
        byte[] objectData = null;
        Object obj = SerializationUtils.deserialize(objectData);
        assertNull(obj);
    }
}