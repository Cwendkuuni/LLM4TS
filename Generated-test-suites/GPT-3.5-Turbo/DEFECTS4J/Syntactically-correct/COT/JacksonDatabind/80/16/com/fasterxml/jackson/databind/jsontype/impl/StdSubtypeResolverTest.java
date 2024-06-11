
import static org.junit.Assert.*;
import org.junit.Test;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType type = new NamedType(TestClass.class);
        resolver.registerSubtypes(type);
        assertNotNull(resolver._registeredSubtypes);
        assertTrue(resolver._registeredSubtypes.contains(type));

    }

    @Test
    public void testRegisterSubtypes_NullArray() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes((NamedType[]) null);
        assertNull(resolver._registeredSubtypes);
    }

    @Test
    public void testRegisterSubtypes_NullClassArray() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes((Class<?>[]) null);
        assertNull(resolver._registeredSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_WithConfig() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, type);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_WithConfig() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, baseType);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    private static class TestClass {}

}
