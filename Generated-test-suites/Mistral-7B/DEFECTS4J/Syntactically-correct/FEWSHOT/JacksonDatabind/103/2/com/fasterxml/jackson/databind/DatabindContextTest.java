package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

public class DatabindContextTest {

    private ObjectMapper objectMapper;
    private MapperConfig<?> config;

    @Before
    public void setUp() {
        config = new MapperConfig.Builder()
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.setConfig(config);
    }

    @Test
    public void testGetConfig() {
        DatabindContext context = objectMapper.getDeserializationContext();
        Assert.assertSame(config, context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = objectMapper.getDeserializationContext();
        Assert.assertSame(config.getDefaultAnnotationIntrospector(), context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = objectMapper.getDeserializationContext();
        Assert.assertTrue(context.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUDE_ANNOTATIONS));
        Assert.assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = objectMapper.getDeserializationContext();
        Assert.assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = objectMapper.getDeserializationContext();
        Assert.assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = objectMapper.getDeserializationContext();
        Assert.assertNull(context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = objectMapper.getDeserializationContext();
        Assert.assertNull(context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = objectMapper.getDeserializationContext();
        Assert.assertNull(context.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testgetAttribute() {
        DatabindContext context = objectMapper.getDeserializationContext();
        context.setAttribute("test", "testValue");
        Assert.assertEquals("testValue", context.getAttribute("test"));
        context.setAttribute(null, null);
        Assert.assertNull(context.getAttribute(null));
    }

    @Test
    public void testsetAttribute() {
        DatabindContext context = objectMapper.getDeserializationContext();
        context.setAttribute("test", null);
        Assert.assertNull(context.getAttribute("test"));
        context.setAttribute("test", "testValue");
        Assert.assertEquals("testValue", context.getAttribute("test"));
    }

    @Test
    public void testConstructType() {
        DatabindContext context = objectMapper.getDeserializationContext();
        JavaType javaType = context.constructType(String[].class);
        Assert.assertNotNull(javaType);
        Assert.assertEquals(javaType.getRawClass(), String[].class);
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = objectMapper.getDeserializationContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subclassType = context.constructSpecializedType(baseType, StringBuffer.class);
        Assert.assertNotNull(subclassType);
        Assert.assertTrue(subclassType.isTypeOrSubTypeOf(StringBuffer.class));
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext context = objectMapper.getDeserializationContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.StringBuffer");
        Assert.assertNotNull(subType);
        Assert.assertTrue(subType.isTypeOrSubTypeOf(StringBuffer.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext context = new TestDatabindContext() {};
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "InvalidTypeId", "Invalid type id");
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = objectMapper.getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();
        Assert.assertNotNull(typeFactory);
        Assert.assertEquals(typeFactory.getClass(), TypeFactory.class);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = objectMapper.getDeserializationContext();
        Annotated annotated = new Annotated() {};
        ObjectIdInfo objectIdInfo = new ObjectIdInfo() {};
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        Assert.assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = objectMapper.getDeserializationContext();
        Annotated annotated = new Annotated() {};
        ObjectIdInfo objectIdInfo = new ObjectIdInfo() {};
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        Assert.assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = objectMapper.getDeserializationContext();
        Annotated annotated = new Annotated() {};
        Object converterDef = new StringConverter() {};
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        Assert.assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = new TestDatabindContext() {};
        JavaType type = context.getTypeFactory().constructType(String.class);
        context.reportBadDefinition(type, "Invalid definition");
    }

    @Test
    public void testReportBadDefinitionClass() throws JsonMappingException {
        DatabindContext context = new TestDatabindContext() {};
        Class<?> type = String.class;
        context.reportBadDefinition(type, "Invalid definition");
    }

    @JsonTypeName("Test")
    static class StringConverter {}

    static class TestDatabindContext extends DatabindContext {
        @Override
        protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
            throw new JsonMappingException(String.format("Invalid type id: %s, typeId: %s, extraDesc: %s", baseType, typeId, extraDesc));
        }
    }
}