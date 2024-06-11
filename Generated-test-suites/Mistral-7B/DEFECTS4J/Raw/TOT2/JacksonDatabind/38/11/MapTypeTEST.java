import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = BasicTypeFactory.intType();
        JavaType valueType = BasicTypeFactory.stringType();
        Object valueHandler = null;
        Object typeHandler = null;

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        assertEquals(mapType, mapTypeInstance.getType());
        assertEquals(bindings, mapTypeInstance._bindings);
        assertEquals(superClass, mapTypeInstance._superClass);
        assertEquals(superInts, mapTypeInstance._superInterfaces);
        assertEquals(keyType, mapTypeInstance._keyType);
        assertEquals(valueType, mapTypeInstance._valueType);
        assertNull(mapTypeInstance._valueHandler);
        assertNull(mapTypeInstance._typeHandler);
        assertFalse(mapTypeInstance._asStatic);
    }
}