
package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Assert;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithAllParameters() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        Assert.assertNotNull(objectIdInfo);
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithAllParametersAndAlwaysAsIdTrue() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        Assert.assertNotNull(objectIdInfo);
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithThreeParameters() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        Assert.assertNotNull(objectIdInfo);
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testDeprecationConstructorWithThreeParameters() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        Assert.assertNotNull(objectIdInfo);
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testDeprecationConstructorWithStringPropertyName() {
        String propertyName = "test";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        Assert.assertNotNull(objectIdInfo);
        Assert.assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);

        Assert.assertNotNull(objectIdInfo);
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertTrue(objectIdInfo.getAlwaysAsId());
    }

    private static class MyObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public void setScope(Class<?> clazz) {
        }

        @Override
        public Class<?> getScope() {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return false;
        }

        @Override
        public ObjectIdGenerator<Object> forScope(Class<?> scope) {
            return null;
        }

        @Override
        public ObjectIdGenerator<Object> newForSerialization(Object context) {
            return null;
        }

        @Override
        public ObjectIdResolver newResolver() {
            return null;
        }

        @Override
        public Object generateId(Object forPojo) {
            return null;
        }
    }

    private static class MyObjectIdResolver implements ObjectIdResolver {
        @Override
        public void bindItem(IdKey id, Object ob) {
        }

        @Override
        public Object resolveId(IdKey id) {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdResolver resolverType) {
            return false;
        }

        @Override
        public ObjectIdResolver newForDeserialization(Object context) {
            return null;
        }
    }
}

