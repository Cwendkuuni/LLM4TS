package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private JsonDeserializer<Object> deser;

    @Mock
    private TypeIdResolver idResolver;

    @Mock
    private BeanProperty property;

    @Mock
    private JavaType baseType;

    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "type", true, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        assertEquals(baseType, typeDeserializer.baseType());
    }

    @Test
    public void testToString() {
        String toString = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest$1BaseClassTest; base-type:" +
                baseType + "; id-resolver: " + idResolver + "]";
        assertEquals(toString, typeDeserializer.toString());
    }

    @Test
    public void testFindDeserializerWithDeser() throws IOException {
        String typeId = "testType";
        typeDeserializer._deserializers.put(typeId, deser);
        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(ctxt, typeId);
        assertNotNull(result);
    }

    @Test
    public void testFindDeserializerWithDefaultImpl() throws IOException {
        typeDeserializer._defaultImplDeserializer = deser;
        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(ctxt, "testType");
        assertNotNull(result);
    }

    @Test
    public void testFindDeserializerWithDefaultImplDeserializerNull() throws IOException {
        typeDeserializer._defaultImplDeserializer = null;
        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(ctxt, "testType");
        assertNull(result);
    }

    @Test
    public void testFindDeserializerWithUnknownType() throws IOException {
        String typeId = "testType";
        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        Object result = typeDeserializer._deserializeWithNativeTypeId(null, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdAndTypeId() throws IOException {
        String typeId = "testType";
        typeDeserializer._deserializers.put(typeId, deser);
        Object result = typeDeserializer._deserializeWithNativeTypeId(null, ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "testType";
        JavaType result = typeDeserializer._handleUnknownTypeId(ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        String extraDesc = "extra";
        JavaType result = typeDeserializer._handleMissingTypeId(ctxt, extraDesc);
        assertNull(result);
    }
}