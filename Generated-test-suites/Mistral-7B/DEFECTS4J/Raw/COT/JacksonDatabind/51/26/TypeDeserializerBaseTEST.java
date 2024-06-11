package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    private static final String BASE_TYPE_NAME = "java.lang.String";
    private static final String TYPE_PROPERTY_NAME = "type";
    private static final boolean TYPE_ID_VISIBLE = true;
    private static final JavaType DEFAULT_IMPL = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);

    @Mock
    private JsonParser jsonParser;
    @Mock
    private DeserializationContext deserializationContext;
    @Mock
    private TypeIdResolver typeIdResolver;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        when(deserializationContext.findContextualValueDeserializer(eq(Object.class), any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));
        when(deserializationContext.isEnabled(eq(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE))).thenReturn(false);
        typeDeserializerBase = new TypeDeserializerBase(ObjectMapper.getDefaultInstance().getTypeFactory().constructType(TypeDeserializerBase.class), typeIdResolver, TYPE_PROPERTY_NAME, TYPE_ID_VISIBLE, DEFAULT_IMPL);
    }

    @Test
    public void testForProperty() {
        BeanProperty beanProperty = mock(BeanProperty.class);
        TypeDeserializer expectedTypeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializerBase.forProperty(beanProperty)).thenReturn(expectedTypeDeserializer);
        assertEquals(expectedTypeDeserializer, typeDeserializerBase.forProperty(beanProperty));
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals(BASE_TYPE_NAME, typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals(TYPE_PROPERTY_NAME, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertEquals(DEFAULT_IMPL.getRawClass(), typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        String expectedToString = "[TypeDeserializerBase; base-type:java.lang.String; id-resolver:";
        assertEquals(expectedToString, typeDeserializerBase.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testForPropertyWithNullProperty() {
        BeanProperty beanProperty = null;
        typeDeserializerBase.forProperty(beanProperty);
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws JsonProcessingException {
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializerBase._deserializers.get(typeId)).thenReturn(deserializer);
        Object result = typeDeserializerBase._findDeserializer(deserializationContext, typeId);
        assertEquals(deserializer, result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithInvalidTypeId() throws IOException {
        typeDeserializerBase._findDeserializer(deserializationContext, "InvalidTypeId");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        Class<?> rawDefaultImpl = DEFAULT_IMPL.getRawClass();
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        synchronized (DEFAULT_IMPL) {
            when(deserializationContext.findContextualValueDeserializer(eq(rawDefaultImpl), any(BeanProperty.class))).thenReturn(deserializer);
            when(typeDeserializerBase._defaultImplDeserializer).thenReturn(null);
            typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);
            verify(typeDeserializerBase, times(1))._defaultImplDeserializer = deserializer;
        }
    }

    @Test
    public void testHandleUnknownTypeIdWithKnownTypeIdResolver() throws IOException {
        when(typeIdResolver.getTypeIdFromValue(anyObject())).thenReturn("com.example.MyClass");
        JavaType type = typeDeserializerBase._handleUnknownTypeId(deserializationContext, "UnknownTypeId", typeIdResolver, null);
        assertEquals("com.example.MyClass", type.getName());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdWithUnknownTypeIdResolver() throws IOException {
        JavaType type = typeDeserializerBase._handleUnknownTypeId(deserializationContext, "UnknownTypeId", null, null);
        assertEquals(null, type);
    }
}