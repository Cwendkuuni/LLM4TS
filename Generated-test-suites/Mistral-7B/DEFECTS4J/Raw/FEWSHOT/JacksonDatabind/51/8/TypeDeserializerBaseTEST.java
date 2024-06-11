package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest extends TestBase {

    private TypeDeserializerBase _td;
    private DeserializationContext _ctxt;
    private JsonParser _parser;
    private JsonFactory _factory;

    @Override
    public void setUp() throws Exception {
        _context.clearFilters();
        _factory = new ObjectMapper().getFactory();
        _ctxt = mock(DeserializationContext.class);
        _parser = _factory.createParser("{}");
        _td = new TypeDeserializerBase(Object.class.getTypeName(), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                    throws IOException {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializer deserializer = _td.forProperty(null);
        assertSame(_td, deserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyWithNullProperty() throws Exception {
        _td.forProperty(null);
    }

    @Test
    public void testTypeInclusion() {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, _td.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("java.lang.Object", _td.baseTypeName());
    }

    @Test
    public void testPropertyName() throws Exception {
        assertNull(_td.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertSame(null, _td.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertNull(_td.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expected = "[TypeDeserializerBase; base-type:java.lang.Object; id-resolver: null]";
        assertEquals(expected, _td.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        when(_td._idResolver.typeFromId(_ctxt, "test")).thenReturn(null);

        JsonDeserializer deserializer = _td._findDeserializer(_ctxt, "test");
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithDeserializer() throws Exception {
        String typeId = "test";
        JsonDeserializer deserializer = mock(JsonDeserializer.class);
        when(_td._idResolver.typeFromId(_ctxt, typeId)).thenReturn(ObjectType.class);
        when(ObjectType.class.getDeclaredField("deserializer")).thenReturn(deserializer);
        when(deserializer.getClass()).thenReturn(JsonDeserializer.class);

        JsonDeserializer foundDeserializer = _td._findDeserializer(_ctxt, typeId);
        assertSame(deserializer, foundDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        when(_td._idResolver.isEnabled(_ctxt, DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);

        JsonDeserializer deserializer = _td._findDefaultImplDeserializer(_ctxt);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullContext() throws Exception {
        JsonDeserializer deserializer = mock(JsonDeserializer.class);
        when(_td._defaultImplDeserializer).thenReturn(deserializer);

        JsonDeserializer foundDeserializer = _td._findDefaultImplDeserializer(null);
        assertSame(deserializer, foundDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithEnabledFeature() throws Exception {
        when(_td._idResolver.isEnabled(_ctxt, DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);

        JsonDeserializer deserializer = mock(JsonDeserializer.class);
        when(_td._defaultImplDeserializer).thenReturn(deserializer);

        JsonDeserializer foundDeserializer = _td._findDefaultImplDeserializer(_ctxt);
        assertSame(deserializer, foundDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullDefaultImplDeserializer() throws Exception {
        when(_td._idResolver.isEnabled(_ctxt, DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(_td._defaultImplDeserializer).thenReturn(null);

        Object type = new Object() {};
        when(_td._idResolver.typeFromId(_ctxt, "test")).thenReturn(type.getClass());
        when(_ctxt.findContextualValueDeserializer(type, null)).thenReturn(mock(JsonDeserializer.class));

        JsonDeserializer foundDeserializer = _td._findDefaultImplDeserializer(_ctxt);
        assertNotNull(foundDeserializer);
    }

    @Test
    public void testHandleUnknownTypeIdWithNullTypeId() throws Exception {
        JavaType result = _td._handleUnknownTypeId(_ctxt, null, _td._idResolver, null);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithNullIdResolver() throws Exception {
        JavaType result = _td._handleUnknownTypeId(_ctxt, "test", null, null);
        fail("expected IOException");
    }

    @Test
    public void testHandleUnknownTypeIdWithValidTypeId() throws Exception {
        JavaType expected = String.class;
        when(_td._idResolver.typeFromId(_ctxt, "test")).thenReturn(expected);

        JavaType result = _td._handleUnknownTypeId(_ctxt, "test", _td._idResolver, null);
        assertSame(expected, result);
    }

    @Test
    public void testHandleUnknownTypeIdWithNullTypeIdReturnNull() throws Exception {
        when(_td._idResolver.typeFromId(_ctxt, "test")).thenReturn(null);

        JavaType result = _td._handleUnknownTypeId(_ctxt, "test", _td._idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdWithNullBaseType() throws Exception {
        JavaType expected = String.class;
        when(_td._idResolver.typeFromId(_ctxt, "test")).thenReturn(expected);

        JavaType result = _td._handleUnknownTypeId(_ctxt, "test", null, null);
        assertSame(expected, result);
    }

    @Test
    public void testHandleUnknownTypeIdWithNullBaseTypeReturnNull() throws Exception {
        JavaType expected = null;
        when(_td._idResolver.typeFromId(_ctxt, "test")).thenReturn(null);

        JavaType result = _td._handleUnknownTypeId(_ctxt, "test", null, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdWithIdResolverThatReturnsNull() throws Exception {
        JavaType expected = null;
        when(_td._idResolver.typeFromId(_ctxt, "test")).thenReturn(null);

        JavaType result = _td._handleUnknownTypeId(_ctxt, "test", _td._idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdWithIdResolverThatReturnsUnknownTypeId() throws Exception {
        JavaType expected = String.class;
        when(_td._idResolver.typeFromId(_ctxt, "test")).thenReturn(new UnknownTypeId("test"));

        JavaType result = _td._handleUnknownTypeId(_ctxt, "test", _td._idResolver, null);
        assertSame(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleUnknownTypeIdWithNullBaseTypeAndNonNativeTypeId() throws Exception {
        when(_td._idResolver.typeFromId(_ctxt, "test")).thenThrow(new RuntimeException());

        _td._handleUnknownTypeId(_ctxt, "test", _td._idResolver, null);
    }
}