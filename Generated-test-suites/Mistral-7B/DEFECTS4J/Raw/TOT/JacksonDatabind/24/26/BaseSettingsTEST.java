package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings settings;

    @Before
    public void setup() throws IOException {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Default();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.DEFAULT();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder typeResolverBuilder = new StandardTypeResolverBuilder();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new MapperFactory().getDefaultInstance();
        settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
                typeResolverBuilder, dateFormat, handlerInstantiator, Locale.getDefault(),
                TimeZone.getDefault(), Base64Variants.DEFAULT);
    }

    @Test
    public testGetters() {
        assertEquals(ClassIntrospector.class, settings.getClassIntrospector().getClass());
        assertEquals(AnnotationIntrospector.class, settings.getAnnotationIntrospector().getClass());
        assertEquals(VisibilityChecker.class, settings.getVisibilityChecker().getClass());
        assertEquals(PropertyNamingStrategy.class, settings.getPropertyNamingStrategy().getClass());
        assertEquals(TypeFactory.class, settings.getTypeFactory().getClass());
        assertEquals(TypeResolverBuilder.class, settings.getTypeResolverBuilder().getClass());
        assertEquals(DateFormat.class, settings.getDateFormat().getClass());
        assertEquals(HandlerInstantiator.class, settings.getHandlerInstantiator().getClass());
        assertEquals(Locale.getDefault(), settings.getLocale());
        assertEquals(TimeZone.getDefault(), settings.getTimeZone());
        assertEquals(Base64Variants.DEFAULT, settings.getBase64Variant());
    }

    @Test
    public testWithClassIntrospector() throws IOException {
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings newSettings = settings.withClassIntrospector(newClassIntrospector);
        assertEquals(newClassIntrospector, newSettings.getClassIntrospector());
        assertEquals(settings.getAnnotationIntrospector(), newSettings.getAnnotationIntrospector());
        assertEquals(settings.getVisibilityChecker(), newSettings.getVisibilityChecker());
        assertEquals(settings.getPropertyNamingStrategy(), newSettings.getPropertyNamingStrategy());
        assertEquals(settings.getTypeFactory(), newSettings.getTypeFactory());
        assertEquals(settings.getTypeResolverBuilder(), newSettings.getTypeResolverBuilder());
        assertEquals(settings.getDateFormat(), newSettings.getDateFormat());
        assertEquals(settings.getHandlerInstantiator(), newSettings.getHandlerInstantiator());
        assertEquals(settings.getLocale(), newSettings.getLocale());
        assertEquals(settings.getTimeZone(), newSettings.getTimeZone());
        assertEquals(settings.getBase64Variant(), newSettings.getBase64Variant());
    }

    @Test
    public testWithAnnotationIntrospector() throws IOException {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newSettings = settings.withAnnotationIntrospector(newAnnotationIntrospector);
        assertEquals(settings.getClassIntrospector(), newSettings.getClassIntrospector());
        assertEquals(newAnnotationIntrospector, newSettings.getAnnotationIntrospector());
        assertEquals(settings.getVisibilityChecker(), newSettings.getVisibilityChecker());
        assertEquals(settings.getPropertyNamingStrategy(), newSettings.getPropertyNamingStrategy());
        assertEquals(settings.getTypeFactory(), newSettings.getTypeFactory());
        assertEquals(settings.getTypeResolverBuilder(), newSettings.getTypeResolverBuilder());
        assertEquals(settings.getDateFormat(), newSettings.getDateFormat());
        assertEquals(settings.getHandlerInstantiator(), newSettings.getHandlerInstantiator());
        assertEquals(settings.getLocale(), newSettings.getLocale());
        assertEquals(settings.getTimeZone(), newSettings.getTimeZone());
        assertEquals(settings.getBase64Variant(), newSettings.getBase64Variant());
    }

    @Test
    public testWithDateFormat() throws IOException, ParseException {
        DateFormat newDateFormat = new StdDateFormat();
        BaseSettings newSettings = settings.withDateFormat(newDateFormat);
        assertEquals(settings.getClassIntrospector(), newSettings.getClassIntrospector());
        assertEquals(settings.getAnnotationIntrospector(), newSettings.getAnnotationIntrospector());
        assertEquals(settings.getVisibilityChecker(), newSettings.getVisibilityChecker());
        assertEquals(settings.getPropertyNamingStrategy(), newSettings.getPropertyNamingStrategy());
        assertEquals(settings.getTypeFactory(), newSettings.getTypeFactory());
        assertEquals(settings.getTypeResolverBuilder(), newSettings.getTypeResolverBuilder());
        assertEquals(newDateFormat, newSettings.getDateFormat());
        assertEquals(settings.getHandlerInstantiator(), newSettings.getHandlerInstantiator());
        assertEquals(settings.getLocale(), newSettings.getLocale());
        assertEquals(settings.getTimeZone(), newSettings.getTimeZone());
        assertEquals(settings.getBase64Variant(), newSettings.getBase64Variant());

        // Test with parsing date format
        String dateString = "2022-03-01";
        ObjectMapper mapper = new ObjectMapper(newSettings);
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<>() {});
        String jsonString = "{\"date\":\"" + dateString + "\"}";
        Object readValue = mapper.readValue(jsonString, javaType);
        assertEquals(dateString, readValue.getClass().getMethod("getValue").invoke(readValue));
    }
}