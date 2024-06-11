package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.WritableTypeSpec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.StdDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.EnumDeserializer;
import com.fasterxml.jackson.databind.deser.impl.EnumValueDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.EnumValues;

/**
 * Test cases for {@link EnumSerializer}.
 */
@JacksonStdImpl
public class EnumSerializerTest {

    private static final Class<EnumSerializer> SERIALIZER_CLASS = EnumSerializer.class;

    @Test
    public void testConstructors() {
        // Test no-arg constructor
        assertInstanceOf(EnumSerializer.class, new EnumSerializer(null, null));
        // Test single arg constructor
        EnumValues enumValues = mockEnumValues();
        EnumSerializer serializer = new EnumSerializer(enumValues, null);
        assertEquals(enumValues, serializer.getEnumValues());
        // Test two arg constructor
        EnumSerializer serializerWithIndex = new EnumSerializer(enumValues, true);
        assertTrue(serializerWithIndex._serializeAsIndex);
        assertEquals(enumValues, serializerWithIndex.getEnumValues());
    }

    @Test
    public void testCreateContextualSerializer() throws Exception {
        // Test null input
        assertSame(SERIALIZER_CLASS, EnumSerializer.createContextual(null, null));
        // Test with EnumValues from constructor
        EnumValues enumValues = mockEnumValues();
        EnumSerializer serializer = new EnumSerializer(enumValues, null);
        EnumSerializer contextualSerializer = EnumSerializer.createContextual(null, null, serializedType(Enum.class), null);
        assertSame(SERIALIZER_CLASS, contextualSerializer);
        assertEquals(enumValues, contextualSerializer.getEnumValues());
        // Test with EnumValues from method
        Class<MyEnum> enumClass = MyEnum.class;
        EnumSerializer baseSerializer = new EnumSerializer(null, null);
        EnumSerializer contextualSerializerWithIndex = EnumSerializer.createContextual(null, null, serializedType(enumClass), null);
        EnumValues enumValues = mockEnumValues(MyEnum.class);
        contextualSerializerWithIndex.setEnumValues(enumValues);
        assertTrue(contextualSerializerWithIndex._serializeAsIndex);
        assertEquals(enumValues, contextualSerializerWithIndex.getEnumValues());
        // Test with JsonFormat.Value
        JsonFormat.Value format = mockJsonFormatValue();
        EnumSerializer contextualSerializerWithText = EnumSerializer.createContextual(null, null, serializedType(enumClass), format);
        assertFalse(contextualSerializerWithText._serializeAsIndex);
    }

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

        // Test serialization with no enumValues and no index mode
        EnumSerializer serializer = new EnumSerializer(null, false);
        MyEnum myEnum1 = MyEnum.VALUE_1;
        String json = writer.writeValueAsString(myEnum1);
        assertEquals("\"VALUE_1\"", json);

        // Test serialization with no enumValues and index mode
        EnumSerializer serializerWithIndex = new EnumSerializer(null, true);
        MyEnum myEnum2 = MyEnum.VALUE_2;
        json = writer.writeValueAsString(myEnum2);
        assertEquals("2", json);

        // Test serialization with enumValues and no index mode
        EnumValues enumValues = mockEnumValues();
        EnumSerializer serializerWithValues = new EnumSerializer(enumValues, false);
        MyEnum myEnum3 = MyEnum.VALUE_3;
        json = writer.writeValueAsString(myEnum3);
        assertEquals("\"VALUE_3\"", json);

        // Test serialization with enumValues and index mode
        EnumSerializer serializerWithValuesAndIndex = new EnumSerializer(enumValues, true);
        MyEnum myEnum4 = MyEnum.VALUE_4;
        json = writer.writeValueAsString(myEnum4);
        assertEquals("3", json);
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        JsonNode schema = EnumSerializer.construct(MyEnum.class, null, null, null).getSchema(null, serializedType(MyEnum.class));
        assertEquals("string", schema.getClass().getSimpleName());
        ObjectNode objectNode = (ObjectNode) schema;
        assertEquals("enum", objectNode.get("type").getValueAsString());
        ArrayNode enumNode = (ArrayNode) objectNode.get("enum");
        assertEquals(4, enumNode.size());
        Iterator<String> iterator = enumNode.elements();
        for (String value : Arrays.asList(MyEnum.VALUE_1.name(), MyEnum.VALUE_2.name(), MyEnum.VALUE_3.name(), MyEnum.VALUE_4.name())) {
            assertEquals(value, iterator.next());
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        JsonFormat.Value format = mockJsonFormatValue();
        JsonFormatVisitorWrapper visitor = mockJsonFormatVisitorWrapper();

        // Test no enumValues and no index mode
        EnumSerializer serializer = new EnumSerializer(null, false);
        serializer.acceptJsonFormatVisitor(visitor, serializedType(MyEnum.class));
        assertEquals("string", visitor.expectStringFormat(serializedType(MyEnum.class)).getClass().getSimpleName());

        // Test no enumValues and index mode
        EnumSerializer serializerWithIndex = new EnumSerializer(null, true);
        serializerWithIndex.acceptJsonFormatVisitor(visitor, serializedType(MyEnum.class));
        assertEquals("integer", visitor.expectIntFormat(serializedType(MyEnum.class), JsonParser.NumberType.INT).getClass().getSimpleName());

        // Test with enumValues and no index mode
        EnumValues enumValues = mockEnumValues();
        EnumSerializer serializerWithValues = new EnumSerializer(enumValues, false);
        serializerWithValues.acceptJsonFormatVisitor(visitor, serializedType(MyEnum.class));
        assertEquals("string", visitor.expectStringFormat(serializedType(MyEnum.class)).getClass().getSimpleName());

        // Test with enumValues and index mode
        EnumSerializer serializerWithValuesAndIndex = new EnumSerializer(enumValues, true);
        serializerWithValuesAndIndex.acceptJsonFormatVisitor(visitor, serializedType(MyEnum.class));
        assertEquals("integer", visitor.expectIntFormat(serializedType(MyEnum.class), JsonParser.NumberType.INT).getClass().getSimpleName());
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.reader();
        String json = "\"VALUE_1\"";
        MyEnum myEnum = reader.readValue(json, MyEnum.class);
        assertEquals(MyEnum.VALUE_1, myEnum);

        json = "2";
        myEnum = reader.readValue(json, MyEnum.class);
        assertEquals(MyEnum.VALUE_2, myEnum);

        json = "{\"enum\":[\"VALUE_3\",\"VALUE_4\"]}";
        MyEnum[] myEnumArray = reader.readValue(json, MyEnum[].class);
        assertEquals(MyEnum.VALUE_3, myEnumArray[0]);
        assertEquals(MyEnum.VALUE_4, myEnumArray[1]);
    }

    private JsonFormat.Value mockJsonFormatValue() {
        JsonFormat.Value value = mock(JsonFormat.Value.class);
        when(value.getShape()).thenReturn(Shape.STRING);
        return value;
    }

    private EnumValues mockEnumValues() {
        return mockEnumValues(null);
    }

    private EnumValues mockEnumValues(Class<?> enumClass) {
        return mockEnumValues(enumClass, Arrays.asList(EnumSet.allOf(enumClass.asSubclass(Enum.class))));
    }

    private EnumValues mockEnumValues(Class<?> enumClass, List<Enum<?>> enumConstants) {
        EnumValues enumValues = mock(EnumValues.class);
        when(enumValues.getEnumClass()).thenReturn(enumClass);
        when(enumValues.enums()).thenReturn(enumConstants.iterator());
        return enumValues;
    }

    private static Writer.DefaultSerializerProvider mockSerializerProvider() {
        return mock(Writer.DefaultSerializerProvider.class);
    }

    private static JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper() {
        return mock(JsonFormatVisitorWrapper.class);
    }

    private static JsonFormatVisitorWrapper mockJsonFormatVisitorWrapperWithIntFormat(SerializerProvider serializers) {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.expectIntFormat(any(), any())).thenReturn(mockIntFormatVisitor(serializers));
        return visitor;
    }

    private static JsonStringFormatVisitor mockIntFormatVisitor(SerializerProvider serializers) {
        return mock(JsonStringFormatVisitor.class);
    }

    private static JsonSerializer<Object> mockEnumDeserializer(ObjectMapper mapper) throws Exception {
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializer.deserialize(any(JsonParser.class), any())).thenAnswer(invocation -> {
            JsonParser parser = invocation.getArgument(0);
            Object text = parser.getText();
            Class<?> enumClass = invocation.getArgument(1).getType();
            return mapper.findValueToValue(text, enumClass, null, null);
        });
        return serializer;
    }

    private static JsonSerializer<Object> mockEnumValueDeserializer(ObjectMapper mapper) throws Exception {
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        when(serializer.deserialize(any(JsonParser.class), any())).thenAnswer(invocation -> {
            JsonParser parser = invocation.getArgument(0);
            Object text = parser.getText();
            Class<?> enumClass = invocation.getArgument(1).getType().getComponentType();
            return mapper.findValueToValue(text, enumClass, null, null);
        });
        return serializer;
    }

    private static Type serializedType(Class<?> type) {
        return new ClassType(type);
    }

    private static PropertyWriter mockPropertyWriter(SerializerProvider serializers, BeanProperty property) {
        PropertyWriter writer = mock(PropertyWriter.class);
        when(writer.getName()).thenReturn(property.getName());
        when(writer.getSerializerProvider()).thenReturn(serializers);
        return writer;
    }

    private static BeanPropertyWriter mockBeanPropertyWriter() {
        return mockBeanPropertyWriter(null);
    }

    private static BeanPropertyWriter mockBeanPropertyWriter(String name) {
        BeanPropertyWriter writer = mock(BeanPropertyWriter.class);
        if (name != null) {
            when(writer.getName()).thenReturn(name);
        }
        return writer;
    }

    private static ObjectNode mockObjectNode() {
        ObjectNode node = mock(ObjectNode.class);
        when(node.putPropertyQuietly(anyString(), any())).thenAnswer(invocation -> node);
        return node;
    }

    private static ArrayNode mockArrayNode() {
        ArrayNode node = mock(ArrayNode.class);
        when(node.add(any())).thenAnswer(invocation -> node);
        return node;
    }

    private static void setEnumValues(EnumSerializer serializer, EnumValues enumValues) {
        serializer.setEnumValues(enumValues);
    }

    private static boolean _serializeAsIndex(SerializerProvider serializers) {
        return EnumSerializer._isShapeWrittenUsingIndex(null, null, false);
    }

    private static boolean _isShapeWrittenUsingIndex(Class<?> enumClass, JsonFormat.Value format, boolean fromClass) {
        return EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, fromClass);
    }

    private static class MyEnum {
        public static final MyEnum VALUE_1 = new MyEnum("VALUE_1");
        public static final MyEnum VALUE_2 = new MyEnum("VALUE_2");
        public static final MyEnum VALUE_3 = new MyEnum("VALUE_3");
        public static final MyEnum VALUE_4 = new MyEnum("VALUE_4");

        private final String name;

        MyEnum(String name) {
            this.name = name;
        }

        @JsonFormat(shape = Shape.STRING)
        public String toString() {
            return name;
        }

        @Override
        public String name() {
            return name;
        }
    }
}