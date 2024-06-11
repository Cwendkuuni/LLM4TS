package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<String>> listType = CollectionType.construct(List.class, null, null, null, String.class);
        assertNotNull(listType);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertEquals(false, listType.isStatic());

        Class<Set<Integer>> setType = CollectionType.construct(Set.class, null, null, null, Integer.class);
        assertNotNull(setType);
        assertEquals(Set.class, setType.getRawClass());
        assertEquals(Integer.class, setType.getContentType());
        assertEquals(false, setType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        try {
            CollectionType listType = CollectionType.construct(List.class, String.class);
            assertFalse("Method should be deprecated", true);
        } catch (DeprecatedException e) {
            // Expected
        }

        try {
            CollectionType setType = CollectionType.construct(Set.class, Integer.class);
            assertFalse("Method should be deprecated", true);
        } catch (DeprecatedException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        Class<ArrayList<String>> arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        Class<?> subclass = LinkedList.class;

        CollectionType originalType = new CollectionType(ArrayList.class, null, null, null, String.class);
        CollectionType narrowedType = originalType._narrow(LinkedList.class);

        assertEquals(LinkedList.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getContentType());
        assertEquals(false, narrowedType.isStatic());

        assertEquals(arrayListType, originalType);
        assertNotSame(arrayListType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        Class<List<String>> listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType stringArrayType = ArrayType.construct(String[].class);

        CollectionType newListType = listType.withContentType(stringArrayType);
        assertNotNull(newListType);
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(String[].class, newListType.getContentType());
        assertEquals(false, newListType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, String.class);
        Object handler = new Object() {
            // Empty test implementation
        };

        CollectionType newCollectionType = collectionType.withTypeHandler(handler);
        assertNotNull(newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(String.class, newCollectionType.getContentType());
        assertEquals(false, newCollectionType.isStatic());
        assertEquals(handler, newCollectionType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<String>> listType = CollectionType.construct(List.class, null, null, null, String.class);
        Object contentHandler = new Object() {
            // Empty test implementation
        };
        Object typeHandler = new Object() {
            // Empty test implementation
        };

        CollectionType newListType = listType.withContentTypeHandler(contentHandler).withTypeHandler(typeHandler);
        assertNotNull(newListType);
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(String.class, newListType.getContentType());
        assertEquals(false, newListType.isStatic());
        assertEquals(contentHandler, newListType._contentTypeHandler);
        assertEquals(typeHandler, newListType._typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, String.class);
        Object handler = new Object() {
            // Empty test implementation
        };

        CollectionType newCollectionType = collectionType.withValueHandler(handler);
        assertNotNull(newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(String.class, newCollectionType.getContentType());
        assertEquals(false, newCollectionType.isStatic());
        assertEquals(handler, newCollectionType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<String>> listType = CollectionType.construct(List.class, null, null, null, String.class);
        Object contentHandler = new Object() {
            // Empty test implementation
        };
        Object valueHandler = new Object() {
            // Empty test implementation
        };

        CollectionType newListType = listType.withContentValueHandler(contentHandler).withValueHandler(valueHandler);
        assertNotNull(newListType);
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(String.class, newListType.getContentType());
        assertEquals(false, newListType.isStatic());
        assertEquals(contentHandler, newListType._contentValueHandler);
        assertEquals(valueHandler, newListType._valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<String>> listType = CollectionType.construct(List.class, null, null, null, String.class);

        CollectionType staticListType = listType.withStaticTyping();
        assertNotNull(staticListType);
        assertEquals(List.class, staticListType.getRawClass());
        assertEquals(String.class, staticListType.getContentType());
        assertEquals(true, staticListType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<List<String>> listType
                = CollectionType.construct(List.class, null, null, null, String.class);
        Class<ArrayList<String>> arrayListType = CollectionType.construct(ArrayList.class, null, null, null, String.class);

        CollectionType newListType = listType.refine(ArrayList.class, null, null, null);
        assertNotNull(newListType);
        assertEquals(ArrayList.class, newListType.getRawClass());
        assertEquals(String.class, newListType.getContentType());
        assertEquals(false, newListType.isStatic());

        assertEquals(listType, arrayListType);
        assertNotSame(listType, newListType);
    }
}