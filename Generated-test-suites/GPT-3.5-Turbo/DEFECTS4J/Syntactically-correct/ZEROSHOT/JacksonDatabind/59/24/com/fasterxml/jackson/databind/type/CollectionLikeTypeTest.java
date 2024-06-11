import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionLikeTypeTest {
    
    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(String.class, null, null, null, null);
        assertNotNull(type);
        assertEquals(String.class, type._elementType.getRawClass());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String.class);
        JavaType elementType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertEquals(Integer.class, type._elementType.getRawClass());
    }
    
    @Test
    public void testNarrow() {
        CollectionLikeType baseType = CollectionLikeType.construct(String.class, null, null, null, null);
        CollectionLikeType narrowedType = (CollectionLikeType)baseType._narrow(Collection.class);
        assertNotNull(narrowedType);
        assertEquals(Collection.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType._elementType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        CollectionLikeType baseType = CollectionLikeType.construct(String.class, null, null, null, null);
        JavaType contentType = new SimpleType(Integer.class);
        CollectionLikeType withContentType = (CollectionLikeType)baseType.withContentType(contentType);
        assertNotNull(withContentType);
        assertEquals(String.class, withContentType.getRawClass());
        assertEquals(Integer.class, withContentType._elementType.getRawClass());
    }
    
    @Test
    public void testWithTypeHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(String.class, null, null, null, null);
        Object typeHandler = new Object();
        CollectionLikeType withTypeHandler = (CollectionLikeType)baseType.withTypeHandler(typeHandler);
        assertNotNull(withTypeHandler);
        assertEquals(String.class, withTypeHandler.getRawClass());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(String.class, null, null, null, null);
        Object typeHandler = new Object();
        CollectionLikeType withContentTypeHandler = (CollectionLikeType)baseType.withContentTypeHandler(typeHandler);
        assertNotNull(withContentTypeHandler);
        assertEquals(String.class, withContentTypeHandler.getRawClass());
        assertEquals(typeHandler, withContentTypeHandler._elementType.getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(String.class, null, null, null, null);
        Object valueHandler = new Object();
        CollectionLikeType withValueHandler = (CollectionLikeType)baseType.withValueHandler(valueHandler);
        assertNotNull(withValueHandler);
        assertEquals(String.class, withValueHandler.getRawClass());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(String.class, null, null, null, null);
        Object valueHandler = new Object();
        CollectionLikeType withContentValueHandler = (CollectionLikeType)baseType.withContentValueHandler(valueHandler);
        assertNotNull(withContentValueHandler);
        assertEquals(String.class, withContentValueHandler.getRawClass());
        assertEquals(valueHandler, withContentValueHandler._elementType.getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionLikeType baseType = CollectionLikeType.construct(String.class, null, null, null, null);
        CollectionLikeType withStaticTyping = (CollectionLikeType)baseType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertEquals(String.class, withStaticTyping.getRawClass());
        assertTrue(withStaticTyping.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        CollectionLikeType baseType = CollectionLikeType.construct(String.class, null, null, null, null);
        CollectionLikeType refinedType = (CollectionLikeType)baseType.refine(Collection.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(Collection.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType._elementType.getRawClass());
    }
    
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertTrue(type.isTrueCollectionType());
    }
    
    @Test
    public void testIsNotTrueCollectionType() {
        CollectionLikeType type = CollectionLikeType.construct(String.class, null, null, null, null);
        assertFalse(type.isTrueCollectionType());
    }
    
    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(String.class, null, null, null, null);
        CollectionLikeType type2 = CollectionLikeType.construct(String.class, null, null, null, null);
        assertTrue(type1.equals(type2));
    }
    
    @Test
    public void testNotEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(String.class, null, null, null, null);
        CollectionLikeType type2 = CollectionLikeType.construct(Integer.class, null, null, null, null);
        assertFalse(type1.equals(type2));
    }
    
    @Test
    public void testToString() {
        CollectionLikeType type = CollectionLikeType.construct(String.class, null, null, null, null);
        assertEquals("[collection-like type; class java.lang.String, contains null]", type.toString());
    }
}