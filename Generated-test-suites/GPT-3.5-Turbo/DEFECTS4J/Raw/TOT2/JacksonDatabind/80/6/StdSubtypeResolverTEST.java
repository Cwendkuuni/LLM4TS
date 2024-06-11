import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        resolver.registerSubtypes(new NamedType(Integer.class, "Integer"));
        assertEquals(1, resolver.collectAndResolveSubtypesByClass(null, null, null).size());
    }

    @Test
    public void testResolutionByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(Integer.class, "Integer"));
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(null, null, null);
        for (NamedType type : result) {
            assertNotNull(type);
        }
    }

    @Test
    public void testResolutionByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(Integer.class, "Integer"));
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        for (NamedType type : result) {
            assertNotNull(type);
        }
    }

    @Test
    public void testResolutionByTypeIdWithNullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(Integer.class, "Integer"));
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        for (NamedType type : result) {
            assertNotNull(type);
        }
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String,NamedType> byName = new LinkedHashMap<String,NamedType>();
        
        ArrayList<NamedType> result = resolver._combineNamedAndUnnamed(Integer.class, typesHandled, byName);
        assertNotNull(result);
    }
}