package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DatabindContextTest {

    private DatabindContext databindContext;

    @Before
    public void setUp() {
        MapperConfig<Object> config = new MapperConfig.StandardConfig();
        databindContext = new DatabindContextBase(config);
    }

    @Test
    public void testGetConfig() {
        assertNotNull(databindContext.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertNotNull(databindContext.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        assertNotNull(databindContext.getConfig());
        assertTrue(databindContext.isEnabled(MapperFeature.DEFAULT_INTENTS));
        assertFalse(databindContext.isEnabled(MapperFeature.AUTO_UNWRAP_ROOT_VALUE));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertFalse(databindContext.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(databindContext.getActiveView());
    }

    @Test
    public void testGetLocale() {
        assertNotNull(databindContext.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertNotNull(databindContext.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        assertNotNull(databindContext.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testGetAttribute() {
        databindContext.setAttribute("test", "testValue");
        assertEquals("testValue", databindContext.getAttribute("test"));
        assertNull(databindContext.getAttribute("notPresent"));
    }

    @Test
    public void testSetAttribute() {
        assertEquals(databindContext, databindContext.setAttribute("test", "testValue"));
    }

    @Test
    public void testConstructType() {
        Type type = new TypeToken<Object>() {}.getType();
        JavaType javaType = databindContext.constructType(type);
        assertNotNull(javaType);
    }

    @Test
    public void testConstructSpecializedType() {
        Type baseType = String.class;
        Type subclassType = new TypeToken<StringBuilder>() {}.getType();
        JavaType javaType = databindContext.constructSpecializedType(databindContext.constructType(baseType), StringBuilder.class);
        assertNotNull(javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeFailure() throws JsonMappingException {
        JavaType baseType = String.class;
        JavaType javaType = databindContext.resolveSubType(baseType, "NonExistentClass");
        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testConstructFromCanonical() {
        String className = StringBuilder.class.getName();
        JavaType javaType = databindContext.getTypeFactory().constructFromCanonical(className);
        assertNotNull(javaType);
    }

    @Test
    public void testFindClass() throws Exception {
        String className = StringBuilder.class.getName();
        JavaType javaType = databindContext.getTypeFactory().findClass(className);
        assertNotNull(javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = String.class;
        String typeId = "NonExistentClass";
        databindContext.invalidTypeIdException(baseType, typeId, "Invalid Type ID");
        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testGetTypeFactory() {
        assertNotNull(databindContext.getTypeFactory());
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerator.PropertyGenerator.class, null, null);
        ObjectIdGenerator<?> objectIdGenerator = databindContext.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonMappingException {
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdResolver.Std.class, null, null);
        ObjectIdResolver objectIdResolver = databindContext.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = null;
        Converter<String, Integer> converter = new StringToInt();
        Object converterDef = converter;
        Converter<Object, Object> converterInstance = databindContext.converterInstance(annotated, converterDef);
        assertNotNull(converterInstance);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType javaType = databindContext.getTypeFactory().constructType(String.class);
        String message = "Invalid definition for type " + javaType.getRawClass().getName();
        assertNull(databindContext.reportBadDefinition(javaType, message));
    }

    private static class StringToInt implements Converter<String, Integer> {
        @Override
        public Integer convert(String value, Class<?> destinationType, JsonMappingContext context)
                throws JsonMappingException {
            return Integer.parseInt(value);
        }
    }
}