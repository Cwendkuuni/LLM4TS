package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    private static final String DEFAULT_IMPL_CLASS_NAME = "java.lang.String";
    private static final String BASE_TYPE_NAME = "com.example.MyBaseClass";
    private static final String TYPE_ID = "MyTypeId";

    private ObjectMapper objectMapper;
    private DeserializationContext context;
    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolver;
    private BeanProperty beanProperty;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        objectMapper = mock(ObjectMapper.class);
        context = mock(DeserializationContext.class);
        typeDeserializerBase = mock(TypeDeserializerBase.class);
        typeIdResolver = mock(TypeIdResolver.class);
        beanProperty = mock(BeanProperty.class);

        when(objectMapper.findValueDeserializer(any(Class.class))).thenReturn(null);
        when(typeDeserializerBase.forProperty(beanProperty)).thenReturn(typeDeserializerBase);
        when(typeDeserializerBase.getTypeIdResolver()).thenReturn(typeIdResolver);
        when(typeIdResolver.typeFromId(context, anyString())).thenAnswer(invocation -> null);
    }

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                baseType, typeIdResolver, null, false, null);

        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.forProperty(beanProperty).getClass());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        assertEquals(BASE_TYPE_NAME, typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals("", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        when(baseType.isContainerType()).thenReturn(false);
        when(baseType.getRawClass().isAssignableFrom(Class.forName(DEFAULT_IMPL_CLASS_NAME))).thenReturn(true);

        JavaType defaultImplType = mock(JavaType.class);
        when(defaultImplType.getRawClass()).thenReturn(Class.forName(DEFAULT_IMPL_CLASS_NAME));

        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, null, false, defaultImplType);

        assertEquals(Class.forName(DEFAULT_IMPL_CLASS_NAME), typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expected = "[TypeDeserializerBase; base-type:com.example.MyBaseClass; id-resolver:....]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer_NullTypeId() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializerBase._deserializers.get(eq(TYPE_ID))).thenReturn(deserializer);

        Object result = typeDeserializerBase._findDeserializer(context, TYPE_ID);

        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDeserializer_NullTypeId_NullResult() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        JsonDeserializer<Object> deserializer = null;
        when(typeDeserializerBase._deserializers.get(eq(TYPE_ID))).thenReturn(deserializer);

        assertNull(typeDeserializerBase._findDeserializer(context, TYPE_ID));
    }

    @Test
    public void testFindDeserializer_NonExistentTypeId() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));
        when(typeIdResolver.typeFromId(context, TYPE_ID)).thenReturn(null);

        assertNull(typeDeserializerBase._findDeserializer(context, TYPE_ID));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(objectMapper.findValueDeserializer(eq(Class.forName(DEFAULT_IMPL_CLASS_NAME)))).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer_NullResult() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        when(objectMapper.findValueDeserializer(eq(Class.forName(DEFAULT_IMPL_CLASS_NAME)))).thenReturn(null);

        assertNull(typeDeserializerBase._findDefaultImplDeserializer(context));
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(null, context, null);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId_NullResult() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(null, context, null);

        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeId_UnknownTypeId() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        when(typeIdResolver.typeFromId(context, TYPE_ID)).thenReturn(null);

        typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), context, TYPE_ID);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        when(baseType.getRawClass()).thenReturn(Class.forName(BASE_TYPE_NAME));

        String expectedMessage = "No (native) type id found when one was expected for polymorphic type handling";

        try {
            typeDeserializerBase._handleUnknownTypeId(context, TYPE_ID, typeIdResolver, baseType);
        } catch (IOException e) {
            assertEquals(expectedMessage, e.getMessage());
            throw e;
        }
    }
}