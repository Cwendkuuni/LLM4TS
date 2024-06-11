import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        NamedType namedType1 = new NamedType(String.class, "String");
        NamedType namedType2 = new NamedType(Integer.class, "Integer");
        
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(namedType1, namedType2);
        
        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
        assertTrue(registeredSubtypes.contains(namedType1));
        assertTrue(registeredSubtypes.contains(namedType2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = new MapperConfig<>();
        AnnotatedMember property = new AnnotatedMember();
        JavaType baseType = new JavaType();
        
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        
        assertNotNull(subtypes);
        assertFalse(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = new MapperConfig<>();
        AnnotatedMember property = new AnnotatedMember();
        JavaType baseType = new JavaType();
        
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        
        assertNotNull(subtypes);
        assertFalse(subtypes.isEmpty());
    }
    
    @Test
    public void testCombineNamedAndUnnamed() {
        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();
        Class<?> rawBase = String.class;
        
        NamedType namedType1 = new NamedType(Integer.class, "Integer");
        NamedType namedType2 = new NamedType(Long.class, "Long");
        
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> result = resolver.combineNamedAndUnnamed(rawBase, typesHandled, byName);
        
        assertNotNull(result);
        assertTrue(result.size() == 1);
        assertTrue(result.contains(namedType1) || result.contains(namedType2));
    }

}