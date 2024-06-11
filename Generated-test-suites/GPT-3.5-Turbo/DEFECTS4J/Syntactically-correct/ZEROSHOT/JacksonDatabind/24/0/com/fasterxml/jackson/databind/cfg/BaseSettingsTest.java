
package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        // Create a sample BaseSettings instance for testing.
        ClassIntrospector ci = new ClassIntrospector();
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.PUBLIC_ONLY;
        VisibilityChecker<?> vc = new VisibilityChecker.Std(visibility, visibility, visibility, visibility, visibility);
        PropertyNamingStrategy pns = new PropertyNamingStrategy.Std();
        TypeFactory tf = new TypeFactory.Default();
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = null;
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = null;

        baseSettings = new BaseSettings(ci, null, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
    }

    @Test
    public void testGetClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        assertEquals(ci, baseSettings.getClassIntrospector());
    }
    
    @Test
    public void testGetVisibilityChecker() {
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.PUBLIC_ONLY;
        VisibilityChecker<?> vc = new VisibilityChecker.Std(visibility, visibility, visibility, visibility, visibility);
        assertEquals(vc, baseSettings.getVisibilityChecker());
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy.Std();
        assertEquals(pns, baseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory tf = new TypeFactory.Default();
        assertEquals(tf, baseSettings.getTypeFactory());
    }

    @Test
    public void testGetDateFormat() {
        DateFormat dateFormat = new StdDateFormat();
        assertEquals(dateFormat, baseSettings.getDateFormat());
    }

    @Test
    public void testGetLocale() {
        Locale locale = Locale.getDefault();
        assertEquals(locale, baseSettings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        assertEquals(timeZone, baseSettings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        Base64Variant defaultBase64 = null;
        assertEquals(defaultBase64, baseSettings.getBase64Variant());
    }
}
