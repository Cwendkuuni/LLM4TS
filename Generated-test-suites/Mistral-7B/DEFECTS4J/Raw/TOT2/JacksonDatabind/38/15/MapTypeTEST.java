import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapType mapType = new MapType(MapType.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals("[map type; class com.fasterxml.jackson.databind.type.MapType, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}