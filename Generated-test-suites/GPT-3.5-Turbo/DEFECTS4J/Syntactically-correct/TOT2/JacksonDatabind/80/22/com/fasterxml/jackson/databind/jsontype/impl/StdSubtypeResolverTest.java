import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType namedType = new NamedType(String.class, "String");
        resolver.registerSubtypes(namedType);
        Set<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        assertTrue(registeredSubtypes.contains(namedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType namedType = new NamedType(Integer.class, "Integer");
        resolver.registerSubtypes(namedType);
        List<NamedType> resolvedSubtypes = new ArrayList<>(resolver.collectAndResolveSubtypesByClass(null, new AnnotatedClass(Integer.class)));
        assertEquals(resolvedSubtypes.size(), 1);
        assertEquals(resolvedSubtypes.get(0), namedType);
    }

    // Add more test cases for other methods as needed

}