package com.fasterxml.jackson.databind.deser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext deserializationContext;

    @Before
    public void setUp() {
        deserializationContext = new DefaultDeserializationContext.Impl(new DeserializerFactoryImpl());
    }

    @After
    public void tearDown() {
        deserializationContext = null;
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<ObjectIdResolver.IdKey>() {
            public boolean canUseFor(ObjectIdResolver resolverType) { return true; }
            public ObjectIdGenerator<ObjectIdResolver.IdKey> newForDeserialization(Object context) { return null; }
            public boolean maySerializeAsObject() { return false; }
            public IdKey key(Object key) { return null; }
            public Object generateId(Object forPojo) { return null; }
            public IdKey key(Object key, Object forPojo) { return null; }
        };
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        ReadableObjectId readableObjectId = deserializationContext.findObjectId(id, generator, resolverType);

        assertNotNull(readableObjectId);
        assertEquals(1, deserializationContext._objectIds.size());
        assertTrue(deserializationContext._objectIds.containsKey(generator.key(id)));
        assertEquals(readableObjectId, deserializationContext._objectIds.get(generator.key(id)));
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        deserializationContext._objectIds = new LinkedHashMap<>();
        deserializationContext._objectIds.put(new ObjectMapper.DefaultDeserializationContext.Impl.ObjectIdGeneratorImpl().key(new Object()),
                new ReadableObjectId(new ObjectMapper.DefaultDeserializationContext.Impl.ObjectIdGeneratorImpl().key(new Object())));
        try {
            deserializationContext.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail("Unexpected UnresolvedForwardReference exception");
        }

        deserializationContext.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        try {
            deserializationContext.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testDeserializerInstance() {
        Annotated annotated = new Annotated() {
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) { return null; }
            public boolean hasAnnotation(Class<?> acls) { return false; }
            public boolean hasOneOf(Class<? extends java.lang.annotation.Annotation>[] annoClasses) { return false; }
            public Class<?> getDeclaringClass() { return null; }
            public java.lang.reflect.Field getAnnotated() { return null; }
        };

        Object deserDef = new JsonDeserializer<Object>() {
            public Object deserialize(JsonParser p, DeserializationContext ctxt) { return null; }
        };

        JsonDeserializer<Object> deserializer = deserializationContext.deserializerInstance(annotated, deserDef);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonDeserializer);
        assertEquals(deserDef, deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() {
        Annotated annotated = new Annotated() {
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) { return null; }
            public boolean hasAnnotation(Class<?> acls) { return false; }
            public boolean hasOneOf(Class<? extends java.lang.annotation.Annotation>[] annoClasses) { return false; }
            public Class<?> getDeclaringClass() { return null; }
            public java.lang.reflect.Field getAnnotated() { return null; }
        };

        Object deserDef = new KeyDeserializer() {
            public Object deserializeKey(String key, DeserializationContext ctxt) { return null; }
        };

        KeyDeserializer keyDeserializer = deserializationContext.keyDeserializerInstance(annotated, deserDef);

        assertNotNull(keyDeserializer);
        assertTrue(keyDeserializer instanceof KeyDeserializer);
        assertEquals(deserDef, keyDeserializer);
    }

    private static class DeserializerFactoryImpl extends DeserializerFactory {
        public JsonDeserializer<Object> createBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) { return null; }
        public JsonDeserializer<Object> createBuilderBasedDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) { return null; }
        public JsonDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) { return null; }
        public JsonDeserializer<Object> createArrayDeserializer(DeserializationContext ctxt, ArrayType type, BeanDescription beanDesc) { return null; }
        public JsonDeserializer<Object> createCollectionDeserializer(DeserializationContext ctxt, CollectionType type, BeanDescription beanDesc) { return null; }
        public JsonDeserializer<Object> createCollectionLikeDeserializer(DeserializationContext ctxt, CollectionLikeType type, BeanDescription beanDesc) { return null; }
        public JsonDeserializer<?> createMapDeserializer(DeserializationContext ctxt, MapType type, BeanDescription beanDesc) { return null; }
        public JsonDeserializer<?> createMapLikeDeserializer(DeserializationContext ctxt, MapLikeType type, BeanDescription beanDesc) { return null; }
        public KeyDeserializer createKeyDeserializer(DeserializationContext ctxt, JavaType type) { return null; }
        public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType) { return null; }
        public TypeDeserializer findPropertyTypeDeserializer(DeserializationConfig config, JavaType baseType, AnnotatedMember annotated) { return null; }
        public TypeDeserializer findPropertyContentTypeDeserializer(DeserializationConfig config, JavaType containerType, AnnotatedMember propertyEntity) { return null; }
        public TypeDeserializer findArrayTypeDeserializer(DeserializationConfig config, ArrayType baseArrayType, BeanDescription beanDesc) { return null; }
        public TypeDeserializer findCollectionTypeDeserializer(DeserializationConfig config, CollectionType baseType, BeanDescription beanDesc) { return null; }
        public TypeDeserializer findReferenceTypeDeserializer(DeserializationConfig config, ReferenceType refType, BeanDescription beanDesc) { return null; }
        public TypeDeserializer findMapTypeDeserializer(DeserializationConfig config, MapType baseType, BeanDescription beanDesc) { return null; }
        public JsonDeserializer<?> findDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) { return null; }
    }
}