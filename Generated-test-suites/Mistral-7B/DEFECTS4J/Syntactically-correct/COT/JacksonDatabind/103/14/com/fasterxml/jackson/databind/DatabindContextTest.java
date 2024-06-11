package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class DatabindContextTest {

    @SuppressWarnings("unchecked")
    public static class TestDatabindContext extends DatabindContext {
        // override the abstract methods for testing
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
            return Locale.US;
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
            return TypeFactory.defaultInstance().constructType(type);
        }

        @Override
        public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
            return TypeFactory.defaultInstance().constructSpecializedType(baseType, subclass);
        }

        @Override
        public JavaType resolveSubType(JavaType baseType, String subClass) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
            throw new JsonMappingException(String.format("Invalid type id: %s, %s", typeId, extraDesc));
        }

        @Override
        public TypeFactory getTypeFactory() {
            return TypeFactory.defaultInstance();
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
            throw new JsonMappingException(msg);
        }

        @Override
        public <T> T reportBadDefinition(Class<?> type, String msg) throws JsonMappingException {
            throw new JsonMappingException(msg);
        }
    }

    @JavaTypeBinding
    static class TestBean {
        private int id;
        private String name;
    }

    @Test
    public void testGetConfig() {
        TestDatabindContext ctx = new TestDatabindContext();
        MapperConfig<?> config = ctx.getConfig();
        assertNotNull(config);
        assertTrue(config.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(config.isEnabled(MapperFeature.FAIL_ON_EMPTY_BEANS));
    }

    @Test
    public void testGetAnnotationIntrospector() {
        TestDatabindContext ctx = new TestDatabindContext();
        AnnotationIntrospector introspector = ctx.getAnnotationIntrospector();
        assertNotNull(introspector);
    }

    @Test
    public void testIsEnabled() {
        TestDatabindContext ctx = new TestDatabindContext();
        assertTrue(ctx.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(ctx.isEnabled(MapperFeature.FAIL_ON_EMPTY_BEANS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        TestDatabindContext ctx = new TestDatabindContext();
        assertTrue(ctx.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        TestDatabindContext ctx = new TestDatabindContext();
        assertNull(ctx.getActiveView());
    }

    @Test
    public void testGetLocale() {
        TestDatabindContext ctx = new TestDatabindContext();
        Locale locale = ctx.getLocale();
        assertEquals(locale.getClass(), Locale.class);
        assertEquals(locale, Locale.US);
    }

    @Test
    public void testGetTimeZone() {
        TestDatabindContext ctx = new TestDatabindContext();
        TimeZone timeZone = ctx.getTimeZone();
        assertEquals(timeZone.getClass(), TimeZone.class);
        assertNotNull(timeZone);
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        TestDatabindContext ctx = new TestDatabindContext();
        JsonFormat.Value format = ctx.getDefaultPropertyFormat(Integer.class);
        assertNull(format);
    }

    @Test
    public void testGetAttribute() {
        TestDatabindContext ctx = new TestDatabindContext();
        Object attribute = ctx.getAttribute("test");
        assertNull(attribute);
    }

    @Test
    public void testSetAttribute() {
        TestDatabindContext ctx = new TestDatabindContext();
        ctx.setAttribute("test", "testValue");
        Object attribute = ctx.getAttribute("test");
        assertEquals(attribute, "testValue");
    }

    @Test
    public void testConstructType() throws JsonProcessingException {
        TestDatabindContext ctx = new TestDatabindContext();
        JavaType javaType = ctx.constructType(TestBean.class);
        assertNotNull(javaType);
        assertEquals(javaType.getRawClass(), TestBean.class);
    }

    @Test
    public void testConstructSpecializedType() throws JsonProcessingException {
        TestDatabindContext ctx = new TestDatabindContext();
        JavaType baseType = ctx.constructType(TestBean.class);
        JavaType specializedType = ctx.constructSpecializedType(baseType, TestBeanWithId.class);
        assertNotNull(specializedType);
        assertEquals(specializedType.getRawClass(), TestBeanWithId.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeFail() throws JsonMappingException {
        TestDatabindContext ctx = new TestDatabindContext();
        JavaType baseType = ctx.constructType(TestBean.class);
        JavaType typeId = ctx.resolveSubType(baseType, "NonExistentSubType");
        fail("Should not reach here");
    }

    @Test
    public void testResolveSubTypePass() throws JsonMappingException {
        TestDatabindContext ctx = new TestDatabindContext();
        JavaType baseType = ctx.constructType(TestBean.class);
        JavaType subType = ctx.constructType(TestBeanWithId.class);
        JavaType resolvedType = ctx.resolveSubType(baseType, subType.getName());
        assertSame(resolvedType, subType);
    }

    @Test
    public void testInvalidTypeIdException() {
        TestDatabindContext ctx = new TestDatabindContext();
        JavaType baseType = ctx.constructType(TestBean.class);
        try {
            ctx.invalidTypeIdException(baseType, "NonExistentType", null);
            fail("Should have thrown JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testTypeFactory() {
        TestDatabindContext ctx = new TestDatabindContext();
        TypeFactory typeFactory = ctx.getTypeFactory();
        assertNotNull(typeFactory);
        JavaType javaType = typeFactory.constructType(TestBean.class);
        assertNotNull(javaType);
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        TestDatabindContext ctx = new TestDatabindContext();
        AnnotatedClass annotatedClass = new AnnotatedClass(TestBeanWithId.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(TestBeanWithId.class, null, null);
        ObjectIdGenerator<?> idGenerator = ctx.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertNotNull(idGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        TestDatabindContext ctx = new TestDatabindContext();
        AnnotatedClass annotatedClass = new AnnotatedClass(TestBeanWithId.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(TestBeanWithId.class, null, null);
        ObjectIdResolver idResolver = ctx.objectIdResolverInstance(annotatedClass, objectIdInfo);
        assertNotNull(idResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        TestDatabindContext ctx = new TestDatabindContext();
        Annotated annotated = new TestAnnotation();
        Object converterDef = new StringConverter();
        Converter<Object, Object> converter = ctx.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        TestDatabindContext ctx = new TestDatabindContext();
        JavaType javaType = ctx.constructType(TestBean.class);
        ctx.reportBadDefinition(javaType, "Test error message");
        fail("Should not reach here");
    }

    static class TestAnnotation {
        @JsonDeserialize(converter = StringConverter.class)
        public String testField;
    }

    static class StringConverter implements Converter<String, String> {
        @Override
        public String readValue(String value) {
            return null;
        }

        @Override
        public String writeValue(String value) {
            return value;
        }
    }

    static class TestBeanWithId extends TestBean {
        @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
        private int id;
    }
}