package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

/**
 * Test cases for {@link EnumSerializer}.
 */
@JacksonStdImpl
public class EnumSerializerTest {

    private static enum TestEnum {
        ONE, TWO, THREE, FOUR;

        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        public String getTestString() {
            return name();
        }
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromEnumConstant(TestEnum.class), null);
        assertNotNull(serializer);
    }

    /**
     * Test {@link #createContextual(SerializerProvider, BeanProperty)} method when JSON format is not present.
     */
    @Test
    public void testCreateContextualWithoutJsonFormat() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromEnumConstant(TestEnum.class), null);
        BeanProperty property = ReflectionTestUtils.makeAccessible(Object.class.getDeclaredField("class"), TestEnum.class);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(objectMapper.getSerializerProvider(), property);
        assertSame(serializer, contextualSerializer);
    }

    /**
     * Test {@link #createContextual(SerializerProvider, BeanProperty)} method when JSON format with Shape.STRING is present.
     */
    @Test
    public void testCreateContextualWithJsonFormatStringShape() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = ObjectMapperTestUtils.createObjectNode();
        node.put("format", "string");
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<?> contextualSerializer = new EnumSerializer(EnumValues.constructFromEnumConstant(TestEnum.class), null)
                .createContextual(objectMapper.getSerializerProvider(), null);
        assertNotNull(contextualSerializer);
        JsonSerializer<?> serializedSerializer = contextualSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        assertTrue(serializedSerializer instanceof EnumSerializer);
        EnumSerializer serializedEnumSerializer = (EnumSerializer) serializedSerializer;
        assertEquals(JsonFormat.Shape.STRING, serializedEnumSerializer._serializeAsIndex);
    }

    /**
     * Test {@link #createContextual(SerializerProvider, BeanProperty)} method when JSON format with Shape.INT is present.
     */
    @Test
    public void testCreateContextualWithJsonFormatIntShape() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = ObjectMapperTestUtils.createObjectNode();
        node.put("format", "number");
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.INT);
        JsonSerializer<?> contextualSerializer = new EnumSerializer(EnumValues.constructFromEnumConstant(TestEnum.class), null)
                .createContextual(objectMapper.getSerializerProvider(), null);
        assertNotNull(contextualSerializer);
        JsonSerializer<?> serializedSerializer = contextualSerializer.createContextual(objectMapper.getSerializerProvider(), null);
        assertTrue(serializedSerializer instanceof EnumSerializer);
        EnumSerializer serializedEnumSerializer = (EnumSerializer) serializedSerializer;
        assertEquals(JsonFormat.Shape.INT, serializedEnumSerializer._serializeAsIndex);
    }

    /**
     * Test serialization.
     */
    @Test
    public void testSerialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromEnumConstant(TestEnum.class), null);
        ObjectWriter writer = objectMapper.getWriterWithView(new SimpleModule().addSerializer(TestEnum.class, serializer).buildView());
        ObjectNode node = writer.writeValueAsObject(TestEnum.ONE);
        assertEquals("ONE", node.get("value").asText());
    }

    /**
     * Test serialization as index.
     */
    @Test
    public void testSerializationAsIndex() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper.SerializerProvider serializers = objectMapper.getSerializerProvider();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromEnumConstant(TestEnum.class), Boolean.TRUE);
        ObjectWriter writer = objectMapper.getWriterWithView(new SimpleModule().addSerializer(TestEnum.class, serializer).buildView());
        ObjectNode node = writer.writeValueAsObject(TestEnum.ONE);
        assertEquals("0", node.get("value").asText());
    }

    /**
     * Test serialization with toString() method.
     */
    @Test
    public void testSerializationWithToStringMethod() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper.SerializerProvider serializers = objectMapper.getSerializerProvider();
        serializers.setConfig(SerializationConfig.builder()
                .withFeature(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                .build());
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromEnumConstant(TestEnum.class), null);
        ObjectWriter writer = objectMapper.getWriterWithView(new SimpleModule().addSerializer(TestEnum.class, serializer).buildView());
        ObjectNode node = writer.writeValueAsObject(TestEnum.ONE);
        assertEquals("ONE", node.get("value").asText());
    }

    /**
     * Test serialization schema.
     */
    @Test
    public void testSerializationSchema() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromEnumConstant(TestEnum.class), null);
        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), Enum.class);
        ArrayNode enumNode = (ArrayNode) schema.get("enum");
        assertEquals(4, enumNode.size());
        assertTrue(Arrays.asList(enumNode.valuesAs(String.class)).containsAll(Arrays.asList(TestEnum.ONE.name(), TestEnum.TWO.name(),
                TestEnum.THREE.name(), TestEnum.FOUR.name())));
    }
}