import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping_Success() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_NotSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_NonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(LinkedList.class, ArrayList.class);
    }

    @Test
    public void testFindTypeMapping_Success() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        JavaType actualType = resolver.findTypeMapping(null, JavaType.construct(List.class));
        JavaType expectedType = JavaType.construct(LinkedList.class);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMapping_NullMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType actualType = resolver.findTypeMapping(null, JavaType.construct(List.class));
        assertNull(actualType);
    }

    @Test
    public void testResolveAbstractType_Null() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType actualType = resolver.resolveAbstractType(null, JavaType.construct(List.class));
        assertNull(actualType);
    }
}