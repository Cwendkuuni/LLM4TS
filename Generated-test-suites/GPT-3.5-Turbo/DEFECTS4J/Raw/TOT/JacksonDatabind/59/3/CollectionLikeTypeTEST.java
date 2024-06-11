import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollectionLikeTypeTest {
    
    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(SimpleType.construct(String.class), type.getContentType());
    }
    
    @Test
    public void testUpgradeFrom() {
        SimpleType baseType = SimpleType.construct(ArrayList.class);
        CollectionLikeType upgradeType = CollectionLikeType.upgradeFrom(baseType, SimpleType.construct(String.class));
        assertEquals(ArrayList.class, upgradeType.getRawClass());
        assertEquals(SimpleType.construct(String.class), upgradeType.getContentType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        BaseType baseType = new BaseType();
        CollectionLikeType.upgradeFrom(baseType, SimpleType.construct(String.class));
    }
    
    @Test
    public void testNarrow() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        CollectionLikeType narrowType = (CollectionLikeType) baseType._narrow(Vector.class);
        assertEquals(Vector.class, narrowType.getRawClass());
        assertEquals(SimpleType.construct(String.class), narrowType.getContentType());
    }
    
    @Test
    public void testWithContentType() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(Integer.class));
        CollectionLikeType withContentType = (CollectionLikeType) baseType.withContentType(SimpleType.construct(String.class));
        assertEquals(ArrayList.class, withContentType.getRawClass());
        assertEquals(SimpleType.construct(String.class), withContentType.getContentType());
    }
    
    @Test
    public void testWithContentTypeSame() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(Integer.class));
        CollectionLikeType withContentType = (CollectionLikeType) baseType.withContentType(SimpleType.construct(Integer.class));
        assertEquals(ArrayList.class, withContentType.getRawClass());
        assertEquals(SimpleType.construct(Integer.class), withContentType.getContentType());
        assertTrue(withContentType == baseType);
    }
    
    @Test
    public void testWithTypeHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        CollectionLikeType withTypeHandler = (CollectionLikeType) baseType.withTypeHandler("typeHandler");
        assertEquals(ArrayList.class, withTypeHandler.getRawClass());
        assertEquals(SimpleType.construct(String.class), withTypeHandler.getContentType());
        assertEquals("typeHandler", withTypeHandler.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        CollectionLikeType withContentTypeHandler = (CollectionLikeType) baseType.withContentTypeHandler("contentTypeHandler");
        assertEquals(ArrayList.class, withContentTypeHandler.getRawClass());
        assertEquals(SimpleType.construct(String.class), withContentTypeHandler.getContentType());
        assertEquals("contentTypeHandler", withContentTypeHandler.getContentTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        CollectionLikeType withValueHandler = (CollectionLikeType) baseType.withValueHandler("valueHandler");
        assertEquals(ArrayList.class, withValueHandler.getRawClass());
        assertEquals(SimpleType.construct(String.class), withValueHandler.getContentType());
        assertEquals("valueHandler", withValueHandler.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        CollectionLikeType withContentValueHandler = (CollectionLikeType) baseType.withContentValueHandler("contentValueHandler");
        assertEquals(ArrayList.class, withContentValueHandler.getRawClass());
        assertEquals(SimpleType.construct(String.class), withContentValueHandler.getContentType());
        assertEquals("contentValueHandler", withContentValueHandler.getContentValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        CollectionLikeType withStaticTyping = (CollectionLikeType) baseType.withStaticTyping();
        assertEquals(ArrayList.class, withStaticTyping.getRawClass());
        assertEquals(SimpleType.construct(String.class), withStaticTyping.getContentType());
        assertTrue(withStaticTyping.isStaticTyping());
    }
    
    @Test
    public void testWithStaticTypingSame() {
        CollectionLikeType baseType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        CollectionLikeType withStaticTyping = (CollectionLikeType) baseType.withStaticTyping();
        assertEquals(ArrayList.class, withStaticTyping.getRawClass());
        assertEquals(SimpleType.construct(String.class), withStaticTyping.getContentType());
        assertTrue(withStaticTyping == baseType);
    }
    
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        assertTrue(collectionType.isTrueCollectionType());
        
        CollectionLikeType nonCollectionType = CollectionLikeType.construct(Object.class, TypeBindings.emptyBindings(),
                null, null, SimpleType.construct(String.class));
        assertFalse(nonCollectionType.isTrueCollectionType());
    }
    
    private static class BaseType extends SimpleType {
        
        public BaseType() {
            super(Integer.class);
        }
    }
}