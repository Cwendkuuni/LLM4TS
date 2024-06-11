package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Unit tests for {@link DatabindContext} class.
 */
public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        DatabindContext context = createTestContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = createTestContext();
        AnnotationIntrospector introspector = context.getAnnotationIntrospector();
        assertNotNull(introspector);
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = createTestContext();
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_USE_ANNOTATIONS));
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_USE_STATIC_TYPES));
        assertFalse(context.isEnabled(MapperFeature.DEFAULT_USE_UNWRAPPERS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = createTestContext();
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = createTestContext();
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = createTestContext();
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = createTestContext();
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = createTestContext();
        assertEquals(JsonFormat.Value.getDefaultInstance(), context.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testGetAttribute() {
        DatabindContext context = createTestContext();
        String key = new Object() {};
        assertNull(context.getAttribute(key));

        context = createTestContextWithAttribute(key, "value");
        assertEquals("value", context.getAttribute(key));
    }

    @Test
    public void testSetAttribute() {
        DatabindContext context = createTestContext();
        String key = new Object() {};
        context.setAttribute(key, "value");
        assertEquals("value", context.getAttribute(key));

        context.setAttribute(key, null);
        assertNull(context.getAttribute(key));
    }

    @Test
    public void testConstructType() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        TypeFactory typeFactory = context.getTypeFactory();
        JavaType javaType = typeFactory.constructType(Integer.class);
        assertEquals(javaType.getRawClass(), Integer.class);

        javaType = typeFactory.constructType(String[].class);
        assertEquals(javaType.getRawClass(), String[].class);
    }

    @Test
    public void testConstructSpecializedType() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        TypeFactory typeFactory = context.getTypeFactory();
        JavaType baseType = typeFactory.constructType(Object.class);
        JavaType specializedType = context.constructSpecializedType(baseType, String.class);
        assertEquals(specializedType.getRawClass(), String.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeInvalidTypeId() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        JavaType baseType = context.getTypeFactory().constructType(Object.class);
        String typeId = "InvalidTypeId";
        context.resolveSubType(baseType, typeId);
    }

    @Test
    public void testResolveSubTypeValidTypeId() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        JavaType baseType = context.getTypeFactory().constructType(Object.class);
        JavaType subType = context.getTypeFactory().constructType(String.class);
        assertEquals(subType, context.resolveSubType(baseType, "java.lang.String"));
    }

    @Test
    public void testConstructFromCanonical() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        TypeFactory typeFactory = context.getTypeFactory();
        JavaType javaType = typeFactory.constructFromCanonical("java.lang.String");
        assertEquals(javaType.getRawClass(), String.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindClass() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        String className = "NonExistentClass";
        context.getTypeFactory().findClass(className);
    }

    @Test
    public void testHandleUnknownTypeId() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        JavaType baseType = context.getTypeFactory().constructType(Object.class);
        String typeId = "SubType";
        context.handleUnknownTypeId(baseType, typeId);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdInvalidTypeId() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        JavaType baseType = context.getTypeFactory().constructType(Object.class);
        String typeId = "InvalidTypeId";
        context.handleUnknownTypeId(baseType, typeId);
    }

    @Test
    public void testInvalidTypeIdException() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        JavaType baseType = context.getTypeFactory().constructType(Object.class);
        String typeId = "SubType";
        JsonMappingException exception = context.invalidTypeIdException(baseType, typeId, null);
        assertNotNull(exception);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        Object converterDef = Mockito.mock(Converter.class);
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertEquals(converterDef, converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testConverterInstanceWithInvalidDefinition() throws JsonProcessingException {
        DatabindContext context = createTestContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        Object converterDef = new Object();
        context.converterInstance(annotated, converterDef);
    }

    @Test
    public void testReportBadDefinitionNullType() throws JsonMappingException {
        DatabindContext context = createTestContext();
        String msg = "Error message";
        assertNull(context.reportBadDefinition(null, msg));
    }

    @Test
    public void testReportBadDefinitionNullMessage() throws JsonMappingException {
        DatabindContext context = createTestContext();
        JavaType type = Mockito.mock(JavaType.class);
        assertNull(context.reportBadDefinition(type, null));
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = createTestContext();
        JavaType type = Mockito.mock(JavaType.class);
        String msg = "Error message";
        Object result = context.reportBadDefinition(type, msg);
        assertNull(result);
    }

    @Test
    public void testFormat() {
        DatabindContext context = createTestContext();
        String msg = "Error message {0}";
        String expected = "Error message value";
        String result = context._format(msg, "value");
        assertEquals(expected, result);
    }

    @Test
    public void testTruncate() {
        DatabindContext context = createTestContext();
        String desc = "This is a very long description with more than 500 characters.";
        String expected = "This is a very long description with more than 500 characters]...";
        String result = context._truncate(desc);
        assertEquals(expected, result);
    }

    @Test
    public void testQuotedString() {
        DatabindContext context = createTestContext();
        String desc = "This is a description with 'special' characters.";
        String expected = "\"This is a description with 'special' characters.\"";
        String result = context._quotedString(desc);
        assertEquals(expected, result);
    }

    @Test
    public void testColonConcat() {
        DatabindContext context = createTestContext();
        String msgBase = "Base message";
        String extra = "Additional message";
        String expected = "Base message: Additional message";
        String result = context._colonConcat(msgBase, extra);
        assertEquals(expected, result);
    }

    @Test
    public void testDesc() {
        DatabindContext context = createTestContext();
        String desc = "This is a description.";
        String expected = "[This is a description.]";
        String result = context._desc(desc);
        assertEquals(expected, result);
    }

    private DatabindContext createTestContext() {
        return Mockito.mock(DatabindContext.class);
    }

    private DatabindContext createTestContextWithAttribute(Object key, String value) {
        MapperConfig<?> config = Mockito.mock(MapperConfig.class);
        HandlerInstantiator handlerInstantiator = Mockito.mock(HandlerInstantiator.class);
        when(handlerInstantiator.getConfig()).thenReturn(config);
        when(config.getHandlerInstantiator()).thenReturn(handlerInstantiator);
        when(config.canOverrideAccessModifiers()).thenReturn(true);
        DatabindContext context = Mockito.mock(DatabindContext.class);
        when(context.getConfig()).thenReturn(config);
        when(context.getTypeFactory()).thenReturn(Mockito.mock(TypeFactory.class));
        when(context.getHandlerInstantiator()).thenReturn(handlerInstantiator);
        when(context.getActiveView()).thenReturn(null);
        when(context.getLocale()).thenReturn(Locale.getDefault());
        when(context.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(context.getDefaultPropertyFormat(Mockito.any(Class.class))).thenReturn(JsonFormat.Value.getDefaultInstance());
        when(context.getAttribute(key)).thenReturn(value);
        return context;
    }
}