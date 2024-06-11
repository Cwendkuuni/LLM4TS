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
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId,
                    String extraDesc) {
                return null;
            }
            
            @Override
            public TypeFactory getTypeFactory() {
                return null;
            }
            
            @Override
            public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated,
                    ObjectIdInfo objectIdInfo) throws JsonMappingException {
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
        };
    }
    
    @Test
    public void testConstructType() {
        JavaType type = databindContext.constructType(null);
        assertNull(type);
    }
    
    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = null;
        Class<?> subclass = null;
        JavaType specializedType = databindContext.constructSpecializedType(baseType, subclass);
        assertNull(specializedType);
    }
    
    @Test
    public void testResolveSubType() {
        JavaType baseType = null;
        String subClass = null;
        try {
            JavaType subType = databindContext.resolveSubType(baseType, subClass);
            assertNull(subType);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testInvalidTypeIdException() {
        JavaType baseType = null;
        String typeId = null;
        String extraDesc = null;
        try {
            JsonMappingException exception = databindContext.invalidTypeIdException(baseType, typeId, extraDesc);
            assertNull(exception);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testReportBadDefinition() {
        JavaType type = null;
        String msg = null;
        try {
            Object result = databindContext.reportBadDefinition(type, msg);
            assertNull(result);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }
}