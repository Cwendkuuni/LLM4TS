
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        Converter<Object,Object> converter = ((StdDelegatingDeserializer<?>) deserializer).getConverter();
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter collectionsConverter 
                = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter;
        
        assertEquals(7, collectionsConverter._kind);
        assertEquals(type, collectionsConverter._inputType);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JavaType type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        Converter<Object,Object> converter = ((StdDelegatingDeserializer<?>) deserializer).getConverter();
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter collectionsConverter 
                = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter;
        
        assertEquals(3, collectionsConverter._kind);
        assertEquals(type, collectionsConverter._inputType);
    }

    @Test
    public void testConverter_SingletonSet() {
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(Set.class, String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);
        
        Set<String> value = Collections.singleton("test");
        Object convertedValue = converter.convert(value);

        assertTrue(convertedValue instanceof Set);
        Set<?> set = (Set<?>) convertedValue;
        assertEquals(1, set.size());
        assertTrue(set.contains("test"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConverter_SingletonSet_InvalidSize() {
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(Set.class, String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);
        
        Set<String> value = Collections.singleton("test1");
        converter.convert(value);
    }

    @Test
    public void testConverter_SingletonList() {
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(2, inputType);
        
        List<String> value = Collections.singletonList("test");
        Object convertedValue = converter.convert(value);

        assertTrue(convertedValue instanceof List);
        List<?> list = (List<?>) convertedValue;
        assertEquals(1, list.size());
        assertEquals("test", list.get(0));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConverter_SingletonList_InvalidSize() {
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(2, inputType);
        
        List<String> value = Collections.singletonList("test1");
        converter.convert(value);
    }

    @Test
    public void testConverter_SingletonMap() {
        JavaType inputType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(3, inputType);
        
        Map<String, Integer> value = Collections.singletonMap("test", 1);
        Object convertedValue = converter.convert(value);

        assertTrue(convertedValue instanceof Map);
        Map<?, ?> map = (Map<?, ?>) convertedValue;
        assertEquals(1, map.size());
        assertEquals(1, map.get("test"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConverter_SingletonMap_InvalidSize() {
        JavaType inputType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(3, inputType);
        
        Map<String, Integer> value = Collections.singletonMap("test1", 1);
        converter.convert(value);
    }

    @Test
    public void testConverter_UnmodifiableSet() {
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(Set.class, String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(4, inputType);
        
        Set<String> value = Collections.unmodifiableSet(Collections.singleton("test"));
        Object convertedValue = converter.convert(value);

        assertTrue(convertedValue instanceof Set);
        Set<?> set = (Set<?>) convertedValue;
        assertEquals(1, set.size());
        assertTrue(set.contains("test"));

        // Check if the returned set is unmodifiable
        try {
            set.add("test2");
        } catch (UnsupportedOperationException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        } catch (Exception e) {
            assertFalse(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testConverter_UnmodifiableList() {
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(5, inputType);
        
        List<String> value = Collections.unmodifiableList(Collections.singletonList("test"));
        Object convertedValue = converter.convert(value);

        assertTrue(convertedValue instanceof List);
        List<?> list = (List<?>) convertedValue;
        assertEquals(1, list.size());
        assertEquals("test", list.get(0));

        // Check if the returned list is unmodifiable
        try {
            list.add("test2");
        } catch (UnsupportedOperationException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        } catch (Exception e) {
            assertFalse(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testConverter_UnmodifiableMap() {
        JavaType inputType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(6, inputType);
        
        Map<String, Integer> value = Collections.unmodifiableMap(Collections.singletonMap("test", 1));
        Object convertedValue = converter.convert(value);

        assertTrue(convertedValue instanceof Map);
        Map<?, ?> map = (Map<?, ?>) convertedValue;
        assertEquals(1, map.size());
        assertEquals(1, map.get("test"));

        // Check if the returned map is unmodifiable
        try {
            map.put("test2", 2);
        } catch (UnsupportedOperationException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        } catch (Exception e) {
            assertFalse(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testConverter_AsList() {
        JavaType inputType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter 
                = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(7, inputType);
        
        List<String> value = Collections.nCopies(5, "test");
        Object convertedValue = converter.convert(value);

        assertTrue(convertedValue instanceof List);
        List<?> list = (List<?>) convertedValue;
        assertEquals(5, list.size());
        assertTrue(list.contains("test"));
    }
}
