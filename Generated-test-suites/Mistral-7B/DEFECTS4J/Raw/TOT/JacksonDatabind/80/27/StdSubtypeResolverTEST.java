package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

/**
 * Test class for {@link StdSubtypeResolver}.
 */
public class StdSubtypeResolverTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPper.configure(MapperFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPper.configure(MapperFeature.AUTO_DETECT_GETTERS_AND_SETTERS, true);
        MAPper.configure(MapperFeature.AUTO_DETECT_TYPES, true);
        MAPper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private static final String TEST_PACKAGE = "com.example";
    private static final String TEST_CLASS1 = "TestClass1";
    private static final String TEST_CLASS2 = "TestClass2";
    private static final String TEST_CLASS3 = "TestClass3";

    @Before
    public void setup() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> testClass1 = Class.forName(TEST_PACKAGE + "." + TEST_CLASS1);
        Class<?> testClass2 = Class.forName(TEST_PACKAGE + "." + TEST_CLASS2);
        Class<?> testClass3 = Class.forName(TEST_PACKAGE + "." + TEST_CLASS3);

        resolver.registerSubtypes(new NamedType(testClass1));
        resolver.registerSubtypes(new NamedType(testClass2));
        resolver.registerSubtypes(new NamedType(testClass3));
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() throws Exception {
        JavaType baseType = MAPper.getTypeFactory().constructType(Object.class);
        AnnotatedMember member = MAPper.getAnnotatedMember(getClass().getDeclaredField("resolver"));
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, member, baseType);

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(3, subtypes.size());
        for (NamedType subtype : subtypes) {
            Assert.assertNotNull(subtype);
            Assert.assertNotNull(subtype.getType());
            Class<?> clazz = subtype.getType();
            Assert.assertEquals(TestClass1.class, clazz == null ? TEST_CLASS1.equals(clazz.getName()) : clazz);
        }
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByClassAnnotated() throws Exception {
        JavaType baseType = MAPper.getTypeFactory().constructType(Object.class);
        Class<?> testClass = Class.forName(TEST_PACKAGE + "." + TEST_CLASS1);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, new AnnotatedClass(testClass));

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(1, subtypes.size());
        NamedType subtype = subtypes.iterator().next();
        Assert.assertNotNull(subtype);
        Assert.assertNotNull(subtype.getType());
        Assert.assertEquals(TestClass1.class, subtype.getType());
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() throws Exception {
        JavaType baseType = MAPper.getTypeFactory().constructType(Object.class);
        AnnotatedMember member = MAPper.getAnnotatedMember(getClass().getDeclaredField("resolver"));
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, member, baseType);

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(3, subtypes.size());
        for (NamedType subtype : subtypes) {
            Assert.assertNotNull(subtype);
            Assert.assertNotNull(subtype.getType());
            Class<?> clazz = subtype.getType();
            if (clazz == null) {
                // anonymous subtypes
                Assert.assertEquals(3, subtypes.size());
                Assert.assertEquals(TestClass1.class.getName(), subtype.getName());
            } else {
                Assert.assertEquals(TestClass1.class, clazz == null ? TEST_CLASS1.equals(clazz.getName()) : clazz);
            }
        }
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotated() throws Exception {
        JavaType baseType = MAPper.getTypeFactory().constructType(Object.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, new AnnotatedClass(getClass()));

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(1, subtypes.size());
        NamedType subtype = subtypes.iterator().next();
        Assert.assertNotNull(subtype);
        Assert.assertNotNull(subtype.getType());
        Assert.assertEquals(StdSubtypeResolverTest.class, subtype.getType());
    }

    private StdSubtypeResolver resolver;

    /**
     * Getter method for the field 'resolver'.
     */
    public StdSubtypeResolver getResolver() {
        return resolver;
    }

    static class AnnotatedClass implements Annotated {
        private static final long serialVersionUID = 1L;

        private final Class<?> _rawClass;

        public AnnotatedClass(Class<?> rawClass) {
            _rawClass = rawClass;
        }

        @Override
        public AnnotatedMember getDeclaredMember(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnnotatedMember getDeclaredMember(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnnotatedClass getDeclaredMemberType(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnnotatedClass getDeclaredMemberType(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Annotated getDeclaredAnnotations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Annotated getDeclaredAnnotations(Class<?> annotationType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Annotated getDeclaredAnnotation(Class<?> annotationType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Annotated getAnnotations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Annotated getAnnotation(Class<?> annotationType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getSimpleName() {
            return _rawClass.getSimpleName();
        }

        @Override
        public String getName() {
            return _rawClass.getName();
        }

        @Override
        public Class<?> getRawType() {
            return _rawClass;
        }

        @Override
        public Type getGenericSuperclass() {
            return null;
        }

        @Override
        public Type[] getInterfaces() {
            return new Type[0];
        }

        @Override
        public Type[] getDeclaredTypes() {
            return new Type[0];
        }

        @Override
        public String getCanonicalName() {
            return _rawClass.getCanonicalName();
        }

        @Override
        public Annotated getEnclosingClass() {
            return null;
        }

        @Override
        public int getModifiers() {
            return _rawClass.getModifiers();
        }
    }
}