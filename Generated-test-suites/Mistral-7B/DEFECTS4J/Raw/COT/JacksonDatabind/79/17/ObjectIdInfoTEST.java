package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;

/**
 * Test class for ObjectIdInfo
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdGenerator> generator = IdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, ObjectIdResolver.class);
        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdConstructor() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdGenerator> generator = IdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, ObjectIdResolver.class);
        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoDefaultConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        assertNotNull(objectIdInfo);
        assertNull(objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdMethod() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdGenerator> generator = IdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, ObjectIdResolver.class);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
        assertEquals(objectIdInfo, objectIdInfoWithAlwaysAsId.getGeneratorType());
        assertEquals(objectIdInfo, objectIdInfoWithAlwaysAsId.getPropertyName());
        assertEquals(objectIdInfo, objectIdInfoWithAlwaysAsId.getScope());
        assertEquals(objectIdInfo, objectIdInfoWithAlwaysAsId.getResolverType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullResolverType() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdGenerator> generator = IdGenerator.class;
        new ObjectIdInfo(propertyName, scope, generator, false, null);
    }

    @Test
    public void testToStringMethod() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdGenerator> generator = IdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, ObjectIdResolver.class);
        String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.IdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoWithCustomResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<IdGenerator> generator = IdGenerator.class;
        ObjectIdResolver customResolver = mock(ObjectIdResolver.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, customResolver);
        assertEquals(customResolver, objectIdInfo.getResolverType());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoWithNullGeneratorType() {
        new ObjectIdInfo(null, null, null, false, ObjectIdResolver.class);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoWithNullPropertyName() {
        new ObjectIdInfo(null, null, null, false, ObjectIdResolver.class);
    }
}