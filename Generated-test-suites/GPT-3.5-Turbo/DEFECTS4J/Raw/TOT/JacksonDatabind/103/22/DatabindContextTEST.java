
package com.fasterxml.jackson.databind;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.*;

import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class DatabindContextTest {

    private DatabindContext databindContext;

    @Before
    public void setUp() {
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
        JavaType javaType = databindContext.constructType(String.class);

        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = databindContext.constructType(Object.class);
        JavaType specializedType = databindContext.constructSpecializedType(baseType, String.class);

        assertNotNull(specializedType);
        assertEquals(String.class, specializedType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = databindContext.constructType(Object.class);
        JavaType subType = databindContext.resolveSubType(baseType, "java.lang.Integer");

        assertNotNull(subType);
        assertEquals(Integer.class, subType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType_NotSubType() throws JsonMappingException {
        JavaType baseType = databindContext.constructType(Object.class);
        databindContext.resolveSubType(baseType, "java.lang.String");
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = null;
        ObjectIdGenerator<?> generator = databindContext.objectIdGeneratorInstance(annotated, objectIdInfo);

        assertNull(generator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = null;
        ObjectIdResolver resolver = databindContext.objectIdResolverInstance(annotated, objectIdInfo);

        assertNull(resolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = null;
        Object converterDef = null;
        Converter<Object, Object> converter = databindContext.converterInstance(annotated, converterDef);

        assertNull(converter);
    }
}

