import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import org.junit.Test;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        Collection<Object> result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithValues() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.END_ARRAY);

        Collection<Object> result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertTrue(result.size() == 1);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNonArray() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        when(parser.isExpectedStartArrayToken()).thenReturn(false);

        deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        assertNotNull(result);
    }

}