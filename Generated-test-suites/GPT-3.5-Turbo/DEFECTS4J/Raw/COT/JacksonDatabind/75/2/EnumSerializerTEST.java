
package com.fasterxml.jackson.databind.ser.std;

import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        // Test 1: Test serialization of enumerated value as index
        EnumValues values = new EnumValues();
        values.add("VALUE1", 0, null);
        EnumSerializer serializer = new EnumSerializer(values, true);
        Enum<?> en = Enum.valueOf(Enum.class, "VALUE1");
        JsonGenerator gen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
        SerializerProvider providers = new ObjectMapper().getSerializerProvider();
        serializer.serialize(en, gen, providers);
        gen.flush();
        gen.close();
    }

    @Test
    public void testCreateContextual() throws Exception {
        // Test 1: Test creating contextual EnumSerializer with new serializeAsIndex value
        EnumValues values = new EnumValues();
        values.add("VALUE1", 0, null);
        Boolean serializeAsIndex = true;
        EnumSerializer serializer = new EnumSerializer(values, serializeAsIndex);
        SerializerProvider providers = new ObjectMapper().getSerializerProvider();
        SerializerProvider newProvider = providers.createInstance(providers.getConfig(), serializer);
        BeanDescription beanDesc = providers.getConfig().introspect(values.getEnumClass());
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(com.fasterxml.jackson.annotation.JsonFormat.Shape.NUMBER);
        EnumSerializer newSerializer = EnumSerializer.construct(values.getEnumClass(), providers.getConfig(), beanDesc, format);
        JsonSerializer<?> contextualSerializer = newSerializer.createContextual(newProvider, null);
        assertNotNull(contextualSerializer);
    }

    @Test
    public void testGetEnumValues() {
        // Test 1: Test getting EnumValues from EnumSerializer
        EnumValues values = new EnumValues();
        values.add("VALUE1", 0, null);
        EnumSerializer serializer = new EnumSerializer(values, true);
        EnumValues actualValues = serializer.getEnumValues();
        assertEquals(values, actualValues);
    }

    @Test
    public void testGetSchema() throws Exception {
        // Test 1: Test getting schema for EnumSerializer when serializeAsIndex is true
        EnumValues values = new EnumValues();
        values.add("VALUE1", 0, null);
        EnumSerializer serializer = new EnumSerializer(values, true);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ArrayNode enumNode = provider.createArrayNode();
        enumNode.add("VALUE1");
        ObjectNode objectNode = provider.createObjectNode();
        objectNode.put("enum", enumNode);
        JsonNode actualSchema = serializer.getSchema(provider, null);
        assertEquals(objectNode, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        // Test 1: Test accepting JsonFormatVisitor for EnumSerializer when serializeAsIndex is true
        EnumValues values = new EnumValues();
        values.add("VALUE1", 0, null);
        EnumSerializer serializer = new EnumSerializer(values, true);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonStringFormatVisitor stringVisitor = provider.getFactory().getCodec().visitorFactory().stringFormatVisitor(provider.getConfig(), null);
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper.Base(provider), null);
    }
}
