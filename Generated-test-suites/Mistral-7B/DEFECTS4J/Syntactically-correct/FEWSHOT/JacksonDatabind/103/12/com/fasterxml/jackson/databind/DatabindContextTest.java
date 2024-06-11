package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

public class DatabindContextTest {

    private static class TestContext extends DatabindContext {
        @Override
        public MapperConfig<?> getConfig() {
            return new MapperConfig.Builder().build();
        }

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return new SimpleAnnotationIntrospector();
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
            return Locale.getDefault();
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
            return null;
        }

        @Override
        public TypeFactory getTypeFactory() {
            return new TypeFactoryBuilder().build();
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
        protected String _format(String msg, Object... msgArgs) {
            return null;
        }

        @Override
        protected String _truncate(String desc) {
            return null;
        }

        @Override
        protected String _quotedString(String desc) {
            return null;
        }

        @Override
        protected String _colonConcat(String msgBase, String extra) {
            return null;
        }

        @Override
        protected String _desc(String desc) {
            return null;
        }
    }

    @Test
    public void testGenericConfigAccess() {
        TestContext ctx = new TestContext();
        assertNotNull(ctx.getConfig());
        assertNotNull(ctx.getAnnotationIntrospector());
    }

    @Test
    public void testAccessToSpecificConfigSettings() {
        TestContext ctx = new TestContext();
        assertTrue(ctx.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertTrue(ctx.canOverrideAccessModifiers());
        assertNull(ctx.getActiveView());
        assertNotNull(ctx.getLocale());
        assertNotNull(ctx.getTimeZone());
        assertNull(ctx.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testGenericAttributes() {
        TestContext ctx = new TestContext();
        assertNull(ctx.getAttribute(null));

        Object key = new Object();
        ctx.setAttribute(key, "value");
        assertEquals("value", ctx.getAttribute(key));
    }

    @Test
    public void testTypeInstantiationResolution() throws Exception {
        TestContext ctx = new TestContext();

        Type type = Object.class;
        JavaType javaType = ctx.constructType(type);
        assertEquals(JavaType.UnknownType.class, javaType.getClass());
        assertNull(javaType.getRawClass());

        JavaType javaType2 = ctx.constructSpecializedType(JavaType.constructRawType(String.class), String[].class);
        assertEquals(String[].class, javaType2.getRawClass());

        JavaType javaType3 = ctx.resolveSubType(JavaType.constructRawType(String.class), "java.lang.String[]");
        assertEquals(String[].class, javaType3.getRawClass());

        JavaType javaType4 = ctx.resolveSubType(JavaType.constructRawType(String.class), "java.util.ArrayList<java.lang.String>.class");
        assertEquals(JavaType.constructRawType(java.util.ArrayList.class), javaType4.getRawClass());
    }

    @Test
    public void testErrorReporting() {
        TestContext ctx = new TestContext();

        JavaType javaType = JavaType.constructRawType(String[].class);
        assertNull(ctx.reportBadDefinition(javaType, "Invalid definition"));

        Class<?> clazz = String[].class;
        assertNull(ctx.reportBadDefinition(clazz, "Invalid definition"));
    }

    @Test
    public void testHelperObjectConstruction() throws Exception {
        TestContext ctx = new TestContext();

        AnnotatedClass annotatedClass = new AnnotatedClass(String[].class, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null);
        ObjectIdGenerator<?> generated = ctx.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertNotNull(generated);

        AnnotatedClass annotatedClass2 = new AnnotatedClass(ObjectIdGenerator.class, null);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null);
        ObjectIdResolver objectIdResolver = ctx.objectIdResolverInstance(annotatedClass2, objectIdInfo2);
        assertNotNull(objectIdResolver);

        AnnotatedMethod annotatedMethod = new AnnotatedMethod(String.class.getMethod("valueOf", String.class), null);
        Object converterDef = JsonTypeInfo.Id.CLASS;
        Converter<Object, Object> converter = ctx.converterInstance(annotatedMethod, converterDef);
        assertNotNull(converter);
    }
}