package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private TypeDeserializerBase typeDeserializerBase;
    private JsonNode jsonNode;

    @Before
    public void beforeEach() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        typeDeserializerBase = new TypeDeserializerBase(
                TypeFactory.defaultInstance.constructType(Object.class), typeIdResolver, "typePropertyName", true, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
        jsonNode = mapper.readTree("{}");
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty beanProperty = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expectedResult = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.Object; id-resolver:null]";
        assertEquals(expectedResult, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "myTypeId";
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        when(typeIdResolver.typeFromId(any(DeserializationContext.class), eq(typeId))).thenReturn(mock(JavaType.class));
        when(typeIdResolver.typeFromId(any(DeserializationContext.class), eq("myTypeId"))).thenReturn(mock(JavaType.class));
        when(typeIdResolver.resolveVisibleType(any(DeserializationContext.class), eq("myTypeId"), any(JavaType.class)))
                .thenReturn(mock(JavaType.class));
        when(typeIdResolver.resolveVisibleType(any(DeserializationContext.class), eq("myTypeId"), any(JavaType.class)))
                .thenReturn(mock(JavaType.class));
        when(jsonDeserializer.getClass()).thenReturn(JsonDeserializer.class);
        when(typeIdResolver.findDeserializer(any(DeserializationContext.class), any(JavaType.class)))
                .thenReturn(jsonDeserializer);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), typeId);
        assertSame(jsonDeserializer, deserializer);

        verify(typeIdResolver).typeFromId(mock(DeserializationContext.class), eq("myTypeId"));
        verify(typeIdResolver).typeFromId(mock(DeserializationContext.class), eq("myTypeId"));
        verify(typeIdResolver).resolveVisibleType(mock(DeserializationContext.class), eq("myTypeId"), any(JavaType.class));
        verify(typeIdResolver).resolveVisibleType(mock(DeserializationContext.class), eq("myTypeId"), any(JavaType.class));
        verify(typeIdResolver).findDeserializer(mock(DeserializationContext.class), any(JavaType.class));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);

        when(jsonDeserializer.getClass()).thenReturn(JsonDeserializer.class);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class));

        assertNull(deserializer);

        verify(jsonDeserializer).getClass();
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdWithNoTypeIdResolver() throws Exception {
        String typeId = "myTypeId";
        when(typeDeserializerBase.getTypeIdResolver()).thenReturn(null);

        typeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), typeId, any(TypeIdResolver.class), any(JavaType.class));
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        String typeId = "myTypeId";
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        when(jsonDeserializer.getClass()).thenReturn(JsonDeserializer.class);
        when(typeDeserializerBase.getTypeIdResolver()).thenReturn(typeIdResolver);

        typeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), typeId, any(TypeIdResolver.class), any(JavaType.class));

        verify(jsonDeserializer).getClass();
        verify(typeIdResolver).typeFromId(mock(DeserializationContext.class), eq(typeId));
        verify(typeIdResolver, never()).findDeserializer(any(DeserializationContext.class), any(JavaType.class));
        verifyNoMoreInteractions(jsonDeserializer, typeIdResolver);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String jsonString = "{\"typeId\":\"com.example.MyClass\", \"value\":1}";
        ObjectReader reader = mapper.readerForTree(TypeDeserializerBase.class);

        Object result = reader.readValue(jsonNode);

        assertEquals(TypeDeserializerBase.class, result.getClass());

        JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
        Object deserializedResult = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mock(DeserializationContext.class));

        assertEquals(TypeDeserializerBase.class, deserializedResult.getClass());
    }
}