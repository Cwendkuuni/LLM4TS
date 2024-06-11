
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUtilCollectionsDeserializersTest {

    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        ctxt = new DeserializationContext();
        type = TypeFactory.defaultInstance().constructType(List.class);
    }

    @Test
    public void testFindForCollection_WhenTypeIsArraysList_ThenReturnStdDelegatingDeserializerForAsList() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_AS_ARRAYS_LIST);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        Converter<?, ?> converter = ((StdDelegatingDeserializer<?>) result).getConverter();
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getKind());
        assertSame(type, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getInputType(null));
    }

    @Test
    public void testFindForCollection_WhenTypeIsSingletonList_ThenReturnStdDelegatingDeserializerForSingletonList() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        Converter<?, ?> converter = ((StdDelegatingDeserializer<?>) result).getConverter();
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getKind());
        assertSame(type, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getInputType(null));
    }

    @Test
    public void testFindForCollection_WhenTypeIsSingletonSet_ThenReturnStdDelegatingDeserializerForSingletonSet() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        Converter<?, ?> converter = ((StdDelegatingDeserializer<?>) result).getConverter();
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getKind());
        assertSame(type, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getInputType(null));
    }

    @Test
    public void testFindForCollection_WhenTypeIsUnmodifiableList_ThenReturnStdDelegatingDeserializerForUnmodifiableList() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        Converter<?, ?> converter = ((StdDelegatingDeserializer<?>) result).getConverter();
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getKind());
        assertSame(type, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getInputType(null));
    }

    @Test
    public void testFindForCollection_WhenTypeIsUnmodifiableSet_ThenReturnStdDelegatingDeserializerForUnmodifiableSet() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        Converter<?, ?> converter = ((StdDelegatingDeserializer<?>) result).getConverter();
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getKind());
        assertSame(type, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getInputType(null));
    }

    @Test
    public void testFindForCollection_WhenTypeIsNotSpecialContainer_ThenReturnNull() throws JsonMappingException {
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

        assertNull(result);
    }

    @Test
    public void testFindForMap_WhenTypeIsSingletonMap_ThenReturnStdDelegatingDeserializerForSingletonMap() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        Converter<?, ?> converter = ((StdDelegatingDeserializer<?>) result).getConverter();
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getKind());
        assertSame(type, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getInputType(null));
    }

    @Test
    public void testFindForMap_WhenTypeIsUnmodifiableMap_ThenReturnStdDelegatingDeserializerForUnmodifiableMap() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);

        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        assertTrue(result instanceof StdDelegatingDeserializer);
        Converter<?, ?> converter = ((StdDelegatingDeserializer<?>) result).getConverter();
        assertTrue(converter instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getKind());
        assertSame(type, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter).getInputType(null));
    }

    @Test
    public void testFindForMap_WhenTypeIsNotSpecialContainer_ThenReturnNull() throws JsonMappingException {
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

        assertNull(result);
    }

}
