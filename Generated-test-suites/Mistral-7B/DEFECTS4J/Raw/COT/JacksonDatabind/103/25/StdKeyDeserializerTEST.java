import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKeyWithValidBooleanKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = deserializer.deserializeKey("true", new DeserializationContext());
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDeserializeKeyWithValidByteKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object result = deserializer.deserializeKey("127", new DeserializationContext());
        assertEquals((Byte) 127, result);
    }

    @Test
    public void testDeserializeKeyWithValidShortKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object result = deserializer.deserializeKey("32767", new DeserializationContext());
        assertEquals((Short) 32767, result);
    }

    @Test
    public void testDeserializeKeyWithValidCharKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object result = deserializer.deserializeKey("A", new DeserializationContext());
        assertEquals((Character) 'A', result);
    }

    @Test
    public void testDeserializeKeyWithValidIntKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object result = deserializer.deserializeKey("42", new DeserializationContext());
        assertEquals(42, result.hashCode());
        assertEquals(Integer.class, result.getClass());
    }

    @Test
    public void testDeserializeKeyWithValidLongKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object result = deserializer.deserializeKey("9223372036854775807", new DeserializationContext());
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testDeserializeKeyWithValidFloatKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object result = deserializer.deserializeKey("3.14", new DeserializationContext());
        assertEquals(Float.parseFloat("3.14"), result, 0.001f);
    }

    @Test
    public void testDeserializeKeyWithValidDoubleKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object result = deserializer.deserializeKey("3.14159265358979323846264338327950288416", new DeserializationContext());
        assertEquals(3.14159265358979323846264338327950288416, result, 0.000001);
    }

    @Test
    public void testDeserializeKeyWithInvalidBooleanKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = deserializer.deserializeKey("invalidKey", new DeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeKeyWithInvalidByteKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        try {
            Object result = deserializer.deserializeKey("256", new DeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertInstanceOf(IllegalArgumentException.class, expected);
        }
    }

    @Test
    public void testDeserializeKeyWithInvalidShortKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        try {
            Object result = deserializer.deserializeKey("-32769", new DeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertInstanceOf(IllegalArgumentException.class, expected);
        }
    }

    @Test
    public void testDeserializeKeyWithInvalidCharKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        try {
            Object result = deserializer.deserializeKey("\u0000", new DeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertInstanceOf(IllegalArgumentException.class, expected);
        }
    }

    @Test(expected = IOException.class)
    public void testDeserializeKeyWithNullKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        deserializer.deserializeKey(null, new DeserializationContext());
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyWithUnsupportedType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        Object key = new Object() {
            @Override
            public String toString() {
                return "unsupportedType";
            }
        };
        Object result = deserializer.deserializeKey(key.toString(), new DeserializationContext());
        fail("Expected JsonProcessingException");
    }
}