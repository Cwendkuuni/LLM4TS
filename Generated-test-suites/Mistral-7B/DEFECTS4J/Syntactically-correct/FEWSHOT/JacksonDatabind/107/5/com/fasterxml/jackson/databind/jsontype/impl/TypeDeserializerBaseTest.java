package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private DeserializationContext _ctxt;
    private TypeDeserializerBase _deserializer;
    private TypeIdResolver _idResolver;
    private BeanProperty _property;
    private final JavaType _baseType = mock(JavaType.class);

    @Before
    public void setup() throws Exception {
        _idResolver = mock(TypeIdResolver.class);
        _idResolver.typeFromId(_ctxt, "testId");
        when(_idResolver.getDescForKnownTypeIds()).thenReturn("knownTypeIds");

        _property = mock(BeanProperty.class);
        _property.getName();

        _ctxt = mock(DeserializationContext.class);
        when(_ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(_ctxt.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
        when(_ctxt.findContextualValueDeserializer(_baseType, _property)).thenReturn(NullifyingDeserializer.instance);
        when(_ctxt.handleMissingTypeId(_baseType, "extraDesc")).thenThrow(new IOException("Test error message"));
        when(_ctxt.handleUnknownTypeId(_baseType, "typeId")).thenReturn(null);

        _deserializer = new TypeDeserializerBase(_baseType, _idResolver, "typePropertyName", true, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                _property = prop;
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                return ctxt.findContextualValueDeserializer(_baseType, _property);
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                    throws IOException {
                return ctxt.findContextualValueDeserializer(_baseType, _property);
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                    throws IOException {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId)
                    throws IOException {
                return null;
            }

            @Override
            protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc)
                    throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializer typeDeserializer = _deserializer.forProperty(_property);
        assertEquals(_deserializer.getClass(), typeDeserializer.getClass());
        assertSame(_deserializer, typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, _deserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals(_baseType.getRawClass().getName(), _deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals("typePropertyName", _deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(_idResolver, _deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertNull(_deserializer.getDefaultImpl());
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws Exception {
        assertNull(_deserializer._findDeserializer(_ctxt, null));
    }

    @Test
    public void testFindDeserializerWithEmptyTypeId() throws Exception {
        String emptyTypeId = "";
        assertNull(_deserializer._findDeserializer(_ctxt, emptyTypeId));
    }

    @Test
    public void testFindDeserializerWithNonEmptyTypeId() throws Exception {
        String typeId = "testId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(_deserializer._findDeserializer(_ctxt, typeId)).thenReturn(deserializer);
        JsonDeserializer<Object> returnedDeserializer = _deserializer._findDeserializer(_ctxt, typeId);
        assertEquals(deserializer, returnedDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullTypeId() throws Exception {
        assertNull(_deserializer._findDefaultImplDeserializer(_ctxt));
    }

    @Test
    public void testFindDefaultImplDeserializerWithNotNullTypeId() throws Exception {
        String typeId = "testId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(_deserializer._findDefaultImplDeserializer(_ctxt)).thenReturn(deserializer);
        JsonDeserializer<Object> returnedDeserializer = _deserializer._findDefaultImplDeserializer(_ctxt);
        assertEquals(deserializer, returnedDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeIdAndNullTypeId() throws Exception {
        assertNull(_deserializer._deserializeWithNativeTypeId(_ctxt.getParser(), _ctxt));
    }

    @Test
    public void testDeserializeWithNativeTypeIdAndEmptyTypeId() throws Exception {
        assertNull(_deserializer._deserializeWithNativeTypeId(_ctxt.getParser(), _ctxt, ""));
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithNonEmptyTypeId() throws Exception {
        String typeId = "testId";
        when(_deserializer._findDeserializer(_ctxt, typeId)).thenReturn(mock(JsonDeserializer.class));
        Object deserializedObject = _deserializer._deserializeWithNativeTypeId(_ctxt.getParser(), _ctxt, typeId);
        assertNull(deserializedObject);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        String typeId = "typeId";
        assertNull(_deserializer._handleUnknownTypeId(_ctxt, typeId));
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        String extraDesc = "extraDesc";
        assertNull(_deserializer._handleMissingTypeId(_ctxt, extraDesc));
        verify(_ctxt).handleMissingTypeId(_baseType, _idResolver, extraDesc);
    }
}