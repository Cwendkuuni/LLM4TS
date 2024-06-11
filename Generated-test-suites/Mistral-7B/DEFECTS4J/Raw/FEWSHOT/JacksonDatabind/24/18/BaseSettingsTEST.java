package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

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

public class BaseSettingsTest {

    private BaseSettings _baseSettings;

    @Before
    public void setUp() {
        _baseSettings = new BaseSettings(
                new ClassIntrospector(),
                new AnnotationIntrospector(),
                new VisibilityChecker.Std(),
                new PropertyNamingStrategy.DEFAULT(),
                new TypeFactory(),
                new SimpleTypeResolverBuilder(),
                new StdDateFormat(),
                new SimpleHandlerInstantiator(),
                Locale.getDefault(),
                TimeZone.getDefault(),
                Base64Variant.DEFAULT);
    }

    @Test
    public void testGetClassIntrospector() {
        ClassIntrospector ci = _baseSettings.getClassIntrospector();
        assertSame(ci, _baseSettings._classIntrospector);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector ai = _baseSettings.getAnnotationIntrospector();
        assertSame(ai, _baseSettings._annotationIntrospector);
    }

    @Test
    public void testGetVisibilityChecker() {
        VisibilityChecker<?> vc = _baseSettings.getVisibilityChecker();
        assertSame(vc, _baseSettings._visibilityChecker);
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        PropertyNamingStrategy pns = _baseSettings.getPropertyNamingStrategy();
        assertSame(pns, _baseSettings._propertyNamingStrategy);
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory tf = _baseSettings.getTypeFactory();
        assertSame(tf, _baseSettings._typeFactory);
    }

    @Test
    public void testGetTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = _baseSettings.getTypeResolverBuilder();
        assertSame(typer, _baseSettings._typeResolverBuilder);
    }

    @Test
    public void testGetDateFormat() {
        DateFormat df = _baseSettings.getDateFormat();
        assertNotNull(df);
    }

    @Test
    public void testGetHandlerInstantiator() {
        HandlerInstantiator hi = _baseSettings.getHandlerInstantiator();
        assertNotNull(hi);
    }

    @Test
    public void testGetLocale() {
        Locale l = _baseSettings.getLocale();
        assertNotNull(l);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone tz = _baseSettings.getTimeZone();
        assertNotNull(tz);
    }

    @Test
    public void testGetBase64Variant() {
        Base64Variant base64 = _baseSettings.getBase64Variant();
        assertSame(base64, Base64Variant.DEFAULT);
    }
}

class SimpleTypeResolverBuilder implements TypeResolverBuilder<Object> {
    @Override
    public JavaType resolveValueType(BeanProperty property, JavaType propertyType) {
        return null;
    }
}

class SimpleHandlerInstantiator implements HandlerInstantiator {
    @Override
    public Object createHandler(Class<?> handlerType) {
        return null;
    }
}