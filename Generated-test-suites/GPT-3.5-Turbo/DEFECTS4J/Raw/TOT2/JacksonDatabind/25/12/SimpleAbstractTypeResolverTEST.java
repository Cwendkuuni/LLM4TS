import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(List.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test
    public void testAddMappingIllegalArgumentException() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(Collection.class, Collection.class);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }

    @Test
    public void testAddMappingSubtypeException() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(Collection.class, HashMap.class);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.Collection to class java.util.HashMap, as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testAddMappingNotAbstractException() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        assertEquals(LinkedList.class, resolver.findTypeMapping(null, new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return List.class;
            }
        }).getRawClass());
    }

    @Test
    public void testFindTypeMappingNull() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNull(resolver.findTypeMapping(null, new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return HashMap.class;
            }
        }));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNull(resolver.resolveAbstractType(null, new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return List.class;
            }
        }));
    }
}