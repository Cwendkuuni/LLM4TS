package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;

public class ObjectIdInfoTest {
    @Test
    public void testConstructor1() {
        PropertyName name = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);
        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
        assertNotNull(info.toString());
    }

    @Test
    public void testConstructor2() {
        PropertyName name = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
        assertNotNull(info.toString());
    }

    @Test
    public void testConstructor3() {
        String name = "name";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        assertEquals(new PropertyName(name), info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
        assertNotNull(info.toString());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo info1 = new ObjectIdInfo(name, scope, gen, resolver);
        ObjectIdInfo info2 = info1.withAlwaysAsId(true);

        assertEquals(name, info2.getPropertyName());
        assertEquals(scope, info2.getScope());
        assertEquals(gen, info2.getGeneratorType());
        assertEquals(resolver, info2.getResolverType());
        assertTrue(info2.getAlwaysAsId());
        assertNotNull(info2.toString());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("name");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);
        assertNotNull(info.toString());
    }

    // Additional methods can be tested similarly
}

class MyObjectIdGenerator implements ObjectIdGenerator<Object> {

    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object pojo) {

    }

    @Override
    public ObjectIdGenerator<Object> forScope(Class<?> scope) {
        return null;
    }

    @Override
    public ObjectIdResolver resolver(SerializerProvider provider) {
        return null;
    }

    @Override
    public ObjectIdGenerator<Object> newForSerialization(Object context) {
        return null;
    }

    @Override
    public boolean canUseFor(ObjectIdGenerator<?> gen) {
        return false;
    }

    @Override
    public Object generateId(Object forPojo) {
        return null;
    }
}

class MyObjectIdResolver implements ObjectIdResolver {

    @Override
    public void bindItem(IdKey id, Object pojo) {

    }

    @Override
    public Object resolveId(IdKey id) {
        return null;
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return null;
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        return false;
    }
}