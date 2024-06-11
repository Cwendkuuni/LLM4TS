package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testConstruct() throws InstantiationException, IllegalAccessException {
        Enum<?>[] values = MyEnum.values();
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, null, null, null);
        assertEquals(MyEnum.class, serializer.handledType());
        assertEquals(Arrays.asList(values), serializer.getEnumValues().values());
    }

    @Test
    public void testCreateContextual_serializeAsIndex_true() throws JsonMappingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(SerializationConfig.class, MyEnum.class), true);
        JsonSerializer<?> contextual = serializer.createContextual(null, null);
        assertTrue(contextual instanceof EnumSerializer);
        assertTrue(((EnumSerializer) contextual)._serializeAsIndex.booleanValue());
    }

    @Test
    public void testCreateContextual_serializeAsIndex_false() throws JsonMappingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(SerializationConfig.class, MyEnum.class), false);
        JsonSerializer<?> contextual = serializer.createContextual(null, null);
        assertNotSame(serializer, contextual);
        assertFalse(((EnumSerializer) contextual)._serializeAsIndex.booleanValue());
    }

    @Test
    public void testSerialize_serializationFeature() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(SerializationConfig.class, MyEnum.class), null);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

        JsonGenerator gen = writer.writeValueAsString(MyEnum.FIRST).getGenerator();
        serializer.serialize(MyEnum.FIRST, gen, null);
        assertEquals("\"FIRST\"", gen.getOutputBuffer().toString());

        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        gen = writer.writeValueAsString(MyEnum.FIRST).getGenerator();
        serializer.serialize(MyEnum.FIRST, gen, null);
        assertEquals("\"FIRST.name()\"", gen.getOutputBuffer().toString());

        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        gen = writer.writeValueAsString(MyEnum.FIRST).getGenerator();
        serializer.serialize(MyEnum.FIRST, gen, null);
        assertEquals("0", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_enumValues() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(SerializationConfig.class, MyEnum.class), null);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

        JsonGenerator gen = writer.writeValueAsString(MyEnum.FIRST).getGenerator();
        serializer.serialize(MyEnum.FIRST, gen, null);
        assertEquals("\"FIRST\"", gen.getOutputBuffer().toString());

        gen = writer.writeValueAsString(MyEnum.SECOND).getGenerator();
        serializer.serialize(MyEnum.SECOND, gen, null);
        assertEquals("\"SECOND\"", gen.getOutputBuffer().toString());

        gen = writer.writeValueAsString(MyEnum.THIRD).getGenerator();
        serializer.serialize(MyEnum.THIRD, gen, null);
        assertEquals("\"THIRD\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(SerializationConfig.class, MyEnum.class), null);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode schemaNode = serializer.getSchema(objectMapper.getSerializerProvider(), null);
        JsonNode schema = schemaNode.get("schema$schema");
        JsonNode enumNode = schema.get("properties").get("MyEnum").get("enum");

        assertEquals(3, enumNode.size());
        assertEquals("FIRST", enumNode.get(0).asText());
        assertEquals("SECOND", enumNode.get(1).asText());
        assertEquals("THIRD", enumNode.get(2).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(SerializationConfig.class, MyEnum.class), null);

        JsonFormatVisitorWrapper visitor = null;
        try {
            visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
            serializer.acceptJsonFormatVisitor(visitor, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        visitor = null;
        try {
            visitor = new JsonFormatVisitorWrapper(null, null, null, null, new JsonStringFormatVisitor(null, null, null, null));

            Set<String> enumValues = new LinkedHashSet<>();
            for (Enum<?> e : MyEnum.values()) {
                if (serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
                    enumValues.add(e.toString());
                } else {
                    enumValues.add(serializer.getEnumValues().serializedValueFor(e));
                }
            }

            visitor.expectStringFormat(null).enumTypes(enumValues);
        } catch (JsonMappingException e) {
            fail();
        }
    }

    private static class MyEnum {
        public static final MyEnum FIRST = new MyEnum("FIRST");
        public static final MyEnum SECOND = new MyEnum("SECOND");
        public static final MyEnum THIRD = new MyEnum("THIRD");

        private MyEnum(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String toString() {
            return name;
        }
    }
}