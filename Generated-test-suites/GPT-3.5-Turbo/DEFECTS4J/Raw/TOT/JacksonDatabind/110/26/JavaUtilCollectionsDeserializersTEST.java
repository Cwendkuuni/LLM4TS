import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;

public class JavaUtilCollectionsDeserializersTest {
    
    @Test
    public void testFindForCollection_TypeAsArraysList_ReturnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.builder().build();
        
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForCollection_TypeSingletonList_ReturnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.builder().build();
        
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForCollection_TypeSingletonSet_ReturnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.builder().build();
        
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForCollection_TypeUnmodifiableList_ReturnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.builder().build();
        
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForCollection_TypeUnmodifiableSet_ReturnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.builder().build();
        
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForCollection_TypeOther_ReturnsNull() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.builder().build();
        
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testFindForMap_TypeSingletonMap_ReturnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.builder().build();
        
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        // Assert
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForMap_TypeUnmodifiableMap_ReturnsStdDelegatingDeserializer() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.builder().build();
        
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        // Assert
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForMap_TypeOther_ReturnsNull() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.builder().build();
        
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        // Assert
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaUtilCollectionsConverter_Convert_TypeSingletonSetWithMultipleEntries_ThrowsIllegalArgumentException() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        
        // Act
        converter.convert(null);
    }
    
    @Test
    public void testJavaUtilCollectionsConverter_Convert_TypeSingletonSetWithOneEntry_ReturnsSingletonSet() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        Set<?> set = new HashSet<>();
        set.add("entry");
        
        // Act
        Object result = converter.convert(set);
        
        // Assert
        assertTrue(result instanceof Set);
        assertEquals(1, ((Set<?>) result).size());
        assertTrue(((Set<?>) result).contains("entry"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testJavaUtilCollectionsConverter_Convert_TypeSingletonListWithMultipleEntries_ThrowsIllegalArgumentException() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);
        
        // Act
        converter.convert(null);
    }
    
    @Test
    public void testJavaUtilCollectionsConverter_Convert_TypeSingletonListWithOneEntry_ReturnsSingletonList() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);
        List<?> list = new ArrayList<>();
        list.add("entry");
        
        // Act
        Object result = converter.convert(list);
        
        // Assert
        assertTrue(result instanceof List);
        assertEquals(1, ((List<?>) result).size());
        assertTrue(((List<?>) result).contains("entry"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testJavaUtilCollectionsConverter_Convert_TypeSingletonMapWithMultipleEntries_ThrowsIllegalArgumentException() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null);
        
        // Act
        converter.convert(null);
    }
    
    @Test
    public void testJavaUtilCollectionsConverter_Convert_TypeSingletonMapWithOneEntry_ReturnsSingletonMap() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null);
        Map<?, ?> map = new HashMap<>();
        map.put("key", "value");
        
        // Act
        Object result = converter.convert(map);
        
        // Assert
        assertTrue(result instanceof Map);
        assertEquals(1, ((Map<?, ?>) result).size());
        assertTrue(((Map<?, ?>) result).containsKey("key"));
        assertEquals("value", ((Map<?, ?>) result).get("key"));
    }
    
    @Test
    public void testJavaUtilCollectionsConverter_Convert_TypeUnmodifiableSet_ReturnsUnmodifiableSet() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, null);
        Set<?> set = new HashSet<>();
        set.add("entry");
        
        // Act
        Object result = converter.convert(Collections.unmodifiableSet(set));
        
        // Assert
        assertTrue(result instanceof Set);
        assertEquals(1, ((Set<?>) result).size());
        assertTrue(((Set<?>) result).contains("entry"));
        
        try {
            ((Set<?>) result).add("newEntry");
            fail("Expected UnsupportedOperationException not thrown.");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
    
    @Test
    public void testJavaUtilCollectionsConverter_Convert_TypeUnmodifiableList_ReturnsUnmodifiableList() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, null);
        List<?> list = new ArrayList<>();
        list.add("entry");
        
        // Act
        Object result = converter.convert(Collections.unmodifiableList(list));
        
        // Assert
        assertTrue(result instanceof List);
        assertEquals(1, ((List<?>) result).size());
        assertTrue(((List<?>) result).contains("entry"));
        
        try {
            ((List<?>) result).add("newEntry");
            fail("Expected UnsupportedOperationException not thrown.");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
    
    @Test
    public void testJavaUtilCollectionsConverter_Convert_TypeUnmodifiableMap_ReturnsUnmodifiableMap() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, null);
        Map<?, ?> map = new HashMap<>();
        map.put("key", "value");
        
        // Act
        Object result = converter.convert(Collections.unmodifiableMap(map));
        
        // Assert
        assertTrue(result instanceof Map);
        assertEquals(1, ((Map<?, ?>) result).size());
        assertTrue(((Map<?, ?>) result).containsKey("key"));
        assertEquals("value", ((Map<?, ?>) result).get("key"));
        
        try {
            ((Map<?, ?>) result).put("newKey", "newValue");
            fail("Expected UnsupportedOperationException not thrown.");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }
}