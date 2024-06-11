package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.SettableBeanProperty.BeanPropertySetup.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.BaseApiModel;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

public class AsWrapperTypeDeserializerTest extends BaseMapTest {

    private static final String CLASS_NAME = AsWrapperTypeDeserializer.class.getName();

    @Test
    public void testAsWrapperTypeDeserializerCreation() {
        JavaType javaType = mapType(BaseApiModel.class.getName(), Foo.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, context.getIdResolver(), "_type", true, Foo.class);
        assertNotNull(deserializer);
    }

    @Test
    public void testDeserializeTypedFromObjectWithValidTypeId() throws Exception {
        String json = "{\"_type\":\"com.example.Foo\"}";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        Object result = objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class);
        assertEquals(new Foo(), result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithInvalidTypeId() throws Exception {
        String json = "{\"_type\":\"com.example.Bar\"}";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingTypeId() throws Exception {
        String json = "{}";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));
    }

    @Test
    public void testDeserializeTypedFromObjectWithNullTypeId() throws Exception {
        String json = "{\"_type\":null}";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyTypeId() throws Exception {
        String json = "{\"_type\":\"\"}";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyJson() throws Exception {
        String json = "{}";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingEndObject() throws Exception {
        String json = "{\"_type\":\"com.example.Foo\"";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));
    }

    @Test
    public void testDeserializeTypedFromObjectWithExtraFields() throws Exception {
        String json = "{\"_type\":\"com.example.Foo\", \"extra\":\"field\"}";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));
    }

    @Test
    public void testDeserializeTypedFromObjectWithTypeIdPropertyName() throws Exception {
        JavaType javaType = mapType(BaseApiModel.class.getName(), Foo.class, "_typeProperty");
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, context.getIdResolver(), "_typeProperty", true, Foo.class);
        String json = "{\"_typeProperty\":\"com.example.Foo\"}";
        Object result = deserializer.deserializeTypedFromObject(jsonParser(), context);
        assertEquals(new Foo(), result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        String json = "[{\"_type\":\"com.example.Foo\"}]";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo[].class);
        Foo[] result = objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo[].class);
        assertEquals(new Foo[1], result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        String json = "\"foo\"";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        String json = "null";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));

        json = "42";
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));

        json = "true";
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));

        json = "[]";
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));

        json = "{\"_type\":\"com.example.Bar\"}";
        assertNull(objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class));

        json = "{\"_type\":\"com.example.Foo\", \"foo\":\"bar\"}";
        Foo foo = new Foo();
        foo.setFoo("bar");
        Object result = objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class);
        assertEquals(foo, result);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        TypeIdResolver idResolver = context.getValueInputFactory().getInputFactory(JavaType.construct(Object.class))
                .getTypeIdResolver();
        ObjectReader fooObjectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        ObjectReader barObjectReader = jsonFactory.getObjectReaderForValueType(Bar.class);

        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapType(BaseApiModel.class.getName(), Foo.class), idResolver, "_type", true, Foo.class);
        JsonDeserializer<Object> fooDeserializer = deserializer.findDeserializer(context, "com.example.Foo");
        assertEquals(fooObjectReader, fooDeserializer);

        deserializer = new AsWrapperTypeDeserializer(mapType(BaseApiModel.class.getName(), Bar.class), idResolver, "_type", true, Bar.class);
        JsonDeserializer<Object> barDeserializer = deserializer.findDeserializer(context, "com.example.Foo");
        assertNull(barDeserializer);

        deserializer = new AsWrapperTypeDeserializer(mapType(BaseApiModel.class.getName(), Foo.class), idResolver, "_type", true, Foo.class);
        JsonDeserializer<Object> fooDeserializer2 = deserializer.findDeserializer(context, "com.example.Bar");
        assertNull(fooDeserializer2);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"_class\":\"com.example.Foo\"}";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        Object result = objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class);
        assertEquals(new Foo(), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdWrongType() throws Exception {
        String json = "{\"_class\":\"com.example.Bar\"}";
        ObjectReader objectReader = jsonFactory.getObjectReaderForValueType(Foo.class);
        objectReader.readValue(new ByteArrayInputStream(json.getBytes()), Foo.class);
    }

    private static JavaType mapType(String baseType, Class<?> valueType, String typePropertyName) {
        return mapType(JsonTypeReference.constructUnresolved(baseType), valueType, typePropertyName);
    }

    private static JavaType mapType(TypeReference baseType, Class<?> valueType, String typePropertyName) {
        return mapType(baseType, valueType, typePropertyName, null);
    }

    private static JavaType mapType(TypeReference baseType, Class<?> valueType, String typePropertyName, String typeIdPropertyName) {
        TypeFactory typeFactory = context.getTypeFactory();
        return typeFactory.constructMapType(BaseApiModel.class, valueType, typePropertyName, baseType, typeIdPropertyName);
    }

    static class Foo extends BaseApiModel {
        private String foo;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        @Override
        public String toString() {
            return getClass().getName() + " [foo=" + foo + "]";
        }
    }

    static class Bar extends BaseApiModel {
        private int bar;

        public int getBar() {
            return bar;
        }

        public void setBar(int bar) {
            this.bar = bar;
        }

        @Override
        public String toString() {
            return getClass().getName() + " [bar=" + bar + "]";
        }
    }
}