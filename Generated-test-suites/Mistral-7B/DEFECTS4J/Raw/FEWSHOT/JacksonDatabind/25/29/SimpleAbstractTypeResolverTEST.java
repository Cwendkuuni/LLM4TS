package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNotNull(resolver);
        assertEquals(0, resolver._mappings.size());
    }

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> listClass = List.class;
        Class<ArrayList> arrayListClass = ArrayList.class;

        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, ArrayList.class);
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        assertEquals(6, resolver._mappings.size());

        Map<ClassKey, Class<?>> expectedMappings = new HashMap<>();
        expectedMappings.put(new ClassKey(Collection.class), ArrayList.class);
        expectedMappings.put(new ClassKey(List.class), ArrayList.class);
        expectedMappings.put(new ClassKey(Collection.class), LinkedList.class);
        expectedMappings.put(new ClassKey(List.class), LinkedList.class);

        for (Map.Entry<ClassKey, Class<?>> entry : resolver._mappings.entrySet()) {
            assertEquals(entry.getValue(), expectedMappings.get(entry.getKey()));
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<Integer> intType = Integer.class;
        Class<List> listType = List.class;
        Class<ArrayList> arrayListType = ArrayList.class;
        Class<LinkedList> linkedListType = LinkedList.class;

        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, ArrayList.class);
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType javaType1 = JavaType.construct(intType, Collections.emptyMap(), null);
        assertNull(resolver.findTypeMapping(null, javaType1));

        JavaType javaType2 = JavaType.construct(List.class, Collections.emptyMap(), null);
        JavaType expectedJavaType2 = JavaType.construct(ArrayList.class, Collections.emptyMap(), null);
        assertEquals(expectedJavaType2, resolver.findTypeMapping(null, javaType2));

        JavaType javaType3 = JavaType.construct(LinkedList.class, Collections.emptyMap(), null);
        JavaType expectedJavaType3 = javaType3;
        assertEquals(expectedJavaType3, resolver.findTypeMapping(null, javaType3));

        JavaType javaType4 = JavaType.construct(ArrayList.class, Collections.emptyMap(), null);
        assertNull(resolver.findTypeMapping(null, javaType4));

        JavaType javaType5 = JavaType.construct(LinkedList.class, Collections.emptyMap(), null);
        assertEquals(javaType5, resolver.findTypeMapping(null, javaType5));

        JavaType javaType6 = JavaType.construct(List.class, Collections.emptyMap(), null);
        JavaType expectedJavaType6 = JavaType.construct(ArrayList.class, Collections.emptyMap(), null);
        assertEquals(expectedJavaType6, resolver.findTypeMapping(null, javaType6));

        JavaType javaType7 = JavaType.construct(Collection.class, Collections.emptyMap(), null);
        JavaType expectedJavaType7 = JavaType.construct(LinkedList.class, Collections.emptyMap(), null);
        assertEquals(expectedJavaType7, resolver.findTypeMapping(null, javaType7));

        JavaType javaType8 = JavaType.construct(LinkedList.class, Collections.emptyMap(), null);
        assertEquals(javaType8, resolver.findTypeMapping(null, javaType8));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<Integer> intType = Integer.class;
        Class<List> listType = List.class;
        Class<ArrayList> arrayListType = ArrayList.class;
        Class<LinkedList> linkedListType = LinkedList.class;

        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(List.class, ArrayList.class);
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType javaType1 = JavaType.construct(intType, Collections.emptyMap(), null);
        assertNull(resolver.resolveAbstractType(null, javaType1));

        JavaType javaType2 = JavaType.construct(List.class, Collections.emptyMap(), null);
        assertNull(resolver.resolveAbstractType(null, javaType2));
    }
}