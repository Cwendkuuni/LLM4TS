import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[\"foo\", \"bar\", \"baz\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Collection<String> result = new StringCollectionDeserializer(JavaType.collectionType(String.class), null, null)
                .deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("foo"));
        assertTrue(result.contains("bar"));
        assertTrue(result.contains("baz"));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[\"foo\", \"bar\", \"baz\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = mapper.getDeserializationContext().findRootValueDeserializer(JavaType.collectionType(String.class));
        Object result = new StringCollectionDeserializer(JavaType.collectionType(String.class), null, null)
                .deserializeWithType(parser, ctxt, typeDeserializer);
        assertNotNull(result);
        assertTrue(result instanceof Collection);
        Collection<String> collection = (Collection<String>) result;
        assertEquals(3, collection.size());
        assertTrue(collection.contains("foo"));
        assertTrue(collection.contains("bar"));
        assertTrue(collection.contains("baz"));
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("null");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Collection<String> result = new StringCollectionDeserializer(JavaType.collectionType(String.class), null, null)
                .deserialize(parser, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Collection<String> result = new StringCollectionDeserializer(JavaType.collectionType(String.class), null, null)
                .deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserializeSingleValue() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"foo\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Collection<String> result = new StringCollectionDeserializer(JavaType.collectionType(String.class), null, null)
                .deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("foo"));
    }
}