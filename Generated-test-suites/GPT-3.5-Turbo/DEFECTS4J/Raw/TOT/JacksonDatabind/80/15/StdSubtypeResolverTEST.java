import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Mock
    private MapperConfig<?> mockConfig;

    @Mock
    private AnnotationIntrospector mockAnnotationIntrospector;

    @Mock
    private AnnotatedMember mockProperty;

    @Mock
    private AnnotatedClass mockAnnotatedClass;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes_withSingleType_shouldAddTypeToRegisteredSubtypes() {
        // Arrange
        NamedType namedType = new NamedType(Object.class);

        // Act
        subtypeResolver.registerSubtypes(namedType);

        // Assert
        Collection<NamedType> registeredSubtypes = getRegisteredSubtypes();
        assertNotNull(registeredSubtypes);
        assertEquals(1, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(namedType));
    }

    @Test
    public void testRegisterSubtypes_withMultipleTypes_shouldAddTypesToRegisteredSubtypes() {
        // Arrange
        NamedType namedType1 = new NamedType(Object.class);
        NamedType namedType2 = new NamedType(String.class);

        // Act
        subtypeResolver.registerSubtypes(namedType1, namedType2);

        // Assert
        Collection<NamedType> registeredSubtypes = getRegisteredSubtypes();
        assertNotNull(registeredSubtypes);
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(namedType1));
        assertTrue(registeredSubtypes.contains(namedType2));
    }

    @Test
    public void testRegisterSubtypes_withNullTypes_shouldNotAddTypesToRegisteredSubtypes() {
        // Arrange

        // Act
        subtypeResolver.registerSubtypes((NamedType[]) null);

        // Assert
        Collection<NamedType> registeredSubtypes = getRegisteredSubtypes();
        assertNull(registeredSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_withRegisteredSubtypes_shouldReturnMatchingSubtypes() {
        // Arrange
        NamedType namedType1 = new NamedType(A.class);
        NamedType namedType2 = new NamedType(B.class);
        subtypeResolver.registerSubtypes(namedType1, namedType2);
        when(mockConfig.getAnnotationIntrospector()).thenReturn(mockAnnotationIntrospector);
        when(mockProperty.getRawType()).thenReturn(A.class);
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(mockConfig, A.class);

        // Act
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mockConfig, mockProperty, null);

        // Assert
        assertNotNull(subtypes);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(namedType1));
        assertTrue(subtypes.contains(namedType2));
    }

    private Collection<NamedType> getRegisteredSubtypes() {
        return subtypeResolver._registeredSubtypes;
    }

    private static class A {}
    private static class B {}

}