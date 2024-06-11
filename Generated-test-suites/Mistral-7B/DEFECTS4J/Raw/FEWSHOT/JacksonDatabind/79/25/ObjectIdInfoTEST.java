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
    public void testObjectIdInfo() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        ObjectIdInfo objectIdInfoWithPropertyName = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
        ObjectIdInfo objectIdInfoWithScope = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo objectIdInfoWithGenerator = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo objectIdInfoWithResolver = new ObjectIdInfo(propertyName, scope, generator, false, null);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfoWithAlwaysAsId.getPropertyName());
        assertEquals(scope, objectIdInfoWithAlwaysAsId.getScope());
        assertNotNull(objectIdInfoWithAlwaysAsId.getGeneratorType());
        assertNotNull(objectIdInfoWithAlwaysAsId.getResolverType());
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfoWithPropertyName.getPropertyName());
        assertEquals(scope, objectIdInfoWithPropertyName.getScope());
        assertNotNull(objectIdInfoWithPropertyName.getGeneratorType());
        assertNotNull(objectIdInfoWithPropertyName.getResolverType());
        assertFalse(objectIdInfoWithPropertyName.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfoWithScope.getPropertyName());
        assertEquals(scope, objectIdInfoWithScope.getScope());
        assertNotNull(objectIdInfoWithScope.getGeneratorType());
        assertNotNull(objectIdInfoWithScope.getResolverType());
        assertFalse(objectIdInfoWithScope.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfoWithGenerator.getPropertyName());
        assertEquals(scope, objectIdInfoWithGenerator.getScope());
        assertNotNull(objectIdInfoWithGenerator.getGeneratorType());
        assertNotNull(objectIdInfoWithGenerator.getResolverType());
        assertFalse(objectIdInfoWithGenerator.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfoWithResolver.getPropertyName());
        assertNull(objectIdInfoWithResolver.getScope());
        assertNull(objectIdInfoWithResolver.getGeneratorType());
        assertNull(objectIdInfoWithResolver.getResolverType());
        assertFalse(objectIdInfoWithResolver.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertEquals(propertyName, objectIdInfoWithAlwaysAsId.getPropertyName());
        assertEquals(scope, objectIdInfoWithAlwaysAsId.getScope());
        assertNotNull(objectIdInfoWithAlwaysAsId.getGeneratorType());
        assertNotNull(objectIdInfoWithAlwaysAsId.getResolverType());
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());

        ObjectIdInfo objectIdInfoWithAlwaysAsId2 = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        assertEquals(objectIdInfoWithAlwaysAsId, objectIdInfoWithAlwaysAsId2);
    }

    @Test
    public void testObjectIdInfoWithJsonIdentityInfoAnnotation() {
        final class TestClass {
            @JsonIdentityInfo(generator = ObjectIdGenerators.IntArrayGenerator.class)
            private int id;
        }

        PropertyName propertyName = new PropertyName("id");
        Class<?> scope = TestClass.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerators.IntArrayGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullPropertyName() {
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        new ObjectIdInfo(null, scope, generator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullScope() {
        PropertyName propertyName = new PropertyName("name");
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        new ObjectIdInfo(propertyName, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullGeneratorType() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        new ObjectIdInfo(propertyName, scope, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullResolverType() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;

        new ObjectIdInfo(propertyName, scope, generator, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullPropertyNameAndNullScope() {
        Class<? extends ObjectIdGenerator<?>> generator = HashIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        new ObjectIdInfo(null, null, generator, false, resolver);
    }
}