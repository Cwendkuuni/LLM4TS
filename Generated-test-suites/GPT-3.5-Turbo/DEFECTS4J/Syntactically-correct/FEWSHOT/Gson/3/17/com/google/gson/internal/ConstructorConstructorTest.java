import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Collection;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.EnumSet;
import java.util.Arrays;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.JsonIOException;
import com.google.gson.internal.LinkedTreeMap;
import sun.misc.Unsafe;

public class ConstructorConstructorTest {
    @Test
    public void testConstructorConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new TreeMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        assertNotNull(constructorConstructor);
        assertTrue(constructorConstructor.get(new TypeToken<Object>() {}.getType()) instanceof ObjectConstructor);
    }
    
    @Test
    public void testGet_WithInstanceCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<Type, InstanceCreator<?>>();
        InstanceCreator<Object> instanceCreator = new InstanceCreator<Object>() {
            public Object createInstance(Type type) {
                return new Object();
            }
        };
        instanceCreators.put(Object.class, instanceCreator);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(new TypeToken<Object>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(Object.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithTypeCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<Type, InstanceCreator<?>>();
        final Type type = new TypeToken<List<String>>() {}.getType();
        InstanceCreator<List<String>> typeCreator = new InstanceCreator<List<String>>() {
            public List<String> createInstance(Type type) {
                return new ArrayList<String>();
            }
        };
        instanceCreators.put(type, typeCreator);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<List<String>> objectConstructor = constructorConstructor.get(type);
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(ArrayList.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithRawTypeCreator() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<Type, InstanceCreator<?>>();
        final Type type = new TypeToken<List<String>>() {}.getType();
        InstanceCreator<List<String>> rawTypeCreator = new InstanceCreator<List<String>>() {
            public List<String> createInstance(Type type) {
                return new LinkedList<String>();
            }
        };
        instanceCreators.put(List.class, rawTypeCreator);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        ObjectConstructor<List<String>> objectConstructor = constructorConstructor.get(type);
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(LinkedList.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<Integer> objectConstructor = constructorConstructor.get(new TypeToken<Integer>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(Integer.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_Collection() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<Collection<Integer>> objectConstructor = constructorConstructor.get(new TypeToken<Collection<Integer>>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(ArrayList.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_SortedSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<SortedSet<Integer>> objectConstructor = constructorConstructor.get(new TypeToken<SortedSet<Integer>>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(TreeSet.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_EnumSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<EnumSet<TestEnum>> objectConstructor = constructorConstructor.get(new TypeToken<EnumSet<TestEnum>>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(EnumSet.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_Set() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<Set<Integer>> objectConstructor = constructorConstructor.get(new TypeToken<Set<Integer>>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(HashSet.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_Queue() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<Queue<Integer>> objectConstructor = constructorConstructor.get(new TypeToken<Queue<Integer>>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(LinkedList.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_Map() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<Map<Integer, String>> objectConstructor = constructorConstructor.get(new TypeToken<Map<Integer, String>>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(LinkedHashMap.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_SortedMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<SortedMap<Integer, String>> objectConstructor = constructorConstructor.get(new TypeToken<SortedMap<Integer, String>>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(TreeMap.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_LinkedHashMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<LinkedHashMap<Integer, String>> objectConstructor = constructorConstructor.get(new TypeToken<LinkedHashMap<Integer, String>>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(LinkedHashMap.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithDefaultImplementationConstructor_LinkedTreeMap() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<LinkedTreeMap<String, Integer>> objectConstructor = constructorConstructor.get(new TypeToken<LinkedTreeMap<String, Integer>>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(LinkedTreeMap.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testGet_WithUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        ObjectConstructor<Unsafe> objectConstructor = constructorConstructor.get(new TypeToken<Unsafe>() {}.getType());
        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertEquals(Unsafe.class, objectConstructor.construct().getClass());
    }
    
    @Test
    public void testToString() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        assertNotNull(constructorConstructor.toString());
    }
}

enum TestEnum {
    TEST1, TEST2, TEST3
}