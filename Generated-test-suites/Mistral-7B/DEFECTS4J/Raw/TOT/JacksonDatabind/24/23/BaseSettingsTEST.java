package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.databind.BaseSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        // Create a BaseSettings instance with default values
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Std();
        PropertyNamingStrategy propertyNamingStrategy = PropertyNamingStrategy.STANDARD_EXCLUDED;
        TypeFactory typeFactory = new TypeFactoryBuilder()
                .build();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant base64 = Base64Variant.STANDARD;
        BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale,
                timeZone, base64);

        // Test getters
        assertEquals(classIntrospector, settings.getClassIntrospector());
        assertEquals(annotationIntrospector, settings.getAnnotationIntrospector());
        assertEquals(visibilityChecker, settings.getVisibilityChecker());
        assertEquals(propertyNamingStrategy, settings.getPropertyNamingStrategy());
        assertEquals(typeFactory, settings.getTypeFactory());
        assertEquals(typeResolverBuilder, settings.getTypeResolverBuilder());
        assertEquals(dateFormat, settings.getDateFormat());
        assertEquals(handlerInstantiator, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(timeZone, settings.getTimeZone());
        assertEquals(base64, settings.getBase64Variant());

        // Test withClassIntrospector method
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings settingsWithNewClassIntrospector = settings.withClassIntrospector(newClassIntrospector);
        assertNotSame(classIntrospector, settingsWithNewClassIntrospector.getClassIntrospector());
        assertEquals(newClassIntrospector, settingsWithNewClassIntrospector.getClassIntrospector());

        // Test withAnnotationIntrospector method
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings settingsWithNewAnnotationIntrospector = settings.withAnnotationIntrospector(newAnnotationIntrospector);
        assertNotSame(annotationIntrospector, settingsWithNewAnnotationIntrospector.getAnnotationIntrospector());
        assertEquals(newAnnotationIntrospector, settingsWithNewAnnotationIntrospector.getAnnotationIntrospector());

        // Test withVisibilityChecker method
        VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker.Std();
        BaseSettings settingsWithNewVisibilityChecker = settings.withVisibilityChecker(newVisibilityChecker);
        assertNotSame(visibilityChecker, settingsWithNewVisibilityChecker.getVisibilityChecker());
        assertEquals(newVisibilityChecker, settingsWithNewVisibilityChecker.getVisibilityChecker());

        // Test serialization with default settings
        ObjectMapper mapper = new ObjectMapper();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String json = mapper.writeValueAsString(localDateTime);
        LocalDateTime deserializedLocalDateTime = mapper.readValue(json, LocalDateTime.class);
        assertEquals(localDateTime, deserializedLocalDateTime);

        // Test serialization with custom settings
        ObjectMapper mapperWithCustomSettings = new ObjectMapper();
        mapperWithCustomSettings.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapperWithCustomSettings.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapperWithCustomSettings.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapperWithCustomSettings.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapperWithCustomSettings.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH));
        mapperWithCustomSettings.setTimeZone(TimeZone.getTimeZone("UTC"));
        LocalDateTime customLocalDateTime = LocalDateTime.now();
        json = mapperWithCustomSettings.writeValueAsString(customLocalDateTime);
        LocalDateTime deserializedCustomLocalDateTime = mapperWithCustomSettings.readValue(json, LocalDateTime.class);
        assertEquals(customLocalDateTime, deserializedCustomLocalDateTime);

        // Test deserialization with custom settings
        ObjectMapper mapperWithCustomDeserializationSettings = new ObjectMapper();
        mapperWithCustomDeserializationSettings.setAnnotationIntrospector(annotationIntrospector);
        mapperWithCustomDeserializationSettings.setVisibilityChecker(visibilityChecker);
        mapperWithCustomDeserializationSettings.setDateFormat(dateFormat);
        mapperWithCustomDeserializationSettings.setHandlerInstantiator(handlerInstantiator);
        mapperWithCustomDeserializationSettings.setLocale(locale);
        mapperWithCustomDeserializationSettings.setTimeZone(timeZone);
        String jsonWithCustomSettings = "{\"localDateTime\":\"2022-12-31 23:59:59\",\"locale\":\"en_US\",\"timeZone\":\"UTC\"}";
        CustomLocalDateTime customLocalDateTimeDeserialization = mapperWithCustomDeserializationSettings.readValue(jsonWithCustomSettings, CustomLocalDateTime.class);
        assertTrue(customLocalDateTimeDeserialization instanceof CustomLocalDateTime);
    }

    // CustomLocalDateTime class for testing deserialization with custom settings
    public static class CustomLocalDateTime {
        private LocalDateTime localDateTime;
        private String locale;
        private String timeZone;

        public CustomLocalDateTime() {
        }

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        @Override
        public String toString() {
            return "CustomLocalDateTime{" +
                    "localDateTime=" + localDateTime +
                    ", locale='" + locale + '\'' +
                    ", timeZone='" + timeZone + '\'' +
                    '}';
        }
    }
}