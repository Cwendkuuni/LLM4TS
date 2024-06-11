package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializer() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization with explicit value method
        JsonValueSerializer serializer = new JsonValueSerializer(mapper.findMethod(JsonValueSerializerTest.class, "getValueMethod1"), null);
        ObjectNode node = mapper.createObjectNode();
        serializer.serialize(new JsonValueSerializerTest(), node, new JsonSerializer.DefaultSerializerProvider());
        assertEquals("foo", node.get("value").asText());

        // Test serialization with implicit value method
        serializer = new JsonValueSerializer(mapper.findMethod(JsonValueSerializerTest.class, "getValueMethod2"), null);
        node.put("value", 123);
        serializer.serialize(node, new JsonGenerator(new ByteArrayOutputStream()), new JsonSerializer.DefaultSerializerProvider());
        assertEquals("123", mapper.writeValueAsString(node));

        // Test serialization with custom value serializer
        JsonSerializer<Integer> intSerializer = new SimpleIntSerializer();
        JsonValueSerializer serializerWithCustomSerializer = new JsonValueSerializer(mapper.findMethod(JsonValueSerializerTest.class, "getValueMethod3"), intSerializer);
        node.put("value", new JsonValueSerializerTest(456));
        serializerWithCustomSerializer.serialize(node, new JsonGenerator(new ByteArrayOutputStream()), new JsonSerializer.DefaultSerializerProvider());
        assertEquals("456", mapper.writeValueAsString(node));

        // Test serialization with contextual serializer
        JsonSerializer<JsonValueSerializerTest> contextualSerializer = mapper.findValueSerializer(JsonValueSerializerTest.class, null);
        JsonValueSerializer serializerWithContextualSerializer = new JsonValueSerializer(mapper.findMethod(JsonValueSerializerTest.class, "getValueMethod4"), contextualSerializer, false);
        node.put("value", new JsonValueSerializerTest(789));
        serializerWithContextualSerializer.serialize(node, new JsonGenerator(new ByteArrayOutputStream()), new JsonSerializer.DefaultSerializerProvider());
        assertEquals("789", mapper.writeValueAsString(node));
    }

    @JsonValue
    public Object getValueMethod1() {
        return "foo";
    }

    @JsonValue
    public int getValueMethod2() {
        return 123;
    }

    public JsonValueSerializerTest getValueMethod3() {
        return this;
    }

    public int getValueMethod4() {
        return 789;
    }

    public static class SimpleIntSerializer extends StdSerializer<Integer> {
        @Override
        public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value);
        }
    }
}