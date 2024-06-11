import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

@RunWith(BlockJUnit4ClassRunner.class)
public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = JavaType.constructRawType(String.class);
        JavaType valueType = JavaType.constructRawType(Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = JavaType.constructRawType(String.class);
        JavaType valueType = JavaType.constructRawType(Integer.class);
        Object valueHandler = new Object() {};
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        MapType mapTypeWithHandler = mapType.withTypeHandler(new Object() {});
        assertNotSame(mapType, mapTypeWithHandler);
        assertEquals(valueHandler, mapTypeWithHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithInvalidKeyType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = JavaType.constructRawType(Object.class);
        JavaType valueType = JavaType.constructRawType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeWithInvalidKeyType = mapType.withKeyType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithInvalidValueType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = JavaType.constructRawType(String.class);
        JavaType valueType = JavaType.constructRawType(Object.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeWithInvalidValueType = mapType.withValueType(null);
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = JavaType.constructRawType(String.class);
        JavaType valueType = JavaType.constructRawType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertTrue(mapTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testMapTypeToString() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = JavaType.constructRawType(String.class);
        JavaType valueType = JavaType.constructRawType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        String expected = "[map type; class " + Map.class.getName() + ", " + keyType + " -> " + valueType + "]";
        assertEquals(expected, mapType.toString());
    }
}