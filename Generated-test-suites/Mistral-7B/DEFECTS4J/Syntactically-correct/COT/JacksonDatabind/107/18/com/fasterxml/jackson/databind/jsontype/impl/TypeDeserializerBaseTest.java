package com.example.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        JavaType javaTypeInt = objectMapper.getTypeFactory().constructType(int.class);
        JavaType javaTypeString = objectMapper.getTypeFactory().constructType(String.class);
        typeDeserializerBase = new TypeDeserializerBase(javaTypeInt, null, null, false, javaTypeString);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty beanProperty = new BeanProperty("testProperty", int.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertEquals(TypeDeserializer.None.class, typeDeserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("int", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals("", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertTrue(typeDeserializerBase.getDefaultImpl() instanceof Class);
        assertEquals(String.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindDeserializerNullTypeId() throws Exception {
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        assertNull(typeDeserializerBase._findDeserializer(deserializationContext, null));
    }

    @Test
    public void testFindDeserializerValidTypeId() throws Exception {
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        String typeId = "com.example.TestClass";
        JsonDeserializer<Object> jsonDeserializer = new TestJsonDeserializer();
        typeDeserializerBase._deserializers.put(typeId, jsonDeserializer);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(deserializationContext, typeId);
        assertEquals(jsonDeserializer.getClass(), deserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        typeDeserializerBase._handleUnknownTypeId(deserializationContext, "invalidTypeId");
    }

    @Test
    public void testFindDefaultImplDeserializerNull() throws Exception {
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(deserializationContext));
    }

    @Test
    public void testFindDefaultImplDeserializerNotNull() throws Exception {
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> jsonDeserializer = new TestJsonDeserializer();
        typeDeserializerBase._defaultImplDeserializer = jsonDeserializer;
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);
        assertEquals(jsonDeserializer.getClass(), deserializer.getClass());
    }

    @Test
    public void testDeserializeWithNativeTypeIdNull() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeWithNativeTypeIdValid() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"" + TypeDeserializerBase.class.getName() + "\"]");
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext()).getClass());
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeId() throws Exception {
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        typeDeserializerBase._handleMissingTypeId(deserializationContext, "missingTypeId");
    }

    static class TestJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }
}