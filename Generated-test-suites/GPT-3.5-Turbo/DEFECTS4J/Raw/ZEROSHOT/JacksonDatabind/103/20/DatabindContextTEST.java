package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                return null;
            }
        };
    }

    @Test
    public void testGetConfig() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetAnnotationIntrospector() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsEnabled() {
        fail("Not yet implemented");
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetActiveView() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetLocale() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetTimeZone() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetAttribute() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetAttribute() {
        fail("Not yet implemented");
    }

    @Test
    public void testConstructType() {
        fail("Not yet implemented");
    }

    @Test
    public void testConstructSpecializedType() {
        fail("Not yet implemented");
    }

    @Test
    public void testResolveSubType() {
        fail("Not yet implemented");
    }

    @Test
    public void testInvalidTypeIdException() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetTypeFactory() {
        fail("Not yet implemented");
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        fail("Not yet implemented");
    }

    @Test
    public void testObjectIdResolverInstance() {
        fail("Not yet implemented");
    }

    @Test
    public void testReportBadDefinitionJavaTypeString() {
        fail("Not yet implemented");
    }

    @Test
    public void testReportBadDefinitionClassOfString() {
        fail("Not yet implemented");
    }

}