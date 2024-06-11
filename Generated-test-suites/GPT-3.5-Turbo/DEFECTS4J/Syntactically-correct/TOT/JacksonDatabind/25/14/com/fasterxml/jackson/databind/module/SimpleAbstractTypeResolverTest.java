import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;

public class SimpleAbstractTypeResolverTest {
    
    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping_typicalUseCase() {
        // Typical use case: Add a mapping from List to ArrayList
        resolver.addMapping(List.class, ArrayList.class);
        // Verify that the mapping is added correctly
        Class<?> subType = resolver._mappings.get(new ClassKey(List.class));
        assertEquals(ArrayList.class, subType);
    }

    @Test
    public void testAddMapping_errorCase_sameClass() {
        try {
            // Error case: Attempt to add a mapping from a class to itself
            resolver.addMapping(List.class, List.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
        // Verify that the mapping is not added
        assertNull(resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test
    public void testAddMapping_errorCase_subtype() {
        try {
            // Error case: Attempt to add a mapping from a non-subtype
            resolver.addMapping(ArrayList.class, List.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList to java.util.List, as latter is not a subtype of former", e.getMessage());
        }
        // Verify that the mapping is not added
        assertNull(resolver._mappings.get(new ClassKey(ArrayList.class)));
    }

    @Test
    public void testAddMapping_errorCase_notAbstract() {
        try {
            // Error case: Attempt to add a mapping from a non-abstract class
            resolver.addMapping(ArrayList.class, LinkedList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.util.ArrayList since it is not abstract", e.getMessage());
        }
        // Verify that the mapping is not added
        assertNull(resolver._mappings.get(new ClassKey(ArrayList.class)));
    }

    @Test
    public void testFindTypeMapping_typicalUseCase() {
        // Typical use case: Find type mapping for a mapped class
        resolver.addMapping(List.class, LinkedList.class);
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(List.class);
        JavaType mappedType = resolver.findTypeMapping(config, type);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_edgeCase_unmappedClass() {
        // Edge case: Find type mapping for an unmapped class
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(ArrayList.class);
        JavaType mappedType = resolver.findTypeMapping(config, type);
        assertNull(mappedType);
    }

    @Test
    public void testResolveAbstractType() {
        // Expected behavior of resolveAbstractType is to always return null
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(List.class);
        JavaType resolvedType = resolver.resolveAbstractType(config, type);
        assertNull(resolvedType);
    }
}