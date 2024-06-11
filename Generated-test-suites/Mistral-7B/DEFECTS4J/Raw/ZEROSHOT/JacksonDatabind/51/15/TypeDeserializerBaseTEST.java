package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends TestBase {

    private static final String TEST_CLASS_NAME = "com.example.TestTypeDeserializerBase";
    private static final String TEST_BASE_TYPE_NAME = "java.lang.Object";
    private static final String TEST_TYPE_PROPERTY_NAME = "typePropertyName";
    private static final boolean TEST_TYPE_ID_VISIBLE = true;
    private static final TypeIdResolver TEST_ID_RESOLVER = mock(TypeIdResolver.class);
    private static final JavaType TEST_DEFAULT_IMP = objectMapper.getTypeFactory().constructType(String.class);
    private TypeDeserializerBase testee;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectMsgContext context = new ObjectMsgContext(objectMapper.getFactory());
        testee = new TypeDeserializerBase(
                objectMapper.getTypeFactory().constructType(TEST_CLASS_NAME),
                TEST_ID_RESOLVER,
                TEST_TYPE_PROPERTY_NAME,
                TEST_TYPE_ID_VISIBLE,
                TEST_DEFAULT_IMP
        ) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };
    }

    @Test
    public void testInstance() {
        assertNotNull(testee);
        assertEquals(TEST_CLASS_NAME, testee.getClass().getName());
        assertEquals(TEST_BASE_TYPE_NAME, testee.baseTypeName());
        assertEquals(TEST_ID_RESOLVER, testee.getTypeIdResolver());
        assertEquals(TEST_TYPE_PROPERTY_NAME, testee.getPropertyName());
        assertEquals(TEST_TYPE_ID_VISIBLE, testee.getTypeIdVisible());
        assertEquals(TEST_DEFAULT_IMP, testee.getDefaultImpl());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = testee.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testForProperty() {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer result = testee.forProperty(property);
        assertEquals(testee, result);
    }

    @Test
    public void testFindDeserializer_NullTypeId() throws IOException {
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), null)).thenReturn(null);
        JsonDeserializer<Object> result = testee._findDeserializer(mock(DeserializationContext.class), null);
        assertNull(result);
    }

    @Test
    public void testFindDeserializer_TypeIdNotFound() throws IOException {
        String typeId = "notFoundTypeId";
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), isA(String.class)))
                .thenReturn(null);
        JsonDeserializer<Object> result = testee._findDeserializer(mock(DeserializationContext.class), typeId);
        assertNull(result);
    }

    @Test
    public void testFindDeserializer_TypeIdFound() throws IOException {
        String typeId = "foundTypeId";
        JavaType type = objectMapper.getTypeFactory().constructType(String.class);
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), isA(String.class)))
                .thenReturn(type);
        JsonDeserializer<Object> result = testee._findDeserializer(mock(DeserializationContext.class), typeId);
        verify(TEST_ID_RESOLVER, times(1)).typeFromId(any(DeserializationContext.class), isA(String.class));
        assertEquals(type, result.getType(mock(DeserializationContext.class)));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), null)).thenReturn(null);
        JsonDeserializer<Object> result = testee._findDefaultImplDeserializer(mock(DeserializationContext.class));
        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializer_WithDefaultImpl() throws IOException {
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), null)).thenReturn(null);
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
        when(testee._defaultImplDeserializer).thenReturn(defaultImplDeserializer);
        JsonDeserializer<Object> result = testee._findDefaultImplDeserializer(mock(DeserializationContext.class));
        assertEquals(defaultImplDeserializer, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getTypeId()).thenReturn(null);
        Object result = testee._deserializeWithNativeTypeId(parser, mock(DeserializationContext.class));
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_TypeIdNotFound() throws IOException {
        String typeId = "notFoundTypeId";
        JsonParser parser = mock(JsonParser.class);
        when(parser.getTypeId()).thenReturn(typeId);
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), isA(String.class)))
                .thenReturn(null);
        Object result = testee._deserializeWithNativeTypeId(parser, mock(DeserializationContext.class));
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_TypeIdFound() throws IOException {
        String typeId = "foundTypeId";
        JavaType type = objectMapper.getTypeFactory().constructType(String.class);
        JsonParser parser = mock(JsonParser.class);
        when(parser.getTypeId()).thenReturn(typeId);
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), isA(String.class)))
                .thenReturn(type);
        Object result = testee._deserializeWithNativeTypeId(parser, mock(DeserializationContext.class));
        verify(parser, times(1)).nextToken();
        verify(parser, times(1)).getValueAsString();
        verify(TEST_ID_RESOLVER, times(1)).typeFromId(any(DeserializationContext.class), isA(String.class));
        assertEquals(type, result.getClass().getDeclaredType());
    }

    @Test
    public void testHandleUnknownTypeId_UnknownTypeId() throws IOException {
        String typeId = "unknownTypeId";
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), isA(String.class)))
                .thenReturn(null);
        JavaType result = testee._handleUnknownTypeId(mock(DeserializationContext.class), typeId, TEST_ID_RESOLVER, TEST_BASE_TYPE_NAME);
        assertNotNull(result);
        assertNull(result.getRawClass());
    }

    @Test
    public void testHandleUnknownTypeId_KnownTypeId() throws IOException {
        String typeId = "knownTypeId";
        JavaType type = objectMapper.getTypeFactory().constructType(Integer.class);
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), isA(String.class)))
                .thenReturn(type);
        JavaType result = testee._handleUnknownTypeId(mock(DeserializationContext.class), typeId, TEST_ID_RESOLVER, TEST_BASE_TYPE_NAME);
        assertNotNull(result);
        assertEquals(type, result);
    }
}