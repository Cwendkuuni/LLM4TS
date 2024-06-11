import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        SerializationUtils.serializationExceptionTest();
    }

    @Test
    public void testSerialize() {
        SerializationUtils.serializationExceptionTest();
    }

    @Test
    public void testSerializeByteArray() {
        SerializationUtils.serializationExceptionTest();
    }

    @Test
    public void testDeserialize() {
        SerializationUtils.serializationExceptionTest();
    }

    @Test
    public void testDeserializeByteArray() {
        SerializationUtils.serializationExceptionTest();
    }
    
}

class SerializationUtils {

    public static void serializationExceptionTest() {
        try {
            SerializationUtils.serialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
        
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
        
        try {
            SerializationUtils.deserialize(new byte[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
        
        try {
            SerializationUtils.clone(null);
            fail("Expected SerializationException");
        } catch (SerializationException ex) {
            // Expected exception
        }
    }
    
}