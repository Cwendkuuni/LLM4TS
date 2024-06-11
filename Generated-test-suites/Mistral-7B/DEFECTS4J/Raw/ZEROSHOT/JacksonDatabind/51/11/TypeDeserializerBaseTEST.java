package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    private static final String CLASS_NAME = "com.example.TestClass";
    private static final String TYPE_ID = "testTypeId";
    private static final String TYPE_ID_STRING = "'testTypeId'";
    private static final String TYPE_ID_NUMBER = "123";

    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolver;
    private JsonDeserializer<Object> jsonDeserializer;
    private DeserializationContext context;
    private JsonParser jsonParser;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        typeIdResolver = mock(TypeIdResolver.class);
        jsonDeserializer = mock(JsonDeserializer.class);
        context = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);

        when(context.getFactory()).thenReturn(objectMapper.getFactory());
        when(context.getTypeFactory()).thenReturn(objectMapper.getTypeFactory());
        when(context.findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.any(BeanProperty.class)))
                .thenReturn(jsonDeserializer);
        when(typeIdResolver.resolveTypeId(Mockito.any(DeserializationContext.class), Mockito.any(Class.class)))
                .thenReturn(TYPE_ID);
        when(typeIdResolver.getTypeFromId(Mockito.any(DeserializationContext.class), Mockito.anyString()))
                .thenReturn(new TypeReference<JavaType>() {}.getType(getClassLoader()));

        typeDeserializerBase = new TypeDeserializerBase(TypeReference.type(getClass()), typeIdResolver, null, false,
                TypeReference.type(getClass())) {

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                return jsonDeserializer;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                    throws IOException {
                return NullifyingDeserializer.instance;
            }
        };
    }

    @Test
    public void forPropertyTest() throws Exception {
        BeanProperty beanProperty = new BeanProperty("testProperty", getClass(), getClass());
        TypeDeserializerBase typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(typeDeserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void getTypeInclusionTest() throws Exception {
        JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.As.EXISTING_PROPERTY("typeProperty");
        typeDeserializerBase = new TypeDeserializerBase(TypeReference.type(getClass()), typeIdResolver, "typeProperty",
                false, TypeReference.type(getClass())) {

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return jsonTypeInfo;
            }
        };
        typeDeserializerBase.getTypeInclusion();
    }

    @Test
    public void baseTypeNameTest() throws Exception {
        String result = typeDeserializerBase.baseTypeName();
        assertNotNull(result);
        assertEquals(CLASS_NAME, result);
    }

    @Test
    public void getPropertyNameTest() throws Exception {
        String result = typeDeserializerBase.getPropertyName();
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void getTypeIdResolverTest() throws Exception {
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(result);
        assertEquals(typeIdResolver, result);
    }

    @Test
    public void getDefaultImplTest() throws Exception {
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        assertNull(result);
    }

    @Test
    public void toStringTest() throws Exception {
        String result = typeDeserializerBase.toString();
        assertNotNull(result);
    }

    @Test
    public void findDeserializerTest() throws Exception {
        String typeId = TYPE_ID;
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);
        assertNotNull(result);
        assertEquals(jsonDeserializer, result);
    }

    @Test
    public void findDefaultImplDeserializerTest() throws Exception {
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNotNull(result);
        assertEquals(NullifyingDeserializer.instance, result);
    }

    @Test(expected = JsonMappingException.class)
    public void deserializeWithNativeTypeIdTest() throws Exception {
        when(jsonParser.getText()).thenReturn(TYPE_ID_STRING);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void deserializeWithNativeTypeIdTestWithNullTypeId() throws Exception {
        when(jsonParser.getText()).thenReturn(null);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
        fail("Expected JsonMappingException");
    }

    @Test
    public void deserializeWithNativeTypeIdTestWithTypeIdNumber() throws Exception {
        when(jsonParser.getText()).thenReturn(TYPE_ID_NUMBER);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, TYPE_ID_NUMBER);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void handleUnknownTypeIdTest() throws Exception {
        when(jsonParser.getText()).thenReturn(TYPE_ID);
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID, typeIdResolver, null);
        fail("Expected JsonMappingException");
    }

    @Test
    public void handleUnknownTypeIdTestWithNullTypeIdResolver() throws Exception {
        when(jsonParser.getText()).thenReturn(TYPE_ID);
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID, null, null);
        assertNull(result);
    }

    @Test
    public void handleUnknownTypeIdTestWithBaseType() throws Exception {
        JavaType baseType = new TypeReference<Class<Object>>() {}.getType(getClassLoader());
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID, typeIdResolver, baseType);
        assertNull(result);
    }

    @Test
    public void handleUnknownTypeIdTestWithBaseTypeClass() throws Exception {
        JavaType baseTypeClass = TypeReference.type(getClass());
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID, typeIdResolver, baseTypeClass);
        assertNull(result);
    }

    @Test
    public void handleUnknownTypeIdTestWithConcreteType() throws Exception {
        JavaType concreteType = TypeReference.type(getClass()).getRawType();
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID, typeIdResolver, concreteType);
        assertNull(result);
    }

    @Test
    public void handleUnknownTypeIdTestWithConcreteTypeClass() throws Exception {
        JavaType concreteTypeClass = new TypeReference<Class<Object>>() {}.getType(getClassLoader());
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID, typeIdResolver, concreteTypeClass);
        assertNull(result);
    }

    @Test
    public void handleUnknownTypeIdTestWithCustomTypeIdResolver() throws Exception {
        when(typeIdResolver.resolveTypeId(context, Mockito.any(Class.class))).thenReturn("CustomTypeId");
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID, typeIdResolver, null);
        assertEquals(TypeReference.type(getClass()), result);
    }

    @Test
    public void handleUnknownTypeIdTestWithConcreteTypeAndCustomTypeIdResolver() throws Exception {
        when(typeIdResolver.getTypeFromId(context, "CustomTypeId"))
                .thenReturn(TypeReference.type(getClass()));
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID, typeIdResolver, null);
        assertEquals(TypeReference.type(getClass()), result);
    }

}