package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testCollectionType() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", listType.toString());
        assertNotNull(listType.getRawClass());
        assertNotNull(listType.getRawClass().getGenericSuperclass());
        assertNotNull(listType.getBindings());
        assertNotNull(listType.getSuperClass());
        assertNotNull(listType.getSuperInterfaces());
        assertNotNull(listType.getContentType());
        assertNull(listType.getValueHandler());
        assertNull(listType.getTypeHandler());
        assertFalse(listType.isStatic());

        JavaType listTypeStatic = CollectionType.construct(ArrayList.class, null, null, null, String.class, null, null, true);
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", listTypeStatic.toString());
        assertNotNull(listTypeStatic.getRawClass());
        assertNotNull(listTypeStatic.getRawClass().getGenericSuperclass());
        assertNotNull(listTypeStatic.getBindings());
        assertNotNull(listTypeStatic.getSuperClass());
        assertNotNull(listTypeStatic.getSuperInterfaces());
        assertNotNull(listTypeStatic.getContentType());
        assertNull(listTypeStatic.getValueHandler());
        assertNull(listTypeStatic.getTypeHandler());
        assertTrue(listTypeStatic.isStatic());
    }

    @Test
    public void testCollectionTypeWithContentType() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithInt = listType.withContentType(Integer.TYPE);
        assertEquals("[collection type; class java.util.ArrayList, contains int]", listTypeWithInt.toString());
        assertNotNull(listTypeWithInt.getRawClass());
        assertNotNull(listTypeWithInt.getRawClass().getGenericSuperclass());
        assertNotNull(listTypeWithInt.getBindings());
        assertNotNull(listTypeWithInt.getSuperClass());
        assertNotNull(listTypeWithInt.getSuperInterfaces());
        assertNotNull(listTypeWithInt.getContentType());
        assertNull(listTypeWithInt.getValueHandler());
        assertNull(listTypeWithInt.getTypeHandler());
        assertFalse(listTypeWithInt.isStatic());
    }

    @Test
    public void testCollectionTypeWithTypeHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithHandler = listType.withTypeHandler(new Object() {});
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", listType.toString());
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", listTypeWithHandler.toString());
        assertNotNull(listTypeWithHandler.getRawClass());
        assertNotNull(listTypeWithHandler.getRawClass().getGenericSuperclass());
        assertNotNull(listTypeWithHandler.getBindings());
        assertNotNull(listTypeWithHandler.getSuperClass());
        assertNotNull(listTypeWithHandler.getSuperInterfaces());
        assertNotNull(listTypeWithHandler.getContentType());
        assertNotNull(listTypeWithHandler.getValueHandler());
        assertNull(listTypeWithHandler.getTypeHandler());
        assertFalse(listTypeWithHandler.isStatic());
    }

    @Test
    public void testCollectionTypeWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithContentHandler = listType.withContentTypeHandler(new Object() {});
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", listType.toString());
        assertEquals("[collection type; class java.util.ArrayList, contains [Ljava.lang.Object;]", listTypeWithContentHandler.toString());
        assertNotNull(listTypeWithContentHandler.getRawClass());
        assertNotNull(listTypeWithContentHandler.getRawClass().getGenericSuperclass());
        assertNotNull(listTypeWithContentHandler.getBindings());
        assertNotNull(listTypeWithContentHandler.getSuperClass());
        assertNotNull(listTypeWithContentHandler.getSuperInterfaces());
        assertNotNull(listTypeWithContentHandler.getContentType());
        assertNull(listTypeWithContentHandler.getValueHandler());
        assertNotNull(listTypeWithContentHandler.getTypeHandler());
        assertFalse(listTypeWithContentHandler.isStatic());
    }

    @Test
    public void testCollectionTypeWithValueHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithHandler = listType.withValueHandler(new Object() {});
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", listType.toString());
        assertEquals("[collection type; class java.util.ArrayList, contains [Ljava.lang.Object;]", listTypeWithHandler.toString());
        assertNotNull(listTypeWithHandler.getRawClass());
        assertNotNull(listTypeWithHandler.getRawClass().getGenericSuperclass());
        assertNotNull(listTypeWithHandler.getBindings());
        assertNotNull(listTypeWithHandler.getSuperClass());
        assertNotNull(listTypeWithHandler.getSuperInterfaces());
        assertNotNull(listTypeWithHandler.getContentType());
        assertNull(listTypeWithHandler.getValueHandler());
        assertNotNull(listTypeWithHandler.getTypeHandler());
        assertFalse(listTypeWithHandler.isStatic());
    }

    @Test
    public void testCollectionTypeWithContentValueHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithContentHandler = listType.withContentValueHandler(new Object() {});
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", listType.toString());
        assertEquals("[collection type; class java.util.ArrayList, contains [Ljava.lang.Object;]", listTypeWithContentHandler.toString());
        assertNotNull(listTypeWithContentHandler.getRawClass());
        assertNotNull(listTypeWithContentHandler.getRawClass().getGenericSuperclass());
        assertNotNull(listTypeWithContentHandler.getBindings());
        assertNotNull(listTypeWithContentHandler.getSuperClass());
        assertNotNull(listTypeWithContentHandler.getSuperInterfaces());
        assertNotNull(listTypeWithContentHandler.getContentType());
        assertNull(listTypeWithContentHandler.getValueHandler());
        assertNotNull(listTypeWithContentHandler.getTypeHandler());
        assertFalse(listTypeWithContentHandler.isStatic());
    }

    @Test
    public void testCollectionTypeWithStaticTyping() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeStatic = listType.withStaticTyping();
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", listType.toString());
        assertEquals("[collection type; class java.util.ArrayList, contains java.lang.String]", listTypeStatic.toString());
        assertNotNull(listTypeStatic.getRawClass());
        assertNotNull(listTypeStatic.getRawClass().getGenericSuperclass());
        assertNotNull(listTypeStatic.getBindings());
        assertNotNull(listTypeStatic.getSuperClass());
        assertNotNull(listTypeStatic.getSuperInterfaces());
        assertNotNull(listTypeStatic.getContentType());
        assertNull(listTypeStatic.getValueHandler());
        assertNull(listTypeStatic.getTypeHandler());
        assertTrue(listTypeStatic.isStatic());
    }

    @Test
    public void testCollectionTypeRefine() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeRefined = listType.refine(LinkedList.class, null, null, null);
        assertEquals("[collection type; class java.util.LinkedList, contains java.lang.String]", listTypeRefined.toString());
        assertNotNull(listTypeRefined.getRawClass());
        assertNotNull(listTypeRefined.getRawClass().getGenericSuperclass());
        assertNotNull(listTypeRefined.getBindings());
        assertNotNull(listTypeRefined.getSuperClass());
        assertNotNull(listTypeRefined.getSuperInterfaces());
        assertNotNull(listTypeRefined.getContentType());
        assertNull(listTypeRefined.getValueHandler());
        assertNull(listTypeRefined.getTypeHandler());
        assertFalse(listTypeRefined.isStatic());
    }
}