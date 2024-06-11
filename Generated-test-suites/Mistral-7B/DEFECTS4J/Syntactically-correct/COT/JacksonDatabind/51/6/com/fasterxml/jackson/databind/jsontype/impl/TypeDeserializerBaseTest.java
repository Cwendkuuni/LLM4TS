package com.example.tests.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstructor() throws Exception {
        Class<?> baseType = Object.class;
        TypeIdResolver idResolver = null;
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());
        assertEquals(baseType, typeDeserializerBase.getBaseType());
        assertNull(typeDeserializerBase.getTypeIdResolver());
        assertEquals("", typeDeserializerBase.getPropertyName());
        assertEquals(typeIdVisible, typeDeserializerBase.isTypeIdVisible());
        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
        assertNull(typeDeserializerBase._property);
        assertNull(typeDeserializerBase._deserializers);
        assertNull(typeDeserializerBase._defaultImplDeserializer);

        TypeDeserializerBase typeDeserializerBaseWithProperty = new TypeDeserializerBase(typeDeserializerBase, new BeanProperty(null, "testProperty"));

        assertEquals(TypeDeserializerBase.class, typeDeserialserializerBaseWithProperty.getClass());
        assertEquals(baseType, typeDeserializerBaseWithProperty.getBaseType());
        assertNotNull(typeDeserializerBaseWithProperty.getTypeIdResolver());
        assertNotNull(typeDeserializerBaseWithProperty.getPropertyName());
        assertEquals(typeIdVisible, typeDeserializerBaseWithProperty.isTypeIdVisible());
        assertEquals(defaultImpl, typeDeserializerBaseWithProperty.getDefaultImpl());
        assertEquals("testProperty", typeDeserializerBaseWithProperty.getPropertyName());
        assertNotNull(typeDeserializerBaseWithProperty._property);
        assertNotNull(typeDeserializerBaseWithProperty._deserializers);
        assertNull(typeDeserializerBaseWithProperty._defaultImplDeserializer);
    }

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        BeanProperty prop = new BeanProperty(null, "testProperty");
        TypeDeserializerBase typeDeserializerForProperty = typeDeserializerBase.forProperty(prop);

        assertNotSame(typeDeserializerBase, typeDeserializerForProperty);
        assertEquals(baseType, typeDeserializerForProperty.getBaseType());
        assertNotNull(typeDeserializerForProperty.getTypeIdResolver());
        assertEquals("testProperty", typeDeserializerForProperty.getPropertyName());
        assertEquals(typeIdVisible, typeDeserializerForProperty.isTypeIdVisible());
        assertEquals(defaultImpl, typeDeserializerForProperty.getDefaultImpl());
        assertEquals("testProperty", typeDeserializerForProperty.getPropertyName());
        assertNotNull(typeDeserializerForProperty._property);
        assertNotNull(typeDeserializerForProperty._deserializers);
        assertNull(typeDeserializerForProperty._defaultImplDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertNull(typeDeserializerBase.getTypeInclusion());

        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };

        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());

        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.WRAP_ROOT_VALUE;
            }
        };

        assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);

        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String typeId1 = "com.example.TestClass1";
        JsonDeserializer<Object> deserializer1 = new TestJsonDeserializer();

        String typeId2 = "com.example.TestClass2";
        JsonDeserializer<Object> deserializer2 = new TestJsonDeserializer();

        typeDeserializerBase._deserializers.put(typeId1, deserializer1);
        typeDeserializerBase._deserializers.put(typeId2, deserializer2);

        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new TestDeserializationContext();

        Object result1 = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, typeId1);
        assertEquals(deserializer1.getClass(), result1.getClass());

        Object result2 = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, typeId2);
        assertEquals(deserializer2.getClass(), result2.getClass());

        // Test default implementation
        JsonDeserializer<Object> defaultImplDeserializer = new TestJsonDeserializer();
        typeDeserializerBase._defaultImplDeserializer = defaultImplDeserializer;

        Object resultDefault = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, null);
        assertEquals(defaultImplDeserializer.getClass(), resultDefault.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(TestDefaultImpl.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext context = new TestDeserializationContext();

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(defaultImplDeserializer.getClass(), result.getClass());
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullIdResolver() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(TestDefaultImpl.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext context = new TestDeserializationContext();
        String typeId = null;

        typeDeserializerBase._handleUnknownTypeId(context, typeId, null, baseType);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullBaseType() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(TestDefaultImpl.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext context = new TestDeserializationContext();
        String typeId = "com.example.TestClass";

        typeDeserializerBase._handleUnknownTypeId(context, typeId, idResolver, baseType);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullTypeId() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(TestDefaultImpl.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext context = new TestDeserializationContext();

        typeDeserializerBase._handleUnknownTypeId(context, null, idResolver, baseType);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdUnknownTypeId() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(TestDefaultImpl.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext context = new TestDeserializationContext();
        String typeId = "unknown.type.id";

        typeDeserializerBase._handleUnknownTypeId(context, typeId, idResolver, baseType);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdTypeIdWithInvalidType() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(TestDefaultImpl.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext context = new TestDeserializationContext();
        String typeId = "java.util.List<java.lang.String>";

        typeDeserializerBase._handleUnknownTypeId(context, typeId, idResolver, baseType);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(TestDefaultImpl.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String json = "{\"typeId\":\"com.example.TestClass1\", \"value\":42}";
        JsonParser jsonParser = new JsonParser(new StringReader(json));
        DeserializationContext context = new TestDeserializationContext();

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, "com.example.TestClass1");

        assertEquals(TestClass1.class, result.getClass());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdInvalidTypeId() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(TestDefaultImpl.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String json = "{\"typeId\":\"invalid.type.id\", \"value\":42}";
        JsonParser jsonParser = new JsonParser(new StringReader(json));
        DeserializationContext context = new TestDeserializationContext();

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, "invalid.type.id");
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws Exception {
        JavaType baseType = JavaType.constructType(Object.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = null;
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(TestDefaultImpl.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String json = "{\"value\":42}";
        JsonParser jsonParser = new JsonParser(new StringReader(json));
        DeserializationContext context = new TestDeserializationContext();

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, null);

        assertEquals(defaultImpl.getRawClass(), result.getClass());
    }

    static class TestJsonDeserializer extends StdDeserializer<Object> {
        public TestJsonDeserializer() {
            this(null);
        }

        public TestJsonDeserializer(Class<Object> vc) {
            this(vc, null);
        }

        public TestJsonDeserializer(Class<Object> vc, Constructor<?> c) {
            super(vc, c);
        }

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Object deserialized = null;
            if (jp.isExpectedStartArrayToken()) {
                JsonToken expectedToken = JsonToken.START_ARRAY;
                deserialized = ctxt.readValueAs(ArrayList.class, expectedToken);
            } else {
                JsonToken expectedToken = JsonToken.VALUE_STRING;
                deserialized = ctxt.readValueAs(String.class, expectedToken);
            }
            return deserialized;
        }
    }

    static class TestDefaultImpl {
    }

    static class BaseTypeIdResolver extends TypeIdResolverBase {
        @Override
        public String getDescForKnownTypeIds() {
            return "known type ids are not statically known";
        }
    }

    static class TestDeserializationContext extends DeserializationContext {
        public TestDeserializationContext() {
            super(new AnnotationIntrospector(), new StandardDeserializationProbe(), new DefaultContextInitializer());
        }
    }
}