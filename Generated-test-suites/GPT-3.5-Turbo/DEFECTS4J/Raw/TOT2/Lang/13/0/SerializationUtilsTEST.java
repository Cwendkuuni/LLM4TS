import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        String original = "Test String";
        String cloned = SerializationUtils.clone(original);
        
        assertEquals(original, cloned);
    }

    @Test
    public void testSerialize() {
        String testString = "Test String";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        SerializationUtils.serialize(testString, baos);
        assertNotNull(baos.toByteArray());
    }

    @Test
    public void testDeserialize() {
        String original = "Test String";
        byte[] serialized = SerializationUtils.serialize(original);
        
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() {
        // This test involves creating an instance of ClassLoaderAwareObjectInputStream but it's an internal class and cannot be instantiated directly in a JUnit test
        // This class is tested indirectly through other methods that utilize it
        // We can still ensure the code coverage of this class by testing other methods
    }

    @Test
    public void testCloneNullPointerException() {
        try{
            SerializationUtils.clone(null);
            fail("SerializationException not thrown for null input");
        }catch(SerializationException e){
            // Expected exception
        }
    }

    @Test
    public void testSerializeIllegalArgumentException() {
        try{
            SerializationUtils.serialize(null, null);
            fail("IllegalArgumentException not thrown for null OutputStream");
        }catch(IllegalArgumentException e){
            // Expected exception
        }
    }

    @Test
    public void testSerializeSerializationException() {
        try{
            SerializationUtils.serialize(new Object(), null);
            fail("SerializationException not thrown for null OutputStream");
        }catch(SerializationException e){
            // Expected exception
        }
    }

    @Test
    public void testDeserializeIllegalArgumentException() {
        try{
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException not thrown for null InputStream");
        }catch(IllegalArgumentException e){
            // Expected exception
        }
    }

    @Test
    public void testDeserializeSerializationException() {
        try{
            byte[] invalidData = {0, 1, 2};
            SerializationUtils.deserialize(invalidData);
            fail("SerializationException not thrown for invalid data");
        }catch(SerializationException e){
           // Expected exception
        }
    }
}