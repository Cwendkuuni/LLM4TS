package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;
import com.fasterxml.jackson.test.util.JacksonTestUtils;

public class AsPropertyTypeDeserializerTest extends BaseTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", "IntegerType");
        node.put("myData", 123);

        JavaType javaType = mapper.getTypeFactory().constructType(int.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
        assertEquals(123, result);

        ObjectNode resultNode = mapper.writeValueAsObject(result);
        assertNotNull(resultNode.get("typeId"));
        assertEquals("IntegerType", resultNode.get("typeId").asText());
    }

    @Test
    public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", "StringType");
        node.put("myData", "Test");

        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, mapper.getTypeFactory().constructType(Integer.class), As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
        assertEquals("Test", result);
        assertNull(resultNode.get("typeId"));
    }

    @Test
    public void testDeserializeTypedFromObjectWithNullTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", null);
        node.put("myData", 123);

        JavaType javaType = mapper.getTypeFactory().constructType(int.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
        assertEquals(123, result);
        assertNull(resultNode.get("typeId"));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedFromObjectWithMissingProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("myData", 123);

        JavaType javaType = mapper.getTypeFactory().constructType(int.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        JavaType javaType = mapper.getTypeFactory().constructType(int.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithArrayWrapper() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", "IntegerType");
        node.put("myData", "[123]");

        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
        assertArrayEquals(new int[]{123}, (int[]) result);
        assertNull(resultNode.get("typeId"));
    }

    @Test
    public void testDeserializeTypedFromObjectWithArrayWrapperAndTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", "IntegerType");
        node.put("typeId", "IntegerTypeId");
        node.put("myData", "[123]");

        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
        assertArrayEquals(new int[]{123}, (int[]) result);
        assertNotNull(resultNode.get("typeId"));
        assertEquals("IntegerTypeId", resultNode.get("typeId").asText());
    }

    @Test
    public void testDeserializeTypedFromObjectWithNestedObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", "NestedObjectType");
        node.set("myData", mapper.readTree("{\"name\": \"Test\", \"number\": 123}"));

        JavaType javaType = mapper.getTypeFactory().constructType(NestedObject.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
        assertEquals(new NestedObject("Test", 123), result);
        assertNull(resultNode.get("typeId"));
    }

    @Test
    public void testDeserializeTypedFromObjectWithNestedObjectAndTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", "NestedObjectType");
        node.put("typeId", "NestedObjectTypeId");
        node.set("myData", mapper.readTree("{\"name\": \"Test\", \"number\": 123}"));

        JavaType javaType = mapper.getTypeFactory().constructType(NestedObject.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
        assertEquals(new NestedObject("Test", 123), result);
        assertNotNull(resultNode.get("typeId"));
        assertEquals("NestedObjectTypeId", resultNode.get("typeId").asText());
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", "IntegerType");
        node.put("[1, 2, 3]");

        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromArray(objectMapper.getFactory().createParser(node), context());
        assertArrayEquals(new int[]{1, 2, 3}, (int[]) result);
        assertNull(resultNode.get("typeId"));
    }

    @Test
    public void testDeserializeTypedFromArrayWithTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", "IntegerType");
        node.put("typeId", "IntegerTypeId");
        node.put("[1, 2, 3]");

        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromArray(objectMapper.getFactory().createParser(node), context());
        assertArrayEquals(new int[]{1, 2, 3}, (int[]) result);
        assertNotNull(resultNode.get("typeId"));
        assertEquals("IntegerTypeId", resultNode.get("typeId").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedFromScalar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typePropertyName", "IntegerType");
        node.put("myData", 123);

        JavaType javaType = mapper.getTypeFactory().constructType(int.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), "typePropertyName", true, null, As.PROPERTY);

        deserializer.deserializeTypedFromScalar(objectMapper.getFactory().createParser(node), context());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedFromAny() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("myData", "[1, 2, 3]");

        JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), null, true, null, As.PROPERTY);

        deserializer.deserializeTypedFromAny(objectMapper.getFactory().createParser(node), context());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("typeId", "IntegerType");
        node.put("myData", 123);

        JavaType javaType = mapper.getTypeFactory().constructType(int.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mock(TypeIdResolver.class), null, true, null, As.PROPERTY);

        Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(node), context());
        assertEquals(123, result);
        assertNull(resultNode.get("typePropertyName"));
        assertNotNull(resultNode.get("typeId"));
        assertEquals("IntegerType", resultNode.get("typeId").asText());
    }

    private static DeserializationContext context() {
        return new TestContext();
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public SerializerProvider serializers() {
            return JacksonTestUtils.createSerializerProvider();
        }

        @Override
        public TypeResolverBuilder typeResolverBuilder() {
            return JacksonTestUtils.createTypeResolverBuilder();
        }
    }

    private static int[] assertArrayEquals(int[] expected, int[] actual) {
        assertArrayEquals(expected, actual, "Arrays not equal");
        return actual;
    }

    private static class NestedObject {
        private final String name;
        private final int number;

        public NestedObject(String name, int number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        public static class NestedObjectDeserializer extends StdSerializer<NestedObject> {
            public NestedObjectDeserializer() {
                this(null);
            }

            public NestedObjectDeserializer(Class<NestedObject> t) {
                this(null);
            }

            public NestedObjectDeserializer(Class<NestedObject> t, JsonSerializer serializer) {
                super(t, serializer);
            }

            @Override
            public NestedObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String name = p.nextTextValue();
                int number = p.nextIntValue();
                return new NestedObject(name, number);
            }
        }

        static {
            ObjectMapper mapper = new ObjectMapper();
            try {
                ObjectWriter writer = mapper.writerFor(new TypeReference<NestedObject>() {});
                MAPPER.registerModule(new SimpleModule() {{
                    addSerializer(NestedObject.class, new NestedObjectDeserializer());
                    addDeserializerFactory(NestedObject.class, new Creator<Object>() {
                        @Override
                        public Object createInstance() {
                            return new NestedObject("", 0);
                        }
                    });
                }});
                MAPPER.registerSerializer(NestedObject.class, new NestedObjectDeserializer());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}