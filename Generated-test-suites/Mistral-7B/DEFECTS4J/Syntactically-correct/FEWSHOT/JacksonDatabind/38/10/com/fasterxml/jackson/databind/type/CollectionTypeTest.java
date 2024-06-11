package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, null);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getComponentType());
        assertNull(collectionType.getTypeParameters());
        assertNull(collectionType.getRawType());
        assertNull(collectionType.getTypeBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructWithTypeBindings() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType elemT = Object.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, null, null, elemT);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getComponentType());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertNull(collectionType.getTypeParameters());
        assertNull(collectionType.getRawType());
        assertNull(collectionType.getTypeBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructWithSuperClassAndSuperInterfaces() {
        Class<?> rawType = ArrayList.class;
        JavaType superClass = List.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Object.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, superClass, superInterfaces, elemT);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getComponentType());
        assertNull(collectionType.getTypeParameters());
        assertNull(collectionType.getRawType());
        assertNull(collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertArrayEquals(superInterfaces, collectionType.getSuperInterfaces());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructWithTypeParameters() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType elemT = Integer.class;
        Type[] typeParameters = {String[].class};
        CollectionType collectionType = CollectionType.construct(rawType, bindings, null, null, elemT, null, null, false, typeParameters);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getComponentType());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(typeParameters, collectionType.getTypeParameters());
        assertNull(collectionType.getRawType());
        assertNull(collectionType.getTypeBindings());
        assertNull(collectionType.getSuperClass());
        assertArrayEquals(superInterfaces, collectionType.getSuperInterfaces());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructWithBindingAndElementType() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType elemT = Integer.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, null, null, elemT);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getComponentType());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertNull(collectionType.getTypeParameters());
        assertNull(collectionType.getRawType());
        assertNull(collectionType.getTypeBindings());
        assertNull(collectionType.getSuperClass());
        assertArrayEquals(new JavaType[0], collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getBounds()[0]);
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, null);
        CollectionType narrowedType = collectionType.narrow(LinkedList.class);
        assertEquals(List.class, narrowedType.getRawClass());
        assertEquals(LinkedList.class, narrowedType.getComponentType());
        assertNull(narrowedType.getTypeParameters());
        assertNull(narrowedType.getRawType());
        assertNull(narrowedType.getTypeBindings());
        assertNull(narrowedType.getSuperClass());
        assertArrayEquals(new JavaType[0], narrowedType.getSuperInterfaces());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, null);
        JavaType contentType = List.class;
        CollectionType withContentType = collectionType.withContentType(contentType);
        assertEquals(List.class, withContentType.getRawClass());
        assertEquals(ArrayList.class, withContentType.getComponentType());
        assertNull(withContentType.getTypeParameters());
        assertNull(withContentType.getRawType());
        assertNull(withContentType.getTypeBindings());
        assertEquals(contentType, withContentType.getSuperClass());
        assertArrayEquals(new JavaType[0], withContentType.getSuperInterfaces());
        assertNull(withContentType.getValueHandler());
        assertNull(withContentType.getTypeHandler());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, null);
        Object typeHandler = new Object();
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertEquals(List.class, withTypeHandler.getRawClass());
        assertEquals(ArrayList.class, withTypeHandler.getComponentType());
        assertNull(withTypeHandler.getTypeParameters());
        assertNull(withTypeHandler.getRawType());
        assertNull(withTypeHandler.getTypeBindings());
        assertNull(withTypeHandler.getSuperClass());
        assertArrayEquals(new JavaType[0], withTypeHandler.getSuperInterfaces());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertNull(withTypeHandler.getValueHandler());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, null);
        Object contentTypeHandler = new Object();
        Object typeHandler = new Object();
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler).withTypeHandler(typeHandler);
        assertEquals(List.class, withContentTypeHandler.getRawClass());
        assertEquals(ArrayList.class, withContentTypeHandler.getComponentType());
        assertNull(withContentTypeHandler.getTypeParameters());
        assertNull(withContentTypeHandler.getRawType());
        assertNull(withContentTypeHandler.getTypeBindings());
        assertNull(withContentTypeHandler.getSuperClass());
        assertArrayEquals(new JavaType[0], withContentTypeHandler.getSuperInterfaces());
        assertEquals(contentTypeHandler, withContentTypeHandler.getTypeHandler());
        assertEquals(typeHandler, withContentTypeHandler.getValueHandler());
        assertFalse(withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, null);
        Object valueHandler = new Object();
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertEquals(List.class, withValueHandler.getRawClass());
        assertEquals(ArrayList.class, withValueHandler.getComponentType());
        assertNull(withValueHandler.getTypeParameters());
        assertNull(withValueHandler.getRawType());
        assertNull(withValueHandler.getTypeBindings());
        assertNull(withValueHandler.getSuperClass());
        assertArrayEquals(new JavaType[0], withValueHandler.getSuperInterfaces());
        assertNull(withValueHandler.getTypeHandler());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, null);
        Object contentValueHandler = new Object();
        Object valueHandler = new Object();
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler).withValueHandler(valueHandler);
        assertEquals(List.class, withContentValueHandler.getRawClass());
        assertEquals(ArrayList.class, withContentValueHandler.getComponentType());
        assertNull(withContentValueHandler.getTypeParameters());
        assertNull(withContentValueHandler.getRawType());
        assertNull(withContentValueHandler.getTypeBindings());
        assertNull(withContentValueHandler.getSuperClass());
        assertArrayEquals(new JavaType[0], withContentValueHandler.getSuperInterfaces());
        assertEquals(contentValueHandler, withContentValueHandler.getValueHandler());
        assertEquals(valueHandler, withContentValueHandler.getTypeHandler());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, null);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertEquals(List.class, withStaticTyping.getRawClass());
        assertEquals(ArrayList.class, withStaticTyping.getComponentType());
        assertNull(withStaticTyping.getTypeParameters());
        assertNull(withStaticTyping.getRawType());
        assertNull(withStaticTyping.getTypeBindings());
        assertNull(withStaticTyping.getSuperClass());
        assertArrayEquals(new JavaType[0], withStaticTyping.getSuperInterfaces());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType elemT = Integer.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, null, null, elemT);
        Type rawType1 = LinkedList.class;
        JavaType[] superInterfaces = new JavaType[] {LinkedList.class};
        CollectionType refinedType = collectionType.refine(rawType1, bindings, null, null);
        assertEquals(List.class, refinedType.getRawClass());
        assertEquals(LinkedList.class, refinedType.getComponentType());
        assertEquals(bindings, refinedType.getTypeBindings());
        assertNull(refinedType.getTypeParameters());
        assertEquals(LinkedList.class, refinedType.getRawType());
        assertNull(refinedType.getTypeBindings());
        assertNull(refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getSuperInterfaces());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }
}