package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfo() {
        PropertyName propName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdNumberObjectIdGenerator> generator = IdNumberObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objIdInfo1 = new ObjectIdInfo(propName, scope, generator);
        ObjectIdInfo objIdInfo2 = new ObjectIdInfo(propName, scope, generator, true, resolver);

        assertEquals(propName, objIdInfo1.getPropertyName());
        assertEquals(scope, objIdInfo1.getScope());
        assertEquals(generator, objIdInfo1.getGeneratorType());
        assertNull(objIdInfo1.getResolverType());
        assertFalse(objIdInfo1.getAlwaysAsId());

        assertEquals(propName, objIdInfo2.getPropertyName());
        assertEquals(scope, objIdInfo2.getScope());
        assertEquals(generator, objIdInfo2.getGeneratorType());
        assertSame(resolver, objIdInfo2.getResolverType());
        assertTrue(objIdInfo2.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithNullProperties() {
        ObjectIdInfo objIdInfo = new ObjectIdInfo(null, null, null, false, null);

        assertNull(objIdInfo.getPropertyName());
        assertNull(objIdInfo.getScope());
        assertNull(objIdInfo.getGeneratorType());
        assertNull(objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithDefaultResolver() {
        PropertyName propName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdNumberObjectIdGenerator> generator = IdNumberObjectIdGenerator.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propName, scope, generator);

        assertEquals(propName, objIdInfo.getPropertyName());
        assertEquals(scope, objIdInfo.getScope());
        assertEquals(generator, objIdInfo.getGeneratorType());
        assertSame(SimpleObjectIdResolver.class, objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithCustomResolver() {
        PropertyName propName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdNumberObjectIdGenerator> generator = IdNumberObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propName, scope, generator, false, resolver);

        assertEquals(propName, objIdInfo.getPropertyName());
        assertEquals(scope, objIdInfo.getScope());
        assertEquals(generator, objIdInfo.getGeneratorType());
        assertSame(resolver, objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdNumberObjectIdGenerator> generator = IdNumberObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propName, scope, generator, true, resolver);

        assertEquals(propName, objIdInfo.getPropertyName());
        assertEquals(scope, objIdInfo.getScope());
        assertEquals(generator, objIdInfo.getGeneratorType());
        assertSame(resolver, objIdInfo.getResolverType());
        assertTrue(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdSetter() {
        PropertyName propName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<IdNumberObjectIdGenerator> generator = IdNumberObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propName, scope, generator, false, resolver);

        ObjectIdInfo objIdInfoWithAlwaysAsId = objIdInfo.withAlwaysAsId(true);

        assertEquals(objIdInfo, objIdInfoWithAlwaysAsId);
        assertTrue(objIdInfoWithAlwaysAsId.getAlwaysAsId());
    }
}