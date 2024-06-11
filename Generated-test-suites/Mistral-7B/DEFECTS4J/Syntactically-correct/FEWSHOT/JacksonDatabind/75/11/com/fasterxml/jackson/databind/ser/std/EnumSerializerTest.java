package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Test cases for {@link EnumSerializer}.
 */
@JacksonStdImpl
public class EnumSerializerTest {

    // Test data
    private static final EnumSerializer SERIALIZER = new EnumSerializer(EnumValues.allValuesFrom(Apple.class), null);
    private static final Apple RED_APPLE = Apple.RED;
    private static final Apple GREEN_APPLE = Apple.GREEN;

    @Test
    public void testConstruct() {
        EnumSerializer serializer = EnumSerializer.construct(Apple.class, new SerializationConfig(), null, null);
        assertEquals(Apple.class, serializer.handledType());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer<Apple> serializer = SERIALIZER.createContextual(new SerializerProvider(), null);
        assertEquals(SERIALIZER, serializer);
    }

    @Test
    public void testSerializeEnum() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SERIALIZER.serialize(RED_APPLE, gen, new SerializerProvider());
        gen.flush();
        assertEquals("RED", gen.getOutputContent().toString());

        gen = mapper.getFactory().createGenerator(System.out);
        SERIALIZER.serialize(GREEN_APPLE, gen, new SerializerProvider());
        gen.flush();
        assertEquals("GREEN", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeEnumAsIndex() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SERIALIZER.serialize(RED_APPLE, gen, new SerializerProvider());
        gen.flush();
        assertEquals("0", gen.getOutputContent().toString());

        gen = mapper.getFactory().createGenerator(System.out);
        SERIALIZER.serialize(GREEN_APPLE, gen, new SerializerProvider());
        gen.flush();
        assertEquals("1", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeEnumToString() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SERIALIZER.serialize(RED_APPLE, gen, new SerializerProvider());
        gen.flush();
        assertEquals("RED", gen.getOutputContent().toString());

        gen = mapper.getFactory().createGenerator(System.out);
        SERIALIZER.serialize(GREEN_APPLE, gen, new SerializerProvider());
        gen.flush();
        assertEquals("GREEN", gen.getOutputContent().toString());
    }

    @Test
    public void testGetSchema() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schema = SERIALIZER.getSchema(mapper.getSerializationConfig(), Apple.class);
        assertEquals(JsonType.STRING, schema.getType());
        assertEquals("string", schema.get("type").asText());

        ArrayNode enumNode = schema.path("enum");
        assertEquals(2, enumNode.size());

        assertEquals("RED", enumNode.get(0).asText());
        assertEquals("GREEN", enumNode.get(1).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJavaTypeSerializerModifier();
        visitor.visitEnumFormat(Apple.class, null, null);
        assertEquals(JsonFormat.Shape.STRING, visitor.getShape());
    }
}

enum Apple {
    RED,
    GREEN
}