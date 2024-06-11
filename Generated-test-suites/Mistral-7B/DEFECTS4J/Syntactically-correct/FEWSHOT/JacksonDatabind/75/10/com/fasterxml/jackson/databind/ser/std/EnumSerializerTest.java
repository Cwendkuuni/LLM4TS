package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Test cases for {@link EnumSerializer}.
 */
@JacksonStdImpl
public class EnumSerializerTest {

    @Test
    public void testSerialization() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromEnumClass(MyEnum.class), null);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(MyEnum.VALUE_1);
        assertEquals("\"VALUE_1\"", json);

        Enum<?> deserialized = mapper.readValue(json, Enum.class);
        assertEquals(MyEnum.VALUE_1, deserialized);

        // Test with index serialization
        serializer = new EnumSerializer(EnumValues.constructFromEnumClass(MyEnum.class), Boolean.TRUE);
        json = mapper.writeValueAsString(MyEnum.VALUE_1);
        assertEquals("0", json);

        deserialized = mapper.readValue(json, Enum.class);
        assertEquals(MyEnum.VALUE_1, deserialized);
    }

    @Test
    public void testSerializationWithCustomFormat() throws Exception {
        MyEnumSerializer serializer = new MyEnumSerializer(EnumValues.constructFromEnumClass(MyEnum.class), null);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(MyEnum.VALUE_1, new MyJsonFormat());
        assertEquals("\"VALUE_1_ENUM\"", json);

        Enum<?> deserialized = mapper.readValue(json, Enum.class);
        assertEquals(MyEnum.VALUE_1, deserialized);
    }

    @Test
    public void testSchemaGeneration() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromEnumClass(MyEnum.class), null);

        Schema schema = serializer.getSchema(new SerializerProvider(), null);

        ObjectNode schemaNode = (ObjectNode) schema;
        ArrayNode enumNode = schemaNode.get("enum");
        assertEquals(2, enumNode.size());
        assertTrue(enumNode.get(0).isTextual());
        assertTrue(enumNode.get(1).isTextual());
        assertEquals("VALUE_1", enumNode.get(0).asText());
        assertEquals("VALUE_2", enumNode.get(1).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromEnumClass(MyEnum.class), null);

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(new byte[0]));
        JsonParser.ContextualSerializationContext context = new JsonParser.ContextualSerializationContext();
        context.setPropertyId("myProperty");
        parser.setContextualHandler(context);

        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(parser), null);

        JsonNode schemaNode = parser.getCodec().treeToValues(parser.readTree(), null);
        ObjectNode schema = (ObjectNode) schemaNode;
        ArrayNode enumNode = schema.get("enum");
        assertEquals(2, enumNode.size());
        assertTrue(enumNode.get(0).isTextual());
        assertTrue(enumNode.get(1).isTextual());
        assertEquals("VALUE_1", enumNode.get(0).asText());
        assertEquals("VALUE_2", enumNode.get(1).asText());
    }

    static class MyJsonFormat extends JsonFormat<Object> {
        @Override
        public Shape shape() {
            return Shape.STRING;
        }

        @Override
        public String serializationShape() {
            return "VALUE_1_ENUM";
        }
    }

    enum MyEnum {
        VALUE_1,
        VALUE_2;
    }

    static class MyEnumSerializer extends EnumSerializer {
        public MyEnumSerializer(EnumValues values, Boolean serializeAsIndex) {
            super(values, serializeAsIndex);
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, Type typeHint)
                throws JsonMappingException {
            visitor.writer().writeString("VALUE_1_ENUM");
        }
    }
}