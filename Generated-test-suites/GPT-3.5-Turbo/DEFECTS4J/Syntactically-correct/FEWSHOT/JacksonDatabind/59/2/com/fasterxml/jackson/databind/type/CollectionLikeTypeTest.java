import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.ibatis.mapping.ParameterMap;
import org.junit.Test;

public class CollectionLikeTypeTest {
    
    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertEquals(type.getContentType(), null);
        assertFalse(type.isContainerType());
        assertFalse(type.isCollectionLikeType());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, baseType);
        assertEquals(type.getContentType(), baseType);
        assertFalse(type.isContainerType());
        assertFalse(type.isCollectionLikeType());
    }
    
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertFalse(type.isTrueCollectionType());
    }
    
    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, null, null, null, null);
        CollectionLikeType type2 = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertEquals(type1, type2);
    }
    
    @Test
    public void testToString() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertEquals(type.toString(), "[collection-like type; class java.util.Collection, contains null]");
    }
}