package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ObjectIdInfoTest {
    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertEquals(name, updatedObjectIdInfo.getPropertyName());
        assertEquals(scope, updatedObjectIdInfo.getScope());
        assertEquals(gen, updatedObjectIdInfo.getGeneratorType());
        assertEquals(resolver, updatedObjectIdInfo.getResolverType());
        assertEquals(true, updatedObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);

        String expected = "ObjectIdInfo: propName=id, scope=java.lang.String, generatorType=com.example.MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }

}

class MyObjectIdGenerator implements ObjectIdGenerator<Object> {
    @Override
    public void bindItem(SerializerProvider provider, Object id) {
        // Implementation details not relevant for testing
    }

    @Override
    public Object generateId(Object forPojo) {
        // Implementation details not relevant for testing
        return null;
    }

    @Override
    public IdGenerator<Object> forScope(Class<?> scope) {
        // Implementation details not relevant for testing
        return null;
    }
}

class MyObjectIdResolver implements ObjectIdResolver {
    @Override
    public void bindItem(IdKey id, Object pojo) {
        // Implementation details not relevant for testing
    }

    @Override
    public Object resolveId(IdKey id) {
        // Implementation details not relevant for testing
        return null;
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        // Implementation details not relevant for testing
        return null;
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        // Implementation details not relevant for testing
        return false;
    }
}