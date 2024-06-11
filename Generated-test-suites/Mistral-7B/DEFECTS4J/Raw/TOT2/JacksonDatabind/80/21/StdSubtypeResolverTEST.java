import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

import org.junit.Test;
import org.junit.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registering subtypes by class
        resolver.registerSubtypes(Child1.class);
        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(1, types.size());
        Assert.assertEquals(Child1.class, types.get(0).getType().asSubTypeOf(String.class));

        // Test registering subtypes by name
        resolver.registerSubtypes(Child2.class);
        types = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(2, types.size());
        Assert.assertEquals(Child1.class, types.get(0).getType().asSubTypeOf(String.class));
        Assert.assertEquals(Child2.class, types.get(1).getType().asSubTypeOf(String.class));

        // Test registering multiple subtypes with same base type (should not cause duplicate)
        resolver.registerSubtypes(Child3.class);
        types = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(3, types.size());
        Assert.assertEquals(Child1.class, types.get(0).getType().asSubTypeOf(String.class));
        Assert.assertEquals(Child2.class, types.get(1).getType().asSubTypeOf(String.class));
        Assert.assertEquals(Child3.class, types.get(2).getType().asSubTypeOf(String.class));

        // Test registering subtypes for non-String base type
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Animal.class);
        types = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(1, types.size());
        Assert.assertNotNull(types.get(0).getName());
        Assert.assertEquals(Animal.class, types.get(0).getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        resolver.registerSubtypes(Child3.class);

        // Test base type without subtypes
        List<NamedType> types1 = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(0, types1.size());

        // Test class with subtypes
        List<NamedType> types2 = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), Parent.class, null);
        Assert.assertEquals(3, types2.size());
        Assert.assertEquals(Child1.class, types2.get(0).getType().asSubTypeOf(String.class));
        Assert.assertEquals(Child2.class, types2.get(1).getType().asSubTypeOf(String.class));
        Assert.assertEquals(Child3.class, types2.get(2).getType().asSubTypeOf(String.class));

        // Test class with no subtypes but having superclass with subtypes
        List<NamedType> types3 = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), ParentWithNoSubtypes.class, null);
        Assert.assertEquals(0, types3.size());

        // Test null base type
        List<NamedType> types4 = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(0, types4.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        resolver.registerSubtypes(Child3.class);

        // Test empty collection
        List<NamedType> types1 = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(0, types1.size());

        // Test collectAndResolveSubtypesByTypeId with base type that has no subtypes
        MapperConfig config = new MapperConfig.StandardMapperConfig();
        ObjectMapper mapper = new MapperFactoryBuilder(config).build();
        AnnotatedMember property = mapper.getFactory().introspect(String.class);
        List<NamedType> types2 = resolver.collectAndResolveSubtypesByTypeId(config, property, null);
        Assert.assertEquals(0, types2.size());

        // Test collectAndResolveSubtypesByTypeId with base type that has subtypes
        List<NamedType> types3 = resolver.collectAndResolveSubtypesByTypeId(config, property, String.class);
        Assert.assertEquals(3, types3.size());
        Assert.assertEquals(Child1.class, types3.get(0).getType().asSubTypeOf(String.class));
        Assert.assertEquals(Child2.class, types3.get(1).getType().asSubTypeOf(String.class));
        Assert.assertEquals(Child3.class, types3.get(2).getType().asSubTypeOf(String.class));

        // Test collectAndResolveSubtypesByTypeId with null base type
        List<NamedType> types4 = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        Assert.assertEquals(0, types4.size());
    }

    @Test
    public void testEdgeCases() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child2.class);
        resolver.registerSubtypes(Child3.class);

        // Test collectAndResolveSubtypesByClass with empty collection
        List<NamedType> types1 = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(0, types1.size());

        // Test collectAndResolveSubtypesByClass with base type equal to one of registered subtypes
        List<NamedType> types2 = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), Child1.class, null);
        Assert.assertEquals(1, types2.size());
        Assert.assertEquals(Child1.class, types2.get(0).getType());

        // Test collectAndResolveSubtypesByClass with base type equal to unknown type
        List<NamedType> types3 = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), UnknownType.class, null);
        Assert.assertEquals(0, types3.size());

        // Test collectAndResolveSubtypesByClass with base type equal to null
        List<NamedType> types4 = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(0, types4.size());

        // Test collectAndResolveSubtypesByTypeId with base type equal to one of registered subtypes
        List<NamedType> types5 = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.StandardMapperConfig(), null, Child1.class);
        Assert.assertEquals(1, types5.size());
        Assert.assertEquals(Child1.class, types5.get(0).getType());

        // Test collectAndResolveSubtypesByTypeId with base type equal to unknown type
        List<NamedType> types6 = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.StandardMapperConfig(), null, UnknownType.class);
        Assert.assertEquals(0, types6.size());

        // Test collectAndResolveSubtypesByTypeId with base type equal to null
        List<NamedType> types7 = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.StandardMapperConfig(), null, null);
        Assert.assertEquals(0, types7.size());

        // Test collectAndResolveSubtypesByTypeId with base type that is not assignable to any registered subtypes
        List<NamedType> types8 = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.StandardMapperConfig(), null, Integer.class);
        Assert.assertEquals(0, types8.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistration() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistrationCollection() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new Object[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistrationCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardMapperConfig(), null, null);
        resolver.registerSubtypes(types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistrationCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.StandardMapperConfig(), null, null);
        resolver.registerSubtypes(types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistrationNullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Child1.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistrationNullType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(null, Child1.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistrationDuplicateRegistration() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Child1.class);
        resolver.registerSubtypes(Child1.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistrationRegistrationOfSuperType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(Parent.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistrationCollectionWithNullClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        List<Class<?>> classes = new ArrayList<>();
        classes.add(null);
        resolver.registerSubtypes(classes);
    }

    static class Parent {
        public static class Child1 {}
        public static class Child2 {}
        public static class Child3 {}
    }

    static class ParentWithNoSubtypes {}

    static class UnknownType {}
}