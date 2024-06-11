import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
        assertNull(resolver._mappings.get(new ClassKey(ArrayList.class)));
    }
    
    @Test
    public void testAddMapping_SuperTypeEqualToSubType() {
        try {
            SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
            resolver.addMapping(List.class, List.class);
            fail("IllegalArgumentException should be thrown when adding mapping from class to itself");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }
    
    @Test
    public void testAddMapping_SubTypeNotSubclassOfSuperType() {
        try {
            SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
            resolver.addMapping(Collection.class, ArrayList.class);
            fail("IllegalArgumentException should be thrown when adding mapping from class to unrelated class");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to class java.util.ArrayList, as latter is not a subtype of former", e.getMessage());
        }
    }
    
    @Test
    public void testAddMapping_SuperTypeNotAbstract() {
        try {
            SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("IllegalArgumentException should be thrown when adding mapping from concrete class");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
    }
    
    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType type1 = new JavaType(LinkedList.class);
        JavaType type2 = new JavaType(ArrayList.class);
        JavaType type3 = new JavaType(HashSet.class);
        assertEquals(LinkedList.class, resolver.findTypeMapping(null, type1).getRawClass());
        assertNull(resolver.findTypeMapping(null, type2));
        assertNull(resolver.findTypeMapping(null, type3));
    }
    
    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType type1 = new JavaType(Collection.class);
        JavaType type2 = new JavaType(List.class);
        assertNull(resolver.resolveAbstractType(null, type1));
        assertNull(resolver.resolveAbstractType(null, type2));
    }

}