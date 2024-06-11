import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = String.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapTypeInstance);
    }
}