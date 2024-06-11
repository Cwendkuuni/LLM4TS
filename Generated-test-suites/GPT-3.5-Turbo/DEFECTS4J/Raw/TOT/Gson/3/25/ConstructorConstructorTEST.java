import org.junit.Before;
import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {

    private ConstructorConstructor constructorConstructor;
    private TypeToken<String> stringToken;
    private TypeToken<Integer> intToken;
    private TypeToken<List<String>> listToken;
    private TypeToken<Set<String>> setToken;

    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor(null);
        stringToken = TypeToken.get(String.class);
        intToken = TypeToken.get(Integer.class);
        listToken = new TypeToken<List<String>>() {};
        setToken = new TypeToken<Set<String>>() {};
    }

    @Test
    public void testGet_WithInstanceCreator_ReturnsObjectConstructor() {
        InstanceCreator<String> instanceCreator = new InstanceCreator<String>() {
            @Override
            public String createInstance(Type type) {
                return "InstanceCreated";
            }
        };

        constructorConstructor = new ConstructorConstructor(
                Collections.<Type, InstanceCreator<?>>singletonMap(String.class, instanceCreator));

        ObjectConstructor<String> objectConstructor = constructorConstructor.get(stringToken);

        assertNotNull(objectConstructor);
        assertEquals("InstanceCreated", objectConstructor.construct());
    }

    @Test
    public void testGet_WithTypeCreator_ReturnsObjectConstructor() {
        constructorConstructor = new ConstructorConstructor(
                Collections.<Type, InstanceCreator<?>>singletonMap(String.class, String::new));

        ObjectConstructor<String> objectConstructor = constructorConstructor.get(stringToken);

        assertNotNull(objectConstructor);
        assertEquals("", objectConstructor.construct());
    }

    @Test
    public void testGet_WithRawTypeCreator_ReturnsObjectConstructor() {
        constructorConstructor = new ConstructorConstructor(
                Collections.<Type, InstanceCreator<?>>singletonMap(CharSequence.class, StringBuilder::new));

        ObjectConstructor<CharSequence> objectConstructor = constructorConstructor.get(stringToken);

        assertNotNull(objectConstructor);
        assertEquals(new StringBuilder(), objectConstructor.construct());
    }

    @Test
    public void testGet_WithDefaultConstructor_ReturnsObjectConstructor() {
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(stringToken);

        assertNotNull(objectConstructor);
        assertEquals("", objectConstructor.construct());
    }

    @Test
    public void testGet_WithCollection_ReturnsObjectConstructor() {
        ObjectConstructor<List<String>> objectConstructor = constructorConstructor.get(listToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct() instanceof ArrayList);
    }

    @Test
    public void testGet_WithSortedSet_ReturnsObjectConstructor() {
        ObjectConstructor<SortedSet<String>> objectConstructor = constructorConstructor.get(
                new TypeToken<SortedSet<String>>() {});

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct() instanceof TreeSet);
    }

    @Test
    public void testGet_WithEnumSet_ReturnsObjectConstructor() {
        ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.get(setToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct() instanceof EnumSet);
    }

    @Test(expected = JsonIOException.class)
    public void testGet_WithInvalidEnumSet_ThrowsJsonIOException() {
        constructorConstructor.get(new TypeToken<Set<Integer>>() {});
    }

    @Test
    public void testGet_WithSet_ReturnsObjectConstructor() {
        ObjectConstructor<Set<String>> objectConstructor = constructorConstructor.get(setToken);

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct() instanceof LinkedHashSet);
    }

    @Test
    public void testGet_WithQueue_ReturnsObjectConstructor() {
        ObjectConstructor<Queue<String>> objectConstructor = constructorConstructor.get(
                new TypeToken<Queue<String>>() {});

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct() instanceof LinkedList);
    }

    @Test
    public void testGet_WithMap_ReturnsObjectConstructor() {
        ObjectConstructor<Map<String, Object>> objectConstructor = constructorConstructor.get(
                new TypeToken<Map<String, Object>>() {});

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct() instanceof LinkedHashMap);
    }

    @Test
    public void testGet_WithSortedMap_ReturnsObjectConstructor() {
        ObjectConstructor<SortedMap<String, Object>> objectConstructor = constructorConstructor.get(
                new TypeToken<SortedMap<String, Object>>() {});

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct() instanceof TreeMap);
    }

    @Test
    public void testGet_WithGenericLinkedHashMap_ReturnsObjectConstructor() {
        ObjectConstructor<Map<String, List<String>>> objectConstructor = constructorConstructor.get(
                new TypeToken<Map<String, List<String>>>() {});

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct() instanceof LinkedHashMap);
    }

    @Test
    public void testGet_WithGenericLinkedTreeMap_ReturnsObjectConstructor() {
        ObjectConstructor<Map<String, Object>> objectConstructor = constructorConstructor.get(
                new TypeToken<Map<String, Object>>() {});

        assertNotNull(objectConstructor);
        assertNotNull(objectConstructor.construct());
        assertTrue(objectConstructor.construct() instanceof LinkedTreeMap);
    }

    @Test(expected = RuntimeException.class)
    public void testGet_WithExceptionInDefaultConstructor_ThrowsRuntimeException() {
        constructorConstructor.get(intToken);
    }

    @Test
    public void testToString_ReturnsInstanceCreatorsToString() {
        String expected = "{}";
        assertEquals(expected, constructorConstructor.toString());
    }

}