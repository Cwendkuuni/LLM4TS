package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructorWithResolver() {
        PropertyName name = new PropertyName("foo");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);
        
        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        PropertyName name = new PropertyName("foo");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        
        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testDeprecatedConstructor() {
        String name = "foo";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        
        assertEquals(new PropertyName(name), info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("foo");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        
        assertFalse(info.getAlwaysAsId());
        
        ObjectIdInfo newInfo = info.withAlwaysAsId(true);
        
        assertTrue(newInfo.getAlwaysAsId());
        assertEquals(name, newInfo.getPropertyName());
        assertEquals(scope, newInfo.getScope());
        assertEquals(gen, newInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, newInfo.getResolverType());
    }
    
    @Test
    public void testToString() {
        PropertyName name = new PropertyName("foo");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        
        String expected = "ObjectIdInfo: propName=" + name
                + ", scope=" + scope.getName()
                + ", generatorType=" + gen.getName()
                + ", alwaysAsId=false";

        assertEquals(expected, info.toString());
    }

}