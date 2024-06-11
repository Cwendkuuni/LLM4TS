
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;

public class StdKeySerializerTest {

    @Test
    public void testConstructor() {
        StdKeySerializer serializer = new StdKeySerializer();
        assertNotNull(serializer);
    }

    @Test
    public void testSerialize() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        String value = "test";

        serializer.serialize(value, jgen, provider);

        verify(jgen).writeFieldName(value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertNotNull(schema);
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectStringFormat(typeHint);
    }

    @Test
    public void testSerializeNullValue() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        String value = null;

        serializer.serialize(value, jgen, provider);

        verify(jgen, never()).writeFieldName(anyString());
    }

    @Test
    public void testSerializeDateValue() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Date value = new Date();

        serializer.serialize(value, jgen, provider);

        verify(provider).defaultSerializeDateKey(value, jgen);
        verify(jgen, never()).writeFieldName(anyString());
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeNullPointerException() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();

        serializer.serialize(value, jgen, provider);
    }
}

