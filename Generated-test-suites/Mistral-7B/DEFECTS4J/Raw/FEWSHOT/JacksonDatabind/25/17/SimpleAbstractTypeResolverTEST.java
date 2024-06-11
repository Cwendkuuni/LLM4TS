package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test adding mappings
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        // Test finding type mappings
        DeserializationConfig config = new DeserializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());

        type = mapper.getTypeFactory().constructType(AbstractMap.SimpleImmutableEntry.class);
        resolvedType = resolver.findTypeMapping(config, type);
        assertNull(resolvedType);

        type = mapper.getTypeFactory().constructType(HashMap.class);
        resolvedType = resolver.findTypeMapping(config, type);
        assertNotNull(resolvedType);
        assertEquals(LinkedHashMap.class, resolvedType.getRawClass());

        // Test resolveAbstractType
        JavaType abstractType = mapper.getTypeFactory().constructType(AbstractList.class);
        JavaType resolvedAbstractType = resolver.resolveAbstractType(config, abstractType);
        assertNull(resolvedAbstractType);

        // Test multiple mappings
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, ArrayList.class);
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType arrayListType = mapper.getTypeFactory().constructType(ArrayList.class);
        JavaType linkedListType = mapper.getTypeFactory().constructType(LinkedList.class);
        resolvedType = resolver.findTypeMapping(config, arrayListType);
        assertEquals(ArrayList.class, resolvedType.getRawClass());
        resolvedType = resolver.findTypeMapping(config, linkedListType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());

        // Test TypeFactory.constructType with ClassIdType
        Map<String, Class<?>> classes = new HashMap<>();
        classes.put("List", ArrayList.class);
        classes.put("LinkedList", LinkedList.class);
        TypeFactory typeFactory = mapper.getTypeFactory().getAnnotationIntrospector().findValueType(new ClassKey(AbstractList.class), classes);
        JavaType typeWithClassId = mapper.getTypeFactory().constructType(typeFactory);
        resolvedType = resolver.findTypeMapping(config, typeWithClassId);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }
}