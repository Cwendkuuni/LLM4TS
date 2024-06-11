package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor_withNameScopeGenResolver() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor_withNameScopeGen() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(null, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor_withStringScopeGen() {
        String name = "testName";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(null, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor_withPropScopeGenAlwaysAsIdResolver() {
        PropertyName prop = new PropertyName("testProp");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = true;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, alwaysAsId, resolver);

        assertEquals(prop, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId_withFalse() {
        PropertyName prop = new PropertyName("testProp");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = false;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, alwaysAsId, resolver);
        ObjectIdInfo updated = objectIdInfo.withAlwaysAsId(true);

        assertEquals(prop, updated.getPropertyName());
        assertEquals(scope, updated.getScope());
        assertEquals(gen, updated.getGeneratorType());
        assertEquals(resolver, updated.getResolverType());
        assertEquals(true, updated.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId_withSameState() {
        PropertyName prop = new PropertyName("testProp");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = false;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, alwaysAsId, resolver);
        ObjectIdInfo updated = objectIdInfo.withAlwaysAsId(alwaysAsId);

        assertEquals(objectIdInfo, updated);
    }

    @Test
    public void testToString() {
        PropertyName prop = new PropertyName("testProp");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        boolean alwaysAsId = false;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(prop, scope, gen, alwaysAsId, resolver);
        String expected = "ObjectIdInfo: propName=" + prop + ", scope=" + scope.getName() + ", generatorType="
                + gen.getName() + ", alwaysAsId=" + alwaysAsId;

        assertEquals(expected, objectIdInfo.toString());
    }
}