import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdSubtypeResolverTest {
    
    @Test
    public void testRegisterSubtypesNamedType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType namedType = new NamedType(Subtype.class);
        resolver.registerSubtypes(namedType);
        assertTrue(resolver.collectAndResolveSubtypesByClass(null, null, null).contains(namedType));
    }
    
    @Test
    public void testRegisterSubtypesClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Subtype.class);
        assertEquals(1, resolver.collectAndResolveSubtypesByClass(null, null, null).size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        assertEquals(0, resolver.collectAndResolveSubtypesByClass(null, null, null).size());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        assertEquals(0, resolver.collectAndResolveSubtypesByTypeId(null, null, null).size());
    }
}