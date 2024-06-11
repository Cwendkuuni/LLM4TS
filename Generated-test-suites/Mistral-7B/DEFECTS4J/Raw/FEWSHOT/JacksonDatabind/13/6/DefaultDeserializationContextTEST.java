package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;

import java.io.Serializable;
import java.util.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdGenerator idGen = mapper.getPolymorphicSerializerProvider().findValueSerializer(String.class).getConfig()
                .getObjectIdGenerator();
        DefaultDeserializationContext context = new Impl(mapper.getDeserializerFactory());

        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context._objectIdResolvers = new ArrayList<>();
        context._objectIdResolvers.add(resolver);

        Object id = "testId";
        ReadableObjectId objId = context.findObjectId(id, idGen, ObjectIdGenerator.IdKey.STD);
        assertNotNull(objId);
        assertEquals(id, objId.getId());
        assertEquals(resolver, objId.getResolver());
    }

    @Test
    public void testCheckUnresolvedObjectId() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = new Impl(mapper.getDeserializerFactory());

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey("testKey"),
                new ReadableObjectId(new ObjectIdGenerator.IdKey("testKey")));

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        context._objectIds.remove(new ObjectIdGenerator.IdKey("testKey"));

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        context._objectIds = null;
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = new Impl(mapper.getDeserializerFactory());

        JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
        Object id = deserializer;

        JsonDeserializer<String> deser = context.deserializerInstance(null, id);
        assertEquals(deserializer, deser);

        deserializer = null;
        deser = context.deserializerInstance(null, id);
        assertNull(deser);

        Annotated ann = null;
        deser = context.deserializerInstance(ann, id);
        assertNull(deser);

        Annotated annWithDeserializer = mapper.findValueSerializer(String.class);
        deser = context.deserializerInstance(annWithDeserializer, id);
        assertEquals(deserializer, deser);

        Class deserClass = deserializer.getClass();
        deser = context.deserializerInstance(null, deserClass);
        assertEquals(deserializer, deser);

        JsonDeserializer<String> deserFromFactory = mapper.getDeserializerFactory().findValueDeserializer(String.class, null);
        deser = context.deserializerInstance(null, deserFromFactory);
        assertEquals(deserializer, deser);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = new Impl(mapper.getDeserializerFactory());

        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        Object id = keyDeserializer;

        KeyDeserializer deser = context.keyDeserializerInstance(null, id);
        assertEquals(keyDeserializer, deser);

        keyDeserializer = null;
        deser = context.keyDeserializerInstance(null, id);
        assertNull(deser);

        Annotated ann = null;
        deser = context.keyDeserializerInstance(ann, id);
        assertNull(deser);

        Annotated annWithDeserializer = mapper.findValueSerializer(String.class);
        deser = context.keyDeserializerInstance(annWithDeserializer, id);
        assertEquals(keyDeserializer, deser);

        Class deserClass = keyDeserializer.getClass();
        deser = context.keyDeserializerInstance(null, deserClass);
        assertEquals(keyDeserializer, deser);

        KeyDeserializer deserFromFactory = mapper.getDeserializerFactory().findKeyDeserializer(String.class, null);
        deser = context.keyDeserializerInstance(null, deserFromFactory);
        assertEquals(keyDeserializer, deser);
    }

    @Test
    public void testResolveId() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = new Impl(mapper.getDeserializerFactory());

        ObjectIdGenerator idGen = mapper.getPolymorphicSerializerProvider().findValueSerializer(String.class).getConfig()
                .getObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        context._objectIdResolvers = new ArrayList<>();
        context._objectIdResolvers.add(resolver);

        Object id = "testId";
        ReadableObjectId objId = context.findObjectId(id, idGen, ObjectIdGenerator.IdKey.STD);
        assertEquals(id, objId.getId());
        assertEquals(resolver, objId.getResolver());

        Object idToResolve = objId;
        JsonParser jp = mapper.getFactory().createParser("[]");
        jp.nextToken();

        Object result = context.handleUnexpectedValue(jp, null, null, null, null);
        assertNull(result);

        JsonDeserializer<String> deserializer = new StandardDeserializer<String>(String.class);
        deserializer.resolve(context);

        Object resolvedId = deserializer.getId();
        Object resolvedDeserializer = deserializer;
        Object deserializedObject = mapper.readValue(jp, String.class);

        Object resolvedIdFromContext = context.findObjectId(resolvedId, idGen, ObjectIdGenerator.IdKey.STD).getId();
        assertEquals(idToResolve, resolvedIdFromContext);

        JsonDeserializer<String> deser = context.deserializerInstance(null, resolvedDeserializer);
        assertEquals(deserializer, deser);

        String deserializedString = (String) deser.deserialize(jp, null);
        assertEquals(deserializedObject, deserializedString);

        JsonDeserializer<String> deserFromContext = context.deserializerInstance(null, ObjectIdGenerator.IdKey.STD, resolvedId);
        assertEquals(deserializer, deserFromContext);

        String deserializedStringFromContext = deserFromContext.deserialize(jp, null);
        assertEquals(deserializedString, deserializedStringFromContext);
    }

    @Test
    public void testFindValueDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = new Impl(mapper.getDeserializerFactory());

        JsonDeserializer<Integer> intDeserializer = new StandardDeserializer<Integer>(Integer.class);
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);

        context._config = new DeserializationConfig(mapper.getFactory(), null);
        context._config.setBeanDescription(new AnnotatedClass(intDeserializer.getType()));
        context._config.setRootValueDeserializer(intDeserializer);

        JsonDeserializer<Integer> deser = context.findValueDeserializer(intDeserializer.getType(), null);
        assertEquals(intDeserializer, deser);

        context._config = new DeserializationConfig(mapper.getFactory(), null);
        context._config.setRootValueDeserializer(stringDeserializer);
        context._config.setBeanDescription(new AnnotatedClass(intDeserializer.getType()));

        deser = context.findValueDeserializer(intDeserializer.getType(), null);
        assertNull(deser);
    }
}