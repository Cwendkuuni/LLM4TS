package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.intros.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {

    @Test
    public void testEnumSerializer() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.valueOf(TestEnum.class), null);
        assertNotNull(serializer);
        assertEquals(TestEnum.class, serializer.handledType());
        assertNull(serializer._values);
        assertEquals(false, serializer._serializeAsIndex);

        // Test serialization with default values
        ObjectMapper mapper = new ObjectMapper();
        TestEnum value = TestEnum.ONE;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        serializer.serialize(value, gen, mapper.getSerializationConfig());
        assertEquals("ONE", gen.getOutputPosition().getText());

        // Test serialization with shape annotation
        TestEnumWithShape shapeAnnotationValue = TestEnumWithShape.ONE;
        JsonFormat shapeAnnotation = new JsonFormat();
        shapeAnnotation.shape(Shape.INT);
        TestEnumWithShape serializableValue = TestEnumWithShape.TWO;
        JsonSerializer<TestEnumWithShape> shapeSerializer = EnumSerializer.construct(
                TestEnumWithShape.class, mapper.getSerializationConfig(), null, shapeAnnotation);
        gen.clear();
        shapeSerializer.serialize(serializableValue, gen, mapper.getSerializationConfig());
        assertEquals("0", gen.getOutputPosition().getText());

        // Test serialization with index and toString features enabled
        ObjectMapper mapperWithFeatures = new ObjectMapper();
        mapperWithFeatures.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        mapperWithFeatures.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TestEnumWithIndexAndToString indexAndToStringValue = TestEnumWithIndexAndToString.TWO;
        JsonSerializer<TestEnumWithIndexAndToString> indexAndToStringSerializer = EnumSerializer.construct(
                TestEnumWithIndexAndToString.class, mapperWithFeatures.getSerializationConfig(), null, null);
        gen.clear();
        indexAndToStringSerializer.serialize(indexAndToStringValue, gen, mapperWithFeatures.getSerializationConfig());
        assertEquals("TWO", gen.getOutputPosition().getText());

        // Test serialization with index and shape annotation
        TestEnumWithIndexAndShape indexAndShapeValue = TestEnumWithIndexAndShape.TWO;
        JsonFormat indexAndShapeAnnotation = new JsonFormat();
        indexAndShapeAnnotation.shape(Shape.INT);
        TestEnumWithIndexAndShape serializableIndexAndShapeValue = TestEnumWithIndexAndShape.THREE;
        JsonSerializer<TestEnumWithIndexAndShape> indexAndShapeSerializer = EnumSerializer.construct(
                TestEnumWithIndexAndShape.class, mapperWithFeatures.getSerializationConfig(), null, indexAndShapeAnnotation);
        gen.clear();
        indexAndShapeSerializer.serialize(serializableIndexAndShapeValue, gen, mapperWithFeatures.getSerializationConfig());
        assertEquals("2", gen.getOutputPosition().getText());

        // Test serialization with custom EnumValues
        EnumValues customValues = new EnumValues(TestEnumCustomValues.class);
        TestEnumCustomValues customValue = TestEnumCustomValues.TWO;
        serializer = new EnumSerializer(customValues, null);
        gen.clear();
        serializer.serialize(customValue, gen, mapperWithFeatures.getSerializationConfig());
        assertEquals("customTwo", gen.getOutputPosition().getText());

        // Test deserialization
        ObjectMapper mapperForDeserialization = new ObjectMapper();
        String jsonString = "\"ONE\"";
        TestEnum deserializedValue = mapperForDeserialization.readValue(jsonString, TestEnum.class);
        assertEquals(TestEnum.ONE, deserializedValue);

        // Test deserialization with shape annotation
        jsonString = "0";
        TestEnumWithShape deserializedShapeValue = mapperForDeserialization.readValue(jsonString,
                TestEnumWithShape.class);
        assertEquals(TestEnumWithShape.ZERO, deserializedShapeValue);

        // Test deserialization with index and toString features enabled
        TestEnumWithIndexAndToString deserializedIndexAndToStringValue = mapperForDeserialization
                .readValue(jsonString, TestEnumWithIndexAndToString.class);
        assertEquals(TestEnumWithIndexAndToString.ZERO, deserializedIndexAndToStringValue);

        // Test deserialization with index and shape annotation
        jsonString = "2";
        TestEnumWithIndexAndShape deserializedIndexAndShapeValue = mapperForDeserialization
                .readValue(jsonString, TestEnumWithIndexAndShape.class);
        assertEquals(TestEnumWithIndexAndShape.THREE, deserializedIndexAndShapeValue);

        // Test deserialization with custom EnumValues
        String customJsonString = "\"customOne\"";
        TestEnumCustomValues deserializedCustomValue = mapperForDeserialization
                .readValue(customJsonString, TestEnumCustomValues.class);
        assertEquals(TestEnumCustomValues.ONE, deserializedCustomValue);

        // Test contextual serialization
        TestEnumWithContextual testEnumWithContextual = TestEnumWithContextual.ONE;
        BeanDescription beanDescription = new BeanDescription(TestEnumWithContextual.class);
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(Shape.STRING);
        JsonSerializer<TestEnumWithContextual> contextualSerializer = EnumSerializer.construct(
                TestEnumWithContextual.class, mapperWithFeatures.getSerializationConfig(), beanDescription, jsonFormat);
        gen.clear();
        contextualSerializer.serialize(testEnumWithContextual, gen, mapperWithFeatures.getSerializationConfig());
        assertEquals("ONE", gen.getOutputPosition().getText());

        // Test contextual serialization with index format
        JsonFormat jsonFormatWithIndex = new JsonFormat();
        jsonFormatWithIndex.shape(Shape.INT);
        JsonSerializer<TestEnumWithContextual> contextualSerializerWithIndex = EnumSerializer.construct(
                TestEnumWithContextual.class, mapperWithFeatures.getSerializationConfig(), beanDescription, jsonFormatWithIndex);
        gen.clear();
        contextualSerializerWithIndex.serialize(testEnumWithContextual, gen, mapperWithFeatures.getSerializationConfig());
        assertEquals("1", gen.getOutputPosition().getText());

        // Test contextual serialization with string format
        JsonFormat jsonFormatWithString = new JsonFormat();
        jsonFormatWithString.shape(Shape.STRING);
        JsonSerializer<TestEnumWithContextual> contextualSerializerWithString = EnumSerializer.construct(
                TestEnumWithContextual.class, mapperWithFeatures.getSerializationConfig(), beanDescription, jsonFormatWithString);
        gen.clear();
        contextualSerializerWithString.serialize(testEnumWithContextual, gen, mapperWithFeatures.getSerializationConfig());
        assertEquals("ONE", gen.getOutputPosition().getText());

        // Test getSchema method
        SerializerProvider serializerProvider = mapperWithFeatures.getSerializationConfig().getSerializerProvider();
        JsonNode schema = EnumSerializer.construct(TestEnum.class, mapperWithFeatures.getSerializationConfig(), null, null)
                .getSchema(serializerProvider, TestEnum.class);
        JsonNode expectedSchema = mapperWithFeatures.readTree("{\"type\": \"string\"}");
        assertSame(expectedSchema, schema);

        // Test acceptJsonFormatVisitor method
        ObjectNode schemaNode = mapperWithFeatures.createObjectNode();
        JsonFormatVisitorWrapper visitor = mapperWithFeatures.getSerializationConfig().getAnnotationIntrospector()
                .findPropertySerializer(TestEnumWithContextual.class, "myEnumProperty");
        EnumSerializer enumSerializer = (EnumSerializer) visitor.getProvider().getSerializerProvider()
                .findValueSerializer(TestEnumWithContextual.class);
        enumSerializer.acceptJsonFormatVisitor(visitor, TestEnumWithContextual.class);
        assertEquals("{\"enum\": [\"ONE\", \"TWO\", \"THREE\"]}", schemaNode.toString());
    }

    static class TestEnum {
        public static enum EnumValues {
            ONE, TWO, THREE;
        }

        public static enum Values {
            VALUE_ONE, VALUE_TWO, VALUE_THREE;
        }

        public static enum TestEnumCustomValues {
            ONE("customOne"), TWO("customTwo"), THREE("customThree");

            private final String name;

            TestEnumCustomValues(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        public static enum TestEnumWithShape {
            ZERO, ONE, TWO, THREE;

            @JsonFormat(shape = Shape.INT)
            public static final TestEnumWithShape[] VALUES = TestEnumWithShape.values();
        }

        public static enum TestEnumWithIndexAndToString {
            ZERO, ONE, TWO, THREE;

            @JsonFormat(shape = Shape.STRING)
            public static final TestEnumWithIndexAndToString[] VALUES = TestEnumWithIndexAndToString.values();

            public String toString() {
                return name();
            }
        }

        public static enum TestEnumWithIndexAndShape {
            ZERO, ONE, TWO, THREE;

            @JsonFormat(shape = Shape.INT)
            public static final TestEnumWithIndexAndShape[] VALUES = TestEnumWithIndexAndShape.values();
        }

        public static enum TestEnumWithContextual {
            ONE, TWO, THREE;

            @JsonFormat(shape = Shape.STRING)
            public String myEnumProperty;
        }
    }

    @JsonFormat(shape = Shape.INT)
    static class TestEnumWithShape {
        public static enum Values {
            ZERO, ONE, TWO, THREE;
        }

        public static TestEnumWithShape[] values() {
            return Values.values();
        }
    }
}