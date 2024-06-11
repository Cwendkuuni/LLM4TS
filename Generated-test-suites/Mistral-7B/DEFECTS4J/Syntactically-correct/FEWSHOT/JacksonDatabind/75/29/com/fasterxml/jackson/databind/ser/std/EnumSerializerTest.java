package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.util.StdSerializer;

public class EnumSerializerTest {

    static class TestEnum extends Enum<TestEnum> {
        TestEnum(String name) {
            super(name);
        }

        @JsonFormat(shape = Shape.NUMBER)
        static TestEnum TEST_ENUM_1 = new TestEnum("TEST_ENUM_1");
        @JsonFormat(shape = Shape.STRING)
        static TestEnum TEST_ENUM_2 = new TestEnum("TEST_ENUM_2");
    }

    @Test
    public void testCreateSerializer() throws Exception {
        EnumSerializer serializer = EnumSerializer.construct(TestEnum.class, null, null, null);
        assertSame(TestEnum.class, serializer.handledType());
    }

    @Test
    public void testSerializeAsIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromAnnotations(TestEnum.class), true);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        serializer.serialize(TestEnum.TEST_ENUM_1, gen, null);
        gen.flush();
        assertEquals(TestEnum.TEST_ENUM_1.ordinal(), mapper.readValue(gen.getOutputBytes(), int.class));
    }

    @Test
    public void testSerializeToString() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromAnnotations(TestEnum.class), false);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        serializer.serialize(TestEnum.TEST_ENUM_1, gen, null);
        gen.flush();
        assertEquals(TestEnum.TEST_ENUM_1.name(), mapper.readValue(gen.getOutputBytes(), String.class));

        serializer = new EnumSerializer(EnumValues.constructFromAnnotations(TestEnum.class), null);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        TestEnum deserialized = parser.readValueAs(TestEnum.class);
        assertEquals(TestEnum.TEST_ENUM_1, deserialized);
    }

    @Test
    public void testSerializeToStringWithCustomValues() throws Exception {
        EnumValues values = new EnumValues(TestEnum.class) {
            @Override
            public SerializableString serializedValueFor(Enum<?> enumValue) {
                return new SimpleSerializableString("CUSTOM_" + enumValue.name());
            }
        };
        EnumSerializer serializer = new EnumSerializer(values, false);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        serializer.serialize(TestEnum.TEST_ENUM_1, gen, null);
        gen.flush();
        assertEquals("CUSTOM_TEST_ENUM_1", mapper.readValue(gen.getOutputBytes(), String.class));
    }

    @Test
    public void testCreateContextualSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromAnnotations(TestEnum.class), false);
        ContextualSerializer contextual = serializer.createContextual(null, null);
        contextual.serialize(TestEnum.TEST_ENUM_1, gen, null);
        gen.flush();
        assertEquals(TestEnum.TEST_ENUM_1.name(), mapper.readValue(gen.getOutputBytes(), String.class));

        serializer = new EnumSerializer(EnumValues.constructFromAnnotations(TestEnum.class), true);
        contextual = serializer.createContextual(null, null);
        contextual.serialize(TestEnum.TEST_ENUM_1, gen, null);
        gen.flush();
        assertEquals(TestEnum.TEST_ENUM_1.ordinal(), mapper.readValue(gen.getOutputBytes(), int.class));

        serializer = new EnumSerializer(EnumValues.constructFromAnnotations(TestEnum.class), null);
        contextual = serializer.createContextual(null, null);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        TestEnum deserialized = parser.readValueAs(TestEnum.class);
        assertEquals(TestEnum.TEST_ENUM_1, deserialized);
    }

    @Test
    public void testCreateContextualSerializerWithCustomFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(Shape.NUMBER);

        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromAnnotations(TestEnum.class), null);
        ContextualSerializer contextual = serializer.createContextual(null, null);
        contextual = contextual.withFormat(new JsonFormat.Value(jsonFormat));
        contextual.serialize(TestEnum.TEST_ENUM_1, gen, null);
        gen.flush();
        assertEquals(TestEnum.TEST_ENUM_1.ordinal(), mapper.readValue(gen.getOutputBytes(), int.class));

        TestEnum deserialized = mapper.readValue(new ByteArrayInputStream(gen.getOutputBytes()), TestEnum.class);
        assertEquals(TestEnum.TEST_ENUM_1, deserialized);
    }

    @Test
    public void testGetSchema() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromAnnotations(TestEnum.class), false);
        Schema schema = serializer.getSchema(null, null);
        assertNotNull(schema);
        Class<TestEnum> enumClass = TestEnum.class;
        JsonTypeName valueType = schema.getSchemaName();
        assertEquals(JsonTypeName.VALUE_STRING, valueType);
        ArrayNode enumNode = schema.getSchema().get("enum");
        List<String> enumValues = Arrays.asList(TestEnum.TEST_ENUM_1.name(), TestEnum.TEST_ENUM_2.name());
        assertEquals(enumValues, Collections.unmodifiableList(enumNode.getValuesAs(String[].class)));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromAnnotations(TestEnum.class), false);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);

        serializer.acceptJsonFormatVisitor(visitor, null);
        assertNull(visitor.asIntFormat());
        JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(TestEnum.class);
        assertEquals(EnumSet.allOf(TestEnum.class), stringVisitor.enumTypes());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithCustomValues() throws Exception {
        EnumValues values = new EnumValues(TestEnum.class) {
            @Override
            public SerializableString serializedValueFor(Enum<?> enumValue) {
                return new SimpleSerializableString("CUSTOM_" + enumValue.name());
            }
        };
        EnumSerializer serializer = new EnumSerializer(values, false);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(TestEnum.class);
        assertEquals(EnumSet.allOf(TestEnum.class), stringVisitor.enumTypes());
        for (TestEnum testEnum : EnumSet.allOf(TestEnum.class)) {
            assertEquals("CUSTOM_" + testEnum.name(), stringVisitor.serializeValue(testEnum, null).toString());
        }
    }
}