package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        // Test the default constructor
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(settings._classIntrospector);
        assertNull(settings._annotationIntrospector);
        assertNull(settings._visibilityChecker);
        assertNull(settings._propertyNamingStrategy);
        assertNull(settings._typeFactory);
        assertNull(settings._typeResolverBuilder);
        assertNull(settings._dateFormat);
        assertNull(settings._handlerInstantiator);
        assertNull(settings._locale);
        assertNull(settings._timeZone);
        assertNull(settings._defaultBase64);

        // Test constructor with arguments
        ClassIntrospector classIntrospector = new MockClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new MockAnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new MockVisibilityChecker();
        PropertyNamingStrategy propertyNamingStrategy = new MockPropertyNamingStrategy();
        TypeFactory typeFactory = new MockTypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new MockTypeResolverBuilder();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new MockHandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;

        BaseSettings settings1 = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

        assertEquals(classIntrospector, settings1._classIntrospector);
        assertEquals(annotationIntrospector, settings1._annotationIntrospector);
        assertEquals(visibilityChecker, settings1._visibilityChecker);
        assertEquals(propertyNamingStrategy, settings1._propertyNamingStrategy);
        assertEquals(typeFactory, settings1._typeFactory);
        assertEquals(typeResolverBuilder, settings1._typeResolverBuilder);
        assertEquals(dateFormat, settings1._dateFormat);
        assertEquals(handlerInstantiator, settings1._handlerInstantiator);
        assertEquals(locale, settings1._locale);
        assertEquals(timeZone, settings1._timeZone);
        assertEquals(defaultBase64, settings1._defaultBase64);

        // Test factory methods
        BaseSettings settings2 = settings1.withClassIntrospector(new MockClassIntrospector());
        assertNotSame(settings1, settings2);
        assertEquals(settings2._classIntrospector, new MockClassIntrospector());

        settings2 = settings1.withAnnotationIntrospector(new MockAnnotationIntrospector());
        assertNotSame(settings1, settings2);
        assertEquals(settings2._annotationIntrospector, new MockAnnotationIntrospector());

        settings2 = settings1.withVisibilityChecker(new MockVisibilityChecker());
        assertNotSame(settings1, settings2);
        assertEquals(settings2._visibilityChecker, new MockVisibilityChecker());

        settings2 = settings1.withPropertyNamingStrategy(new MockPropertyNamingStrategy());
        assertNotSame(settings1, settings2);
        assertEquals(settings2._propertyNamingStrategy, new MockPropertyNamingStrategy());

        settings2 = settings1.withTypeFactory(new MockTypeFactory());
        assertNotSame(settings1, settings2);
        assertEquals(settings2._typeFactory, new MockTypeFactory());

        settings2 = settings1.withTypeResolverBuilder(new MockTypeResolverBuilder());
        assertNotSame(settings1, settings2);
        assertEquals(settings2._typeResolverBuilder, new MockTypeResolverBuilder());

        settings2 = settings1.withDateFormat(new StdDateFormat());
        assertNotSame(settings1, settings2);
        assertEquals(settings2._dateFormat, new StdDateFormat());

        settings2 = settings1.withHandlerInstantiator(new MockHandlerInstantiator());
        assertNotSame(settings1, settings2);
        assertEquals(settings2._handlerInstantiator, new MockHandlerInstantiator());

        settings2 = settings1.withLocale(new Locale("en", "US"));
        assertNotSame(settings1, settings2);
        assertEquals(settings2._locale, new Locale("en", "US"));

        settings2 = settings1.with(TimeZone.getTimeZone("UTC"));
        assertNotSame(settings1, settings2);
        assertEquals(settings2._timeZone, TimeZone.getTimeZone("UTC"));

        settings2 = settings1.with(Base64Variant.URL_SAFE);
        assertNotSame(settings1, settings2);
        assertEquals(settings2._defaultBase64, Base64Variant.URL_SAFE);
    }
}

class MockClassIntrospector extends ClassIntrospector {
    // Add any necessary mocks or stubs here
}

class MockAnnotationIntrospector extends AnnotationIntrospector {
    // Add any necessary mocks or stubs here
}

class MockVisibilityChecker extends VisibilityChecker<Object> {
    // Add any necessary mocks or stubs here
}

class MockPropertyNamingStrategy extends PropertyNamingStrategy {
    // Add any necessary mocks or stubs here
}

class MockTypeFactory extends TypeFactory {
    // Add any necessary mocks or stubs here
}

class MockTypeResolverBuilder extends TypeResolverBuilder<Object> {
    // Add any necessary mocks or stubs here
}

class MockHandlerInstantiator implements HandlerInstantiator {
    // Add any necessary mocks or stubs here
}