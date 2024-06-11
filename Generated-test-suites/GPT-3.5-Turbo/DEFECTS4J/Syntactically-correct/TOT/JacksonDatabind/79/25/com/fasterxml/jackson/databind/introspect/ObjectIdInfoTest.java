
import org.junit.Test;
import org.junit.Assert;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorAndResolver() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, resolver);
        
        Assert.assertEquals(propName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithPropertyNameAndScopeAndGenerator() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);
        
        Assert.assertEquals(propName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithPropertyNameAndScopeAndGenerator_Deprecated() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);
        
        Assert.assertEquals(propName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithStringNameAndScopeAndGenerator_Deprecated() {
        String propName = "id";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);
        
        Assert.assertEquals(new PropertyName(propName), objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithPropAndScopeAndGeneratorAndAlwaysAsId() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, alwaysAsId);
        
        Assert.assertEquals(propName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertTrue(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithPropAndScopeAndGeneratorAndAlwaysAsIdAndResolver() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator, alwaysAsId, resolver);
        
        Assert.assertEquals(propName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(resolver, objectIdInfo.getResolverType());
        Assert.assertTrue(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);
        
        Assert.assertEquals(propName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
        
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        Assert.assertEquals(propName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator, objectIdInfo.getGeneratorType());
        Assert.assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        Assert.assertTrue(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        PropertyName propName = new PropertyName("id");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propName, scope, generator);
        
        String expectedString = "ObjectIdInfo: propName=id, scope=java.lang.Object, generatorType=" +
                "com.fasterxml.jackson.databind.introspect.ObjectIdGenerator, alwaysAsId=false";
        Assert.assertEquals(expectedString, objectIdInfo.toString());
    }
}
