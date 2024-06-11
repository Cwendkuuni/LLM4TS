import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // Test registration of subtypes by class
        resolver.registerSubtypes(String.class, MyStringSubtype.class);
        // Test registration of subtypes by name
        resolver.registerSubtypes(MyBaseClass.class, "MySubtype");

        // Test that registered subtypes are present
        assertTrue(resolver.hasSubtype(String.class));
        assertTrue(resolver.hasSubtype(MyStringSubtype.class));
        assertTrue(resolver.hasSubtype(MyBaseClass.class));
        assertTrue(resolver.hasSubtype("MySubtype"));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // Test with empty list of registered subtypes
        MapperConfig config = new MapperFactoryMapper().getConfig();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        JavaType baseType = JavaType.construct(MyBaseClass.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertNull(subtypes);

        // Test with registered subtypes by class
        resolver.registerSubtypes(MyStringSubtype.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertFalse(subtypes.isEmpty());
        assertTrue(subtypes.contains(new NamedType(MyStringSubtype.class)));

        // Test with registered subtypes by name
        resolver.registerSubtypes(MyBaseClass.class, "MySubtype");
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertFalse(subtypes.isEmpty());
        assertTrue(subtypes.contains(new NamedType(MySubtype.class)));

        // Test with both registered subtypes by class and by name
        resolver.registerSubtypes(MyStringSubtype.class);
        resolver.registerSubtypes(MyBaseClass.class, "MySubtype");
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertFalse(subtypes.isEmpty());
        assertTrue(subtypes.contains(new NamedType(MyStringSubtype.class)));
        assertTrue(subtypes.contains(new NamedType(MySubtype.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // Test with empty list of registered subtypes
        MapperConfig config = new MapperFactoryMapper().getConfig();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, MyBaseClass.class);
        assertNull(subtypes);

        // Test with registered subtypes by class
        resolver.registerSubtypes(MyStringSubtype.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, MyBaseClass.class);
        assertFalse(subtypes.isEmpty());
        assertTrue(subtypes.contains(new NamedType(MyStringSubtype.class)));

        // Test with registered subtypes by name
        resolver.registerSubtypes(MyBaseClass.class, "MySubtype");
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, MyBaseClass.class);
        assertFalse(subtypes.isEmpty());
        assertTrue(subtypes.contains(new NamedType(MySubtype.class)));

        // Test with both registered subtypes by class and by name
        resolver.registerSubtypes(MyStringSubtype.class);
        resolver.registerSubtypes(MyBaseClass.class, "MySubtype");
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, MyBaseClass.class);
        assertFalse(subtypes.isEmpty());
        assertTrue(subtypes.contains(new NamedType(MyStringSubtype.class)));
        assertTrue(subtypes.contains(new NamedType(MySubtype.class)));
    }

    @Test
    public void testEmptyClasses() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // Test with empty classes
        MapperConfig config = new MapperFactoryMapper().getConfig();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, Void.class);
        assertNull(subtypes);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, Void.class);
        assertNull(subtypes);
    }

    @Test
    public void testNullArguments() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // Test with null arguments
        assertNull(resolver.collectAndResolveSubtypesByClass(null, null, null));
        assertNull(resolver.collectAndResolveSubtypesByTypeId(null, null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperFactoryMapper().getConfig();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, Object.class);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = NullPointerException.class)
    public void testNullSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperFactoryMapper().getConfig();
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, MyBaseClass.class);
        assertNull(subtypes);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, MyBaseClass.class);
        assertNull(subtypes);
        resolver.registerSubtypes(null);
        resolver.registerSubtypes(MyBaseClass.class, null);
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, MyBaseClass.class);
        assertNull(subtypes);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, MyBaseClass.class);
        assertNull(subtypes);
    }

    @Test
    public void testMultipleInheritance() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperFactoryMapper().getConfig();
        // Test with classes having multiple inheritance
        resolver.registerSubtypes(MyBaseClass1.class);
        resolver.registerSubtypes(MyBaseClass2.class);
        resolver.registerSubtypes(MySubclass1.class);
        resolver.registerSubtypes(MySubclass2.class);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, MySubclass1.class);
        assertTrue(subtypes.contains(new NamedType(MySubclass1.class)));
        assertTrue(subtypes.contains(new NamedType(MyBaseClass1.class)));
        assertTrue(subtypes.contains(new NamedType(MyBaseClass2.class)));

        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, MySubclass1.class);
        assertTrue(subtypes.contains(new NamedType(MySubclass1.class)));
        assertTrue(subtypes.contains(new NamedType(MyBaseClass1.class)));
        assertTrue(subtypes.contains(new NamedType(MyBaseClass2.class)));
    }

    static class MyBaseClass {
    }

    static class MySubtype extends MyBaseClass {
    }

    static class MyStringSubtype extends String {
    }

    static class MySubclass1 extends MyBaseClass1 implements MyBaseClass2 {
    }

    static class MyBaseClass1 {
    }

    static class MyBaseClass2 {
    }
}