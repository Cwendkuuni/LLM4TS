package com.yourpackage.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> intListType = new ArrayList<Integer>().getClass();
        CollectionType collectionType = CollectionType.construct(intListType, null, null, null, null);
        assertEquals(intListType, collectionType.getRawClass());
    }

    @Test
    public void testConstructWithBindings() {
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        assertEquals(List.class, collectionType.getRawClass());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> intListType = new ArrayList<Integer>().getClass();
        CollectionType collectionType = CollectionType.construct(intListType, null);
        assertEquals(intListType, collectionType.getRawClass());
    }

    @Test
    public void testNarrow() {
        Class<ArrayList<Integer>> intArrayListType = new ArrayList<Integer>().getClass();
        CollectionType intListType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        CollectionType intArrayListTypeWrapper = intListType.narrow(intArrayListType);
        assertEquals(intArrayListType, intArrayListTypeWrapper.getRawClass());
    }

    @Test
    public void testWithContentType() {
        Class<List<String>> stringListType = new ArrayList<String>().getClass();
        CollectionType intListType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        CollectionType stringListTypeWrapper = intListType.withContentType(String.class);
        assertEquals(stringListType, stringListTypeWrapper.getRawClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionType intListType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        Object handler = new Object();
        CollectionType stringListTypeWrapper = intListType.withContentTypeHandler(handler);
        assertNotSame(intListType, stringListTypeWrapper);
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType intListType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        Object handler = new Object();
        CollectionType intListTypeWrapper = intListType.withTypeHandler(handler);
        assertNotSame(intListType, intListTypeWrapper);
    }

    @Test
    public void testWithValueHandler() {
        CollectionType intListType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        Object handler = new Object();
        CollectionType intListTypeWrapper = intListType.withValueHandler(handler);
        assertNotSame(intListType, intListTypeWrapper);
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionType intListType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        Object handler = new Object();
        CollectionType intListTypeWrapper = intListType.withContentValueHandler(handler);
        assertNotSame(intListType, intListTypeWrapper);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType intListType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        CollectionType staticIntListTypeWrapper = intListType.withStaticTyping();
        assertTrue(staticIntListTypeWrapper.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineWithInvalidClass() {
        CollectionType intListType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        CollectionType refinedType = intListType.refine(String.class, null, null, null);
    }

    @Test
    public void testRefine() {
        CollectionType intListType = CollectionType.construct(List.class, null, null, null, Integer.TYPE);
        CollectionType refinedType = intListType.refine(ArrayList.class, null, null, null);
        assertEquals(ArrayList.class, refinedType.getRawClass());
    }
}