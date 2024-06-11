package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JavaType javaType;

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        typeDeserializerBase = new TypeDeserializerBase(javaType, mock(TypeIdResolver.class), "typePropertyName", true, TypeDeserializerBase.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        Assert.assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        Assert.assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        Assert.assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        Assert.assertEquals(null, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        Assert.assertNotNull(typeDeserializerBase.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        // Test when typeId is null
        JsonParseException exception = Assert.assertThrows(
                JsonParseException.class,
                () -> typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), deserializationContext)
        );
        Assert.assertEquals("No (native) type id found when one was expected for polymorphic type handling", exception.getMessage());

        // Test when typeId is not null
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getTypeId()).thenReturn("testTypeId");
        JsonMappingException exception2 = Assert.assertThrows(
                JsonMappingException.class,
                () -> typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext)
        );
        Assert.assertEquals("No (native) type id found when one was expected for polymorphic type handling", exception2.getMessage());
    }

    @Test
    public void testDeserializeWithNativeTypeId2() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getTypeId()).thenReturn("testTypeId");

        JsonDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
        when(typeDeserializerBase._findDefaultImplDeserializer(deserializationContext)).thenReturn(nullifyingDeserializer);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId3() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getTypeId()).thenReturn("testTypeId");

        Map<String, JsonDeserializer<Object>> deserializers = new HashMap<>();
        deserializers.put("testTypeId", mock(JsonDeserializer.class));
        when(typeDeserializerBase._findDeserializer(deserializationContext, "testTypeId")).thenReturn(deserializers.get("testTypeId"));
        when(typeDeserializerBase._deserializers).thenReturn(deserializers);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);
        Assert.assertNotNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        JsonParseException exception = Assert.assertThrows(
                JsonParseException.class,
                () -> typeDeserializerBase._handleUnknownTypeId(deserializationContext, "unknownTypeId", mock(TypeIdResolver.class), javaType)
        );
        Assert.assertEquals("Could not resolve type id 'unknownTypeId' into a subtype of com.fasterxml.jackson.databind.JavaType: known type ids are not statically known",
                exception.getMessage());
    }

}