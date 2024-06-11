package com.fasterxml.jackson.databind;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.*;

import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for DatabindContext.
 */
public class DatabindContextTest {
    
    private DatabindContext context;

    @Before
    public void setUp() {
        context = new DatabindContext() {
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
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
                return null;
            }

            @Override
            public TypeFactory getTypeFactory() {
                return null;
            }

            @Override
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                return null;
            }
        };
    }

    @Test
    public void testConstructType() {
        Type type = null;
        JavaType result = context.constructType(type);
        assertNull(result);
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = null;
        Class<?> subclass = null;
        JavaType result = context.constructSpecializedType(baseType, subclass);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = null;
        String subClass = null;
        context.resolveSubType(baseType, subClass);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = null;
        ObjectIdGenerator<?> result = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(result);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = null;
        ObjectIdResolver result = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(result);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = null;
        Object converterDef = null;
        Converter<Object, Object> result = context.converterInstance(annotated, converterDef);
        assertNull(result);
    }
}