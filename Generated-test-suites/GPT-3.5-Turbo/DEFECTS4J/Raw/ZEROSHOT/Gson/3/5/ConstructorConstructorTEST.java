import org.junit.Assert;
import org.junit.Test;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
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
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.LinkedTreeMap;
import sun.misc.Unsafe;

public class ConstructorConstructorTest {

    @SuppressWarnings("unchecked")
    private ConstructorConstructor constructorConstructor;

    public ConstructorConstructorTest() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        this.constructorConstructor = new ConstructorConstructor(instanceCreators);
    }

    @Test
    public void testGet_InstanceCreator() {
        TypeToken<String> typeToken = new TypeToken<String>() {
        };
        ObjectConstructor<String> result = constructorConstructor.get(typeToken);
        Assert.assertNull(result);
    }
    
    @Test
    public void testGet_RawTypeCreator() {
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {
        };
        ObjectConstructor<Integer> result = constructorConstructor.get(typeToken);
        Assert.assertNull(result);
    }

    @Test
    public void testGet_DefaultConstructor() {
        TypeToken<Double> typeToken = new TypeToken<Double>() {
        };
        ObjectConstructor<Double> result = constructorConstructor.get(typeToken);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Collection_SortedSet() {
        TypeToken<SortedSet<String>> typeToken = new TypeToken<SortedSet<String>>() {
        };
        ObjectConstructor<SortedSet<String>> result = constructorConstructor.get(typeToken);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Collection_EnumSet() {
        TypeToken<EnumSet<TestEnum>> typeToken = new TypeToken<EnumSet<TestEnum>>() {
        };
        ObjectConstructor<EnumSet<TestEnum>> result = constructorConstructor.get(typeToken);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Collection_Set() {
        TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {
        };
        ObjectConstructor<Set<String>> result = constructorConstructor.get(typeToken);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Collection_Queue() {
        TypeToken<Queue<String>> typeToken = new TypeToken<Queue<String>>() {
        };
        ObjectConstructor<Queue<String>> result = constructorConstructor.get(typeToken);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Collection_List() {
        TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {
        };
        ObjectConstructor<ArrayList<String>> result = constructorConstructor.get(typeToken);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Map_SortedMap() {
        TypeToken<SortedMap<String, Integer>> typeToken = new TypeToken<SortedMap<String, Integer>>() {
        };
        ObjectConstructor<SortedMap<String, Integer>> result = constructorConstructor.get(typeToken);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Map_ParameterizedType() {
        TypeToken<Map<Integer, String>> typeToken = new TypeToken<Map<Integer, String>>() {
        };
        ObjectConstructor<Map<Integer, String>> result = constructorConstructor.get(typeToken);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGet_DefaultImplementationConstructor_Map_LinkedHashMap() {
        TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {
        };
        ObjectConstructor<Map<String, Integer>> result = constructorConstructor.get(typeToken);
        Assert.assertNotNull(result);
    }

    @Test
    public void testToString() {
        String result = constructorConstructor.toString();
        Assert.assertNotNull(result);
    }
}

enum TestEnum {
    ONE,
    TWO,
    THREE
}