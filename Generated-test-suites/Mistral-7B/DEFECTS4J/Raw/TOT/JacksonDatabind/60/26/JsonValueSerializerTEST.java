package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonValueSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testValueSerializerWithSimpleType() throws JsonProcessingException {
        // Test serialization for a simple type
        @JsonSerialize(using = JsonValueSerializer.class)
        static class TestClass {
            private int intValue;

            public int getIntValue() {
                return intValue;
            }
        }

        TestClass testClass = new TestClass();
        testClass.getIntValue(); // invoke the getter

        JsonSerializer<TestClass> serializer = mapper.findSerializer(TestClass.class, null);
        ObjectNode node = mapper.readValue(serializer.serializeWithType(testClass, null, null, null), ObjectNode.class);
        assertEquals(Integer.TYPE.getName(), node.get("$type").asText());
        assertEquals(Integer.valueOf(0), node.get(0));
    }

    @Test
    public void testValueSerializerWithEnumType() throws JsonProcessingException {
        // Test serialization for an Enum type
        @JsonSerialize(using = JsonValueSerializer.class)
        enum TestEnum {
            VALUE_ONE, VALUE_TWO, VALUE_THREE
        }

        TestEnum testEnum = TestEnum.VALUE_ONE;

        JsonSerializer<TestEnum> serializer = mapper.findSerializer(TestEnum.class, null);
        ObjectNode node = mapper.readValue(serializer.serializeWithType(testEnum, null, null, null), ObjectNode.class);
        assertEquals(TestEnum.class.getName(), node.get("$type").asText());
        assertEquals(TestEnum.VALUE_ONE.name(), node.get(0).asText());
    }

    @Test
    public void testValueSerializerWithCustomTypeSerializer() throws JsonProcessingException {
        // Test serialization with a custom TypeSerializer
        @JsonSerialize(using = JsonValueSerializer.class)
        static class TestClassWithCustomSerializer {
            private String customString;

            public String getCustomString() {
                return customString;
            }

            public static class CustomSerializer extends JsonSerializer<TestClassWithCustomSerializer> {
                @Override
                public void serializeWithType(TestClassWithCustomSerializer value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
                    typeSer.writeTypePrefixForScalar(value, gen);
                    gen.writeString(value.getCustomString());
                    typeSer.writeTypeSuffixForScalar(value, gen);
                }
            }
        }

        TestClassWithCustomSerializer testClass = new TestClassWithCustomSerializer();
        testClass.setCustomString("test");

        JsonSerializer<TestClassWithCustomSerializer> serializer = mapper.findValueSerializer(TestClassWithCustomSerializer.class, null);
        ObjectNode node = mapper.readValue(serializer.serializeWithType(testClass, null, null, null), ObjectNode.class);
        assertEquals("com.fasterxml.jackson.databind.ser.std.TestClassWithCustomSerializer", node.get("$type").asText());
        assertEquals("test", node.get(0));
    }

    @Test
    public void testValueSerializerWithSchema() throws JsonProcessingException {
        // Test serialization with JSON Schema
        @JsonSerialize(using = JsonValueSerializer.class)
        static class TestClassWithSchema {
            private int intValue;

            public int getIntValue() {
                return intValue;
            }
        }

        TestClassWithSchema testClass = new TestClassWithSchema();
        testClass.getIntValue(); // invoke the getter

        JsonSerializer<TestClassWithSchema> serializer = mapper.findSerializer(TestClassWithSchema.class, null);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), null);
        ObjectNode schemaNode = (ObjectNode) schema;
        assertEquals("integer", schemaNode.get("type").asText());
        assertNotNull(schemaNode.get("properties"));
    }

    @Test
    public void testValueSerializerWithNullValue() throws JsonProcessingException {
        // Test serialization with null value
        @JsonSerialize(using = JsonValueSerializer.class)
        static class TestClass {
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        TestClass testClass = new TestClass();
        testClass.setValue(null);

        JsonSerializer<TestClass> serializer = mapper.findSerializer(TestClass.class, null);
        ObjectNode node = mapper.readValue(serializer.serializeWithType(testClass, null, null, null), ObjectNode.class);
        assertEquals(null, node.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testValueSerializerWithException() throws JsonProcessingException {
        // Test serialization with exception
        @JsonSerialize(using = JsonValueSerializer.class)
        static class TestClass {
            private String value;

            public String getValue() {
                throw new RuntimeException("Test exception");
            }
        }

        TestClass testClass = new TestClass();

        JsonSerializer<TestClass> serializer = mapper.findSerializer(TestClass.class, null);
        ObjectNode node = mapper.readValue(serializer.serializeWithType(testClass, null, null, null), ObjectNode.class);
        // This test should fail with JsonMappingException
    }
}