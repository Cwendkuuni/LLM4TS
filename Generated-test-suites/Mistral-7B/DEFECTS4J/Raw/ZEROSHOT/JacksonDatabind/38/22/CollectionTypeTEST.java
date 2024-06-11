package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

    @Test
    public void testCollectionTypeConstruction() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, null);
        assertNotNull(listType);
        assertEquals(listClass, listType.getRawClass());
    }

    @Test
    public void testCollectionTypeConstructDeprecated() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null);
        assertNotNull(listType);
        assertEquals(listClass, listType.getRawClass());
    }

    @Test
    public void testCollectionTypeNarrow() {
        Class<?> listClass = ArrayList.class;
        Class<?> subListClass = ArrayListSubclass.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, null);
        JavaType narrowedType = listType.narrow(subListClass);
        assertNotNull(narrowedType);
        assertEquals(subListClass, narrowedType.getRawClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCollectionTypeNarrowDeprecated() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null);
        JavaType narrowedType = listType.narrow(ArrayListSubclass.class);
    }

    @Test
    public void testCollectionTypeWithContentType() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, null);
        JavaType elemType = IntType.INSTANCE;
        JavaType listWithIntType = listType.withContentType(elemType);
        assertNotNull(listWithIntType);
        assertEquals(listClass, listWithIntType.getRawClass());
        assertEquals(elemType, listWithIntType.getContentType());
    }

    @Test
    public void testCollectionTypeWithTypeHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, null);
        Object handler = new Object() {};
        JavaType listWithHandler = listType.withTypeHandler(handler);
        assertNotNull(listWithHandler);
        assertEquals(listClass, listWithHandler.getRawClass());
        assertEquals(handler, listWithHandler.getTypeHandler());
    }

    @Test
    public void testCollectionTypeWithContentTypeHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, null);
        Object elemHandler = new Object() {};
        Object listHandler = new Object() {};
        JavaType listWithHandlers = listType.withContentTypeHandler(elemHandler).withTypeHandler(listHandler);
        assertNotNull(listWithHandlers);
        assertEquals(listClass, listWithHandlers.getRawClass());
        assertEquals(elemHandler, listWithHandlers.getContentTypeHandler());
        assertEquals(listHandler, listWithHandlers.getTypeHandler());
    }

    @Test
    public void testCollectionTypeWithValueHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, null);
        Object handler = new Object() {};
        JavaType listWithHandler = listType.withValueHandler(handler);
        assertNotNull(listWithHandler);
        assertEquals(listClass, listWithHandler.getRawClass());
        assertEquals(handler, listWithHandler.getValueHandler());
    }

    @Test
    public void testCollectionTypeWithContentValueHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, null);
        Object elemHandler = new Object() {};
        Object listHandler = new Object() {};
        JavaType listWithHandlers = listType.withContentValueHandler(elemHandler).withValueHandler(listHandler);
        assertNotNull(listWithHandlers);
        assertEquals(listClass, listWithHandlers.getRawClass());
        assertEquals(elemHandler, listWithHandlers.getContentValueHandler());
        assertEquals(listHandler, listWithHandlers.getValueHandler());
    }

    @Test
    public void testCollectionTypeWithStaticTyping() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, null);
        JavaType staticListType = listType.withStaticTyping();
        assertTrue(staticListType.isStatic());
        assertFalse(listType.isStatic());
    }

    @Test
    public void testCollectionTypeRefine() {
        Class<?> listClass = ArrayList.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, null);
        Class<?> subListClass = ArrayListSubclass.class;
        JavaType refinedType = listType.refine(subListClass, null, null, null);
        assertNotNull(refinedType);
        assertEquals(subListClass, refinedType.getRawClass());
    }
}