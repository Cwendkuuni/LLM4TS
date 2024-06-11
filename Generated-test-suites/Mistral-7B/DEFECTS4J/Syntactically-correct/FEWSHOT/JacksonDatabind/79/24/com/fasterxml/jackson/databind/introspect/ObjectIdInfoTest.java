package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedObjectIdentifier;
import com.fasterxml.jackson.databind.introspect.ValueIdentifier;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoCreation() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = null;
        Class<? extends ObjectIdResolver> resolver = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());

        generator = AnnotationIntrospector.findIdGenerator(NestedObjectIdentifier.class);
        Class<? extends ObjectIdResolver> simpleResolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfoWithIdGen = new ObjectIdInfo(propertyName, scope, generator, false, simpleResolver);
        assertNotNull(objectIdInfoWithIdGen);
        assertEquals(propertyName, objectIdInfoWithIdGen.getPropertyName());
        assertEquals(scope, objectIdInfoWithIdGen.getScope());
        assertNotNull(objectIdInfoWithIdGen.getGeneratorType());
        assertNotNull(objectIdInfoWithIdGen.getResolverType());
        assertFalse(objectIdInfoWithIdGen.getAlwaysAsId());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfoWithIdGen.withAlwaysAsId(true);
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
        assertNotSame(objectIdInfoWithIdGen, objectIdInfoWithAlwaysAsId);

        ObjectIdInfo objectIdInfoWithoutResolver = new ObjectIdInfo(propertyName, scope, generator, true, null);
        assertNotNull(objectIdInfoWithoutResolver);
        assertEquals(propertyName, objectIdInfoWithoutResolver.getPropertyName());
        assertEquals(scope, objectIdInfoWithoutResolver.getScope());
        assertNotNull(objectIdInfoWithoutResolver.getGeneratorType());
        assertNull(objectIdInfoWithoutResolver.getResolverType());
        assertTrue(objectIdInfoWithoutResolver.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = null;
        Class<? extends ObjectIdResolver> resolver = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        String toString = objectIdInfo.toString();
        String expectedToString = "ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=null, alwaysAsId=false";
        assertEquals(expectedToString, toString);

        generator = AnnotationIntrospector.findIdGenerator(NestedObjectIdentifier.class);
        Class<? extends ObjectIdResolver> simpleResolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfoWithIdGen = new ObjectIdInfo(propertyName, scope, generator, false, simpleResolver);
        toString = objectIdInfoWithIdGen.toString();
        expectedToString = "ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.NestedObjectIdentifier, alwaysAsId=false";
        assertEquals(expectedToString, toString);

        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfoWithIdGen.withAlwaysAsId(true);
        toString = objectIdInfoWithAlwaysAsId.toString();
        expectedToString = "ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.NestedObjectIdentifier, alwaysAsId=true";
        assertEquals(expectedToString, toString);

        ObjectIdInfo objectIdInfoWithoutResolver = new ObjectIdInfo(propertyName, scope, generator, true, null);
        toString = objectIdInfoWithoutResolver.toString();
        expectedToString = "ObjectIdInfo: propName=id, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.NestedObjectIdentifier, alwaysAsId=true";
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testGetters() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = null;
        Class<? extends ObjectIdResolver> resolver = null;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertSame(Object.class, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());

        generator = AnnotationIntrospector.findIdGenerator(NestedObjectIdentifier.class);
        resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfoWithIdGen = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals(propertyName, objectIdInfoWithIdGen.getPropertyName());
        assertSame(Object.class, objectIdInfoWithIdGen.getScope());
        assertNotNull(objectIdInfoWithIdGen.getGeneratorType());
        assertNotNull(objectIdInfoWithIdGen.getResolverType());
        assertFalse(objectIdInfoWithIdGen.getAlwaysAsId());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfoWithIdGen.withAlwaysAsId(true);
        assertEquals(propertyName, objectIdInfoWithAlwaysAsId.getPropertyName());
        assertSame(Object.class, objectIdInfoWithAlwaysAsId.getScope());
        assertNotNull(objectIdInfoWithAlwaysAsId.getGeneratorType());
        assertNotNull(objectIdInfoWithAlwaysAsId.getResolverType());
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());

        ObjectIdInfo objectIdInfoWithoutResolver = new ObjectIdInfo(propertyName, scope, generator, true, null);
        assertEquals(propertyName, objectIdInfoWithoutResolver.getPropertyName());
        assertSame(Object.class, objectIdInfoWithoutResolver.getScope());
        assertNotNull(objectIdInfoWithoutResolver.getGeneratorType());
        assertNull(objectIdInfoWithoutResolver.getResolverType());
        assertTrue(objectIdInfoWithoutResolver.getAlwaysAsId());
    }

    @Test
    public void testDeprecatedConstructors() {
        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = null;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertNull(objectIdInfo._resolver);

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true);
        assertNull(objectIdInfo._resolver);

        String name = "id";
        objectIdInfo = new ObjectIdInfo(name, scope, generator);
        assertEquals(new PropertyName(name), objectIdInfo._propertyName);
    }
}