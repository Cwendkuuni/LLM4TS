import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {
    
    @Test
    public void testConstructor1() {
        PropertyName name = new PropertyName("id");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);
        
        assertNotNull(objectIdInfo);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructor2() {
        PropertyName name = new PropertyName("id");
        Class<?> scope = Integer.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);
        
        assertNotNull(objectIdInfo);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testConstructor3() {
        String name = "id";
        Class<?> scope = Double.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);
        
        assertNotNull(objectIdInfo);
        assertNotNull(objectIdInfo.getPropertyName());
        assertEquals(name, objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("id");
        Class<?> scope = Long.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);
        
        boolean originalAlwaysAsId = objectIdInfo.getAlwaysAsId();
        ObjectIdInfo objectIdInfoCopy = objectIdInfo.withAlwaysAsId(!originalAlwaysAsId);
        
        assertNotNull(objectIdInfoCopy);
        assertEquals(name, objectIdInfoCopy.getPropertyName());
        assertEquals(scope, objectIdInfoCopy.getScope());
        assertEquals(generator, objectIdInfoCopy.getGeneratorType());
        assertEquals(resolver, objectIdInfoCopy.getResolverType());
        
        if (originalAlwaysAsId) {
            assertFalse(objectIdInfo.getAlwaysAsId());
            assertTrue(objectIdInfoCopy.getAlwaysAsId());
        } else {
            assertTrue(objectIdInfo.getAlwaysAsId());
            assertFalse(objectIdInfoCopy.getAlwaysAsId());
        }
    }
}