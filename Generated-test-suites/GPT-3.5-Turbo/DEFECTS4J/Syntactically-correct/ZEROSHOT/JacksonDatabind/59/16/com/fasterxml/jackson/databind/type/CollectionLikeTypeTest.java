import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import com.fasterxml.jackson.databind.JavaType;

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
    public void testConstruct_2() {
        Class<?> rawType = null;
        JavaType elemT = null;
        CollectionLikeType result = CollectionLikeType.construct(rawType, elemT);
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
    public void testNarrow() {
        Class<?> subclass = null;
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType result = instance._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType result = instance.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType result = instance.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType result = instance.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType result = instance.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType result = instance.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType result = instance.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = null;
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType result = instance.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        boolean result = instance.isTrueCollectionType();
        assertTrue(result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        boolean result = instance.equals(o);
        assertFalse(result);
    }

    @Test
    public void testToString() {
        CollectionLikeType instance = new CollectionLikeType(null, null, null, null, null, null, null, false);
        String result = instance.toString();
        assertNotNull(result);
    }
}