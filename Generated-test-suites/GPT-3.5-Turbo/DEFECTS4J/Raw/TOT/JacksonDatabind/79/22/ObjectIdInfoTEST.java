import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        PropertyName propertyName = PropertyName.construct("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertEquals("test", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructor2() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Integer.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertEquals("test", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(Integer.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Double.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertFalse(objectIdInfo.getAlwaysAsId());
        
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);
        
        assertTrue(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Float.class;
        Class<? extends ObjectIdGenerator<?>> generator = MyObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = MyObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
        
        assertEquals("ObjectIdInfo: propName=test, scope=java.lang.Float, generatorType=com.example.MyObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }
    
    private static class MyObjectIdGenerator implements ObjectIdGenerator<Object> {
        // Implementation not needed for these tests
    }
    
    private static class MyObjectIdResolver implements ObjectIdResolver {
        // Implementation not needed for these tests
    }

}