
package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Assert;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerializeEnumAsIndex() throws IOException {
        EnumValues values = EnumValues.constructFromName(null, DummyEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, true);

        JsonSerializer<Object> contextSerializer = serializer.createContextual(null, null);
        Assert.assertEquals(serializer, contextSerializer);

        DummyEnum enumValue = DummyEnum.VALUE_1;

        DummyJsonGenerator generator = new DummyJsonGenerator();

        serializer.serialize(enumValue, generator, null);

        Assert.assertEquals(enumValue.ordinal(), generator.getLastIntValue());
    }

    @Test
    public void testSerializeEnumAsName() throws IOException {
        EnumValues values = EnumValues.constructFromName(null, DummyEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, false);

        JsonSerializer<Object> contextSerializer = serializer.createContextual(null, null);
        Assert.assertEquals(serializer, contextSerializer);

        DummyEnum enumValue = DummyEnum.VALUE_2;

        DummyJsonGenerator generator = new DummyJsonGenerator();

        serializer.serialize(enumValue, generator, null);

        Assert.assertEquals(enumValue.toString(), generator.getLastStringValue());
    }

    @Test
    public void testGetEnumValues() {
        EnumValues values = EnumValues.constructFromName(null, DummyEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, true);

        Assert.assertEquals(values, serializer.getEnumValues());
    }

    @Test
    public void testGetSchemaAsIndex() {
        EnumValues values = EnumValues.constructFromName(null, DummyEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, true);
        ObjectNode objectNode = new ObjectNode(null);

        DummyJsonSerializerProvider provider = new DummyJsonSerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);

        objectNode.put("type", "integer");
        objectNode.put("required", true);

        Assert.assertEquals(objectNode, schema);
    }

    @Test
    public void testGetSchemaNotAsIndex() {
        EnumValues values = EnumValues.constructFromName(null, DummyEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, false);
        ObjectNode objectNode = new ObjectNode(null);

        DummyJsonSerializerProvider provider = new DummyJsonSerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);

        objectNode.put("type", "string");
        objectNode.put("required", true);

        ArrayNode enumNode = new ArrayNode(null);
        enumNode.add("VALUE_1");
        enumNode.add("VALUE_2");

        objectNode.put("enum", enumNode);

        Assert.assertEquals(objectNode, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitorAsIndex() throws Exception {
        EnumValues values = EnumValues.constructFromName(null, DummyEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, true);

        DummyJsonFormatVisitorWrapper visitorWrapper = new DummyJsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitorWrapper, null);

        Assert.assertEquals(JsonParser.NumberType.INT, visitorWrapper.getLastNumberType());
    }

    @Test
    public void testAcceptJsonFormatVisitorNotAsIndex() throws Exception {
        EnumValues values = EnumValues.constructFromName(null, DummyEnum.class);
        EnumSerializer serializer = new EnumSerializer(values, false);

        DummyJsonFormatVisitorWrapper visitorWrapper = new DummyJsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitorWrapper, null);

        DummyJsonStringFormatVisitor stringVisitor = visitorWrapper.getLastStringVisitor();
        Assert.assertNotNull(stringVisitor);

        Set<String> expectedEnums = new LinkedHashSet<>();
        expectedEnums.add("VALUE_1");
        expectedEnums.add("VALUE_2");

        Set<String> actualEnums = new HashSet<>(stringVisitor.getEnumTypes());

        Assert.assertEquals(expectedEnums, actualEnums);
    }

    private enum DummyEnum {
        VALUE_1,
        VALUE_2
    }

    private static class DummyJsonGenerator extends JsonGenerator {

        private int lastIntValue;
        private String lastStringValue;

        @Override
        public void writeNumber(int v) throws IOException {
            lastIntValue = v;
        }

        @Override
        public void writeString(String text) throws IOException {
            lastStringValue = text;
        }

        public int getLastIntValue() {
            return lastIntValue;
        }

        public String getLastStringValue() {
            return lastStringValue;
        }

        // Other methods are not implemented
    }

    private static class DummyJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper.Base {

        private JsonParser.NumberType lastNumberType;
        private DummyJsonStringFormatVisitor lastStringVisitor;

        @Override
        public JsonStringFormatVisitor expectStringFormat(Class<?> type) {
            lastStringVisitor = new DummyJsonStringFormatVisitor();
            return lastStringVisitor;
        }

        @Override
        public void visitIntFormat(JsonParser.NumberType type) {
            lastNumberType = type;
        }

        public JsonParser.NumberType getLastNumberType() {
            return lastNumberType;
        }

        public DummyJsonStringFormatVisitor getLastStringVisitor() {
            return lastStringVisitor;
        }

        // Other methods are not implemented
    }

    private static class DummyJsonStringFormatVisitor implements JsonStringFormatVisitor {

        private Set<String> enumTypes;

        @Override
        public void enumTypes(Set<String> enums) {
            enumTypes = enums;
        }

        public Set<String> getEnumTypes() {
            return enumTypes;
        }

        // Other methods are not implemented
    }

    private static class DummyJsonSerializerProvider extends SerializerProvider {

        // Not implemented
    }

}
