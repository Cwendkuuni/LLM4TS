
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JavaType javaType;

    @Before
    public void setUp() {
        deserializationContext = mock(DeserializationContext.class);
        javaType = mock(JavaType.class);
    }

    @Test
    public void testFindForCollection() throws JsonMappingException {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(deserializationContext,
                javaType);

        assertNull(deserializer);

        when(javaType.hasRawClass(List.class)).thenReturn(true);
        when(javaType.getRawClass()).thenReturn(List.class);
        when(javaType.findSuperType(List.class)).thenReturn(javaType);

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(deserializationContext, javaType);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> delegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        Converter<?, ?> converter = delegatingDeserializer.getConverter();
        assertNotNull(converter);
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter collectionsConverter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter;
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, collectionsConverter._kind);
        assertSame(javaType, collectionsConverter._inputType);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(deserializationContext,
                javaType);

        assertNull(deserializer);

        when(javaType.hasRawClass(Map.class)).thenReturn(true);
        when(javaType.getRawClass()).thenReturn(Map.class);
        when(javaType.findSuperType(Map.class)).thenReturn(javaType);

        deserializer = JavaUtilCollectionsDeserializers.findForMap(deserializationContext, javaType);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        StdDelegatingDeserializer<?> delegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        Converter<?, ?> converter = delegatingDeserializer.getConverter();
        assertNotNull(converter);
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter collectionsConverter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter;
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, collectionsConverter._kind);
        assertSame(javaType, collectionsConverter._inputType);
    }

    @Test
    public void testConverter() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaType);

        assertNotNull(converter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, converter._kind);
        assertSame(javaType, converter._inputType);

        Object value = new HashSet<>();
        Object convertedValue = converter.convert(value);
        assertTrue(convertedValue instanceof Set<?>);
        assertEquals(1, ((Set<?>) convertedValue).size());
        assertSame(value, ((Set<?>) convertedValue).iterator().next());

        TypeFactory typeFactory = mock(TypeFactory.class);
        assertSame(javaType, converter.getInputType(typeFactory));
        assertSame(javaType, converter.getOutputType(typeFactory));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverterCheckSingleton() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(
                JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaType);

        assertNotNull(converter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, converter._kind);
        assertSame(javaType, converter._inputType);

        Object value = new HashSet<>();
        converter._checkSingleton(((Set<?>) value).size());
    }
}
