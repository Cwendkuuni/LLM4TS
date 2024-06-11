package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgMatchers; import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import static org.junit.Assert.*;

/**
 * Test class for {@link TypeDeserializerBase}
 */
public class TypeDeserializerBaseTest {

    private static final String TEST_TYPE_PROPERTY_NAME = "testTypePropertyName";
    private static final boolean TEST_TYPE_ID_VISIBLE = true;
    private static final JavaType TEST_BASE_TYPE = Mockito.mock(JavaType.class);
    private static final TypeIdResolver TEST_ID_RESOLVER = Mockito.mock(TypeIdResolver.class);
    private static final String TEST_TYPE_ID = "testTypeId";
    private static final JavaType TEST_DEFAULT_IMPL = Mockito.mock(JavaType.class);
    private static final DeserializationContext TEST_CTX = Mockito.mock(DeserializationContext.class);
    private static final Object TEST_JSON_PARSER = Mockito.mock(JsonParser.class);
    private static final String TEST_TYPE_ID_STR = "testTypeIdStr";
    private static final String TEST_EXTRA_DESC = "testExtraDesc";
    private static final JsonDeserializer<Object> TEST_DESERIALIZER = Mockito.mock(JsonDeserializer.class);

    private TypeDeserializerBase _typeDeserializerBase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(TEST_ID_RESOLVER.typeFromId(ArgMatchers.eq(TEST_CTX), ArgMatchers.eq(TEST_TYPE_ID_STR)))
            .thenReturn(TEST_DEFAULT_IMPL);
        _typeDeserializerBase = new TypeDeserializerBase(TEST_BASE_TYPE, TEST_ID_RESOLVER,
                TEST_TYPE_PROPERTY_NAME, TEST_TYPE_ID_VISIBLE, TEST_DEFAULT_IMPL);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty TEST_PROPERTY = Mockito.mock(BeanProperty.class);
        TypeDeserializer expectedTypeDeserializer = new TestTypeDeserializer();
        when(_typeDeserializerBase.forProperty(TEST_PROPERTY)).thenReturn(expectedTypeDeserializer);
        assertEquals(expectedTypeDeserializer, _typeDeserializerBase.forProperty(TEST_PROPERTY));
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, _typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals(TEST_BASE_TYPE.getRawClass().getName(), _typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals(TEST_TYPE_PROPERTY_NAME, _typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(TEST_ID_RESOLVER, _typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertEquals(TEST_DEFAULT_IMPL.getRawClass(), _typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expectedToString = "[TypeDeserializerBase;" +
                " base-type:" + TEST_BASE_TYPE + "; " +
                "id-resolver:" + TEST_ID_RESOLVER + ']';
        assertEquals(expectedToString, _typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        when(_typeDeserializerBase._findDefaultImplDeserializer(TEST_CTX)).thenReturn(TEST_DESERIALIZER);
        assertEquals(TEST_DESERIALIZER, _typeDeserializerBase._findDeserializer(TEST_CTX, TEST_TYPE_ID));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        assertEquals(TEST_DESERIALIZER, _typeDeserializerBase._findDefaultImplDeserializer(TEST_CTX));
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullTypeId() throws Exception {
        _typeDeserializerBase._handleUnknownTypeId(TEST_CTX, null, TEST_ID_RESOLVER, TEST_BASE_TYPE);
    }

    @Test
    public void testHandleUnknownTypeIdKnownTypeId() throws Exception {
        _typeDeserializerBase._handleUnknownTypeId(TEST_CTX, TEST_TYPE_ID, TEST_ID_RESOLVER, TEST_BASE_TYPE);
        verify(TEST_ID_RESOLVER, times(1)).typeFromId(TEST_CTX, TEST_TYPE_ID);
    }

    @Test
    public void testHandleUnknownTypeIdUnknownTypeId() throws Exception {
        _typeDeserializerBase._handleUnknownTypeId(TEST_CTX, "unknownTypeId", TEST_ID_RESOLVER, TEST_BASE_TYPE);
        verify(TEST_CTX, times(1)).handleUnknownTypeId(TEST_BASE_TYPE, TEST_TYPE_ID, TEST_ID_RESOLVER, TEST_EXTRA_DESC);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws Exception {
        _typeDeserializerBase._deserializeWithNativeTypeId(TEST_JSON_PARSER, TEST_CTX);
        verify(TEST_CTX, times(1)).findContextualValueDeserializer(_defaultImpl, TEST_PROPERTY);
    }

    @Test
    public void testDeserializeWithNativeTypeIdKnownTypeId() throws Exception {
        String expectedTypeIdStr = "knownTypeId";
        when(_jsonParser.getTypeId()).thenReturn(expectedTypeIdStr);
        when(_typeDeserializerBase._findDeserializer(TEST_CTX, expectedTypeIdStr)).thenReturn(TEST_DESERIALIZER);
        Object result = _typeDeserializerBase._deserializeWithNativeTypeId(TEST_JSON_PARSER, TEST_CTX);
        assertEquals(TEST_DESERIALIZER.deserialize(TEST_JSON_PARSER, TEST_CTX), result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws Exception {
        String expectedTypeIdStr = "unknownTypeId";
        when(_jsonParser.getTypeId()).thenReturn(expectedTypeIdStr);
        when(_typeDeserializerBase._handleUnknownTypeId(TEST_CTX, expectedTypeIdStr, TEST_ID_RESOLVER, TEST_BASE_TYPE))
            .thenReturn(TEST_DEFAULT_IMPL);
        Object result = _typeDeserializerBase._deserializeWithNativeTypeId(TEST_JSON_PARSER, TEST_CTX);
        assertNull(result);
    }

    private static class TestTypeDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }
}