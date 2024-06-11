package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        MapperConfig config = new MapperFactoryBuilder().build();
        subtypeResolver = new StdSubtypeResolver();
        config.setSubtypeResolver(subtypeResolver);
        objectMapper = new ObjectMapper(config);
    }

    /**
     * Test for the registeredSubtypes method.
     */
    @Test
    public void testRegisteredSubtypes() {
        Class<?> parent = Parent.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;

        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Register subtypes by class
        resolver.registerSubtypes(subtype1, subtype2);
        assertEquals(2, resolver.getRegisteredSubtypes().size());
        assertEquals(subtype1.getName(), resolver.getRegisteredSubtypes().get(0).getName());
        assertEquals(subtype2.getName(), resolver.getRegisteredSubtypes().get(1).getName());

        // Register subtypes by annotation
        AnnotationIntrospector introspector = new AnnotationIntrospector();
        List<NamedType> subtypes = introspector.findSubtypes(SubtypeAnnotation.class);
        resolver.registerSubtypes(subtypes.toArray(new NamedType[0]));
        assertEquals(2, resolver.getRegisteredSubtypes().size());
        assertEquals(SubtypeAnnotation1.class.getName(), resolver.getRegisteredSubtypes().get(0).getName());
        assertEquals(SubtypeAnnotation2.class.getName(), resolver.getRegisteredSubtypes().get(1).getName());
    }

    /**
     * Test for the collectAndResolveSubtypesByClass method.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Test with empty list
        assertEquals(Collections.emptyList(), subtypeResolver.collectAndResolveSubtypesByClass(null, null, null));

        // Test with no registered subtypes
        Parent parent = new Parent();
        assertEquals(Collections.singletonList(new NamedType(Parent.class)),
                subtypeResolver.collectAndResolveSubtypesByClass(null, null, null));

        // Test with registered subtypes by class
        subtypeResolver.registerSubtypes(Child1.class, Child2.class);
        Parent parentWithChild1 = new Parent(new Child1());
        assertEquals(3, subtypeResolver.collectAndResolveSubtypesByClass(null, null, null).size());
        assertEquals(Child1.class, subtypeResolver.collectAndResolveSubtypesByClass(null, null, null).get(1).getType());
        assertEquals(Child2.class, subtypeResolver.collectAndResolveSubtypesByClass(null, null, null).get(2).getType());

        // Test with registered subtypes by annotation
        subtypeResolver.registerSubtypes(AnnotationSubtype1.class, AnnotationSubtype2.class);
        Parent parentWithAnnotationSubtype1 = new Parent(new AnnotationSubtype1());
        assertEquals(4, subtypeResolver.collectAndResolveSubtypesByClass(null, null, null).size());
        assertEquals(AnnotationSubtype1.class, subtypeResolver.collectAndResolveSubtypesByClass(null, null, null).get(3).getType());
        assertEquals(AnnotationSubtype2.class, subtypeResolver.collectAndResolveSubtypesByClass(null, null, null).get(4).getType());
    }

    /**
     * Test for the collectAndResolveSubtypesByClass method with recursion.
     */
    @Test
    public void testCollectAndResolveSubtypesByClassWithRecursion() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Register recursive subtypes by class
        subtypeResolver.registerSubtypes(SubtypeWithRecursion1.class, SubtypeWithRecursion2.class);
        SubtypeWithRecursion parentWithRecursion = new SubtypeWithRecursion1();
        assertEquals(4, subtypeResolver.collectAndResolveSubtypesByClass(null, null, null).size());
        assertEquals(SubtypeWithRecursion1.class, subtypeResolver.collectAndResolveSubtypesByClass(null, null, null).get(1).getType());
        assertEquals(SubtypeWithRecursion2.class, subtypeResolver.collectAndResolveSubtypesByClass(null, null, null).get(3).getType());
    }

    /**
     * Test for the collectAndResolveSubtypesByTypeId method.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Test with empty list
        assertEquals(Collections.emptyList(), subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null));

        // Test with no registered subtypes
        Parent parent = new Parent();
        assertEquals(Collections.singletonList(new NamedType(Parent.class)),
                subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null));

        // Test with registered subtypes by class
        subtypeResolver.registerSubtypes(Child1.class, Child2.class);
        Parent parentWithChild1 = new Parent(new Child1());
        assertEquals(3, subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null).size());
        assertEquals(Child1.class, subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null).get(1).getType());
        assertEquals(Child2.class, subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null).get(2).getType());

        // Test with registered subtypes by annotation
        subtypeResolver.registerSubtypes(AnnotationSubtype1.class, AnnotationSubtype2.class);
        Parent parentWithAnnotationSubtype1 = new Parent(new AnnotationSubtype1());
        assertEquals(4, subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null).size());
        assertEquals(AnnotationSubtype1.class, subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null).get(3).getType());
        assertEquals(AnnotationSubtype2.class, subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null).get(4).getType());
    }

    /**
     * Test for the collectAndResolveSubtypesByTypeId method with recursion.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithRecursion() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Register recursive subtypes by class
        subtypeResolver.registerSubtypes(SubtypeWithRecursion1.class, SubtypeWithRecursion2.class);
        SubtypeWithRecursion parentWithRecursion = new SubtypeWithRecursion1();
        assertEquals(4, subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null).size());
        assertEquals(SubtypeWithRecursion1.class, subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null).get(1).getType());
        assertEquals(SubtypeWithRecursion2.class, subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null).get(3).getType());
    }

    /**
     * Test for the collectAndResolveSubtypesByTypeId method with unregistered subtypes.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithUnregisteredSubtypes() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        Parent parent = new Parent();
        subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);
    }

    /**
     * Test for the collectAndResolveSubtypesByTypeId method with circular references.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithCircularReferences() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(CircularReference1.class, CircularReference2.class);
        CircularReference parent = new CircularReference1();
    }

    /**
     * Test for the combineNamedAndUnnamed method.
     */
    @Test
    public void testCombineNamedAndUnnamed() {
        Class<?> parent = Parent.class;
        Class<?> subtype1 = Child1.class;
        Class<?> subtype2 = Child2.class;
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Test with empty list
        assertEquals(Collections.emptyList(), subtypeResolver.combineNamedAndUnnamed(parent, Collections.emptySet(), Collections.emptyMap()));

        // Test with no registered subtypes
        assertEquals(Collections.singletonList(new NamedType(parent)), subtypeResolver.combineNamedAndUnnamed(parent, Collections.singleton(parent), Collections.emptyMap()));

        // Test with registered subtypes by class
        subtypeResolver.registerSubtypes(subtype1, subtype2);
        assertEquals(3, subtypeResolver.combineNamedAndUnnamed(parent, Arrays.asList(parent, subtype1, subtype2), Collections.emptyMap()).size());
        assertEquals(subtype1.getName(), subtypeResolver.combineNamedAndUnnamed(parent, Arrays.asList(parent, subtype1, subtype2), Collections.emptyMap()).get(1).getName());
        assertEquals(subtype2.getName(), subtypeResolver.combineNamedAndUnnamed(parent, Arrays.asList(parent, subtype1, subtype2), Collections.emptyMap()).get(2).getName());

        // Test with registered subtypes by annotation
        subtypeResolver.registerSubtypes(AnnotationSubtype1.class, AnnotationSubtype2.class);
        assertEquals(4, subtypeResolver.combineNamedAndUnnamed(parent, Arrays.asList(parent, AnnotationSubtype1.class, AnnotationSubtype2.class), Collections.emptyMap()).size());
        assertEquals(AnnotationSubtype1.class.getName(), subtypeResolver.combineNamedAndUnnamed(parent, Arrays.asList(parent, AnnotationSubtype1.class, AnnotationSubtype2.class), Collections.emptyMap()).get(3).getType());
        assertEquals(AnnotationSubtype2.class.getName(), subtypeResolver.combineNamedAndUnnamed(parent, Arrays.asList(parent, AnnotationSubtype1.class, AnnotationSubtype2.class), Collections.emptyMap()).get(4).getType());
    }
}

class Parent {
    // Empty class
}

class Child1 extends Parent {
    // Empty class
}

class Child2 extends Parent {
    // Empty class
}

@JsonTypeName("SubtypeAnnotation1")
class SubtypeAnnotation1 extends Parent {
    // Empty class
}

@JsonTypeName("SubtypeAnnotation2")
class SubtypeAnnotation2 extends Parent {
    // Empty class
}

class SubtypeWithRecursion1 extends Parent {
    private SubtypeWithRecursion2 subtypeWithRecursion2;

    public SubtypeWithRecursion1() {
    }

    public SubtypeWithRecursion1(SubtypeWithRecursion2 subtypeWithRecursion2) {
        this.subtypeWithRecursion2 = subtypeWithRecursion2;
    }

    public SubtypeWithRecursion2 getSubtypeWithRecursion2() {
        return subtypeWithRecursion2;
    }

    public void setSubtypeWithRecursion2(SubtypeWithRecursion2 subtypeWithRecursion2) {
        this.subtypeWithRecursion2 = subtypeWithRecursion2;
    }
}

class SubtypeWithRecursion2 extends Parent {
    private SubtypeWithRecursion1 subtypeWithRecursion1;

    public SubtypeWithRecursion2() {
    }

    public SubtypeWithRecursion2(SubtypeWithRecursion1 subtypeWithRecursion1) {
        this.subtypeWithRecursion1 = subtypeWithRecursion1;
    }

    public SubtypeWithRecursion1 getSubtypeWithRecursion1() {
        return subtypeWithRecursion1;
    }

    public void setSubtypeWithRecursion1(SubtypeWithRecursion1 subtypeWithRecursion1) {
        this.subtypeWithRecursion1 = subtypeWithRecursion1;
    }
}

class CircularReference1 extends Parent {
    private CircularReference2 circularReference2;

    public CircularReference1() {
    }

    public CircularReference1(CircularReference2 circularReference2) {
        this.circularReference2 = circularReference2;
    }

    public CircularReference2 getCircularReference2() {
        return circularReference2;
    }

    public void setCircularReference2(CircularReference2 circularReference2) {
        this.circularReference2 = circularReference2;
    }
}

class CircularReference2 extends Parent {
    private CircularReference1 circularReference1;

    public CircularReference2() {
    }

    public CircularReference2(CircularReference1 circularReference1) {
        this.circularReference1 = circularReference1;
    }

    public CircularReference1 getCircularReference1() {
        return circularReference1;
    }

    public void setCircularReference1(CircularReference1 circularReference1) {
        this.circularReference1 = circularReference1;
    }
}

public interface AnnotatedMember {
    // Empty interface
}

public interface AnnotatedClass {
    // Empty interface
}

public class AnnotatedClassResolver {
    // Empty class
}