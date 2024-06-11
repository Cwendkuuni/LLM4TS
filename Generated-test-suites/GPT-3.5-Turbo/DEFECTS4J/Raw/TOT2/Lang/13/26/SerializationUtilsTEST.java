import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    private static final String TEST_STRING = "Test String";
    private static final int TEST_INT = 10;

    private SerializationUtils.ClassLoaderAwareObjectInputStream mockObjectInputStream;
    
    @Before
    public void setUp() {
        mockObjectInputStream = new SerializationUtils.ClassLoaderAwareObjectInputStream(null, null);
    }

    @Test
    public void testClone() {
        String clonedString = SerializationUtils.clone(TEST_STRING);
        assertEquals(TEST_STRING, clonedString);
        
        Integer clonedInt = SerializationUtils.clone(TEST_INT);
        assertEquals(TEST_INT, clonedInt.intValue());
    }

    @Test
    public void testSerializeDeserialize() {
        byte[] serializedData = SerializationUtils.serialize(TEST_STRING);
        String deserializedString = (String) SerializationUtils.deserialize(serializedData);
        assertEquals(TEST_STRING, deserializedString);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeWithNullObjectShouldThrowException() {
        SerializationUtils.serialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullOutputStreamShouldThrowException() {
        SerializationUtils.serialize(TEST_STRING, null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeWithNullInputStreamShouldThrowException() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullByteArrayShouldThrowException() {
        SerializationUtils.deserialize((byte[]) null);
    }

}