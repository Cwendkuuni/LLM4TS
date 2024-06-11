import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void registerSubtypes_WhenMultipleTypesAreRegistered_ShouldAddAllTypesToRegisteredSubtypes() {
        // Arrange
        NamedType[] types = new NamedType[3];
        types[0] = new NamedType(TestClass1.class);
        types[1] = new NamedType(TestClass2.class);
        types[2] = new NamedType(TestClass3.class);

        // Act
        subtypeResolver.registerSubtypes(types);

        // Assert
        assertNotNull(subtypeResolver._registeredSubtypes);
        assertEquals(3, subtypeResolver._registeredSubtypes.size());
        assertTrue(subtypeResolver._registeredSubtypes.contains(types[0]));
        assertTrue(subtypeResolver._registeredSubtypes.contains(types[1]));
        assertTrue(subtypeResolver._registeredSubtypes.contains(types[2]));
    }

    @Test
    public void registerSubtypes_WhenSingleTypeIsRegistered_ShouldAddTypeToRegisteredSubtypes() {
        // Arrange
        NamedType type = new NamedType(TestClass1.class);

        // Act
        subtypeResolver.registerSubtypes(type);

        // Assert
        assertNotNull(subtypeResolver._registeredSubtypes);
        assertTrue(subtypeResolver._registeredSubtypes.contains(type));
    }

    @Test
    public void collectAndResolveSubtypesByClass_WhenRegisteredSubtypesExist_ShouldReturnAllRegisteredSubtypes() {
        // Arrange
        NamedType[] registeredSubtypes = new NamedType[3];
        registeredSubtypes[0] = new NamedType(TestClass1.class);
        registeredSubtypes[1] = new NamedType(TestClass2.class);
        registeredSubtypes[2] = new NamedType(TestClass3.class);
        subtypeResolver._registeredSubtypes = new LinkedHashSet<>(Arrays.asList(registeredSubtypes));

        MapperConfig config = new MapperConfig() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedMember property = null;
        JavaType baseType = null;

        // Act
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(registeredSubtypes)));
    }

    @Test
    public void collectAndResolveSubtypesByClass_WhenRegisteredSubtypesDoNotExist_ShouldReturnEmptyCollection() {
        // Arrange
        subtypeResolver._registeredSubtypes = null;

        MapperConfig config = new MapperConfig() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedMember property = null;
        JavaType baseType = null;

        // Act
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void collectAndResolveSubtypesByTypeId_WhenRegisteredSubtypesExist_ShouldReturnAllRegisteredSubtypes() {
        // Arrange
        NamedType[] registeredSubtypes = new NamedType[3];
        registeredSubtypes[0] = new NamedType(TestClass1.class);
        registeredSubtypes[1] = new NamedType(TestClass2.class);
        registeredSubtypes[2] = new NamedType(TestClass3.class);
        subtypeResolver._registeredSubtypes = new LinkedHashSet<>(Arrays.asList(registeredSubtypes));

        MapperConfig config = new MapperConfig() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedMember property = null;
        JavaType baseType = null;

        // Act
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(registeredSubtypes)));
    }

    @Test
    public void collectAndResolveSubtypesByTypeId_WhenRegisteredSubtypesDoNotExist_ShouldReturnEmptyCollection() {
        // Arrange
        subtypeResolver._registeredSubtypes = null;

        MapperConfig config = new MapperConfig() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedMember property = null;
        JavaType baseType = null;

        // Act
        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    class TestClass1 {}
    class TestClass2 {}
    class TestClass3 {}
}