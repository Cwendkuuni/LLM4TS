package com.example; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private DatabindContext context;
    private MapperConfig<Object> config;

    @Before
    public void setUp() {
        config = new MapperConfig<>(new AnnotationIntrospector());
        context = new JacksonDatabindContext(config);
    }

    @After
    public void tearDown() {
        context = null;
        config = null;
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
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_INTROSPECTION));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
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
        Locale locale = Locale.US;
        context.setLocale(locale);
        assertEquals(locale, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        context.setTimeZone(timeZone);
        assertEquals(timeZone, context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(baseType);
        assertNotNull(defaultPropertyFormat);
    }

    @Test
    public void testGetAttribute() {
        String key = "test";
        Object value = "test value";
        context.setAttribute(key, value);
        assertEquals(value, context.getAttribute(key));
    }

    @Test
    public void testConstructType() {
        Type type = String[].class;
        JavaType javaType = context.constructType(type);
        assertNotNull(javaType);
        assertEquals(String[].class, javaType.getType());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.util.List");
        assertNotNull(subType);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "java.util.Set");
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        ObjectIdInfo objectIdInfo = new StandardObjectIdGenerator.KeyIdGenerator().getClassInfo();
        ObjectIdGenerator<?> idGenerator = context.objectIdGeneratorInstance(null, objectIdInfo);
        assertNotNull(idGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        ObjectIdInfo objectIdInfo = new StandardObjectIdResolver.KeyIdResolver().getClassInfo();
        ObjectIdResolver idResolver = context.objectIdResolverInstance(null, objectIdInfo);
        assertNotNull(idResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        StdConverter.StringConverter stringConverter = new StdConverter.StringConverter();
        Annotated annotated = new AnnotatedClass(String.class);
        Converter<Object,Object> converter = context.converterInstance(annotated, stringConverter);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType(InvalidDefinition.class);
        String msg = "Invalid definition: " + _quotedString("InvalidDefinition class");
        Object result = context.reportBadDefinition(type, msg);
        fail("reportBadDefinition should throw JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionWithClass() throws JsonMappingException {
        String msg = "Invalid definition: " + _quotedString("InvalidDefinition class");
        context.reportBadDefinition(InvalidDefinition.class, msg);
    }
}