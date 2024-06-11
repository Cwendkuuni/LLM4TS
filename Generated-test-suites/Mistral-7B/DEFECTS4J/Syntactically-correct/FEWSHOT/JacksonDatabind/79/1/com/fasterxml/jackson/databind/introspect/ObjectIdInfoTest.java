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
        Class<ObjectIdGenerators.ByName> generator = ObjectIdGenerators.ByName.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        ObjectIdInfo objectIdInfoWithResolver = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfoWithAlwaysAsId.getPropertyName());
        assertEquals(scope, objectIdInfoWithAlwaysAsId.getScope());
        assertEquals(generator, objectIdInfoWithAlwaysAsId.getGeneratorType());
        assertNull(objectIdInfoWithAlwaysAsId.getResolverType());
        assertEquals(true, objectIdInfoWithAlwaysAsId.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfoWithResolver.getPropertyName());
        assertEquals(scope, objectIdInfoWithResolver.getScope());
        assertNull(objectIdInfoWithResolver.getGeneratorType());
        assertEquals(resolver, objectIdInfoWithResolver.getResolverType());
        assertEquals(false, objectIdInfoWithResolver.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoDeprecatedConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<ObjectIdGenerators.ByName> generator = ObjectIdGenerators.ByName.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());

        assertEquals(propertyName, objectIdInfoWithAlwaysAsId.getPropertyName());
        assertEquals(scope, objectIdInfoWithAlwaysAsId.getScope());
        assertEquals(generator, objectIdInfoWithAlwaysAsId.getGeneratorType());
        assertNull(objectIdInfoWithAlwaysAsId.getResolverType());
        assertEquals(true, objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithNullArguments() {
        assertNull(new ObjectIdInfo(null, null, null));
    }
}