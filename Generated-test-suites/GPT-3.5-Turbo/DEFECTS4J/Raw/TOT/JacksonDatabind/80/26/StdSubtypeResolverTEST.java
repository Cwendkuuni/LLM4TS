import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;

import java.util.Collection;
import java.util.List;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver stdSubtypeResolver;
    private NamedType namedType;

    @Before
    public void setup() {
        stdSubtypeResolver = new StdSubtypeResolver();
        namedType = new NamedType(JavaType.class);
    }

    @Test
    public void testRegisterSubtypes() {
        stdSubtypeResolver.registerSubtypes(namedType);
        Collection<NamedType> namedTypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(null, null, null);
        assertTrue(namedTypes.contains(namedType));
    }

    @Test
    public void testRegisterSubtypesWithArray() {
        NamedType[] namedTypes = {namedType};
        stdSubtypeResolver.registerSubtypes(namedTypes);
        Collection<NamedType> resolvedNamedTypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(null, null, null);
        assertTrue(resolvedNamedTypes.contains(namedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithRegisteredSubtypes() {
        stdSubtypeResolver.registerSubtypes(namedType);
        Collection<NamedType> resolvedNamedTypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(null, null, JavaType.class);
        assertTrue(resolvedNamedTypes.contains(namedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithoutRegisteredSubtypes() {
        Collection<NamedType> resolvedNamedTypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(null, null, null);
        assertEquals(0, resolvedNamedTypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithAnnotatedTypes() {
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector() {
            public Collection<NamedType> findSubtypes(AnnotatedClass ac) {
                List<NamedType> namedTypes = new ArrayList<>();
                namedTypes.add(namedType);
                return namedTypes;
            }
        };
        Collection<NamedType> resolvedNamedTypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(annotationIntrospector, null, null);
        assertTrue(resolvedNamedTypes.contains(namedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithNullAnnotatedTypes() {
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector() {
            public Collection<NamedType> findSubtypes(AnnotatedClass ac) {
                return null;
            }
        };
        Collection<NamedType> resolvedNamedTypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(annotationIntrospector, null, null);
        assertEquals(0, resolvedNamedTypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithRegisteredSubtypes() {
        stdSubtypeResolver.registerSubtypes(namedType);
        Collection<NamedType> resolvedNamedTypes = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(null, null, JavaType.class);
        assertTrue(resolvedNamedTypes.contains(namedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithoutRegisteredSubtypes() {
        Collection<NamedType> resolvedNamedTypes = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertEquals(0, resolvedNamedTypes.size());
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        Set<Class<?>> typesHandled = new HashSet<>();
        typesHandled.add(JavaType.class);
        Map<String, NamedType> byName = new HashMap<>();
        NamedType namedType1 = new NamedType(JavaType.class, "NamedType1");
        NamedType namedType2 = new NamedType(JavaType.class, "NamedType2");
        byName.put("NamedType1", namedType1);
        byName.put("NamedType2", namedType2);

        Collection<NamedType> combinedNamedTypes = stdSubtypeResolver._combineNamedAndUnnamed(JavaType.class, typesHandled, byName);
        assertTrue(combinedNamedTypes.contains(namedType1));
        assertTrue(combinedNamedTypes.contains(namedType2));
    }

}