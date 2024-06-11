package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsCreation() throws Exception {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Std();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new SimpleTypeResolverBuilder();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new MapperFactory().getDefaultInstance()
                .getSerializerProvider()
                .getHandlerInstancer();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.getDefaultVariant();

        BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale,
                timeZone, defaultBase64);

        // Basic checks
        assertNotNull(settings);
        assertEquals(classIntrospector, settings._classIntrospector);
        assertEquals(annotationIntrospector, settings._annotationIntrospector);
        assertEquals(visibilityChecker, settings._visibilityChecker);
        assertEquals(propertyNamingStrategy, settings._propertyNamingStrategy);
        assertEquals(typeFactory, settings._typeFactory);
        assertEquals(typeResolverBuilder, settings._typeResolverBuilder);
        assertEquals(dateFormat, settings._dateFormat);
        assertEquals(handlerInstantiator, settings._handlerInstantiator);
        assertEquals(locale, settings._locale);
        assertEquals(timeZone, settings._timeZone);
        assertEquals(defaultBase64, settings._defaultBase64);

        // Test methods
        BaseSettings newSettings = settings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(settings, newSettings);
        assertEquals(new ClassIntrospector(), newSettings._classIntrospector);
        newSettings = settings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(settings, newSettings);
        assertEquals(new AnnotationIntrospector(), newSettings._annotationIntrospector);
        newSettings = settings.withVisibilityChecker(new VisibilityChecker.Std());
        assertNotSame(settings, newSettings);
        assertEquals(new VisibilityChecker.Std(), newSettings._visibilityChecker);
        newSettings = settings.withPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
        assertNotSame(settings, newSettings);
        assertEquals(new PropertyNamingStrategy.SnakeCaseStrategy(), newSettings._propertyNamingStrategy);
        newSettings = settings.withTypeFactory(new TypeFactory());
        assertNotSame(settings, newSettings);
        assertEquals(new TypeFactory(), newSettings._typeFactory);
        newSettings = settings.withTypeResolverBuilder(new SimpleTypeResolverBuilder());
        assertNotSame(settings, newSettings);
        assertEquals(new SimpleTypeResolverBuilder(), newSettings._typeResolverBuilder);
        newSettings = settings.withDateFormat(null);
        assertNotSame(settings, newSettings);
        assertNull(newSettings._dateFormat);
        newSettings = settings.withDateFormat(new StdDateFormat());
        assertNotSame(settings, newSettings);
        assertSame(new StdDateFormat(), newSettings._dateFormat);
        newSettings = settings.withHandlerInstantiator(new MapperFactory().getDefaultInstance()
                .getSerializerProvider()
                .getHandlerInstancer());
        assertNotSame(settings, newSettings);
        assertEquals(new MapperFactory().getDefaultInstance()
                .getSerializerProvider()
                .getHandlerInstancer(), newSettings._handlerInstantiator);
        newSettings = settings.with(Locale.US);
        assertNotSame(settings, newSettings);
        assertEquals(Locale.US, newSettings._locale);
        newSettings = settings.with(TimeZone.getTimeZone("UTC"));
        assertNotSame(settings, newSettings);
        assertEquals(TimeZone.getTimeZone("UTC"), newSettings._timeZone);
        newSettings = settings.with(Base64Variants.US_ASCII);
        assertNotSame(settings, newSettings);
        assertEquals(Base64Variants.US_ASCII, newSettings._defaultBase64);

        // Test methods with errors
        try {
            settings.with(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.withClassIntrospector(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.withAnnotationIntrospector(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.withVisibilityChecker(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.withPropertyNamingStrategy(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.withTypeFactory(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.withTypeResolverBuilder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.withDateFormat(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.withHandlerInstantiator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.with(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.with(new TimeZone("Unknown"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            settings.with(Base64Variants.VINT);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getters
        Object getClassIntrospector = settings.getClassIntrospector();
        assertEquals(classIntrospector, getClassIntrospector);
        Object getAnnotationIntrospector = settings.getAnnotationIntrospector();
        assertEquals(annotationIntrospector, getAnnotationIntrospector);
        Object getVisibilityChecker = settings.getVisibilityChecker();
        assertEquals(visibilityChecker, getVisibilityChecker);
        Object getPropertyNamingStrategy = settings.getPropertyNamingStrategy();
        assertEquals(propertyNamingStrategy, getPropertyNamingStrategy);
        Object getTypeFactory = settings.getTypeFactory();
        assertEquals(typeFactory, getTypeFactory);
        Object getTypeResolverBuilder = settings.getTypeResolverBuilder();
        assertEquals(typeResolverBuilder, getTypeResolverBuilder);
        Object getDateFormat = settings.getDateFormat();
        assertEquals(dateFormat, getDateFormat);
        Object getHandlerInstantiator = settings.getHandlerInstantiator();
        assertEquals(handlerInstantiator, getHandlerInstantiator);
        Object getLocale = settings.getLocale();
        assertEquals(locale, getLocale);
        Object getTimeZone = settings.getTimeZone();
        assertEquals(timeZone, getTimeZone);
        Object getBase64Variant = settings.getBase64Variant();
        assertEquals(defaultBase64, getBase64Variant);

        // Test method chaining
        BaseSettings chainedSettings = settings
                .withClassIntrospector(new ClassIntrospector())
                .withAnnotationIntrospector(new AnnotationIntrospector())
                .withVisibilityChecker(new VisibilityChecker.Std())
                .withPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy())
                .withTypeFactory(new TypeFactory())
                .withTypeResolverBuilder(new SimpleTypeResolverBuilder())
                .withDateFormat(new StdDateFormat())
                .withHandlerInstantiator(new MapperFactory().getDefaultInstance()
                        .getSerializerProvider()
                        .getHandlerInstancer())
                .with(Locale.US)
                .with(TimeZone.getTimeZone("UTC"))
                .with(Base64Variants.US_ASCII);
        assertNotSame(settings, chainedSettings);
        assertEquals(new ClassIntrospector(), chainedSettings._classIntrospector);
        assertEquals(new AnnotationIntrospector(), chainedSettings._annotationIntrospector);
        assertEquals(new VisibilityChecker.Std(), chainedSettings._visibilityChecker);
        assertEquals(new PropertyNamingStrategy.SnakeCaseStrategy(), chainedSettings._propertyNamingStrategy);
        assertEquals(new TypeFactory(), chainedSettings._typeFactory);
        assertEquals(new SimpleTypeResolverBuilder(), chainedSettings._typeResolverBuilder);
        assertSame(new StdDateFormat(), chainedSettings._dateFormat);
        assertEquals(new MapperFactory().getDefaultInstance()
                .getSerializerProvider()
                .getHandlerInstancer(), chainedSettings._handlerInstantiator);
        assertEquals(Locale.US, chainedSettings._locale);
        assertEquals(TimeZone.getTimeZone("UTC"), chainedSettings._timeZone);
        assertEquals(Base64Variants.US_ASCII, chainedSettings._defaultBase64);

        // Test getters with chained settings
        Object getClassIntrospectorChained = chainedSettings.getClassIntrospector();
        assertEquals(new ClassIntrospector(), getClassIntrospectorChained);
        Object getAnnotationIntrospectorChained = chainedSettings.getAnnotationIntrospector();
        assertEquals(new AnnotationIntrospector(), getAnnotationIntrospectorChained);
        Object getVisibilityCheckerChained = chainedSettings.getVisibilityChecker();
        assertEquals(new VisibilityChecker.Std(), getVisibilityCheckerChained);
        Object getPropertyNamingStrategyChained = chainedSettings.getPropertyNamingStrategy();
        assertEquals(new PropertyNamingStrategy.SnakeCaseStrategy(), getPropertyNamingStrategyChained);
        Object getTypeFactoryChained = chainedSettings.getTypeFactory();
        assertEquals(new TypeFactory(), getTypeFactoryChained);
        Object getTypeResolverBuilderChained = chainedSettings.getTypeResolverBuilder();
        assertEquals(new SimpleTypeResolverBuilder(), getTypeResolverBuilderChained);
        Object getDateFormatChained = chainedSettings.getDateFormat();
        assertSame(new StdDateFormat(), getDateFormatChained);
        Object getHandlerInstantiatorChained = chainedSettings.getHandlerInstantiator();
        assertEquals(new MapperFactory().getDefaultInstance()
                .getSerializerProvider()
                .getHandlerInstancer(), getHandlerInstantiatorChained);
        Object getLocaleChained = chainedSettings.getLocale();
        assertEquals(Locale.US, getLocaleChained);
        Object getTimeZoneChained = chainedSettings.getTimeZone();
        assertEquals(TimeZone.getTimeZone("UTC"), getTimeZoneChained);
        Object getBase64VariantChained = chainedSettings.getBase64Variant();
        assertEquals(Base64Variants.US_ASCII, getBase64VariantChained);
    }

    @Test
    public void testDateFormatSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker.Std(), new PropertyNamingStrategy.Default(),
                new TypeFactory(), new SimpleTypeResolverBuilder(),
                new StdDateFormat(), new MapperFactory().getDefaultInstance()
                        .getSerializerProvider()
                        .getHandlerInstancer(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variants.getDefaultVariant());

        LocalDateTime dateTime = LocalDateTime.now();
        String json = mapper.writeValueAsString(settings);
        Object readObject = mapper.readValue(json, BaseSettings.class);

        assertEquals(settings, readObject);
        DateFormat dateFormat = readObject.getDateFormat();
        assertEquals(settings._dateFormat, dateFormat);
        assertEquals(dateFormat.getClass(), StdDateFormat.class);
        assertEquals(dateFormat.getTimeZone(), settings._timeZone);
        assertEquals(dateFormat.getLocale(), settings._locale);

        LocalDateTime parsedDateTime = LocalDateTime.parse(dateFormat.format(dateTime.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()));
        assertEquals(dateTime, parsedDateTime);
    }
}