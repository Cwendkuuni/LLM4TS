
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        // Test for CLASS_AS_ARRAYS_LIST
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));

        // Test for CLASS_SINGLETON_LIST
        assertTrue(JavaUtilCollectionsDeserializers.findForCollection(null, JavaType.fromClass(Collections.emptyList().getClass())) instanceof StdDelegatingDeserializer<?>);

        // Test for CLASS_SINGLETON_SET
        assertTrue(JavaUtilCollectionsDeserializers.findForCollection(null, JavaType.fromClass(Collections.emptySet().getClass())) instanceof StdDelegatingDeserializer<?>);

        // Test for CLASS_UNMODIFIABLE_LIST
        assertTrue(JavaUtilCollectionsDeserializers.findForCollection(null, JavaType.fromClass(Collections.unmodifiableList(Collections.emptyList()).getClass())) instanceof StdDelegatingDeserializer<?>);

        // Test for CLASS_UNMODIFIABLE_SET
        assertTrue(JavaUtilCollectionsDeserializers.findForCollection(null, JavaType.fromClass(Collections.unmodifiableSet(Collections.emptySet()).getClass())) instanceof StdDelegatingDeserializer<?>);

        // Test for invalid type
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, JavaType.fromClass(String.class)));
    }

    @Test
    public void testFindForMap() {
        // Test for CLASS_SINGLETON_MAP
        assertTrue(JavaUtilCollectionsDeserializers.findForMap(null, JavaType.fromClass(Collections.singletonMap(null, null).getClass())) instanceof StdDelegatingDeserializer<?>);

        // Test for CLASS_UNMODIFIABLE_MAP
        assertTrue(JavaUtilCollectionsDeserializers.findForMap(null, JavaType.fromClass(Collections.unmodifiableMap(Collections.singletonMap(null, null)).getClass())) instanceof StdDelegatingDeserializer<?>);

        // Test for invalid type
        assertNull(JavaUtilCollectionsDeserializers.findForMap(null, JavaType.fromClass(String.class)));
    }

    @Test
    public void testJavaUtilCollectionsConverter() {
        // Test for TYPE_SINGLETON_SET
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter1 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        Set<Object> set1 = Collections.singleton(Boolean.TRUE);
        assertEquals(Collections.singleton(set1.iterator().next()), converter1.convert(set1));

        // Test for TYPE_SINGLETON_LIST
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter2 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);
        List<Object> list1 = Collections.singletonList(Boolean.TRUE);
        assertEquals(Collections.singletonList(list1.get(0)), converter2.convert(list1));

        // Test for TYPE_SINGLETON_MAP
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter3 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null);
        Map<Object, Object> map1 = Collections.singletonMap("a", "b");
        Map.Entry<?, ?> entry = map1.entrySet().iterator().next();
        assertEquals(Collections.singletonMap(entry.getKey(), entry.getValue()), converter3.convert(map1));

        // Test for TYPE_UNMODIFIABLE_SET
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter4 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, null);
        Set<Object> set2 = Collections.singleton(Boolean.TRUE);
        assertEquals(Collections.unmodifiableSet(set2), converter4.convert(set2));

        // Test for TYPE_UNMODIFIABLE_LIST
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter5 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, null);
        List<Object> list2 = Collections.singletonList(Boolean.TRUE);
        assertEquals(Collections.unmodifiableList(list2), converter5.convert(list2));

        // Test for TYPE_UNMODIFIABLE_MAP
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter6 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, null);
        Map<Object, Object> map2 = Collections.singletonMap("a", "b");
        assertEquals(Collections.unmodifiableMap(map2), converter6.convert(map2));

        // Test for TYPE_AS_LIST
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter7 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, null);
        List<Object> list3 = Collections.singletonList(Boolean.TRUE);
        assertEquals(list3, converter7.convert(list3));
    }
}
