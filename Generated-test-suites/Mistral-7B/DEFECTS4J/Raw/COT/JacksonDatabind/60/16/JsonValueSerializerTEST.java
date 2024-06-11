package com.example.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    private ObjectTestObject testObject;
    private JsonObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        testObject = new ObjectTestObject();
        testObject.setName("Test Name");
        testObject.setValue(42);

        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializer_EmptyClass_DefaultSerializer() throws IOException {
        JsonSerializer<Object> serializer = mapper.findValueSerializer(Object.class, null);
        JsonNode result = mapper.writeValueAsTree(new Object());

        assertEquals(JsonNodeFactory.instance.textValue("null"), result);
        assertEquals(serializer.getClass(), JsonValueSerializer.class);
    }

    @Test
    public void testSerializer_JsonValueAnnotatedMethod_CustomSerializer() throws IOException {
        Method getValueMethod = ObjectTestObject.class.getMethod("getValue");
        JsonSerializer<Object> customSerializer = mapper.findValueSerializer(getValueMethod.getReturnType(), null);
        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, customSerializer);

        JsonNode result = mapper.writeValueAsTree(testObject);

        assertEquals(JsonNodeFactory.instance.numberValue(42), result.get(0));
        assertEquals(customSerializer.getClass(), JsonValueSerializer.getValueSerializer(serializer).getClass());
    }

    @Test
    public void testSerializer_JsonValueAnnotatedMethod_NullValue() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(testObject.getClass().getMethod("getValue"), null);

        JsonNode result = mapper.writeValueAsTree(testObject);

        assertEquals(JsonNodeFactory.instance.nullValue(), result.get(0));
    }

    @Test
    public void testSerializer_JsonValueAnnotatedMethod_Enum_StringFormatVisitor() throws IOException {
        Class<MyEnum> enumClass = MyEnum.class;
        Method getValueMethod = ObjectTestEnum.class.getMethod("getValue");
        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, null);

        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getSchemaProvider().findSchemaSerializer(Object.class);
        visitor = visitor.wrap(new JsonStringFormatVisitor(null));

        JsonNode schemaNode = serializer.acceptJsonFormatVisitor(visitor, Object.class);

        Set<String> enums = schemaNode.path("enumValues").asTextual();
        assertTrue(enums.contains("FIRST"));
        assertTrue(enums.contains("SECOND"));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializer_JsonValueAnnotatedMethod_Exception() throws IOException {
        Method getValueMethod = ObjectTestThrowException.class.getMethod("getValue");
        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, null);

        JsonNode result = mapper.writeValueAsTree(new ObjectTestThrowException());
    }

    // Add more test cases as needed for other methods or edge cases in the JsonValueSerializer class.

    static class ObjectTestObject {
        private String name;
        private int value;

        public String getName() {
            return name;
        }

        @JsonValue
        public int getValue() {
            return value;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    static class ObjectTestEnum {
        private MyEnum value;

        @JsonValue
        public MyEnum getValue() {
            return value;
        }

        public void setValue(MyEnum value) {
            this.value = value;
        }
    }

    static class ObjectTestThrowException {
        @JsonValue
        public int getValue() throws IOException {
            throw new IOException("Test Exception");
        }
    }

    static enum MyEnum {
        FIRST,
        SECOND
    }
}