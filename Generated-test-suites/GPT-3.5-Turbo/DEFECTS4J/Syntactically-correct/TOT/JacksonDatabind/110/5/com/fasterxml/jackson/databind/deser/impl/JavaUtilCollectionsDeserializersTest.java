import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        // Test typical use case with CLASS_AS_ARRAYS_LIST
        JavaType collectionType1 = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<?> deserializer1 = JavaUtilCollectionsDeserializers.findForCollection(null, collectionType1);
        assertTrue(deserializer1 instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> delegatingDeserializer1 = (StdDelegatingDeserializer<?>) deserializer1;
        assertTrue(delegatingDeserializer1.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        // Test typical use case with CLASS_SINGLETON_LIST
        JavaType collectionType2 = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<?> deserializer2 = JavaUtilCollectionsDeserializers.findForCollection(null, collectionType2);
        assertTrue(deserializer2 instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> delegatingDeserializer2 = (StdDelegatingDeserializer<?>) deserializer2;
        assertTrue(delegatingDeserializer2.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        // Test typical use case with CLASS_SINGLETON_SET
        JavaType collectionType3 = TypeFactory.defaultInstance().constructCollectionType(Set.class, Double.class);
        JsonDeserializer<?> deserializer3 = JavaUtilCollectionsDeserializers.findForCollection(null, collectionType3);
        assertTrue(deserializer3 instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> delegatingDeserializer3 = (StdDelegatingDeserializer<?>) deserializer3;
        assertTrue(delegatingDeserializer3.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        // Test typical use case with CLASS_UNMODIFIABLE_LIST
        JavaType collectionType4 = TypeFactory.defaultInstance().constructCollectionType(List.class, Float.class);
        JsonDeserializer<?> deserializer4 = JavaUtilCollectionsDeserializers.findForCollection(null, collectionType4);
        assertTrue(deserializer4 instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> delegatingDeserializer4 = (StdDelegatingDeserializer<?>) deserializer4;
        assertTrue(delegatingDeserializer4.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        // Test typical use case with CLASS_UNMODIFIABLE_SET
        JavaType collectionType5 = TypeFactory.defaultInstance().constructCollectionType(Set.class, Long.class);
        JsonDeserializer<?> deserializer5 = JavaUtilCollectionsDeserializers.findForCollection(null, collectionType5);
        assertTrue(deserializer5 instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> delegatingDeserializer5 = (StdDelegatingDeserializer<?>) deserializer5;
        assertTrue(delegatingDeserializer5.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        // Test invalid type
        JavaType invalidType = TypeFactory.defaultInstance().constructType(Map.class);
        JsonDeserializer<?> deserializer6 = JavaUtilCollectionsDeserializers.findForCollection(null, invalidType);
        assertNull(deserializer6);
    }

    @Test
    public void testFindForMap() {
        // Test typical use case with CLASS_SINGLETON_MAP
        JavaType mapType1 = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        JsonDeserializer<?> deserializer1 = JavaUtilCollectionsDeserializers.findForMap(null, mapType1);
        assertTrue(deserializer1 instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> delegatingDeserializer1 = (StdDelegatingDeserializer<?>) deserializer1;
        assertTrue(delegatingDeserializer1.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        // Test typical use case with CLASS_UNMODIFIABLE_MAP
        JavaType mapType2 = TypeFactory.defaultInstance().constructMapType(Map.class, Float.class, Boolean.class);
        JsonDeserializer<?> deserializer2 = JavaUtilCollectionsDeserializers.findForMap(null, mapType2);
        assertTrue(deserializer2 instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> delegatingDeserializer2 = (StdDelegatingDeserializer<?>) deserializer2;
        assertTrue(delegatingDeserializer2.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        // Test invalid type
        JavaType invalidType = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> deserializer3 = JavaUtilCollectionsDeserializers.findForMap(null, invalidType);
        assertNull(deserializer3);
    }

    @Test
    public void testJavaUtilCollectionsConverter_convert() {
        // Test TYPE_SINGLETON_SET case
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter1 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET,
                        TypeFactory.defaultInstance().constructCollectionType(Set.class, Integer.class));
        Set<Integer> set1 = Collections.singleton(123);
        Object convertedSet1 = converter1.convert(set1);
        assertEquals(Collections.singleton(123), convertedSet1);

        Set<Integer> emptySet1 = Collections.emptySet();
        try {
            converter1.convert(emptySet1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test TYPE_SINGLETON_LIST case
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter2 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST,
                        TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
        List<String> list1 = Collections.singletonList("test");
        Object convertedList1 = converter2.convert(list1);
        assertEquals(Collections.singletonList("test"), convertedList1);

        List<String> emptyList1 = Collections.emptyList();
        try {
            converter2.convert(emptyList1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test TYPE_SINGLETON_MAP case
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter3 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP,
                        TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class));
        Map<String, Integer> map1 = Collections.singletonMap("key", 123);
        Object convertedMap1 = converter3.convert(map1);
        assertEquals(Collections.singletonMap("key",123), convertedMap1);

        Map<String, Integer> emptyMap1 = Collections.emptyMap();
        try {
            converter3.convert(emptyMap1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test TYPE_UNMODIFIABLE_SET case
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter4 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET,
                        TypeFactory.defaultInstance().constructCollectionType(Set.class, Integer.class));
        Set<Integer> unmodifiableSet = Collections.unmodifiableSet(set1);
        Object convertedUnmodifiableSet = converter4.convert(unmodifiableSet);
        assertEquals(Collections.unmodifiableSet(set1), convertedUnmodifiableSet);

        // Test TYPE_UNMODIFIABLE_LIST case
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter5 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST,
                        TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
        List<String> unmodifiableList = Collections.unmodifiableList(list1);
        Object convertedUnmodifiableList = converter5.convert(unmodifiableList);
        assertEquals(Collections.unmodifiableList(list1), convertedUnmodifiableList);

        // Test TYPE_UNMODIFIABLE_MAP case
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter6 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP,
                        TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class));
        Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(map1);
        Object convertedUnmodifiableMap = converter6.convert(unmodifiableMap);
        assertEquals(Collections.unmodifiableMap(map1), convertedUnmodifiableMap);

        // Test TYPE_AS_LIST case
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter7 =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_AS_LIST,
                        TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class));
        List<Integer> inputList = Arrays.asList(1, 2, 3);
        Object convertedList2 = converter7.convert(inputList);
        assertEquals(inputList, convertedList2);
    }

    @Test
    public void testJavaUtilCollectionsConverter_getInputType() {
        JavaType expectedInputType =
                TypeFactory.defaultInstance().constructCollectionType(Set.class, Float.class);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, expectedInputType);

        JavaType actualInputType = converter.getInputType(null);
        assertEquals(expectedInputType, actualInputType);
    }

    @Test
    public void testJavaUtilCollectionsConverter_getOutputType() {
        JavaType expectedOutputType =
                TypeFactory.defaultInstance().constructCollectionType(Set.class, Float.class);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, expectedOutputType);

        JavaType actualOutputType = converter.getOutputType(null);
        assertEquals(expectedOutputType, actualOutputType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaUtilCollectionsConverter_checkSingleton_invalidSize() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET,
                        TypeFactory.defaultInstance().constructCollectionType(Set.class, Integer.class));
        converter._checkSingleton(2);
    }

    @Test
    public void testJavaUtilCollectionsConverter_checkSingleton_validSize() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET,
                        TypeFactory.defaultInstance().constructCollectionType(Set.class, Integer.class));
        converter._checkSingleton(1);
    }
}