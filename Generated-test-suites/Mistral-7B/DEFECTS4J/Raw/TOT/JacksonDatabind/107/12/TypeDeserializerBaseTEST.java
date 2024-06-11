package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseType() throws Exception {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseBaseTest.MyTypeDeserializerBase(
                JsonFactory.instance.constructType(java.util.Map.class),
                TypeIdResolverBase.getInstance(),
                "typePropertyName",
                true,
                JsonFactory.instance.constructType(java.util.HashMap.class));

        assertEquals("java.util.Map", typeDeserializer.baseTypeName());
    }

    @Test
    public void testForProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        BeanProperty property = new BeanProperty(TypeDeserializerBase.class, "property");
        TypeDeserializerBase forPropertyTypeDeserializer = typeDeserializer.forProperty(property);
        assertEquals(TypeDeserializerBase.class, forPropertyTypeDeserializer.getClass());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, typeId);

        assertNotNull(deserializer);

        // Test with null typeId
        deserializer = typeDeserializer._findDeserializer(ctxt, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);

        assertNotNull(deserializer);

        // Test with null context
        deserializer = typeDeserializer._findDefaultImplDeserializer(null);
        assertNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);

        String json = "{\"typeId\":\"java.util.HashMap\",\"value\":1}";
        JsonParser jp = mapper.getFactory().createParser(json);

        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext(), "java.util.HashMap");

        assertTrue(result instanceof Map);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);

        String json = "{\"typeId\":\"UnknownType\"}";
        JsonParser jp = mapper.getFactory().createParser(json);

        typeDeserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext(), "UnknownType");
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String typeId = "UnknownType";
        JavaType result = typeDeserializer._handleUnknownTypeId(ctxt, typeId);

        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMissingTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String extraDesc = "Missing type id";
        JavaType result = typeDeserializer._handleMissingTypeId(ctxt, extraDesc);

        fail("Should have thrown JsonMappingException");
    }
}