package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DatabindContextTest {
    // Test double for abstract methods
    private static class TestingDatabindContext extends DatabindContext {
        @Override
        public MapperConfig<?> getConfig() {
            return new MapperConfig.Builder().build();
        }

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return new StandardAnnotationIntrospector();
        }

        @Override
        public boolean isEnabled(MapperFeature feature) {
            return true;
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
            return null;
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
    }

    @Test
    public void testGetConfig() {
        TestingDatabindContext context = new TestingDatabindContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        TestingDatabindContext context = new TestingDatabindContext();
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);
    }

    @Test
    public void testIsEnabled() {
        TestingDatabindContext context = new TestingDatabindContext();
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertTrue(context.isEnabled(MapperFeature.CAN_OVERLOAD_ACCESS_MODIFIERS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        TestingDatabindContext context = new TestingDatabindContext();
        assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        TestingDatabindContext context = new TestingDatabindContext();
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        TestingDatabindContext context = new TestingDatabindContext();
        Locale locale = context.getLocale();
        assertNotNull(locale);
    }

    @Test
    public void testGetTimeZone() {
        TestingDatabindContext context = new TestingDatabindContext();
        TimeZone timeZone = context.getTimeZone();
        assertNotNull(timeZone);
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        TestingDatabindContext context = new TestingDatabindContext();
        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(String.class);
        assertNull(defaultPropertyFormat);
    }

    @Test
    public void testGetAttribute() {
        TestingDatabindContext context = new TestingDatabindContext();
        Object attribute = context.getAttribute("testKey");
        assertNull(attribute);

        context = new TestingDatabindContext();
        context.setAttribute("testKey", "testValue");
        Object testAttribute = context.getAttribute("testKey");
        assertEquals("testValue", testAttribute);
    }

    @Test
    public void testConstructType() {
        TestingDatabindContext context = new TestingDatabindContext();
        Type type = new TypeToken<List<String>>(){}.getType();
        JavaType javaType = context.constructType(type);
        assertNotNull(javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testConstructTypeWithNullType() {
        TestingDatabindContext context = new TestingDatabindContext();
        context.constructType(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testConstructSpecializedTypeWithNullBaseType() {
        TestingDatabindContext context = new TestingDatabindContext();
        JavaType baseType = null;
        JavaType specializedType = context.constructSpecializedType(baseType, String.class);
    }

    @Test
    public void testConstructSpecializedType() {
        TestingDatabindContext context = new TestingDatabindContext();
        JavaType javaType = context.constructType(String.class);
        JavaType specializedType = context.constructSpecializedType(javaType, String[].class);
        assertNotNull(specializedType);
    }

    @Test
    public void testResolveSubTypeWithValidTypeId() {
        TestingDatabindContext context = new TestingDatabindContext();
        JavaType baseType = context.constructType(String.class);
        JavaType resolvedType = context.resolveSubType(baseType, "java.util.List");
        assertNotNull(resolvedType);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeWithInvalidTypeId() {
        TestingDatabindContext context = new TestingDatabindContext();
        JavaType baseType = context.constructType(String.class);
        context.resolveSubType(baseType, "InvalidTypeId");
    }

    @Test
    public void testInvalidTypeIdException() {
        TestingDatabindContext context = new TestingDatabindContext() {
            @Override
            protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId,
                    String extraDesc) {
                return new JsonMappingException("Invalid type id: " + typeId);
            }
        };
        JavaType baseType = context.constructType(String.class);
        String typeId = "InvalidTypeId";
        assertNotNull(context.invalidTypeIdException(baseType, typeId, ""));
    }

    @Test
    public void testGetTypeFactory() {
        TestingDatabindContext context = new TestingDatabindContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        TestingDatabindContext context = new TestingDatabindContext();
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, null);
        assertNotNull(objectIdGenerator);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdGeneratorInstanceWithNullAnnotated() {
        TestingDatabindContext context = new TestingDatabindContext();
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdGeneratorInstanceWithNullObjectIdInfo() {
        TestingDatabindContext context = new TestingDatabindContext();
        ObjectIdInfo objectIdInfo = null;
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(new AnnotatedClass(String.class), objectIdInfo);
    }

    @Test
    public void testObjectIdResolverInstance() {
        TestingDatabindContext context = new TestingDatabindContext();
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, null);
        assertNotNull(objectIdResolver);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdResolverInstanceWithNullAnnotated() {
        TestingDatabindContext context = new TestingDatabindContext();
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdResolverInstanceWithNullObjectIdInfo() {
        TestingDatabindContext context = new TestingDatabindContext();
        ObjectIdInfo objectIdInfo = null;
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(new AnnotatedClass(String.class), objectIdInfo);
    }

    @Test
    public void testConverterInstance() {
        TestingDatabindContext context = new TestingDatabindContext();
        Annotated annotated = new AnnotatedField("testField", String.class, null);
        Object converterDef = new SimpleStringConverter();
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testConverterInstanceWithNullAnnotated() {
        TestingDatabindContext context = new TestingDatabindContext();
        Object converterDef = new SimpleStringConverter();
        Converter<Object, Object> converter = context.converterInstance(null, converterDef);
    }

    @Test(expected = JsonMappingException.class)
    public void testConverterInstanceWithNullConverterDef() {
        TestingDatabindContext context = new TestingDatabindContext();
        Annotated annotated = new AnnotatedField("testField", String.class, null);
        Converter<Object, Object> converter = context.converterInstance(annotated, null);
    }

    @Test
    public void testReportBadDefinitionWithJavaType() {
        TestingDatabindContext context = new TestingDatabindContext() {
            @Override
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                return (T) new JsonMappingException("Bad definition for " + type);
            }
        };
        JavaType javaType = context.constructType(String.class);
        JsonMappingException exception = context.reportBadDefinition(javaType, "Invalid definition");
        assertNotNull(exception);
    }

    @Test
    public void testReportBadDefinitionWithClass() {
        TestingDatabindContext context = new TestingDatabindContext() {
            @Override
            public <T> T reportBadDefinition(Class<?> type, String msg) throws JsonMappingException {
                return (T) new JsonMappingException("Bad definition for " + type);
            }
        };
        Class<?> clazz = String.class;
        JsonMappingException exception = context.reportBadDefinition(clazz, "Invalid definition");
        assertNotNull(exception);
    }
}

class AnnotatedClass implements Annotated {
    private final Class<?> clazz;

    public AnnotatedClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getAnnotations(Class<? extends Annotation> annotationType) {
        return new Annotation[0];
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return null;
    }

    @Override
    public String getAnnotationName() {
        return "";
    }

    @Override
    public Class<?> getAnnotatedClass() {
        return clazz;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return false;
    }

    @Override
    public String toString() {
        return clazz.getName();
    }
}

class AnnotatedField implements Annotated {
    private final String name;
    private final Class<?> type;
    private final Object value;

    public AnnotatedField(String name, Class<?> type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getAnnotations(Class<? extends Annotation> annotationType) {
        return new Annotation[0];
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return null;
    }

    @Override
    public String getAnnotationName() {
        return "";
    }

    @Override
    public Class<?> getAnnotatedClass() {
        return null;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return false;
    }

    @Override
    public String toString() {
        return "@" + (value != null ? value.getClass().getName() : "") + " " + name + " " + type;
    }
}

class SimpleStringConverter implements Converter<Object, String> {
    @Override
    public String convert(Object value, Type descriptor) {
        return value.toString();
    }
}