package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

public class EnumSerializerTest {


    // Helper methods for tests
    private JsonGenerator getJsonGenerator(StringBuilder resultBuilder) {
        return new JsonGenerator() {
            @Override
            public void writeStartArray() throws IOException {
                resultBuilder.append("[");
            }

            @Override
            public void writeEndArray() throws IOException {
                resultBuilder.append("]");
            }

            @Override
            public void writeStartObject() throws IOException {}

            @Override
            public void writeEndObject() throws IOException {}

            @Override
            public void writeFieldName(String name) throws IOException {}

            @Override
            public void writeString(String text) throws IOException {
                resultBuilder.append(text);
            }

            @Override
            public void writeNumber(int v) throws IOException {}

            @Override
            public void close() throws IOException {}

            @Override
            public void flush() throws IOException {}
        };
    }

    private SerializerProvider getSerializerProvider() {
        return new DefaultSerializerProvider.Impl();
    }

    private ObjectNode createSchemaNode(String type, boolean b) {
        return new ObjectNode(null);
    }

    @Test
    public void testSerialize() throws IOException {
        Enum<String> en = new Enum<String>() {
            @Override
            public String name() {
                return "ENUM";
            }

            @Override
            public int ordinal() {
                return 0;
            }
        };

        StringBuilder stringBuilder = new StringBuilder();
        JsonGenerator jsonGenerator = getJsonGenerator(stringBuilder);
        SerializerProvider serializerProvider = getSerializerProvider();
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(this.getClass(), (Class<Enum<?>>) en.getClass()), null);

        enumSerializer.serialize(en, jsonGenerator, serializerProvider);

        assertEquals("ENUM", stringBuilder.toString());
    }

    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(this.getClass(), (Class<Enum<?>>) EnumSerializerTest.class), null);
        ObjectNode resultNode = (ObjectNode) enumSerializer.getSchema(getSerializerProvider(), EnumSerializerTest.class);

        assertNotNull(resultNode);
        assertEquals("string", resultNode.get("type").asText());
        assertNotNull(resultNode.get("enum"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(this.getClass(), (Class<Enum<?>>) EnumSerializer.class), null);

        JsonStringFormatVisitor jsonStringFormatVisitor = new JsonStringFormatVisitor.Base();
        enumSerializer.acceptJsonFormatVisitor(jsonStringFormatVisitor, ManagedReferenceType.class);

        Set<String> enums = new LinkedHashSet<>();
        for(Enum<?> e : EnumValues.constructFromName(this.getClass(), (Class<Enum<?>>) EnumSerializer.class).enums()){
            enums.add(e.toString());
        }

        assertEquals(enums, jsonStringFormatVisitor.getEnums());
    }

}