package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdSerializer;

import org.junit.Test;

/**
 * Unit tests for {@link BaseSettings} class
 */
public class BaseSettingsTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Std();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;

        BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

        // verify constructor arguments
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
        assertSame(defaultBase64, settings._defaultBase64);
    }

    /**
     * Test method {@link BaseSettings#withClassIntrospector(ClassIntrospector)}.
     */
    @Test
    public void testWithClassIntrospector() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings newSettings = settings.withClassIntrospector(newClassIntrospector);

        // verify that new instance has new class introspector
        assertNotSame(settings._classIntrospector, newSettings._classIntrospector);
        assertSame(newClassIntrospector, newSettings._classIntrospector);
    }

    /**
     * Test method {@link BaseSettings#withAnnotationIntrospector(AnnotationIntrospector)}.
     */
    @Test
    public void testWithAnnotationIntrospector() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newSettings = settings.withAnnotationIntrospector(newAnnotationIntrospector);

        // verify that new instance has new annotation introspector
        assertNotSame(settings._annotationIntrospector, newSettings._annotationIntrospector);
        assertSame(newAnnotationIntrospector, newSettings._annotationIntrospector);
    }

    /**
     * Test method {@link BaseSettings#withPropertyNamingStrategy(PropertyNamingStrategy)}.
     */
    @Test
    public void testWithPropertyNamingStrategy() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy.Default();
        BaseSettings newSettings = settings.withPropertyNamingStrategy(newPropertyNamingStrategy);

        // verify that new instance has new property naming strategy
        assertNotSame(settings._propertyNamingStrategy, newSettings._propertyNamingStrategy);
        assertSame(newPropertyNamingStrategy, newSettings._propertyNamingStrategy);
    }

    /**
     * Test method {@link BaseSettings#withTypeFactory(TypeFactory)}.
     */
    @Test
    public void testWithTypeFactory() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        TypeFactory newTypeFactory = new TypeFactory();
        BaseSettings newSettings = settings.withTypeFactory(newTypeFactory);

        // verify that new instance has new type factory
        assertNotSame(settings._typeFactory, newSettings._typeFactory);
        assertSame(newTypeFactory, newSettings._typeFactory);
    }

    /**
     * Test method {@link BaseSettings#withTypeResolverBuilder(TypeResolverBuilder)}.
     */
    @Test
    public void testWithTypeResolverBuilder() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilderDefault();
        BaseSettings newSettings = settings.withTypeResolverBuilder(newTypeResolverBuilder);

        // verify that new instance has new type resolver builder
        assertNotSame(settings._typeResolverBuilder, newSettings._typeResolverBuilder);
        assertSame(newTypeResolverBuilder, newSettings._typeResolverBuilder);
    }

    /**
     * Test method {@link BaseSettings#withDateFormat(DateFormat)}.
     */
    @Test
    public void testWithDateFormat() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        DateFormat newDateFormat = new StdDateFormat();
        BaseSettings newSettings = settings.withDateFormat(newDateFormat);

        // verify that new instance has new date format
        assertNotSame(settings._dateFormat, newSettings._dateFormat);
        assertSame(newDateFormat, newSettings._dateFormat);
    }

    /**
     * Test method {@link BaseSettings#withHandlerInstantiator(HandlerInstantiator)}.
     */
    @Test
    public void testWithHandlerInstantiator() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();
        BaseSettings newSettings = settings.withHandlerInstantiator(newHandlerInstantiator);

        // verify that new instance has new handler instantiantor
        assertNotSame(settings._handlerInstantiator, newSettings._handlerInstantiator);
        assertSame(newHandlerInstantiator, newSettings._handlerInstantiator);
    }

    /**
     * Test method {@link BaseSettings#withLocale(Locale)}.
     */
    @Test
    public void testWithLocale() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        Locale newLocale = new Locale("en_US");
        BaseSettings newSettings = settings.with(newLocale);

        // verify that new instance has new locale
        assertNotSame(settings._locale, newSettings._locale);
        assertSame(newLocale, newSettings._locale);
    }

    /**
     * Test method {@link BaseSettings#withTimeZone(TimeZone)} with valid timezone.
     */
    @Test
    public void testWithTimeZoneValid() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        TimeZone newTimeZone = new TimeZone("UTC");
        BaseSettings newSettings = settings.with(newTimeZone);

        // verify that new instance has new timezone
        assertNotSame(settings._timeZone, newSettings._timeZone);
        assertSame(newTimeZone, newSettings._timeZone);

        // verify that new date format also uses new timezone
        DateFormat dateFormat = newSettings.getDateFormat();
        assertEquals(newTimeZone, dateFormat.getTimeZone());
    }

    /**
     * Test method {@link BaseSettings#withTimeZone(TimeZone)} with null timezone.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWithTimeZoneNull() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        BaseSettings newSettings = settings.with(null);
    }

    /**
     * Test method {@link BaseSettings#with(TimeZone)} with valid timezone.
     */
    @Test
    public void testWithTimeZone() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        TimeZone timeZone = new TimeZone("UTC");
        BaseSettings newSettings = settings.with(timeZone);

        // verify that new instance has new timezone
        assertNotSame(settings._timeZone, newSettings._timeZone);
        assertSame(timeZone, newSettings._timeZone);

        // verify that new date format also uses new timezone
        DateFormat dateFormat = newSettings.getDateFormat();
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    /**
     * Test method {@link BaseSettings#with(Base64Variant)} with valid Base64Variant.
     */
    @Test
    public void testWithBase64VariantValid() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        Base64Variant newBase64Variant = new Base64Variant() {
            @Override
            public byte[] encode(byte[] bytes) {
                return new byte[0];
            }

            @Override
            public byte[] decode(byte[] bytes) {
                return new byte[0];
            }
        };

        BaseSettings newSettings = settings.with(newBase64Variant);

        // verify that new instance has new Base64Variant
        assertNotSame(settings._defaultBase64, newSettings._defaultBase64);
        assertSame(newBase64Variant, newSettings._defaultBase64);
    }

    /**
     * Test method {@link BaseSettings#with(Base64Variant)} with null Base64Variant.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWithBase64VariantNull() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
                new PropertyNamingStrategy.Default(), new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator(), Locale.getDefault(), TimeZone.getDefault(), Base64Variant.DEFAULT);

        BaseSettings newSettings = settings.with(null);
    }
}

class TypeResolverBuilderDefault extends TypeResolverBuilder {
    @Override
    public JavaType buildType(Class<?> rawType, TypeResolverBuilder.BeanDescription beanDesc) {
        return null;
    }

    @Override
    public JavaType buildType(Class<?> rawType, JavaType baseType, TypeResolverBuilder.BeanDescription beanDesc) {
        return null;
    }

    @Override
    public JavaType buildType(Class<?> rawType, TypeResolverBuilder.BeanDescription beanDesc, JavaType baseType) {
        return null;
    }

    @Override
    public JavaType buildType(String name, TypeResolverBuilder.BeanDescription beanDesc) {
        return null;
    }

    @Override
    public JavaType buildType(String name, TypeResolverBuilder.BeanDescription beanDesc, JavaType baseType) {
        return null;
    }

    @Override
    public JavaType buildType(JavaType baseType, TypeResolverBuilder.BeanDescription beanDesc) {
        return null;
    }

    @Override
    public JavaType buildType(JavaType baseType, TypeResolverBuilder.BeanDescription beanDesc, JavaType baseType2) {
        return null;
    }
}

class HandlerInstantiatorTest extends StdSerializer<Object> {
    @Override
    public Object createInstance(BeanProperty.Std property, JavaType typeToInstance) {
        return null;
    }
}

class PropertyNamingStrategyTest extends StdSerializer<Object> {
    @Override
    public String nameForProperty(PropertyName propertyName) {
        return null;
    }
}