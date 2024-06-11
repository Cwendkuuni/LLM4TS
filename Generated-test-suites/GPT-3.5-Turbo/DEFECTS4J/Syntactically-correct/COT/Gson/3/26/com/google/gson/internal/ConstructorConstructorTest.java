
import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
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
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;

public class ConstructorConstructorTest {

    @Test
    public void testGet_TypeToken() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<Object> typeToken = new TypeToken<Object>() {};

        // Test
        ObjectConstructor<Object> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_RawTypeMatch() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<Object> typeToken = new TypeToken<Object>() {
            @Override public Type getType() {
                return Object.class;
            }
        };

        // Test
        ObjectConstructor<Object> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultConstructor() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<TestObject> typeToken = new TypeToken<TestObject>() {};

        // Test
        ObjectConstructor<TestObject> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Collection() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<Collection<TestObject>> typeToken = new TypeToken<Collection<TestObject>>() {};

        // Test
        ObjectConstructor<Collection<TestObject>> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_SortedSet() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<SortedSet<TestObject>> typeToken = new TypeToken<SortedSet<TestObject>>() {};

        // Test
        ObjectConstructor<SortedSet<TestObject>> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_EnumSet() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<EnumSet<TestObject>> typeToken = new TypeToken<EnumSet<TestObject>>() {};

        // Test
        ObjectConstructor<EnumSet<TestObject>> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Set() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<Set<TestObject>> typeToken = new TypeToken<Set<TestObject>>() {};

        // Test
        ObjectConstructor<Set<TestObject>> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Queue() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<Queue<TestObject>> typeToken = new TypeToken<Queue<TestObject>>() {};

        // Test
        ObjectConstructor<Queue<TestObject>> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Map() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<Map<TestObject, TestObject>> typeToken = new TypeToken<Map<TestObject, TestObject>>() {};

        // Test
        ObjectConstructor<Map<TestObject, TestObject>> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_SortedMap() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<SortedMap<TestObject, TestObject>> typeToken = new TypeToken<SortedMap<TestObject, TestObject>>() {};

        // Test
        ObjectConstructor<SortedMap<TestObject, TestObject>> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_LinkedHashMap() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<Map<String, TestObject>> typeToken = new TypeToken<Map<String, TestObject>>() {};

        // Test
        ObjectConstructor<Map<String, TestObject>> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_LinkedTreeMap() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<Map<TestObject, TestObject>> typeToken = new TypeToken<Map<TestObject, TestObject>>() {};

        // Test
        ObjectConstructor<Map<TestObject, TestObject>> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testGet_UnsafeAllocator() {
        // Setup
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        TypeToken<TestObject> typeToken = new TypeToken<TestObject>() {};

        // Test
        ObjectConstructor<TestObject> result = constructorConstructor.get(typeToken);

        // Verify
        assertNotNull(result);
    }

    private static class TestObject {
        // Empty test object
    }
}
