import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class CollectionLikeTypeTest {

    // Test case for construct()
    @Test
    public void testConstruct() {
        JavaType elementType = new JavaType();
        CollectionLikeType result = CollectionLikeType.construct(Collection.class, elementType);
        
        Assert.assertEquals(Collection.class, result.getRawClass());
        Assert.assertEquals(elementType, result.getContentType());
    }

    // Test case for upgradeFrom()
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType();
        CollectionLikeType result = CollectionLikeType.upgradeFrom(baseType, elementType);
        
        Assert.assertEquals(elementType, result.getContentType());
        Assert.assertTrue(result.isTrueCollectionType());
    }

    // Test case for isTrueCollectionType()
    @Test
    public void testIsTrueCollectionType() {
        JavaType elementType = new JavaType();
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elementType);
        
        Assert.assertTrue(collectionLikeType.isTrueCollectionType());
    }

    // Test case for equals()
    @Test
    public void testEquals() {
        JavaType elementType1 = new JavaType();
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(Collection.class, elementType1);
        JavaType elementType2 = new JavaType();
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(Collection.class, elementType2);
        
        Assert.assertEquals(collectionLikeType1, collectionLikeType2);
    }

    // Test case for toString()
    @Test
    public void testToString() {
        JavaType elementType = new JavaType();
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elementType);
        
        Assert.assertEquals("[collection-like type; class java.util.Collection, contains " + elementType + "]", collectionLikeType.toString());
    }

    
}