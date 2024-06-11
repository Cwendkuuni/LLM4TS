package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private static final String TYPE_ID_NAME = "TestTypeId";

    private TypeDeserializerBase typeDeserializerBase;
    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        context = mock(DeserializationContext.class);
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        typeDeserializerBase = new TypeDeserializerBase(
                JavaType.valueOf(String.class), mock(TypeIdResolver.class), "typePropertyName", true, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                if (TYPE_ID_NAME.equals(typeId)) {
                    return spy(new StdDeserializer<String>(String.class));
                }
                return null;
            }
        };
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase",
                typeDeserializerBase.getClass().getName());
        assertEquals(JavaType.valueOf(String.class), typeDeserializerBase.baseType());
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
        assertTrue(typeDeserializerBase.isTypeIdVisible());
        assertNotNull(typeDeserializerBase._deserializers);
        assertNull(typeDeserializerBase._defaultImplDeserializer);
    }

    /**
     * Test {@link #forProperty(BeanProperty)}.
     *
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testForProperty() throws IOException, JsonProcessingException {
        BeanProperty beanProperty = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertEquals(String.class, typeDeserializer.getClass());
        assertEquals(typeDeserializerBase, typeDeserializer.baseType());
        assertNotNull(typeDeserializer.getTypeIdResolver());
        assertEquals("typePropertyName", typeDeserializer.getPropertyName());
        assertTrue(typeDeserializer.isTypeIdVisible());
        assertNotNull(typeDeserializer._deserializers);
        assertNull(typeDeserializer._defaultImplDeserializer);
    }

    /**
     * Test {@link #getTypeInclusion()}.
     */
    @Test
     public void testgetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    /**
     * Test {@link #baseTypeName()}.
     */
    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    /**
     * Test {@link #toString()}.
     */
    @Test
    public void testToString() {
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.contains("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase"));
        assertTrue(toString.contains("; base-type:java.lang.String"));
        assertTrue(toString.contains("; id-resolver: "));
    }

    /**
     * Test {@link #_findDeserializer(DeserializationContext, String)}.
     *
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testFindDeserializer() throws IOException, JsonProcessingException {
        when(context.findContextualValueDeserializer(eq(String.class), any(BeanProperty.class)))
                .thenReturn(spy(new StdDeserializer<String>(String.class)));

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, TYPE_ID_NAME);

        assertEquals(String.class, deserializer.getClass());
        verify(context).findContextualValueDeserializer(eq(String.class), any(BeanProperty.class));
    }

    /**
     * Test {@link #_findDefaultImplDeserializer(DeserializationContext)}.
     *
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testFindDefaultImplDeserializer() throws IOException, JsonProcessingException {
        when(context.isEnabled(eq(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE))).thenReturn(false);
        when(context.findContextualValueDeserializer(eq(Void.class), any(BeanProperty.class)))
                .thenReturn(NullifyingDeserializer.instance);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertEquals(NullifyingDeserializer.class, deserializer.getClass());
        verify(context).isEnabled(eq(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE));
        verify(context).findContextualValueDeserializer(eq(Void.class), any(BeanProperty.class));
    }

    /**
     * Test {@link #_deserializeWithNativeTypeId(JsonParser, DeserializationContext)} with known type id.
     *
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException, JsonProcessingException {
        when(context.findContextualValueDeserializer(eq(String.class), any(BeanProperty.class)))
                .thenReturn(spy(new StdDeserializer<String>(String.class)));

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), context, TYPE_ID_NAME);

        assertEquals("", result);
        verify(context).findContextualValueDeserializer(eq(String.class), any(BeanProperty.class));
    }

    /**
     * Test {@link #_deserializeWithNativeTypeId(JsonParser, DeserializationContext)} with no type id.
     *
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializeWithNativeTypeIdNoTypeId() throws IOException, JsonProcessingException {
        when(context.findContextualValueDeserializer(eq(Void.class), any(BeanProperty.class)))
                .thenReturn(NullifyingDeserializer.instance);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), context, null);

        assertNull(result);
        verify(context).findContextualValueDeserializer(eq(Void.class), any(BeanProperty.class));
    }

    /**
     * Test {@link #_handleUnknownTypeId(DeserializationContext, String)} with a valid type id.
     *
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testHandleUnknownTypeId() throws IOException, JsonProcessingException {
        JavaType javaType = mock(JavaType.class);
        when(context.handleMissingTypeId(eq(JavaType.valueOf(String.class)), any(TypeIdResolver.class), anyString()))
                .thenReturn(javaType);

        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID_NAME);

        assertEquals(javaType, result);
        verify(context).handleMissingTypeId(eq(JavaType.valueOf(String.class)), any(TypeIdResolver.class),
                anyString());
    }

    /**
     * Test {@link #_handleMissingTypeId(DeserializationContext, String)} with no type id.
     *
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testHandleMissingTypeId() throws IOException, JsonProcessingException {
        JavaType javaType = mock(JavaType.class);
        when(context.handleMissingTypeId(eq(JavaType.valueOf(String.class)), any(TypeIdResolver.class), anyString()))
                .thenReturn(javaType);

        JavaType result = typeDeserializerBase._handleMissingTypeId(context, "");

        assertEquals(javaType, result);
        verify(context).handleMissingTypeId(eq(JavaType.valueOf(String.class)), any(TypeIdResolver.class),
                anyString());
    }

}