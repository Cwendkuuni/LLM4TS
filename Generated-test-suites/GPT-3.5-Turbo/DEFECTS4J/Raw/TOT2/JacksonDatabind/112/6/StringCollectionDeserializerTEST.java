
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

public class StringCollectionDeserializerTest {

    @Test
    public void testStringCollectionDeserializerConstructor() {
        JavaType collectionType = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<?> valueDeser = null;
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, valueDeser, valueInstantiator);

        assertNotNull(deserializer);
        assertEquals(collectionType, deserializer.getContentType());
        assertEquals(valueDeser, deserializer.getContentDeserializer());
        assertEquals(valueInstantiator, deserializer.getValueInstantiator());
    }

    @Test
    public void testDeserializeMethod() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        List<String> result = new ArrayList<>();
        Collection<String> collection = deserializer.deserialize(parser, context, result);

        assertNotNull(collection);
    }

    @Test
    public void testHandleNonArrayMethod() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        List<String> result = new ArrayList<>();
        Collection<String> collection = deserializer.handleNonArray(parser, context, result);

        assertNotNull(collection);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMethodWithException() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, new StringDeserializer(), null);
        List<String> result = new ArrayList<>();
        deserializer.deserialize(parser, context, result);
    }
}
