package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsWithClassIntrospector() {
        ClassIntrospector ci = null;
        BaseSettings baseSettings1 = new BaseSettings(ci, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(new ClassIntrospector() {/* blank */}, null, null, null, null, null, null, null, null, null, null, null);
        assertEquals(baseSettings2, baseSettings1.withClassIntrospector(new ClassIntrospector() {/* blank */}));
    }

    @Test
    public void testBaseSettingsWithAnnotationIntrospector() {
        AnnotationIntrospector ai = null;
        BaseSettings baseSettings1 = new BaseSettings(null, ai, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, new AnnotationIntrospector() {/* blank */}, null, null, null, null, null, null, null, null, null, null);
        assertEquals(baseSettings2, baseSettings1.withAnnotationIntrospector(new AnnotationIntrospector() {/* blank */}));
    }

    @Test
    public void testBaseSettingsWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, AnnotationIntrospectorPair.create(ai, null));
        assertEquals(baseSettings2, baseSettings.withInsertedAnnotationIntrospector(ai));
    }

    @Test
    public void testBaseSettingsWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, AnnotationIntrospectorPair.create(null, ai));
        assertEquals(baseSettings2, baseSettings.withAppendedAnnotationIntrospector(ai));
    }

    @Test
    public void testBaseSettingsWithVisibilityChecker() {
        VisibilityChecker<?> vc = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, vc, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, new VisibilityChecker<Object>() {/* blank */}, null, null, null, null, null, null, null, null, null);
        assertEquals(baseSettings2, baseSettings1.withVisibilityChecker(new VisibilityChecker<Object>() {/* blank */}));
    }

    @Test
    public void testBaseSettingsWithVisibility() {
        PropertyAccessor forMethod = null;
        JsonAutoDetect.Visibility visibility = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null)
                .withVisibility(forMethod, visibility);
        assertEquals(baseSettings2, baseSettings1.withVisibility(forMethod, visibility));
    }

    @Test
    public void testBaseSettingsWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, null, pns, null, null, null, null, null, null, null, null);
        assertEquals(baseSettings2, baseSettings1.withPropertyNamingStrategy(pns));
    }

    @Test
    public void testBaseSettingsWithTypeFactory() {
        TypeFactory tf = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, null, null, tf, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null)
                .withTypeFactory(tf);
        assertEquals(baseSettings2, baseSettings1.withTypeFactory(tf));
    }

    @Test
    public void testBaseSettingsWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, null, null, null, typer, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null)
                .withTypeResolverBuilder(typer);
        assertEquals(baseSettings2, baseSettings1.withTypeResolverBuilder(typer));
    }

    @Test
    public void testBaseSettingsWithDateFormat() {
        DateFormat df = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, null, null, null, null, df, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, null, null, null, null, new StdDateFormat(), null, null, null, null, null)
                .withDateFormat(df);
        assertEquals(baseSettings2, baseSettings1.withDateFormat(df));
    }

    @Test
    public void testBaseSettingsWithHandlerInstantiator() {
        HandlerInstantiator hi = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, null, null, null, null, null, hi, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null)
                .withHandlerInstantiator(hi);
        assertEquals(baseSettings2, baseSettings1.withHandlerInstantiator(hi));
    }

    @Test
    public void testBaseSettingsWithLocale() {
        Locale l = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, null, null, null, null, null, null, l, null, null, null);
        assertEquals(baseSettings2, baseSettings1.with(l));
    }

    @Test
    public void testBaseSettingsWithTimeZone() {
        TimeZone tz = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null)
                .with(tz);
        assertEquals(baseSettings2, baseSettings1.with(tz));
    }

    @Test
    public void testBaseSettingsWithBase64Variant() {
        Base64Variant base64 = null;
        BaseSettings baseSettings1 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings2 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, base64);
        assertEquals(baseSettings2, baseSettings1.with(base64));
    }

    @Test
    public void testBaseSettingsWithNullArguments() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getClassIntrospector());
        assertNull(baseSettings.getAnnotationIntrospector());
        assertNull(baseSettings.getVisibilityChecker());
        assertNull(baseSettings.getPropertyNamingStrategy());
        assertNull(baseSettings.getTypeFactory());
        assertNull(baseSettings.getTypeResolverBuilder());
        assertNull(baseSettings.getDateFormat());
        assertNull(baseSettings.getHandlerInstantiator());
        assertNull(baseSettings.getLocale());
        assertNull(baseSettings.getTimeZone());
        assertNull(baseSettings.getBase64Variant());
    }
}