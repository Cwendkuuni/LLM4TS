package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CollectionLikeTypeTest {

    private TypeFactory typeFactory;
    private JavaType stringType;
    private JavaType intType;
    private JavaType listType;
    private JavaType mapType;
    private JavaType collectionLikeType;
    private JavaType arrayType;

    @Before
    public void setUp() {
        typeFactory = Mockito.spy(new TypeFactory());
        stringType = typeFactory.constructType(String.class);
        intType = typeFactory.constructType(int.class);
        listType = TypeFactory.defaultInstance().constructCollectionType(List.class, intType);
        mapType = TypeFactory.defaultInstance().constructType(HashMap.class, intType, String.class);
        arrayType = TypeFactory.defaultInstance().constructType(int[].class);

        typeFactory.findTypeParams(ArrayList.class);
        assertNotNull(typeFactory._typeBindings);

        collectionLikeType = CollectionLikeType.construct(ArrayList.class, typeFactory._typeBindings, null, null, intType);
    }

    @Test
    public void testCollectionLikeType() {
        assertEquals(ArrayList.class, collectionLikeType._class);
        assertEquals(intType, collectionLikeType._elementType);
        assertFalse(collectionLikeType.isTrueCollectionType());
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType upgraded = CollectionLikeType.upgradeFrom(intType.getClass(), intType);
        assertEquals(CollectionLikeType.class, upgraded.getClass());
        assertEquals(intType, ((CollectionLikeType) upgraded)._elementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType upgraded = CollectionLikeType.upgradeFrom(String.class, intType);
    }

    @Test
    public void testConstruct() {
        JavaType arrayList = CollectionLikeType.construct(ArrayList.class, typeFactory._typeBindings, null, null, intType);
        assertEquals(ArrayList.class, arrayList._class);
        assertEquals(intType, arrayList._elementType);
    }

    @Test
    public void testConstructWithEmptyTypeBindings() {
        JavaType arrayList = CollectionLikeType.construct(ArrayList.class, null, null, null, intType);
        assertEquals(ArrayList.class, arrayList._class);
        assertEquals(intType, arrayList._elementType);
    }

    @Test
    public void testConstructWithOneTypeBinding() {
        JavaType arrayList = CollectionLikeType.construct(ArrayList.class, typeFactory._typeBindings, null, null, intType, intType);
        assertEquals(ArrayList.class, arrayList._class);
        assertEquals(intType, arrayList._elementType);
    }

    @Test
    public void testConstructWithTypeVariable() {
        JavaType arrayList = CollectionLikeType.construct(ArrayList.class, typeFactory._typeBindings, null, null, intType, TypeVariable.class);
        assertEquals(ArrayList.class, arrayList._class);
        assertNull(arrayList._elementType);
    }

    @Test
    public void testConstructWithTypeVariableAndCustomHandler() {
        TypeDeserializer dummyDeserializer = Mockito.mock(TypeDeserializer.class);
        TypeSerializer dummySerializer = Mockito.mock(TypeSerializer.class);

        JavaType arrayList = CollectionLikeType.construct(ArrayList.class, typeFactory._typeBindings, null, null, intType, TypeVariable.class, dummyDeserializer, dummySerializer);
        assertEquals(ArrayList.class, arrayList._class);
        assertNull(arrayList._elementType);
    }

    @Test
    public void testNarrow() {
        JavaType list = CollectionLikeType.construct(LinkedHashSet.class, null, null, null, intType);
        JavaType narrowed = collectionLikeType._narrow(LinkedHashSet.class);
        assertNotSame(collectionLikeType, narrowed);
        assertEquals(LinkedHashSet.class, narrowed._class);
        assertEquals(intType, narrowed._elementType);
    }

    @Test
    public void testRefine() {
        JavaType refined = collectionLikeType.refine(ArrayList.class, null, null, null);
        assertNotSame(collectionLikeType, refined);
        assertEquals(ArrayList.class, refined._class);
        assertEquals(intType, refined._elementType);
    }

    @Test
    public void testGetContentType() {
        assertEquals(intType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentTypeHandler() {
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testGetContentTypeHandlerWithCustomHandler() {
        TypeDeserializer dummyDeserializer = Mockito.mock(TypeDeserializer.class);
        TypeSerializer dummySerializer = Mockito.mock(TypeSerializer.class);
        JavaType collectionLikeTypeWithCustomHandler = new CollectionLikeType(collectionLikeType._class, collectionLikeType._bindings, collectionLikeType._superClass, collectionLikeType._superInterfaces, collectionLikeType._elementType, dummyDeserializer, dummySerializer, collectionLikeType._asStatic);
        assertEquals(dummyDeserializer, collectionLikeTypeWithCustomHandler.getContentTypeHandler());
    }

    @Test
    public void testGetValueHandler() {
        assertNull(collectionLikeType.getValueHandler());
    }

    @Test
    public void testGetValueHandlerWithCustomHandler() {
        Object dummyHandler = Mockito.mock(Object.class);
        JavaType collectionLikeTypeWithCustomHandler = new CollectionLikeType(collectionLikeType._class, collectionLikeType._bindings, collectionLikeType._superClass, collectionLikeType._superInterfaces, collectionLikeType._elementType, dummyHandler, null, collectionLikeType._asStatic);
        assertEquals(dummyHandler, collectionLikeTypeWithCustomHandler.getValueHandler());
    }

    @Test
    public void testIsContainerType() {
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testHasHandlers() {
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder expected = new StringBuilder("ArrayList<T>");
        StringBuilder actual = collectionLikeType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder expected = new StringBuilder("[");
        expected.append(intType.getGenericSignature(new StringBuilder()));
        expected.append("]");
        StringBuilder actual = collectionLikeType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        assertEquals("java.util.ArrayList", collectionLikeType.buildCanonicalName());
    }

    @Test
    public void testIsTrueCollectionType() {
        assertFalse(collectionLikeType.isTrueCollectionType());
    }
}