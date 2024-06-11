package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.fasterxml.jackson.annotate.JsonAutoDetect;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test class for BaseSettings.java
 */
public class BaseSettingsTest {

    @Test
    public void testConstructor() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new StdPropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new StandardTypeResolverBuilder();
        DateFormat df = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, tz, defaultBase64);
        assertNotNull(settings);

        // check that constructor arguments are assigned
        assertSame(ci, settings._classIntrospector);
        assertSame(ai, settings._annotationIntrospector);
        assertSame(vc, settings._visibilityChecker);
        assertSame(pns, settings._propertyNamingStrategy);
        assertSame(tf, settings._typeFactory);
        assertSame(typer, settings._typeResolverBuilder);
        assertSame(df, settings._dateFormat);
        assertSame(hi, settings._handlerInstantiator);
        assertSame(locale, settings._locale);
        assertSame(tz, settings._timeZone);
        assertSame(defaultBase64, settings._defaultBase64);
    }

    // Test getter methods here

    // Test setter methods here

    // Test factory methods here

    @Test
    public void testWith() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new StdPropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new StandardTypeResolverBuilder();
        DateFormat df = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, tz, defaultBase64);

        BaseSettings settingsWithCi = settings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(settings, settingsWithCi);
        assertSame(settingsWithCi._classIntrospector, new ClassIntrospector());

        BaseSettings settingsWithAi = settings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(settings, settingsWithAi);
        assertSame(settingsWithAi._annotationIntrospector, new AnnotationIntrospector());

        BaseSettings settingsWithVc = settings.withVisibilityChecker(new VisibilityChecker<Object>() {
            @Override
            public boolean isVisible(PropertyDescriptor property, JsonAutoDetect.Visibility visibility) {
                return true;
            }
        });
        assertNotSame(settings, settingsWithVc);
        assertSame(settingsWithVc._visibilityChecker, new VisibilityChecker<Object>() {
            @Override
            public boolean isVisible(PropertyDescriptor property, JsonAutoDetect.Visibility visibility) {
                return true;
            }
        });

        BaseSettings settingsWithPns = settings.withPropertyNamingStrategy(new StdPropertyNamingStrategy() {
            @Override
            public String nameForProperty(PropertyDescriptor property) {
                return "test";
            }
        });
        assertNotSame(settings, settingsWithPns);
        assertSame(settingsWithPns._propertyNamingStrategy, new StdPropertyNamingStrategy() {
            @Override
            public String nameForProperty(PropertyDescriptor property) {
                return "test";
            }
        });

        BaseSettings settingsWithTf = settings.withTypeFactory(tf);
        assertNotSame(settings, settingsWithTf);
        assertSame(settingsWithTf._typeFactory, tf);

        BaseSettings settingsWithTyper = settings.withTypeResolverBuilder(typer);
        assertNotSame(settings, settingsWithTyper);
        assertSame(settingsWithTyper._typeResolverBuilder, typer);

        BaseSettings settingsWithDf = settings.withDateFormat(df);
        assertNotSame(settings, settingsWithDf);
        assertSame(settingsWithDf._dateFormat, df);

        BaseSettings settingsWithHi = settings.withHandlerInstantiator(hi);
        assertNotSame(settings, settingsWithHi);
        assertSame(settingsWithHi._handlerInstantiator, hi);

        BaseSettings settingsWithLocale = settings.with(locale);
        assertNotSame(settings, settingsWithLocale);
        assertSame(settingsWithLocale._locale, locale);

        BaseSettings settingsWithTz = settings.with(tz);
        assertNotSame(settings, settingsWithTz);
        assertSame(settingsWithTz._locale, locale);
        assertSame(settingsWithTz._timeZone, tz);

        BaseSettings settingsWithBase64 = settings.with(Base64Variant.URL_SAFE);
        assertNotSame(settings, settingsWithBase64);
        assertSame(settingsWithBase64._defaultBase64, Base64Variant.URL_SAFE);
    }
}