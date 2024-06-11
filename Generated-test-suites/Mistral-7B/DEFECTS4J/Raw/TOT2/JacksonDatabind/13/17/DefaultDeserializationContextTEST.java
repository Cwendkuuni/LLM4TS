package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.util.*;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest extends TestBase {

    private DefaultDeserializationContext context;

    @org.junit.Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
    }

    // Test for findObjectId method with valid id and ObjectIdGenerator
    @org.junit.Test
    public void testFindObjectIdValidId() throws Exception {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }
        };
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        ReadableObjectId id = context.findObjectId(new Object(), idGenerator, idResolver);
        assertNotNull(id);
    }

    // Test for findObjectId method with null id and ObjectIdGenerator
    @org.junit.Test
    public void testFindObjectIdNullId() throws Exception {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }
        };
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        ReadableObjectId id = context.findObjectId(null, idGenerator, idResolver);
        assertNull(id);
    }

    // Test for findObjectId method with null id, null ObjectIdGenerator, and null ObjectIdResolver
    @org.junit.Test
    public void testFindObjectIdNullIdNullObjectIdGeneratorNullObjectIdResolver() throws Exception {
        ObjectIdId id = context.findObjectId(null, null, null);
        assertNull(id);
    }

    // Test for checkUnresolvedObjectId method with unresolved object ids
    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdUnresolvedObjectIds() throws Exception {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }
        };
        ObjectIdResolver idResolver = new SimpleObjectIdResolver();
        context._objectIds = new LinkedHashMap<>();
        context._objectIdResolvers = new ArrayList<>();
        context.findObjectId(new Object(), idGenerator, idResolver);
        context.checkUnresolvedObjectId();
    }

    // Test for deserializerInstance method with null Annotated and JsonDeserializer
    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializerInstanceNullAnnotatedJsonDeserializer() throws Exception {
        Annotated ann = null;
        JsonDeserializer deserializer = null;
        JsonDeserializer deser = context.deserializerInstance(ann, deserializer);
    }

    // Test for deserializerInstance method with Annotated and null JsonDeserializer
    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializerInstanceAnnotatedNullJsonDeserializer() throws Exception {
        Annotated ann = new AnnotatedClass(String.class);
        JsonDeserializer deserializer = null;
        JsonDeserializer deser = context.deserializerInstance(ann, deserializer);
    }

    // Test for deserializerInstance method with Annotated and valid JsonDeserializer
    @org.junit.Test
    public void testDeserializerInstanceAnnotatedValidJsonDeserializer() throws Exception {
        Annotated ann = new AnnotatedClass(String.class);
        JsonDeserializer deserializer = new StdDeserializer();
        JsonDeserializer deser = context.deserializerInstance(ann, deserializer);
        assertEquals(deserializer.getClass(), deser.getClass());
    }

    // Test for keyDeserializerInstance method with null Annotated and KeyDeserializer
    @org.junit.Test(expected = JsonMappingException.class)
    public void testKeyDeserializerInstanceNullAnnotatedKeyDeserializer() throws Exception {
        Annotated ann = null;
        KeyDeserializer deserializer = null;
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserializer);
    }

    // Test for keyDeserializerInstance method with Annotated and null KeyDeserializer
    @org.junit.Test(expected = JsonMappingException.class)
    public void testKeyDeserializerInstanceAnnotatedNullKeyDeserializer() throws Exception {
        Annotated ann = new AnnotatedClass(String.class);
        KeyDeserializer deserializer = null;
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserializer);
    }

    // Test for keyDeserializerInstance method with Annotated and valid KeyDeserializer
    @org.junit.Test
    public void testKeyDeserializerInstanceAnnotatedValidKeyDeserializer() throws Exception {
        Annotated ann = new AnnotatedClass(String.class);
        KeyDeserializer deserializer = new StdKeyDeserializer();
        KeyDeserializer deser = context.keyDeserializerInstance(ann, deserializer);
        assertEquals(deserializer.getClass(), deser.getClass());
    }
}