package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() throws JsonProcessingException, ParseException {
        // Create a BaseSettings instance with default values
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Std();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.DEFAULT();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
        DateFormat dateFormat = StdDateFormat.DEFAULT;
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;
        BaseSettings baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy,
                typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

        // Test getters
        ClassIntrospector classIntrospectorTest = baseSettings.getClassIntrospector();
        AnnotationIntrospector annotationIntrospectorTest = baseSettings.getAnnotationIntrospector();
        VisibilityChecker<?> visibilityCheckerTest = baseSettings.getVisibilityChecker();
        PropertyNamingStrategy propertyNamingStrategyTest = baseSettings.getPropertyNamingStrategy();
        TypeFactory typeFactoryTest = baseSettings.getTypeFactory();
        TypeResolverBuilder<?> typeResolverBuilderTest = baseSettings.getTypeResolverBuilder();
        DateFormat dateFormatTest = baseSettings.getDateFormat();
        HandlerInstantiator handlerInstantiatorTest = baseSettings.getHandlerInstantiator();
        Locale localeTest = baseSettings.getLocale();
        TimeZone timeZoneTest = baseSettings.getTimeZone();
        Base64Variant defaultBase64Test = baseSettings.getBase64Variant();
        assertEquals(classIntrospector, classIntrospectorTest);
        assertEquals(annotationIntrospector, annotationIntrospectorTest);
        assertEquals(visibilityChecker, visibilityCheckerTest);
        assertEquals(propertyNamingStrategy, propertyNamingStrategyTest);
        assertEquals(typeFactory, typeFactoryTest);
        assertEquals(typeResolverBuilder, typeResolverBuilderTest);
        assertEquals(dateFormat, dateFormatTest);
        assertEquals(handlerInstantiator, handlerInstantiatorTest);
        assertEquals(locale, localeTest);
        assertEquals(timeZone, timeZoneTest);
        assertEquals(defaultBase64, defaultBase64Test);

        // Test withClassIntrospector method
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(newClassIntrospector);
        assertNotSame(classIntrospector, newClassIntrospector);
        assertEquals(newClassIntrospector, newBaseSettings.getClassIntrospector());

        // Test withAnnotationIntrospector method
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newBaseSettings2 = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);
        assertNotSame(annotationIntrospector, newAnnotationIntrospector);
        assertEquals(newAnnotationIntrospector, newBaseSettings2.getAnnotationIntrospector());

        // Test serialization and deserialization
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baseSettings);
        BaseSettings deserializedBaseSettings = mapper.readValue(jsonString, BaseSettings.class);

        // Test equals and hashCode methods
        assertEquals(baseSettings, deserializedBaseSettings);
        assertEquals(baseSettings.hashCode(), deserializedBaseSettings.hashCode());

        // Test DateFormat with custom values
        DateFormat customDateFormat = new StdDateFormat();
        customDateFormat.setLocale(Locale.US);
        customDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        BaseSettings customBaseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy,
                typeFactory, typeResolverBuilder, customDateFormat, handlerInstantiator, locale, timeZone, defaultBase64);
        BaseSettings customBaseSettingsDeserialized = mapper.readValue(jsonString, BaseSettings.class);
        customBaseSettingsDeserialized.setDateFormat(customDateFormat);
        assertEquals(customBaseSettings, customBaseSettingsDeserialized);
        assertEquals(customDateFormat, customBaseSettings.getDateFormat());
    }
}