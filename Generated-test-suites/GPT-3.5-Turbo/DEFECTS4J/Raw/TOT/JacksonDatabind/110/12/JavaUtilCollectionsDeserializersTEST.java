
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setup() {
        ctxt = new ObjectMapper().getDeserializationContext();
        type = TypeFactory.defaultInstance().constructType(String.class);
    }

    @Test
    public void testFindForCollection_WithArraysList() throws JsonMappingException {
        // Arrange
        CtMixinHelper classAsArraysList = new CtMixinHelper(Arrays.asList(1, 2, 3).getClass());

        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertEquals(StdDelegatingDeserializer.class, result.getClass());
        assertEquals(classAsArraysList.getDelegate().getClass(), ((StdDelegatingDeserializer<?>) result).getDelegatee().getClass());
    }

    @Test
    public void testFindForCollection_WithSingletonList() throws JsonMappingException {
        // Arrange
        CtMixinHelper classSingletonList = new CtMixinHelper(Collections.singletonList("singletonList").getClass());

        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertEquals(StdDelegatingDeserializer.class, result.getClass());
        assertEquals(classSingletonList.getDelegate().getClass(), ((StdDelegatingDeserializer<?>) result).getDelegatee().getClass());
    }

    @Test
    public void testFindForCollection_WithSingletonSet() throws JsonMappingException {
        // Arrange
        CtMixinHelper classSingletonSet = new CtMixinHelper(Collections.singleton("singletonSet").getClass());

        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertEquals(StdDelegatingDeserializer.class, result.getClass());
        assertEquals(classSingletonSet.getDelegate().getClass(), ((StdDelegatingDeserializer<?>) result).getDelegatee().getClass());
    }

    @Test
    public void testFindForCollection_WithUnmodifiableList() throws JsonMappingException {
        // Arrange
        CtMixinHelper classUnmodifiableList = new CtMixinHelper(Collections.unmodifiableList(Arrays.asList(1, 2, 3)).getClass());

        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertEquals(StdDelegatingDeserializer.class, result.getClass());
        assertEquals(classUnmodifiableList.getDelegate().getClass(), ((StdDelegatingDeserializer<?>) result).getDelegatee().getClass());
    }

    @Test
    public void testFindForCollection_WithUnmodifiableSet() throws JsonMappingException {
        // Arrange
        CtMixinHelper classUnmodifiableSet = new CtMixinHelper(Collections.unmodifiableSet(new HashSet<>(Arrays.asList("unmodifableSet"))).getClass());

        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertEquals(StdDelegatingDeserializer.class, result.getClass());
        assertEquals(classUnmodifiableSet.getDelegate().getClass(), ((StdDelegatingDeserializer<?>) result).getDelegatee().getClass());
    }

    @Test
    public void testFindForCollection_WithInvalidType() throws JsonMappingException {
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNull(result);
    }

    @Test
    public void testFindForMap_WithSingletonMap() throws JsonMappingException {
        // Arrange
        Map<String, String> map = Collections.singletonMap("key", "value");
        CtMixinHelper classSingletonMap = new CtMixinHelper(map.getClass());

        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        // Assert
        assertEquals(StdDelegatingDeserializer.class, result.getClass());
        assertEquals(classSingletonMap.getDelegate().getClass(), ((StdDelegatingDeserializer<?>) result).getDelegatee().getClass());
    }

    @Test
    public void testFindForMap_WithUnmodifiableMap() throws JsonMappingException {
        // Arrange
        Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
        CtMixinHelper classUnmodifiableMap = new CtMixinHelper(map.getClass());

        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        // Assert
        assertEquals(StdDelegatingDeserializer.class, result.getClass());
        assertEquals(classUnmodifiableMap.getDelegate().getClass(), ((StdDelegatingDeserializer<?>) result).getDelegatee().getClass());
    }

    @Test
    public void testFindForMap_WithInvalidType() throws JsonMappingException {
        // Act
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        // Assert
        assertNull(result);
    }

    private static class CtMixinHelper {

        private final Class<?> delegate;

        private CtMixinHelper(Class<?> delegate) {
            this.delegate = delegate;
        }

        public Class<?> getDelegate() {
            return delegate;
        }
    }
}
