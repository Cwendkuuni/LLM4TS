package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.*;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(result);
    }

    // Helper class used for converting from various generic container types into more specific implementations
    // accessible via java.util.Collections.
    private static class JavaUtilCollectionsConverterHelper implements Converter<Object,Object> {
        private final JavaType _inputType;
        private final int _kind;

        public JavaUtilCollectionsConverterHelper(int kind, JavaType inputType) {
            _inputType = inputType;
            _kind = kind;
        }
        
        @Override
        public Object convert(Object value) {
            if (value == null) {
                return null;
            }
            
            switch (_kind) {
            case JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET:
                {
                    Set<?> set = (Set<?>) value;
                    _checkSingleton(set.size());
                    return Collections.singleton(set.iterator().next());
                }
            case JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST:
                {
                    List<?> list = (List<?>) value;
                    _checkSingleton(list.size());
                    return Collections.singletonList(list.get(0));
                }
            case JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP:
                {
                    Map<?,?> map = (Map<?,?>) value;
                    _checkSingleton(map.size());
                    Map.Entry<?,?> entry = map.entrySet().iterator().next();
                    return Collections.singletonMap(entry.getKey(), entry.getValue());
                }

            case JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET:
                return Collections.unmodifiableSet((Set<?>) value);
            case JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST:
                return Collections.unmodifiableList((List<?>) value);
            case JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP:
                return Collections.unmodifiableMap((Map<?,?>) value);

            case JavaUtilCollectionsDeserializers.TYPE_AS_LIST:
            default:
                return value;
            }
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return _inputType;
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return _inputType;
        }

        private void _checkSingleton(int size) {
            if (size != 1) {
                throw new IllegalArgumentException("Can not deserialize Singleton container from "+size+" entries");
            }
        }
    }

    @Test
    public void testConverter() {
        int kind = 1;
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsConverter result = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
        assertNotNull(result);
    }
}