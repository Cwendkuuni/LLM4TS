
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_withClassAsArraysList() {
        // Test when type has raw class CLASS_AS_ARRAYS_LIST
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_AS_ARRAYS_LIST);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForCollection_withClassSingletonList() {
        // Test when type has raw class CLASS_SINGLETON_LIST
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollection_withClassSingletonSet() {
        // Test when type has raw class CLASS_SINGLETON_SET
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollection_withClassUnmodifiableList() {
        // Test when type has raw class CLASS_UNMODIFIABLE_LIST
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForCollection_withClassUnmodifiableSet() {
        // Test when type has raw class CLASS_UNMODIFIABLE_SET
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_withClassSingletonMap() {
        // Test when type has raw class CLASS_SINGLETON_MAP
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindForMap_withClassUnmodifiableMap() {
        // Test when type has raw class CLASS_UNMODIFIABLE_MAP
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
    }
    
    @Test
    public void testConverter_withTypeSingletonSet() {
        // Test when kind is TYPE_SINGLETON_SET
        JavaType concreteType = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        Class<?> rawSuper = Set.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, concreteType, rawSuper);
        
        Set<?> set = converter.convert(Collections.singleton(Boolean.TRUE));
        assertEquals(1, set.size());
        assertTrue(set.contains(Boolean.TRUE));
    }
    
    @Test
    public void testConverter_withTypeSingletonList() {
        // Test when kind is TYPE_SINGLETON_LIST
        JavaType concreteType = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        Class<?> rawSuper = List.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, concreteType, rawSuper);
        
        List<?> list = converter.convert(Collections.singletonList(Boolean.TRUE));
        assertEquals(1, list.size());
        assertTrue(list.contains(Boolean.TRUE));
    }
    
    @Test
    public void testConverter_withTypeSingletonMap() {
        // Test when kind is TYPE_SINGLETON_MAP
        JavaType concreteType = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        Class<?> rawSuper = Map.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, concreteType, rawSuper);
        
        Map<?, ?> map = converter.convert(Collections.singletonMap("a", "b"));
        assertEquals(1, map.size());
        assertTrue(map.containsKey("a"));
        assertEquals("b", map.get("a"));
    }
    
    @Test
    public void testConverter_withTypeUnmodifiableSet() {
        // Test when kind is TYPE_UNMODIFIABLE_SET
        JavaType concreteType = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);
        Class<?> rawSuper = Set.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, concreteType, rawSuper);
        
        Set<?> set = converter.convert(Collections.singleton(Boolean.TRUE));
        assertEquals(1, set.size());
        assertTrue(set.contains(Boolean.TRUE));
        
        try {
            set.add(Boolean.FALSE);
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testConverter_withTypeUnmodifiableList() {
        // Test when kind is TYPE_UNMODIFIABLE_LIST
        JavaType concreteType = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);
        Class<?> rawSuper = List.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, concreteType, rawSuper);
        
        List<?> list = converter.convert(Collections.singletonList(Boolean.TRUE));
        assertEquals(1, list.size());
        assertTrue(list.contains(Boolean.TRUE));
        
        try {
            list.add(Boolean.FALSE);
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testConverter_withTypeUnmodifiableMap() {
        // Test when kind is TYPE_UNMODIFIABLE_MAP
        JavaType concreteType = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
        Class<?> rawSuper = Map.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, concreteType, rawSuper);
        
        Map<?, ?> map = converter.convert(Collections.singletonMap("a", "b"));
        assertEquals(1, map.size());
        assertTrue(map.containsKey("a"));
        assertEquals("b", map.get("a"));
        
        try {
            map.put("c", "d");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
}
