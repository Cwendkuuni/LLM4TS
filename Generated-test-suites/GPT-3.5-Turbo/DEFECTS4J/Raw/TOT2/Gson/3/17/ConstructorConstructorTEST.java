import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConstructorConstructorTest {

    @Test
    public void testGet_instanceCreatorsNotNull() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        assertNotNull(constructorConstructor.instanceCreators);
    }

    @Test
    public void testGet_defaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        TypeToken<String> typeToken = TypeToken.get(String.class);
        Constructor testConstructor = constructorConstructor.get(typeToken);
        assertNotNull(testConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor_SortedSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        ObjectConstructor testConstructor = constructorConstructor.newDefaultImplementationConstructor(SortedSet.class, TreeSet.class);
        assertNotNull(testConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor_EnumSet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        ObjectConstructor testConstructor = constructorConstructor.newDefaultImplementationConstructor(EnumSet.class, EnumSet.class);
        assertNotNull(testConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor_Set() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        ObjectConstructor testConstructor = constructorConstructor.newDefaultImplementationConstructor(Set.class, Set.class);
        assertNotNull(testConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor_Queue() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        ObjectConstructor testConstructor = constructorConstructor.newDefaultImplementationConstructor(Queue.class, Queue.class);
        assertNotNull(testConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor_Map() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        ObjectConstructor testConstructor = constructorConstructor.newDefaultImplementationConstructor(SortedMap.class, TreeMap.class);
        assertNotNull(testConstructor);
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        TypeToken<String> typeToken = TypeToken.get(String.class);
        Constructor testConstructor = constructorConstructor.get(typeToken);
        assertNotNull(testConstructor);
    }

    @Test
    public void testToString() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        assertEquals("{}", constructorConstructor.toString());
    }

}