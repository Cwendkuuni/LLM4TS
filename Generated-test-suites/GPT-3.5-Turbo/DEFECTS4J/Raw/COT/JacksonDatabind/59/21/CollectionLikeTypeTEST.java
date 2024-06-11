import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemT = new CollectionLikeType(Object.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, elemT);
        assertNotNull(type);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new CollectionLikeType(Collection.class);
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(type);
    }

    @Test
    public void test_narrow() {
        JavaType baseType = new CollectionLikeType(Collection.class);
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(baseType);
        JavaType narrowedType = type._narrow(Collection.class);
        assertNotNull(narrowedType);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class);
        JavaType newType = type.withContentType(contentType);
        assertNotNull(newType);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType type = new CollectionLikeType(Collection.class);
        CollectionLikeType newType = type.withStaticTyping();
        assertNotNull(newType);
    }

    @Test
    public void testGetContentType() {
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, elementType);
        JavaType contentType = type.getContentType();
        assertNotNull(contentType);
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, elementType);
        Object valueHandler = type.getContentValueHandler();
        assertNull(valueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, elementType);
        Object typeHandler = type.getContentTypeHandler();
        assertNull(typeHandler);
    }

    @Test
    public void testHasHandlers() {
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, elementType);
        boolean hasHandlers = type.hasHandlers();
        assertFalse(hasHandlers);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, elementType);
        StringBuilder signature = type.getErasedSignature(sb);
        assertNotNull(signature);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, elementType);
        StringBuilder signature = type.getGenericSignature(sb);
        assertNotNull(signature);
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, elementType);
        String canonicalName = type.buildCanonicalName();
        assertNotNull(canonicalName);
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, elementType);
        boolean isTrueCollectionType = type.isTrueCollectionType();
        assertTrue(isTrueCollectionType);
    }

    @Test
    public void testEquals() {
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type1 = new CollectionLikeType(Collection.class, elementType);
        CollectionLikeType type2 = new CollectionLikeType(Collection.class, elementType);
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testToString() {
        JavaType elementType = new CollectionLikeType(String.class);
        CollectionLikeType type = new CollectionLikeType(Collection.class, elementType);
        String str = type.toString();
        assertNotNull(str);
    }

}