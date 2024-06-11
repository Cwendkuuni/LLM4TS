import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_singletonSet() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> stdDelegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(stdDelegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) stdDelegatingDeserializer.getDelegatee();
        Object result = converter.convert(Collections.singleton(42));
        assertTrue(result instanceof Set);
        assertEquals(1, ((Set<?>) result).size());
        assertEquals(42, ((Set<?>) result).iterator().next());
    }

    @Test
    public void testFindForCollection_singletonList() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> stdDelegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(stdDelegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) stdDelegatingDeserializer.getDelegatee();
        Object result = converter.convert(Collections.singletonList(42));
        assertTrue(result instanceof List);
        assertEquals(1, ((List<?>) result).size());
        assertEquals(42, ((List<?>) result).get(0));
    }

    @Test
    public void testFindForCollection_singletonMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> stdDelegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(stdDelegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) stdDelegatingDeserializer.getDelegatee();
        Object result = converter.convert(Collections.singletonMap("key", "value"));
        assertTrue(result instanceof Map);
        assertEquals(1, ((Map<?, ?>) result).size());
        assertEquals("value", ((Map<?, ?>) result).get("key"));
    }

    @Test
    public void testFindForCollection_unmodifiableSet() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> stdDelegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(stdDelegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) stdDelegatingDeserializer.getDelegatee();
        Object result = converter.convert(Collections.unmodifiableSet(Collections.singleton(42)));
        assertTrue(result instanceof Set);
        assertEquals(1, ((Set<?>) result).size());
        assertEquals(42, ((Set<?>) result).iterator().next());
    }

    @Test
    public void testFindForCollection_unmodifiableList() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> stdDelegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(stdDelegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) stdDelegatingDeserializer.getDelegatee();
        Object result = converter.convert(Collections.unmodifiableList(Collections.singletonList(42)));
        assertTrue(result instanceof List);
        assertEquals(1, ((List<?>) result).size());
        assertEquals(42, ((List<?>) result).get(0));
    }

    @Test
    public void testFindForMap_singletonMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> stdDelegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(stdDelegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) stdDelegatingDeserializer.getDelegatee();
        Object result = converter.convert(Collections.singletonMap("key", "value"));
        assertTrue(result instanceof Map);
        assertEquals(1, ((Map<?, ?>) result).size());
        assertEquals("value", ((Map<?, ?>) result).get("key"));
    }

    @Test
    public void testFindForMap_unmodifiableMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> stdDelegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(stdDelegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) stdDelegatingDeserializer.getDelegatee();
        Object result = converter.convert(Collections.unmodifiableMap(Collections.singletonMap("key", "value")));
        assertTrue(result instanceof Map);
        assertEquals(1, ((Map<?, ?>) result).size());
        assertEquals("value", ((Map<?, ?>) result).get("key"));
    }

    @Test
    public void testFindForCollection_invalidType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_invalidType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = TypeFactory.defaultInstance().constructType(Set.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(deserializer);
    }

}