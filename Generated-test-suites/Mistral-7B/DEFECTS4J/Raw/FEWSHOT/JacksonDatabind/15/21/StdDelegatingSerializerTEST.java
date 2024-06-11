package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testConvertValue() throws JsonProcessingException, JsonGenerationException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(new IdentityConverter()) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };

        Assert.assertEquals("1", serializer.convertValue(1));
        Assert.assertEquals("2.0", serializer.convertValue(2.0));
        Assert.assertEquals("true", serializer.convertValue(true));
        Assert.assertEquals("false", serializer.convertValue(false));
        Assert.assertEquals("null", serializer.convertValue(null));
        Assert.assertEquals("Test", serializer.convertValue("Test"));
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = mapper.findValueSerializer(String.class);

        Assert.assertNull(serializer.serialize(null, null, null));

        String input = "Test";
        String output = "\"Test\"";

        byte[] serialized = serializer.serialize(input, null, null);
        Assert.assertEquals(output, new String(serialized));
    }

    @Test
    public void testSerializeWithType() throws JsonProcessingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = mapper.findValueSerializer(String.class);

        Assert.assertNull(serializer.serializeWithType(null, null, null, null));

        String input = "Test";
        String output = "\"Test\"";

        ObjectWriter writer = mapper.writer().withTypeSerializer(String.class, serializer);
        JsonNode node = writer.writeValueAsTree(input);
        Assert.assertEquals(output, node.toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = mapper.findValueSerializer(String.class);

        Assert.assertNull(serializer.getSchema(null, null));

        Schema schema = mapper.schemaForSerialization(String.class);
        Assert.assertEquals(schema, serializer.getSchema(null, String.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = mapper.findValueSerializer(String.class);

        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getDefaultOutputFormatter().getFormatVisitor();
        serializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    static class IdentityConverter implements Converter<Object, String> {
        @Override
        public String convert(Object value) {
            return value.toString();
        }
    }
}