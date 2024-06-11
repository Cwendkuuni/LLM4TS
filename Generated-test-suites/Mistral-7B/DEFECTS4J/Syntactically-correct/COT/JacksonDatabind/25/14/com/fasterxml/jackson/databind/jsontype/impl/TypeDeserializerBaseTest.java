package com.fasterxml.jackson.databind.jsontype.impl; // make sure the package is correct

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext context;

    @Mock
    private JsonParser parser;

    @Mock
    private BeanProperty property;

    @Mock
    private TypeIdResolver typeIdResolver;

    @Mock
    private JsonDeserializer<Object> deserializer;

    @Mock
    private JavaType javaType;

    @Mock
    private TypeDeserializer typeDeserializer;

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        typeDeserializerBase = new TypeDeserializerBase(
                javaType, typeIdResolver, "_typePropertyName", true, Object.class) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return deserializer;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }

            @Override
            protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testForProperty() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializerBase._findDeserializer(context, "typeId")).thenReturn(typeDeserializer);

        TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(property);

        verify(typeDeserializerBase).forProperty(property);
        verify(typeDeserializerBase)._deserializers.put("typeId", typeDeserializer);
        assertEquals(typeDeserializer, actualTypeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As actualTypeInclusion = typeDeserializerBase.getTypeInclusion();

        assertNotNull(actualTypeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String actualBaseTypeName = typeDeserializerBase.baseTypeName();

        assertEquals("java.lang.Object", actualBaseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String actualPropertyName = typeDeserializerBase.getPropertyName();

        assertNotNull(actualPropertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver actualTypeIdResolver = typeDeserializerBase.getTypeIdResolver();

        assertNotNull(actualTypeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> actualDefaultImpl = typeDeserializerBase.getDefaultImpl();

        assertNull(actualDefaultImpl);
    }

    @Test
    public void testToString() {
        String actualToString = typeDeserializerBase.toString();

        assertNotNull(actualToString);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(typeDeserializerBase._deserializers.get("typeId")).thenReturn(deserializer);

        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(context, "typeId");

        verify(typeDeserializerBase)._deserializers.get("typeId");
        assertEquals(deserializer, actualDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> actualDefaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertNull(actualDefaultImplDeserializer);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNull() throws IOException {
        typeDeserializerBase._deserializeWithNativeTypeId(parser, context, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdUnknown() throws IOException {
        when(typeIdResolver.typeFromId(context, "unknownTypeId")).thenReturn(javaType);

        typeDeserializerBase._deserializeWithNativeTypeId(parser, context, "unknownTypeId");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        when(typeIdResolver.typeFromId(context, "knownTypeId")).thenReturn(javaType);

        Object actualHandleUnknownTypeId = typeDeserializerBase._handleUnknownTypeId(context, "knownTypeId", typeIdResolver, javaType);

        assertNull(actualHandleUnknownTypeId);
        verify(context).unknownTypeException(javaType, "knownTypeId", "known type ids = [Ljava.lang.Class;@1b");
    }
}