import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping_typicalUseCase() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        
        assertNotNull(resolver._mappings.get(new ClassKey(Collection.class)));
    }

    @Test
    public void testAddMapping_sameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_notSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to java.util.ArrayList, as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testAddMapping_notAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping_mappingExists() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        DeserializationConfig config = new DeserializationConfig(null);
        JavaType type = new JavaType(Collection.class);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getRawClass());
    }

    @Test
    public void testFindTypeMapping_mappingDoesNotExist() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig(null);
        JavaType type = new JavaType(List.class);
        
        JavaType result = resolver.findTypeMapping(config, type);
        
        assertNull(result);
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig(null);
        JavaType type = new JavaType(Collection.class);
        
        JavaType result = resolver.resolveAbstractType(config, type);
        
        assertNull(result);
    }
}