package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer forProperty = deserializer.forProperty(property);
        assertEquals("Expected correct TypeDeserializer forProperty", TypeDeserializer.class, forProperty.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyWithNullProperty() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                null, null, "", true, null);
        deserializer.forProperty(null);
    }

    @Test
    public void testFindDeserializerWithNullContext() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                null, null, "", true, null);
        assertNull("expected null deserializer with null context", deserializer._findDeserializer(null, ""));
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        String typeId = "com.test.StringType";
        JsonDeserializer<Object> deserializerForTypeId = deserializer._findDeserializer(
                mapper.getDeserializationContext(), typeId);
        assertNotNull("expected non-null deserializer for valid typeId", deserializerForTypeId);
    }

    @Test(expected = NullPointerException.class)
    public void testFindDeserializerWithNullTypeId() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                null, null, "", true, null);
        deserializer._findDeserializer(null, null);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullContext() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                null, null, "", true, null);
        assertNull("expected null deserializer with null context", deserializer._findDefaultImplDeserializer(null));
    }

    @Test
    public void testFindDefaultImplDeserializerWithContext() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        JsonDeserializer<Object> deserializerForDefaultImpl = deserializer._findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertNotNull("expected non-null deserializer for default impl", deserializerForDefaultImpl);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        String json = "{\"typeId\":\"com.test.StringType\", \"value\":\"test\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = deserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext());
        assertEquals("Expected correct deserialized object", "test", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullContext() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        String json = "{\"typeId\":\"com.test.StringType\", \"value\":\"test\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer._deserializeWithNativeTypeId(jp, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullJson() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        JsonParser jp = mapper.getFactory().createParser(null);
        deserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithEmptyJson() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        String json = "{}";
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnknownTypeId() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        String json = "{\"typeId\":\"com.test.UnknownType\", \"value\":\"test\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        deserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        String json = "{\"typeId\":\"com.test.UnknownType\"}";
        Object result = deserializer._handleUnknownTypeId(mapper.getDeserializationContext(), "com.test.UnknownType",
                mapper.getPolymorphicTypeIdResolver(), mapper.getTypeFactory().constructType(Object.class));
        assertNull("expected null result from _handleUnknownTypeId", result);
    }

    @Test
    public void testToString() {
        JsonFactory jf = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jf);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(Object.class),
                mapper.getPolymorphicTypeIdResolver(), "",
                true, mapper.getTypeFactory().constructType(String.class));

        String result = deserializer.toString();
        String expected = "[TypeDeserializerBase; base-type:java.lang.Object; id-resolver:com.fasterxml.jackson.databind.JsonDeserializer$NullingDeserializer@2665d46b; ]";
        assertEquals("Expected correct TypeDeserializerBase toString", expected, result);
    }
}