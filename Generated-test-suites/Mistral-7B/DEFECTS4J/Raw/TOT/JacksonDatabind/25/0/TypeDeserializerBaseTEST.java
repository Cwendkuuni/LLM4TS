package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        typeDeserializerBase = mapper.getTypeFactory().constructValueDeserializer(javaType);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty beanProperty = new BeanProperty("testProperty", String.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(forProperty);
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY, forProperty.getTypeInclusion());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals("testProperty", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = mapper.getTypeFactory().constructValueDeserializer(JavaType.of(MyClass.class));
        typeDeserializerBase._deserializers.put(typeId, deserializer);
        JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId);
        assertEquals(deserializer, foundDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        assertTrue(typeDeserializerBase._findDefaultImplDeserializer(mapper.getDeserializationContext()) instanceof NullifyingDeserializer);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        String typeId = "unknownTypeId";
        typeDeserializerBase._handleUnknownTypeId(mapper.getDeserializationContext(), typeId, null, null);
    }

    private static class MyClass {}
}