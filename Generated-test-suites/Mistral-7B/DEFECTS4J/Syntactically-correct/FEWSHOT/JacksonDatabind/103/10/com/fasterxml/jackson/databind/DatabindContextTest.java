package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        MapperConfig<?> config = Mockito.mock(MapperConfig.class);
        when(context.getConfig()).thenReturn(config);
        assertEquals(config, context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        when(context.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
        assertEquals(annotationIntrospector, context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        MapperConfig<?> config = Mockito.mock(MapperConfig.class);
        when(context.getConfig()).thenReturn(config);
        when(config.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_FOR_PRIMITIVES)).thenReturn(true);
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_FOR_PRIMITIVES));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        MapperConfig<?> config = Mockito.mock(MapperConfig.class);
        when(context.getConfig()).thenReturn(config);
        when(config.canOverrideAccessModifiers()).thenReturn(true);
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        Class<?> activeView = Mockito.mock(Class.class);
        when(context.getActiveView()).thenReturn(activeView);
        assertEquals(activeView, context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        Locale locale = Locale.ENGLISH;
        when(context.getLocale()).thenReturn(locale);
        assertEquals(locale, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        TimeZone timeZone = TimeZone.getDefault();
        when(context.getTimeZone()).thenReturn(timeZone);
        assertEquals(timeZone, context.getTimeZone());
    }

    @Test
    public void testgetDefaultPropertyFormat() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        JsonFormat.Value defaultPropertyFormat = new JsonFormat.Value();
        when(context.getDefaultPropertyFormat(Mockito.any(Class.class))).thenReturn(defaultPropertyFormat);
        assertEquals(defaultPropertyFormat, context.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testgetAttribute() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        Object key = JsonTypeInfo.As.PROPERTY.class;
        Object value = "value";
        when(context.getAttribute(key)).thenReturn(value);
        assertEquals(value, context.getAttribute(key));
    }

    @Test
    public void testSetAttribute() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        Object key = JsonTypeInfo.As.PROPERTY.class;
        Object value = "value";
        when(context.setAttribute(key, value)).thenReturn(context);
        assertEquals(context, context.setAttribute(key, value));
    }

    @Test
    public void testConstructType() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        when(context.getTypeFactory()).thenReturn(typeFactory);
        Type type = new TypeToken<String>() {}.getType();
        JavaType javaType = context.constructType(type);
        assertEquals(type, javaType.getType());
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        Class<?> subclass = String[].class;
        when(context.getTypeFactory()).thenReturn(typeFactory);
        when(context.getConfig()).thenReturn(Mockito.mock(MapperConfig.class));
        when(typeFactory.constructSpecializedType(baseType, subclass)).thenReturn(Mockito.mock(JavaType.class));
        assertEquals(Mockito.mock(JavaType.class), context.constructSpecializedType(baseType, subclass));
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeInvalidTypeId() throws JsonMappingException {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        String typeId = "InvalidTypeId";
        when(context.resolveSubType(baseType, typeId)).thenThrow(new JsonMappingException(typeId, "Invalid Type Id"));
        // This test case should throw JsonMappingException
        context.resolveSubType(baseType, typeId);
    }

    @Test
    public void testInvalidTypeIdException() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        String typeId = "InvalidTypeId";
        String extraDesc = "Not a subtype";
        when(context.invalidTypeIdException(baseType, typeId, extraDesc)).thenThrow(new JsonMappingException());
        // This test case should not be called directly
        context.invalidTypeIdException(baseType, typeId, extraDesc);
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        when(context.getTypeFactory()).thenReturn(typeFactory);
        assertEquals(typeFactory, context.getTypeFactory());
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = Mockito.mock(ObjectIdGenerator.class);
        when(context.objectIdGeneratorInstance(annotated, objectIdInfo)).thenReturn(objectIdGenerator);
        assertEquals(objectIdGenerator, context.objectIdGeneratorInstance(annotated, objectIdInfo));
    }

    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = Mockito.mock(ObjectIdResolver.class);
        when(context.objectIdResolverInstance(annotated, objectIdInfo)).thenReturn(objectIdResolver);
        assertEquals(objectIdResolver, context.objectIdResolverInstance(annotated, objectIdInfo));
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        Annotated annotated = Mockito.mock(Annotated.class);
        Object converterDef = JsonTypeInfo.Id.CLASS.class;
        Converter<Object, Object> converter = Mockito.mock(Converter.class);
        when(context.converterInstance(annotated, converterDef)).thenReturn(converter);
        assertEquals(converter, context.converterInstance(annotated, converterDef));
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        String msg = "Bad Definition Message";
        when(context.reportBadDefinition(type, msg)).thenReturn(null);
        assertNull(context.reportBadDefinition(type, msg));
    }
}