package com.fasterxml.jackson.databind;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        JavaType type = databindContext.constructType(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
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
        String subClass = "java.lang.String";
        JavaType resolvedType = databindContext.resolveSubType(baseType, subClass);
        assertNotNull(resolvedType);
        assertEquals(String.class, resolvedType.getRawClass());
    }
    
    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = null;
        ObjectIdGenerator<?> generator = databindContext.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(generator);
    }
    
    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = null;
        ObjectIdResolver resolver = databindContext.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(resolver);
    }
    
    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = null;
        Object converterDef = null;
        Converter<Object, Object> converter = databindContext.converterInstance(annotated, converterDef);
        assertNull(converter);
    }
    
    @Test
    public void testReportBadDefinitionWithJavaType() throws JsonMappingException {
        JavaType type = databindContext.constructType(String.class);
        String msg = "Invalid definition";
        String result = databindContext.reportBadDefinition(type, msg);
        assertNotNull(result);
        assertEquals(msg, result);
    }
    
    @Test
    public void testReportBadDefinitionWithClass() throws JsonMappingException {
        Class<?> type = String.class;
        String msg = "Invalid definition";
        String result = databindContext.reportBadDefinition(type, msg);
        assertNotNull(result);
        assertEquals(msg, result);
    }
    
}