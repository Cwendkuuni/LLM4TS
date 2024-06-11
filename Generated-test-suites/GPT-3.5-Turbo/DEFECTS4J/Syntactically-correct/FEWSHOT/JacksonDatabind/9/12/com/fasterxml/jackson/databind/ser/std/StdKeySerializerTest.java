

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StdKeySerializerTest {
    @Test
    public void testSerialize() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize("test", jgen, provider);
        verify(jgen).writeFieldName("test");

        Date date = new Date();
        serializer.serialize(date, jgen, provider);
        verify(provider).defaultSerializeDateKey(date, jgen);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = String.class;

        JsonNode schema = serializer.getSchema(provider, typeHint);
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
}

