package com.fasterxml.jackson.databind;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DatabindContextTest {

    private DatabindContext databindContext;

    @Before
    public void setup() {
        databindContext = new DatabindContext() {
            @Override
            public MapperConfig<?> getConfig() {
                return null;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }

            @Override
            public boolean isEnabled(MapperFeature feature) {
                return false;
            }

            @Override
            public boolean canOverrideAccessModifiers() {
                return false;
            }

            @Override
            public Class<?> getActiveView() {
                return null;
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
            public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
                return null;
            }

            @Override
            public Object getAttribute(Object key) {
                return null;
            }

            @Override
            public DatabindContext setAttribute(Object key, Object value) {
                return null;
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
                return null;
            }

            @Override
            public TypeFactory getTypeFactory() {
                return null;
            }

            @Override
            public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo) throws JsonMappingException {
                return null;
            }

            @Override
            public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo) {
                return null;
            }

            @Override
            public Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef) throws JsonMappingException {
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
        };
    }

    @Test
    public void testGetConfig() {
        assertNull(databindContext.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertNull(databindContext.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        assertFalse(databindContext.isEnabled(null));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertFalse(databindContext.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(databindContext.getActiveView());
    }

    @Test
    public void testGetLocale() {
        assertNull(databindContext.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertNull(databindContext.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        assertNull(databindContext.getDefaultPropertyFormat(null));
    }

    @Test
    public void testGetAttribute() {
        assertNull(databindContext.getAttribute(null));
    }

    @Test
    public void testSetAttribute() {
        assertNull(databindContext.setAttribute(null, null));
    }

    @Test
    public void testConstructType() {
        assertNull(databindContext.constructType(null));
    }

    @Test
    public void testConstructSpecializedType() {
        assertNull(databindContext.constructSpecializedType(null, null));
    }

    @Test
    public void testResolveSubType() {
        assertNull(databindContext.resolveSubType(null, null));
    }

    @Test
    public void testInvalidTypeIdException() {
        assertNull(databindContext.invalidTypeIdException(null, null, null));
    }

    @Test
    public void testGetTypeFactory() {
        assertNull(databindContext.getTypeFactory());
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        assertNull(databindContext.objectIdGeneratorInstance(null, null));
    }

    @Test
    public void testObjectIdResolverInstance() {
        assertNull(databindContext.objectIdResolverInstance(null, null));
    }

    @Test
    public void testConverterInstance() {
        assertNull(databindContext.converterInstance(null, null));
    }

    @Test
    public void testReportBadDefinitionWithType() {
        assertNull(databindContext.reportBadDefinition(null, null));
    }

    @Test
    public void testReportBadDefinitionWithClass() {
        assertNull(databindContext.reportBadDefinition(null, null));
    }
}