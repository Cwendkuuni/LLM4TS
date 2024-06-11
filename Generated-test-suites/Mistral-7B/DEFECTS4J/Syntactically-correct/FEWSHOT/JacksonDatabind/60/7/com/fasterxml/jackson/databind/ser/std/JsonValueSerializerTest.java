package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.*;
import com.fasterxml.jackson.databind.jsonschema.*;

public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    @JsonTest
    public void testJsonValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test with annotated method
        JsonValue annotatedMethod = new JsonValue();
        AnnotatedMethod accessorMethod = AnnotatedMethod.construct(JsonValue.class.getDeclaredMethod("getValue"), JsonValue.class);
        accessorMethod.setAnnotation(annotatedMethod);

        JsonSerializer<Object> serializer = mapper.findValueSerializer(JsonValue.class, null);
        assertEquals(JsonValueSerializer.class, serializer.getClass());

        // Test with resolved serializer
        BeanProperty property = new BeanProperty("property", JsonValue.class);
        JsonSerializer<Object> resolvedSerializer = serializer.withResolved(property, new StdSerializer<>(String.class), false);
        assertEquals(StdSerializer.class, resolvedSerializer.getClass());

        // Test serialization
        Object jsonValueInstance = new JsonValue();
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};
        JsonValueSerializer serializerUnderTest = new JsonValueSerializer(accessorMethod, valueSerializer);

        String input = "Hello World!";
        JsonValue jsonValue = new JsonValue() {
            @JsonValue
            public String getValue() {
                return input;
            }
        };

        JsonNode result = mapper.writeValueAsTree(jsonValue);
        assertEquals(input, result.asText());

        // Test serialization with type information
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        JsonNode resultWithType = mapper.writeValueAsTree(jsonValue, typeSerializer);
        assertEquals(input + " : String", resultWithType.getClassId().getDisplayName());

        // Test deserialization
        JsonNode jsonNode = mapper.readTree("{\"value\": \"Hello World!\"}");
        JsonValue deserializedJsonValue = mapper.readValue(jsonNode, JsonValue.class);
        assertEquals(input, deserializedJsonValue.getValue());

        // Test handling of null value
        JsonNode nullResult = mapper.writeValueAsTree(jsonValue, typeSerializer);
        JsonNode nullJsonNode = mapper.readTree(mapper.writeValueAsString(nullResult));
        assertNull(mapper.readValue(nullJsonNode, JsonValue.class));

        // Test serialization with enum types
        Class<MyEnum> enumClass = MyEnum.class;
        JsonValueSerializer enumSerializer = new JsonValueSerializer(new AnnotatedMethod(enumClass.getMethod("getValue"), enumClass), null);
        String enumJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MyEnum("TEST"));
        assertEquals(enumJson, enumSerializer.getValue(new MyEnum("TEST")).getValue());

        // Test serialization with custom serializer
        JsonSerializer<String> customSerializer = new StdSerializer<String>(String.class) {};
        JsonValue jsonValueWithCustomSerializer = new JsonValue() {
            @JsonValue
            public String getValue() {
                return "Custom Value";
            }

            @JsonSerialize
            public JsonSerializer<String> getValueSerializer() {
                return customSerializer;
            }
        };

        JsonSerializer<Object> jsonValueSerializerWithCustomSerializer = mapper.findValueSerializer(JsonValue.class, null);
        JsonValueSerializer customSerializerWrapper = (JsonValueSerializer) jsonValueSerializerWithCustomSerializer;
        customSerializerWrapper._valueSerializer = customSerializer;

        JsonNode customResult = mapper.writeValueAsTree(jsonValueWithCustomSerializer);
        assertEquals("Custom Value", customResult.asText());
    }

    @JsonTest
    public void testJsonValueSerializerContextualization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Class<MyBean> myBeanClass = MyBean.class;
        BeanProperty property = new BeanProperty("property", MyBean.class);

        JsonValue annotatedMethod = new JsonValue();
        AnnotatedMethod accessorMethod = AnnotatedMethod.construct(myBeanClass.getDeclaredMethod("getValue"), MyBean.class);
        accessorMethod.setAnnotation(annotatedMethod);

        JsonSerializer<Object> serializer = mapper.findValueSerializer(JsonValue.class, null);
        assertEquals(JsonValueSerializer.class, serializer.getClass());

        JsonSerializer<Object> resolvedSerializer = serializer.withResolved(property, null, false);
        assertEquals(MyBeanSerializer.class, resolvedSerializer.getClass());
    }

    static class MyBean {
        private String property;

        @JsonValue
        public String getValue() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

    static class MyEnum {
        private final String value;

        MyEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}