import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructWithTypeBindingsAndSuperTypes() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        JavaType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, String.class);
        assertEquals(javaType, collectionLikeType.getRawType());
        assertEquals(String.class, collectionLikeType.getContentType());
    }

    @Test
    public void testUpgradeFromSimpleType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
        assertEquals(javaType, collectionLikeType.getRawType());
        assertEquals(String.class, collectionLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        CollectionLikeType.upgradeFrom(javaType, String.class); // should throw an exception
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(String.class, collectionLikeType.getContentType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType1 = (CollectionLikeType) javaType1;
        CollectionLikeType collectionLikeType2 = (CollectionLikeType) javaType2;
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }

    @Test
    public void testHashCode() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType1 = (CollectionLikeType) javaType;
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType2 = (CollectionLikeType) javaType2;
        assertEquals(collectionLikeType1.hashCode(), collectionLikeType2.hashCode());
    }
}