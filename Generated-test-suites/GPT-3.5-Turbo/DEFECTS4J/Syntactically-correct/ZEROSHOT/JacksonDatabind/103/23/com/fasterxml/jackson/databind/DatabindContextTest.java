package com.fasterxml.jackson.databind;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.*;

import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        };
    }

    @Test
    public void testGetConfig() {
        assertNull(context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertNull(context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        assertNull(context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertNull(context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        assertNull(context.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testGetAttribute() {
        assertNull(context.getAttribute("test"));
    }

    @Test
    public void testSetAttribute() {
        assertNull(context.setAttribute("test", "value"));
    }

    @Test
    public void testConstructType() {
        assertNull(context.constructType(String.class));
    }

    @Test
    public void testConstructSpecializedType() {
        assertNull(context.constructSpecializedType(null, String.class));
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        assertNull(context.resolveSubType(null, "subClass"));
    }

    @Test(expected = NullPointerException.class)
    public void testResolveSubTypeWithNullBaseType() throws JsonMappingException {
        context.resolveSubType(null, null);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        assertNull(context.objectIdGeneratorInstance(null, null));
    }

    @Test
    public void testObjectIdResolverInstance() {
        assertNull(context.objectIdResolverInstance(null, null));
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        assertNull(context.converterInstance(null, null));
    }

    @Test
    public void testReportBadDefinitionWithType() throws JsonMappingException {
        assertNull(context.reportBadDefinition(null, "msg"));
    }

    @Test
    public void testReportBadDefinitionWithClass() throws JsonMappingException {
        assertNull(context.reportBadDefinition(String.class, "msg"));
    }
}