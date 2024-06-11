package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test mapping Collection to LinkedList
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        // Test mapping Map to HashMap
        resolver.addMapping(Map.class, HashMap.class);
        resolver.addMapping(Map.class, TreeMap.class);

        // Test mapping abstract class to another abstract class
        resolver.addMapping(AbstractClass1.class, AbstractClass2.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType mappedType = resolver.findTypeMapping(null, javaType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(TreeMap.class);
        mappedType = resolver.findTypeMapping(null, javaType);
        assertEquals(HashMap.class, mappedType.getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(AbstractClass1.class);
        mappedType = resolver.findTypeMapping(null, javaType);
        assertEquals(AbstractClass2.class, mappedType.getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertNull(resolver.findTypeMapping(null, javaType));
    }

    static class AbstractClass1 {
        // Abstract class 1
    }

    static class AbstractClass2 {
        // Abstract class 2
    }

    static class AbstractClass3 extends AbstractClass1 {
        // Abstract class 3 which extends AbstractClass1
    }

    static class AbstractClass4 extends AbstractClass2 {
        // Abstract class 4 which extends AbstractClass2
    }

    static class ConcreteClass extends AbstractClass3 {
        // Concrete class which extends AbstractClass3
    }

    @Test
    public void testFindTypeMappingWithInheritance() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractClass1.class, AbstractClass4.class);

        JavaType javaType = TypeFactory.defaultInstance().constructType(ConcreteClass.class);
        JavaType mappedType = resolver.findTypeMapping(null, javaType);
        assertEquals(AbstractClass4.class, mappedType.getRawClass());
    }

    // Test for cycle in inheritance
    @Test(expected = IllegalArgumentException.class)
    public void testFindTypeMappingWithCycle() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractClass1.class, AbstractClass2.class);
        resolver.addMapping(AbstractClass2.class, AbstractClass1.class);

        JavaType javaType = TypeFactory.defaultInstance().constructType(AbstractClass1.class);
        resolver.findTypeMapping(null, javaType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType javaType = TypeFactory.defaultInstance().constructType(AbstractClass1.class);
        JavaType mappedType = resolver.resolveAbstractType(null, javaType);
        assertNull(mappedType);
    }
}