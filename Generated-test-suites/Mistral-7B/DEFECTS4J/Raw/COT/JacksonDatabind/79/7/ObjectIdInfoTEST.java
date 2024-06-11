package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SimpleObjectIdResolver;

import org.junit.Test;

/**
 * Test class for ObjectIdInfo
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdByIntGenerator> generator = IdByIntGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.databind.introspect.IdByIntGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGeneratorAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdByIntGenerator> generator = IdByIntGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.databind.introspect.IdByIntGenerator, alwaysAsId=true", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGeneratorAndResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdByIntGenerator> generator = IdByIntGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.databind.introspect.IdByIntGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructorWithNullPropertyName() {
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdByIntGenerator> generator = IdByIntGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructorWithNullScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<IdByIntGenerator> generator = IdByIntGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null, generator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructorWithNullGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructorWithNullResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdByIntGenerator> generator = IdByIntGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdByIntGenerator> generator = IdByIntGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.databind.introspect.IdByIntGenerator, alwaysAsId=true", withAlwaysAsId.toString());
    }

    @Test
    public void testObjectIdInfoGetters() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdByIntGenerator> generator = IdByIntGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, SimpleObjectIdResolver.class);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }
}