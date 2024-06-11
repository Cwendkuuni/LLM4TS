import org.junit.Test;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType type = new NamedType(String.class, "String");
        resolver.registerSubtypes(type);
        assertNotNull(resolver._registeredSubtypes);
        assertEquals(1, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(type));
    }

    @Test
    public void testRegisterSubtypes_multipleTypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType type1 = new NamedType(String.class, "String");
        NamedType type2 = new NamedType(Integer.class, "Integer");
        resolver.registerSubtypes(type1, type2);
        assertNotNull(resolver._registeredSubtypes);
        assertEquals(2, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(type1));
        assertTrue(resolver._registeredSubtypes.contains(type2));
    }

    @Test
    public void testRegisterSubtypes_withClasses() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?>[] classes = {String.class, Integer.class};
        resolver.registerSubtypes(classes);
        assertNotNull(resolver._registeredSubtypes);
        assertEquals(2, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(new NamedType(String.class)));
        assertTrue(resolver._registeredSubtypes.contains(new NamedType(Integer.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_noRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(null, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_withRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?>[] classes = {String.class, Integer.class};
        resolver.registerSubtypes(classes);
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(null, null);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(String.class)));
        assertTrue(result.contains(new NamedType(Integer.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_noRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(null, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_withRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?>[] classes = {String.class, Integer.class};
        resolver.registerSubtypes(classes);
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(null, null);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(String.class)));
        assertTrue(result.contains(new NamedType(Integer.class)));
    }
}