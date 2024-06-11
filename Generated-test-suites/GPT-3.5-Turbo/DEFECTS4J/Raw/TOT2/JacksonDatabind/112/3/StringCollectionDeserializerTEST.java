import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = null; // Create mock parser for testing
        DeserializationContext context = null; // Create mock context for testing
        Collection<String> result = new ArrayList<>();
        Collection<String> expected = Arrays.asList("String1", "String2", "String3");

        deserializer.deserialize(parser, context, result);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = null; // Create mock parser for testing
        DeserializationContext context = null; // Create mock context for testing
        Collection<String> result = new ArrayList<>();
        Collection<String> expected = Arrays.asList("CustomString1", "CustomString2", "CustomString3");

        // Mock custom deserializer
        deserializer.deserializeUsingCustom(parser, context, result, (p, ctxt) -> "Custom" + p.getValueAsString());

        assertEquals(expected, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        JsonParser parser = null; // Create mock parser for testing
        DeserializationContext context = null; // Create mock context for testing
        Collection<String> result = new ArrayList<>();
        String value = "SingleValue";
        Collection<String> expected = Arrays.asList(value);

        deserializer.handleNonArray(parser, context, result);

        assertEquals(expected, result);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        DeserializationContext context = null; // Create mock context for testing

        deserializer.createContextual(context, null);

        assertNotNull(deserializer);
    }

    @Test
    public void testIsCachable() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);

        boolean result = deserializer.isCachable();

        assertEquals(true, result);
    }

}