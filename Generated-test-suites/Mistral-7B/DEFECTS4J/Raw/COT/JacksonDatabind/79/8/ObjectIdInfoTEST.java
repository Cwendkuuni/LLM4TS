package com.example.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfo() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdInfo> objectIdInfoClass = ObjectIdInfo.class;
        try {
            Constructor<ObjectIdInfo> constructor = objectIdInfoClass.getConstructor(PropertyName.class,
                    Class.class, Class.class, boolean.class, Class.class);
            ObjectIdInfo objectIdInfo = constructor.newInstance(name, Object.class,
                    ObjectIdGenerator.IdentityHashCodeObjectIdGenerator.class, false,
                    ObjectIdResolver.class);
            assertNotNull(objectIdInfo);
            assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
            assertEquals(Object.class, objectIdInfo.getScope());
            assertEquals(ObjectIdGenerator.IdentityHashCodeObjectIdGenerator.class,
                    objectIdInfo.getGeneratorType());
            assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
            assertFalse(objectIdInfo.getAlwaysAsId());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            fail("Failed to create ObjectIdInfo instance");
        }
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdInfo> objectIdInfoClass = ObjectIdInfo.class;
        try {
            ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, Object.class,
                    ObjectIdGenerator.IdentityHashCodeObjectIdGenerator.class, true,
                    ObjectIdResolver.class);
            ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
            assertNotSame(objectIdInfo, newObjectIdInfo);
            assertFalse(newObjectIdInfo.getAlwaysAsId());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create ObjectIdInfo instance with alwaysAsId parameter");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoNullPropertyName() {
        Class<ObjectIdInfo> objectIdInfoClass = ObjectIdInfo.class;
        try {
            Constructor<ObjectIdInfo> constructor = objectIdInfoClass.getConstructor(PropertyName.class,
                    Class.class, Class.class, boolean.class, Class.class);
            ObjectIdInfo objectIdInfo = constructor.newInstance(null, Object.class,
                    ObjectIdGenerator.IdentityHashCodeObjectIdGenerator.class, false,
                    ObjectIdResolver.class);
            fail("Expected IllegalArgumentException with null PropertyName");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoNullResolver() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdInfo> objectIdInfoClass = ObjectIdInfo.class;
        try {
            Constructor<ObjectIdInfo> constructor = objectIdInfoClass.getConstructor(PropertyName.class,
                    Class.class, Class.class, boolean.class, Class.class);
            ObjectIdInfo objectIdInfo = constructor.newInstance(name, Object.class,
                    ObjectIdGenerator.IdentityHashCodeObjectIdGenerator.class, false, null);
            fail("Expected IllegalArgumentException with null resolver");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdInfo> objectIdInfoClass = ObjectIdInfo.class;
        try {
            Constructor<ObjectIdInfo> constructor = objectIdInfoClass.getConstructor(PropertyName.class,
                    Class.class, Class.class, boolean.class, Class.class);
            ObjectIdInfo objectIdInfo = constructor.newInstance(name, Object.class,
                    ObjectIdGenerator.IdentityHashCodeObjectIdGenerator.class, false,
                    ObjectIdResolver.class);
            String toString = objectIdInfo.toString();
            String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$IdentityHashCodeObjectIdGenerator, alwaysAsId=false";
            assertEquals(expected, toString);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail("Failed to create ObjectIdInfo instance");
        }
    }
}