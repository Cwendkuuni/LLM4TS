package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

    @Test
    public void testCollectionTypeConstructor() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
        CollectionType collectionType = new CollectionType(listClass, null, null, null, JavaType.of(Integer.class), null, null, false);
        assertEquals(listType, collectionType);
    }

    @Test
    public void testDeprecatedConstructMethod() {
        try {
            CollectionType collectionType = new CollectionType(List.class, JavaType.of(Integer.class));
            fail("Expected DeprecationException");
        } catch (DeprecationException e) {
            // Expected
        }
    }

    @Test
    public void testNarrowMethod() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
        CollectionType listCollectionType = new CollectionType(listType, null, null, null, JavaType.of(Integer.class), null, null, false);
        CollectionType arrayListCollectionType = new CollectionType(arrayListClass, null, null, null, JavaType.of(Integer.class), null, null, false);
        CollectionType narrowedArrayListCollectionType = listCollectionType.narrow(ArrayList.class);
        assertEquals(arrayListCollectionType, narrowedArrayListCollectionType);
    }

    @Test
    public void testDeprecatedNarrowMethod() {
        try {
            CollectionType collectionType = new CollectionType(List.class, JavaType.of(Integer.class));
            CollectionType narrowedCollectionType = collectionType._narrow(ArrayList.class);
            fail("Expected DeprecationException");
        } catch (DeprecationException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<List<String>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(String.class));
        CollectionType listCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(String.class), null, null, false);
        JavaType stringListType = listCollectionType.withContentType(JavaType.of(String[].class));
        CollectionType stringListCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(String[].class), null, null, false);
        assertEquals(stringListCollectionType, stringListType);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
        CollectionType listCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(Integer.class), null, null, false);
        Object handler = new Object();
        CollectionType handlerListCollectionType = listCollectionType.withTypeHandler(handler);
        assertEquals(handler, handlerListCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<List<String>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(String.class));
        CollectionType listCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(String.class), null, null, false);
        Object handler = new Object();
        JavaType stringListType = listCollectionType.withContentTypeHandler(handler);
        CollectionType stringListCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(String.class), null, handler, false);
        assertEquals(stringListCollectionType, stringListType);
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
        CollectionType listCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(Integer.class), null, null, false);
        Object handler = new Object();
        CollectionType handlerListCollectionType = listCollectionType.withValueHandler(handler);
        assertEquals(handler, handlerListCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<List<String>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(String.class));
        CollectionType listCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(String.class), null, null, false);
        Object handler = new Object();
        JavaType stringListType = listCollectionType.withContentValueHandler(handler);
        CollectionType stringListCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(String.class), null, handler, false);
        assertEquals(stringListCollectionType, stringListType);
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
        CollectionType listCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(Integer.class), null, null, false);
        CollectionType staticListCollectionType = listCollectionType.withStaticTyping();
        assertTrue(staticListCollectionType.isStatic());
    }

    @Test
    public void testRefineMethod() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
        CollectionType listCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(Integer.class), null, null, false);
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        CollectionType refinedCollectionType = listCollectionType.refine(arrayListClass, null, null, null);
        assertEquals(new CollectionType(arrayListClass, null, null, null, JavaType.of(Integer.class), null, null, false), refinedCollectionType);
    }

    @Test
    public void testToStringMethod() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.of(Integer.class));
        CollectionType listCollectionType = new CollectionType(listClass, null, null, null, JavaType.of(Integer.class), null, null, false);
        assertEquals("[collection type; class " + List.class.getName() + ", contains " + Integer.class + "]", listCollectionType.toString());
    }
}