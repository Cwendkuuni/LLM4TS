package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DefaultDeserializationContextTest {
    
    private DefaultDeserializationContext deserializationContext;
    
    @Before
    public void setUp() {
        deserializationContext = new DefaultDeserializationContext.Impl(new DeserializerFactory());
    }
    
    @Test
    public void testFindObjectId() {
        ObjectIdGenerator<IdKey> gen = new ObjectIdGenerator<IdKey>() {
            @Override
            public IdKey key(Object id) {
                return null;
            }

            @Override
            public IdKey key(Object id, Class<?> type) {
                return null;
            }

            @Override
            public ObjectIdGenerator<IdKey> forScope(Class<?> scope) {
                return null;
            }

            @Override
            public IdKey generateId(Object forPojo) {
                return null;
            }

            @Override
            public boolean canUseFor(ObjectIdGenerator<?> gen) {
                return false;
            }

            @Override
            public ObjectIdGenerator<IdKey> newForSerialization(Object context) {
                return null;
            }

            @Override
            public Object generateId(Object forPojo, Object context) {
                return null;
            }

            @Override
            public boolean maySerializeAsObject() {
                return false;
            }
        };
        
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId objectId = deserializationContext.findObjectId("id", gen, resolver);
        
        assertNull(objectId);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        deserializationContext.checkUnresolvedObjectId();
        // No assertion as this method does not return anything.
    }
    
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        Annotated annotated = new Annotated() {
            @Override
            public Class<?> getDeclaringClass() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public boolean hasAnnotation(Class<?> acls) {
                return false;
            }

            @Override
            public boolean hasOneOf(Class<? extends java.lang.annotation.Annotation>[] annoClasses) {
                return false;
            }

            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        
        Object deserDef = null;
        
        JsonDeserializer<Object> deserializer = deserializationContext.deserializerInstance(annotated, deserDef);
        
        assertNull(deserializer);
    }
    
    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        Annotated annotated = new Annotated() {
            @Override
            public Class<?> getDeclaringClass() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public boolean hasAnnotation(Class<?> acls) {
                return false;
            }

            @Override
            public boolean hasOneOf(Class<? extends java.lang.annotation.Annotation>[] annoClasses) {
                return false;
            }

            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        
        Object deserDef = null;
        
        KeyDeserializer keyDeserializer = deserializationContext.keyDeserializerInstance(annotated, deserDef);
        
        assertNull(keyDeserializer);
    }
}