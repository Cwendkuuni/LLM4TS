package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.As;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

/**
 * Unit tests for {@link AsPropertyTypeDeserializer}.
 */
public class AsPropertyTypeDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testConstructor1() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testConstructor2() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class);
        As inclusion = As.PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        assertEquals(inclusion, deserializer.getTypeInclusion());
    }

    @Test
    public void testConstructor3() throws Exception {
        AsPropertyTypeDeserializer src = new AsPropertyTypeDeserializer(mapper, "propertyName", String.class, true, Integer.class, As.PROPERTY);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(src, null);
        assertEquals(src.getTypeInclusion(), deserializer.getTypeInclusion());
    }

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = mapper.findPropertyOffset("propertyName", String.class);
        AsPropertyTypeDeserializer deserializerForProperty = deserializer.forProperty(property);
        assertEquals(deserializer, deserializerForProperty);
    }

    @Test
    public void testDeserializeTypedFromObject_TypeIdVisible() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JsonParser jp = mapper.getFactory().createParser("{\"propertyName\":{\"type\":\"Integer\",\"value\":42}}");
        Object result = deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
        assertEquals(Integer.class, result.getClass());
        assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromObject_TypeIdNotVisible() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JsonParser jp = mapper.getFactory().createParser("{\"propertyName\":{\"type\":\"Integer\",\"value\":42}}");
        Object result = deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
        assertEquals(String.class, result.getClass());
        assertTrue(result instanceof AbstractValue);
        assertEquals("{\"type\":\"Integer\",\"value\":42}", mapper.writerWithView(new ObjectWriter(mapper.getFactory())).writeValueAsString(result));
    }

    @Test
    public void testDeserializeTypedFromObject_NoTypeId() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        JavaType
 defaultImpl = mapper.getTypeFactory().constructType(Integer.class);
AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

JsonParser jp = mapper.getFactory().createParser("{\"propertyName\":42}"); Object result = deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext()); assertEquals(String.class, result.getClass()); assertTrue(result instanceof AbstractValue); assertEquals("{\"propertyName\":42}", mapper.writerWithView(new ObjectWriter(mapper.getFactory())).writeValueAsString(result)); }

@Test public void testDeserializeTypedFromObject_MissingProperty() throws Exception { JsonParser jp = mapper.getFactory().createParser("{\"value\":42}"); AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper, "propertyName", String.class, true, Integer.class, As.PROPERTY); try { deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext()); fail(); } catch (IOException e) { assertEquals("missing property 'propertyName' that is to contain type id (for class java.lang.String)", e.getMessage()); } }

@Test public void testDeserializeTypedFromObject_WrongPropertyName() throws Exception { JsonParser jp = mapper.getFactory().createParser("{\"anotherPropertyName\":{\"type\":\"Integer\",\"value\":42}}"); AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(mapper, "propertyName", String.class, true, Integer.class, As.PROPERTY); try { deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext()); fail(); } catch (IOException e) { assertEquals("missing property 'propertyName' that is to contain type id (for class java.lang.String)", e.getMessage()); } }

@Test public void testDeserializeTypedForId() throws Exception { JavaType baseType = mapper.getTypeFactory().constructType(String.class); TypeIdResolver idResolver = mapper.getTypeFactory(); String typePropertyName = "type"; boolean typeIdVisible = false; JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class); AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

JsonParser jp = mapper.getFactory().createParser("{\"propertyName\":{\"type\":\"Integer\",\"value\":42}}"); Object result = deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext()); TokenBuffer tb = new TokenBuffer(jp, mapper.getDeserializationContext()); deserializer.deserializeTypedForId(jp, mapper.getDeserializationContext(), tb); assertEquals("{\"type\":\"Integer\",\"value\":42}", tb.asText()); }

@Test public void testDeserializeTypedUsingDefaultImpl() throws Exception { JavaType baseType = mapper.getTypeFactory().constructType(String.class); TypeIdResolver idResolver = mapper.getTypeFactory(); String typePropertyName = "type"; boolean typeIdVisible = false; JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class); AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

JsonParser jp = mapper.getFactory().createParser("{\"value\":42}"); Object result = deserializer.deserializeTypedUsingDefaultImpl(jp, mapper.getDeserializationContext(), null); assertEquals(Integer.class, result.getClass()); assertEquals(42, result); }

@Test public void testDeserializeTypedUsingDefaultImpl_WithTokenBuffer() throws Exception { JavaType baseType = mapper.getTypeFactory().constructType(String.class); TypeIdResolver idResolver = mapper.getTypeFactory(); String typePropertyName = "type"; boolean typeIdVisible = false; JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class); AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

JsonParser jp = mapper.getFactory().createParser("{\"value\":42}"); TokenBuffer tb = new TokenBuffer(jp, mapper.getDeserializationContext()); Object result = deserializer.deserializeTypedUsingDefaultImpl(jp, mapper.getDeserializationContext(), tb); assertEquals("{\"value\":42}", tb.asText()); }

@Test public void testDeserializeTypedUsingDefaultImpl_WithTokenBufferAndTypeId() throws Exception { JavaType baseType = mapper.getTypeFactory().constructType(String.class); TypeIdResolver idResolver = mapper.getTypeFactory(); String typePropertyName = "type"; boolean typeIdVisible = false; JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class); AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

JsonParser jp = mapper.getFactory().createParser("{\"type\":\"Integer\",\"value\":42}"); TokenBuffer tb = new TokenBuffer(jp, mapper.getDeserializationContext()); Object result = deserializer.deserializeTypedUsingDefaultImpl(jp, mapper.getDeserializationContext(), tb); assertEquals("{\"type\":\"Integer\",\"value\":42}", tb.asText()); }

@Test public void testDeserializeTypedFromAny() throws Exception { JavaType baseType = mapper.getTypeFactory().constructType(String.class); TypeIdResolver idResolver = mapper.getTypeFactory(); String typePropertyName = "type"; boolean typeIdVisible = false; JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class); AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

JsonParser jp = mapper.getFactory().createParser("{\"value\":42}"); Object result = deserializer.deserializeTypedFromAny(jp, mapper.getDeserializationContext()); assertEquals(Integer.class, result.getClass()); assertEquals(42, result); }

@Test public void testDeserializeTypedFromArray() throws Exception { JavaType baseType = mapper.getTypeFactory().constructType(List.class); TypeIdResolver idResolver = mapper.getTypeFactory(); String typePropertyName = "type"; boolean typeIdVisible = false; JavaType defaultImpl = mapper.getTypeFactory().constructType(ArrayList.class); AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

JsonParser jp = mapper.getFactory().createParser("[1, 2, 3]"); Object result = deserializer.deserializeTypedFromArray(jp, mapper.getDeserializationContext()); assertEquals(ArrayList.class, result.getClass()); assertTrue(result instanceof AbstractList); assertEquals(3, ((AbstractList) result).size()); }

@Test public void testDeserializeTypedFromScalar() throws Exception { JavaType baseType = mapper.getTypeFactory().constructType(String.class); TypeIdResolver idResolver = mapper.getTypeFactory(); String typePropertyName = "type"; boolean typeIdVisible = false; JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class); AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

JsonParser jp = mapper.getFactory().createParser("42"); Object result = deserializer.deserializeTypedFromScalar(jp, mapper.getDeserializationContext()); assertEquals(Integer.class, result.getClass()); assertEquals(42, result); }
}