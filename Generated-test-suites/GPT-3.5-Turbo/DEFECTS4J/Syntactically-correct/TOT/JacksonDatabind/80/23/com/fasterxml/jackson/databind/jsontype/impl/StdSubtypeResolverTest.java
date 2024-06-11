import static org.junit.Assert.*;
import java.lang.reflect.Modifier;
import java.util.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes_NamedType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType namedType = new NamedType(TestClass.class, "test");
        
        resolver.registerSubtypes(namedType);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        
        assertEquals(1, subtypes.size());
        assertEquals(namedType, subtypes.iterator().next());
    }
    
    @Test
    public void testRegisterSubtypes_ClassArray() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?>[] classArray = {TestClass.class};
        
        resolver.registerSubtypes(classArray);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        
        assertEquals(1, subtypes.size());
        assertEquals(new NamedType(TestClass.class), subtypes.iterator().next());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass_NullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        
        assertNotNull(subtypes);
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass_RegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(TestClass.class);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, null);
        
        assertEquals(1, subtypes.size());
        assertEquals(new NamedType(TestClass.class), subtypes.iterator().next());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass_AnnotatedSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = new TestAnnotationIntrospector();
        AnnotatedMember property = null;
        JavaType baseType = null;
                
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, property, baseType);
        
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(TestClass.class, "annotated")));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByClass_BaseTypeSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = new TestAnnotationIntrospector();
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type);
        
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(TestClass.class, "base")));
        
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId_NullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        
        assertNotNull(subtypes);
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId_RegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(TestClass.class);
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        
        assertEquals(1, subtypes.size());
        assertEquals(new NamedType(TestClass.class), subtypes.iterator().next());
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId_AnnotatedSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = new TestAnnotationIntrospector();
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, property, baseType);
        
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(TestClass.class, "annotated")));
    }
    
    @Test
    public void testCollectAndResolveSubtypesByTypeId_BaseTypeSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector ai = new TestAnnotationIntrospector();
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, type);
        
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(TestClass.class, "base")));
    }
    
    private static class TestClass {
    
    }
    
    private static class TestAnnotationIntrospector extends AnnotationIntrospector {
    
    }
}