package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeUtils;

import org.junit.Test;

/**
 * Test class for AsPropertyTypeDeserializer
 */
public class AsPropertyTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAsPropertyTypeDeserializerConstructor() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl, As.PROPERTY);
        assertNotNull(deserializer);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testForProperty() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(new AsPropertyTypeDeserializer(new AsPropertyTypeDeserializer(null, null), null), null);
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AsPropertyTypeDeserializer newDeserializer = deserializer.forProperty(property);
        assertEquals(deserializer, newDeserializer);
    }

    @Test
    public void testDeserializeTypedFromObject_NoTypeId() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"test\":\"Hello\"}");
        Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("Hello", result);
    }

    @Test
    public void testDeserializeTypedFromObject_WithTypeId() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"type\":\"String\",\"test\":\"Hello\"}");
        Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("Hello", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObject_InvalidJson() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"test\":\"Hello\"");
        deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"type\":\"String\",\"test\":\"Hello\"}");
        Object result = deserializer._deserializeTypedForId(parser, mapper.getDeserializationContext(), null);
        assertNotNull(result);
        assertEquals("Hello", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"test\":123}");
        Object result = deserializer._deserializeTypedUsingDefaultImpl(parser, mapper.getDeserializationContext(), null);
        assertNotNull(result);
        assertEquals(123, result);

        parser = mapper.getFactory().createParser("{\"test\":\"123\"}");
        result = deserializer._deserializeTypedUsingDefaultImpl(parser, mapper.getDeserializationContext(), null);
        assertNotNull(result);
        assertEquals(123, result);

        parser = mapper.getFactory().createParser("{\"test\":[1,2,3]}");
        result = deserializer._deserializeTypedUsingDefaultImpl(parser, mapper.getDeserializationContext(), null);
        assertNotNull(result);
        assertTrue(result instanceof List);
        List list = (List) result;
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WithTokenBuffer() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"test\":[1,2,3]}");
        Object result = deserializer._deserializeTypedUsingDefaultImpl(parser, mapper.getDeserializationContext(), new TokenBuffer(parser, mapper.getDeserializationContext()));
        assertNotNull(result);
        assertTrue(result instanceof List);
        List list = (List) result;
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedUsingDefaultImpl_InvalidJson() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser("{\"test\":}");
        deserializer._deserializeTypedUsingDefaultImpl(parser, mapper.getDeserializationContext(), null);
    }
}