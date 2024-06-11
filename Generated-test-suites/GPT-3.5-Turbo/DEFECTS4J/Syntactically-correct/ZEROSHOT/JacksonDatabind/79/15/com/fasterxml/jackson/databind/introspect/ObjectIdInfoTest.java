package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setup() {
        PropertyName propertyName = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    }

    @Test
    public void testWithAlwaysAsId() {
        boolean state = true;
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(state);

        Assert.assertEquals(state, updatedObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = new PropertyName("testName");

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
    }

    @Test
    public void testGetScope() {
        Class<?> scope = String.class;

        Assert.assertEquals(scope, objectIdInfo.getScope());
    }

    @Test
    public void testGetGeneratorType() {
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
    }

    @Test
    public void testGetResolverType() {
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
    }

    @Test
    public void testGetAlwaysAsId() {
        boolean alwaysAsId = false;

        Assert.assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expectedString = "ObjectIdInfo: propName=testName, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";

        Assert.assertEquals(expectedString, objectIdInfo.toString());
    }
}