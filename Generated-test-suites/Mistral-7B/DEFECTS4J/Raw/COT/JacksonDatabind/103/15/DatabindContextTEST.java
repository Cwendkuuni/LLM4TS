package com.example.test;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class DatabindContextTest {

    private DatabindContext context;
    private MapperConfig<ObjectMapper> config;

    @Before
    public void setUp() {
        config = new MapperConfig<ObjectMapper>(ObjectMapper.class);
        context = config.getDefaultContext();
    }

    @Test
    public void testGetConfig() {
        assertNotNull(context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertNotNull(context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled_MapperFeature() {
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_USE_STATIC_TYPING));
        assertFalse(context.isEnabled(MapperFeature.DEFAULT_FAIL_ON_EMPTY_BEANS));
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
        assertEquals("en_US", context.getLocale().toString());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = context.getTimeZone();
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testGetDefaultPropertyFormat_Class() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(baseType);
        assertNotNull(defaultPropertyFormat);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "InvalidTypeId");
    }

    @Test
    public void testConstructType_Type() {
        assertNotNull(context.constructType(Integer.TYPE));
    }

    @Test
    public void testConstructSpecializedType_JavaType_Class() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType specializedType = context.constructSpecializedType(baseType, String[].class);
        assertNotNull(specializedType);
    }

    @Test
    public void testResolveSubType_JavaType_String() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType resolvedType = context.resolveSubType(baseType, "java.util.List");
        assertNotNull(resolvedType);
        assertEquals(JavaType.constructType(List.class), resolvedType);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType_JavaType_InvalidString() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.resolveSubType(baseType, "InvalidType");
    }

    @Test
    public void testReportBadDefinition_JavaType_String() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType(int[].class);
        Type typeInfo = new TypeToken<int[]>(){}.getType();
        Object result = context.reportBadDefinition(type, "Type mismatch: %s and %s", typeInfo.getTypeName(), type.getRawClass().getName());
        assertNotNull(result);
    }

    @Test
    public void testReportBadDefinition_Class_String() throws JsonMappingException {
        Class<String> type = String.class;
        Object result = context.reportBadDefinition(type, "Type mismatch: %s and %s", type.getName(), String[].class.getName());
        assertNotNull(result);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null, JsonTypeInfo.Id.NAME, ObjectIdGenerator.Value.IDENTITY);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonMappingException {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null, JsonTypeInfo.Id.NAME, ObjectIdResolver.None.class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance_Annotated_ConverterDef() throws JsonMappingException {
        Annotated annotated = null;
        Object converterDef = Converter.StringConverter.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testConverterInstance_Annotated_InvalidConverterDef() throws JsonMappingException {
        Annotated annotated = null;
        Object converterDef = Object.class;
        context.converterInstance(annotated, converterDef);
    }

    @Test
    public void test_format() {
        String msg = "%s is of type %s";
        String msgArgs = "John Doe is of type String";
        String result = context._format(msg, "John Doe", "String");
        assertEquals(msgArgs, result);
    }

    @Test
    public void test_truncate() {
        String desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        String result = context._truncate(desc);
        assertTrue(desc.length() > result.length());
        assertEquals(desc.substring(0, 51), result.substring(0, 51));
        assertEquals(desc.substring(desc.length() - 51), result.substring(desc.length() - 51));
    }

    @Test
    public void test_quotedString() {
        String desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        String result = context._quotedString(desc);
        assertEquals("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit.\"", result);
    }

    @Test
    public void test_colonConcat() {
        String msgBase = "The problem is: ";
        String extra = "the data binding process failed.";
        String result = context._colonConcat(msgBase, extra);
        assertEquals(msgBase + ": " + extra, result);
    }

    @Test
    public void test_desc() {
        String desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        String result = context._desc(desc);
        assertEquals("[Lorem ipsum dolor sit amet, consectetur adipiscing elit.]", result);
    }
}