import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = JavaType.constructRawClassType(String.class);
        JavaType valueType = JavaType.constructRawClassType(Integer.class);

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInterfaces, keyType, valueType);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = JavaType.constructRawClassType(String.class);
        JavaType valueType = JavaType.constructRawClassType(Integer.class);

        Object handler = new Object();

        MapType mapType = new MapType(rawType, bindings, superClass, superInterfaces, keyType, valueType);
        MapType withHandler = mapType.withTypeHandler(handler);

        assertEquals(handler, withHandler.getTypeHandler());
        assertEquals(mapType, withHandler); // should be the same instance
    }

    @Test(expected = IllegalStateException.class)
    public void testDeprecatedConstruct() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClassType(String.class);
        JavaType valueType = JavaType.constructRawClassType(Integer.class);

        MapType mapType = MapType.construct(rawType, keyType, valueType);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeprecatedNarrow() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.constructCollectionType(Map.class, String.class, Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = JavaType.constructRawClassType(String.class);
        JavaType valueType = JavaType.constructRawClassType(Integer.class);

        MapType mapType = new MapType(rawType, bindings, superClass, superInterfaces, keyType, valueType);

        mapType._narrow(Map.class);
    }
}