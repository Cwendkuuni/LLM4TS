package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMappings() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        Map<ClassKey, Class<?>> mappings = resolver.getMappings();
        assertEquals(2, mappings.size());
        assertEquals(Collection.class, mappings.get(new ClassKey(Collection.class)).getRawClass());
        assertEquals(LinkedList.class, mappings.get(new ClassKey(List.class)).getRawClass());
    }

    @Test
    public void testFindTypeMapping() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().setAbstractTypeResolver(resolver));

        JavaType javaType = mapper.getTypeFactory().constructType(ArrayList.class);
        JavaType mappedType = resolver.findTypeMapping(mapper.getConfig(), javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        JavaType javaType2 = mapper.getTypeFactory().constructType(HashSet.class);
        JavaType mappedType2 = resolver.findTypeMapping(mapper.getConfig(), javaType2);
        assertNull(mappedType2);
    }

    @Test
    public void testResolveAbstractType() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType mappedType = resolver.resolveAbstractType(mapper.getConfig(), javaType);
        assertNull(mappedType);
    }

    @Test
    public void testFindTypeMappingWithGenericType() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().setAbstractTypeResolver(resolver));

        JavaType javaType = mapper.getTypeFactory().constructType(new ClassIdType(ArrayList.class, 0));
        JavaType mappedType = resolver.findTypeMapping(mapper.getConfig(), javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        JavaType javaType2 = mapper.getTypeFactory().constructType(new ClassIdType(ArrayList.class, 1));
        JavaType mappedType2 = resolver.findTypeMapping(mapper.getConfig(), javaType2);
        assertNull(mappedType2);
    }
}