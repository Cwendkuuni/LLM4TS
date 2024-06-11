import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
    }

    @Test
    public void testMapType() {
        Class<?> mapType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        MapType mapType = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, asStatic);
        assertNotNull(mapType);
    }

    @Test
    public void testMapTypeProtected() {
        TypeBase base = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = new MapType(base, keyT, valueT);
        assertNotNull(mapType);
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        assertNotNull(mapType);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType result = mapType._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType result = mapType.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyType(keyType);
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType keyType = null;
        JavaType valueType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType result = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueHandler() {
        Object h = null;
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        String result = mapType.toString();
        assertNotNull(result);
    }
}