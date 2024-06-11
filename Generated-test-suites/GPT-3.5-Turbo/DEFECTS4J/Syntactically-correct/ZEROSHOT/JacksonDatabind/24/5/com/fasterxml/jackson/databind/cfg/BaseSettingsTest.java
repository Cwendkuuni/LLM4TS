
package com.fasterxml.jackson.databind.cfg;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = new Base64Variant();

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        
        ClassIntrospector newCi = new ClassIntrospector();
        
        BaseSettings newSettings = baseSettings.withClassIntrospector(newCi);
        
        assertNotSame(baseSettings, newSettings);
        assertSame(newCi, newSettings.getClassIntrospector());
    }

    // write tests for other methods in a similar way
    // ...
}

