package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind. Jane.assertInstanceOf;
import static com.fasterxml.jackson.databind.Jacksons.newInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {
    private static class TestSet implements Set<String> {
        private final String value;

        TestSet(String value) {
            this.value = value;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return value.equals(o);
        }

        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public String next() {
                    return value;
                }
            };
        }

        @Override
        public Object[] toArray() {
            return new Object[]{value};
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return (T[]) new Object[]{value};
        }

        @Override
        public boolean add(String e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return c.contains(value);
        }

        @Override
        public boolean addAll(Collection<? extends String> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    private static class TestList implements List<String> {
        private final String value;

        TestList(String value) {
            this.value = value;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return value.equals(o);
        }

        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public String next() {
                    return value;
                }
            };
        }

        @Override
        public Object[] toArray() {
            return new Object[]{value};
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return (T[]) new Object[]{value};
        }

        @Override
        public boolean add(String e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends String> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    private static class TestMap implements Map<String, String> {
        private final Map.Entry<String, String> entry;

        TestMap(String key, String value) {
            this.entry = new AbstractMap.SimpleEntry<>(key, value);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return entry.getKey().equals(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return entry.getValue().equals(value);
        }

        @Override
        public Map.Entry<String, String> entrySet() {
            return entry;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestMap testMap = (TestMap) o;

            return entry.equals(testMap.entry);
        }

        @Override
        public int hashCode() {
            return entry.hashCode();
        }
    }

    @Test
    public void testFindForCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JavaType type;

        // Test for empty array list
        type = mapper.getTypeFactory().constructType(List.class, new Class[0]);
        StdValueInstantiator inst = new StdValueInstantiator();
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, type));
        assertInstanceOf(List.class, inst.createValueInstance(type));

        // Test for singleton list
        type = mapper.getTypeFactory().constructType(List.class, String.class);
        Map<String, Class<?>> contextTypeMapper = ctxt.getTypeMapper();
        assertEquals(List.class, contextTypeMapper.mapTypeFromValue(new TextNode("java.util.ArrayList"), type));
        assertEquals(List.class, JavaUtilCollectionsDeserializers.findForCollection(ctxt, type).getInputType(ctxt.getTypeFactory()));
        List<String> list = new ArrayList<>();
        list.add("foo");
        assertEquals("foo", JavaUtilCollectionsDeserializers.findForCollection(ctxt, type).deserialize(ctxt, null, list));

        // Test for unmodifiable list
        type = mapper.getTypeFactory().constructType(List.class, String.class);
        assertEquals(List.class, JavaUtilCollectionsDeserializers.findForCollection(ctxt, type).getInputType(ctxt.getTypeFactory()));
        List<String> unmodList = Collections.unmodifiableList(list);
        assertEquals("foo", JavaUtilCollectionsDeserializers.findForCollection(ctxt, type).deserialize(ctxt, null, unmodList));

        // Test for singleton set
        type = mapper.getTypeFactory().constructType(Set.class, String.class);
        assertEquals(Set.class, JavaUtilCollectionsDeserializers.findForCollection(ctxt, type).getInputType(ctxt.getTypeFactory()));
        Set<String> set = new HashSet<>();
        set.add("foo");
        assertEquals("foo", JavaUtilCollectionsDeserializers.findForCollection(ctxt, type).deserialize(ctxt, null, set));

        // Test for unmodifiable set
        type = mapper.getTypeFactory().constructType(Set.class, String.class);
        assertEquals(Set.class, JavaUtilCollectionsDeserializers.findForCollection(ctxt, type).getInputType(ctxt.getTypeFactory()));
        Set<String> unmodSet = Collections.unmodifiableSet(set);
        assertEquals("foo", JavaUtilCollectionsDeserializers.findForCollection(ctxt, type).deserialize(ctxt, null, unmodSet));

        // Test for singleton map
        type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        MapType mapType = (MapType) contextTypeMapper.mapTypeFromValue(new TextNode("java.util.HashMap"), type);
        assertEquals(Map.class, JavaUtilCollectionsDeserializers.findForMap(ctxt, type).getInputType(ctxt.getTypeFactory()));
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        assertEquals("b", JavaUtilCollectionsDeserializers.findForMap(ctxt, type).deserialize(ctxt, null, map));

        // Test for unmodifiable map
        type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        assertEquals(Map.class, JavaUtilCollectionsDeserializers.findForMap(ctxt, type).getInputType(ctxt.getTypeFactory()));
        Map<String, String> unmodMap = Collections.unmodifiableMap(map);
        assertEquals("b", JavaUtilCollectionsDeserializers.findForMap(ctxt, type).deserialize(ctxt, null, unmodMap));

        // Test for unmodifiable map from unmodifiable collection of maps
        type = mapper.getTypeFactory().constructType(Map.class, new Class[2], Map.class);
        Map<String, Map<String, String>> map2 = new HashMap<>();
        map2.put("a", map);
        assertEquals(map, JavaUtilCollectionsDeserializers.findForMap(ctxt, type).deserialize(ctxt, null, map2));
        Map<String, Map<String, String>> unmodMap2 = Collections.unmodifiableMap(map2);
        assertEquals(map, JavaUtilCollectionsDeserializers.findForMap(ctxt, type).deserialize(ctxt, null, unmodMap2));

        // Test for empty map
        type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        assertInstanceOf(Map.class, new TextNode("{}").getValue(TextNode.ClassLoaderInputFilter.INSTANCE));
        assertEquals(Map.class, JavaUtilCollectionsDeserializers.findForMap(ctxt, type).getInputType(ctxt.getTypeFactory()));
        assertNull(JavaUtilCollectionsDeserializers.findForMap(ctxt, type).deserialize(ctxt, null, new TextNode("{}")));

        // Test for map with multiple entries
        type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String, String> map3 = new HashMap<>();
        map3.put("a", "b");
        map3.put("c", "d");
        assertEquals(map3, JavaUtilCollectionsDeserializers.findForMap(ctxt, type).deserialize(ctxt, null, map3));

        // Test for null input
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, null));
        assertNull(JavaUtilCollectionsDeserializers.findForMap(ctxt, null));
    }
}