import static org.junit.Assert.*;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        // Typical use case
        // Testing for TYPE_AS_LIST
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);

        // Edge case
        // Testing for TYPE_SINGLETON_LIST
        type = TypeFactory.defaultInstance().constructType(Collections.singletonList("").getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);

        // Error scenario
        // Testing for unknown type
        type = TypeFactory.defaultInstance().constructType(LinkedList.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() {
        // Typical use case
        // Testing for TYPE_SINGLETON_MAP
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singletonMap("", "").getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);

        // Edge case
        // Testing for TYPE_UNMODIFIABLE_MAP
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(Collections.emptyMap()).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);

        // Error scenario
        // Testing for unknown type
        type = TypeFactory.defaultInstance().constructType(HashMap.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testConverter() {
        // Typical use case
        // Testing for TYPE_SINGLETON_SET
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, TypeFactory.defaultInstance().constructType(Set.class), Set.class);
        Set<String> set = new HashSet<>();
        set.add("item");
        Object result = converter.convert(set);
        assertTrue(result instanceof Set);
        assertEquals("item", ((Set<?>) result).iterator().next());

        // Edge case
        // Testing for TYPE_AS_LIST
        converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, TypeFactory.defaultInstance().constructType(List.class), List.class);
        List<String> list = new ArrayList<>();
        list.add("item");
        result = converter.convert(list);
        assertEquals(list, result);

        // Error scenario
        // Testing for wrong collection kind
        boolean exceptionThrown = false;
        try {
            converter = JavaUtilCollectionsDeserializers.converter(0, TypeFactory.defaultInstance().constructType(Set.class), Set.class);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testJavaUtilCollectionsConverter() {
        // Typical use case
        // Testing for TYPE_SINGLETON_LIST
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, TypeFactory.defaultInstance().constructType(List.class));
        List<String> list = new ArrayList<>();
        list.add("item");
        Object result = converter.convert(list);
        assertTrue(result instanceof List);
        assertEquals("item", ((List<?>) result).get(0));

        // Edge case
        // Testing for TYPE_UNMODIFIABLE_LIST
        converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, TypeFactory.defaultInstance().constructType(List.class));
        List<String> unmodifiableList = Collections.unmodifiableList(list);
        result = converter.convert(unmodifiableList);
        assertTrue(result instanceof List);
        assertEquals(unmodifiableList, result);

        // Error scenario
        // Testing for null value
        List<?> nullList = null;
        result = converter.convert(nullList);
        assertNull(result);
    }

    @Test
    public void testConverterMethods() {
        // Typical use case
        // Testing getInputType()
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, TypeFactory.defaultInstance().constructType(Set.class));
        JavaType inputType = TypeFactory.defaultInstance().constructType(Set.class);
        JavaType result = converter.getInputType(TypeFactory.defaultInstance());
        assertEquals(inputType, result);

        // Typical use case
        // Testing getOutputType()
        JavaType outputType = TypeFactory.defaultInstance().constructType(Set.class);
        result = converter.getOutputType(TypeFactory.defaultInstance());
        assertEquals(outputType, result);
    }

    @Test
    public void test_checkSingleton() {
        // Typical use case
        // Testing for valid singleton size
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, TypeFactory.defaultInstance().constructType(List.class));
        List<String> list = new ArrayList<>();
        list.add("item");
        boolean exceptionThrown = false;
        try {
            converter._checkSingleton(list.size());
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);

        // Error scenario
        // Testing for invalid singleton size
        exceptionThrown = false;
        try {
            converter._checkSingleton(list.size() + 1);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}