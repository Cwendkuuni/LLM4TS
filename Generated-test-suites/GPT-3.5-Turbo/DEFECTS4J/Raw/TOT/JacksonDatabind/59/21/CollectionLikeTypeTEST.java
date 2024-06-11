import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        JavaType elemT = JavaType.forContentType(Object.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        
        assertEquals(rawType, collectionLikeType.getRawClass());
        assertEquals(elemT, collectionLikeType.getContentType());
        assertFalse(collectionLikeType.isTrueCollectionType());
        assertTrue(collectionLikeType.isContainerType());
        assertTrue(collectionLikeType.isCollectionLikeType());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.forContentType(String.class);
        JavaType elementType = JavaType.forContentType(Object.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        assertEquals(baseType.getRawClass(), collectionLikeType.getRawClass());
        assertEquals(elementType, collectionLikeType.getContentType());
        assertFalse(collectionLikeType.isTrueCollectionType());
        assertTrue(collectionLikeType.isContainerType());
        assertTrue(collectionLikeType.isCollectionLikeType());
    }
    
    @Test
    public void testWithContentType() {
        Class<?> rawType = Collection.class;
        JavaType elemT = JavaType.forContentType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        
        JavaType newElemT = JavaType.forContentType(Integer.class);
        CollectionLikeType modifiedType = (CollectionLikeType) collectionLikeType.withContentType(newElemT);
        
        assertEquals(rawType, modifiedType.getRawClass());
        assertEquals(newElemT, modifiedType.getContentType());
        assertFalse(modifiedType.isTrueCollectionType());
        assertTrue(modifiedType.isContainerType());
        assertTrue(modifiedType.isCollectionLikeType());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Collection.class;
        JavaType elemT = JavaType.forContentType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        
        Object contentTypeHandler = new Object();
        CollectionLikeType modifiedType = (CollectionLikeType) collectionLikeType.withContentTypeHandler(contentTypeHandler);
        
        assertEquals(rawType, modifiedType.getRawClass());
        assertEquals(elemT, modifiedType.getContentType());
        assertEquals(contentTypeHandler, modifiedType.getContentTypeHandler());
        assertFalse(modifiedType.isTrueCollectionType());
        assertTrue(modifiedType.isContainerType());
        assertTrue(modifiedType.isCollectionLikeType());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Collection.class;
        JavaType elemT = JavaType.forContentType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        
        Object contentValueHandler = new Object();
        CollectionLikeType modifiedType = (CollectionLikeType) collectionLikeType.withContentValueHandler(contentValueHandler);
        
        assertEquals(rawType, modifiedType.getRawClass());
        assertEquals(elemT, modifiedType.getContentType());
        assertEquals(contentValueHandler, modifiedType.getContentValueHandler());
        assertFalse(modifiedType.isTrueCollectionType());
        assertTrue(modifiedType.isContainerType());
        assertTrue(modifiedType.isCollectionLikeType());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Collection.class;
        JavaType elemT = JavaType.forContentType(Object.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        
        CollectionLikeType modifiedType = collectionLikeType.withStaticTyping();

        assertEquals(rawType, modifiedType.getRawClass());
        assertEquals(elemT.withStaticTyping(), modifiedType.getContentType());
        assertFalse(modifiedType.isTrueCollectionType());
        assertTrue(modifiedType.isContainerType());
        assertTrue(modifiedType.isCollectionLikeType());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = Collection.class;
        JavaType elemT = JavaType.forContentType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        
        Class<?> refinedRawType = List.class;
        JavaType refinedBaseType = JavaType.forContentType(Object.class);
        JavaType refinedSuperClass = JavaType.forContentType(String.class);
        JavaType[] refinedSuperInts = new JavaType[]{JavaType.forContentType(Integer.class)};
        CollectionLikeType refinedType = (CollectionLikeType) collectionLikeType.refine(refinedRawType, null, refinedSuperClass, refinedSuperInts);
        
        assertEquals(refinedRawType, refinedType.getRawClass());
        assertEquals(Object.class, refinedType.getBindings().getBoundType(0));
        assertEquals(refinedSuperClass, refinedType.getSuperClass());
        assertEquals(refinedSuperInts[0], refinedType.getInterfaces().get(0));
        assertEquals(elemT, refinedType.getContentType());
        assertFalse(refinedType.isTrueCollectionType());
        assertTrue(refinedType.isContainerType());
        assertTrue(refinedType.isCollectionLikeType());
    }
    
    @Test
    public void testIsTrueCollectionType() {
        Class<?> rawType = List.class;
        JavaType elemT = JavaType.forContentType(Object.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        
        assertTrue(collectionLikeType.isTrueCollectionType());
    }
    
    @Test
    public void testEquals() {
        Class<?> rawType1 = List.class;
        JavaType elemT = JavaType.forContentType(String.class);
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(rawType1, elemT);

        Class<?> rawType2 = Collection.class;
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(rawType2, elemT);
        
        assertTrue(collectionLikeType1.equals(collectionLikeType1));
        assertFalse(collectionLikeType1.equals(collectionLikeType2));
    }
    
    @Test
    public void testToString() {
        Class<?> rawType = List.class;
        JavaType elemT = JavaType.forContentType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        
        String expectedString = "[collection-like type; class java.util.List, contains " + elemT + "]";
        
        assertEquals(expectedString, collectionLikeType.toString());
    }

}