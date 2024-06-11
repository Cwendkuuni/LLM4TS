package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);

        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Integer.class, ObjectIdGenerator.class);

        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Integer.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorUsingDeprecatedMethod() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("name", Long.class, ObjectIdGenerator.class);

        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Long.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Float.class, ObjectIdGenerator.class, ObjectIdResolver.class);

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertNotNull(newObjectIdInfo);
        assertEquals("name", newObjectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Float.class, newObjectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, newObjectIdInfo.getGeneratorType());
        assertEquals(ObjectIdResolver.class, newObjectIdInfo.getResolverType());
        assertEquals(true, newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Double.class, ObjectIdGenerator.class, ObjectIdResolver.class);

        String expected = "ObjectIdInfo: propName=name, scope=java.lang.Double, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
  
        assertEquals(expected, objectIdInfo.toString());
    }
}