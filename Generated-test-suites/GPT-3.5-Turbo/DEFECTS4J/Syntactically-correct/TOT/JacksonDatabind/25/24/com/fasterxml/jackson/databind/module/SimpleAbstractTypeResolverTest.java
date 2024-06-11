import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    
    private SimpleAbstractTypeResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }
    
    @Test
    public void testAddMapping_WithValidArguments_ReturnsResolver() {
        Class<Collection<Object>> superType = Collection.class;
        Class<LinkedList<Object>> subType = LinkedList.class;
        
        SimpleAbstractTypeResolver returnedResolver = resolver.addMapping(superType, subType);
        
        assertEquals(resolver, returnedResolver);
        assertTrue(resolver._mappings.containsKey(new ClassKey(superType)));
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_WithSameClassAsArguments_ThrowsIllegalArgumentException() {
        Class<Collection<Object>> superType = Collection.class;
        
        resolver.addMapping(superType, superType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_WithNonSubTypeAsArguments_ThrowsIllegalArgumentException() {
        Class<Collection<Object>> superType = Collection.class;
        Class<LinkedList<String>> subType = LinkedList.class;
        
        resolver.addMapping(superType, subType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_WithNonAbstractSuperType_ThrowsIllegalArgumentException() {
        Class<LinkedList<Object>> superType = LinkedList.class;
        Class<LinkedList<Object>> subType = LinkedList.class;
        
        resolver.addMapping(superType, subType);
    }
    
    @Test
    public void testFindTypeMapping_WithMappingExists_ReturnsMappedType() {
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType type = new JavaType(Collection.class);
        
        JavaType resolvedType = resolver.findTypeMapping(null, type);
        
        assertEquals(new JavaType(LinkedList.class), resolvedType);
    }
    
    @Test
    public void testFindTypeMapping_WithMappingNotExists_ReturnsNull() {
        JavaType type = new JavaType(Collection.class);
        
        JavaType resolvedType = resolver.findTypeMapping(null, type);
        
        assertNull(resolvedType);
    }
    
    @Test
    public void testResolveAbstractType_ReturnsNull() {
        JavaType type = new JavaType(Collection.class);
        
        JavaType resolvedType = resolver.resolveAbstractType(null, type);
        
        assertNull(resolvedType);
    }

}