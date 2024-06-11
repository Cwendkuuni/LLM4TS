
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructorWithResolver() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = CustomObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = CustomObjectIdResolver.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);
        
        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(resolver, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithoutResolver() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = CustomObjectIdGenerator.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        
        assertEquals(name, info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithDeprecatedMethod() {
        String name = "test";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = CustomObjectIdGenerator.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen);
        
        assertEquals(new PropertyName(name), info.getPropertyName());
        assertEquals(scope, info.getScope());
        assertEquals(gen, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = CustomObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = CustomObjectIdResolver.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);
        
        ObjectIdInfo infoWithAlwaysAsId = info.withAlwaysAsId(true);
        
        assertTrue(infoWithAlwaysAsId.getAlwaysAsId());
    }
    
    // Additional test cases for potential edge cases and exceptions
    
    @Test
    public void testConstructorWithNullResolver() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = CustomObjectIdGenerator.class;
        
        try {
            ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    @Test
    public void testWithAlwaysAsIdNoStateChange() {
        PropertyName name = new PropertyName("test");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = CustomObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = CustomObjectIdResolver.class;
        
        ObjectIdInfo info = new ObjectIdInfo(name, scope, gen, resolver);
        
        ObjectIdInfo infoWithAlwaysAsId = info.withAlwaysAsId(false);
        
        assertSame(info, infoWithAlwaysAsId);
        assertEquals(info.getAlwaysAsId(), infoWithAlwaysAsId.getAlwaysAsId());
    }
    
    // Custom ObjectIdGenerator and ObjectIdResolver classes used for testing
    
    static class CustomObjectIdGenerator<T> extends ObjectIdGenerator<T> {
        // custom implementation
    }
    
    static class CustomObjectIdResolver extends ObjectIdResolver {
        // custom implementation
    }
}

