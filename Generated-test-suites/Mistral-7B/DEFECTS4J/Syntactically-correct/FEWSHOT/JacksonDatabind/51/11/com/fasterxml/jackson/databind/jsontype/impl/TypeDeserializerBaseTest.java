package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends TestBase {

    private static final TypeIdResolver idResolver = mock(TypeIdResolver.class);
    private static final Object defaultImpl = new Object() {};

    @Test
    public void testTypeDeserializerBaseConstruction() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, "", false, defaultImpl);
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());
        assertEquals(null, typeDeserializerBase.getProperty());
        assertEquals("", typeDeserializerBase.getTypePropertyName());
        assertFalse(typeDeserializerBase.isTypeIdVisible());
        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
        assertEquals(1, typeDeserializerBase._deserializers.size());
    }

    @Test
    public void testTypeDeserializerBaseCopyConstruction() {
        TypeDeserializerBase baseTypeDeserializer = createBaseTypeDeserializer();
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase copy = new TypeDeserializerBase(baseTypeDeserializer, property);
        assertEquals(baseTypeDeserializer, copy.getBaseType());
        assertEquals(baseTypeDeserializer.getIdResolver(), copy.getIdResolver());
        assertEquals(baseTypeDeserializer.getTypePropertyName(), copy.getTypePropertyName());
        assertEquals(baseTypeDeserializer.isTypeIdVisible(), copy.isTypeIdVisible());
        assertEquals(baseTypeDeserializer.getDefaultImpl(), copy.getDefaultImpl());
        assertEquals(baseTypeDeserializer.getDefaultImplDeserializer(), copy.getDefaultImplDeserializer());
        assertEquals(property, copy.getProperty());
    }

    @Test
    public void testFindDeserializerWithNullContext() {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        assertNull(typeDeserializerBase._findDeserializer(null, ""));
    }

    @Test
    public void testFindDeserializerWithEmptyTypeId() {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        assertNull(typeDeserializerBase._findDeserializer(mockContext(), ""));
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(mockContext(), "typeId")).thenReturn(mockType(JsonType.construct("typeId", String.class)));
        when(typeDeserializerBase._findDeserializer(mockContext(), "typeId")).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(mockContext(), "typeId");
        assertEquals(deserializer, result);
        verify(typeDeserializerBase)._deserializers.put("typeId", deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(mockContext(), "typeId")).thenReturn(mockType(JsonType.construct("typeId", String.class)));
        when(typeDeserializerBase._findDeserializer(mockContext(), "typeId")).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(mockContext());
        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(mockContext(), "typeId")).thenReturn(mockType(JsonType.construct("typeId", String.class)));
        when(typeDeserializerBase._findDeserializer(mockContext(), "typeId")).thenReturn(deserializer);
        when(typeDeserializerBase._findDefaultImplDeserializer(mockContext())).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(mockContext());
        assertEquals(deserializer, result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        when(idResolver.typeFromId(mockContext(), "typeId")).thenReturn(null);

        TypeIdResolver baseTypeIdResolver = mock(TypeIdResolver.class);
        when(idResolver.getTypeIdResolver()).thenReturn(baseTypeIdResolver);
        when(baseTypeIdResolver.getDescForKnownTypeIds()).thenReturn("desc");

        Object result = typeDeserializerBase._handleUnknownTypeId(mockContext(), "typeId", idResolver, null);
        assertEquals("Unknown type id: 'typeId'. Cannot deserialize instance of "
                + TypeDeserializerBase.class.getName() + " out of START_ARRAY token\n"
                + " at [Source: (String)null; line: -1, column: -1]", result.toString());
        verify(mockContext()).handleUnknownTypeId(_baseType, "typeId", idResolver, "desc");
    }

    @Test
    public void testHandleUnknownTypeIdWithNullTypeIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        when(idResolver.typeFromId(mockContext(), "typeId")).thenReturn(null);

        Object result = typeDeserializerBase._handleUnknownTypeId(mockContext(), "typeId", null, null);
        assertEquals("Unknown type id: 'typeId'. Cannot deserialize instance of "
                + TypeDeserializerBase.class.getName() + " out of START_ARRAY token\n"
                + " at [Source: (String)null; line: -1, column: -1]", result.toString());
        verifyZeroInteractions(idResolver);
    }

    @Test
    public void testHandleUnknownTypeIdWithNullBaseType() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        when(idResolver.typeFromId(mockContext(), "typeId")).thenReturn(null);

        Object result = typeDeserializerBase._handleUnknownTypeId(mockContext(), "typeId", idResolver, null);
        assertEquals("Unknown type id: 'typeId'. Cannot deserialize instance of "
                + TypeDeserializerBase.class.getName() + " out of START_ARRAY token\n"
                + " at [Source: (String)null; line: -1, column: -1]", result.toString());
        verifyZeroInteractions(idResolver);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(null));
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullProperty() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(mockContext()));
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializerBase._findDeserializer(mockContext(), "typeId")).thenReturn(deserializer);
        when(typeDeserializerBase._getDefaultImpl()).thenReturn(defaultImpl);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(mockContext());
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        when(typeDeserializerBase._findDeserializer(mockContext(), "typeId")).thenReturn(mock(JsonDeserializer.class));
        when(typeDeserializerBase._getDefaultImpl()).thenReturn(null);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(mockContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"typeId\"}");

        when(idResolver.typeFromId(mockContext(), "typeId")).thenReturn(mockType(JsonType.construct("typeId", String.class)));
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializerBase._findDeserializer(mockContext(), "typeId")).thenReturn(deserializer);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, mockContext());
        assertEquals(deserializer.deserialize(jp, mockContext()), result);
        verify(deserializer).deserialize(jp, mockContext());
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithNullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{}");

        when(typeDeserializerBase._findDefaultImplDeserializer(mockContext())).thenReturn(mock(JsonDeserializer.class));

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, mockContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithNullTypeIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"typeId\"}");

        when(typeDeserializerBase._findDefaultImplDeserializer(mockContext())).thenReturn(mock(JsonDeserializer.class));
        when(typeDeserializerBase.getTypeIdResolver()).thenReturn(null);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, mockContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithNullBaseType() throws IOException {
        TypeDeserializerBase typeDeserializerBase = createBaseTypeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"typeId\"}");

        when(typeDeserializerBase._findDefaultImplDeserializer(mockContext())).thenReturn(mock(JsonDeserializer.class));
        when(typeDeserializerBase.getBaseType()).thenReturn(null);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, mockContext());
        assertNull(result);
    }

    private TypeDeserializerBase createBaseTypeDeserializer() {
        return new TypeDeserializerBase(null, idResolver, "", false, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
    }

    private DeserializationContext mockContext() {
        return new TestBase.SimpleContext();
    }

    private JsonType mockType(Class<?> clazz) {
        return new JsonType() {
            @Override
            public Class<?> getRawClass() {
                return clazz;
            }

            @Override
            public JsonType construct(Class<?> rawClass, String name) {
                return this;
            }
        };
    }
}