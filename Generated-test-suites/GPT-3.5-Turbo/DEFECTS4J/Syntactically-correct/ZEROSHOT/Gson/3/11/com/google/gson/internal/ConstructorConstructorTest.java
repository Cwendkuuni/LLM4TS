import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {
    
    private ConstructorConstructor constructorConstructor;
    
    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor(null);
    }
    
    @Test
    public void testGet_withInstanceCreator() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_withRawTypeCreator() {
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_withDefaultConstructor() {
        TypeToken<Double> typeToken = new TypeToken<Double>() {};
        ObjectConstructor<Double> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_withDefaultImplementationConstructor() {
        TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {};
        ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testGet_withUnsafeAllocator() {
        TypeToken<Long> typeToken = new TypeToken<Long>() {};
        ObjectConstructor<Long> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }
}