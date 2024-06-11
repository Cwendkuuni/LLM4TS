
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void findForCollection_ReturnsDeserializer_ForArraysList() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(Object.class);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void findForCollection_ReturnsDeserializer_ForSingletonList() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void findForCollection_ReturnsDeserializer_ForSingletonSet() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void findForCollection_ReturnsDeserializer_ForUnmodifiableList() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void findForCollection_ReturnsDeserializer_ForUnmodifiableSet() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void findForCollection_ReturnsNull_ForUnknownType() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        // Assert
        assertNull(deserializer);
    }
    
    @Test
    public void findForMap_ReturnsDeserializer_ForSingletonMap() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void findForMap_ReturnsDeserializer_ForUnmodifiableMap() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void findForMap_ReturnsNull_ForUnknownType() {
        // Arrange
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        // Act
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        // Assert
        assertNull(deserializer);
    }
    
    @Test
    public void converter_CreatesJavaUtilCollectionsConverter() {
        // Arrange
        int kind = 1;
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Object.class);
        
        // Act
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(kind, concreteType, List.class);
        
        // Assert
        assertNotNull(converter);
        assertEquals(kind, converter._kind);
        assertEquals(concreteType, converter._inputType);
    }
    
    @Test
    public void convert_ReturnsUnmodifiableSet_ForTypeUnmodifiableSet() {
        // Arrange
        Object value = Collections.singleton("Test");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, null);
        
        // Act
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertTrue(result instanceof UnmodifiableSet);
    }
    
    @Test
    public void convert_ReturnsUnmodifiableList_ForTypeUnmodifiableList() {
        // Arrange
        Object value = Collections.singletonList("Test");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, null);
        
        // Act
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(result instanceof UnmodifiableList);
    }
    
    @Test
    public void convert_ReturnsUnmodifiableMap_ForTypeUnmodifiableMap() {
        // Arrange
        Object value = Collections.singletonMap("Key", "Value");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, null);
        
        // Act
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Map);
        assertTrue(result instanceof UnmodifiableMap);
    }
    
    @Test
    public void convert_ReturnsSingletonSet_ForTypeSingletonSet() {
        // Arrange
        Object value = Collections.singleton("Test");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        
        // Act
        Object result = converter.convert(value);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertEquals(1, ((Set) result).size());
        assertTrue(((Set) result).contains("Test"));
    }
    
    @Test
    public void convert_ReturnsSingletonList_ForTypeSingletonList() {
        // Arrange
        Object value = Collections.singletonList("Test");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);
        
        // Act
        Object result = converter.convert(value);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertEquals(1, ((List) result).size());
        assertTrue(((List) result).contains("Test"));
    }
    
    @Test
    public void convert_ReturnsSingletonMap_ForTypeSingletonMap() {
        // Arrange
        Object value = Collections.singletonMap("Key", "Value");
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null);
        
        // Act
        Object result = converter.convert(value);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Map);
        assertEquals(1, ((Map) result).size());
        assertTrue(((Map) result).containsKey("Key"));
        assertTrue(((Map) result).containsValue("Value"));
    }
    
    @Test
    public void convert_ReturnsValue_AsIs_ForTypeAsList() {
        // Arrange
        Object value = "Test";
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, null);
        
        // Act
        Object result = converter.convert(value);

        // Assert
        assertEquals(value, result);
    }
    
    @Test
    public void getInputType_ReturnsInputType() {
        // Arrange
        JavaType inputType = TypeFactory.defaultInstance().constructType(Object.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);

        // Act
        JavaType result = converter.getInputType(TypeFactory.defaultInstance());

        // Assert
        assertEquals(inputType, result);
    }
    
    @Test
    public void getOutputType_ReturnsInputType() {
        // Arrange
        JavaType inputType = TypeFactory.defaultInstance().constructType(Object.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);

        // Act
        JavaType result = converter.getOutputType(TypeFactory.defaultInstance());

        // Assert
        assertEquals(inputType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void _checkSingleton_ThrowsIllegalArgumentException_ForSizeNotEqualToOne() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);

        // Act
        converter._checkSingleton(2);
    }

    @Test
    public void _checkSingleton_DoesNotThrowException_ForSizeEqualToOne() {
        // Arrange
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);

        // Act
        converter._checkSingleton(1);

        // Assert
        // no exception thrown
    }
}

