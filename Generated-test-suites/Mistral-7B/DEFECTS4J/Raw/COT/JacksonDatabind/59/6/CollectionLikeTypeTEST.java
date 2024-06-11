package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeModifier;

/**
 * Test suite for CollectionLikeType class.
 */
public class CollectionLikeTypeTest {

    private JavaType _intType;
    private JavaType _stringType;
    private JavaType _listType;
    private JavaType _collectionLikeType;
    private JavaType _collectionType;
    private CollectionLikeType _collectionLikeTypeInstance;

    @Before
    public void setUp() {
        _intType = TypeFactory.getDefaultInstance().constructType(int.class);
        _stringType = TypeFactory.getDefaultInstance().constructType(String.class);
        _listType = new ArrayListType(TypeFactory.getDefaultInstance().constructType(Integer.class),
                TypeBindings.emptyBindings(), null, null, _intType);
        _collectionType = TypeFactory.getDefaultInstance().constructType(Collection.class);
        _collectionLikeType = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(),
                _listType.getRawClass(), null, _intType);
        _collectionLikeTypeInstance = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(),
                _listType.getRawClass(), null, _intType);
    }

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        assertEquals(_listType.getRawClass(), _collectionLikeType.getRawClass());
        assertEquals(_intType, _collectionLikeType.getElementType());
    }

    /**
     * Test constructor with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullArgumentsConstructor() {
        CollectionLikeType.construct(null, _intType);
    }

    /**
     * Test upgradeFrom method with valid arguments.
     */
    @Test
    public void testUpgradeFrom() {
        JavaType javaType = new ArrayType(int[].class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, _intType);
        assertEquals(javaType.getComponentType(), collectionLikeType.getElementType());
    }

    /**
     * Test upgradeFrom method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentUpgradeFrom() {
        CollectionLikeType.upgradeFrom(new TypeFactory().constructType(String.class), _intType);
    }

    /**
     * Test isTrueCollectionType method.
     */
    @Test
    public void testIsTrueCollectionType() {
        assertTrue(_collectionLikeTypeInstance.isTrueCollectionType());
        assertFalse(_collectionType.isTrueCollectionType());
    }

    /**
     * Test getContentType method.
     */
    @Test
    public void testGetContentType() {
        assertEquals(_intType, _collectionLikeTypeInstance.getContentType());
    }

    /**
     * Test getContentValueHandler method.
     */
    @Test
    public void testGetContentValueHandler() {
        assertNull(_collectionLikeTypeInstance.getContentValueHandler());
    }

    /**
     * Test getContentTypeHandler method.
     */
    @Test
    public void testGetContentTypeHandler() {
        assertNull(_collectionLikeTypeInstance.getContentTypeHandler());
    }

    /**
     * Test hasHandlers method.
     */
    @Test
    public void testHasHandlers() {
        assertFalse(_collectionLikeTypeInstance.hasHandlers());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        CollectionLikeType otherCollectionLikeType = new CollectionLikeType(_collectionLikeTypeInstance._class,
                _collectionLikeTypeInstance._bindings,
                _collectionLikeTypeInstance._superClass,
                _collectionLikeTypeInstance._superInterfaces,
                _collectionLikeTypeInstance._elementType,
                _collectionLikeTypeInstance._valueHandler,
                _collectionLikeTypeInstance._typeHandler,
                _collectionLikeTypeInstance._asStatic);
        assertTrue(_collectionLikeTypeInstance.equals(otherCollectionLikeType));
    }

    /**
     * Test equals method with null argument.
     */
    @Test
    public void testEqualsNullArgument() {
        assertFalse(_collectionLikeTypeInstance.equals(null));
    }

    /**
     * Test equals method with different types.
     */
    @Test
    public void testEqualsDifferentTypes() {
        JavaType otherType = TypeFactory.getDefaultInstance().constructType(String.class);
        assertFalse(_collectionLikeTypeInstance.equals(otherType));
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String expected = "[collection-like type; class java.util.ArrayList, contains class int]";
        assertEquals(expected, _collectionLikeTypeInstance.toString());
    }
}