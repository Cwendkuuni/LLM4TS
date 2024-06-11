import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.deserialize(null, null);
        String[] expected = new String[0];
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] actual = deserializer.handleNonArray(null, null);
        String[] expected = new String[1];
        expected[0] = null;
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object actual = deserializer.deserializeWithType(null, null, null);
        assertNull(actual);
    }

    @Test
    public void testContextual() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> actual = deserializer.createContextual(null, null);
        assertNull(actual);
    }

}