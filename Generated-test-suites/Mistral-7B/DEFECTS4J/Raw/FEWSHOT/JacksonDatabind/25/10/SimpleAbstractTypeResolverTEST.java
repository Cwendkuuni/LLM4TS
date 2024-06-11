package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @org.junit.Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
    }

    @org.junit.Test
    public void testFindTypeMapping() {
        DeserializationConfig config = new DeserializationConfig();
        config.setRootValueConstructor(null);
        JavaType javaType = new ClassType(Collection.class);

        JavaType mappedType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        javaType = new ClassType(List.class);
        mappedType = resolver.findTypeMapping(config, javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());

        javaType = new ClassType(ArrayList.class);
        mappedType = resolver.findTypeMapping(config, javaType);
        assertNull(mappedType);

        javaType = new ClassType(LinkedList.class);
        mappedType = resolver.findTypeMapping(config, javaType);
        assertNull(mappedType);
    }

    @org.junit.Test
    public void testResolveAbstractType() {
        DeserializationConfig config = new DeserializationConfig();
        config.setRootValueConstructor(null);
        JavaType javaType = new ClassType(Collection.class);

        JavaType mappedType = resolver.resolveAbstractType(config, javaType);
        assertNull(mappedType);
    }

    static class ClassType extends JavaType {
        private final Class<?> rawClass;

        public ClassType(Class<?> rawClass) {
            super(null, null, rawClass);
            this.rawClass = rawClass;
        }

        @Override
        public Class<?> getRawClass() {
            return rawClass;
        }
    }
}