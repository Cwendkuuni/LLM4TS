import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

    private ConstructorConstructor constructorConstructor;

    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    }

    @Test
    public void testGet_InstanceCreator() {
        TypeToken<String> typeToken = TypeToken.get(String.class);
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testGet_DefaultConstructor() {
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
        ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor_Collection() {
        TypeToken<ArrayList<Integer>> typeToken = new TypeToken<ArrayList<Integer>>() {};
        ObjectConstructor<ArrayList<Integer>> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor_Map() {
        TypeToken<LinkedHashMap<String, Integer>> typeToken = new TypeToken<LinkedHashMap<String, Integer>>() {};
        ObjectConstructor<LinkedHashMap<String, Integer>> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testNewUnsafeAllocator() {
        TypeToken<Double> typeToken = TypeToken.get(Double.class);
        ObjectConstructor<Double> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }
}