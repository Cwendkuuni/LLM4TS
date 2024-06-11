
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.StdKeySerializer;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StdKeySerializerTest {

    @Test
    public void testSerialize() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonFactory factory = new JsonFactory();
        Map<Object, Object> map = new HashMap<>();

        // Test serialize Date key
        Date date = new Date();
        JsonGenerator generator = factory.createGenerator(System.out);
        serializer.serialize(date, generator, provider);
        generator.close();

        // Test serialize other key types
        String key1 = "key1";
        map.put(key1, "value1");

        String key2 = "";
        map.put(key2, "value2");

        Integer key3 = 3;
        map.put(key3, "value3");

        Object key4 = new Object();
        map.put(key4, "value4");

        JsonNode key5 = new ObjectMapper().readTree("{\"name\":\"value\"}");
        map.put(key5, "value5");

        ObjectNode key6 = new ObjectMapper().readTree("{\"name\":\"value\"}");
        map.put(key6, "value6");

        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            generator = factory.createGenerator(System.out);
            serializer.serialize(entry.getKey(), generator, provider);
            generator.close();
        }
    }

    @Test
    public void testGetSchema() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonFactory factory = new JsonFactory();
        ObjectNode schema;

        schema = (ObjectNode) serializer.getSchema(provider, String.class);
        assertEquals("string", schema.get("type").asText());

        schema = (ObjectNode) serializer.getSchema(provider, Integer.class);
        assertEquals("string", schema.get("type").asText());

        schema = (ObjectNode) serializer.getSchema(provider, Date.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonFactory factory = new JsonFactory();
        JsonFormatVisitorWrapper visitor;

        visitor = new ObjectMapper().getJsonFactory().getCodec().visitorForFormat(provider, String.class);
        serializer.acceptJsonFormatVisitor(visitor, String.class);

        visitor = new ObjectMapper().getJsonFactory().getCodec().visitorForFormat(provider, Integer.class);
        serializer.acceptJsonFormatVisitor(visitor, Integer.class);

        visitor = new ObjectMapper().getJsonFactory().getCodec().visitorForFormat(provider, Date.class);
        serializer.acceptJsonFormatVisitor(visitor, Date.class);
    }
}

