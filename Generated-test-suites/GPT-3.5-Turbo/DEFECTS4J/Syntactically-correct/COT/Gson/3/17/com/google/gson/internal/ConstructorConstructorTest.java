import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {

    // Test ObjectConstructor implementation
    @Test
    public void testObjectConstructor() {
        TypeToken<String> typeToken = new TypeToken<String>() {};
        ObjectConstructor<String> objectConstructor = new ConstructorConstructor(Collections.emptyMap()).get(typeToken);
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    // Test get method with registered instance creator
    @Test
    public void testGetWithInstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(String.class, () -> "test");
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        TypeToken<String> typeToken = new TypeToken<String>() {};
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertEquals("test", objectConstructor.construct());
    }

    // Test get method with raw type instance creator
    @Test
    public void testGetWithRawTypeInstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        instanceCreators.put(CharSequence.class, () -> "test");
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        TypeToken<String> typeToken = new TypeToken<String>() {};
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertEquals("test", objectConstructor.construct());
    }

    // Test get method with default constructor
    @Test
    public void testGetWithDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<String> typeToken = new TypeToken<String>() {};
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    // Test get method with default implementation constructor for Collection
    @Test
    public void testGetWithDefaultImplementationConstructorForCollection() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<Collection<String>> typeToken = new TypeToken<Collection<String>>() {};
        ObjectConstructor<Collection<String>> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertTrue(objectConstructor.construct() instanceof ArrayList);
    }

    // Test get method with default implementation constructor for SortedSet
    @Test
    public void testGetWithDefaultImplementationConstructorForSortedSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<SortedSet<String>> typeToken = new TypeToken<SortedSet<String>>() {};
        ObjectConstructor<SortedSet<String>> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertTrue(objectConstructor.construct() instanceof TreeSet);
    }

    // Test get method with default implementation constructor for EnumSet
    @Test
    public void testGetWithDefaultImplementationConstructorForEnumSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<EnumSet<TestEnum>> typeToken = new TypeToken<EnumSet<TestEnum>>() {};
        ObjectConstructor<EnumSet<TestEnum>> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertTrue(objectConstructor.construct() instanceof EnumSet);
    }

    // Test get method with default implementation constructor for Set
    @Test
    public void testGetWithDefaultImplementationConstructorForSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {};
        ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertTrue(objectConstructor.construct() instanceof LinkedHashSet);
    }

    // Test get method with default implementation constructor for Queue
    @Test
    public void testGetWithDefaultImplementationConstructorForQueue() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<Queue<String>> typeToken = new TypeToken<Queue<String>>() {};
        ObjectConstructor<Queue<String>> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertTrue(objectConstructor.construct() instanceof LinkedList);
    }

    // Test get method with default implementation constructor for Map
    @Test
    public void testGetWithDefaultImplementationConstructorForMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
        ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertTrue(objectConstructor.construct() instanceof LinkedHashMap);
    }

    // Test get method with default implementation constructor for SortedMap
    @Test
    public void testGetWithDefaultImplementationConstructorForSortedMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<SortedMap<String, Integer>> typeToken = new TypeToken<SortedMap<String, Integer>>() {};
        ObjectConstructor<SortedMap<String, Integer>> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertTrue(objectConstructor.construct() instanceof TreeMap);
    }

    // Test get method with default implementation constructor for LinkedHashMap
    @Test
    public void testGetWithDefaultImplementationConstructorForLinkedHashMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<Map<TestEnum, Integer>> typeToken = new TypeToken<Map<TestEnum, Integer>>() {};
        ObjectConstructor<Map<TestEnum, Integer>> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertTrue(objectConstructor.construct() instanceof LinkedHashMap);
    }

    // Test get method with unsafe allocator
    @Test
    public void testGetWithUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());

        TypeToken<TestEnum> typeToken = new TypeToken<TestEnum>() {};
        ObjectConstructor<TestEnum> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
    }

    // Test toString method
    @Test
    public void testToString() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap());
        assertNotNull(constructorConstructor.toString());
    }

    enum TestEnum {
        ONE, TWO, THREE;
    }
}