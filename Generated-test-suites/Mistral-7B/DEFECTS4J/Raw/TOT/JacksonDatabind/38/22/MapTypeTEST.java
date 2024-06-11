import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        // Test construct method with valid arguments
        JavaType keyType = JavaType.constructType(int[].class);
        JavaType valueType = JavaType.constructType(String.class);
        TypeBindings bindings = TypeBindings.empty();
        Class<?> rawType = Map.class;
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals("[map type; class Map<int[], String>, int[] -> String]", mapType.toString());

        // Test construct method with deprecated arguments
        JavaType keyType2 = JavaType.constructType(String.class);
        JavaType valueType2 = JavaType.constructType(Integer.class);
        MapType mapType2 = MapType.construct(String[].class, keyType2, valueType2);
        assertEquals("[map type; class [Ljava.lang.String;->Ljava/lang/Integer;]", mapType2.toString());

        // Test withTypeHandler method
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withTypeHandler(handler);
        assertEquals("[map type; class Map<int[], String>, int[] -> String, type handler = " + handler + "]", mapTypeWithHandler.toString());

        // Test withKeyTypeHandler method
        JavaType keyTypeWithHandler = JavaType.constructType(Long.class);
        MapType mapTypeWithKeyHandler = mapTypeWithHandler.withKeyTypeHandler(keyTypeWithHandler);
        assertEquals("[map type; class Map<Long, String>, Long -> String, type handler = " + handler + "]", mapTypeWithKeyHandler.toString());

        // Test withKeyValueHandler method
        JavaType keyValueHandler = JavaType.constructType(Double[].class);
        MapType mapTypeWithKeyValueHandler = mapTypeWithKeyHandler.withKeyValueHandler(keyValueHandler);
        assertEquals("[map type; class Map<Long, [Ljava.lang.Double;>], Long -> [Ljava.lang.Double;>, key handler = " + keyValueHandler + ", type handler = " + handler + "]", mapTypeWithKeyValueHandler.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidArguments() {
        // Test construct method with invalid arguments (null keyType)
        JavaType keyType = null;
        JavaType valueType = JavaType.constructType(String.class);
        TypeBindings bindings = TypeBindings.empty();
        Class<?> rawType = Map.class;
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidArguments2() {
        // Test construct method with invalid arguments (null valueType)
        JavaType keyType = JavaType.constructType(int[].class);
        JavaType valueType = null;
        TypeBindings bindings = TypeBindings.empty();
        Class<?> rawType = Map.class;
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = JavaType.constructType(int[].class);
        JavaType valueType = JavaType.constructType(String.class);
        TypeBindings bindings = TypeBindings.empty();
        Class<?> rawType = Map.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertEquals("[map type; class Map<int[], String>, int[] -> String, static typing = true]", mapTypeWithStaticTyping.toString());
    }
}