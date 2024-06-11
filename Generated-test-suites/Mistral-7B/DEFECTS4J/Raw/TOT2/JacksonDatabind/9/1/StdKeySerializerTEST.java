import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_Date() throws JsonProcessingException {
        Date date = new Date();
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, jgen, mapper.getSerializerProvider());
        jgen.flush();

        String expected = "\"2022-03-01T00:00:00.000Z\""; // current date formatted as ISO 8601
        JsonNode actual = mapper.readTree(System.in); // read the serialized date from the console

        assertEquals(expected, actual.asText());
    }

    @Test
    public void testSerialize_String() throws JsonProcessingException {
        String value = "test_key";
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, jgen, mapper.getSerializerProvider());
        jgen.flush();

        String expected = "\"" + value + "\"";
        JsonNode actual = mapper.readTree(System.in);

        assertEquals(expected, actual.asText());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode expected = mapper.createObjectNode().put("type", "string");
        JsonNode actual = serializer.getSchema(mapper.getSerializerProvider(), Object.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(SerializerProvider.StandardSerializers.FORMATTER_DEFAULT_VISITOR, Object.class);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);

        assertTrue(visitor.isFormatEmpty());
        assertTrue(visitor.isFormatContainerNode());
        assertTrue(visitor.isFormatString());
    }
}