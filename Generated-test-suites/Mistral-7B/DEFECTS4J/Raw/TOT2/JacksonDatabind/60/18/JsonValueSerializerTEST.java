import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializer_BasicUsage() throws IOException {
        JsonMapper mapper = new JsonMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Given
        MyClass myClass = new MyClass("Hello World!");
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, myClass.get_accessorMethod(), null, false);

        // When
        String json = mapper.writeValueAsString(myClass);
        String jsonFromObjectMapper = objectMapper.writeValueAsString(myClass);

        // Then
        JsonNode jsonNode = mapper.readTree(json);
        JsonNode jsonNodeFromObjectMapper = objectMapper.readTree(jsonFromObjectMapper);
        Assert.assertEquals(jsonNode, jsonNodeFromObjectMapper);
        Assert.assertEquals(json, jsonFromObjectMapper);
    }

    @Test
    public void testJsonValueSerializer_NullValue() throws IOException {
        JsonMapper mapper = new JsonMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Given
        MyClass myClass = new MyClass(null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, myClass.get_accessorMethod(), null, false);

        // When
        String json = mapper.writeValueAsString(myClass);
        String jsonFromObjectMapper = objectMapper.writeValueAsString(myClass);

        // Then
        JsonNode jsonNode = mapper.readTree(json);
        JsonNode jsonNodeFromObjectMapper = objectMapper.readTree(jsonFromObjectMapper);
        Assert.assertEquals(JsonNodeFactory.instance.nullNode, jsonNode.get("value"));
        Assert.assertEquals(JsonNodeFactory.instance.nullNode, jsonNodeFromObjectMapper.get("value"));
        Assert.assertEquals(json, jsonFromObjectMapper);
    }

    @Test
    public void testJsonValueSerializer_EmptyList() throws IOException {
        JsonMapper mapper = new JsonMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Given
        MyClass myClass = new MyClass(new LinkedHashSet<>());
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, myClass.get_accessorMethod(), null, false);

        // When
        String json = mapper.writeValueAsString(myClass);
        String jsonFromObjectMapper = objectMapper.writeValueAsString(myClass);

        // Then
        JsonNode jsonNode = mapper.readTree(json);
        JsonNode jsonNodeFromObjectMapper = objectMapper.readTree(jsonFromObjectMapper);
        Assert.assertEquals(JsonNodeFactory.instance.arrayValue(), jsonNode.get("value"));
        Assert.assertEquals(JsonNodeFactory.instance.arrayValue(), jsonNodeFromObjectMapper.get("value"));
        Assert.assertEquals(json, jsonFromObjectMapper);
    }

    @Test
    public void testJsonValueSerializer_ArrayValue() throws IOException {
        JsonMapper mapper = new JsonMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Given
        MyClass myClass = new MyClass(new int[]{1, 2, 3});
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, myClass.get_accessorMethod(), null, false);

        // When
        String json = mapper.writeValueAsString(myClass);
        String jsonFromObjectMapper = objectMapper.writeValueAsString(myClass);

        // Then
        JsonNode jsonNode = mapper.readTree(json);
        JsonNode jsonNodeFromObjectMapper = objectMapper.readTree(jsonFromObjectMapper);
        Assert.assertEquals(JsonNodeFactory.instance.value(new int[]{1, 2, 3}), jsonNode.get("value"));
        Assert.assertEquals(JsonNodeFactory.instance.value(new int[]{1, 2, 3}), jsonNodeFromObjectMapper.get("value"));
        Assert.assertEquals(json, jsonFromObjectMapper);
    }

    @Test
    public void testJsonValueSerializer_Exception() throws IOException {
        JsonMapper mapper = new JsonMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Given
        MyClass myClass = new MyClass() {
            @Override
            public String _accessorMethod() throws Exception {
                throw new IOException("Test Exception");
            }
        };
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, myClass.getClass().getDeclaredMethod("_accessorMethod"), null, false);

        // When & Then
        try {
            String json = mapper.writeValueAsString(myClass);
            String jsonFromObjectMapper = objectMapper.writeValueAsString(myClass);
            Assert.fail();
        } catch (JsonProcessingException e) {
            // Expected
            Assert.assertEquals("Failed to serialize value of type [simple type, class com.fasterxml.jackson.databind.ser.std.JsonValueSerializer$MyClass$1]", e.getMessage());
        }
    }

    @Test
    public void testJsonValueSerializer_EnumUsage() throws IOException {
        JsonMapper mapper = new JsonMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Given
        MyEnumClass myEnumClass = MyEnumClass.RED;
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, myEnumClass.getClass().getDeclaredMethod("getValue"), null, false);

        // When
        String json = mapper.writeValueAsString(myEnumClass);
        String jsonFromObjectMapper = objectMapper.writeValueAsString(myEnumClass);

        // Then
        JsonNode jsonNode = mapper.readTree(json);
        JsonNode jsonNodeFromObjectMapper = objectMapper.readTree(jsonFromObjectMapper);
        Assert.assertEquals("RED", jsonNode.get("value"));
        Assert.assertEquals("RED", jsonNodeFromObjectMapper.get("value"));
        Assert.assertEquals(json, jsonFromObjectMapper);
    }

    @Test
    public void testJsonValueSerializer_NullAccessMethod() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
        Assert.assertNull(jsonValueSerializer);
    }

    @Test
    public void testJsonValueSerializer_NullProperty() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false, null);
        Assert.assertNull(jsonValueSerializer);
    }

    @Test
    public void testJsonValueSerializer_NullSerializer() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false, null, null);
        Assert.assertNull(jsonValueSerializer);
    }

    @Test
    public void testJsonValueSerializer_ForceTypeInformationFlag() {
        JsonMapper mapper = new JsonMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Given
        MyClass myClass = new MyClass(1);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, myClass.get_accessorMethod(), null, true);

        // When
        String json = mapper.writeValueAsString(myClass);
        String jsonFromObjectMapper = objectMapper.writeValueAsString(myClass);

        // Then
        JsonNode jsonNode = mapper.readTree(json);
        JsonNode jsonNodeFromObjectMapper = objectMapper.readTree(jsonFromObjectMapper);
        Assert.assertEquals("java.lang.Integer", jsonNode.getClass().getName());
        Assert.assertEquals("java.lang.Integer", jsonNodeFromObjectMapper.getClass().getName());
        Assert.assertEquals(json, jsonFromObjectMapper);
    }

    private static class MyClass {
        @JsonValue
        public String get_accessorMethod() {
            return "value";
        }

        @JsonValue
        public int _accessorMethod() {
            return 1;
        }

        public Set<Integer> getValue() {
            return new LinkedHashSet<>();
        }

        public int[] getValueArray() {
            return new int[]{1, 2, 3};
        }

        public static enum EnumValue {
            RED,
            GREEN,
            BLUE
        }

        public EnumValue getValueEnum() {
            return EnumValue.RED;
        }
    }

    private static class MyEnumClass {
        @JsonValue
        public static String getValue() {
            return "RED";
        }

        public static MyEnumClass RED = new MyEnumClass();
        public static MyEnumClass GREEN = new MyEnumClass();
        public static MyEnumClass BLUE = new MyEnumClass();
    }
}