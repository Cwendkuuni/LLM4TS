import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        Date date = new Date();
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        keySerializer.serialize(date, jgen, mapper.getSerializerProvider());
        jgen.flush();

        JsonNode actual = mapper.readTree(System.in);
        String expected = new SimpleDateFormat("\"\"\"ISODate(\"\"" + DateFormat.getDateInstance(DateFormat.LONG).format(date) + "\"\")\"\"\"");
        assertEquals(expected, actual.asText());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        keySerializer.serialize("sampleKey", jgen, mapper.getSerializerProvider());
        jgen.writeEndObject();
        jgen.flush();

        JsonNode actual = mapper.readTree(System.in);
        String expected = "\"sampleKey\"";
        assertEquals(expected, actual.asText());
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        keySerializer.serialize(null, jgen, mapper.getSerializerProvider());
        jgen.writeEndObject();
        jgen.flush();

        JsonNode actual = mapper.readTree(System.in);
        String expected = "null";
        assertEquals(expected, actual.asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        keySerializer.serialize(new ArrayList<>(), jgen, mapper.getSerializerProvider());
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();

        JsonNode actual = keySerializer.getSchema(mapper.getSerializerProvider(), Object.class);
        String expected = "\"string\"";
        assertEquals(expected, actual.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();

        JsonFactory factory = mapper.getFactory();
        JsonFormatVisitorWrapper visitor = factory.getJsonFormatVisitor(SerializerProvider.StdioProvider.INSTANCE, Object.class);
        keySerializer.acceptJsonFormatVisitor(visitor, ObjectType.construct(Map.class, String.class, Object.class));

        JsonNode actual = visitor.getSchema();
        String expected = "{\"type\": \"object\", \"properties\": {\"$id\": 0, \"type\": \"object\", \"properties\": {\"$id\": 1, \"type\": \"string\"}}};";
        assertEquals(expected, actual.toString());
    }
}