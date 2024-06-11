import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import com.fasterxml.jackson.databind.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        Assert.assertNotNull(deserializer);
    }
    
    @Test
    public void testConverterSingletonSet() {
        Set<Boolean> set = new HashSet<>();
        set.add(true);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, TypeFactory.defaultInstance().constructType(Set.class));
        Object result = converter.convert(set);
        Assert.assertTrue(result instanceof Set);
        Set<?> resultSet = (Set<?>) result;
        Assert.assertEquals(1, resultSet.size());
        Assert.assertTrue(resultSet.contains(true));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConverterSingletonSetMultipleEntries() {
        Set<Boolean> set = new HashSet<>();
        set.add(true);
        set.add(false);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, TypeFactory.defaultInstance().constructType(Set.class));
        converter.convert(set);
    }
    
    @Test
    public void testConverterSingletonList() {
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, TypeFactory.defaultInstance().constructType(List.class));
        Object result = converter.convert(list);
        Assert.assertTrue(result instanceof List);
        List<?> resultList = (List<?>) result;
        Assert.assertEquals(1, resultList.size());
        Assert.assertTrue(resultList.contains(true));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConverterSingletonListMultipleEntries() {
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        list.add(false);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, TypeFactory.defaultInstance().constructType(List.class));
        converter.convert(list);
    }
    
    @Test
    public void testConverterSingletonMap() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, TypeFactory.defaultInstance().constructType(Map.class));
        Object result = converter.convert(map);
        Assert.assertTrue(result instanceof Map);
        Map<?, ?> resultMap = (Map<?, ?>) result;
        Assert.assertEquals(1, resultMap.size());
        Assert.assertTrue(resultMap.containsKey("a"));
        Assert.assertTrue(resultMap.containsValue("b"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConverterSingletonMapMultipleEntries() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        map.put("c", "d");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, TypeFactory.defaultInstance().constructType(Map.class));
        converter.convert(map);
    }
    
    @Test
    public void testConverterUnmodifiableSet() {
        Set<Boolean> set = new HashSet<>();
        set.add(true);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, TypeFactory.defaultInstance().constructType(Set.class));
        Object result = converter.convert(set);
        Assert.assertTrue(result instanceof Set);
        Set<?> resultSet = (Set<?>) result;
        Assert.assertEquals(1, resultSet.size());
        Assert.assertTrue(resultSet.contains(true));
        Assert.assertTrue(resultSet instanceof UnmodifiableSet);
    }
    
    @Test
    public void testConverterUnmodifiableList() {
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, TypeFactory.defaultInstance().constructType(List.class));
        Object result = converter.convert(list);
        Assert.assertTrue(result instanceof List);
        List<?> resultList = (List<?>) result;
        Assert.assertEquals(1, resultList.size());
        Assert.assertTrue(resultList.contains(true));
        Assert.assertTrue(resultList instanceof UnmodifiableList);
    }
    
    @Test
    public void testConverterUnmodifiableMap() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, TypeFactory.defaultInstance().constructType(Map.class));
        Object result = converter.convert(map);
        Assert.assertTrue(result instanceof Map);
        Map<?, ?> resultMap = (Map<?, ?>) result;
        Assert.assertEquals(1, resultMap.size());
        Assert.assertTrue(resultMap.containsKey("a"));
        Assert.assertTrue(resultMap.containsValue("b"));
        Assert.assertTrue(resultMap instanceof UnmodifiableMap);
    }
    
    @Test
    public void testConverterAsList() {
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, TypeFactory.defaultInstance().constructType(List.class));
        Object result = converter.convert(list);
        Assert.assertEquals(list, result);
    }

}