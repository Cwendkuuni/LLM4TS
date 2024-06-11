import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.constructType(List.class);

        // Test 1: CLASS_AS_ARRAYS_LIST
        JsonDeserializer<?> result1 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result1);

        // Test 2: CLASS_SINGLETON_LIST
        type = mapper.constructType(JavaUtilCollectionsDeserializersTest.class);
        JsonDeserializer<?> result2 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result2);

        // Test 3: CLASS_SINGLETON_SET
        type = mapper.constructType(Set.class);
        JsonDeserializer<?> result3 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result3);
        assertTrue(result3 instanceof StdDelegatingDeserializer<?>);

        // Test 4: CLASS_UNMODIFIABLE_LIST
        type = mapper.constructType(List.class);
        JsonDeserializer<?> result4 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result4);
        assertTrue(result4 instanceof StdDelegatingDeserializer<?>);

        // Test 5: CLASS_UNMODIFIABLE_SET
        type = mapper.constructType(Set.class);
        JsonDeserializer<?> result5 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result5);
        assertTrue(result5 instanceof StdDelegatingDeserializer<?>);

        // Test 6: Unmatched type
        type = mapper.constructType(LinkedList.class);
        JsonDeserializer<?> result6 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result6);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.constructType(Map.class);

        // Test 1: CLASS_SINGLETON_MAP
        JsonDeserializer<?> result1 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result1);
        assertTrue(result1 instanceof StdDelegatingDeserializer<?>);

        // Test 2: CLASS_UNMODIFIABLE_MAP
        JsonDeserializer<?> result2 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result2);
        assertTrue(result2 instanceof StdDelegatingDeserializer<?>);

        // Test 3: Unmatched type
        type = mapper.constructType(HashMap.class);
        JsonDeserializer<?> result3 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(result3);
    }

    @Test
    public void testConverter() {
        int kind = 7;
        JavaType concreteType = new JavaType();
        Class<?> rawSuper = List.class;

        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
        assertNotNull(converter);

        int kind2 = 8;
        JavaType concreteType2 = new JavaType();
        Class<?> rawSuper2 = Set.class;

        JavaUtilCollectionsConverter converter2 = JavaUtilCollectionsDeserializers.converter(kind2, concreteType2, rawSuper2);
        assertNotNull(converter2);
    }

    @Test
    public void testConvert() {
        // Test 1: TYPE_SINGLETON_SET
        Set<String> set = new HashSet<>();
        set.add("element1");

        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, new JavaType(), Set.class);
        Object result1 = converter.convert(set);
        assertNotNull(result1);
        assertTrue(result1 instanceof Set<?>);

        // Test 2: TYPE_SINGLETON_LIST
        List<Integer> list = new ArrayList<>();
        list.add(1);

        JavaUtilCollectionsConverter converter2 = JavaUtilCollectionsDeserializers.converter(2, new JavaType(), List.class);
        Object result2 = converter2.convert(list);
        assertNotNull(result2);
        assertTrue(result2 instanceof List<?>);

        // Test 3: TYPE_SINGLETON_MAP
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        JavaUtilCollectionsConverter converter3 = JavaUtilCollectionsDeserializers.converter(3, new JavaType(), Map.class);
        Object result3 = converter3.convert(map);
        assertNotNull(result3);
        assertTrue(result3 instanceof Map<?, ?>);

        // Test 4: TYPE_UNMODIFIABLE_SET
        Set<Double> set2 = new HashSet<>();
        set2.add(1.5);

        JavaUtilCollectionsConverter converter4 = JavaUtilCollectionsDeserializers.converter(4, new JavaType(), Set.class);
        Object result4 = converter4.convert(set2);
        assertNotNull(result4);
        assertTrue(result4 instanceof Set<?>);

        // Test 5: TYPE_UNMODIFIABLE_LIST
        List<Float> list2 = new ArrayList<>();
        list2.add(0.5f);

        JavaUtilCollectionsConverter converter5 = JavaUtilCollectionsDeserializers.converter(5, new JavaType(), List.class);
        Object result5 = converter5.convert(list2);
        assertNotNull(result5);
        assertTrue(result5 instanceof List<?>);

        // Test 6: TYPE_UNMODIFIABLE_MAP
        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(5, 10);

        JavaUtilCollectionsConverter converter6 = JavaUtilCollectionsDeserializers.converter(6, new JavaType(), Map.class);
        Object result6 = converter6.convert(map2);
        assertNotNull(result6);
        assertTrue(result6 instanceof Map<?, ?>);

        // Test 7: TYPE_AS_LIST
        List<Character> list3 = new ArrayList<>();
        list3.add('a');

        JavaUtilCollectionsConverter converter7 = JavaUtilCollectionsDeserializers.converter(7, new JavaType(), List.class);
        Object result7 = converter7.convert(list3);
        assertNotNull(result7);
        assertTrue(result7 instanceof List<?>);
    }

    @Test
    public void testGetInputType() {
        JavaType inputType = new JavaType();

        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, inputType, Set.class);
        JavaType result = converter.getInputType(null);
        assertEquals(inputType, result);
    }

    @Test
    public void testGetOutputType() {
        JavaType inputType = new JavaType();

        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, inputType, Set.class);
        JavaType result = converter.getOutputType(null);
        assertEquals(inputType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSingleton() {
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, new JavaType(), Set.class);
        converter._checkSingleton(2);
    }

}