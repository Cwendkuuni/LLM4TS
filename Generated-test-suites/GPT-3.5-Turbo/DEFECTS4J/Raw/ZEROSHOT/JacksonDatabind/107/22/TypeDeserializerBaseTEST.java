package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JsonDeserializer<Object> jsonDeserializer;

    @Mock
    private TypeIdResolver typeIdResolver;

    @Mock
    private JavaType baseType;

    @Mock
    private BeanProperty beanProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "typePropertyName", true, baseType) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        String expected = "java.lang.String";
        String actual = typeDeserializerBase.baseTypeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeName() {
        String expected = "typePropertyName";
        String actual = typeDeserializerBase.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver expected = typeIdResolver;
        TypeIdResolver actual = typeDeserializerBase.getTypeIdResolver();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> expected = baseType.getRawClass();
        Class<?> actual = typeDeserializerBase.getDefaultImpl();
        assertEquals(expected, actual);
    }

    @Test
    public void testBaseType() {
        JavaType expected = baseType;
        JavaType actual = typeDeserializerBase.baseType();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;";
        String actual = typeDeserializerBase.toString();
        assertEquals(expected, actual.substring(0, 50));
    }

    @Test
    public void testFindDeserializerWhenDeserializerExists() throws IOException {
        String typeId = "testTypeId";
        typeDeserializerBase._deserializers.put(typeId, jsonDeserializer);

        JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(deserializationContext, typeId);

        assertEquals(jsonDeserializer, actual);
    }

    @Test
    public void testFindDeserializerWhenDeserializerDoesNotExistAndTypeIsNull() throws IOException {
        String typeId = "testTypeId";
        JavaType type = null;
        TypeDeserializerBase partialTypeDeserializerBase = mock(TypeDeserializerBase.class);

        Object expected = NullifyingDeserializer.instance;

        Object actual = typeDeserializerBase._findDeserializer(partialTypeDeserializerBase, typeId);

        assertEquals(expected, actual);
        verify(partialTypeDeserializerBase).deserialize(deserializationContext, typeId);
    }

    @Test
    public void testFindDeserializerWhenDeserializerDoesNotExist() throws IOException {
        String typeId = "testTypeId";
        JavaType type = mock(JavaType.class);
        typeDeserializerBase._idResolver = typeIdResolver;
        TypeDeserializerBase partialTypeDeserializerBase = mock(TypeDeserializerBase.class);

        typeDeserializerBase._findDeserializer(deserializationContext, typeId);
        verify(typeIdResolver).typeFromId(deserializationContext, typeId);
    }
}