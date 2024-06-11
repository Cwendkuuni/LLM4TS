
package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {
    
    private ObjectIdInfo objectIdInfo;
    private PropertyName propertyName;
    private Class<?> scope;
    private Class<? extends ObjectIdGenerator<?>> generator;
    private Class<? extends ObjectIdResolver> resolver;
    private boolean alwaysAsId;
    
    @Before
    public void setUp() {
        propertyName = new PropertyName("test");
        scope = String.class;
        generator = ObjectIdGeneratorTestClass.class;
        resolver = SimpleObjectIdResolverTestClass.class;
        alwaysAsId = true;
        
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
    }
    
    @Test
    public void testConstructorWithAlwaysAsId() {
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());    
    }
    
    @Test
    public void testConstructorWithoutAlwaysAsId() {
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());    
    }
    
    @Test
    public void testDeprecatedConstructor() {
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());    
    }
    
    @Test
    public void testDeprecatedConstructorWithoutResolver() {
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());   
        
    }
    
    @Test
    public void testDeprecatedConstructorWithString() {
        objectIdInfo = new ObjectIdInfo("test", scope, generator);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());   
        
    }
    
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
        
        assertEquals(objectIdInfo.getPropertyName(), updatedObjectIdInfo.getPropertyName());
        assertEquals(objectIdInfo.getScope(), updatedObjectIdInfo.getScope());
        assertEquals(objectIdInfo.getGeneratorType(), updatedObjectIdInfo.getGeneratorType());
        assertEquals(objectIdInfo.getResolverType(), updatedObjectIdInfo.getResolverType());
        assertFalse(updatedObjectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        String expected = "ObjectIdInfo: propName=" + propertyName
                + ", scope=" + scope.getName()
                + ", generatorType=" + generator.getName()
                + ", alwaysAsId=" + alwaysAsId;
        
        assertEquals(expected, objectIdInfo.toString());
    }
    
    private static class ObjectIdGeneratorTestClass extends ObjectIdGenerator<Object> {
        
        @Override
        public ObjectIdGenerator<Object> newForSerialization(Object contextual) {
            return null;
        }

        @Override
        public ObjectIdGenerator<Object> newForSerialization(Object contextual,
                ObjectIdResolver resolver) {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return false;
        }

        @Override
        public Class<?> getScope() {
            return null;
        }

        @Override
        public Object generateId(Object forPojo) {
            return null;
        }

        @Override
        public IdKey key(Object key) {
            return null;
        }

        @Override
        public Object resolveId(ObjectIdGenerator.IdKey id) {
            return null;
        }
        
    }
    
    private static class SimpleObjectIdResolverTestClass extends SimpleObjectIdResolver {
        
    }
}
