import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        JavaType elemT = new JavaType(Integer.class);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = new JavaType[0];
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(collectionLikeType);
    }
    
    @Test
    public void testConstructWithNullElemT() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = new JavaType[0];
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, null);
        assertNotNull(collectionLikeType);
    }
    
    @Test
    public void testConstructWithNullRawType() {
        JavaType elemT = new JavaType(Integer.class);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInts = new JavaType[0];
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(null, bindings, superClass, superInts, elemT);
        assertNotNull(collectionLikeType);
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(collectionLikeType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(elementType, baseType);
    }
    
    @Test
    public void testIsTrueCollectionType() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        boolean isTrueCollectionType = collectionLikeType.isTrueCollectionType();
        assertFalse(isTrueCollectionType);
    }
    
    @Test
    public void testIsTrueCollectionTypeWithTrueCollectionType() {
        JavaType elementType = new JavaType(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(new JavaType(Collection.class), elementType);
        boolean isTrueCollectionType = collectionLikeType.isTrueCollectionType();
        assertTrue(isTrueCollectionType);
    }
    
    @Test
    public void testEquals() {
        CollectionLikeType collectionLikeType1 = CollectionLikeType.upgradeFrom(new JavaType(Collection.class), new JavaType(Integer.class));
        CollectionLikeType collectionLikeType2 = CollectionLikeType.upgradeFrom(new JavaType(Collection.class), new JavaType(Integer.class));
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        CollectionLikeType collectionLikeType1 = CollectionLikeType.upgradeFrom(new JavaType(Collection.class), new JavaType(Integer.class));
        CollectionLikeType collectionLikeType2 = CollectionLikeType.upgradeFrom(new JavaType(List.class), new JavaType(Integer.class));
        assertFalse(collectionLikeType1.equals(collectionLikeType2));
    }
    
    @Test
    public void testEqualsWithDifferentElementType() {
        CollectionLikeType collectionLikeType1 = CollectionLikeType.upgradeFrom(new JavaType(Collection.class), new JavaType(Integer.class));
        CollectionLikeType collectionLikeType2 = CollectionLikeType.upgradeFrom(new JavaType(Collection.class), new JavaType(String.class));
        assertFalse(collectionLikeType1.equals(collectionLikeType2));
    }
    
    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(new JavaType(Collection.class), new JavaType(Integer.class));
        String str = collectionLikeType.toString();
        assertEquals("[collection-like type; class java.util.Collection, contains class java.lang.Integer]", str);
    }
}