
package com.fasterxml.jackson.databind.introspect;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructor() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        ObjectIdInfo objectIdInfoWithId = objectIdInfo.withAlwaysAsId(true);
        
        Assert.assertEquals(propertyName, objectIdInfoWithId.getPropertyName());
        Assert.assertEquals(scope, objectIdInfoWithId.getScope());
        Assert.assertEquals(generator, objectIdInfoWithId.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfoWithId.getResolverType());
        Assert.assertTrue(objectIdInfoWithId.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        String expectedString = "ObjectIdInfo: propName=" + propertyName + ", scope=" + scope.getName()
                + ", generatorType=" + generator.getName() + ", alwaysAsId=false";
        
        Assert.assertEquals(expectedString, objectIdInfo.toString());
    }
    
}
