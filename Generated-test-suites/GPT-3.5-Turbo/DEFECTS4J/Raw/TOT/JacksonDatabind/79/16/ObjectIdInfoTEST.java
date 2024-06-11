package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    }

    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals("id", propertyName.getSimpleName());
    }

    @Test
    public void testGetScope() {
        Class<?> scope = objectIdInfo.getScope();
        assertEquals(Object.class, scope);
    }

    @Test
    public void testGetGeneratorType() {
        Class<? extends ObjectIdGenerator<?>> generator = objectIdInfo.getGeneratorType();
        assertEquals(MyObjectIdGenerator.class, generator);
    }

    @Test
    public void testGetResolverType() {
        Class<? extends ObjectIdResolver> resolver = objectIdInfo.getResolverType();
        assertEquals(MyObjectIdResolver.class, resolver);
    }

    @Test
    public void testGetAlwaysAsId() {
        boolean alwaysAsId = objectIdInfo.getAlwaysAsId();
        assertFalse(alwaysAsId); // Default value should be false
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    private static class MyObjectIdGenerator implements ObjectIdGenerator<Object> {
        // Implementation details don't matter for this test
    }

    private static class MyObjectIdResolver extends SimpleObjectIdResolver {
        // Implementation details don't matter for this test
    }
}