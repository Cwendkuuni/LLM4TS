import com.fasterxml.jackson.databind.MapperFactoryicator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.impl.MapperConfig;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class)), subtypes);
    }

    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Child1.class, "child1");
        resolver.registerSubtypes(Child2.class, "child2");
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(
                new NamedType(Child1.class, "child1"),
                new NamedType(Child2.class, "child2")), subtypes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSubtypesNull() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSubtypesEmptyArray() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new Class[0]);
    }

    @Test
    public void testRegisterSubtypesByClassWithExistingSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class)), subtypes);
        resolver.registerSubtypes(Child3.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class), new NamedType(Child3.class)), subtypes);
    }

    @Test
    public void testRegisterSubtypesByNameWithExistingSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Child1.class, "child1");
        resolver.registerSubtypes(Child2.class, "child2");
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(
                new NamedType(Child1.class, "child1"),
                new NamedType(Child2.class, "child2")), subtypes);
        resolver.registerSubtypes(Child3.class, "child3");
        subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(
                new NamedType(Child1.class, "child1"),
                new NamedType(Child2.class, "child2"),
                new NamedType(Child3.class, "child3")), subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertTrue(subtypes.isEmpty());
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class)), subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithExistingSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Collections.emptyList(), subtypes);
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class)), subtypes);
        resolver.registerSubtypes(Child3.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class), new NamedType(Child3.class)), subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType);
        assertTrue(subtypes.isEmpty());
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class)), subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithExistingSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType);
        assertTrue(subtypes.isEmpty());
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class)), subtypes);
        resolver.registerSubtypes(Child3.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class), new NamedType(Child3.class)), subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithEmptyMap() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        Map<String, NamedType> map = new LinkedHashMap<>();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType, Collections.emptyMap());
        assertTrue(subtypes.isEmpty());
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType, Collections.emptyMap());
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class)), subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithExistingMap() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        Map<String, NamedType> map = new LinkedHashMap<>();
        map.put("child1", new NamedType(Child1.class));
        map.put("child2", new NamedType(Child2.class));
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType, map);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class)), subtypes);
        resolver.registerSubtypes(Child3.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType, map);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class), new NamedType(Child3.class)), subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithExistingMapAndClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = MapperFactoryicator.getDefaultInstance().constructType(Parent.class);
        Map<String, NamedType> map = new LinkedHashMap<>();
        map.put("child1", new NamedType(Child1.class));
        map.put("child2", new NamedType(Child2.class));
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType, map);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class)), subtypes);
        resolver.registerSubtypes(Child3.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType, map);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class), new NamedType(Child3.class)), subtypes);
        resolver.registerSubtypes(Child3.class, "child3");
        subtypes = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder(), null, baseType, map);
        assertEquals(Arrays.asList(new NamedType(Child1.class), new NamedType(Child2.class), new NamedType(Child3.class), new NamedType(Child3.class, "child3")), subtypes);
    }

    @Test
    public void testToString() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        assertEquals("StdSubtypeResolver@15db9742", resolver.toString());
    }

    static class Parent {
    }

    static class Child1 extends Parent {
    }

    static class Child2 extends Parent {
    }

    static class Child3 extends Parent {
    }
}