import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, resolverType);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertEquals(resolverType, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithoutResolver() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType);
        
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithStringPropertyName() {
        String propertyName = "test";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType);
        
        assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generatorType, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generatorType = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolverType = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generatorType, resolverType);
        
        boolean alwaysAsId = !objectIdInfo.getAlwaysAsId();
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(alwaysAsId);
        
        assertEquals(propertyName, updatedObjectIdInfo.getPropertyName());
        assertEquals(scope, updatedObjectIdInfo.getScope());
        assertEquals(generatorType, updatedObjectIdInfo.getGeneratorType());
        assertEquals(resolverType, updatedObjectIdInfo.getResolverType());
        assertEquals(alwaysAsId, updatedObjectIdInfo.getAlwaysAsId());
    }
}