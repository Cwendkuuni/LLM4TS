package com.fasterxml.jackson.databind.test;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectToPrettyText;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        DatabindContext context = createContext();
        assertNotNull(context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = createContext();
        assertNotNull(context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = createContext();
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_INTROSPECTION));
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_SETTERS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = createContext();
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = createContext();
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = createContext();
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = createContext();
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testgetDefaultPropertyFormat() {
        DatabindContext context = createContext();
        JsonFormat.Value format = context.getDefaultPropertyFormat(String.class);
        assertNotNull(format);
        assertEquals(JsonFormat.Value.STANDARD, format);
    }

    @Test
    public void testGetAttribute() {
        DatabindContext context = createContext();
        Object attr = context.setAttribute("test", "value").getAttribute("test");
        assertEquals("value", attr);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttributeNullKey() {
        DatabindContext context = createContext();
        context.getAttribute(null);
    }

    @Test
    public void testGetAttributeUnknownKey() {
        DatabindContext context = createContext();
        Object attr = context.getAttribute("test");
        assertNull(attr);
    }

    @Test
    public void testConstructType() {
        DatabindContext context = createContext();
        JavaType javaType = context.constructType(String.class);
        assertNotNull(javaType);
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = createContext();
        JavaType javaType = context.constructType(String.class);
        JavaType specializedType = context.constructSpecializedType(javaType, String[].class);
        assertNotNull(specializedType);
        assertFalse(specializedType.isTypeOrSubTypeOf(javaType));
        assertTrue(specializedType.isTypeOrSubTypeOf(javaType.getRawClass()));
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext context = createContext();
        JavaType baseType = context.constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.util.ArrayList");
        assertNotNull(subType);
        assertTrue(subType.isTypeOrSubTypeOf(baseType.getRawClass()));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext context = createContext();
        JavaType baseType = context.constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.Integer");
        context.invalidTypeIdException(baseType, "java.lang.Integer", "Invalid type id");
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = createContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = createContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = createContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = createContext();
        Annotated annotated = Mockito.mock(Annotated.class);
        Object converterDef = Mockito.mock(String[].class);
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = createContext();
        JavaType javaType = Mockito.mock(JavaType.class);
        String msg = "Test message";
        Object result = context.reportBadDefinition(javaType, msg);
        assertNotNull(result);
        assertEquals(msg, result.toString());
    }

    private DatabindContext createContext() {
        return Mockito.mock(DatabindContext.class);
    }
}