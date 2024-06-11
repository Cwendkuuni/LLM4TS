
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType namedType = new NamedType(Object.class);
        subtypeResolver.registerSubtypes(namedType);

        assertNotNull(subtypeResolver._registeredSubtypes);
        assertTrue(subtypeResolver._registeredSubtypes.contains(namedType));
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        Class<?>[] classes = {Integer.class, String.class};
        subtypeResolver.registerSubtypes(classes);

        assertNotNull(subtypeResolver._registeredSubtypes);
        assertEquals(subtypeResolver._registeredSubtypes.size(), 2);
    }

    @Mock
    private MapperConfig<JavaType> config;

    @Mock
    private AnnotationIntrospector ai;

    @Mock
    private AnnotatedMember property;

    @Mock
    private JavaType baseType;

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, annotatedClass, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithRegisteredSubtypes() {
        NamedType namedType = new NamedType(Object.class);
        subtypeResolver.registerSubtypes(namedType);

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, annotatedClass, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.contains(namedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithAnnotatedSubtypes() {
        NamedType namedType = new NamedType(Object.class);
        when(ai.findSubtypes(property)).thenReturn(List.of(namedType));

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.contains(namedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithAnnotatedSubtypesAndRegisteredSubtypes() {
        NamedType registeredType = new NamedType(Object.class);
        NamedType annotatedType = new NamedType(String.class);

        subtypeResolver.registerSubtypes(registeredType);
        when(ai.findSubtypes(property)).thenReturn(List.of(annotatedType));

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.contains(registeredType));
        assertTrue(subtypes.contains(annotatedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, annotatedClass, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithRegisteredSubtypes() {
        NamedType namedType = new NamedType(Object.class);
        subtypeResolver.registerSubtypes(namedType);

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, annotatedClass, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.contains(namedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedSubtypes() {
        NamedType namedType = new NamedType(Object.class);
        when(ai.findSubtypes(property)).thenReturn(List.of(namedType));

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.contains(namedType));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedSubtypesAndRegisteredSubtypes() {
        NamedType registeredType = new NamedType(Object.class);
        NamedType annotatedType = new NamedType(String.class);

        subtypeResolver.registerSubtypes(registeredType);
        when(ai.findSubtypes(property)).thenReturn(List.of(annotatedType));

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.contains(registeredType));
        assertTrue(subtypes.contains(annotatedType));
    }
}
