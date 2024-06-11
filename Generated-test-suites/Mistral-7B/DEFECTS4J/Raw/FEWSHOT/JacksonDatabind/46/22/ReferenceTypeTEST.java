package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceType() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(refType.getClass(), referenceType.getContentType().getType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(1, referenceType.containedTypeCount());
        assertEquals(refType, referenceType.containedType(0));
        assertEquals("T", referenceType.containedTypeName(0));
        assertEquals(referenceType.toString(), "[reference type, class [Ljava.lang.Integer;<T>]" +
                "[Ljava.util.List;<Ljava.lang.Integer;>]");
        assertEquals(referenceType.hashCode(), 1720528401);
        assertTrue(referenceType.equals(referenceType));

        JavaType refTypeStatic = JavaType.constructCollectionType(List.class, Integer.class).withStaticTyping();
        ReferenceType referenceTypeStatic = ReferenceType.construct(Integer.class, refTypeStatic, null, null);
        assertTrue(referenceTypeStatic.isStatic());
        assertFalse(referenceTypeStatic.equals(referenceType));

        JavaType refTypeWithHandler = JavaType.constructCollectionType(List.class, Integer.class).withoutTypeHandler();
        ReferenceType referenceTypeWithHandler = ReferenceType.construct(Integer.class, refTypeWithHandler, null, null);
        assertNull(referenceTypeWithHandler.getTypeHandler());
        assertNotNull(referenceTypeWithHandler.getValueHandler());
        assertFalse(referenceTypeWithHandler.equals(referenceType));

        JavaType refTypeWithRefHandler = JavaType.constructCollectionType(List.class, Integer.class).withoutValueHandler();
        ReferenceType referenceTypeWithRefHandler = ReferenceType.construct(Integer.class, refTypeWithRefHandler, null, null);
        assertNotNull(referenceTypeWithRefHandler.getTypeHandler());
        assertNull(referenceTypeWithRefHandler.getValueHandler());
        assertFalse(referenceTypeWithRefHandler.equals(referenceType));
    }

    @Test
    public void testNarrow() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType subclassType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        ReferenceType narrowed = referenceType._narrow(subclassType.getRawClass());
        assertEquals(subclassType.getClass(), narrowed.getContentType().getType());
        assertTrue(narrowed.equals(new ReferenceType(subclassType.getRawClass(), refType, null, null)));
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        ReferenceType withTypeHandler = referenceType.withTypeHandler(handler);
        assertNotNull(withTypeHandler.getTypeHandler());
        assertFalse(withTypeHandler.equals(referenceType));
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        JavaType contentTypeWithHandler = JavaType.constructCollectionType(ArrayList.class, Integer.class).withTypeHandler(handler);
        ReferenceType withContentTypeHandler = referenceType.withContentTypeHandler(contentTypeWithHandler);
        assertNotNull(withContentTypeHandler.getReferencedType().getTypeHandler());
        assertFalse(withContentTypeHandler.equals(referenceType));
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        ReferenceType withValueHandler = referenceType.withValueHandler(handler);
        assertNotNull(withValueHandler.getValueHandler());
        assertFalse(withValueHandler.equals(referenceType));
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        JavaType contentTypeWithHandler = JavaType.constructCollectionType(ArrayList.class, Integer.class).withValueHandler(handler);
        ReferenceType withContentValueHandler = referenceType.withContentValueHandler(contentTypeWithHandler);
        assertNotNull(withContentValueHandler.getReferencedType().getValueHandler());
        assertFalse(withContentValueHandler.equals(referenceType));
    }

    @Test
    public void testStaticTyping() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class).withoutStaticTyping();
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        ReferenceType withStaticTyping = referenceType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
        assertFalse(withStaticTyping.equals(referenceType));
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testToString() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);

        assertEquals("[reference type, class Integer<T>][List<Integer>]", referenceType.toString());
    }

    @Test
    public void testHashCode() {
        JavaType refType_1 = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType refType_2 = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType_1 = ReferenceType.construct(Integer.class, refType_1, null, null);
        ReferenceType referenceType_2 = ReferenceType.construct(Integer.class, refType_2, null, null);

        assertEquals(1720528401, referenceType_1.hashCode());
        assertEquals(1720528401, referenceType_2.hashCode());
        assertFalse(referenceType_1.equals(referenceType_2));
    }
}