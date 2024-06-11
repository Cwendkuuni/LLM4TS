import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        resolver.registerSubtypes(subtype1, subtype2);
        JavaType javaType = MapperFactory.defaultInstance().constructType(baseType);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, javaType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    @Test
    public void testRegisterSubtypesByClass_NullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        resolver.registerSubtypes(subtype1, subtype2);
        JavaType javaType = null;
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, javaType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSubtypesByClass_NullClasses() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?>[] classes = null;

        resolver.registerSubtypes(classes);
    }

    @Test
    public void testRegisterSubtypesByAnnotation() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;

        resolver.registerSubtypes(subtype1);
        JavaType javaType = MapperFactory.defaultInstance().constructType(baseType);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, javaType);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSubtypesByAnnotation_NullClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> clazz = null;

        resolver.registerSubtypes(clazz);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_Empty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType javaType = MapperFactory.defaultInstance().constructType(String.class);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, javaType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_RegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        resolver.registerSubtypes(subtype1, subtype2);
        JavaType javaType = MapperFactory.defaultInstance().constructType(baseType);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, javaType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_AnnotationSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;

        resolver.registerSubtypes(subtype1);
        JavaType javaType = MapperFactory.defaultInstance().constructType(baseType);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, javaType);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_BaseTypeIsSubtype() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype = String.class;

        JavaType javaType = MapperFactory.defaultInstance().constructType(baseType);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, javaType);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_Empty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType javaType = MapperFactory.defaultInstance().constructType(String.class);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, javaType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_RegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        resolver.registerSubtypes(subtype1, subtype2);
        JavaType javaType = MapperFactory.defaultInstance().constructType(baseType);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, javaType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_AnnotationSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;

        resolver.registerSubtypes(subtype1);
        JavaType javaType = MapperFactory.defaultInstance().constructType(baseType);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, javaType);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_BaseTypeIsSubtype() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype = String.class;

        JavaType javaType = MapperFactory.defaultInstance().constructType(baseType);
        SubtypeResolver subtypeResolver = resolver.construct(javaType);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, javaType);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype)));
    }
}