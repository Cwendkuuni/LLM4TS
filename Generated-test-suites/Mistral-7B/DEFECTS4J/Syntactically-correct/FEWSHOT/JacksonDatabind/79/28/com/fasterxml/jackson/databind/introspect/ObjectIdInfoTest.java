package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoCreation() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdentityInfoStdGenerator> generator = IdentityInfoStdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.IdentityInfoStdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());

        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfoWithResolver = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.IdentityInfoStdGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfoWithResolver.toString());
        assertNotNull(objectIdInfoWithResolver.getResolverType());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = new ObjectIdInfo(propertyName, scope, generator, true, null);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.IdentityInfoStdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoCreationWithNullPropertyName() {
        new ObjectIdInfo(null, String.class, IdentityInfoStdGenerator.class, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoCreationWithNullScope() {
        new ObjectIdInfo(new PropertyName("name"), null, IdentityInfoStdGenerator.class, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoCreationWithNullGenerator() {
        new ObjectIdInfo(new PropertyName("name"), String.class, null, false, null);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdentityInfoStdGenerator> generator = IdentityInfoStdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals("ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.IdentityInfoStdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithNullPropertyName() {
        Class<String> scope = String.class;
        Class<IdentityInfoStdGenerator> generator = IdentityInfoStdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator, false, null);
        assertEquals("ObjectIdInfo: propName=null, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.IdentityInfoStdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertNull(objectIdInfo.getPropertyName());
    }
}