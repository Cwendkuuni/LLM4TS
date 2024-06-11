
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext deserializationContext;
    @Mock
    private JsonDeserializer jsonDeserializer;
    @Mock
    private JavaType javaType;
    private TypeDeserializerBase typeDeserializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        typeDeserializer = new TypeDeserializerBase(javaType, mock(TypeIdResolver.class),
                "testProperty", true, Object.class) {
            @Override
            public com.fasterxml.jackson.databind.type.TypeDeserializer forProperty(com.fasterxml.jackson.databind.BeanProperty prop) {
                return null;
            }

            @Override
            public com.fasterxml.jackson.annotation.JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        Map<String, JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>();
        deserializers.put("TestType", jsonDeserializer);
        typeDeserializer._deserializers = deserializers;
    }

    @Test
    void testBaseTypeName() {
        assertEquals("java.lang.Object", typeDeserializer.baseTypeName());
    }

    @Test
    void testGetPropertyName() {
        assertEquals("testProperty", typeDeserializer.getPropertyName());
    }

    @Test
    void testGetTypeIdResolver() {
        assertNotNull(typeDeserializer.getTypeIdResolver());
    }

    @Test
    void testGetDefaultImpl() {
        assertEquals(Object.class, typeDeserializer.getDefaultImpl());
    }

    @Test
    void test_findDeserializer() throws IOException {
        when(jsonDeserializer.deserialize(any(), eq(deserializationContext))).thenReturn("TestObject");
        assertEquals("TestObject", typeDeserializer._findDeserializer(deserializationContext, "TestType").deserialize(null, deserializationContext));
    }

    @Test
    void test_handleUnknownTypeId() throws IOException {
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.getDescForKnownTypeIds()).thenReturn("known1, known2");
        JsonMappingException exception = assertThrows(JsonMappingException.class,
                () -> typeDeserializer._handleUnknownTypeId(deserializationContext, "UnknownType", typeIdResolver, javaType));
        assertTrue(exception.getMessage().contains("known type ids = known1, known2"));
    }
}
