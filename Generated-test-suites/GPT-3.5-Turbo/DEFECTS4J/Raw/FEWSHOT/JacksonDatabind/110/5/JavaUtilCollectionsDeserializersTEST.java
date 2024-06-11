import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_AsArraysList() {
        assertTrue(JavaUtilCollectionsDeserializers.findForCollection(null, null) instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_SingletonList() {
        assertTrue(JavaUtilCollectionsDeserializers.findForCollection(null, null) instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_SingletonSet() {
        assertTrue(JavaUtilCollectionsDeserializers.findForCollection(null, null) instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_UnmodifiableList() {
        assertTrue(JavaUtilCollectionsDeserializers.findForCollection(null, null) instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_UnmodifiableSet() {
        assertTrue(JavaUtilCollectionsDeserializers.findForCollection(null, null) instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForCollection_Null() {
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));
    }
    
    @Test
    public void testFindForMap_SingletonMap() {
        assertTrue(JavaUtilCollectionsDeserializers.findForMap(null, null) instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForMap_UnmodifiableMap() {
        assertTrue(JavaUtilCollectionsDeserializers.findForMap(null, null) instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForMap_Null() {
        assertNull(JavaUtilCollectionsDeserializers.findForMap(null, null));
    }
    
    @Test
    public void testConverter_SingletonSet() {
        Map<?, ?> map = new HashMap<>();
        map.put("a", "b");
        assertEquals(Collections.singleton("b"), new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null).convert(map));
    }
    
    @Test
    public void testConverter_SingletonList() {
        List<?> list = Collections.singletonList("test");
        assertEquals(Collections.singletonList("test"), new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null).convert(list));
    }
    
    @Test
    public void testConverter_SingletonMap() {
        Map<?, ?> map = new HashMap<>();
        map.put("a", "b");
        assertEquals(Collections.singletonMap("a", "b"), new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null).convert(map));
    }
    
    @Test
    public void testConverter_UnmodifiableSet() {
        Set<?> set = Collections.singleton("test");
        assertEquals(Collections.unmodifiableSet(set), new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, null).convert(set));
    }
    
    @Test
    public void testConverter_UnmodifiableList() {
        List<?> list = Collections.singletonList("test");
        assertEquals(Collections.unmodifiableList(list), new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, null).convert(list));
    }
    
    @Test
    public void testConverter_UnmodifiableMap() {
        Map<?, ?> map = Collections.singletonMap("a", "b");
        assertEquals(Collections.unmodifiableMap(map), new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, null).convert(map));
    }
    
    @Test
    public void testConverter_AsList() {
        List<?> list = Collections.singletonList("test");
        assertEquals(list, new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, null).convert(list));
    }
}