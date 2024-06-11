import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {
    
    @Test
    public void testGet_withInstanceCreator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
            @Override
            public String createInstance(Type type) {
                return "test";
            }
        };
        constructorConstructor.instanceCreators.put(String.class, instanceCreator);
        
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(new TypeToken<String>(){});
        
        assertNotNull(objectConstructor);
        assertEquals("test", objectConstructor.construct());
    }

    @Test
    public void testGet_withRawTypeCreator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
            @Override
            public String createInstance(Type type) {
                return "raw type";
            }
        };
        constructorConstructor.instanceCreators.put(String.class, instanceCreator);
        
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(new TypeToken<String>(){});
        
        assertNotNull(objectConstructor);
        assertEquals("raw type", objectConstructor.construct());
    }

    @Test
    public void testGet_withDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(new TypeToken<String>(){});
        
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGet_withDefaultImplementationConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        
        ObjectConstructor<TreeSet<Object>> objectConstructor = constructorConstructor.get(new TypeToken<TreeSet<Object>>(){});
        
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGet_withUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(new TypeToken<String>(){});
        
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }
}