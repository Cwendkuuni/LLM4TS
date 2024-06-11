import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionLikeType result = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(result);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType elementType = null;
        CollectionLikeType result = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        JavaType result = CollectionLikeTypeTest._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        JavaType result = CollectionLikeType.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionLikeType result = CollectionLikeType.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionLikeType result = CollectionLikeType.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionLikeType result = CollectionLikeType.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionLikeType result = CollectionLikeType.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType result = CollectionLikeType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = null;
        JavaType result = CollectionLikeType.refine(rawType, bindings, superClass, superInterfaces, elemT);
        assertNotNull(result);
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType instance = new CollectionLikeType();
        boolean expResult = false;
        boolean result = instance.isTrueCollectionType();
        assertEquals(expResult, result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        CollectionLikeType instance = new CollectionLikeType();
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
    }

    @Test
    public void testToString() {
        CollectionLikeType instance = new CollectionLikeType();
        String expResult = "[collection-like type; class com.fasterxml.jackson.databind.type.CollectionLikeType, contains null]";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}