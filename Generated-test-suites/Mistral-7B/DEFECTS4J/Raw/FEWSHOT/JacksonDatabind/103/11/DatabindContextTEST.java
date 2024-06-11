package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DatabindContextTest {

    abstract class TestDatabindContext extends DatabindContext {
        MapperConfig<?> config;

        TestDatabindContext(MapperConfig<?> config) {
            this.config = config;
        }

        @Override
        public MapperConfig<?> getConfig() {
            return config;
        }

        @Override
        public abstract JavaType getActiveView();

        @Override
        public abstract JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType);

        @Override
        public abstract ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
                throws JsonMappingException;

        @Override
        public abstract ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo);

        @Override
        public abstract Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef)
                throws JsonMappingException;

        @Override
        public abstract <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException;

        @Override
        public abstract <T> T reportBadDefinition(Class<?> type, String msg) throws JsonMappingException;

        @Override
        public abstract TypeFactory getTypeFactory();

        @Override
        public abstract boolean canOverrideAccessModifiers();

        @Override
        public abstract boolean isEnabled(MapperFeature feature);

        @Override
        public abstract Locale getLocale();

        @Override
        public abstract TimeZone getTimeZone();

        @Override
        public abstract Object getAttribute(Object key);

        @Override
        public abstract DatabindContext setAttribute(Object key, Object value);

        @Override
        public abstract JavaType constructType(Type type);

        @Override
        public abstract JavaType constructSpecializedType(JavaType baseType, Class<?> subclass);

        @Override
        protected abstract JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc);
    }

    @Test
    public void testAbstractMethods() {
        MapperConfig<?> config = new MapperConfig.Builder().build();
        TestDatabindContext testContext = new TestDatabindContext(config) {
            @Override
            public JavaType getActiveView() {
                return null;
            }

            @Override
            public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
                return null;
            }

            @Override
            public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo) {
                return null;
            }

            @Override
            public Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                return null;
            }

            @Override
            public <T> T reportBadDefinition(Class<?> type, String msg) throws JsonMappingException {
                return null;
            }

            @Override
            public TypeFactory getTypeFactory() {
                return null;
            }

            @Override
            public boolean canOverrideAccessModifiers() {
                return false;
            }

            @Override
            public boolean isEnabled(MapperFeature feature) {
                return false;
            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public TimeZone getTimeZone() {
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
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
                return null;
            }
        };

        assertNull(testContext.getActiveView());
        assertNull(testContext.getDefaultPropertyFormat(Object.class));
        assertNull(testContext.objectIdGeneratorInstance(null, null));
        assertNull(testContext.objectIdResolverInstance(null, null));
        assertNull(testContext.converterInstance(null, null));
        assertNull(testContext.reportBadDefinition(Object.class, ""));
        assertNull(testContext.reportBadDefinition(String.class, ""));
        assertNull(testContext.getTypeFactory());
        assertFalse(testContext.canOverrideAccessModifiers());
        assertFalse(testContext.isEnabled(MapperFeature.AUTO_DETECT_CREATOR));
        assertNull(testContext.getLocale());
        assertNull(testContext.getTimeZone());
        assertNull(testContext.getAttribute(null));
        assertEquals(testContext, testContext.setAttribute(null, null));
        assertNull(testContext.constructType(null));
        assertNull(testContext.constructSpecializedType(Object.class, String.class));
        assertNull(testContext.invalidTypeIdException(null, null, null));
    }
}