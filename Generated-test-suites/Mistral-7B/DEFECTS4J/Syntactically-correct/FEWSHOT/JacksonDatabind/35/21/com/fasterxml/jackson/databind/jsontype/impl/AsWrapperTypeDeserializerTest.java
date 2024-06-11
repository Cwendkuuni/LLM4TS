package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsWrapperTypeDeserializerTest extends BaseTest {

    private static final String BASE_TYPE_NAME = "com.example.MyBaseClass";
    private static final String SUB_TYPE_NAME_1 = "com.example.MySubType1";
    private static final String SUB_TYPE_NAME_2 = "com.example.MySubType2";

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.idFromValueAndType(any(), any())).thenReturn("typeId1");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "{\"type\":\"typeId1\",\"value\":{\"name\":\"John\",\"age\":30}}";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(jp, context());
        assertEquals(MySubType1.class, result.getClass());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.idFromValueAndType(any(), any())).thenReturn("typeId1");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "[{\"type\":\"typeId1\",\"value\":{\"name\":\"John\",\"age\":30}}]";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromArray(jp, context());
        assertEquals(MySubType1[].class, result.getClass());
        assertArrayEquals(new MySubType1[]{new MySubType1()}, result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.idFromValueAndType(any(), any())).thenReturn("typeId1");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "{\"type\":\"typeId1\",\"value\":42}";
        JsonParser jp = mapper.getFactory().createParser(json);
        assertNull(deserializer.deserializeTypedFromScalar(jp, context()));
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.idFromValueAndType(any(), any())).thenReturn("typeId1");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "{\"type\":\"typeId1\",\"value\":{\"name\":\"John\",\"age\":30}}";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromAny(jp, context());
        assertEquals(MySubType1.class, result.getClass());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.idFromValueAndType(any(), any())).thenReturn(null);
        when(idResolver.interpretTypeId(eq("typeId1"), any())).thenReturn(MySubType1.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "{\"@type\":\"typeId1\",\"value\":{\"name\":\"John\",\"age\":30}}";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(jp, context());
        assertEquals(MySubType1.class, result.getClass());
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "[{\"type\":\"typeId1\",\"value\":{\"name\":\"John\",\"age\":30}}}"; // missing END_OBJECT
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer.deserializeTypedFromArray(jp, context());
    }

    @Test(expected = IOException.class)
    public void testDeserializeMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "{\"value\":{\"name\":\"John\",\"age\":30}}"; // missing type id
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer.deserializeTypedFromObject(jp, context());
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "{\"type\":\"invalidTypeId\",\"value\":{\"name\":\"John\",\"age\":30}}";
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer.deserializeTypedFromObject(jp, context());
    }

    @Test
    public void testDeserializeWithTypeIdVisible() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "{\"type\":\"typeId1\",\"_type\":\"typeId2\",\"value\":{\"name\":\"John\",\"age\":30}}";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(jp, context());
        assertEquals(MySubType1.class, result.getClass());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithTypeIdInvisible() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", false, MySubType1.class);

        String json = "{\"type\":\"typeId1\",\"value\":{\"name\":\"John\",\"age\":30}}";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(jp, context());
        assertEquals(MySubType1.class, result.getClass());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeDefaultImpl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MyBaseClass.class);

        String json = "{\"type\":\"typeId1\",\"value\":{\"name\":\"John\",\"age\":30}}";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(jp, context());
        assertEquals(MyBaseClass.class, result.getClass());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithSubType1() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.idFromValueAndType(any(), any())).thenReturn("typeId1");
        when(idResolver.interpretTypeId(eq("typeId1"), any())).thenReturn(MySubType1.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "{\"type\":\"typeId1\",\"value\":{\"name\":\"John\",\"age\":30}}";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(jp, context());
        assertEquals(MySubType1.class, result.getClass());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithSubType2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.idFromValueAndType(any(), any())).thenReturn("typeId2");
        when(idResolver.interpretTypeId(eq("typeId2"), any())).thenReturn(MySubType2.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(BASE_TYPE_NAME), idResolver, "_type", true, MySubType1.class);

        String json = "{\"type\":\"typeId2\",\"value\":{\"name\":\"John\",\"age\":30}}";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(jp, context());
        assertEquals(MySubType2.class, result.getClass());
        assertNotNull(result);
    }

    private static Object context() {
        return new TestContext(new JavaType(new TypeReference<MyBaseClass>() {}), new TypeIdResolver() {
            @Override
            public IdTypeId idFromValueAndType(Object value, JavaType valueType) {
                return null;
            }

            @Override
            public JavaType idFromValueAndType(Object value, JavaType valueType, JavaType existingId) {
                return null;
            }

            @Override
            public java.lang.Class<?> interpretTypeId(String id, DeserializationConfig config, BeanDescription beanDesc) throws IOException {
                if (id.equals("typeId1")) {
                    return MySubType1.class;
                } else if (id.equals("typeId2")) {
                    return MySubType2.class;
                }
                return null;
            }
        });
    }

    private static class TestContext extends JsonDeserializationContext {
        private final JavaType _type;
        private final TypeIdResolver _idResolver;

        public TestContext(JavaType type, TypeIdResolver idResolver) {
            super(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            _type = type;
            _idResolver = idResolver;
        }

        @Override
        public JavaType getTypeDescriptor() {
            return _type;
        }

        @Override
        public TypeIdResolver getValueTypeResolver() {
            return _idResolver;
        }
    }

    private static class MyBaseClass {}

    private static class MySubType1 {}

    private static class MySubType2 {}
}