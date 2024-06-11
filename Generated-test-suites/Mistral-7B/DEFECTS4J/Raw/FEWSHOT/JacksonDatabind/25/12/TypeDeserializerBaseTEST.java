package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class TypeDeserializerBaseTest extends TestBase {

    private static final TypeDeserializerBase TEST_SUBCLASS = new TestTypeDeserializerBase();
    private static final JavaType BASE_TYPE = TestBase.createSimpleType(String.class);
    private static final TypeIdResolver ID_RESOLVER = mock(TypeIdResolver.class);
    private static final String TYPE_PROPERTY_NAME = "typePropertyName";
    private static final boolean TYPE_ID_VISIBLE = true;

    @SuppressWarnings("unchecked")
    private static final Map<String, JsonDeserializer<Object>> DESERIALIZERS = new ConcurrentHashMap<String, JsonDeserializer<Object>>() {
        {
            put("StringType", new StringDeserializer());
            put("NumberType", new NumberDeserializer());
        }
    };

    private static class TestTypeDeserializerBase extends TypeDeserializerBase {

        public TestTypeDeserializerBase() {
            super(BASE_TYPE, ID_RESOLVER, TYPE_PROPERTY_NAME, TYPE_ID_VISIBLE, String.class);
        }

        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializerForProperty(prop);
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            return DESERIALIZERS.get(typeId);
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return NullifyingDeserializer.instance;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                throws IOException {
            return null;
        }

        @Override
        protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType)
                throws IOException {
            throw new RuntimeException("Should not be called");
        }

    }

    private static class TestTypeDeserializerForProperty extends TypeDeserializerBase {

        public TestTypeDeserializerForProperty(BeanProperty prop) {
            super(TEST_SUBCLASS, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXISTING_PROPERTY;
        }

    }

    private static class StringDeserializer extends StdDeserializer<String> {

        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(String.class, vc);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }

    }

    private static class NumberDeserializer extends StdDeserializer<Number> {

        public NumberDeserializer() {
            this(null);
        }

        public NumberDeserializer(Class<?> vc) {
            super(Number.class, vc);
        }

        @Override
        public Number deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsNumber();
        }

    }

    @Test
    public void testTypeDeserializerBase() throws IOException {
        when(ID_RESOLVER.typeFromId(any(DeserializationContext.class), any(String.class))).thenReturn(null);

        TypeDeserializer deserializer = TEST_SUBCLASS;
        DeserializationContext ctxt = createContext();

        assertSame(BASE_TYPE, deserializer.getType(ctxt));
        assertEquals(TYPE_PROPERTY_NAME, deserializer.getPropertyName());
        assertEquals(ID_RESOLVER, deserializer.getTypeIdResolver());
        assertEquals(TYPE_ID_VISIBLE, deserializer.isTypeIdVisible());
        assertEquals(JsonTypeInfo.As.UNKNOWN, deserializer.getTypeInclusion());

        assertNull(deserializer.forProperty(null));

        assertEquals("TestTypeDeserializerBase", deserializer.toString());
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty prop = mock(BeanProperty.class);
        when(prop.getName()).thenReturn("testProperty");

        TypeDeserializer deserializer = TEST_SUBCLASS;
        DeserializationContext ctxt = createContext();

        TypeDeserializer forProperty = deserializer.forProperty(prop);
        assertNotNull(forProperty);
        assertSame(TEST_SUBCLASS.getClass(), forProperty.getClass());
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, forProperty.getTypeInclusion());

        // Test that property is correctly set
        assertEquals("testProperty", forProperty.getPropertyName());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(ID_RESOLVER.typeFromId(any(DeserializationContext.class), any(String.class))).thenReturn(createSimpleType(String.class));

        TypeDeserializer deserializer = TEST_SUBCLASS;
        DeserializationContext ctxt = createContext();

        JsonDeserializer<Object> deserializer1 = new StringDeserializer();
        when(ID_RESOLVER.deserializerForId(any(DeserializationContext.class), any(String.class))).thenReturn(deserializer1);

        JsonDeserializer<Object> deserializer2 = new NumberDeserializer();
        when(ID_RESOLVER.deserializerForId(any(DeserializationContext.class), any(String.class))).thenReturn(deserializer2, deserializer1);

        JsonDeserializer<Object> foundDeserializer = deserializer._findDeserializer(ctxt, "StringType");
        assertSame(deserializer1, foundDeserializer);
        foundDeserializer = deserializer._findDeserializer(ctxt, "NumberType");
        assertSame(deserializer2, foundDeserializer);
        foundDeserializer = deserializer._findDeserializer(ctxt, "UnknownType");
        assertNull(foundDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(ID_RESOLVER.typeFromId(any(DeserializationContext.class), any(String.class))).thenReturn(null);

        TypeDeserializer deserializer = TEST_SUBCLASS;
        DeserializationContext ctxt = createContext();

        JsonDeserializer<Object> deserializer1 = new StringDeserializer();
        when(ctxt.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class))).thenReturn(deserializer1);

        JsonDeserializer<Object> foundDeserializer = deserializer._findDefaultImplDeserializer(ctxt);
        assertSame(deserializer1, foundDeserializer);
    }

    @Test(expected = RuntimeException.class)
    public void testHandleUnknownTypeId() throws IOException {
        when(ID_RESOLVER.typeFromId(any(DeserializationContext.class), any(String.class))).thenReturn(null);

        TypeDeserializer deserializer = TEST_SUBCLASS;
        DeserializationContext ctxt = createContext();

        deserializer._handleUnknownTypeId(ctxt, "UnknownType", ID_RESOLVER, BASE_TYPE);
    }

}