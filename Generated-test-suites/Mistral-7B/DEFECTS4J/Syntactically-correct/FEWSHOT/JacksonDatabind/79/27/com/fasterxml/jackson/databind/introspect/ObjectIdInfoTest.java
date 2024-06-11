package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = SimpleObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertEquals(propertyName, objectIdInfo1.getPropertyName());
        assertEquals(scope, objectIdInfo1.getScope());
        assertEquals(generator, objectIdInfo1.getGeneratorType());
        assertEquals(resolver, objectIdInfo1.getResolverType());
        assertFalse(objectIdInfo1.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfo2.getPropertyName());
        assertEquals(scope, objectIdInfo2.getScope());
        assertEquals(generator, objectIdInfo2.getGeneratorType());
        assertEquals(resolver, objectIdInfo2.getResolverType());
        assertFalse(objectIdInfo2.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = SimpleObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        ObjectIdInfo objectIdInfoWithAlwaysAsIdOff = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsIdOff);
        assertTrue(objectIdInfo.getAlwaysAsId());
        assertFalse(objectIdInfoWithAlwaysAsIdOff.getAlwaysAsId());
    }
}