
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
    
    private static class TypeDeserializerBaseDummy extends TypeDeserializerBase {
        
        private static final long serialVersionUID = 1L;
        
        public TypeDeserializerBaseDummy(JavaType baseType, TypeIdResolver idRes, String typePropName,
                boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropName, typeIdVisible, defaultImpl);
        }
        
        public TypeDeserializerBaseDummy(TypeDeserializerBaseDummy src, BeanProperty property) {
            super(src, property);
        }
        
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseDummy(this, prop);
        }
        
        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
        
        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                throws IOException {
            return super._findDefaultImplDeserializer(ctxt);
        }
        
        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                throws IOException {
            return super._deserializeWithNativeTypeId(jp, ctxt, typeId);
        }
        
        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                throws IOException {
            return super._findDeserializer(ctxt, typeId);
        }
        
        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                TypeIdResolver idResolver, JavaType baseType) throws IOException {
            return super._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        }
        
    }
    
    private static class TypeIdResolverDummy implements TypeIdResolver {
        
        @Override
        public void init(JavaType baseType) {
        }
        
        @Override
        public String idFromValue(Object value) {
            return null;
        }
        
        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }
        
        @Override
        public JavaType typeFromId(DatabindContext context, String id) throws IOException {
            return null;
        }
        
        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }
        
        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
        
    }
    
    private TypeDeserializerBaseDummy deserializer;
    private JsonFactory jsonFactory;
    private DeserializationContext deserializationContext;
    private ObjectMapper objectMapper;
    private BeanProperty beanProperty;
    private static final JavaType BASE_TYPE = SimpleType.construct(Object.class);
    
    @Before
    public void setup() {
        TypeIdResolverDummy typeIdResolver = new TypeIdResolverDummy();
        deserializer = new TypeDeserializerBaseDummy(BASE_TYPE, typeIdResolver, "type", true, Object.class);
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        deserializationContext = objectMapper.createDeserializationContext(null, jsonFactory);
    }
    
    @Test
    public void testFindDefaultImplDeserializer_DefaultImplEnabled() throws IOException {
        JsonDeserializer<Object> deserializerResult = deserializer._findDefaultImplDeserializer(deserializationContext);
        
        assertNotNull(deserializerResult);
    }
    
    @Test
    public void testFindDefaultImplDeserializer_DefaultImplDisabled() throws IOException {
        deserializationContext.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        JsonDeserializer<Object> deserializerResult = deserializer._findDefaultImplDeserializer(deserializationContext);
        
        assertNotNull(deserializerResult);
    }
    
    @Test
    public void testFindDefaultImplDeserializer_DefaultImplIsNull() throws IOException {
        deserializer = new TypeDeserializerBaseDummy(BASE_TYPE, null, "type", true, null);
        JsonDeserializer<Object> deserializerResult = deserializer._findDefaultImplDeserializer(deserializationContext);
        
        assertNull(deserializerResult);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_TypeIdIsNull() throws IOException {
        String json = "{\"type\": null, \"value\": \"Test\"}";
        JsonParser jp = jsonFactory.createParser(json);
        
        Object deserializedObject = deserializer._deserializeWithNativeTypeId(jp, deserializationContext, null);
        
        assertNotNull(deserializedObject);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_DeserializerIsNull() throws IOException {
        String json = "{\"type\": \"test\", \"value\": \"Test\"}";
        JsonParser jp = jsonFactory.createParser(json);
        String typeId = "test";
        Map<String, JsonDeserializer<Object>> deserializers = new HashMap<>();
        
        Object deserializedObject = deserializer._deserializeWithNativeTypeId(jp, deserializationContext, typeId);
        
        assertNull(deserializedObject);
    }
    
    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeId_TypeIdIsNull_FailsWhenExpected() throws IOException {
        String json = "{\"type\": null, \"value\": \"Test\"}";
        JsonParser jp = jsonFactory.createParser(json);
        deserializer._deserializeWithNativeTypeId(jp, deserializationContext, null);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        String json = "{\"type\": \"test\", \"value\": \"Test\"}";
        JsonParser jp = jsonFactory.createParser(json);
        String typeId = "test";
        StdDeserializer<Object> deserializer = new StdDeserializer<Object>(Object.class) {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.nextTextValue();
            }
        };
        Map<String, JsonDeserializer<Object>> deserializers = new HashMap<>();
        deserializers.put(typeId, deserializer);
        
        deserializer._deserializeWithNativeTypeId(jp, deserializationContext, typeId);
        
        // verify that deserialization is done without errors
        jp.nextToken();
        String value = jp.getText();
        assertEquals("Test", value);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_TypeIdIsString() throws IOException {
        String json = "{\"type\": \"test\", \"value\": \"Test\"}";
        JsonParser jp = jsonFactory.createParser(json);
        String typeId = "test";
        StdDeserializer<Object> deserializer = new StdDeserializer<Object>(Object.class) {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.nextTextValue();
            }
        };
        Map<Object, JsonDeserializer<Object>> deserializers = new HashMap<>();
        deserializers.put(typeId, deserializer);
        
        deserializer._deserializeWithNativeTypeId(jp, deserializationContext, typeId);
        
        // verify that deserialization is done without errors
        jp.nextToken();
        String value = jp.getText();
        assertEquals("Test", value);
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "unknown";
        TypeIdResolverDummy idResolver = new TypeIdResolverDummy();
        
        JsonDeserializer<Object> deserializer = deserializer._handleUnknownTypeId(deserializationContext, typeId,
            idResolver, BASE_TYPE);
        
        assertNull(deserializer);
    }
}

