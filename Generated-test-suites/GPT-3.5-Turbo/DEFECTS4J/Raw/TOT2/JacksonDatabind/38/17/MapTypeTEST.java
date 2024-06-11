import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(MapType.class, null, null, null, JavaType.INT, JavaType.STRING);
        
        assertEquals(mapType.getKeyType(), JavaType.INT);
        assertEquals(mapType.getValueType(), JavaType.STRING);
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        MapType mapType = new MapType(MapType.class, null, null, null, JavaType.INT, JavaType.STRING, null, null, false);
        
        MapType newMapType = mapType.withTypeHandler(typeHandler);
        
        assertEquals(newMapType.getTypeHandler(), typeHandler);
    }

    // Additional test cases for other methods...

}