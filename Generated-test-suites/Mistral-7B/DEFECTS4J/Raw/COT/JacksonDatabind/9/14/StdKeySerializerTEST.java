import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StdKeySerializerTest {

    @Test
    public void testSerializeObject() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jgen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        Object value = new Date();
        serializer.serialize(value, jgen, provider);
        jgen.flush();

        String expected = "\"%TD-%tm-%tdT%TT:%ss.%SSZ\"";
        String actual = jgen.getOutputBuffer().toString();
        assertEquals(expected, actual);

        value = "TestString";
        serializer.serialize(value, jgen, provider);
        jgen.flush();

        expected = "\"TestString\"";
        actual = jgen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeDateNull() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jgen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        serializer.serialize(null, jgen, provider);
        jgen.flush();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode expected = mapper.valueToTree("\"string\"");
        JsonNode actual = serializer.getSchema(new SerializerProvider(), Object.class.getType());

        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorNullTypeHint() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializers.StringSerializer());

        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorValidTypeHint() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializers.StringSerializer());

        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        serializer.acceptJsonFormatVisitor(visitor, javaType);

        assertEquals(StandardSerializer.Feature.WRITE_BIGDECIMAL_AS_PLAIN, visitor.getFormatFeatures().intValue());
    }
}