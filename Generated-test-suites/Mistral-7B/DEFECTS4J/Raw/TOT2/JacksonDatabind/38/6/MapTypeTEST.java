import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = JavaType.constructRawListType(String.class);
        JavaType valueType = JavaType.constructRawListType(Integer.class);
        Object typeHandler = new MyTypeHandler();

        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        MapType mapTypeWithHandler = mapType.withTypeHandler(typeHandler);

        assertEquals(mapType.toString(), "[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + "]");
        assertEquals(mapTypeWithHandler.toString(), "[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + ", TypeHandler : " + typeHandler.getClass().getName() + "]");
    }

    static class MyTypeHandler {
        // Add necessary functionalities for testing
    }
}