import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {
    
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;
        
        resolver.addMapping(superType, subType);
        
        assertNotNull(resolver._mappings.get(new ClassKey(superType)));
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }
    
    @Test
    public void testAddMappingWithSameClasses() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> type = Collection.class;
        
        try {
            resolver.addMapping(type, type);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testAddMappingWithNonSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        try {
            resolver.addMapping(List.class, LinkedList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        
        JavaType inputType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType outputType = resolver.findTypeMapping(null, inputType);
        
        assertNotNull(outputType);
        assertEquals(subType, outputType.getRawClass());
    }
    
    @Test
    public void testFindTypeMappingWithUnmappedType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        JavaType inputType = TypeFactory.defaultInstance().constructType(Set.class);
        JavaType outputType = resolver.findTypeMapping(null, inputType);
        
        assertNull(outputType);
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        JavaType inputType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType outputType = resolver.resolveAbstractType(null, inputType);
        
        assertNull(outputType);
    }
}