package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for abstract class {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWithValidTypeId() throws Exception {
        TypeIdResolver idResolver = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
                if ("StringType".equals(id)) {
                    return BasicTypeFactory.instance.constructType(String.class);
                }
                if ("IntegerType".equals(id)) {
                    return BasicTypeFactory.instance.constructType(Integer.class);
                }
                return null;
            }

            @Override
            public String idFromValue(Object value) {
                return null;
            }
        };

        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                BasicTypeFactory.instance.constructType(Object.class), idResolver,
                "typePropertyName", true, BasicTypeFactory.instance.constructType(String.class)) {
            @Override
            protected JsonDeserializer<Object> forProperty(BeanProperty prop) {
                return null;
            }
        };

        JsonDeserializer<Object> deserializerForString = deserializer.findDeserializer(new TestContext(), "StringType");
        JsonDeserializer<Object> deserializerForInteger = deserializer.findDeserializer(new TestContext(), "IntegerType");

        assertNotNull(deserializerForString);
        assertNotNull(deserializerForInteger);
        assertNull(deserializer.findDeserializer(new TestContext(), "UnknownTypeId"));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                BasicTypeFactory.instance.constructType(Object.class), null,
                "typePropertyName", true, null) {
            @Override
            protected JsonDeserializer<Object> forProperty(BeanProperty prop) {
                return null;
            }
        };

        JsonDeserializer<Object> deserializerForVoid = deserializer.findDefaultImplDeserializer(new TestContext());
        JsonDeserializer<Object> deserializerForObject = deserializer.findDefaultImplDeserializer(new TestContext());

        assertNotNull(deserializerForVoid);
        assertEquals(NullifyingDeserializer.instance.getClass(), deserializerForVoid.getClass());
        assertNull(deserializerForObject);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeIdResolver idResolver = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
                return null;
            }

            @Override
            public String idFromValue(Object value) {
                return null;
            }
        };

        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                BasicTypeFactory.instance.constructType(Object.class), idResolver,
                "typePropertyName", true, null) {
            @Override
            protected JsonDeserializer<Object> forProperty(BeanProperty prop) {
                return null;
            }
        };

        try {
            deserializer.handleUnknownTypeId(new TestContext(), "UnknownTypeId");
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id 'UnknownTypeId' for POJO property 'typePropertyName'", e.getMessage());
        }
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                BasicTypeFactory.instance.constructType(Object.class), null,
                "typePropertyName", true, null) {
            @Override
            protected JsonDeserializer<Object> forProperty(BeanProperty prop) {
                return null;
            }
        };

        try {
            deserializer.handleMissingTypeId(new TestContext(), "MissingTypeId");
            fail();
        } catch (JsonMappingException e) {
            assertEquals("No type id found for POJO property 'typePropertyName' (for default value: null)", e.getMessage());
        }
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JavaType getRootValueType() {
            return BasicTypeFactory.instance.constructType(Object.class);
        }

        @Override
        public JavaType getTypeFactory() {
            return BasicTypeFactory.instance;
        }

        @Override
        public Object readValue(Class<?> type, JsonParser parser, boolean lenient) throws IOException {
            return parser.getValueAsToken().getValueAsString();
        }

        @Override
        public Object deserializeFromObject(Object bean, JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return null;
        }

        @Override
        public Object deserializeFromString(String text, Class<?> valueType, JsonParser p)
                throws IOException {
            return text;
        }

        @Override
        public Object deserializeFromNumber(Number n, Class<?> valueType, JsonParser p)
                throws IOException {
            return null;
        }

        @Override
        public Object deserializeFromBoolean(boolean b, Class<?> valueType, JsonParser p) throws IOException {
            return null;
        }

        @Override
        public Object deserializeFromNull(Class<?> valueType, JsonParser p) throws IOException {
            return null;
        }

        @Override
        public Object deserializeFromInteger(int num, Class<?> valueType, JsonParser p) throws IOException {
            return num;
        }

        @Override
        public Object deserializeFromRawNumber(String text, Class<?> valueType, JsonParser p) throws IOException {
            return Long.parseLong(text);
        }

        @Override
        public Object deserializeFromObjectId(String id, Class<?> valueType, JsonParser p) throws IOException {
            return null;
        }

        @Override
        public JsonDeserializer<Object> findContextualValueDeserializer(JavaType type, PropertyName propertyName) {
            return null;
        }

        @Override
        public Object findValueWithTypeId(String id, Class<?> valueType, JsonParser p) throws IOException {
            return null;
        }

        @Override
        public Object findValueWithTypeId(String id, Class<?> valueType, JsonParser p, DeserializationFeature[] features)
                throws IOException {
            return null;
        }

        @Override
        public JavaType handleUnknownTypeId(JavaType baseType, String id, TypeIdResolver idResolver,
                String description) throws IOException {
            return null;
        }

        @Override
        public JavaType handleMissingTypeId(JavaType baseType, TypeIdResolver idResolver, String description)
                throws IOException {
            return null;
        }

        @Override
        public void findAndRegisterAlternativeDeserializers(JavaType baseType, JavaType alternativeType,
                String alternativeName) {
        }

        @Override
        public void findAndRegisterAlternativeDeserializers(JavaType baseType, JavaType alternativeType,
                String alternativeName, JavaType propertyType) {
        }

        @Override
        public void findAndRegisterAlternativeDeserializers(JavaType baseType, JavaType alternativeType,
                String alternativeName, PropertyName alternativeName, JavaType propertyType) {
        }

        @Override
        public void findAndRegisterDeserializerForProperty(BeanProperty property) {
        }

        @Override
        public void findAndRegisterDeserializerForSubType(JavaType subType) {
        }

        @Override
        public void findAndRegisterDeserializerForKey(JavaType keyType) {
        }

        @Override
        public void registerDeserializerForValue(JavaType valueType, JsonDeserializer<?> deserializer) {
        }

        @Override
        public <T> void registerSubType(JavaType baseType, JavaType subType, JsonDeserializer<T> deserializer) {
        }

        @Override
        public <T> void registerSubType(JavaType baseType, JavaType subType, JsonDeserializer<T> deserializer,
                String propertyName) {
        }

        @Override
        public <T> void registerKeyDeserializer(JavaType keyType, JsonDeserializer<T> deserializer) {
        }

        @Override
        public void registerTypeIdResolver(TypeIdResolver idResolver) {
        }

        @Override
        public void registerValueDeserializer(JavaType type, JsonDeserializer<?> deserializer) {
        }

        @Override
        public void registerValueDeserializer(Class<?> valueClass, JsonDeserializer<?> deserializer) {
        }

        @Override
        public void registerGenericTypeDeserializer(JavaType type, JsonDeserializer<Object> deserializer) {
        }

        @Override
        public void registerGenericTypeDeserializer(Class<?> rawType, JsonDeserializer<Object> deserializer) {
        }

        @Override
        public void registerConverter(Converter<?> converter) {
        }

        @Override
        public void setCurrentName(String name) {
        }

        @Override
        public void setCurrentObject(Object o) {
        }

        @Override
        public void setCurrentContextualHandler(ContextualDeserializer<?> contextualHandler) {
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public void setEnabled(DeserializationFeature feature, boolean enabled) {
        }

        @Override
        public void setBeanDescription(BeanDescription beanDescription) {
        }

        @Override
        public void setSerializationConfig(SerializationConfig config) {
        }

        @Override
        public void setRootValue(Object value) {
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public ObjectValueFactory valueFactory() {
            return null;
        }

        @Override
        public ObjectWriter creator() {
            return null;
        }

        @Override
        public void setInputSource(InputSource src) throws IOException {
        }

        @Override
        public JsonParser createParser(InputSource src) throws IOException, JsonParseException {
            return null;
        }

        @Override
        public JsonParser createParser(char[] chars, int start, int end) throws IOException, JsonParseException {
            return null;
        }

        @Override
        public JsonParser createParser(String text) throws IOException, JsonParseException {
            return null;
        }

        @Override
        public JsonParser createParser(File file) throws IOException, JsonParseException {
            return null;
        }

        @Override
        public JsonParser createParser(URL url) throws IOException, JsonParseException {
            return null;
        }

        @Override
        public JsonParser createParser(InputStream stream) throws IOException, JsonParseException {
            return null;
        }

        @Override
        public JsonParser createParser(byte[] bytes) throws IOException, JsonParseException {
            return null;
        }

        @Override
        public void reset() throws IOException {
        }

        @Override
        public void setBindingFactory(BeanProperty.StdGetter bindingFactory) {
        }

        @Override
        public void setBindingFactory(BeanProperty.Setter bindingFactory) {
        }

        @Override
        public void setBindingFactory(BeanProperty.Finder bindingFactory) {
        }
    };
}