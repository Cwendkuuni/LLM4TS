package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

/**
 * Test class for {@link ObjectIdInfo}
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdentityHashCodeObjectIdGenerator();
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generator, objectIdInfo.getGeneratorType());
        assertSame(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdentityHashCodeObjectIdGenerator();
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(false);
        assertNotSame(objectIdInfo, withAlwaysAsId);
        assertFalse(withAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = new IdentityHashCodeObjectIdGenerator();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        String toString = objectIdInfo.toString();
        String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=null, alwaysAsId=false";
        assertEquals(expected, toString);
    }

    @Test
    @JsonIdentityInfo(generator = ObjectIdGenerator.IdentityHashCodeGenerator.class, property = "testProperty")
    public static class TestClass {
        private String id;
        private String testProperty;
    }

    @Test
    public void testObjectIdInfoWithJsonIdentityInfoAnnotation() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = TestClass.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.IdentityHashCodeGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        String toString = objectIdInfo.toString();
        String expected = "ObjectIdInfo: propName=testProperty, scope=class com.fasterxml.jackson.databind.introspect.test.ObjectIdInfoTest$TestClass, generatorType=class com.fasterxml.jackson.databind.Introspect$IdentityHashCodeGenerator, alwaysAsId=false";
        assertEquals(expected, toString);
    }
}

class IdentityHashCodeObjectIdGenerator {}