package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.test.BaseMapTest;

/**
 * Test case for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest extends BaseMapTest {

    private static final String TYPE_ID_PROPERTY = "typeId";
    private static final String TYPE_ID = "com.example.MyType";

    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        JsonTypeInfo.Default typeInfo = new JsonTypeInfo.Default();
        typeDeserializerBase = new TypeDeserializerBase(
                objectMapper.getTypeFactory().constructType(JsonDeserializer.class),
                null, TYPE_ID_PROPERTY, true, null) {

            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return new TestTypeDeserializer();
            }
        };
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty.Std("property", String.class);
        TypeDeserializer expectedTypeDeserializer = new TestTypeDeserializer();
        TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertSame("Unexpected type deserializer", expectedTypeDeserializer, actualTypeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As expectedTypeInclusion = JsonTypeInfo.As.EXISTING_PROPERTY;
        JsonTypeInfo.As actualTypeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals("Unexpected type inclusion", expectedTypeInclusion, actualTypeInclusion);
    }

    @Test
    public void testBaseTypeName() throws IOException {
        String expectedBaseTypeName = TypeDeserializerBase.class.getName();
        String actualBaseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("Unexpected base type name", expectedBaseTypeName, actualBaseTypeName);
    }

    @Test
    public void testToString() throws IOException {
        String expectedToStringRepresentation = "[TypeDeserializerBase; base-type:com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; id-resolver:null]";
        String actualToStringRepresentation = typeDeserializerBase.toString();
        assertEquals("Unexpected toString representation", expectedToStringRepresentation, actualToStringRepresentation);
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        DeserializationContext context = objectMapper.getDeserializationContext();
        assertNull("Unexpected deserializer for null type id", typeDeserializerBase._findDeserializer(context, null));
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws IOException {
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> expectedDeserializer = new TestJsonDeserializer();
        context.addHandler(JsonDeserializer.None.class, expectedDeserializer);
        String unknownTypeId = "unknownTypeId";
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(context, unknownTypeId);
        assertSame("Unexpected deserializer for unknown type id", expectedDeserializer, actualDeserializer);
    }

    @Test
    public void testFindDeserializerWithDefaultImplTypeId() throws IOException {
        DeserializationContext context = objectMapper.getDeserializationContext();
        Class<?> defaultImplType = JsonDeserializer.None.class;
        JsonDeserializer<Object> expectedDeserializer = new TestJsonDeserializer();
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(context, null);
        assertSame("Unexpected deserializer for null type id", expectedDeserializer, actualDeserializer);
        assertEquals("Unexpected base type for defaultImplDeserializer", defaultImplType, actualDeserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> expectedDeserializer = new TestJsonDeserializer();
        context.addHandler(JsonDeserializer.None.class, expectedDeserializer);
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertSame("Unexpected deserializer for defaultImplDeserializer", expectedDeserializer, actualDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        DeserializationContext context = objectMapper.getDeserializationContext();
        String nativeTypeId = TYPE_ID;
        Object expectedObject = new TestObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.addHandler(new TestTypeIdHandler());
        JsonParser jsonParser = mapper.getFactory().createParser("{\"typeId\":\"" + nativeTypeId + "\", value: \"test\"}");
        Object actualObject = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
        assertSame("Unexpected deserialized object", expectedObject, actualObject);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeWithInvalidTypeId() throws IOException {
        DeserializationContext context = objectMapper.getDeserializationContext();
        String invalidTypeId = "invalidTypeId";
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"typeId\":\"" + invalidTypeId + "\", value: \"test\"}");
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext context = objectMapper.getDeserializationContext();
        String unknownTypeId = "unknownTypeId";
        JavaType expectedType = objectMapper.getTypeFactory().constructType(String.class);
        JavaType actualType = typeDeserializerBase._handleUnknownTypeId(context, unknownTypeId, null, null);
        assertSame("Unexpected type for unknown type id", expectedType, actualType);
    }

    private static class TestTypeDeserializer extends TypeDeserializer {

        @Override
        public JsonDeserializer<Object> createContextualDeserializer(DeserializationContext ctxt) throws IOException {
            return new TestJsonDeserializer();
        }

        @Override
        public Class<?> getType(DeserializationContext ctxt) {
            return String.class;
        }
    }

    private static class TestJsonDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Converter<String, TestObject> converter = ctxt.converter(new TypeReference<Converter<String, TestObject>>() {});
            TestObject testObject = converter.convertValue(jp.getValueAsString());
            return testObject;
        }

        @Override
        public boolean isCachable() {
            return true;
        }
    }

    private static class TestTypeIdHandler extends TypeIdHandlerBase {

        @Override
        public void handleTypeId(JsonParser jp, Map<String, String> hs) throws IOException {
            hs.put(TYPE_ID_PROPERTY, TYPE_ID);
        }
    }

    private static class TestObject {

        // empty class

    }
}