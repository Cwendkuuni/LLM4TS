package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        CollectionLikeType clt1 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(clt1.isContainerType());

        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt2 = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(clt2.isContainerType());

        JavaType integerType = TypeFactory.defaultInstance().constructType(int.class);
        CollectionLikeType clt3 = new CollectionLikeType(integerType, null);
        assertFalse(clt3.isContainerType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType clt1 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(clt1.isTrueCollectionType());

        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt2 = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(clt2.isTrueCollectionType());

        JavaType integerType = TypeFactory.defaultInstance().constructType(int.class);
        CollectionLikeType clt3 = new CollectionLikeType(integerType, null);
        assertFalse(clt3.isTrueCollectionType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType clt1 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(clt1.isCollectionLikeType());

        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt2 = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(clt2.isCollectionLikeType());

        JavaType integerType = TypeFactory.defaultInstance().constructType(int.class);
        CollectionLikeType clt3 = new CollectionLikeType(integerType, null);
        assertTrue(clt3.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        JavaType elementType = clt.getContentType();
        assertEquals(Integer.class, elementType.getRawType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        Object valueHandler = clt.getContentValueHandler();
        assertNull(valueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        Object typeHandler = clt.getContentTypeHandler();
        assertNull(typeHandler);
    }

    @Test
    public void testHasHandlers() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(clt.hasHandlers());

        JavaType listTypeWithHandler = TypeFactory.defaultInstance().constructType(MyList.class);
        CollectionLikeType cltWithHandler = new CollectionLikeType(listTypeWithHandler.getRawType(), TypeBindings.emptyBindings(), null, null, null, new MyListHandler(), null, false);
        assertTrue(cltWithHandler.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        clt.getErasedSignature(sb);
        assertEquals("java.util.Collection", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        clt.getGenericSignature(sb);
        assertEquals("java.util.Collection<T>", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        CollectionLikeType clt = new CollectionLikeType(listType.getRawType(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals("java.util.ArrayList", clt.buildCanonicalName());
    }
}