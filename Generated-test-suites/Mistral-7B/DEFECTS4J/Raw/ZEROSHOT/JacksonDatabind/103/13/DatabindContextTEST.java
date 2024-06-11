package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        MapperConfig<?> config = mock(MapperConfig.class);
        PowerMockito.when(context.getConfig()).thenReturn(config);
        assertEquals(config, context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        PowerMockito.when(context.getAnnotationIntrospector()).thenReturn(introspector);
        assertEquals(introspector, context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        MapperFeature feature = MapperFeature.DEFAULT_INTROSPECTION;
        PowerMockito.when(context.isEnabled(feature)).thenReturn(true);
        assertTrue(context.isEnabled(feature));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        PowerMockito.when(context.canOverrideAccessModifiers()).thenReturn(true);
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        PowerMockito.when(context.getActiveView()).thenReturn(Object.class);
        assertEquals(Object.class, context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        PowerMockito.when(context.getLocale()).thenReturn(Locale.getDefault());
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        PowerMockito.when(context.getTimeZone()).thenReturn(TimeZone.getDefault());
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testgetDefaultPropertyFormat() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        PowerMockito.when(context.getDefaultPropertyFormat(Object.class)).thenReturn(JsonFormat.Value.stdFormat());
        assertEquals(JsonFormat.Value.stdFormat(), context.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testGetAttribute() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        Object key = new Object();
        Object value = new Object();
        PowerMockito.when(context.getAttribute(eq(key))).thenReturn(value);
        assertEquals(value, context.getAttribute(key));
    }

    @Test
    public void testSetAttribute() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        Object key = new Object();
        Object value = new Object();
        context.setAttribute(key, value);
        verify(context).setAttribute(eq(key), any());
    }

    @Test
    public void testConstructType() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        TypeFactory typeFactory = mock(TypeFactory.class);
        PowerMockito.when(context.getTypeFactory()).thenReturn(typeFactory);
        Type type = new TypeToken<String>() {}.getType();
        JavaType javaType = context.constructType(type);
        verify(typeFactory).constructType(type);
        assertEquals(javaType.getType(), type);
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        TypeFactory typeFactory = mock(TypeFactory.class);
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        Class<?> subclass = String[].class;
        JavaType javaType = context.constructSpecializedType(baseType, subclass);
        verify(typeFactory).constructSpecializedType(baseType, subclass);
        assertEquals(javaType.getType(), subclass);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "[invalid]";
        PowerMockito.doThrow(new JsonMappingException("Test")).when(context).invalidTypeIdException(baseType, typeId, anyString());
        context.resolveSubType(baseType, typeId);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        DatabindContext context = mock(DatabindContext.class);
        ObjectIdGenerator<?> gen = PowerMockito.mock(ObjectIdGenerator.class);
        PowerMockito.when(context.objectIdGeneratorInstance(any(Annotated.class), any(ObjectIdInfo.class))).thenReturn(gen);
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> result = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        verify(context).objectIdGeneratorInstance(annotated, objectIdInfo);
        assertEquals(gen, result);
    }

    @Test
    public void testObjectIdResolverInstance() {
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        DatabindContext context = mock(DatabindContext.class);
        ObjectIdResolver resolver = PowerMockito.mock(ObjectIdResolver.class);
        PowerMockito.when(context.objectIdResolverInstance(any(Annotated.class), any(ObjectIdInfo.class))).thenReturn(resolver);
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdResolver result = context.objectIdResolverInstance(annotated, objectIdInfo);
        verify(context).objectIdResolverInstance(annotated, objectIdInfo);
        assertEquals(resolver, result);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        DatabindContext context = mock(DatabindContext.class);
        Converter<Object, Object> converter = PowerMockito.mock(Converter.class);
        PowerMockito.when(context.converterInstance(any(Annotated.class), any(Object.class))).thenReturn(converter);
        Annotated annotated = mock(Annotated.class);
        Object converterDef = new StringConverter();
        Converter<Object, Object> result = context.converterInstance(annotated, converterDef);
        verify(context).converterInstance(annotated, converterDef);
        assertEquals(converter, result);
    }

    @Test
    public void testReportBadDefinition() {
        DatabindContext context = PowerMockito.spy(mock(DatabindContext.class));
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        String msg = "Test";
        ArgumentCaptor<JsonMappingException> exceptionCaptor = ArgumentCaptor.forClass(JsonMappingException.class);
        PowerMockito.doThrow(new JsonMappingException(msg)).when(context).reportBadDefinition(javaType, anyString());
        try {
            context.reportBadDefinition(javaType, "Test");
            fail();
        } catch (JsonMappingException e) {
            assertEquals(msg, e.getMessage());
            verify(context).reportBadDefinition(javaType, anyString());
        }
    }

    @Test
    public void testFormat() {
        DatabindContext context = mock(DatabindContext.class);
        String msg = "Test {0}";
        Object arg = "arg";
        String result = context._format(msg, arg);
        assertEquals(_format(msg, arg), result);
    }

    @Test
    public void testTruncate() {
        DatabindContext context = mock(DatabindContext.class);
        String desc = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest";
        String result = context._truncate(desc);
        assertEquals(_truncate(desc), result);
    }

    @Test
    public void testQuotedString() {
        DatabindContext context = mock(DatabindContext.class);
        String desc = "Test";
        String result = context._quotedString(desc);
        assertEquals(_quotedString(desc), result);
    }

    @Test
    public void testColonConcat() {
        DatabindContext context = mock(DatabindContext.class);
        String msgBase = "Test";
        String extra = " arg";
        String result = context._colonConcat(msgBase, extra);
        assertEquals(_colonConcat(msgBase, extra), result);
    }

    @Test
    public void testDesc() {
        DatabindContext context = mock(DatabindContext.class);
        String desc = "Test";
        String result = context._desc(desc);
        assertEquals(_desc(desc), result);
    }
}

class StringConverter implements Converter<String, String> {
    // empty implementation for testing
}