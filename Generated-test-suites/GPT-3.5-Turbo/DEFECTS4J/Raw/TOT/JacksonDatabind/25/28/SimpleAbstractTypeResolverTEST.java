import org.junit.Assert;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Typical use case, adding valid mapping
        resolver.addMapping(Collection.class, ArrayList.class);
        
        // Edge case, adding mapping with a sub-class that extends the super-type
        resolver.addMapping(List.class, LinkedList.class);
        
        // Error scenario, adding mapping with the same class as super-type and sub-type
        try {
            resolver.addMapping(ArrayList.class, ArrayList.class);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
        
        // Error scenario, adding mapping with sub-type that is not a subtype of super-type
        try {
            resolver.addMapping(List.class, HashMap.class);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Can not add mapping from class java.util.List to java.util.HashMap, as latter is not a subtype of former", e.getMessage());
        }
        
        // Error scenario, adding mapping with super-type that is not abstract
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Typical use case, mapping exists
        resolver.addMapping(Collection.class, ArrayList.class);
        Class<?> dst = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(Collection.class)).getRawClass();
        Assert.assertEquals(ArrayList.class, dst);
        
        // Error scenario, mapping does not exist
        dst = resolver.findTypeMapping(null, TypeFactory.defaultInstance().constructType(List.class)).getRawClass();
        Assert.assertNull(dst);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Always returns null, so just a simple test to validate the behavior
        JavaType type = resolver.resolveAbstractType(null, TypeFactory.defaultInstance().constructType(Collection.class));
        Assert.assertNull(type);
    }
}