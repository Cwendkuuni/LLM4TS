import org.junit.Test;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import static org.junit.Assert.*;
import java.util.Collection;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(String.class));
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        assertEquals(1, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_AnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, AnnotatedClass.class);
        assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertNotNull(subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_AnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, AnnotatedClass.class);
        assertNotNull(subtypes);
    }

}