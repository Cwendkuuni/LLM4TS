package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<String>> listType = CollectionType.construct(List.class, null, String.class, null);
        assertNotNull(listType);
        assertEquals(List.class, listType.getRawType());
        assertEquals(String.class, listType.getComponentType());
        assertEquals(false, listType._asStatic);

        Class<Set<Integer>> setType = CollectionType.construct(Set.class, null, Integer.class, null);
        assertNotNull(setType);
        assertEquals(Set.class, setType.getRawType());
        assertEquals(Integer.class, setType.getComponentType());
        assertEquals(false, setType._asStatic);
    }

    @Test
    public void testConstructWithBindings() {
        TypeBindings bindings = new TypeBindings();
        JavaType listType = CollectionType.construct(List.class, bindings, String.class, null);
        assertNotNull(listType);
        assertEquals(List.class, listType.getRawType());
        assertEquals(String.class, listType.getComponentType());
        assertEquals(false, listType._asStatic);
        assertEquals(bindings, listType._bindings);

        JavaType setType = CollectionType.construct(Set.class, bindings, Integer.class, null);
        assertNotNull(setType);
        assertEquals(Set.class, setType.getRawType());
        assertEquals(Integer.class, setType.getComponentType());
        assertEquals(false, setType._asStatic);
        assertEquals(bindings, setType._bindings);
    }

    @Test
    public void testConstructDeprecated() {
        try {
            JavaType listType = CollectionType.construct(List.class, null);
            fail("Expected DeprecationException");
        } catch (DeprecationException e) {
            // Expected
        }

        try {
            JavaType setType = CollectionType.construct(Set.class, null);
            fail("Expected DeprecationException");
        } catch (DeprecationException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(List.class, null, String.class, null);
        JavaType subListType = CollectionType.construct(ArrayList.class, null, String.class, null);
        JavaType narrowedListType = listType._narrow(ArrayList.class);
        assertNotNull(narrowedListType);
        assertEquals(ArrayList.class, narrowedListType.getRawType());
        assertEquals(String.class, narrowedListType.getComponentType());
        assertEquals(false, narrowedListType._asStatic);
        assertEquals(listType, narrowedListType.getBaseType());
        assertNotSame(listType, narrowedListType);

        JavaType subSetType = CollectionType.construct(HashSet.class, null, Integer.class, null);
        JavaType narrowedSetType = listType._narrow(HashSet.class);
        assertNull(narrowedSetType);
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(List.class, null, String.class, null);
        JavaType stringArrayType = JavaType.constructArrayType(String[].class);
        JavaType withContentTypeListType = listType.withContentType(stringArrayType);
        assertNotNull(withContentTypeListType);
        assertEquals(List.class, withContentTypeListType.getRawType());
        assertEquals(String[].class, withContentTypeListType.getComponentType());
        assertEquals(false, withContentTypeListType._asStatic);
        assertEquals(stringArrayType, withContentTypeListType.getContentType());
        assertNotSame(listType, withContentTypeListType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, String.class, null);
        Object handler = new Object() {};
        JavaType withTypeHandlerListType = listType.withTypeHandler(handler);
        assertNotNull(withTypeHandlerListType);
        assertEquals(List.class, withTypeHandlerListType.getRawType());
        assertEquals(String.class, withTypeHandlerListType.getComponentType());
        assertEquals(false, withTypeHandlerListType._asStatic);
        assertEquals(handler, withTypeHandlerListType.getTypeHandler());
        assertNotSame(listType, withTypeHandlerListType);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, String.class, null);
        Object handler = new Object() {};
        JavaType withContentTypeHandlerListType = listType.withContentTypeHandler(handler);
        assertNotNull(withContentTypeHandlerListType);
        assertEquals(List.class, withContentTypeHandlerListType.getRawType());
        assertEquals(String.class, withContentTypeHandlerListType.getComponentType());
        assertEquals(false, withContentTypeHandlerListType._asStatic);
        assertEquals(handler, withContentTypeHandlerListType.getContentTypeHandler());
        assertNotSame(listType, withContentTypeHandlerListType);
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, String.class, null);
        Object handler = new Object() {};
        JavaType withValueHandlerListType = listType.withValueHandler(handler);
        assertNotNull(withValueHandlerListType);
        assertEquals(List.class, withValueHandlerListType.getRawType());
        assertEquals(String.class, withValueHandlerListType.getComponentType());
        assertEquals(false, withValueHandlerListType._asStatic);
        assertEquals(handler, withValueHandlerListType.getValueHandler());
        assertNotSame(listType, withValueHandlerListType);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, String.class, null);
        Object handler = new Object() {};
        JavaType withContentValueHandlerListType = listType.withContentValueHandler(handler);
        assertNotNull(withContentValueHandlerListType);
        assertEquals(List.class, withContentValueHandlerListType.getRawType());
        assertEquals(String.class, withContentValueHandlerListType.getComponentType());
        assertEquals(false, withContentValueHandlerListType._asStatic);
        assertEquals(handler, withContentValueHandlerListType.getContentValueHandler());
        assertNotSame(listType, withContentValueHandlerListType);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(List.class, null, String.class, null);
        JavaType staticListType = listType.withStaticTyping();
        assertNotNull(staticListType);
        assertEquals(List.class, staticListType.getRawType());
        assertEquals(String.class, staticListType.getComponentType());
        assertEquals(true, staticListType._asStatic);
        assertSame(listType.getComponentType(), staticListType.getComponentType());
        assertEquals(listType, staticListType.getBaseType());
        assertNotSame(listType, staticListType);
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(List.class, null, String.class, null);
        JavaType arrayListType = CollectionType.construct(ArrayList.class, null, String.class, null);
        JavaType refinedListType = listType.refine(ArrayList.class, null, arrayListType.getType(), null);
        assertNotNull(refinedListType);
        assertEquals(ArrayList.class, refinedListType.getRawType());
        assertEquals(String.class, refinedListType.getComponentType());
        assertEquals(false, refinedListType._asStatic);
        assertEquals(arrayListType.getTypeBindings(), refinedListType._bindings);
        assertSame(listType.getComponentType(), refinedListType.getComponentType());
        assertEquals(listType, refinedListType.getBaseType());
        assertNotSame(listType, refinedListType);
    }
}