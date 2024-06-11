
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        // Test for CLASS_AS_ARRAYS_LIST
        type = TypeFactory.defaultInstance().constructType(Arrays.asList(null, null).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);

        // Test for CLASS_SINGLETON_LIST
        type = TypeFactory.defaultInstance().constructType(Collections.singletonList(Boolean.TRUE).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);

        // Test for CLASS_SINGLETON_SET
        type = TypeFactory.defaultInstance().constructType(Collections.singleton(Boolean.TRUE).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
        
        // Test for CLASS_UNMODIFIABLE_LIST
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
        
        // Test for CLASS_UNMODIFIABLE_SET
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
    }

    @Test
    public void testFindForMap() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        // Test for CLASS_SINGLETON_MAP
        type = TypeFactory.defaultInstance().constructType(Collections.singletonMap("a", "b").getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);

        // Test for CLASS_UNMODIFIABLE_MAP
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConvert_SingletonSet_SizeNotOne() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        Set<?> set = new HashSet<>();
        converter.convert(set);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConvert_SingletonList_SizeNotOne() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);
        List<?> list = new ArrayList<>();
        converter.convert(list);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConvert_SingletonMap_SizeNotOne() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null);
        Map<?, ?> map = new HashMap<>();
        converter.convert(map);
    }

    @Test
    public void testConvert_UnmodifiableSet() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, null);
        Set<?> set = new HashSet<>();
        Object result = converter.convert(set);
        assertNotNull(result);
        assertTrue(result instanceof Set<?>);
    }

    @Test
    public void testConvert_UnmodifiableList() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, null);
        List<?> list = new ArrayList<>();
        Object result = converter.convert(list);
        assertNotNull(result);
        assertTrue(result instanceof List<?>);
    }

    @Test
    public void testConvert_UnmodifiableMap() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                        JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, null);
        Map<?, ?> map = new HashMap<>();
        Object result = converter.convert(map);
        assertNotNull(result);
        assertTrue(result instanceof Map<?, ?>);
    }
}
