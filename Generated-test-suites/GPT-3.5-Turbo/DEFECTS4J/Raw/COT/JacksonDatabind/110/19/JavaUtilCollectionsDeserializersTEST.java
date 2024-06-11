
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        Assert.assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        Assert.assertEquals(StdDelegatingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testConverterSingletonSet() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        Set<Boolean> set = new HashSet<>();
        set.add(true);
        Object result = converter.convert(set);
        Assert.assertEquals(Collections.singleton(set.iterator().next()), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterSingletonSetInvalidSize() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        Set<Boolean> set = new HashSet<>();
        Object result = converter.convert(set);
    }

    @Test
    public void testConverterSingletonList() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        Object result = converter.convert(list);
        Assert.assertEquals(Collections.singletonList(list.get(0)), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterSingletonListInvalidSize() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);
        List<Boolean> list = new ArrayList<>();
        Object result = converter.convert(list);
    }

    @Test
    public void testConverterSingletonMap() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null);
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        Object result = converter.convert(map);
        Assert.assertEquals(Collections.singletonMap("a", "b"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterSingletonMapInvalidSize() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null);
        Map<String, String> map = new HashMap<>();
        Object result = converter.convert(map);
    }

    @Test
    public void testConverterUnmodifiableSet() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, null);
        Set<Boolean> set = new HashSet<>();
        set.add(true);
        Object result = converter.convert(set);
        Assert.assertEquals(Collections.unmodifiableSet(set), result);
    }

    @Test
    public void testConverterUnmodifiableList() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, null);
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        Object result = converter.convert(list);
        Assert.assertEquals(Collections.unmodifiableList(list), result);
    }

    @Test
    public void testConverterUnmodifiableMap() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, null);
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        Object result = converter.convert(map);
        Assert.assertEquals(Collections.unmodifiableMap(map), result);
    }

    @Test
    public void testConverterDefault() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, null);
        List<String> list = new ArrayList<>();
        list.add("a");
        Object result = converter.convert(list);
        Assert.assertEquals(list, result);
    }
}

