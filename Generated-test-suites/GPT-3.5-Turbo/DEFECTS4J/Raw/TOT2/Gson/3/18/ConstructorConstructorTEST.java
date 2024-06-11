import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConstructorConstructorTest {

    @Test
    public void testGetWithInstanceCreator() {
        // Setup
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(String.class, new SampleInstanceCreator());
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        // Test
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(TypeToken.get(String.class));
        assertNotNull(objectConstructor);
        assertEquals("SampleInstanceCreatedString", objectConstructor.construct());
    }

    @Test
    public void testGetWithDefaultConstructor() {
        // Setup
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        // Test
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(TypeToken.get(String.class));
        assertNotNull(objectConstructor);
        assertEquals("", objectConstructor.construct());
    }

    @Test
    public void testGetWithDefaultImplementationConstructor() {
        // Setup
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        // Test
        ObjectConstructor<Map<String, Object>> mapConstructor = constructorConstructor.get(TypeToken.get(Map.class));
        assertNotNull(mapConstructor);
        Map<String, Object> map = mapConstructor.construct();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testToString() {
        // Setup
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(String.class, new SampleInstanceCreator());
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        // Test
        String toStringResult = constructorConstructor.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("String"));
    }

    private static class SampleInstanceCreator implements InstanceCreator<String> {
        @Override
        public String createInstance(Type type) {
            return "SampleInstanceCreatedString";
        }
    }
}