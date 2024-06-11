package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StandardConverter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdInfo;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

public class DatabindContextTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        JsonFactory factory = new JsonFactory();
        MapperConfig config = new MapperConfig(factory);
        mapper = new ObjectMapper(config);
    }

    @After
    public void tearDown() {
        // Reset the default instance so that tests don't interfere with each other
        mapper = null;
    }

    @Test
    public void testGetConfig() {
        DatabindContext ctx = mapper.getDeserializationContext();
        MapperConfig<?> config = ctx.getConfig();
        Assert.assertNotNull(config);
    }

    @Test
    public void testIsEnabled() {
        DatabindContext ctx = mapper.getDeserializationContext();
        Assert.assertTrue(ctx.isEnabled(MapperFeature.DEFAULT_INTERNAL_DATE_FORMAT));
        Assert.assertFalse(ctx.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
    }

    @Test
    public void testGetActiveView() {
        DatabindContext ctx = mapper.getDeserializationContext();
        Assert.assertNull(ctx.getActiveView());

        ctx = mapper.getSerializerProvider().getDefaultContext();
        Assert.assertNull(ctx.getActiveView());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext ctx = mapper.getDeserializationContext();
        Assert.assertNotNull(ctx.getAnnotationIntrospector());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext ctx = mapper.getDeserializationContext();
        JavaType javaType = ctx.getTypeFactory().constructType(String.class);
        JsonFormat.Value format = ctx.getDefaultPropertyFormat(javaType);
        Assert.assertNotNull(format);
    }

    @Test
    public void testGetAttribute() {
        DatabindContext ctx = mapper.getDeserializationContext();
        ctx.setAttribute("testKey", "testValue");
        Object testValue = ctx.getAttribute("testKey");
        Assert.assertEquals("testValue", testValue);
    }

    @Test
    public void testConstructType() {
        DatabindContext ctx = mapper.getDeserializationContext();
        Type intType = int[].class;
        JavaType javaType = ctx.constructType(intType);
        Assert.assertTrue(javaType.isAssignableFrom(int[].class));
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext ctx = mapper.getDeserializationContext();
        JavaType baseType = ctx.getTypeFactory().constructType(String.class);
        JavaType subType = ctx.resolveSubType(baseType, "java.util.ArrayList");
        Assert.assertTrue(subType.isAssignableFrom(java.util.ArrayList.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext ctx = mapper.getDeserializationContext();
        JavaType baseType = ctx.getTypeFactory().constructType(String.class);
        JavaType subType = ctx.getTypeFactory().constructType(Object.class);
        ctx.invalidTypeIdException(baseType, "java.util.ArrayList", "Invalid type id: not a subtype of String");
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext ctx = mapper.getDeserializationContext();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, String.class, null, null, null);
        ObjectIdGenerator<?> gen = ctx.objectIdGeneratorInstance(null, objectIdInfo);
        Assert.assertNotNull(gen);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext ctx = mapper.getDeserializationContext();
        JsonProperty prop = new JsonProperty();
        prop.value(StandardConverter.Instance.class);
        Object converterDef = prop.getAnnotation(Converter.class);
        Converter<Object, Object> converter = ctx.converterInstance(null, converterDef);
        Assert.assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonProcessingException {
        DatabindContext ctx = mapper.getDeserializationContext();
        JavaType javaType = ctx.getTypeFactory().constructType(String[].class);
        String msg = ctx.reportBadDefinition(javaType, "Invalid type definition for String[]");
        Assert.assertNotNull(msg);
    }
}