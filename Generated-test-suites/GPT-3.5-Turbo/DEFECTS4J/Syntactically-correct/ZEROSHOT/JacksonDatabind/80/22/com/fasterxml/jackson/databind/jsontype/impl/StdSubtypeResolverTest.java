import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType[] types = new NamedType[2];
        types[0] = new NamedType(SubType1.class);
        types[1] = new NamedType(SubType2.class);
        resolver.registerSubtypes(types);
        Collection<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(new NamedType(SubType1.class)));
        assertTrue(registeredSubtypes.contains(new NamedType(SubType2.class)));
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(SubType1.class, SubType2.class);
        Collection<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(new NamedType(SubType1.class)));
        assertTrue(registeredSubtypes.contains(new NamedType(SubType2.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithConfigAndType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, type);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithConfigAndType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, baseType);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> rawBase = SubType1.class;
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String,NamedType> byName = new LinkedHashMap<String,NamedType>();
        Collection<NamedType> result = resolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

class SubType1 {}
class SubType2 {}