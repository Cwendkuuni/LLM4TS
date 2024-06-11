
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testFindForCollection() {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        // Test with Collections.asLis(Class)
        JavaType asListType = TypeFactory.defaultInstance().constructType(Holder.class);
        JsonDeserializer<?> asListDeserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, asListType);
        
        assertTrue(asListDeserializer instanceof StdDelegatingDeserializer<?>);
        assertTrue(((StdDelegatingDeserializer<?>) asListDeserializer).getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) ((StdDelegatingDeserializer<?>) asListDeserializer).getDelegatee())._kind);
        
        // Test with Collections.singletonList(Class)
        JavaType singletonListType = TypeFactory.defaultInstance().constructType(java.util.concurrent.CopyOnWriteArrayList.class);
        JsonDeserializer<?> singletonListDeserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, singletonListType);
        
        assertTrue(singletonListDeserializer instanceof StdDelegatingDeserializer<?>);
        assertTrue(((StdDelegatingDeserializer<?>) singletonListDeserializer).getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) ((StdDelegatingDeserializer<?>) singletonListDeserializer).getDelegatee())._kind);
        
        // Test with Collections.singleton(Class)
        JavaType singletonSetType = TypeFactory.defaultInstance().constructType(java.util.concurrent.CopyOnWriteArraySet.class);
        JsonDeserializer<?> singletonSetDeserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, singletonSetType);
        
        assertTrue(singletonSetDeserializer instanceof StdDelegatingDeserializer<?>);
        assertTrue(((StdDelegatingDeserializer<?>) singletonSetDeserializer).getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) ((StdDelegatingDeserializer<?>) singletonSetDeserializer).getDelegatee())._kind);
        
        // Test with Collections.unmodifiableList(Class)
        JavaType unmodifiableListType = TypeFactory.defaultInstance().constructType(java.util.SortedSet.class);
        JsonDeserializer<?> unmodifiableListDeserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, unmodifiableListType);
        
        assertTrue(unmodifiableListDeserializer instanceof StdDelegatingDeserializer<?>);
        assertTrue(((StdDelegatingDeserializer<?>) unmodifiableListDeserializer).getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) ((StdDelegatingDeserializer<?>) unmodifiableListDeserializer).getDelegatee())._kind);
        
        // Test with Collections.unmodifiableSet(Class)
        JavaType unmodifiableSetType = TypeFactory.defaultInstance().constructType(java.util.HashSet.class);
        JsonDeserializer<?> unmodifiableSetDeserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, unmodifiableSetType);
        
        assertTrue(unmodifiableSetDeserializer instanceof StdDelegatingDeserializer<?>);
        assertTrue(((StdDelegatingDeserializer<?>) unmodifiableSetDeserializer).getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) ((StdDelegatingDeserializer<?>) unmodifiableSetDeserializer).getDelegatee())._kind);
        
        // Test with type that does not match any special containers
        JavaType nonSpecialContainerType = TypeFactory.defaultInstance().constructType(java.util.List.class);
        JsonDeserializer<?> nonSpecialContainerDeserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, nonSpecialContainerType);
        
        assertNull(nonSpecialContainerDeserializer);
    }
    
    @Test
    public void testFindForMap() {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        // Test with Collections.singletonMap(key, value)
        JavaType singletonMapType = TypeFactory.defaultInstance().constructType(MapEntry.class);
        JsonDeserializer<?> singletonMapDeserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, singletonMapType);
        
        assertTrue(singletonMapDeserializer instanceof StdDelegatingDeserializer<?>);
        assertTrue(((StdDelegatingDeserializer<?>) singletonMapDeserializer).getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) ((StdDelegatingDeserializer<?>) singletonMapDeserializer).getDelegatee())._kind);
        
        // Test with Collections.unmodifiableMap(Collections.singletonMap(key, value))
        JavaType unmodifiableMapType = TypeFactory.defaultInstance().constructType(java.util.NavigableMap.class);
        JsonDeserializer<?> unmodifiableMapDeserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, unmodifiableMapType);
        
        assertTrue(unmodifiableMapDeserializer instanceof StdDelegatingDeserializer<?>);
        assertTrue(((StdDelegatingDeserializer<?>) unmodifiableMapDeserializer).getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) ((StdDelegatingDeserializer<?>) unmodifiableMapDeserializer).getDelegatee())._kind);
        
        // Test with type that does not match any special containers
        JavaType nonSpecialMapType = TypeFactory.defaultInstance().constructType(java.util.HashMap.class);
        JsonDeserializer<?> nonSpecialMapDeserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, nonSpecialMapType);
        
        assertNull(nonSpecialMapDeserializer);
    }
    
    private class Holder {
        public List<?> list;
    }
    
    private class MapEntry {
        public Map<?, ?> map;
    }

}
