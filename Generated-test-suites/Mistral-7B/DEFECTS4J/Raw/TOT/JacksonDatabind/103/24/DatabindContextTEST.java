package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DatabindContextTest {

    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        context = objectMapper.getDeserializationContext();
        context.setLocale(Locale.US);
        context.setTimeZone(TimeZone.getDefault());
        objectMapper.configure(MapperFeature.AUTO_DETECT_GETTERS, true);
        objectMapper.configure(MapperFeature.AUTO_DETECT_SETTERS, true);
        objectMapper.configure(MapperFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        context.setAnnotationIntrospector(objectMapper.getAnnotationIntrospector());
        context.setTypeFactory(objectMapper.getTypeFactory());
    }

    @Test
    public void testGenericConfigAccess() throws IOException, JsonMappingException {
        MapperConfig<?> config = context.getConfig();
        Assert.assertNotNull(config);

        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        Assert.assertNotNull(annotationIntrospector);

        Assert.assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
        Assert.assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_VALUE_IDS));

        Class<?> activeView = context.getActiveView();
        Assert.assertNull(activeView);

        Locale locale = context.getLocale();
        Assert.assertEquals(Locale.US, locale);

        TimeZone timeZone = context.getTimeZone();
        Assert.assertNotNull(timeZone);

        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(String.class);
        Assert.assertNotNull(defaultPropertyFormat);

        Object attribute = context.getAttribute("test");
        Assert.assertNull(attribute);

        context.setAttribute("test", 42);
        Object attributeValue = context.getAttribute("test");
        Assert.assertEquals(Integer.valueOf(42), attributeValue);

        JavaType javaType = context.constructType(String.class);
        Assert.assertNotNull(javaType);

        JavaType specializedType = context.constructSpecializedType(javaType, String[].class);
        Assert.assertNotNull(specializedType);

        JavaType resolvedSubType = context.resolveSubType(javaType, "java.util.ArrayList");
        Assert.assertNotNull(resolvedSubType);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setFailureHandler(e -> {
            throw new RuntimeException(e);
        });

        JavaType mapperType = context.getTypeFactory().constructType(ObjectMapper.class);
        Assert.assertNotNull(mapperType);

        context.invalidTypeIdException(javaType, "java.util.ArrayList", "Failed to resolve subtype");
    }

    @Test
    public void testAttributeAccess() throws IOException, JsonMappingException {
        context.setAttribute("test", "testValue");
        Object attribute = context.getAttribute("test");
        Assert.assertEquals("testValue", attribute);
    }

    @Test
    public void testErrorReporting() throws IOException, JsonMappingException {
        JavaType javaType = context.getTypeFactory().constructType(Object.class);
        try {
            context.reportBadDefinition(javaType, "Failed to define type Object");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Failed to define type Object", e.getMessage());
        }

        Class<?> clazz = String[].class;
        try {
            context.reportBadDefinition(clazz, "Failed to define type String[]");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Failed to define type String[]", e.getMessage());
        }
    }
}