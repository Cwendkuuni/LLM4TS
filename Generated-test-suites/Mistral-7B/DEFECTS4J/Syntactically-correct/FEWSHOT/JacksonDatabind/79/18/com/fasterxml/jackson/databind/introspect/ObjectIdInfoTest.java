package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo objectIdInfo3 = new ObjectIdInfo(propertyName.getId(), scope, generator);
        ObjectIdInfo objectIdInfo4 = new ObjectIdInfo(propertyName, scope, generator, true, null);

        assertEquals(propertyName, objectIdInfo1.getPropertyName());
        assertEquals(scope, objectIdInfo1.getScope());
        assertEquals(generator, objectIdInfo1.getGeneratorType());
        assertEquals(resolver, objectIdInfo1.getResolverType());
        assertEquals(false, objectIdInfo1.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfo2.getPropertyName());
        assertEquals(scope, objectIdInfo2.getScope());
        assertEquals(generator, objectIdInfo2.getGeneratorType());
        assertNull(objectIdInfo2.getResolverType());
        assertEquals(false, objectIdInfo2.getAlwaysAsId());

        assertEquals(propertyName.getId(), objectIdInfo3.getPropertyName().getId());
        assertEquals(scope, objectIdInfo3.getScope());
        assertEquals(generator, objectIdInfo3.getGeneratorType());
        assertNull(objectIdInfo3.getResolverType());
        assertEquals(false, objectIdInfo3.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfo4.getPropertyName());
        assertEquals(scope, objectIdInfo4.getScope());
        assertEquals(generator, objectIdInfo4.getGeneratorType());
        assertNull(objectIdInfo4.getResolverType());
        assertEquals(true, objectIdInfo4.getAlwaysAsId());

        ObjectIdInfo objectIdInfo5 = objectIdInfo1.withAlwaysAsId(true);
        assertEquals(objectIdInfo1, objectIdInfo5);
        assertEquals(true, objectIdInfo5.getAlwaysAsId());
    }

    @Test
    public void testNullArguments() {
        assertNull(new ObjectIdInfo(null, null, null, false, null));
        assertNull(new ObjectIdInfo(null, null, CustomObjectIdGenerator.class, false, null));
        assertNull(new ObjectIdInfo(new PropertyName("name"), null, null, false, null));
        assertNull(new ObjectIdInfo(new PropertyName("name"), null, CustomObjectIdGenerator.class, false, null));
        assertNull(new ObjectIdInfo(new PropertyName("name"), null, CustomObjectIdGenerator.class, true, null));
        assertNull(new ObjectIdInfo(new PropertyName("name"), null, CustomObjectIdGenerator.class, false, CustomObjectIdResolver.class));
        assertNull(new ObjectIdInfo(new PropertyName("name"), null, CustomObjectIdGenerator.class, false, null));
        assertNull(new ObjectIdInfo(new PropertyName("name"), null, null, true, CustomObjectIdResolver.class));
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo objectIdInfo3 = new ObjectIdInfo(propertyName.getId(), scope, generator);
        ObjectIdInfo objectIdInfo4 = new ObjectIdInfo(propertyName, scope, generator, true, null);

        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=false",
                objectIdInfo1.toString());
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=false",
                objectIdInfo1.toString());
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=false",
                objectIdInfo2.toString());
        assertEquals("ObjectIdInfo: propName=name, scope=null, generatorType=null, alwaysAsId=false",
                objectIdInfo3.toString());
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=true",
                objectIdInfo4.toString());
    }
}