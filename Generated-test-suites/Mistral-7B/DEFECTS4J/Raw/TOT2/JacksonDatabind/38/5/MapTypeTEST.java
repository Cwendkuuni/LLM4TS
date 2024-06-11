import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testDefaultConstructor() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        assertNotNull(mapType);
    }
}