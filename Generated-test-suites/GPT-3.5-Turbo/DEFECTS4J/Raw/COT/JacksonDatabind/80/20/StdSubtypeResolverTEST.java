import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Modifier;
import java.util.*;
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
        NamedType namedType1 = new NamedType(Object.class);
        NamedType namedType2 = new NamedType(String.class);
        NamedType namedType3 = new NamedType(Integer.class);
        
        resolver.registerSubtypes(namedType1, namedType2, namedType3);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null);
        
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
        assertTrue(subtypes.contains(namedType3));
    }

    @Test
    public void testRegisterSubtypesClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        resolver.registerSubtypes(Object.class, String.class, Integer.class);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null);
        
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Object.class)));
        assertTrue(subtypes.contains(new NamedType(String.class)));
        assertTrue(subtypes.contains(new NamedType(Integer.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> annotatedClass = Object.class;
        MapperConfig<?> config = null;
        AnnotationIntrospector ai = null;
        HashMap<NamedType, NamedType> collected = new HashMap<NamedType, NamedType>();
        
        resolver._collectAndResolve(annotatedClass, new NamedType(Object.class), config, ai, collected);
        
        assertEquals(1, collected.size());
        assertTrue(collected.containsKey(new NamedType(Object.class)));
    }


    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> annotatedClass = Object.class;
        NamedType namedType = new NamedType(Object.class);
        MapperConfig<?> config = null;
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String,NamedType> byName = new LinkedHashMap<String,NamedType>();
        
        resolver._collectAndResolveByTypeId(annotatedClass, namedType, config, typesHandled, byName);
        
        assertEquals(1, typesHandled.size());
        assertTrue(typesHandled.contains(Object.class));
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String,NamedType> byName = new LinkedHashMap<String,NamedType>();
        typesHandled.add(String.class);
        byName.put("Type1", new NamedType(Long.class, "Type1"));
        byName.put("Type2", new NamedType(Float.class, "Type2"));
        byName.put("Type3", new NamedType(Double.class, "Type3"));
        
        Collection<NamedType> result = resolver._combineNamedAndUnnamed(Float.class, typesHandled, byName);
        
        assertEquals(4, result.size());
        assertTrue(result.contains(new NamedType(Long.class, "Type1")));
        assertTrue(result.contains(new NamedType(Float.class)));
        assertTrue(result.contains(new NamedType(Double.class, "Type3")));
        assertTrue(result.contains(new NamedType(String.class)));
    }

}