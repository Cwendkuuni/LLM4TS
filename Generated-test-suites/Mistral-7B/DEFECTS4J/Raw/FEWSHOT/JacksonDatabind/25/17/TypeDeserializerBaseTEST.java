package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    private TypeIdResolver _idResolver;
    private TypeDeserializerBase _testee;
    private JsonDeserializer<Object> _defaultImplDeserializer;
    private DeserializationContext _ctxt;

    @Before
    public void setup() {
        _idResolver = mock(TypeIdResolver.class);
        _testee = new BaseTypeDeserializer(new BaseType(null), _idResolver, "_typePropertyName", true, null);
        _ctxt = mock(DeserializationContext.class);
        _defaultImplDeserializer = mock(JsonDeserializer.class);
        when(_ctxt.findContextualValueDeserializer(_testee._defaultImpl, _testee._property)).thenReturn(_defaultImplDeserializer);
    }

    @Test
    public void testFindDeserializer_WhenTypeIdIsResolved_ReturnsDeserializer() throws IOException {
        String typeId = "testTypeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(_idResolver.typeFromId(_ctxt, typeId)).thenReturn(new JavaType(String.class));
        when(_ctxt.findContextualValueDeserializer(JavaType.getType(String.class), _testee._property)).thenReturn(deserializer);

        JsonDeserializer<Object> result = _testee._findDeserializer(_ctxt, typeId);

        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDeserializer_WhenTypeIdIsNotResolved_ReturnsDefaultImplDeserializer() throws IOException {
        _testee = new BaseTypeDeserializer(new BaseType(null), _idResolver, "_typePropertyName", true, null);
        JsonDeserializer<Object> result = _testee._findDeserializer(_ctxt, "notResolvedTypeId");

        assertSame(_defaultImplDeserializer, result);
    }

    @Test
    public void testFindDeserializer_WhenIdResolverIsNull_ReturnsNull() throws IOException {
        _testee = new BaseTypeDeserializer(new BaseType(null), null, "_typePropertyName", true, null);
        JsonDeserializer<Object> result = _testee._findDeserializer(_ctxt, "notResolvedTypeId");

        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializer_WhenDefaultImplIsNotNull_ReturnsDeserializer() throws IOException {
        _testee = new BaseTypeDeserializer(new BaseType(null), _idResolver, "_typePropertyName", true, Object.class);

        JsonDeserializer<Object> result = _testee._findDefaultImplDeserializer(_ctxt);

        assertEquals(_defaultImplDeserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer_WhenDefaultImplIsNull_ReturnsNull() throws IOException {
        _testee = new BaseTypeDeserializer(new BaseType(null), _idResolver, "_typePropertyName", true, null);

        JsonDeserializer<Object> result = _testee._findDefaultImplDeserializer(_ctxt);

        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId_ThrowsException() throws IOException {
        String typeId = "unknownTypeId";
        _testee = new BaseTypeDeserializer(new BaseType(null), _idResolver, "_typePropertyName", true, null);

        _testee._handleUnknownTypeId(_ctxt, typeId, _idResolver, null);
    }

    static class BaseTypeDeserializer extends TypeDeserializerBase<BaseType> {
        public BaseTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        BaseTypeDeserializer(TypeDeserializerBase<BaseType> src, BeanProperty property) {
            super(src, property);
        }
    }

    static class BaseType {

    }
}