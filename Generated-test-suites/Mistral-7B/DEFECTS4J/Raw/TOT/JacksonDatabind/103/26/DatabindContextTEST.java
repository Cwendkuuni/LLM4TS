package com.example.test.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabindContextTest {

    @Test
    public void testGenericConfigAccess() throws JsonMappingException {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public MapperConfig<?> getConfig() {
                MapperConfig<ObjectMapper.DefaultConfig> config = mock(MapperConfig.class);
                when(config.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));
                when(config.isEnabled(MapperFeature.AUTO_DETECT_CREATORS)).thenReturn(true);
                when(config.canOverrideAccessModifiers()).thenReturn(true);
                when(config.getTypeFactory()).thenReturn(mock(TypeFactory.class));
                return config;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return mock(AnnotationIntrospector.class);
            }

            @Override
            public boolean isEnabled(MapperFeature feature) {
                return true;
            }

            @Override
            public boolean canOverrideAccessModifiers() {
                return true;
            }

            @Override
            public Class<?> getActiveView() {
                return null;
            }

            @Override
            public Locale getLocale() {
                return Locale.ENGLISH;
            }

            @Override
            public TimeZone getTimeZone() {
                return TimeZone.getDefault();
            }

            @Override
            public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
                return null;
            }

            @Override
            public Object getAttribute(Object key) {
                return null;
            }

            @Override
            public DatabindContext setAttribute(Object key, Object value) {
                return this;
            }

            @Override
            public JavaType constructType(Type type) {
                return null;
            }

            @Override
            public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
                return null;
            }

            @Override
            public JavaType resolveSubType(JavaType baseType, String subClass) throws JsonMappingException {
                return null;
            }

            @Override
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
                throw new JsonMappingException("Error: Invalid type id");
            }

            @Override
            public TypeFactory getTypeFactory() {
                return mock(TypeFactory.class);
            }
        };

        assertEquals(config, context.getConfig());
        assertEquals(mock(AnnotationIntrospector.class), context.getAnnotationIntrospector());
        assertEquals(true, context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertEquals(true, context.canOverrideAccessModifiers());
        assertNull(context.getActiveView());
        assertEquals(Locale.ENGLISH, context.getLocale());
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
        assertNull(context.getDefaultPropertyFormat(String.class));
        assertNull(context.getAttribute(MapperFeature.class));

        context.setAttribute(MapperFeature.AUTO_DETECT_CREATORS, false);
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    @Test
    public void testTypeInstantiationResolution() throws JsonMappingException, ClassNotFoundException {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public MapperConfig<?> getConfig() {
                MapperConfig<ObjectMapper.DefaultConfig> config = mock(MapperConfig.class);
                TypeFactory typeFactory = mock(TypeFactory.class);
                when(config.getTypeFactory()).thenReturn(typeFactory);
                when(typeFactory.constructType(String[].class)).thenReturn(TypeFactory.defaultInstance.constructType(String[].class));
                when(typeFactory.constructSpecializedType(TypeFactory.defaultInstance.constructType(List.class), String[].class))
                        .thenReturn(TypeFactory.defaultInstance.constructType(java.util.List.class, String.class));
                when(typeFactory.constructFromCanonical("java.util.List"))
                        .thenReturn(TypeFactory.defaultInstance.constructType(java.util.List.class));
                when(typeFactory.constructFromCanonical("java.util.ArrayList"))
                        .thenReturn(TypeFactory.defaultInstance.constructType(java.util.ArrayList.class));
                return config;
            }

            @Override
            public JavaType constructType(Type type) {
                return null;
            }

            @Override
            public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
                return null;
            }

            @Override
            public JavaType resolveSubType(JavaType baseType, String subClass) throws JsonMappingException {
                return null;
            }

            @Override
            public TypeFactory getTypeFactory() {
                return mock(TypeFactory.class);
            }

            @Override
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
                throw new JsonMappingException("Error: Invalid type id");
            }
        };

        assertEquals(TypeFactory.defaultInstance.constructType(String[].class), context.constructType(String[].class));
        assertEquals(TypeFactory.defaultInstance.constructType(java.util.List.class, String.class), context.constructSpecializedType(TypeFactory.defaultInstance.constructType(List.class), String[].class));
        assertEquals(TypeFactory.defaultInstance.constructType(java.util.List.class), context.resolveSubType(TypeFactory.defaultInstance.constructType(List.class), "java.util.List"));
        assertEquals(TypeFactory.defaultInstance.constructType(java.util.ArrayList.class), context.resolveSubType(TypeFactory.defaultInstance.constructType(List.class), "java.util.ArrayList"));
        assertThrows(JsonMappingException.class, () -> context.resolveSubType(TypeFactory.defaultInstance.constructType(List.class), "java.util.LinkedList"));
    }

    @Test
    public void testErrorReporting() throws JsonMappingException, JsonProcessingException {
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public MapperConfig<?> getConfig() {
                MapperConfig<ObjectMapper.DefaultConfig> config = mock(MapperConfig.class);
                when(config.getTypeFactory()).thenReturn(mock(TypeFactory.class));
                return config;
            }

            @Override
            public JavaType constructType(Type type) {
                return null;
            }

            @Override
            public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
                return null;
            }

            @Override
            public JavaType resolveSubType(JavaType baseType, String subClass) throws JsonMappingException {
                return null;
            }

            @Override
            public TypeFactory getTypeFactory() {
                return mock(TypeFactory.class);
            }

            @Override
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.setMapperConfig(getConfig());
                return mapper.readerForType(getConfig().constructType(Object.class)).readValueExplicitly("{}")
                        .getMappingException();
            }
        };

        assertThrows(JsonMappingException.class, () -> context.reportBadDefinition(TypeFactory.defaultInstance.constructType(Object.class), "Invalid definition"));
    }
}