import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class NullifyingDeserializerTest {
    
    private final ObjectMapper mapper = new ObjectMapper();
    private final NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
    
    @Test
    public void testDeserialize_shouldSkipAllContent() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode rootNode = mapper.readTree(jsonFactory.createParser("{\"key\":\"value\"}"));
        DeserializationContext context = mock(DeserializationContext.class);

        Object result = deserializer.deserialize(rootNode.traverse(jsonFactory), context);

        assertNull(result);
        verify(context, times(1)).skipChildren();
    }

    @Test
    public void testDeserializeWithType_shouldSkipAllContentForNonArrayOrObject() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode rootNode = mapper.readTree(jsonFactory.createParser("{\"key\":\"value\"}"));
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        Object result = deserializer.deserializeWithType(rootNode.traverse(jsonFactory), context, typeDeserializer);

        assertNull(result);
        verify(context, never()).skipChildren();
        verify(typeDeserializer, never()).deserializeTypedFromAny(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test
    public void testDeserializeWithType_shouldDelegateToTypeDeserializerForArray() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode rootNode = mapper.readTree(jsonFactory.createParser("[1, 2, 3]"));
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        deserializer.deserializeWithType(rootNode.traverse(jsonFactory), context, typeDeserializer);

        verify(typeDeserializer, times(1)).deserializeTypedFromAny(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test
    public void testDeserializeWithType_shouldDelegateToTypeDeserializerForObject() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode rootNode = mapper.readTree(jsonFactory.createParser("{\"key\":\"value\"}"));
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        deserializer.deserializeWithType(rootNode.traverse(jsonFactory), context, typeDeserializer);

        verify(typeDeserializer, times(1)).deserializeTypedFromAny(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test
    public void testDeserializeWithType_shouldDelegateToTypeDeserializerForFieldName() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode rootNode = mapper.readTree(jsonFactory.createParser("{\"key\":\"value\"}").nextToken());
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        deserializer.deserializeWithType(rootNode.traverse(jsonFactory), context, typeDeserializer);

        verify(typeDeserializer, times(1)).deserializeTypedFromAny(any(JsonParser.class), any(DeserializationContext.class));
    }
}