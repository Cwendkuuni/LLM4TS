package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor1() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoPropertyIdGenerator> generator = IdentityInfoPropertyIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor2() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoPropertyIdGenerator> generator = IdentityInfoPropertyIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor3() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoPropertyIdGenerator> generator = IdentityInfoPropertyIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", scope, generator, false);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor4() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoPropertyIdGenerator> generator = IdentityInfoPropertyIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoPropertyIdGenerator> generator = IdentityInfoPropertyIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertNotSame(objectIdInfo, newObjectIdInfo);
        assertEquals(propertyName, newObjectIdInfo.getPropertyName());
        assertEquals(scope, newObjectIdInfo.getScope());
        assertEquals(generator, newObjectIdInfo.getGeneratorType());
        assertEquals(resolver, newObjectIdInfo.getResolverType());
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullPropertyName() {
        Class<String> scope = String.class;
        Class<IdentityInfoPropertyIdGenerator> generator = IdentityInfoPropertyIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<IdentityInfoPropertyIdGenerator> generator = IdentityInfoPropertyIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null, generator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdentityInfoPropertyIdGenerator> generator = IdentityInfoPropertyIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withResolverType(null);
    }
}