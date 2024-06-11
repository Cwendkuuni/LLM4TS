package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdentityInfoStandardSerializer> generator = IdentityInfoStandardSerializer.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
        assertEquals(propertyName, objectIdInfoWithAlwaysAsId.getPropertyName());
        assertEquals(scope, objectIdInfoWithAlwaysAsId.getScope());
        assertEquals(generator, objectIdInfoWithAlwaysAsId.getGeneratorType());
        assertEquals(resolver, objectIdInfoWithAlwaysAsId.getResolverType());
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdentityInfoStandardSerializer> generator = IdentityInfoStandardSerializer.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoDeprecatedConstructor() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdentityInfoStandardSerializer> generator = IdentityInfoStandardSerializer.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName.getId(), scope, generator);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdentityInfoStandardSerializer> generator = IdentityInfoStandardSerializer.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, null);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());

        ObjectIdInfo objectIdInfoWithAlwaysAsIdFalse = new ObjectIdInfo(propertyName, scope, generator, false, null);
        assertEquals(propertyName, objectIdInfoWithAlwaysAsIdFalse.getPropertyName());
        assertEquals(scope, objectIdInfoWithAlwaysAsIdFalse.getScope());
        assertEquals(generator, objectIdInfoWithAlwaysAsIdFalse.getGeneratorType());
        assertNull(objectIdInfoWithAlwaysAsIdFalse.getResolverType());
        assertFalse(objectIdInfoWithAlwaysAsIdFalse.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<IdentityInfoStandardSerializer> generator = IdentityInfoStandardSerializer.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        String expected = "ObjectIdInfo: propName=name, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.databind.ser.std.IdentityInfoStandardSerializer, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }
}