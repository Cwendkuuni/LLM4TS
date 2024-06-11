import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {

    // Test ConstructorConstructor.get(TypeToken<T> typeToken)
    // First try an instance creator
    @Test
    public void testGet_InstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(Integer.class, new InstanceCreator<Integer>() {
            @Override
            public Integer createInstance(Type type) {
                return new Integer(5);
            }
        });
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
        Integer result = objectConstructor.construct();
        assertEquals(new Integer(5), result);
    }

    // Next try raw type match for instance creators
    @Test
    public void testGet_RawTypeCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(Number.class, new InstanceCreator<Number>() {
            @Override
            public Number createInstance(Type type) {
                return new Integer(10);
            }
        });
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
        Integer result = objectConstructor.construct();
        assertEquals(new Integer(10), result);
    }

    // Test newDefaultConstructor(Class<? super T> rawType)
    @Test
    public void testNewDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ObjectConstructor<String> objectConstructor = constructorConstructor.newDefaultConstructor(String.class);
        assertNotNull(objectConstructor);
    }

    // Test newDefaultImplementationConstructor(Type type, Class<? super T> rawType)
    @Test
    public void testNewDefaultImplementationConstructor_List() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        TypeToken<List<Integer>> typeToken = new TypeToken<List<Integer>>() {};
        ObjectConstructor<List<Integer>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), List.class);
        List<Integer> result = objectConstructor.construct();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNewDefaultImplementationConstructor_Map() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
        ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), Map.class);
        Map<String, Integer> result = objectConstructor.construct();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test newUnsafeAllocator(Type type, Class<? super T> rawType)
    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        TypeToken<String> typeToken = new TypeToken<String>() {};
        ObjectConstructor<String> objectConstructor = constructorConstructor.newUnsafeAllocator(typeToken.getType(), String.class);
        String result = objectConstructor.construct();
        assertNotNull(result);
    }

}