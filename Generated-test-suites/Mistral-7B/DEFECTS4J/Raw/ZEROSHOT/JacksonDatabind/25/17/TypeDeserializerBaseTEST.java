package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        jsonFactory = objectMapper.getFactory();
        typeDeserializerBase = new MyTypeDeserializerBase(TypeFactory.defaultInstance.constructType(Map.class),
                new MyTypeIdResolver(), "typePropertyName", true, String.class);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty beanProperty = new BeanProperty("testPropertyName", String.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(forProperty);
        assertInstanceOf(TypeDeserializer.class, forProperty);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals(Map.class.getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(MyTypeIdResolver.class, typeDeserializerBase.getTypeIdResolver().getClass());
    }

    @Test
    public void testDefaultImpl() throws Exception {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class java.util.Map; id-resolver:class com.fasterxml.jackson.databind.jsontype.impl.MyTypeIdResolver;]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "StringMapKeyValue";
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertNotNull(deserializer);
        assertInstanceOf(StringMapKeyValueDeserializer.class, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNotNull(deserializer);
        assertInstanceOf(NullifyingDeserializer.class, deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws Exception {
        String typeId = "UnknownTypeId";
        DeserializationContext context = objectMapper.getDeserializationContext();
        typeDeserializerBase._handleUnknownTypeId(context, typeId, null, null);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"typeId\":\"StringMapKeyValue\",\"key\":\"testKey\",\"value\":\"testValue\"}";
        JsonParser jsonParser = jsonFactory.createParser(json);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        assertInstanceOf(Map.class, result);
        Map<String, String> map = (Map<String, String>) result;
        assertEquals("testKey", map.get("key"));
        assertEquals("testValue", map.get("value"));
    }

    static class MyTypeIdResolver extends TypeIdResolverBase {

        @Override
        public String getDescForKnownTypeIds() {
            return "StringMapKeyValue";
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String id) {
            if ("StringMapKeyValue".equals(id)) {
                return TypeFactory.defaultInstance.constructType(new TypeReference<Map<String, String>>() {}.getType());
            }
            return super.typeFromId(ctxt, id);
        }
    }

    static class StringMapKeyValueDeserializer extends StdSerializer<Map<String, String>> {

        public StringMapKeyValueDeserializer() {
            this(null);
        }

        public StringMapKeyValueDeserializer(Class<Map<String, String>> t) {
            this(null);
        }

        public StringMapKeyValueDeserializer(Class<Map<String, String>> t, BeanProperty property) {
            this(null);
            _valueClass = t;
            _property = property;
        }

        public StringMapKeyValueDeserializer(Class<Map<String, String>> t, BeanProperty property, JsonDeserializer<Object> deser) {
            this(t, property);
            _valueDeserializer = deser;
        }

        @Override
        public Map<String, String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Map<String, String> map = new HashMap<>();
            while (jp.nextToken() != null) {
                String key = jp.getCurrentName();
                JsonParser fieldParser = jp.getCodec().treeAsTokens(jp);
                String value = fieldParser.nextValue().textValue();
                map.put(key, value);
            }
            return map;
        }
    }

    static class MyTypeDeserializerBase extends TypeDeserializerBase {

        public MyTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver,
                String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        protected Class<?> _handleMalformedTypeId(DeserializationContext ctxt, String typeId,
                JavaType baseType) {
            return null;
        }
    }
}