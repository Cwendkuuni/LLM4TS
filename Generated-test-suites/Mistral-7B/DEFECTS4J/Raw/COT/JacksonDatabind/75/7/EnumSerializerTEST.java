package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

/**
 * JUnit test cases for EnumSerializer class.
 *
 * @author John Doe
 */
public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private EnumValues enumValues;

    @Before
    public void setUp() {
        // Create EnumSerializer instance with EnumValues
        MyEnum[] myEnums = MyEnum.values();
        enumValues = new EnumValues(MyEnum.class, Arrays.asList(myEnums));
        enumSerializer = new EnumSerializer(enumValues, null);
    }

    @Test
    public void testSerializeAsIndex() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0);

        // Serialize as index
        enumSerializer.serialize(MyEnum.VALUE1, gen, null);
        assertEquals("1", gen.getOutputContent().toString().trim());

        gen.close();
    }

    @Test
    public void testSerializeWithToString() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        // Serialize using toString()
        enumSerializer.serialize(MyEnum.VALUE1, gen, null);
        assertEquals("VALUE1", gen.getOutputContent().toString().trim());

        gen.close();
    }

    @Test
    public void testSerializeWithName() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0);

        // Serialize using name()
        enumSerializer.serialize(MyEnum.VALUE1, gen, null);
        assertEquals("VALUE1_NAME", gen.getOutputContent().toString().trim());

        gen.close();
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0);
        ObjectMapper mapper = new ObjectMapper();

        // Serialize with custom format using annotation
        MyEnumValue customAnnotation = new MyEnumValue(MyEnum.VALUE2, "CUSTOM_NAME");
        MyEnumValueWithCustomFormat enumValue = new MyEnumValueWithCustomFormat(customAnnotation);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setEnumValues(new EnumValues(MyEnumValueWithCustomFormat.class, Arrays.asList(enumValue)));
        mapper.addValueInclusion(MyEnumValueWithCustomFormat.class, "VALUE", "customName");

        enumSerializer = enumSerializer.createContextual(null, null);
        enumSerializer.acceptJsonFormatVisitor(mapper.getSerializationConfig().getFactory(), MyEnumValueWithCustomFormat.class);

        // Serialize with custom format using annotation
        enumSerializer.serialize(enumValue, gen, null);
        assertEquals("CUSTOM_NAME", gen.getOutputContent().toString().trim());

        gen.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedSerializationShape() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0);

        ObjectMapper mapper = new ObjectMapper();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.ARRAY);

        enumSerializer = enumSerializer.createContextual(null, null);
        enumSerializer.acceptJsonFormatVisitor(mapper.getSerializationConfig().getFactory(), MyEnum.class);

        // Test unsupported serialization shape
        enumSerializer.serialize(MyEnum.VALUE1, gen, null);

        gen.close();
    }

    @Test
    public void testGetSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode schema = enumSerializer.getSchema(mapper.getSerializationConfig(), MyEnum.class.getType());

        // Check if schema contains expected schema information
        ObjectNode schemaObject = (ObjectNode) schema;
        JsonNode enumNode = schemaObject.get("enum");
        assertEquals(8, enumNode.size());
        assertTrue(enumNode.has("VALUE1"));
        assertTrue(enumNode.has("VALUE2"));
        assertTrue(enumNode.has("VALUE3"));
        assertTrue(enumNode.has("VALUE4"));
        assertTrue(enumNode.has("VALUE5"));
        assertTrue(enumNode.has("VALUE6"));
        assertTrue(enumNode.has("VALUE7"));
        assertTrue(enumNode.has("VALUE8"));
    }

    @Test
    public void testCreateContextual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MyEnumValue customAnnotation1 = new MyEnumValue(MyEnum.VALUE1, "CUSTOM_NAME1");
        MyEnumValue customAnnotation2 = new MyEnumValue(MyEnum.VALUE2, "CUSTOM_NAME2");
        MyEnumValueWithCustomFormat enumValue1 = new MyEnumValueWithCustomFormat(customAnnotation1);
        MyEnumValueWithCustomFormat enumValue2 = new MyEnumValueWithCustomFormat(customAnnotation2);

        // Test createContextual() with custom format using annotation
        JsonFormat.Value format1 = new JsonFormat.Value();
        format1.setShape(JsonFormat.Shape.STRING);
        format1.setEnumValues(new EnumValues(MyEnumValueWithCustomFormat.class, Arrays.asList(enumValue1)));
        mapper.addValueInclusion(MyEnumValueWithCustomFormat.class, "VALUE", "customName");
        JsonFormat.Value format2 = new JsonFormat.Value();
        format2.setShape(JsonFormat.Shape.STRING);
        format2.setEnumValues(new EnumValues(MyEnumValueWithCustomFormat.class, Arrays.asList(enumValue2)));
        mapper.addValueInclusion(MyEnumValueWithCustomFormat.class, "VALUE", "customName");

        SerializationConfig config = mapper.getSerializationConfig();
        ContextualSerializer<Enum<?>> serializer1 = enumSerializer.createContextual(null, null);
        ContextualSerializer<Enum<?>> serializer2 = enumSerializer.createContextual(null, null);

        // Test if serializers are different based on custom format
        assertNotSame(serializer1, serializer2);
        assertFalse(serializer1.equals(serializer2));

        // Test if serializers can be used to serialize different enums
        ArrayNode enumNodes1 = mapper.writeValueAsTree(new ObjectNode(ObjectMapper.getDefaultInstance().getFactory())
                .put("test", serializer1.serializeValueAsString(MyEnum.VALUE1, null)));
        ArrayNode enumNodes2 = mapper.writeValueAsTree(new ObjectNode(ObjectMapper.getDefaultInstance().getFactory())
                .put("test", serializer2.serializeValueAsString(MyEnum.VALUE2, null)));
        assertNotSame(enumNodes1.toString(), enumNodes2.toString());
    }

    enum MyEnum {
        VALUE1("VALUE1_NAME", 1),
        VALUE2("VALUE2_NAME", 2),
        VALUE3("VALUE3_NAME", 3),
        VALUE4("VALUE4_NAME", 4),
        VALUE5("VALUE5_NAME", 5),
        VALUE6("VALUE6_NAME", 6),
        VALUE7("VALUE7_NAME", 7),
        VALUE8("VALUE8_NAME", 8);

        private final String name;
        private final int value;

        MyEnum(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @JsonFormat(shape = Shape.STRING, value = "VALUE{name}")
        public String getName() {
            return name;
        }
    }

    @JsonFormat(shape = Shape.STRING, value = "CUSTOM_NAME{name}")
    static class MyEnumValue {
        private final MyEnum value;
        private final String customName;

        MyEnumValue(MyEnum value, String customName) {
            this.value = value;
            this.customName = customName;
        }

        @JsonValue
        public MyEnum getValue() {
            return value;
        }
    }

    @JsonFormat(shape = Shape.STRING, value = "customName")
    static class MyEnumValueWithCustomFormat {
        private final MyEnumValue myEnumValue;

        MyEnumValueWithCustomFormat(MyEnumValue myEnumValue) {
            this.myEnumValue = myEnumValue;
        }

        @JsonValue
        public MyEnumValue getValue() {
            return myEnumValue;
        }
    }
}