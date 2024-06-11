import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = Mockito.mock(TypeBindings.class);
        JavaType superClass = Mockito.mock(JavaType.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = Mockito.mock(JavaType.class);
        JavaType valueT = Mockito.mock(JavaType.class);

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);

        assertEquals("[map type; class " + mapType.getName() + ", " + keyT + " -> " + valueT + "]", mapTypeInstance.toString());
    }
}