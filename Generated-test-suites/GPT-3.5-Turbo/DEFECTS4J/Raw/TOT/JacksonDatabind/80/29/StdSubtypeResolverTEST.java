
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

    @Test
    public void testRegisterSubtypes() {
        NamedType firstSubtype = new NamedType(FirstSubtype.class, "FirstSubtype");
        NamedType secondSubtype = new NamedType(SecondSubtype.class, "SecondSubtype");
        NamedType thirdSubtype = new NamedType(ThirdSubtype.class, "ThirdSubtype");

        subtypeResolver.registerSubtypes(firstSubtype, secondSubtype, thirdSubtype);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

        Assert.assertEquals(3, resolvedSubtypes.size());
        Assert.assertTrue(resolvedSubtypes.contains(firstSubtype));
        Assert.assertTrue(resolvedSubtypes.contains(secondSubtype));
        Assert.assertTrue(resolvedSubtypes.contains(thirdSubtype));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        NamedType firstSubtype = new NamedType(FirstSubtype.class, "FirstSubtype");
        NamedType secondSubtype = new NamedType(SecondSubtype.class, "SecondSubtype");
        NamedType thirdSubtype = new NamedType(ThirdSubtype.class, "ThirdSubtype");

        subtypeResolver.registerSubtypes(firstSubtype, secondSubtype);
        subtypeResolver.registerSubtypes(ThirdSubtype.class);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        Assert.assertEquals(3, resolvedSubtypes.size());
        Assert.assertTrue(resolvedSubtypes.contains(firstSubtype));
        Assert.assertTrue(resolvedSubtypes.contains(secondSubtype));
        Assert.assertTrue(resolvedSubtypes.contains(thirdSubtype));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        NamedType firstSubtype = new NamedType(FirstSubtype.class, "FirstSubtype");
        NamedType secondSubtype = new NamedType(SecondSubtype.class, "SecondSubtype");
        NamedType thirdSubtype = new NamedType(ThirdSubtype.class, "ThirdSubtype");

        subtypeResolver.registerSubtypes(firstSubtype, secondSubtype);
        subtypeResolver.registerSubtypes(ThirdSubtype.class);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        Assert.assertEquals(3, resolvedSubtypes.size());
        Assert.assertTrue(resolvedSubtypes.contains(firstSubtype));
        Assert.assertTrue(resolvedSubtypes.contains(secondSubtype));
        Assert.assertTrue(resolvedSubtypes.contains(thirdSubtype));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithAnnotatedTypes() {
        MapperConfig config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        NamedType annotatedSubtype = new NamedType(AnnotatedSubtype.class, "AnnotatedSubtype");

        subtypeResolver.registerSubtypes(annotatedSubtype);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        Assert.assertEquals(1, resolvedSubtypes.size());
        Assert.assertTrue(resolvedSubtypes.contains(annotatedSubtype));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedTypes() {
        MapperConfig config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        NamedType annotatedSubtype = new NamedType(AnnotatedSubtype.class, "AnnotatedSubtype");

        subtypeResolver.registerSubtypes(annotatedSubtype);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        Assert.assertEquals(1, resolvedSubtypes.size());
        Assert.assertTrue(resolvedSubtypes.contains(annotatedSubtype));
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        Class<?> rawBaseType = BaseClass.class;

        NamedType namedType = new NamedType(SpecialClass.class, "SpecialClass");

        Set<Class<?>> handledTypes = new HashSet<>();
        handledTypes.add(SpecialClass.class);

        Map<String, NamedType> namedTypes = new HashMap<>();
        namedTypes.put("OtherClass1", new NamedType(OtherClass1.class, "OtherClass1"));
        namedTypes.put("OtherClass2", new NamedType(OtherClass2.class, "OtherClass2"));

        Collection<NamedType> combinedTypes = subtypeResolver._combineNamedAndUnnamed(rawBaseType, handledTypes, namedTypes);

        Assert.assertEquals(3, combinedTypes.size());
        Assert.assertTrue(combinedTypes.contains(namedType));
        Assert.assertTrue(combinedTypes.contains(new NamedType(OtherClass1.class, "OtherClass1")));
        Assert.assertTrue(combinedTypes.contains(new NamedType(OtherClass2.class, "OtherClass2")));
    }

    private static class BaseClass {

    }

    private static class SpecialClass extends BaseClass implements AnnotatedClass {

    }

    private static class OtherClass1 extends BaseClass {

    }

    private static class OtherClass2 extends BaseClass {

    }

    private static class FirstSubtype {

    }

    private static class SecondSubtype {

    }

    private static class ThirdSubtype {

    }

    private static class AnnotatedSubtype {

    }
}

