package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructEmpty() {
        try {
            SimpleType.construct(Map.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Collection.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType.construct(String[].class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testBuildCanonicalNameNoBindings() {
        SimpleType notSpecializedType = new SimpleType(OrderedPair.class);
        assertEquals("com.fasterxml.jackson.databind.type.OrderedPair", notSpecializedType.buildCanonicalName());
        
        SimpleType specializedType = new SimpleType(OrderedPair.class, new TypeBindings());
        assertEquals("com.fasterxml.jackson.databind.type.OrderedPair", specializedType.buildCanonicalName());
    }

    @Test
    public void testBuildCanonicalNameWithBindings() {
        TypeBindings bindings = new TypeBindings();
        bindings.addTypeParameter(new SimpleType(String.class));
        bindings.addTypeParameter(new SimpleType(Integer.class));
        
        SimpleType notSpecializedType = new SimpleType(OrderedPair.class, bindings);
        assertEquals("com.fasterxml.jackson.databind.type.OrderedPair<java.lang.String,java.lang.Integer>", notSpecializedType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType notContainerType = new SimpleType(OrderedPair.class);
        assertFalse(notContainerType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder expected = new StringBuilder();
        expected.append("Lcom/fasterxml/jackson/databind/type/OrderedPair;");
        
        SimpleType type = new SimpleType(OrderedPair.class);
        StringBuilder actual = new StringBuilder();
        type.getErasedSignature(actual);
        
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder expected = new StringBuilder();
        expected.append("Lcom/fasterxml/jackson/databind/type/OrderedPair<Ljava/lang/String;Ljava/lang/Integer;>;");
        
        TypeBindings bindings = new TypeBindings();
        bindings.addTypeParameter(new SimpleType(String.class));
        bindings.addTypeParameter(new SimpleType(Integer.class));
        
        SimpleType type = new SimpleType(OrderedPair.class, bindings);
        
        StringBuilder actual = new StringBuilder();
        type.getGenericSignature(actual);
        
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        SimpleType notSpecializedType = new SimpleType(OrderedPair.class);
        assertEquals("[simple type, class com.fasterxml.jackson.databind.type.OrderedPair]", notSpecializedType.toString());
    }


    public static class OrderedPair<A, B> {
        private final A first;
        private final B second;

        public OrderedPair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }
}