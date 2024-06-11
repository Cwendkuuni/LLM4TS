package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link TypeDeserializerBase}
 */
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        JavaType baseType = JavaType.constructFromClass(String.class);
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, false, null);
    }

    @Test
    public void testForProperty() throws JsonProcessingException {
        BeanProperty property = new BeanProperty("name", String.class);
        TypeDeserializerBase newTypeDeserializer = typeDeserializer.forProperty(property);
        assertNotNull(newTypeDeserializer);
        assertEquals(String.class, newTypeDeserializer.getDefaultImpl());
    }

    @Test(expected = IllegalStateException.class)
    public void testForPropertyNullProperty() {
        typeDeserializer.forProperty(null);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals(String.class.getName(), typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver.getClass(), typeDeserializer.getTypeIdResolver().getClass());
    }

    @Test
    public void testDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        String expected = "[TypeDeserializerBase; base-type:java.lang.String; id-resolver:class com.fasterxml.jackson.databind.DefaultTypeIdResolver]";
        assertEquals(expected, typeDeserializer.toString());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        String typeId = "TestType";
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        typeDeserializer._deserializers.put(typeId, deserializer);

        JsonDeserializer<Object> actual = typeDeserializer._findDeserializer(ctxt, typeId);
        assertEquals(deserializer, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerNullTypeId() {
        typeDeserializer._findDeserializer(null, null);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Class<String> defaultImpl = String.class;

        JsonDeserializer<Object> actual = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNull(actual);
    }

    @Test
    public void testFindDefaultImplDeserializerNotNull() throws IOException {
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Class<String> defaultImpl = String.class;
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        synchronized (defaultImpl) {
            typeDeserializer._defaultImplDeserializer = deserializer;
        }

        JsonDeserializer<Object> actual = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertEquals(deserializer, actual);
    }

}

class DefaultTypeIdResolver implements TypeIdResolver {

}