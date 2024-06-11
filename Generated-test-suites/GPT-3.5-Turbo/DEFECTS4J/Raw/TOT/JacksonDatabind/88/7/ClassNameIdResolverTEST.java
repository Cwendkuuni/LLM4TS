package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Assert.assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Exception exception = null;
        try {
            resolver.registerSubtype(Object.class, "Object");
        } catch (Exception e) {
            exception = e;
        }
        Assert.assertNull(exception);
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue(new Object());
        Assert.assertNotNull(id);
        Assert.assertTrue(id.length() > 0);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType(new Object(), Object.class);
        Assert.assertNotNull(id);
        Assert.assertTrue(id.length() > 0);
    }

    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Exception exception = null;
        try {
            resolver.typeFromId(null, "com.example.MyClass");
        } catch (Exception e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof IOException);
    }

    @Test
    public void testPrivateMethod_idFrom() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.getClass().getDeclaredMethod("_idFrom", Object.class, Class.class, TypeFactory.class)
                .invoke(resolver, new Object(), Object.class, null).toString();
        Assert.assertNotNull(id);
        Assert.assertTrue(id.length() > 0);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Assert.assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}