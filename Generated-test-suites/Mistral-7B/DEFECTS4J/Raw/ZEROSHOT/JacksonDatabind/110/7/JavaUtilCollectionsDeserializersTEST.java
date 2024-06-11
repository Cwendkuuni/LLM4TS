package com.yourpackage; // replace with your package name

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType type = mock(JavaType.class);
        when(type.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET)).thenReturn(true);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType type = mock(JavaType.class);
        when(type.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST)).thenReturn(true);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType type = mock(JavaType.class);
        when(type.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET)).thenReturn(true);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType type = mock(JavaType.class);
        when(type.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST)).thenReturn(true);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType type = mock(JavaType.class);
        when(type.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP)).thenReturn(true);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType type = mock(JavaType.class);
        when(type.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP)).thenReturn(true);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testConvert_SingletonSet() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType inputType = mock(JavaType.class);
        when(inputType.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET)).thenReturn(true);
        JavaType outputType = mock(JavaType.class);
        when(typeFactory.constructCollectionType(List.class, String.class)).thenReturn(outputType);

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);
        Object result = converter.convert(Collections.singleton("test"));
        assertNotNull(result);
        assertEquals(Collections.singletonMap("test", "test"), result);
    }

    @Test
    public void testConvert_SingletonList() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType inputType = mock(JavaType.class);
        when(inputType.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST)).thenReturn(true);
        JavaType outputType = mock(JavaType.class);
        when(typeFactory.constructCollectionType(List.class, String.class)).thenReturn(outputType);

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(7, inputType);
        Object result = converter.convert(Collections.singletonList("test"));
        assertNotNull(result);
        assertEquals(Collections.singletonList("test"), result);
    }

    @Test
    public void testConvert_SingletonMap() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType inputType = mock(JavaType.class);
        when(inputType.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP)).thenReturn(true);
        JavaType outputType = mock(JavaType.class);
        when(typeFactory.constructCollectionType(Map.class, String.class, String.class)).thenReturn(outputType);

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(3, inputType);
        Object result = converter.convert(Collections.singletonMap("testKey", "testValue"));
        assertNotNull(result);
        assertEquals(Collections.singletonMap("testKey", "testValue"), result);
    }

    @Test
    public void testConvert_UnmodifiableSet() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType inputType = mock(JavaType.class);
        when(inputType.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET)).thenReturn(true);
        JavaType outputType = mock(JavaType.class);
        when(typeFactory.constructCollectionType(Set.class, String.class)).thenReturn(outputType);

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);
        Object result = converter.convert(Collections.unmodifiableSet(Collections.singleton("test")));
        assertNotNull(result);
        assertEquals(Collections.unmodifiableSet(Collections.singleton("test")), result);
    }

    @Test
    public void testConvert_UnmodifiableList() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType inputType = mock(JavaType.class);
        when(inputType.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST)).thenReturn(true);
        JavaType outputType = mock(JavaType.class);
        when(typeFactory.constructCollectionType(List.class, String.class)).thenReturn(outputType);

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(7, inputType);
        Object result = converter.convert(Collections.unmodifiableList(Collections.singletonList("test")));
        assertNotNull(result);
        assertEquals(Collections.unmodifiableList(Collections.singletonList("test")), result);
    }

    @Test
    public void testConvert_UnmodifiableMap() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType inputType = mock(JavaType.class);
        when(inputType.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP)).thenReturn(true);
        JavaType outputType = mock(JavaType.class);
        when(typeFactory.constructCollectionType(Map.class, String.class, String.class)).thenReturn(outputType);

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(3, inputType);
        Object result = converter.convert(Collections.unmodifiableMap(Collections.singletonMap("testKey", "testValue")));
        assertNotNull(result);
        assertEquals(Collections.unmodifiableMap(Collections.singletonMap("testKey", "testValue")), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_WrongCollectionType() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType inputType = mock(JavaType.class);
        when(inputType.hasRawClass(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP)).thenReturn(false);
        JavaType outputType = mock(JavaType.class);
        when(typeFactory.constructCollectionType(Map.class, String.class, String.class)).thenReturn(outputType);

        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(3, inputType);
        Object result = converter.convert(Collections.unmodifiableMap(Collections.singletonMap("testKey", "testValue")));
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected instanceof Collection && actual instanceof Collection) {
            assertEquals(((Collection) expected).size(), ((Collection) actual).size());
            for (Object e : (Collection) expected) {
                assertTrue( ((Collection) actual).contains(e) );
            }
        } else {
            super.assertEquals(expected, actual);
        }
    }
}