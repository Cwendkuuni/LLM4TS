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
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings settings;

    @Before
    public void setup() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Default();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.DEFAULT();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<Object> typeResolverBuilder = new SimpleTypeResolverBuilder();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
                typeResolverBuilder, dateFormat, handlerInstantiator, Locale.ENGLISH, TimeZone.getDefault(), Base64Variant.STANDARD);
    }

    @Test
    public void testGetters() {
        ClassIntrospector classIntrospector = settings.getClassIntrospector();
        AnnotationIntrospector annotationIntrospector = settings.getAnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = settings.getVisibilityChecker();
        PropertyNamingStrategy propertyNamingStrategy = settings.getPropertyNamingStrategy();
        TypeFactory typeFactory = settings.getTypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = settings.getTypeResolverBuilder();
        DateFormat dateFormat = settings.getDateFormat();
        HandlerInstantiator handlerInstantiator = settings.getHandlerInstantiator();
        Locale locale = settings.getLocale();
        TimeZone timeZone = settings.getTimeZone();
        Base64Variant base64Variant = settings.getBase64Variant();

        assertSame(classIntrospector, settings._classIntrospector);
        assertSame(annotationIntrospector, settings._annotationIntrospector);
        assertSame(visibilityChecker, settings._visibilityChecker);
        assertSame(propertyNamingStrategy, settings._propertyNamingStrategy);
        assertSame(typeFactory, settings._typeFactory);
        assertSame(typeResolverBuilder, settings._typeResolverBuilder);
        assertSame(dateFormat, settings._dateFormat);
        assertSame(handlerInstantiator, settings._handlerInstantiator);
        assertSame(locale, settings._locale);
        assertSame(timeZone, settings._timeZone);
        assertSame(base64Variant, settings._defaultBase64);
    }

    @Test
    public void testWithClassIntrospector() {
        settings = settings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(settings._classIntrospector, settings._classIntrospector);
        assertSame(settings._annotationIntrospector, settings._annotationIntrospector);
        assertSame(settings._visibilityChecker, settings._visibilityChecker);
        assertSame(settings._propertyNamingStrategy, settings._propertyNamingStrategy);
        assertSame(settings._typeFactory, settings._typeFactory);
        assertSame(settings._typeResolverBuilder, settings._typeResolverBuilder);
        assertSame(settings._dateFormat, settings._dateFormat);
        assertSame(settings._handlerInstantiator, settings._handlerInstantiator);
        assertSame(settings._locale, settings._locale);
        assertSame(settings._timeZone, settings._timeZone);
        assertSame(settings._defaultBase64, settings._defaultBase64);
    }

    @Test
    public void testWithAnnotationIntrospector() {
        settings = settings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertSame(settings._classIntrospector, settings._classIntrospector);
        assertNotSame(settings._annotationIntrospector, settings._annotationIntrospector);
        assertSame(settings._visibilityChecker, settings._visibilityChecker);
        assertSame(settings._propertyNamingStrategy, settings._propertyNamingStrategy);
        assertSame(settings._typeFactory, settings._typeFactory);
        assertSame(settings._typeResolverBuilder, settings._typeResolverBuilder);
        assertSame(settings._dateFormat, settings._dateFormat);
        assertSame(settings._handlerInstantiator, settings._handlerInstantiator);
        assertSame(settings._locale, settings._locale);
        assertSame(settings._timeZone, settings._timeZone);
        assertSame(settings._defaultBase64, settings._defaultBase64);
    }

    @Test
    public void testWithDateFormat() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        settings = settings.withDateFormat(dateFormat);
        assertNotSame(settings._dateFormat, settings._dateFormat);
        assertEquals(dateFormat.getClass(), settings._dateFormat.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullDateFormat() {
        settings.withDateFormat(null);
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public Object instantiateValueHandler(DeserializationConfig config, JavaType type)
                    throws InstantiationException, IllegalAccessException {
                throw new RuntimeException();
            }
        };
        settings = settings.withHandlerInstantiator(handlerInstantiator);
        assertNotSame(settings._handlerInstantiator, settings._handlerInstantiator);
        assertSame(settings._classIntrospector, settings._classIntrospector);
        assertSame(settings._annotationIntrospector, settings._annotationIntrospector);
        assertSame(settings._visibilityChecker, settings._visibilityChecker);
        assertSame(settings._propertyNamingStrategy, settings._propertyNamingStrategy);
        assertSame(settings._typeFactory, settings._typeFactory);
        assertSame(settings._typeResolverBuilder, settings._typeResolverBuilder);
        assertSame(settings._dateFormat, settings._dateFormat);
        assertSame(settings._locale, settings._locale);
        assertSame(settings._timeZone, settings._timeZone);
        assertSame(settings._defaultBase64, settings._defaultBase64);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullLocale() {
        settings.with(null);
    }

    @Test
    public void testWithLocale() {
        settings = settings.with(Locale.US);
        assertNotSame(settings._locale, settings._locale);
        assertEquals(Locale.US, settings._locale);
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = new TimeZone("GMT+1") {
            @Override
            public String getID() {
                return "GMT+1";
            }
        };
        settings = settings.with(timeZone);
        assertNotSame(settings._timeZone, settings._timeZone);
        assertEquals(timeZone, settings._timeZone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZone() {
        settings.with(null);
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64Variant = new Base64Variant("MyBase64") {
            @Override
            public String getEncoding() {
                return "MyBase64";
            }
        };
        settings = settings.with(base64Variant);
        assertNotSame(settings._defaultBase64, settings._defaultBase64);
        assertEquals(base64Variant, settings._defaultBase64);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullBase64Variant() {
        settings.with(null);
    }
}