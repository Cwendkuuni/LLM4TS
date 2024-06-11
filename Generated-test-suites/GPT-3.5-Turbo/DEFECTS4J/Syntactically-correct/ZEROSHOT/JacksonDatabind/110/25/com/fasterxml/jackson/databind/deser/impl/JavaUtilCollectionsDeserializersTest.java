import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singleton(Boolean.TRUE).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
        StdDelegatingDeserializer<?> stdDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, stdDeserializer.getDelegatee().getClass());
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singletonMap("a", "b").getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
        assertNotNull(deserializer);
        assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
        StdDelegatingDeserializer<?> stdDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, stdDeserializer.getDelegatee().getClass());
    }

    @Test
    public void testConverter() {
        JavaType concreteType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
        JavaType superType = TypeFactory.defaultInstance().constructSimpleType(List.class, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, concreteType, List.class);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, converter.getClass());
        assertEquals(TypeFactory.defaultInstance().constructType(List.class), converter.getInputType(TypeFactory.defaultInstance()));
        assertEquals(TypeFactory.defaultInstance().constructType(List.class), converter.getOutputType(TypeFactory.defaultInstance()));
    }

    @Test
    public void testConverter_convert_TYPE_SINGLETON_SET() {
        Set<String> input = Collections.singleton("test");
        JavaType concreteType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, concreteType);
        Set<?> result = (Set<?>) converter.convert(input);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test", result.iterator().next());
    }

    @Test
    public void testConverter_convert_TYPE_SINGLETON_LIST() {
        List<String> input = Collections.singletonList("test");
        JavaType concreteType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, concreteType);
        List<?> result = (List<?>) converter.convert(input);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));
    }

    @Test
    public void testConverter_convert_TYPE_SINGLETON_MAP() {
        Map<String, String> input = Collections.singletonMap("a", "b");
        JavaType concreteType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, concreteType);
        Map<?, ?> result = (Map<?, ?>) converter.convert(input);
        assertNotNull(result);
        assertEquals(1, result.size());
        Map.Entry<?, ?> entry = result.entrySet().iterator().next();
        assertEquals("a", entry.getKey());
        assertEquals("b", entry.getValue());
    }

    @Test
    public void testConverter_convert_TYPE_UNMODIFIABLE_SET() {
        Set<String> input = new HashSet<>();
        input.add("test");
        JavaType concreteType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, concreteType);
        Set<?> result = (Set<?>) converter.convert(input);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test", result.iterator().next());
    }

    @Test
    public void testConverter_convert_TYPE_UNMODIFIABLE_LIST() {
        List<String> input = new ArrayList<>();
        input.add("test");
        JavaType concreteType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, concreteType);
        List<?> result = (List<?>) converter.convert(input);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));
    }

    @Test
    public void testConverter_convert_TYPE_UNMODIFIABLE_MAP() {
        Map<String, String> input = new HashMap<>();
        input.put("a", "b");
        JavaType concreteType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, concreteType);
        Map<?, ?> result = (Map<?, ?>) converter.convert(input);
        assertNotNull(result);
        assertEquals(1, result.size());
        Map.Entry<?, ?> entry = result.entrySet().iterator().next();
        assertEquals("a", entry.getKey());
        assertEquals("b", entry.getValue());
    }

    @Test
    public void testConverter_convert_TYPE_AS_LIST() {
        List<String> input = new ArrayList<>();
        input.add("test");
        JavaType concreteType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, concreteType);
        List<?> result = (List<?>) converter.convert(input);
        assertNotNull(result);
        assertEquals(input, result);
    }
}