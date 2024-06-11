
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    private DeserializationContext context;
    private JavaType javaType;

    @Before
    public void setUp() {
        context = new DeserializationContext(null, null, null);
        javaType = TypeFactory.defaultInstance().constructType(Object.class);
    }

    @Test
    public void testFindForCollection() {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
        assertNull(deserializer);
    }

    @Test
    public void testConverter_SingletonSet() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Set.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, concreteType);
        Set<Object> input = new HashSet<>(Collections.singletonList("test"));
        Object output = converter.convert(input);
        assertTrue(output instanceof Set);
        Set<Object> outputSet = (Set<Object>) output;
        assertEquals(1, outputSet.size());
        assertTrue(outputSet.contains("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverter_SingletonSet_MultipleEntries() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Set.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, concreteType);
        Set<Object> input = new HashSet<>(Arrays.asList("test1", "test2"));
        converter.convert(input);
    }

    @Test
    public void testConverter_SingletonList() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(List.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, concreteType);
        List<Object> input = new ArrayList<>(Collections.singletonList("test"));
        Object output = converter.convert(input);
        assertTrue(output instanceof List);
        List<Object> outputList = (List<Object>) output;
        assertEquals(1, outputList.size());
        assertTrue(outputList.contains("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverter_SingletonList_MultipleEntries() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(List.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, concreteType);
        List<Object> input = new ArrayList<>(Arrays.asList("test1", "test2"));
        converter.convert(input);
    }

    @Test
    public void testConverter_SingletonMap() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, concreteType);
        Map<Object, Object> input = new HashMap<>(Collections.singletonMap("key", "value"));
        Object output = converter.convert(input);
        assertTrue(output instanceof Map);
        Map<Object, Object> outputMap = (Map<Object, Object>) output;
        assertEquals(1, outputMap.size());
        assertTrue(outputMap.containsKey("key"));
        assertEquals("value", outputMap.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverter_SingletonMap_MultipleEntries() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, concreteType);
        Map<Object, Object> input = new HashMap<>();
        input.put("key1", "value1");
        input.put("key2", "value2");
        converter.convert(input);
    }

    @Test
    public void testConverter_UnmodifiableSet() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Set.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, concreteType);
        Set<Object> input = new HashSet<>(Collections.singletonList("test"));
        Object output = converter.convert(input);
        assertTrue(output instanceof Set);
        Set<Object> outputSet = (Set<Object>) output;
        assertNotSame(input, outputSet);
        assertEquals(input, output);
    }

    @Test
    public void testConverter_UnmodifiableList() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(List.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, concreteType);
        List<Object> input = new ArrayList<>(Collections.singletonList("test"));
        Object output = converter.convert(input);
        assertTrue(output instanceof List);
        List<Object> outputList = (List<Object>) output;
        assertNotSame(input, outputList);
        assertEquals(input, output);
    }

    @Test
    public void testConverter_UnmodifiableMap() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, concreteType);
        Map<Object, Object> input = new HashMap<>(Collections.singletonMap("key", "value"));
        Object output = converter.convert(input);
        assertTrue(output instanceof Map);
        Map<Object, Object> outputMap = (Map<Object, Object>) output;
        assertNotSame(input, outputMap);
        assertEquals(input, output);
    }
}
