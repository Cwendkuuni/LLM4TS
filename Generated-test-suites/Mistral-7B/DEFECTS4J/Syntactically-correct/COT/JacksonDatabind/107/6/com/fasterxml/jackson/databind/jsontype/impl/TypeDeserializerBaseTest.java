package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase testee;
    private DeserializationContext context;

    @Before
    public void setUp() {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        testee = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
        context = new TestContext();
    }

    @Test
    public void testForProperty() {
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer deserializer = testee.forProperty(property);
        assertNotNull(deserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = testee.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = testee.baseTypeName();
        assertEquals("typeDeserializerBaseClass", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = testee.getPropertyName();
        assertEquals("typePropertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = testee.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        String validTypeId = "com.fasterxml.jackson.databind.jsontype.impl.ValidType";
        JsonDeserializer<Object> deserializer = testee._findDeserializer(context, validTypeId);
        assertNotNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithInvalidTypeId() throws IOException {
        String invalidTypeId = "com.fasterxml.jackson.databind.jsontype.impl.InvalidType";
        testee._findDeserializer(context, invalidTypeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = testee._findDefaultImplDeserializer(context);
        assertNotNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String unknownTypeId = "unknownTypeId";
        JavaType handledType = testee._handleUnknownTypeId(context, unknownTypeId);
        assertNotNull(handledType);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(JavaType type, BeanProperty property)
                throws JsonMappingException {
            return new StubJsonDeserializer();
        }

        @Override
        public JsonDeserializer<Object> findBeanDeserializer(BeanDescription beanDesc)
                throws JsonMappingException {
            return new StubJsonDeserializer();
        }

        @Override
        public JavaType findTypeDeserializer(JavaType type, DeserializationConfig config)
                throws JsonMappingException {
            return new StubTypeDeserializer();
        }

        @Override
        public Object findValueHandler(DeserializationConfig config, BeanProperty property) {
            return null;
        }

        @Override
        public Object findValueHandler(DeserializationConfig config, Class<?> valueType) {
            return null;
        }

        @Override
        public Object findValueHandler(DeserializationConfig config, JavaType valueType) {
            return null;
        }

        @Override
        public Object handleUnknownValue(Object value, DeserializationConfig config)
                throws JsonMappingException, IOException {
            return null;
        }

        @Override
        public Object handleParsingException(ParsingException pex) throws IOException {
            return null;
        }

        @Override
        public Object handleUnexpectedValue(Object value, DeserializationContext ctxt)
                throws JsonMappingException, IOException {
            return null;
        }

        @Override
        public JsonParser.Feature overrideFeatures() {
            return JsonParser.Feature.AUTO_CLOSE_SOURCE;
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public boolean isEnabled(DeserializerFeature feature) {
            return true;
        }

        @Override
        public void reset(JsonFactory f) {
            // empty
        }
    }

    private static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class StubTypeDeserializer implements TypeDeserializer {
        @Override
        public Object deserializeTypedFromObject(Object o, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}