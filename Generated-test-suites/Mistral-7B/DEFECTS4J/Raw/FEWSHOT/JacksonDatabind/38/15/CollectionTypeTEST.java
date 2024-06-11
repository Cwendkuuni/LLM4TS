package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        assertNotNull(listType);
        assertEquals("java.util.ArrayList, contains class java.lang.String", listType.toString());
    }

    @Test
    public void testConstructDeprecated() {
        try {
            JavaType listType = CollectionType.construct(ArrayList.class, String.class);
            assertFalse("Deprecated constructor should not be called", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeRefined = listType.refine(LinkedList.class, null, null, null);
        assertNotNull(listTypeRefined);
        assertEquals("java.util.LinkedList, contains class java.lang.String", listTypeRefined.toString());
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        try {
            JavaType listTypeNarrowed = listType._narrow(LinkedList.class);
            fail("Narrowing to unrelated collection type should not be possible");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeWithIntContent = listType.withContentType(Integer.TYPE);
        assertNotNull(listTypeWithIntContent);
        assertEquals("java.util.ArrayList, contains class int", listTypeWithIntContent.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        Object intHandler = new Object() {};
        JavaType listTypeWithIntHandler = listType.withContentTypeHandler(intHandler);
        assertNotNull(listTypeWithIntHandler);
        assertEquals("java.util.ArrayList, contains class int, content handler: class org.junit.internal.Assert$NotNullHandler", listTypeWithIntHandler.toString());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        Object handler = new Object() {};
        JavaType listTypeWithHandler = listType.withTypeHandler(handler);
        assertNotNull(listTypeWithHandler);
        assertEquals("java.util.ArrayList, handler: class org.junit.internal.Assert$NotNullHandler", listTypeWithHandler.toString());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        Object handler = new Object() {};
        JavaType listTypeWithValueHandler = listType.withValueHandler(handler);
        assertNotNull(listTypeWithValueHandler);
        assertEquals("java.util.ArrayList, value handler: class org.junit.internal.Assert$NotNullHandler", listTypeWithValueHandler.toString());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        Object handler = new Object() {};
        JavaType listTypeWithContentValueHandler = listType.withContentValueHandler(handler);
        assertNotNull(listTypeWithContentValueHandler);
        assertEquals("java.util.ArrayList, value handler: class org.junit.internal.Assert$NotNullHandler, contains handler: class org.junit.internal.Assert$NotNullHandler", listTypeWithContentValueHandler.toString());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        JavaType listTypeStatic = listType.withStaticTyping();
        assertNotNull(listTypeStatic);
        assertEquals("java.util.ArrayList<java.lang.String>", listTypeStatic.toString());
    }
}