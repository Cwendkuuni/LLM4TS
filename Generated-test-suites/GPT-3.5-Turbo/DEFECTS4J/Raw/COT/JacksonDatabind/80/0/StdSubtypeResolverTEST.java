import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class StdSubtypeResolverTest {
    private StdSubtypeResolver subtypeResolver;
    private MapperConfig<?> config;
    private AnnotationIntrospector annotationIntrospector;
    private AnnotatedClass annotatedClass;
    private AnnotatedMember annotatedMember;
    private JavaType baseType;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
        config = mock(MapperConfig.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
        annotatedClass = mock(AnnotatedClass.class);
        annotatedMember = mock(AnnotatedMember.class);
        baseType = mock(JavaType.class);
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = {new NamedType(String.class)};
        subtypeResolver.registerSubtypes(types);
        Collection<NamedType> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertNotNull(registeredSubtypes);
        assertEquals(1, registeredSubtypes.size());
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        subtypeResolver.registerSubtypes(String.class);
        Collection<NamedType> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertNotNull(registeredSubtypes);
        assertEquals(1, registeredSubtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithProperty() {
        Set<NamedType> expectedSubtypes = new HashSet<>();
        NamedType namedType = new NamedType(String.class);
        expectedSubtypes.add(namedType);
        NamedType[] namedTypes = {namedType};
        subtypeResolver.registerSubtypes(namedTypes);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, annotatedMember, baseType);
        assertNotNull(resolvedSubtypes);
        assertEquals(expectedSubtypes, new HashSet<>(resolvedSubtypes));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithType() {
        Set<NamedType> expectedSubtypes = new HashSet<>();
        NamedType namedType = new NamedType(String.class);
        expectedSubtypes.add(namedType);
        NamedType[] namedTypes = {namedType};
        subtypeResolver.registerSubtypes(namedTypes);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, annotatedClass);
        assertNotNull(resolvedSubtypes);
        assertEquals(expectedSubtypes, new HashSet<>(resolvedSubtypes));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithProperty() {
        Set<NamedType> expectedSubtypes = new HashSet<>();
        NamedType namedType = new NamedType(String.class);
        expectedSubtypes.add(namedType);
        NamedType[] namedTypes = {namedType};
        subtypeResolver.registerSubtypes(namedTypes);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, annotatedMember, baseType);
        assertNotNull(resolvedSubtypes);
        assertEquals(expectedSubtypes, new HashSet<>(resolvedSubtypes));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithType() {
        Set<NamedType> expectedSubtypes = new HashSet<>();
        NamedType namedType = new NamedType(String.class);
        expectedSubtypes.add(namedType);
        NamedType[] namedTypes = {namedType};
        subtypeResolver.registerSubtypes(namedTypes);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);
        assertNotNull(resolvedSubtypes);
        assertEquals(expectedSubtypes, new HashSet<>(resolvedSubtypes));
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        Set<Class<?>> typesHandled = new HashSet<>();
        typesHandled.add(String.class);
        Map<String, NamedType> byName = new LinkedHashMap<>();
        NamedType expectedNamedType = new NamedType(Integer.class);
        byName.put(expectedNamedType.getName(), expectedNamedType);

        Collection<NamedType> combined = subtypeResolver.combineNamedAndUnnamed(String.class, typesHandled, byName);
        assertNotNull(combined);
        assertEquals(2, combined.size());
        assertTrue(combined.contains(expectedNamedType));
        assertTrue(combined.contains(new NamedType(Object.class)));
    }
}