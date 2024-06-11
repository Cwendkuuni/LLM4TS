package com.example; // replace with your package name

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        BeanProperty property = new BeanProperty.Std("propertyName", String.class);
        TypeDeserializerBase newDeserializer = deserializer.forProperty(property);
        assertEquals(String.class, newDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, deserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        assertEquals("java.lang.String", deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        assertEquals("propertyName", deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        assertNotNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testFindDeserializerNullTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, null);
        assertNull(deser);
    }

    @Test
    public void testFindDeserializerInvalidTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        String invalidTypeId = "com.example.InvalidType";
        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, invalidTypeId);
        assertNull(deser);
    }

    @Test
    public void testFindDeserializerValidTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapperForTest = mapper.copy();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        DeserializationContext ctxt = mapperForTest.getDeserializationContext();
        JavaType javaType = mapperForTest.getTypeFactory()
                .constructType(String.class, String.class.getName());
        ctxt.addValueHandler(new StdScalarValueHandlerFactory().findValueHandler(javaType));
        String validTypeId = "com.example.StringType";
        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, validTypeId);
        assertNotNull(deser);
        assertTrue(deser instanceof com.fasterxml.jackson.databind.deser.std.StringDeserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullContext() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        deserializer._findDeserializer(null, "testTypeId");
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        JsonDeserializer<Object> deser = deserializer._findDefaultImplDeserializer(null);
        assertNull(deser);
    }

    @Test
    public void testFindDefaultImplDeserializerWithContext() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Object> deser = deserializer._findDefaultImplDeserializer(ctxt);
        assertNotNull(deser);
        assertTrue(deser instanceof NullifyingDeserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = ctxt.getTypeFactory().constructType(String.class, "com.example.TestClass");
        Object result = deserializer._handleUnknownTypeId(ctxt, javaType.getName());
        assertNull(result);
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class, String.class, null, true, null);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = ctxt.getTypeFactory().constructType(String.class, null);
        Object result = deserializer._handleMissingTypeId(ctxt, "Missing type id");
        assertNull(result);
    }

}