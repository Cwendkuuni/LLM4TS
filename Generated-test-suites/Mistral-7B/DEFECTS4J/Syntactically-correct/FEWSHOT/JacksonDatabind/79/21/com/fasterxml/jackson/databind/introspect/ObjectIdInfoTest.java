package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    @Test
    public void testNewInstance() {
        PropertyName name = new PropertyName("name");
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;
        Class<ObjectIdResolver> resolverType = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, Object.class, generatorType, false, resolverType);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertSame(Object.class, objectIdInfo.getScope());
        assertSame(generatorType, objectIdInfo.getGeneratorType());
        assertSame(resolverType, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScope() {
        PropertyName name = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;
        Class<ObjectIdResolver> resolverType = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generatorType, resolverType);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generatorType, objectIdInfo.getGeneratorType());
        assertSame(resolverType, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorType() {
        PropertyName name = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generatorType);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generatorType, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorTypeAndAlwaysAsId() {
        PropertyName name = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generatorType, true);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generatorType, objectIdInfo.getGeneratorType());
        assertSame(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorTypeAndResolverType() {
        PropertyName name = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generatorType, null, resolverType);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generatorType, objectIdInfo.getGeneratorType());
        assertSame(resolverType, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testNewInstanceWithAlwaysAsId() {
        PropertyName name = new PropertyName("name");
        Class<Object> scope = Object.class;
        Class<ObjectIdGenerator<Object>> generatorType = ObjectIdGenerator.class;
        Class<ObjectIdResolver> resolverType = ObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generatorType, false, resolverType).withAlwaysAsId(true);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generatorType, objectIdInfo.getGeneratorType());
        assertSame(resolverType, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }
}