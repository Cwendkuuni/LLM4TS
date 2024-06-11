package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {
    
    private SimpleType simpleType;
    private Class<?> cls;
    private TypeBindings bindings;
    private JavaType superClass;
    private JavaType[] superInterfaces;
    private Object valueHandler;
    private Object typeHandler;
    private boolean asStatic;

    @Before
    public void setUp() {
        cls = Integer.class;
        bindings = TypeBindings.emptyBindings();
        superClass = null;
        superInterfaces = null;
        valueHandler = null;
        typeHandler = null;
        asStatic = false;
        
        simpleType = new SimpleType(cls, bindings, superClass, 
                                    superInterfaces, valueHandler, 
                                    typeHandler, asStatic);
    }

    @Test
    public void testConstructor() {
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInterfaces, simpleType.getInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStatic());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType copy = new SimpleType(simpleType);
        assertEquals(cls, copy.getRawClass());
        assertEquals(bindings, copy.getBindings());
        assertEquals(superClass, copy.getSuperClass());
        assertEquals(superInterfaces, copy.getInterfaces());
        assertEquals(valueHandler, copy.getValueHandler());
        assertEquals(typeHandler, copy.getTypeHandler());
        assertEquals(asStatic, copy.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType unsafe = SimpleType.constructUnsafe(cls);
        assertEquals(cls, unsafe.getRawClass());
        assertNull(unsafe.getBindings());
        assertNull(unsafe.getSuperClass());
        assertNull(unsafe.getInterfaces());
        assertNull(unsafe.getValueHandler());
        assertNull(unsafe.getTypeHandler());
    }

    @Test
    public void testConstruct() {
        SimpleType constructed = SimpleType.construct(cls);
        assertEquals(cls, constructed.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        Class<?> mapClass = Map.class;
        SimpleType.construct(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        Class<?> collectionClass = Collection.class;
        SimpleType.construct(collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        Class<?> arrayClass = cls.getComponentType();
        SimpleType.construct(arrayClass);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Number.class;
        SimpleType narrowedType = simpleType._narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(bindings, narrowedType.getBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertEquals(superInterfaces, narrowedType.getInterfaces());
        assertEquals(valueHandler, narrowedType.getValueHandler());
        assertEquals(typeHandler, narrowedType.getTypeHandler());
        assertEquals(asStatic, narrowedType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = TypeFactory.unknownType();
        SimpleType withContent = simpleType.withContentType(contentType);
        assertEquals(simpleType, withContent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        Object contentHandler = new Object();
        simpleType.withContentTypeHandler(contentHandler);
    }

    @Test
    public void testWithValueHandler() {
        Object newValueHandler = new Object();
        SimpleType withValue = simpleType.withValueHandler(newValueHandler);
        assertEquals(simpleType, withValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        simpleType.withContentValueHandler(contentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = cls;
        TypeBindings newBindings = null;
        JavaType newSuperClass = null;
        JavaType[] newInterfaces = null;
        JavaType refinedType = simpleType.refine(rawType, newBindings,
                                                 newSuperClass, newInterfaces);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        assertEquals(cls.getName(), erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        assertEquals(cls.getName() + ';', genericSignature.toString());
    }

    @Test
    public void testToString() {
        String expected = "[simple type, class " + cls.getName() + "]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEqualsWithEqualObjects() {
        SimpleType other = new SimpleType(cls, bindings, superClass, 
                                          superInterfaces, valueHandler, 
                                          typeHandler, asStatic);
        assertTrue(simpleType.equals(other));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        Class<?> differentClass = Long.class;
        SimpleType other = new SimpleType(differentClass, bindings, 
                                          superClass, superInterfaces, 
                                          valueHandler, typeHandler, asStatic);
        assertFalse(simpleType.equals(other));
    }
}