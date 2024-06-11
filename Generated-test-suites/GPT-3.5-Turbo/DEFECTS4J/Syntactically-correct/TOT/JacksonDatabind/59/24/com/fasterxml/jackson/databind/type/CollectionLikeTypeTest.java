package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CollectionLikeTypeTest {

    private CollectionLikeType collectionLikeType;

    @Before
    public void setUp() {
        collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
    }
    
    @Test
    public void testConstructor_WithParameters_ShouldSetFieldsCorrectly() {
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getContentType().getRawClass());
    }
    
    @Test
    public void testConstruct_WithRawTypeAndElementType_ShouldCreateNewCollectionLikeType() {
        CollectionLikeType newCollectionLikeType = CollectionLikeType.construct(Collection.class, SimpleType.construct(Integer.class));
        
        assertEquals(Collection.class, newCollectionLikeType.getRawClass());
        assertEquals(Integer.class, newCollectionLikeType.getContentType().getRawClass());
    }
    
    @Test
    public void testConstruct_WithTypeArguments_ShouldCreateNewCollectionLikeType() {
        CollectionLikeType newCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.create(Collection.class, SimpleType.construct(Integer.class)), null, null, SimpleType.construct(Integer.class));
        
        assertEquals(Collection.class, newCollectionLikeType.getRawClass());
        assertEquals(Integer.class, newCollectionLikeType.getContentType().getRawClass());
    }
    
    @Test
    public void testConstructor_WithoutTypeBindings_ShouldCreateCollectionLikeTypeWithEmptyTypeBindings() {
        TypeBindings expectedTypeBindings = TypeBindings.emptyBindings();
        
        assertEquals(expectedTypeBindings, collectionLikeType.getBindings());
    }
    
    @Test
    public void testConstructor_WithTypeBindings_ShouldCreateCollectionLikeTypeWithTypeBindings() {
        TypeBindings typeBindings = TypeBindings.create(Collection.class, SimpleType.construct(String.class));
        collectionLikeType = CollectionLikeType.construct(Collection.class, typeBindings, null, null, SimpleType.construct(String.class));

        assertEquals(typeBindings, collectionLikeType.getBindings());
    }
    
    @Test
    public void testConstruct_WithSuperInterfacesAndActualTypeBindings_ShouldCreateNewType() {
        JavaType[] superInterfaces = { SimpleType.construct(Number.class) };
        
        CollectionLikeType newCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.create(Collection.class, SimpleType.construct(Integer.class)), null, superInterfaces, SimpleType.construct(Integer.class));
        
        assertEquals(Collection.class, newCollectionLikeType.getRawClass());
        assertEquals(Integer.class, newCollectionLikeType.getContentType().getRawClass());

    }
    
    @Test
    public void testEquals_TwoCollectionLikeTypesWithTheSameRawClassAndElementType_ShouldReturnTrue() {
        CollectionLikeType other = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(String.class));
        
        assertTrue(collectionLikeType.equals(other));
    }
    
    @Test
    public void testToString_ShouldReturnExpectedString() {
        String expectedString = "[collection-like type; class java.util.Collection, contains java.lang.String]";
        
        assertEquals(expectedString, collectionLikeType.toString());
    }

}