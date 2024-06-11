package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.config.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private DatabindContext context;
    private MapperConfig<ObjectMapper> config;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        config = new MapperConfig<ObjectMapper>(ObjectMapper.getDefaultInstance());
        config.disable(MapperFeature.AUTO_DETECT_CREATOR);
        mapper = new ObjectMapper(config);
        context = mapper.getDeserializationContext();
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);
    }

    @Test
    public void testIsEnabled_MapperFeature() {
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_USE_STATIC_TYPES));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATOR));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        Locale locale = context.getLocale();
        assertEquals(Locale.getDefault(), locale);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = context.getTimeZone();
        assertEquals(TimeZone.getDefault(), timeZone);
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        Class<Object> baseType = Object.class;
        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(baseType);
        assertNull(defaultPropertyFormat);
    }

    @Test
    public void testGetAttribute() {
        Object key = "testKey";
        Object value = "testValue";
        context.setAttribute(key, value);
        Object attribute = context.getAttribute(key);
        assertEquals(value, attribute);

        context.setAttribute(key, null);
        attribute = context.getAttribute(key);
        assertNull(attribute);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttributeWithNullKey() {
        context.getAttribute(null);
    }

    @Test
    public void testConstructType() {
        Type type = String.class;
        JavaType javaType = context.constructType(type);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.getTypeFactory().constructType(Integer.class);
        Class<?> subclass = Integer[].class;
        JavaType javaType = context.constructSpecializedType(baseType, subclass);
        assertNotNull(javaType);
        assertEquals(Integer[].class, javaType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "java.util.ArrayList";
        JavaType javaType = context.resolveSubType(baseType, typeId);
        assertNotNull(javaType);
        assertEquals(java.util.ArrayList.class, javaType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "java.util.ArrayList";
        context.invalidTypeIdException(baseType, typeId, "Unexpected error message");
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.CLASS, String[].class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdGeneratorInstanceWithNullAnnotated() throws JsonMappingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.CLASS, String[].class);
        context.objectIdGeneratorInstance(null, objectIdInfo);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdGeneratorInstanceWithNullObjectIdInfo() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(String[].class);
        context.objectIdGeneratorInstance(annotated, null);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.NAME, "testName");
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdResolverInstanceWithNullAnnotated() throws JsonMappingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.NAME, "testName");
        context.objectIdResolverInstance(null, objectIdInfo);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdResolverInstanceWithNullObjectIdInfo() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(String[].class);
        context.objectIdResolverInstance(annotated, null);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(String.class);
        Class<?> converterDef = Converter.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testConverterInstanceWithNullAnnotated() throws JsonMappingException {
        Class<?> converterDef = Converter.class;
        context.converterInstance(null, converterDef);
    }

    @Test(expected = JsonMappingException.class)
    public void testConverterInstanceWithNullConverterDef() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(String.class);
        context.converterInstance(annotated, null);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType javaType = context.getTypeFactory().constructType(new TypeReference<String[][]>() {}.getType());
        String msg = "Test error message";
        assertEquals(javaType, context.reportBadDefinition(javaType, msg));
    }

    @Test
    public void testReportBadDefinitionWithStringType() throws JsonMappingException {
        Class<String> type = String.class;
        String msg = "Test error message";
        assertEquals(type, context.reportBadDefinition(type, msg));
    }

    /*
    /**********************************************************
    /* Edge cases and exception handling tests
    /**********************************************************
     */

    @Test
    public void testGetConfigWithNullContext() {
        assertNull(DatabindContext.class.cast(null).getConfig());
    }

    @Test(expected = NullPointerException.class)
    public void testIsEnabledWithNullMapperFeature() {
        context.isEnabled(null);
    }

    @Test
    public void testCanOverrideAccessModifiersWithMapperConfigThatDisablesIt() {
        MapperConfig<ObjectMapper> config = new MapperConfig<ObjectMapper>(ObjectMapper.getDefaultInstance());
        config.disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        DatabindContext context = config.getDeserializationContext();
        assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveViewWithNullContext() {
        assertNull(DatabindContext.class.cast(null).getActiveView());
    }

    @Test
    public void testGetLocaleWithNullContext() {
        assertNull(DatabindContext.class.cast(null).getLocale());
    }

    @Test
    public void testGetTimeZoneWithNullContext() {
        assertNull(DatabindContext.class.cast(null).getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormatWithNullContext() {
        assertNull(DatabindContext.class.cast(null).getDefaultPropertyFormat(Object.class));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttributeWithNullContext() {
        DatabindContext context = null;
        context.getAttribute("testKey");
    }

    @Test
    public void testGetAttributeWithNullKey() {
        context.setAttribute("testKey", "testValue");
        Object attribute = context.getAttribute(null);
        assertNull(attribute);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructTypeWithNullContext() {
        Type type = null;
        context.constructType(type);
    }

    @Test
    public void testConstructTypeWithNullType() {
        context.constructType(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeWithNullContext() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = null;
        context.resolveSubType(baseType, typeId);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeWithNullTypeId() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "";
        context.resolveSubType(baseType, typeId);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeWithInvalidTypeId() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "invalidTypeId";
        context.resolveSubType(baseType, typeId);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdExceptionWithNullBaseType() throws JsonMappingException {
        String typeId = "testTypeId";
        context.invalidTypeIdException(null, typeId, "Unexpected error message");
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdExceptionWithNullTypeId() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, null, "Unexpected error message");
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdExceptionWithEmptyTypeId() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "", "Unexpected error message");
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdExceptionWithInvalidTypeId() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "invalidTypeId", "Unexpected error message");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdGeneratorInstanceWithNullAnnotatedObjectIdInfo() throws JsonMappingException {
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(null, null);
        assertNull(objectIdGenerator);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdGeneratorInstanceWithNullAnnotated() throws JsonMappingException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.CLASS, String[].class);
        context.objectIdGeneratorInstance(null, objectIdInfo);
    }

    @Test(expected = JsonMappingException.class)
    public void testObjectIdGeneratorInstanceWithNullObjectIdInfo() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(String[].class);
        context.objectIdGeneratorInstance(annotated, null);
    }

    @Test
    public void testObjectIdResolverInstanceWithNullAnnotatedObjectIdInfo() {
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(null, null);
        assertNull(objectIdResolver);
    }

    @Test
    public void testObjectIdResolverInstanceWithNullAnnotated() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.NAME, "testName");
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(null, objectIdInfo);
        assertNull(objectIdResolver);
    }

    @Test
    public void testObjectIdResolverInstanceWithNullObjectIdInfo() {
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, null);
        assertNull(objectIdResolver);
    }

    @Test(expected = JsonMappingException.class)
    public void testConverterInstanceWithNullConverterDefAnnotated() throws JsonMappingException {
        Class<?> converterDef = Converter.class;
        context.converterInstance(null, converterDef);
    }

    @Test(expected = JsonMappingException.class)
    public void testConverterInstanceWithNullConverterDef() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(String.class);
        context.converterInstance(annotated, null);
    }

    @Test
    public void testReportBadDefinitionWithNullType() throws JsonMappingException {
        String msg = "Test error message";
        context.reportBadDefinition(null, msg);
    }

    @Test
    public void testReportBadDefinitionWithNullMsg() throws JsonMappingException {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        context.reportBadDefinition(javaType, null);
    }
}

// Test class DatabindContextTest starts here
// ... test methods defined above
