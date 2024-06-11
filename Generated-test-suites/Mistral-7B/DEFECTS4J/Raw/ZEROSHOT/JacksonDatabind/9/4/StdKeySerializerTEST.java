import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNodeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        StdKeySerializer serializer = new StdKeySerializer();

        Date date = new Date();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(date, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        Assertions.assertEquals("\"2023-03-05T12:34:56.789Z\"", jsonGenerator.getOutputBuffer().toString().trim());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        StdKeySerializer serializer = new StdKeySerializer();

        String value = "test";
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        Assertions.assertEquals("{\"@class\":\"String\",\"value\":\"test\"}", mapper.getFactory().createParser(jsonGenerator.getOutputBuffer().toString()).readValueAs(String.class));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), mapper.getTypeFactory().constructType(Map.class.getName(), String.class, String.class));

        Assertions.assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode expectedNode = JsonNodeFactory.instance.textNode("string");

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getDefaultSerializerProvider().getFieldValueSerializerProvider().findValueSerializer(mapper.getTypeFactory().constructType(Map.class.getName(), String.class, String.class), null);
        serializer.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(Map.class.getName(), String.class, String.class));

        Assertions.assertEquals(expectedNode, visitor.getSchema());
    }

    @Test
    public void testSerializeMapWithStringsAndDates() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        Map<String, Date> map = new HashMap<>();
        map.put("test", new Date());

        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);

        mapper.writeValue(jsonGenerator, map);

        String jsonString = jsonGenerator.getOutputBuffer().toString().trim();
        jsonGenerator.close();

        Map<String, Date> deserializedMap = mapper.readValue(jsonString, mapper.getTypeFactory().constructType(Map.class, String.class, Date.class));

        Assertions.assertEquals(map, deserializedMap);
    }

    @Test
    public void testSerializeMapWithDatesAndStrings() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        Map<Date, String> map = new HashMap<>();
        map.put(new Date(), "test");

        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);

        mapper.writeValue(jsonGenerator, map);

        String jsonString = jsonGenerator.getOutputBuffer().toString().trim();
        jsonGenerator.close();

        Map<Date, String> deserializedMap = mapper.readValue(jsonString, mapper.getTypeFactory().constructType(Map.class, Date.class, String.class));

        Assertions.assertEquals(map, deserializedMap);
    }
}