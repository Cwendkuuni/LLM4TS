package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EnumSerializerTest {

    @Test
    public void testSerialization() throws JsonProcessingException, IOException {
        Enum<?> enumValue = MyEnum.ONE;
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(enumValue);
        assertEquals("\"ONE\"", json);

        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(MyEnum.class.getEnumConstants(), null);
        serializer.serialize(enumValue, gen, null);
        assertEquals("\"ONE\"", gen.getOutputValues().toString());

        ObjectNode schema = serializer.getSchema(null, null);
        assertEquals("string", schema.get("type").asText());
        assertEquals("[\"ONE\", \"TWO\"]", schema.get("enum").asText());

        TypeSerializer ts = mapper.getTypeSerializer();
        ObjectNode schemaWithTypeSerializer = serializer.getSchema(null, MyEnum.class);
        assertEquals("string", schemaWithTypeSerializer.get("type").asText());
        assertEquals("[\"ONE\", \"TWO\"]", schemaWithTypeSerializer.get("enum").asText());
        assertEquals("null", ts.serializeValueAsString(null, null, null));
        assertNull(ts.deserialize(schemaWithTypeSerializer.get("enum").get(0).asText(), MyEnum.class, null));
    }

    @Test
    public void testSerializationWithIndex() throws JsonProcessingException, IOException {
        Enum<?> enumValue = MyEnum.ONE;
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withFeature(com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_INDEX);
        String json = writer.writeValueAsString(enumValue);
        assertEquals("0", json);

        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(MyEnum.class.getEnumConstants(), true);
        serializer.serialize(enumValue, gen, null);
        assertEquals(0, gen.getNextValue());

        ObjectNode schema = serializer.getSchema(null, null);
        assertEquals("integer", schema.get("type").asText());

        ObjectNode schemaWithTypeSerializer = serializer.getSchema(null, MyEnum.class);
        assertEquals("integer", schemaWithTypeSerializer.get("type").asText());

        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.INT);
        EnumSerializer serializerWithJsonFormat = new EnumSerializer(MyEnum.class.getEnumConstants(), null, jsonFormat);
        assertEquals(JsonFormat.Shape.INT, serializerWithJsonFormat.getShape());
    }

    @Test
    public void testSerializationWithToString() throws JsonProcessingException, IOException {
        Enum<?> enumValue = MyEnum.TWO;
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withFeature(com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        String json = writer.writeValueAsString(enumValue);
        assertEquals("\"TWO_LONG_NAME\"", json);

        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(MyEnum.class.getEnumConstants(), null, null, true);
        serializer.serialize(enumValue, gen, null);
        assertEquals("\"TWO_LONG_NAME\"", gen.getOutputValues().toString());

        ObjectNode schema = serializer.getSchema(null, null);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testSerializationWithNull() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withNullValue(MyEnum.class);
        String json = writer.writeValueAsString(null);
        assertEquals("null", json);

        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(MyEnum.class.getEnumConstants(), null);
        serializer.serialize(null, gen, null);
        assertNull(gen.getOutputValues());

        ObjectNode schema = serializer.getSchema(null, null);
        assertEquals("null", schema.get("default").asText());
    }

    @Test
    public void testSerializationWithShapeAnnotation() throws JsonProcessingException, IOException {
        MyEnumWithShape annotatedEnum = MyEnumWithShape.ONE;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(annotatedEnum.getClass().getEnumConstants(), null);
        serializer.serialize(annotatedEnum, gen, null);
        assertEquals(JsonFormat.Shape.OBJECT, serializer.getShape());
    }

    @Test
    public void testSerializationWithShapeAnnotationArray() throws JsonProcessingException, IOException {
        MyEnumWithShapeArray annotatedEnumArray = MyEnumWithShapeArray.ONE;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(annotatedEnumArray.getClass().getEnumConstants(), null);
        serializer.serialize(annotatedEnumArray, gen, null);
        assertEquals(JsonFormat.Shape.ARRAY, serializer.getShape());
    }

    @Test
    public void testSerializationWithShapeAnnotationUnion() throws JsonProcessingException, IOException {
        MyEnumWithShapeUnion annotatedEnumUnion = MyEnumWithShapeUnion.ONE;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(annotatedEnumUnion.getClass().getEnumConstants(), null);
        serializer.serialize(annotatedEnumUnion, gen, null);
        assertEquals(JsonFormat.Shape.ANY, serializer.getShape());
    }

    @Test
    public void testSerializationWithShapeAnnotationNonNull() throws JsonProcessingException, IOException {
        MyEnumWithShapeNonNull annotatedEnumNonNull = MyEnumWithShapeNonNull.ONE;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(annotatedEnumNonNull.getClass().getEnumConstants(), null);
        serializer.serialize(annotatedEnumNonNull, gen, null);
        assertEquals(JsonFormat.Shape.STRING, serializer.getShape());
    }

    @Test
    public void testSerializationWithJsonFormatAnnotation() throws JsonProcessingException, IOException {
        MyEnumWithJsonFormat annotatedEnum = MyEnumWithJsonFormat.ONE;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(annotatedEnum.getClass().getEnumConstants(), null);
        serializer.serialize(annotatedEnum, gen, null);
        assertEquals(JsonFormat.Shape.STRING, serializer.getShape());
        assertEquals("\"ONE_SHORT_NAME\"", gen.getOutputValues().toString());
    }

    @Test
    public void testSerializationWithJsonFormatAnnotationArray() throws JsonProcessingException, IOException {
        MyEnumWithJsonFormatArray annotatedEnumArray = MyEnumWithJsonFormatArray.ONE;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(annotatedEnumArray.getClass().getEnumConstants(), null);
        serializer.serialize(annotatedEnumArray, gen, null);
        assertEquals(JsonFormat.Shape.ARRAY, serializer.getShape());
        assertEquals("[\"ONE_SHORT_NAME\", \"TWO_SHORT_NAME\"]", gen.getOutputValues().toString());
    }

    @Test
    public void testSerializationWithJsonFormatAnnotationUnion() throws JsonProcessingException, IOException {
        MyEnumWithJsonFormatUnion annotatedEnumUnion = MyEnumWithJsonFormatUnion.ONE;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(annotatedEnumUnion.getClass().getEnumConstants(), null);
        serializer.serialize(annotatedEnumUnion, gen, null);
        assertEquals(JsonFormat.Shape.ANY, serializer.getShape());
        assertEquals("[\"ONE_SHORT_NAME\", \"TWO_SHORT_NAME\"]", gen.getOutputValues().toString());
    }

    @Test
    public void testSerializationWithJsonFormatAnnotationNonNull() throws JsonProcessingException, IOException {
        MyEnumWithJsonFormatNonNull annotatedEnumNonNull = MyEnumWithJsonFormatNonNull.ONE;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        EnumSerializer serializer = new EnumSerializer(annotatedEnumNonNull.getClass().getEnumConstants(), null);
        serializer.serialize(annotatedEnumNonNull, gen, null);
        assertEquals(JsonFormat.Shape.STRING, serializer.getShape());
        assertEquals("\"ONE_LONG_NAME\"", gen.getOutputValues().toString());
    }

    private enum MyEnum {
        ONE,
        TWO
    }

    @JsonFormat(shape = Shape.OBJECT)
    private enum MyEnumWithShape {
        ONE,
        TWO
    }

    @JsonFormat(shape = Shape.ARRAY)
    private enum MyEnumWithShapeArray {
        ONE,
        TWO
    }

    @JsonFormat(shape = Shape.ANY)
    private enum MyEnumWithShapeUnion {
        ONE,
        TWO
    }

    @JsonFormat(shape = Shape.STRING, value = "ONE_LONG_NAME")
    private enum MyEnumWithShapeNonNull {
        ONE,
        TWO
    }

    private static class MyEnumWithJsonFormat {
        @JsonFormat(shape = Shape.STRING, value = "ONE_SHORT_NAME")
        String name;

        MyEnumWithJsonFormat(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class MyEnumWithJsonFormatArray {
        @JsonFormat(shape = Shape.ARRAY, value = {"ONE_SHORT_NAME", "TWO_SHORT_NAME"})
        String[] names;

        MyEnumWithJsonFormatArray(String[] names) {
            this.names = names;
        }

        public String[] getNames() {
            return names;
        }

        @Override
        public String toString() {
            return "[\"ONE_SHORT_NAME\", \"TWO_SHORT_NAME\"]";
        }
    }

    private static class MyEnumWithJsonFormatUnion {
        @JsonFormat(shape = Shape.ANY, value = {"ONE_SHORT_NAME", "TWO_SHORT_NAME"})
        String name;

        MyEnumWithJsonFormatUnion(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "[\"ONE_SHORT_NAME\", \"TWO_SHORT_NAME\"]";
        }
    }

    private static class MyEnumWithJsonFormatNonNull {
        @JsonFormat(shape = Shape.STRING, value = "ONE_LONG_NAME")
        String name;

        MyEnumWithJsonFormatNonNull(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ONE_LONG_NAME";
        }
    }
}