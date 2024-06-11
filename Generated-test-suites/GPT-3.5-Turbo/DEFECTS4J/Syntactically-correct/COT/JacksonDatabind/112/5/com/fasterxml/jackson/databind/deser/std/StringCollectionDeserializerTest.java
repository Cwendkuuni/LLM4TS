
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
            null, null, null);

    @Test
    public void testDeserialize() throws IOException {
        Collection<String> result = deserializer
                .deserialize(createJsonParser("[\"one\", \"two\", \"three\"]"), null);
        assertEquals(3, result.size());
        assertTrue(result.contains("one"));
        assertTrue(result.contains("two"));
        assertTrue(result.contains("three"));
    }

    @Test
    public void testDeserializeWithNullValues() throws IOException {
        Collection<String> result = deserializer
                .deserialize(createJsonParser("[null, \"two\", null]"), null);
        assertEquals(3, result.size());
        assertTrue(result.contains(null));
        assertTrue(result.contains("two"));
        assertTrue(result.contains(null));
    }

    @Test
    public void testDeserializeWithEmptyArray() throws IOException {
        Collection<String> result = deserializer.deserialize(createJsonParser("[]"), null);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeWithSingleValue() throws IOException {
        Collection<String> result = deserializer
                .deserialize(createJsonParser("\"one\""), null);
        assertEquals(1, result.size());
        assertTrue(result.contains("one"));
    }

    @Test
    public void testDeserializeWithInvalidToken() throws IOException {
        assertNull(deserializer.deserialize(createJsonParser("123"), null));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        assertNull(deserializer.deserializeWithType(createJsonParser("[]"), null, null));
    }

    private static JsonParser createJsonParser(String json) throws IOException {
        return new JsonFactory().createParser(json);
    }
}
