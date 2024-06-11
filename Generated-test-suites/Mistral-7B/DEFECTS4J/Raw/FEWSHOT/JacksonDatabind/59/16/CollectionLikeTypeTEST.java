package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), _bogusSuperClass(Collection.class), null, JavaType.of(String.class), null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        CollectionLikeType nonCollectionLikeType = new CollectionLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, JavaType.of(String.class), null, null, false);
        assertFalse(nonCollectionLikeType.isContainerType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        CollectionLikeType nonCollectionType = new CollectionLikeType(HashMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null, JavaType.of(String.class), null, null, false);
        assertFalse(nonCollectionType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        JavaType expectedContentType = JavaType.of(String.class);
        JavaType actualContentType = collectionLikeType.getContentType();
        assertEquals(expectedContentType, actualContentType);
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        Object expectedContentValueHandler = JavaType.of(String.class).getValueHandler();
        Object actualContentValueHandler = collectionLikeType.getContentValueHandler();
        assertEquals(expectedContentValueHandler, actualContentValueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        Object expectedContentTypeHandler = JavaType.of(String.class).getTypeHandler();
        Object actualContentTypeHandler = collectionLikeType.getContentTypeHandler();
        assertEquals(expectedContentTypeHandler, actualContentTypeHandler);
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        String expectedCanonicalName = "[Ljava.util.ArrayList;";
        String actualCanonicalName = collectionLikeType.buildCanonicalName();
        assertEquals(expectedCanonicalName, actualCanonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        StringBuilder expectedErasedSignature = new StringBuilder();
        expectedErasedSignature.append("[L");
        expectedErasedSignature.append(ArrayList.class.getName());
        expectedErasedSignature.append(';');
        StringBuilder actualErasedSignature = new StringBuilder();
        collectionLikeType.getErasedSignature(actualErasedSignature);
        assertEquals(expectedErasedSignature.toString(), actualErasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        StringBuilder expectedGenericSignature = new StringBuilder();
        expectedGenericSignature.append("[");
        expectedGenericSignature.append("java.util.ArrayList<");
        expectedGenericSignature.append("Ljava/lang/String;");
        expectedGenericSignature.append(">");
        expectedGenericSignature.append(']');
        StringBuilder actualGenericSignature = new StringBuilder();
        collectionLikeType.getGenericSignature(actualGenericSignature);
        assertEquals(expectedGenericSignature.toString(), actualGenericSignature.toString());
    }

    @Test
    public void testEquals() {
        CollectionLikeType firstCollectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        CollectionLikeType secondCollectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        CollectionLikeType thirdCollectionLikeType = new CollectionLikeType(HashMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null, JavaType.of(String.class), null, null, false);

        assertTrue(firstCollectionLikeType.equals(firstCollectionLikeType));
        assertTrue(firstCollectionLikeType.equals(secondCollectionLikeType));
        assertFalse(firstCollectionLikeType.equals(thirdCollectionLikeType));
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        String expectedToString = "[collection-like type; class java.util.ArrayList, contains java.lang.String]";
        String actualToString = collectionLikeType.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testConstruct() {
        JavaType stringJavaType = JavaType.of(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, stringJavaType);
        JavaType expectedElementType = JavaType.of(String.class);
        JavaType actualElementType = collectionLikeType.getElementType();
        assertEquals(expectedElementType, actualElementType);
    }

    @Test
    public void testConstructWithTypeBindings() {
        JavaType stringJavaType = JavaType.of(String.class);
        JavaType collectionClassJavaType = JavaType.of(ArrayList.class);
        TypeVariable<?>[] typeVariables = ArrayList.class.getTypeParameters();
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, stringJavaType);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, typeBindings, _bogusSuperClass(ArrayList.class), null, stringJavaType);
        JavaType expectedElementType = JavaType.of(String.class);
        JavaType actualElementType = collectionLikeType.getElementType();
        assertEquals(expectedElementType, actualElementType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType stringJavaType = JavaType.of(String.class);
        JavaType listJavaType = JavaType.constructParametricType(ArrayList.class, ArrayList.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(listJavaType, stringJavaType);
        JavaType expectedElementType = JavaType.of(String.class);
        JavaType actualElementType = collectionLikeType.getElementType();
        assertEquals(expectedElementType, actualElementType);
    }

    @Test
    public void testRefine() {
        JavaType stringJavaType = JavaType.of(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, stringJavaType, null, null, false);
        JavaType expectedRefinedType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, stringJavaType, null, null, true);
        JavaType actualRefinedType = collectionLikeType.refine(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null);
        assertEquals(expectedRefinedType, actualRefinedType);
    }

    @Test
    public void testNarrow() {
        JavaType stringJavaType = JavaType.of(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, stringJavaType, null, null, false);
        JavaType expectedNarrowedType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, stringJavaType, null, null, true);
        JavaType actualNarrowedType = collectionLikeType.narrow(ArrayList.class);
        assertEquals(expectedNarrowedType, actualNarrowedType);
    }

    @Test
    public void testWithContentType() {
        JavaType originalElementType = JavaType.of(Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, originalElementType, null, null, false);
        JavaType expectedWithContentType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        JavaType actualWithContentType = collectionLikeType.withContentType(JavaType.of(String.class));
        assertEquals(expectedWithContentType, actualWithContentType);
    }

    @Test
    public void testWithTypeHandler() {
        Object stringValueHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        JavaType expectedWithTypeHandler = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), stringValueHandler, null, false);
        JavaType actualWithTypeHandler = collectionLikeType.withTypeHandler(stringValueHandler);
        assertEquals(expectedWithTypeHandler, actualWithTypeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object stringValueHandler = new Object();
        Object stringTypeHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        JavaType expectedWithContentTypeHandler = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, stringTypeHandler, false);
        JavaType actualWithContentTypeHandler = collectionLikeType.withContentTypeHandler(stringTypeHandler);
        assertEquals(expectedWithContentTypeHandler, actualWithContentTypeHandler);
    }

    @Test
    public void testWithValueHandler() {
        Object stringValueHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        JavaType expectedWithValueHandler = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), stringValueHandler, null, false);
        JavaType actualWithValueHandler = collectionLikeType.withValueHandler(stringValueHandler);
        assertEquals(expectedWithValueHandler, actualWithValueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object stringValueHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        JavaType expectedWithContentValueHandler = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, stringValueHandler, false);
        JavaType actualWithContentValueHandler = collectionLikeType.withContentValueHandler(stringValueHandler);
        assertEquals(expectedWithContentValueHandler, actualWithContentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, false);
        JavaType expectedWithStaticTyping = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.of(String.class), null, null, true);
        JavaType actualWithStaticTyping = collectionLikeType.withStaticTyping();
        assertEquals(expectedWithStaticTyping, actualWithStaticTyping);
    }
}