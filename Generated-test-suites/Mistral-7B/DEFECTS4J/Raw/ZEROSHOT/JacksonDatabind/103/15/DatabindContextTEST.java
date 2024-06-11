package com.example.junit;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test class for DatabindContext.
 */
public class DatabindContextTest {

    /**
     * Test for getConfig() method.
     */
    @Test
    public void testGetConfig() {
        DatabindContext context = createContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    /**
     * Test for getAnnotationIntrospector() method.
     */
    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = createContext();
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);
    }

    /**
     * Test for isEnabled() method.
     */
    @Test
    public void testIsEnabled() {
        DatabindContext context = createContext();
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    /**
     * Test for canOverrideAccessModifiers() method.
     */
    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = createContext();
        assertTrue(context.canOverrideAccessModifiers());
    }

    /**
     * Test for getActiveView() method.
     */
    @Test
    public void testGetActiveView() {
        DatabindContext context = createContext();
        assertNull(context.getActiveView());
    }

    /**
     * Test for getLocale() method.
     */
    @Test
    public void testGetLocale() {
        DatabindContext context = createContext();
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    /**
     * Test for getTimeZone() method.
     */
    @Test
    public void testGetTimeZone() {
        DatabindContext context = createContext();
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    /**
     * Test for getDefaultPropertyFormat() method.
     */
    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = createContext();
        JsonFormat.Value format = context.getDefaultPropertyFormat(Object.class);
        assertNull(format);
    }

    /**
     * Test for getAttribute() method.
     */
    @Test
    public void testGetAttribute() {
        DatabindContext context = createContext();
        Object attribute = context.getAttribute("testAttribute");
        assertNull(attribute);

        context = createContextWithAttribute();
        attribute = context.getAttribute("testAttribute");
        assertEquals("testAttributeValue", attribute);
    }

    /**
     * Test for setAttribute() method.
     */
    @Test
    public void testSetAttribute() {
        DatabindContext context = createContext();
        context.setAttribute("testAttribute", "testAttributeValue");

        Object attribute = context.getAttribute("testAttribute");
        assertEquals("testAttributeValue", attribute);
    }

    /**
     * Test for constructType() method.
     */
    @Test
    public void testConstructType() {
        DatabindContext context = createContext();
        Type type = new TypeReference<java.util.List<String>>(){}.getType();
        JavaType javaType = context.constructType(type);
        assertNotNull(javaType);
    }

    /**
     * Test for constructSpecializedType() method.
     */
    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = createContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subclassType = context.constructSpecializedType(baseType, java.util.ArrayList.class);
        assertNotNull(subclassType);
    }

    /**
     * Test for resolveSubType() method.
     */
    @Test
    public void testResolveSubType() {
        DatabindContext context = createContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String subclass = "java.util.ArrayList";
        JavaType subType = context.resolveSubType(baseType, subclass);
        assertNotNull(subType);
    }

    /**
     * Test for invalidTypeIdException() method.
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() {
        DatabindContext context = createContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "InvalidTypeId";
        context.invalidTypeIdException(baseType, typeId, "Error message");
    }

    /**
     * Test for getTypeFactory() method.
     */
    @Test
    public void testGetTypeFactory() {
        DatabindContext context = createContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    /**
     * Test for objectIdGeneratorInstance() method.
     */
    @Test
    public void testObjectIdGeneratorInstance() {
        DatabindContext context = createContext();
        AnnotatedClass annotatedClass = new AnnotatedClass("com.example.MyClass");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerator.class, annotatedClass);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    /**
     * Test for objectIdResolverInstance() method.
     */
    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = createContext();
        AnnotatedClass annotatedClass = new AnnotatedClass("com.example.MyClass");
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdResolver.class, annotatedClass);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    /**
     * Test for converterInstance() method.
     */
    @Test
    public void testConverterInstance() {
        DatabindContext context = createContext();
        Annotated annotated = new AnnotatedClass("com.example.ConverterClass");
        Object converterDef = Converter.StringToInteger.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    /**
     * Test for reportBadDefinition() method.
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() {
        DatabindContext context = createContext();
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        context.reportBadDefinition(javaType, "Bad definition message");
    }

    /**
     * Create a mock DatabindContext.
     */
    private DatabindContext createContext() {
        MapperConfig<?> config = new MapperConfig.Builder().build();
        return new TestDatabindContext(config);
    }

    /**
     * Create a mock DatabindContext with an attribute.
     */
    private DatabindContext createContextWithAttribute() {
        MapperConfig<?> config = new MapperConfig.Builder()
                .propertyName("testAttribute")
                .value("testAttributeValue")
                .build();
        return new TestDatabindContext(config);
    }

    /**
     * Test DatabindContext subclass for testing purposes.
     */
    private static class TestDatabindContext extends DatabindContext {
        public TestDatabindContext(MapperConfig<?> config) {
            super();
            this.config = config;
        }

        @Override
        public MapperConfig<?> getConfig() {
            return config;
        }
    }
}