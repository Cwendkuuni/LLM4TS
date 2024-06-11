import static org.junit.Assert.*;
import java.lang.reflect.Modifier;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;
    private NamedType namedType;

    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
        namedType = new NamedType(Object.class);
    }

    @Test
    public void testRegisterSubtypes() {
        subtypeResolver.registerSubtypes(namedType);
        assertEquals(subtypeResolver._registeredSubtypes.size(), 1);
    }

    @Test
    public void testRegisterSubtypesArray() {
        NamedType[] namedTypes = { namedType };
        subtypeResolver.registerSubtypes(namedTypes);
        assertEquals(subtypeResolver._registeredSubtypes.size(), 1);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(result);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithType() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertNotNull(result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithType() {
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType);
        assertNotNull(result);
    }

    @Test
    public void test_collectAndResolve() {
        MapperConfig<?> config = null;
        AnnotationIntrospector ai = null;
        HashMap<NamedType, NamedType> collectedSubtypes = new HashMap<NamedType, NamedType>();
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        resolver._collectAndResolve(annotatedType, namedType, config, ai, collectedSubtypes);
        assertEquals(collectedSubtypes.size(), 1);
    }

    @Test
    public void test_collectAndResolveByTypeId() {
        MapperConfig<?> config = null;
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String,NamedType> byName = new LinkedHashMap<String,NamedType>();
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        resolver._collectAndResolveByTypeId(annotatedType, namedType, config, typesHandled, byName);
        assertEquals(byName.size(), 1);
    }

    @Test
    public void test_combineNamedAndUnnamed() {
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String,NamedType> byName = new LinkedHashMap<String,NamedType>();
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        ArrayList<NamedType> result = (ArrayList<NamedType>) resolver._combineNamedAndUnnamed(Object.class, typesHandled, byName);
        assertNotNull(result);
    }

}