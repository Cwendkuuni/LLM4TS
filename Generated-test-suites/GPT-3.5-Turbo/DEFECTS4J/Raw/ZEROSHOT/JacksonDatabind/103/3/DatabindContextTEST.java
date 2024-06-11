package com.fasterxml.jackson.databind;

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
        JavaType javaType = context.constructType(Integer.class);
        assertNotNull(javaType);
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.constructType(Number.class);
        JavaType specializedType = context.constructSpecializedType(baseType, Integer.class);
        assertNotNull(specializedType);
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.constructType(Number.class);
        String subClass = "java.lang.Integer";
        JavaType subType = context.resolveSubType(baseType, subClass);
        assertNotNull(subType);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = null;
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = null;
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = null;
        Object converterDef = null;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);
    }
    
    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = context.constructType(Integer.class);
        String msg = "Invalid definition";
        Object result = context.reportBadDefinition(type, msg);

        assertNull(result);
    }
}