import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FromStringDeserializerTest {
    
    private static final String VALUE = "testvalue";

    private FromStringDeserializer<Object> deserializer;

    @Before
    public void setup() {
        deserializer = new FromStringDeserializer.Std(String.class, 0);
    }

    @Test
    public void testDeserialize_ValidValue_ReturnsDeserializedObject() throws Exception {
        Object result = deserializer.deserialize(VALUE, null);
        assertEquals(VALUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidValue_ThrowsIllegalArgumentException() throws Exception {
        deserializer.deserialize(null, null);
    }

    @Test
    public void testDeserialize_EmptyValue_ReturnsNull() throws Exception {
        Object result = deserializer.deserialize("", null);
        assertNull(result);
    }

    @Test
    public void testDeserialize_EdgeCaseValue_ReturnsDeserializedObject() throws Exception {
        Object result = deserializer.deserialize(" ", null);
        assertEquals(" ", result);
    }
}