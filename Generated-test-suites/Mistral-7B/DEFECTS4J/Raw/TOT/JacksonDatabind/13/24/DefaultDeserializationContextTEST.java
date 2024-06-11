package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.FieldVisibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerCache;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.DeserializationContext.HandlerInstantiator;
import com.fasterxml.jackson.databind.ObjectIdResolver;
import com.fasterxml.jackson.databind.DeserializerCache.KeyType;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    public static void main(String[] args) throws Exception {
        testFindObjectId();
        testCheckUnresolvedObjectId();
        testDeserializerInstance();
        testKeyDeserializerInstance();
    }

    static void testFindObjectId() {
        ObjectMapper mapper = new ObjectMapper();

        DeserializationConfig config = mapper.getConfig();
        DeserializerFactory factory = mapper.getDeserializerFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return IdKey.STD_OBJECT;
            }
        };

        // Test with null id
        assertNull(ctxt.findObjectId(null, idGen, ObjectIdResolver.class));

        // Test with id that has been used before
        Object id = new Object();
        ctxt._objectIds = new LinkedHashMap<>();
        ctxt.findObjectId(id, idGen, ObjectIdResolver.class);
        assertNotNull(ctxt._objectIds.get(IdKey.STD_OBJECT));

        // Test with id that hasn't been used before
        id = new Object();
        ctxt._objectIds = new LinkedHashMap<>();
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DeserializationContext ctxt) {
                return new ObjectIdResolver() {
                    @Override
                    public boolean canUseFor(ObjectIdResolver resolverType) {
                        return resolverType.getClass() == SimpleObjectIdResolver.class;
                    }

                    @Override
                    public void resolvePropertyId(Object id, MapperParser mp, DeserializationContext ctxt) throws IOException {
                        // do nothing
                    }
                };
            }
        };
        ctxt._objectIdResolvers = new ArrayList<>();
        ctxt.findObjectId(id, idGen, ObjectIdResolver.class);
        assertNotNull(ctxt._objectIds.get(IdKey.STD_OBJECT));
    }

    static void testCheckUnresolvedObjectId() {
        ObjectMapper mapper = new ObjectMapper();

        DeserializationConfig config = mapper.getConfig();
        DeserializerFactory factory = mapper.getDeserializerFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        ctxt._objectIds = new LinkedHashMap<>();
        ctxt._objectIdResolvers = new ArrayList<>();

        Object id = new Object();

        // Test with empty map
        try {
            ctxt.checkUnresolvedObjectId();
            assert false : "Expected UnresolvedForwardReference";
        } catch (UnresolvedForwardReference e) {
            // Expected
        }

        // Test with no unresolved ids
        ctxt.checkUnresolvedObjectId();

        // Test with unresolved ids
        ctxt._objectIds.put(IdKey.STD_OBJECT, new ReadableObjectId(IdKey.STD_OBJECT));
        try {
            ctxt.checkUnresolvedObjectId();
            assert false : "Expected UnresolvedForwardReference";
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    static void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        DeserializationConfig config = mapper.getConfig();
        DeserializerFactory factory = mapper.getDeserializerFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test with null def
        assertNull(ctxt.deserializerInstance(null, null));

        // Test with JsonDeserializer
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        JsonDeserializer<String> deser = (JsonDeserializer<String>) ctxt.deserializerInstance(new AnnotatedClassTest(), deserializer);
        assertNotNull(deser);

        // Test with Class
        JsonDeserializer<String> deserializer2 = new JsonDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        JsonDeserializer<String> deser2 = (JsonDeserializer<String>) ctxt.deserializerInstance(new AnnotatedClassTest(), String.class);
        assertNotNull(deser2);

        // Test with custom class
        class CustomDeserializer extends JsonDeserializer<String> {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        }
        CustomDeserializer deser3 = (CustomDeserializer) ctxt.deserializerInstance(new AnnotatedClassTest(), CustomDeserializer.class);
        assertNotNull(deser3);

        // Test with null config
        config = null;
        DeserializerProvider provider = mapper.getDeserializerProvider();
        JsonDeserializer<String> deserializer3 = provider.findValueDeserializer(String.class, null);
        JsonDeserializer<String> deser3 = (JsonDeserializer<String>) ctxt.deserializerInstance(new AnnotatedClassTest(), deserializer3.getClass());
        assertNotNull(deser3);
    }

    static void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        DeserializationConfig config = mapper.getConfig();
        DeserializerFactory factory = mapper.getDeserializerFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test with null def
        assertNull(ctxt.keyDeserializerInstance(null, null));

        // Test with JsonDeserializer
        KeyDeserializer deserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        KeyDeserializer deser = (KeyDeserializer) ctxt.keyDeserializerInstance(new AnnotatedClassTest(), deserializer);
        assertNotNull(deser);

        // Test with Class
        KeyDeserializer deserializer2 = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        KeyDeserializer deser2 = (KeyDeserializer) ctxt.keyDeserializerInstance(new AnnotatedClassTest(), KeyDeserializer.class);
        assertNotNull(deser2);

        // Test with custom class
        class CustomKeyDeserializer extends KeyDeserializer {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        }
        CustomKeyDeserializer deser3 = (CustomKeyDeserializer) ctxt.keyDeserializerInstance(new AnnotatedClassTest(), CustomKeyDeserializer.class);
        assertNotNull(deser3);

        // Test with null config
        config = null;
        DeserializerProvider provider = mapper.getDeserializerProvider();
        KeyDeserializer deserializer3 = provider.findKeyDeserializer(JsonParser.Feature.USE_LEGACY_KEY_DESERIALIZER, null);
        KeyDeserializer deser3 = (KeyDeserializer) ctxt.keyDeserializerInstance(new AnnotatedClassTest(), deserializer3.getClass());
        assertNotNull(deser3);
    }

    static class AnnotatedClassTest {
        @JsonProperty
        @JsonAutoDetect(fieldVisibility = FieldVisibility.ANY)
        private String prop;

        @JsonIgnore
        @JsonIgnoreProperties(value = {"prop"})
        private List<Foo> foos;
    }

    static class Foo {
        @JsonAutoDetect(fieldVisibility = FieldVisibility.ANY)
        private String bar;
    }

    static class SerializerProviderMock implements SerializerProvider {
        @Override
        public ObjectWriter getObjectWriter(Class<?> type) {
            return null;
        }

        @Override
        public ObjectWriter getObjectWriter(Class<?> type, Locale locale) {
            return null;
        }

        @Override
        public ObjectWriter getObjectWriter(Class<?> type, JsonFormat.Value format) {
            return null;
        }

        @Override
        public ObjectWriter getObjectWriter(Class<?> type, JsonFormat.Value format, Locale locale) {
            return null;
        }

        @Override
        public JsonGenerator getGeneratorForCurrentOutput(JsonGenerator jg) {
            return null;
        }

        @Override
        public JsonGenerator getGeneratorForCurrentOutput(JsonGenerator jg, Locale locale) {
            return null;
        }

        @Override
        public JsonGenerator getGeneratorForCurrentOutput(JsonGenerator jg, JsonFormat.Value format) {
            return null;
        }

        @Override
        public JsonGenerator getGeneratorForCurrentOutput(JsonGenerator jg, Locale locale, JsonFormat.Value format) {
            return null;
        }

        @Override
        public JsonGenerator getValueGenerator(JsonSerializer<?> vs, JsonSerializer<Object> defSerializer) {
            return null;
        }

        @Override
        public JsonGenerator getValueGenerator(JsonSerializer<?> vs, JsonSerializer<Object> defSerializer, Locale locale) {
            return null;
        }

        @Override
        public JsonGenerator getValueGenerator(JsonSerializer<?> vs, JsonSerializer<Object> defSerializer, JsonFormat.Value format) {
            return null;
        }

        @Override
        public JsonGenerator getValueGenerator(JsonSerializer<?> vs, JsonSerializer<Object> defSerializer, Locale locale, JsonFormat.Value format) {
            return null;
        }

        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> nullValueSerializer) {
            return null;
        }

        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> nullValueSerializer, Locale locale) {
            return null;
        }

        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> nullValueSerializer, JsonFormat.Value format) {
            return null;
        }

        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> nullValueSerializer, Locale locale,
                JsonFormat.Value format) {
            return null;
        }

        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type) {
            return null;
        }

        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, Locale locale) {
            return null;
        }

        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, JsonFormat.Value format) {
            return null;
        }

        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, Locale locale, JsonFormat.Value format) {
            return null;
        }
    }
}