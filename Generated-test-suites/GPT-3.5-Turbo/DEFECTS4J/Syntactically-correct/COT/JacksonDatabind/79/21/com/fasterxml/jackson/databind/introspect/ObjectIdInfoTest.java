
package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    // Test case for constructor with all parameters
    @Test
    public void testConstructorWithAllParameters() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test case for constructor with all parameters and alwaysAsId as true
    @Test
    public void testConstructorWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    // Test case for constructor with deprecated parameters
    @Test
    public void testConstructorWithDeprecatedParameters() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test case for withAlwaysAsId method
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, objectIdInfo.withAlwaysAsId(false).getPropertyName());
        assertEquals(scope, objectIdInfo.withAlwaysAsId(false).getScope());
        assertEquals(generator, objectIdInfo.withAlwaysAsId(false).getGeneratorType());
        assertEquals(resolver, objectIdInfo.withAlwaysAsId(false).getResolverType());
        assertFalse(objectIdInfo.withAlwaysAsId(false).getAlwaysAsId());

        assertEquals(propertyName, objectIdInfo.withAlwaysAsId(true).getPropertyName());
        assertEquals(scope, objectIdInfo.withAlwaysAsId(true).getScope());
        assertEquals(generator, objectIdInfo.withAlwaysAsId(true).getGeneratorType());
        assertEquals(resolver, objectIdInfo.withAlwaysAsId(true).getResolverType());
        assertTrue(objectIdInfo.withAlwaysAsId(true).getAlwaysAsId());
    }

}
