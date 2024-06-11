import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType subtype1 = new NamedType(ExampleClass1.class, "example1");
        NamedType subtype2 = new NamedType(ExampleClass2.class, "example2");
        resolver.registerSubtypes(subtype1, subtype2);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        assertTrue(subtypes.contains(subtype1));
        assertTrue(subtypes.contains(subtype2));
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> subtype1Class = ExampleClass1.class;
        Class<?> subtype2Class = ExampleClass2.class;
        resolver.registerSubtypes(subtype1Class, subtype2Class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        assertTrue(subtypes.contains(new NamedType(subtype1Class)));
        assertTrue(subtypes.contains(new NamedType(subtype2Class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType subtype1 = new NamedType(ExampleClass1.class, "example1");
        NamedType subtype2 = new NamedType(ExampleClass2.class, "example2");
        resolver.registerSubtypes(subtype1, subtype2);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        assertTrue(subtypes.contains(subtype1));
        assertTrue(subtypes.contains(subtype2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType subtype1 = new NamedType(ExampleClass1.class, "example1");
        NamedType subtype2 = new NamedType(ExampleClass2.class, "example2");
        resolver.registerSubtypes(subtype1, subtype2);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertTrue(subtypes.contains(subtype1));
        assertTrue(subtypes.contains(subtype2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithClasses() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> subtype1Class = ExampleClass1.class;
        Class<?> subtype2Class = ExampleClass2.class;
        resolver.registerSubtypes(subtype1Class, subtype2Class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertTrue(subtypes.contains(new NamedType(subtype1Class)));
        assertTrue(subtypes.contains(new NamedType(subtype2Class)));
    }
}