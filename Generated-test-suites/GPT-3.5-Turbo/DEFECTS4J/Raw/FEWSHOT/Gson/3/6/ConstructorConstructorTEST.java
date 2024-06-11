import static org.junit.Assert.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import java.util.Map;
import java.lang.reflect.Type;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ArrayList;

public class ConstructorConstructorTest {
    
    private ConstructorConstructor constructorConstructor;
    
    @Test
    public void testGet_InstanceCreatorExists() {
        InstanceCreator<Double> instanceCreator = new InstanceCreator<>() {
            @Override
            public Double createInstance(Type type) {
                return 3.14;
            }
        };
        
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        instanceCreators.put(Double.class, instanceCreator);
        constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        ObjectConstructor<Double> objectConstructor = constructorConstructor.get(new TypeToken<Double>() {});
        
        assertEquals(3.14, objectConstructor.construct(), 0.000001);
    }
    
    @Test
    public void testGet_RawTypeCreatorExists() {
        InstanceCreator<Double> instanceCreator = new InstanceCreator<>() {
            @Override
            public Double createInstance(Type type) {
                return 3.14;
            }
        };
        
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        instanceCreators.put(Number.class, instanceCreator);
        constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        ObjectConstructor<Double> objectConstructor = constructorConstructor.get(new TypeToken<Double>(){});
        
        assertEquals(3.14, objectConstructor.construct(), 0.000001);
    }
    
    @Test
    public void testGet_DefaultConstructorExists() {
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        
        ObjectConstructor<ArrayList<?>> objectConstructor = constructorConstructor.get(new TypeToken<ArrayList<?>>(){});
        
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Collection() {
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        
        ObjectConstructor<TreeSet<?>> objectConstructor = constructorConstructor.get(new TypeToken<TreeSet<?>>(){});
        
        assertEquals(new TreeSet<>(), objectConstructor.construct());
        
        ObjectConstructor<LinkedHashSet<?>> objectConstructor2 = constructorConstructor.get(new TypeToken<LinkedHashSet<?>>(){});
        
        assertEquals(new LinkedHashSet<>(), objectConstructor2.construct());
        
        ObjectConstructor<LinkedList<?>> objectConstructor3 = constructorConstructor.get(new TypeToken<LinkedList<?>>(){});
        
        assertEquals(new LinkedList<>(), objectConstructor3.construct());
        
        ObjectConstructor<ArrayList<?>> objectConstructor4 = constructorConstructor.get(new TypeToken<ArrayList<?>>(){});
        
        assertEquals(new ArrayList<>(), objectConstructor4.construct());
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_EnumSet() {
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        
        ObjectConstructor<TreeSet<Double>> objectConstructor = constructorConstructor.get(new TypeToken<TreeSet<Double>>(){});
        
        assertEquals(new TreeSet<>(), objectConstructor.construct());
        
        ObjectConstructor<LinkedHashSet<Double>> objectConstructor2 = constructorConstructor.get(new TypeToken<LinkedHashSet<Double>>(){});
        
        assertEquals(new LinkedHashSet<>(), objectConstructor2.construct());
        
        ObjectConstructor<LinkedList<Double>> objectConstructor3 = constructorConstructor.get(new TypeToken<LinkedList<Double>>(){});
        
        assertEquals(new LinkedList<>(), objectConstructor3.construct());
        
        ObjectConstructor<ArrayList<Double>> objectConstructor4 = constructorConstructor.get(new TypeToken<ArrayList<Double>>(){});
        
        assertEquals(new ArrayList<>(), objectConstructor4.construct());
    }
    
    @Test
    public void testGet_DefaultImplementationConstructor_Map() {
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        
        ObjectConstructor<TreeMap<?,?>> objectConstructor = constructorConstructor.get(new TypeToken<TreeMap<?,?>>(){});
        
        assertEquals(new TreeMap<>(), objectConstructor.construct());
        
        ObjectConstructor<LinkedHashMap<?,?>> objectConstructor2 = constructorConstructor.get(new TypeToken<LinkedHashMap<?,?>>(){});
        
        assertEquals(new LinkedHashMap<>(), objectConstructor2.construct());
        
        ObjectConstructor<LinkedTreeMap<?,?>> objectConstructor3 = constructorConstructor.get(new TypeToken<LinkedTreeMap<?,?>>(){});
        
        assertEquals(new LinkedTreeMap<>(), objectConstructor3.construct());
        
        ObjectConstructor<LinkedTreeMap<String, Integer>> objectConstructor4 = constructorConstructor.get(new TypeToken<LinkedTreeMap<String, Integer>>(){});
        
        assertEquals(new LinkedTreeMap<>(), objectConstructor4.construct());
    }
    
    @Test
    public void testGet_UnsafeAllocatorExists() {
        constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        
        ObjectConstructor<Double> objectConstructor = constructorConstructor.get(new TypeToken<Double>(){});
        
        assertNotNull(objectConstructor.construct());
    }

    @Test
    public void testToString() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        instanceCreators.put(Double.class, new InstanceCreator<>(){});
        instanceCreators.put(Integer.class, new InstanceCreator<>(){});
        
        constructorConstructor = new ConstructorConstructor(instanceCreators);
        
        assertEquals("{class java.lang.Double=<null>, class java.lang.Integer=<null>}", constructorConstructor.toString());
    }
}