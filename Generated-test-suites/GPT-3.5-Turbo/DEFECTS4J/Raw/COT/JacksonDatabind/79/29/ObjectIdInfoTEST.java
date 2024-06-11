
package com.fasterxml.jackson.databind.introspect;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = ObjectIdInfoTest.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, generator, resolver);
        
        Assert.assertEquals(name, info.getPropertyName());
        Assert.assertEquals(scope, info.getScope());
        Assert.assertEquals(generator, info.getGeneratorType());
        Assert.assertEquals(resolver, info.getResolverType());
        Assert.assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = ObjectIdInfoTest.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, generator, resolver);
        
        ObjectIdInfo modifiedInfo = info.withAlwaysAsId(true);
        
        Assert.assertEquals(name, modifiedInfo.getPropertyName());
        Assert.assertEquals(scope, modifiedInfo.getScope());
        Assert.assertEquals(generator, modifiedInfo.getGeneratorType());
        Assert.assertEquals(resolver, modifiedInfo.getResolverType());
        Assert.assertTrue(modifiedInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = ObjectIdInfoTest.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, generator, resolver);
        
        String expected = "ObjectIdInfo: propName=test, scope=com.fasterxml.jackson.databind.introspect.ObjectIdInfoTest, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        
        Assert.assertEquals(expected, info.toString());
    }
}
