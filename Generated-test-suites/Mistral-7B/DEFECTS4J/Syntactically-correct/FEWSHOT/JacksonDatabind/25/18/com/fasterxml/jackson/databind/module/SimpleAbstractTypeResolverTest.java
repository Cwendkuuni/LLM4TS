package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test adding mappings for abstract types with a subtype
        Class<?> superType = Collection.class;
        Class<? extends Collection> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        assertNotNull(resolver._mappings.get(new ClassKey(superType)));
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));

        // Test adding mappings for abstract types with an abstract subtype
        superType = List.class;
        Class<? extends List> abstractSubType = ArrayList.class;
        resolver.addMapping(superType, abstractSubType);
        assertNotNull(resolver._mappings.get(new ClassKey(superType)));
        assertEquals(abstractSubType, resolver._mappings.get(new ClassKey(superType)));

        // Test adding mappings for abstract types with a concrete subtype
        superType = Collection.class;
        Class<? extends Collection> concreteSubType = ArrayList.class;
        resolver.addMapping(superType, concreteSubType);
        assertNotNull(resolver._mappings.get(new ClassKey(superType)));
        assertEquals(concreteSubType, resolver._mappings.get(new ClassKey(superType)));

        // Test adding mapping for incompatible types
        Class<?> invalidSuperType = String.class;
        Class<? extends Number> invalidSubType = Integer.class;
        try {
            resolver.addMapping(invalidSuperType, invalidSubType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test adding mapping for same class
        try {
            resolver.addMapping(Integer.class, Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test adding mapping for non-abstract super type
        try {
            Class<?> nonAbstractSuperType = Integer.TYPE.getClass();
            Class<? extends Number> subType2 = Long.class;
            resolver.addMapping(nonAbstractSuperType, subType2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType javaType = new ClassType(Collection.class);
        JavaType mappedType = resolver.findTypeMapping(null, javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        javaType = new ClassType(List.class);
        mappedType = resolver.findTypeMapping(null, javaType);
        assertNotNull(mappedType);
        assertEquals(ArrayList.class, mappedType.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithNullType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, ArrayList.class);

        JavaType mappedType = resolver.findTypeMapping(null, null);
        assertNull(mappedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType javaType = new ClassType(Collection.class);
        JavaType mappedType = resolver.resolveAbstractType(null, javaType);
        assertNull(mappedType);
    }

    @Test
    public void testSimpleAbstractTypeResolverSerialization() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Add some mappings
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, ArrayList.class);

        byte[] serialized = Helper.serialize(resolver);
        SimpleAbstractTypeResolver deserialized = (SimpleAbstractTypeResolver) Helper.deserialize(serialized);

        // Check that mappings are still present after deserialization
        assertEquals(2, deserialized._mappings.size());

        JavaType javaType = new ClassType(Collection.class);
        JavaType mappedType = deserialized.findTypeMapping(null, javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        javaType = new ClassType(List.class);
        mappedType = deserialized.findTypeMapping(null, javaType);
        assertNotNull(mappedType);
        assertEquals(ArrayList.class, mappedType.getRawClass());
    }

    private static byte[] serialize(Object obj) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream());
        out.writeObject(obj);
        return out.toByteArray();
    }

    private static Object deserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return new ObjectInputStream(in).readObject();
    }

    private static byte[] serialize(Object obj, String className) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(className));
        out.writeObject(obj);
        out.close();
        return Files.readAllBytes(Paths.get(className));
    }

    private static Object deserialize(String className) throws Exception {
        return new ObjectInputStream(new FileInputStream(className)).readObject();
    }

    static class Helper {
        static byte[] serialize(Object obj) throws Exception {
            return serialize(obj, "tempFile.ser");
        }

        static Object deserialize(String className) throws Exception {
            return deserialize(className);
        }

        static Object deserialize(byte[] bytes) throws Exception {
            return deserialize(new ByteArrayInputStream(bytes));
        }

        static Object deserialize(InputStream inputStream) throws Exception {
            return new ObjectInputStream(inputStream).readObject();
        }
    }
}