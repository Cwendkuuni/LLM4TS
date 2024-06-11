package com.example; // replace with your package name

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    

    @Test
    public void testJsonValueSerializer_BasicFunctionality() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization with no explicit serializer provided
        JsonValueSerializer jsonValueSerializer1 = mapper.findValueSerializer(String.class, null);
        assertNotNull(jsonValueSerializer1);

        // Test serialization with explicit serializer provided
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer jsonValueSerializer2 = new JsonValueSerializer(mapper.findMethodValueSerializer(JsonValueSerializer.class, "getValue", Object.class, String.class), stringSerializer, false);
        assertNotNull(jsonValueSerializer2);

        ObjectNode jsonNode1 = mapper.writeValueAsTree(new Object(), jsonValueSerializer1);
        ObjectNode jsonNode2 = mapper.writeValueAsTree(new Object(), jsonValueSerializer2);

        // Check that the serialized JSON is the same for both cases
        assertEquals(jsonNode1.toString(), jsonNode2.toString());
    }

    @Test
    public void testJsonValueSerializer_SerializationWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer jsonValueSerializer = mapper.findValueSerializer(String.class, null);

        ObjectNode jsonNode = mapper.writeValueAsTree(new Object(), jsonValueSerializer);

        JsonSerializer<Object> ser = mapper.findValueSerializer(String.class, null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        ObjectNode jsonNodeWithType = mapper.writeValueAsTree(new Object(), jsonValueSerializer, typeSerializer);

        // Check that the serialized JSON with type information is different from the one without
        assertNotSame(jsonNode.toString(), jsonNodeWithType.toString());
    }

    @Test
    public void testJsonValueSerializer_NullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer jsonValueSerializer = mapper.findValueSerializer(String.class, null);

        ObjectNode jsonNode = mapper.writeValueAsTree(new Object(), jsonValueSerializer);

        Object node = jsonNode.path("").asText();
        assertNull(node);
    }

    @Test
    public void testJsonValueSerializer_IOException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer jsonValueSerializer = mapper.findValueSerializer(String.class, null);

        ObjectNode jsonNode = mapper.writeValueAsTree(new Object(), jsonValueSerializer);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out, JsonMapping.DefaultSerializerProvider.instance());

        try {
            jsonValueSerializer.serialize(null, generator, mapper.getDefaultSerializerProvider());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        ObjectNode jsonNodeWithException = mapper.writeValueAsTree(new Object(), jsonValueSerializer);

        String errorMessage = jsonNodeWithException.path("").asText();
        String expectedErrorMessage = "com.fasterxml.jackson.core.JsonGenerationException: : readObject";
        assertTrue(errorMessage.contains(expectedErrorMessage));
    }

    @Test
    public void testJsonValueSerializer_InvocationTargetException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer jsonValueSerializer = mapper.findValueSerializer(String.class, null);

        ObjectNode jsonNode = mapper.writeValueAsTree(new Object(), jsonValueSerializer);

        JsonSerializer<Object> ser = mapper.findValueSerializer(String.class, null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        try {
            jsonValueSerializer.serializeWithType(null, mapper.getFactory().createGenerator(System.out, mapper.getDefaultSerializerProvider()), mapper.getDefaultSerializerProvider(), typeSerializer);
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            // Expected
        }

        ObjectNode jsonNodeWithException = mapper.writeValueAsTree(new Object(), jsonValueSerializer);

        String errorMessage = jsonNodeWithException.path("").asText();
        String expectedErrorMessage = "java.lang.NullPointerException";
        assertTrue(errorMessage.contains(expectedErrorMessage));
    }

    @Test
    public void testJsonValueSerializer_JsonMappingException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer jsonValueSerializer = mapper.findValueSerializer(String.class, null);

        ObjectNode jsonNode = mapper.writeValueAsTree(new Object(), jsonValueSerializer);

        try {
            JsonValueSerializer jsonValueSerializerWithException = new JsonValueSerializer(null, null, null, false);
            jsonValueSerializerWithException.serialize(null, null, mapper.getDefaultSerializerProvider());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        ObjectNode jsonNodeWithException = mapper.writeValueAsTree(new Object(), jsonValueSerializer);

        String errorMessage = jsonNodeWithException.path("").asText();
        String expectedErrorMessage = "No serializer found for class null and no properties discovered to serialize";
        assertTrue(errorMessage.contains(expectedErrorMessage));
    }

    @Test
    public void testJsonValueSerializer_EnumSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Create an Enum class with a custom JsonValue annotated method
        class MyEnum {
            public enum Type {
                VALUE1("Value1"), VALUE2("Value2"), VALUE3("Value3");

                private final String name;

                Type(String name) {
                    this.name = name;
                }

                @JsonValue
                public String getValue() {
                    return name;
                }
            }
        }

        // Test serialization with no explicit serializer provided
        JsonValueSerializer<MyEnum.Type> jsonValueSerializer1 = mapper.findValueSerializer(MyEnum.Type.class, null);
        assertNotNull(jsonValueSerializer1);

        // Test serialization with explicit serializer provided
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer<MyEnum.Type> jsonValueSerializer2 = new JsonValueSerializer<>(mapper.findMethodValueSerializer(JsonValueSerializer.class, "getValue", MyEnum.Type.class, String.class), stringSerializer, false);
        assertNotNull(jsonValueSerializer2);

        ObjectNode jsonNode1 = mapper.writeValueAsTree(MyEnum.Type.VALUE1, jsonValueSerializer1);
        ObjectNode jsonNode2 = mapper.writeValueAsTree(MyEnum.Type.VALUE1, jsonValueSerializer2);

        // Check that the serialized JSON is the same for both cases
        assertEquals(jsonNode1.toString(), jsonNode2.toString());
    }

    @Test
    public void testJsonValueSerializer_EnumSerializationWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Create an Enum class with a custom JsonValue annotated method
        class MyEnum {
            public enum Type {
                VALUE1("Value1"), VALUE2("Value2"), VALUE3("Value3");

                private final String name;

                Type(String name) {
                    this.name = name;
                }

                @JsonValue
                public String getValue() {
                    return name;
                }
            }
        }

        JsonValueSerializer<MyEnum.Type> jsonValueSerializer = mapper.findValueSerializer(MyEnum.Type.class, null);

        ObjectNode jsonNode = mapper.writeValueAsTree(MyEnum.Type.VALUE1, jsonValueSerializer);

        JsonSerializer<Object> ser = mapper.findValueSerializer(String.class, null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        ObjectNode jsonNodeWithType = mapper.writeValueAsTree(MyEnum.Type.VALUE1, jsonValueSerializer, typeSerializer);

        // Check that the serialized JSON with type information is different from the one without
        assertNotSame(jsonNode.toString(), jsonNodeWithType.toString());
    }

    @Test
    public void testJsonValueSerializer_EnumSerializationWithNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Create an Enum class with a custom JsonValue annotated method
        class MyEnum {
            public enum Type {
                VALUE1("Value1"), VALUE2("Value2"), VALUE3("Value3");

                private final String name;

                Type(String name) {
                    this.name = name;
                }

                @JsonValue
                public String getValue() {
                    return name;
                }
            }
        }

        JsonValueSerializer<MyEnum.Type> jsonValueSerializer = mapper.findValueSerializer(MyEnum.Type.class, null);

        ObjectNode jsonNode = mapper.writeValueAsTree(MyEnum.Type.VALUE1, jsonValueSerializer);

        ObjectNode jsonNodeWithNullValue = mapper.writeValueAsTree(null, jsonValueSerializer);

        Object node = jsonNodeWithNullValue.path("").asText();
        assertNull(node);
    }

    
}