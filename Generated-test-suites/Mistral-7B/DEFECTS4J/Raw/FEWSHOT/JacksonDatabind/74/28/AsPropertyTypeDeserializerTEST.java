package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject_JSONObjectWithPropertyTypeId() throws Exception {
        String json = "{\"type\":\"MyClass\", \"data\":\"some data\"}";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                new TypeIdResolver() {
                    @Override
                    public IdTypeId resolveId(MapperConfig config, TokenIdResolver resolver) {
                        return new IdTypeId(MyClass.class.getName(), null);
                    }
                }, "type", true, null, As.PROPERTY);

        Object result = mapper.readValue(json, deserializer.getType(MyClass.class));
        assertEquals(new MyClass("some data"), result);
    }

    @Test
    public void testDeserializeTypedFromObject_JSONObjectWithoutPropertyTypeId() throws Exception {
        String json = "{\"data\":\"some data\"}";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                new TypeIdResolver() {
                    @Override
                    public IdTypeId resolveId(MapperConfig config, TokenIdResolver resolver) {
                        return null;
                    }
                }, "type", true, null, As.PROPERTY);

        JsonParseException exception = assertThrows(JsonParseException.class, () -> mapper.readValue(json, deserializer.getType(MyClass.class)));
        assertEquals("Missing property 'type' that is to contain type id (for class MyClass)", exception.getMessage());
    }

    @Test
    public void testDeserializeTypedFromObject_JSONObjectWithInvalidPropertyTypeId() throws Exception {
        String json = "{\"type\":\"InvalidType\", \"data\":\"some data\"}";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                new TypeIdResolver() {
                    @Override
                    public IdTypeId resolveId(MapperConfig config, TokenIdResolver resolver) {
                        return new IdTypeId("InvalidType", null);
                    }
                }, "type", true, null, As.PROPERTY);

        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> mapper.readValue(json, deserializer.getType(MyClass.class)));
        assertEquals("Unknown class: 'InvalidType'", exception.getMessage());
    }

    @Test
    public void testDeserializeTypedFromObject_JSONObjectWithMultiplePropertyTypeIds() throws Exception {
        String json = "{\"type1\":\"MyClass1\", \"type2\":\"MyClass2\", \"data\":\"some data\"}";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                new TypeIdResolver() {
                    @Override
                    public IdTypeId resolveId(MapperConfig config, TokenIdResolver resolver) {
                        String propertyName = resolver.getCurrentName();
                        if ("type1".equals(propertyName)) {
                            return new IdTypeId("MyClass1", null);
                        } else if ("type2".equals(propertyName)) {
                            return new IdTypeId("MyClass2", null);
                        }
                        return null;
                    }
                }, "type1", true, null, As.PROPERTY);

        Object result = mapper.readValue(json, deserializer.getType(MyClass.class));
        fail("Expected exception thrown but it wasn't");
    }

    @Test
    public void testDeserializeTypedFromObject_JSONArray() throws Exception {
        String json = "[\"MyClass\", \"some data\"]";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                new TypeIdResolver() {
                    @Override
                    public IdTypeId resolveId(MapperConfig config, TokenIdResolver resolver) {
                        return new IdTypeId("MyClass", null);
                    }
                }, "type", true, null, As.PROPERTY);

        JsonParseException exception = assertThrows(JsonParseException.class, () -> mapper.readValue(json, deserializer.getType(MyClass.class)));
        assertEquals("Expected a String but was not started: [\"MyClass\", \"some data\"]", exception.getMessage());
    }

    @Test
    public void testDeserializeTypedFromObject_JSONScalar() throws Exception {
        String json = "\"some data\"";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                new TypeIdResolver() {
                    @Override
                    public IdTypeId resolveId(MapperConfig config, TokenIdResolver resolver) {
                        return null;
                    }
                }, "type", true, null, As.PROPERTY);

        JsonParseException exception = assertThrows(JsonParseException.class, () -> mapper.readValue(json, deserializer.getType(MyClass.class)));
        assertEquals("Missing property 'type' that is to contain type id (for class MyClass)", exception.getMessage());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_JSONArray() throws Exception {
        String json = "[\"MyClass\", \"some data\"]";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                null, null, null, null, As.PROPERTY);

        Object result = mapper.readerForListOf(MyClass.class).readValue(json);
        assertEquals(1, result.size());
        assertEquals(new MyClass("some data"), result.get(0));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_JSONObjectWithPropertyTypeId() throws Exception {
        String json = "{\"type\":\"MyClass\", \"data\":\"some data\"}";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                null, null, null, null, As.PROPERTY);

        Object result = mapper.readerForMapOf(String.class, MyClass.class).readValue(json);
        assertEquals(new MyClass("some data"), result.get("type"));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_JSONObjectWithoutPropertyTypeId() throws Exception {
        String json = "{\"data\":\"some data\"}";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                null, null, null, null, As.PROPERTY);

        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> mapper.readerForMapOf(String.class, MyClass.class).readValue(json));
        assertEquals("Missing property 'type' that is to contain type id (for class MyClass)", exception.getMessage());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_JSONObjectWithMultiplePropertyTypeIds() throws Exception {
        String json = "{\"type1\":\"MyClass1\", \"type2\":\"MyClass2\", \"data\":\"some data\"}";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                null, null, null, null, As.PROPERTY);

        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> mapper.readerForMapOf(String.class, MyClass.class).readValue(json));
        assertEquals("Missing property 'type' that is to contain type id (for class MyClass)", exception.getMessage());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_JSONObjectWithInvalidPropertyTypeIds() throws Exception {
        String json = "{\"type\":\"InvalidType\", \"data\":\"some data\"}";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                null, null, null, null, As.PROPERTY);

        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> mapper.readerForMapOf(String.class, MyClass.class).readValue(json));
        assertEquals("Unknown class: 'InvalidType'", exception.getMessage());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_JSONObjectWithCustomValueDeserializer() throws Exception {
        String json = "{\"type\":\"MyClass\", \"data\":{\"name\":\"John Doe\"}}";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MyClass.class, new StubMyClassDeserializer());
        mapper.registerModule(module);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                null, null, null, null, As.PROPERTY);

        Object result = mapper.readerForMapOf(String.class, MyClass.class).readValue(json);
        assertEquals(new MyClass(new StubJohnDoe()), result.get("type"));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_JSONObjectWithCustomValueDeserializer_NoPropertyTypeId() throws Exception {
        String json = "{\"data\":{\"name\":\"John Doe\"}}";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MyClass.class, new StubMyClassDeserializer());
        mapper.registerModule(module);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                null, null, null, null, As.PROPERTY);

        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> mapper.readerForMapOf(String.class, MyClass.class).readValue(json));
        assertEquals("Missing property 'type' that is to contain type id (for class MyClass)", exception.getMessage());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_JSONArray() throws Exception {
        String json = "[\"MyClass\", \"some data\"]";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                null, null, null, null, As.PROPERTY);

        JsonParseException exception = assertThrows(JsonParseException.class, () -> mapper.readerForListOf(MyClass.class).readValue(json));
        assertEquals("Expected a String but was not started: [\"MyClass\", \"some data\"]", exception.getMessage());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_JSONScalar() throws Exception {
        String json = "\"some data\"";
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(MyClass.class),
                null, null, null, null, As.PROPERTY);

        JsonParseException exception = assertThrows(JsonParseException.class, () -> mapper.readerForMapOf(String.class, MyClass.class).readValue(json));
        assertEquals("Missing property 'type' that is to contain type id (for class MyClass)", exception.getMessage());
    }

    static class MyClass {
        private final String data;

        public MyClass(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        @Override
        public String toString() {
            return "MyClass{" +
                    "data='" + data + '\'' +
                    '}';
        }
    }

    static class StubJohnDoe {
        private final String name;

        public StubJohnDoe() {
            this.name = "John Doe";
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "StubJohnDoe{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    static class StubMyClassDeserializer extends JsonDeserializer<MyClass> {
        @Override
        public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectMapper mapper = p.getCodec().getCarrierCache().findAndClearCurrentContext();
            Object result = mapper.readValue(p, StubJohnDoe.class);
            return new MyClass(ObjectMapperUtil.convertValue(result, String.class));
        }
    }
}