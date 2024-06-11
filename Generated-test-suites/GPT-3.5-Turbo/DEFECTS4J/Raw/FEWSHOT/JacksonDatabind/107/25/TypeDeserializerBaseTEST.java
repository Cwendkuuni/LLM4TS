package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TypeDeserializerBaseTest {

    @Mock
    private TypeIdResolver _idResolver;

    @Mock
    private BeanProperty _property;

    @Mock
    private JavaType _defaultImpl;

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        typeDeserializerBase = new TypeDeserializerBaseMock(_defaultImpl);
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("_typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(_idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertEquals(Object.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        assertEquals(_defaultImpl, typeDeserializerBase.baseType());
    }

    @Test
    public void testToString() {
        String expectedString = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseMock; base-type:_defaultImpl; id-resolver: "
                + _idResolver + "]";
        assertEquals(expectedString, typeDeserializerBase.toString());
    }

    @Test
    public void testForProperty() {
        BeanProperty beanProperty = mock(BeanProperty.class);
        TypeDeserializerBase newTypeDeserializerBase = typeDeserializerBase.forProperty(beanProperty);
        assertEquals(beanProperty, newTypeDeserializerBase._property);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(null, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithTypeId() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object typeId = "test";
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, typeId);
        assertNull(result);
    }

    @Test
    public void test_findDeserializer() throws IOException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        String typeId = "test";
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(deserializationContext, typeId);
        assertNull(result);
    }

    @Test
    public void test_findDefaultImplDeserializer() throws IOException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);
        assertEquals(NullifyingDeserializer.instance, result);
    }

    @Test(expected = JsonMappingException.class)
    public void test_handleUnknownTypeId() throws IOException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        String typeId = "test";
        typeDeserializerBase._handleUnknownTypeId(deserializationContext, typeId);
    }

    private class TypeDeserializerBaseMock extends TypeDeserializerBase {
        private static final long serialVersionUID = 1L;

        protected TypeDeserializerBaseMock(JavaType defaultImpl) {
            super(null, null, "_typePropertyName", true, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }

}