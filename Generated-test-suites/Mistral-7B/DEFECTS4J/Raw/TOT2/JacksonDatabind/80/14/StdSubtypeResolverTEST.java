import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registering subtypes using classes
        resolver.registerSubtypes(ChildType1.class, ChildType2.class);
        List<NamedType> registeredTypes = resolver.getRegisteredSubtypes();
        assertEquals(2, registeredTypes.size());
        assertTrue(registeredTypes.get(0) instanceof NamedType);
        assertTrue(registeredTypes.get(1) instanceof NamedType);
        assertEquals(ChildType1.class, registeredTypes.get(0).getType());
        assertEquals(ChildType2.class, registeredTypes.get(1).getType());

        // Test registering subtypes using named types
        NamedType namedType1 = new NamedType(ChildType1.class, "ChildType1");
        NamedType namedType2 = new NamedType(ChildType2.class, "ChildType2");
        resolver.registerSubtypes(namedType1, namedType2);
        registeredTypes = resolver.getRegisteredSubtypes();
        assertEquals(2, registeredTypes.size());
        assertTrue(registeredTypes.get(0) instanceof NamedType);
        assertTrue(registeredTypes.get(1) instanceof NamedType);
        assertEquals(ChildType1.class, registeredTypes.get(0).getType());
        assertEquals(ChildType2.class, registeredTypes.get(1).getType());

        // Test registering subtypes with the same name
        resolver.registerSubtypes(new NamedType(ChildType3.class, "ChildType1"));
        registeredTypes = resolver.getRegisteredSubtypes();
        assertEquals(3, registeredTypes.size());
        assertTrue(registeredTypes.get(0) instanceof NamedType);
        assertTrue(registeredTypes.get(1) instanceof NamedType);
        assertTrue(registeredTypes.get(2) instanceof NamedType);
        assertEquals(ChildType1.class, registeredTypes.get(0).getType());
        assertEquals(ChildType2.class, registeredTypes.get(1).getType());
        assertEquals(ChildType3.class, registeredTypes.get(2).getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(ChildType1.class, ChildType2.class, ChildType3.class);

        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        ObjectMapper mapper = builder.build();
        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();

        JavaType baseType = mapper.getTypeFactory().constructType(BaseType.class);

        // Check subtypes collected by class
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);
        assertEquals(3, subtypes.size());
        for (NamedType namedType : subtypes) {
            Class<?> type = namedType.getType();
            assertTrue(type.isAssignableFrom(BaseType.class));
            if (type.equals(ChildType1.class)) {
                assertEquals("ChildType1", namedType.getName());
            } else if (type.equals(ChildType2.class)) {
                assertEquals("ChildType2", namedType.getName());
            } else {
                assertEquals("ChildType3", namedType.getName());
            }
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(ChildType1.class, ChildType2.class, ChildType3.class);

        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        ObjectMapper mapper = builder.build();
        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();

        JavaType baseType = mapper.getTypeFactory().constructType(BaseType.class);

        // Check subtypes collected by type id
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);
        assertEquals(3, subtypes.size());
        for (NamedType namedType : subtypes) {
            Class<?> type = namedType.getType();
            assertTrue(type.isAssignableFrom(BaseType.class));
            if (type.equals(ChildType1.class)) {
                assertEquals("ChildType1", namedType.getName());
            } else if (type.equals(ChildType2.class)) {
                assertEquals("ChildType2", namedType.getName());
            } else {
                assertEquals("ChildType3", namedType.getName());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithNullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(ChildType1.class, ChildType2.class, ChildType3.class);

        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        ObjectMapper mapper = builder.build();
        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();

        // Check error when baseType is null
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, null);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithNoSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        ObjectMapper mapper = builder.build();
        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();

        JavaType baseType = mapper.getTypeFactory().constructType(BaseType.class);

        // Check empty list when there are no subtypes
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithMultipleSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(ChildType1.class, ChildType2.class, ChildType3.class);

        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        ObjectMapper mapper = builder.build();
        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();

        JavaType baseType = mapper.getTypeFactory().constructType(BaseType.class);

        // Check multiple subtypes with the same name
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);
        assertEquals(3, subtypes.size());
        for (NamedType namedType : subtypes) {
            Class<?> type = namedType.getType();
            assertTrue(type.isAssignableFrom(BaseType.class));
            if (type.equals(ChildType1.class)) {
                assertEquals("ChildType1", namedType.getName());
            } else if (type.equals(ChildType2.class)) {
                assertEquals("ChildType2", namedType.getName());
            } else {
                assertEquals("ChildType3", namedType.getName());
            }
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithDuplicateClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(ChildType1.class, ChildType1.class, ChildType3.class);

        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        ObjectMapper mapper = builder.build();
        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();

        JavaType baseType = mapper.getTypeFactory().constructType(BaseType.class);

        // Check that only unique subtypes are returned
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);
        assertEquals(2, subtypes.size());
        for (NamedType namedType : subtypes) {
            Class<?> type = namedType.getType();
            assertTrue(type.isAssignableFrom(BaseType.class));
            if (type.equals(ChildType1.class)) {
                assertEquals("ChildType1", namedType.getName());
            } else {
                assertEquals("ChildType3", namedType.getName());
            }
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAbstractBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(ChildType1.class, ChildType2.class, ChildType3.class);

        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        ObjectMapper mapper = builder.build();
        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();

        JavaType baseType = mapper.getTypeFactory().constructType(AbstractBaseType.class);

        // Check subtypes collected by type id when baseType is abstract
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);
        assertEquals(3, subtypes.size());
        for (NamedType namedType : subtypes) {
            Class<?> type = namedType.getType();
            assertTrue(type.isAssignableFrom(AbstractBaseType.class));
            if (type.equals(ChildType1.class)) {
                assertEquals("ChildType1", namedType.getName());
            } else if (type.equals(ChildType2.class)) {
                assertEquals("ChildType2", namedType.getName());
            } else {
                assertEquals("ChildType3", namedType.getName());
            }
        }
    }

    @Test
    public void testGetRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(ChildType1.class, ChildType2.class, ChildType3.class);

        List<NamedType> registeredTypes = resolver.getRegisteredSubtypes();
        assertEquals(3, registeredTypes.size());
        for (NamedType namedType : registeredTypes) {
            Class<?> type = namedType.getType();
            assertTrue(type.isAssignableFrom(BaseType.class));
            if (type.equals(ChildType1.class)) {
                assertEquals("ChildType1", namedType.getName());
            } else if (type.equals(ChildType2.class)) {
                assertEquals("ChildType2", namedType.getName());
            } else {
                assertEquals("ChildType3", namedType.getName());
            }
        }
    }

    @Test
    public void testGetRegisteredSubtypesEmpty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        List<NamedType> registeredTypes = resolver.getRegisteredSubtypes();
        assertEquals(0, registeredTypes.size());
    }

    private static class BaseType {
    }

    private static class AbstractBaseType {
    }

    private static class ChildType1 extends BaseType {
    }

    private static class ChildType2 extends BaseType {
    }

    private static class ChildType3 extends BaseType {
    }
}