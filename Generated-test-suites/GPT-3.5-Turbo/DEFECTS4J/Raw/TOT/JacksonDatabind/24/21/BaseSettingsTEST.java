import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Locale;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class BaseSettingsTest {

    private BaseSettings baseSettings;
    private DateFormat dateFormat;

    @Before
    public void setUp() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker<>();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder<>();
        DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant base64Variant = new Base64Variant();

        baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy,
                typeFactory, typeResolverBuilder, defaultDateFormat, handlerInstantiator, locale, timeZone, base64Variant);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings updatedSettings = baseSettings.withClassIntrospector(newClassIntrospector);
        
        assertEquals(newClassIntrospector, updatedSettings.getClassIntrospector());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings updatedSettings = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);

        assertEquals(newAnnotationIntrospector, updatedSettings.getAnnotationIntrospector());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings updatedSettings = baseSettings.withInsertedAnnotationIntrospector(newAnnotationIntrospector);

        assertEquals(newAnnotationIntrospector, updatedSettings.getAnnotationIntrospector());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings updatedSettings = baseSettings.withAppendedAnnotationIntrospector(newAnnotationIntrospector);

        assertEquals(newAnnotationIntrospector, updatedSettings.getAnnotationIntrospector());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker<>();
        BaseSettings updatedSettings = baseSettings.withVisibilityChecker(newVisibilityChecker);

        assertEquals(newVisibilityChecker, updatedSettings.getVisibilityChecker());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithVisibility() {
        BaseSettings updatedSettings = baseSettings.withVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);

        VisibilityChecker<?> visibilityChecker = updatedSettings.getVisibilityChecker();
        assertNotNull(visibilityChecker);
        assertEquals(JsonAutoDetect.Visibility.PUBLIC_ONLY, visibilityChecker.getVisibility(PropertyAccessor.SETTER));
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy newNamingStrategy = new PropertyNamingStrategy();
        BaseSettings updatedSettings = baseSettings.withPropertyNamingStrategy(newNamingStrategy);

        assertEquals(newNamingStrategy, updatedSettings.getPropertyNamingStrategy());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory newTypeFactory = new TypeFactory();
        BaseSettings updatedSettings = baseSettings.withTypeFactory(newTypeFactory);

        assertEquals(newTypeFactory, updatedSettings.getTypeFactory());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilder<>();
        BaseSettings updatedSettings = baseSettings.withTypeResolverBuilder(newTypeResolverBuilder);

        assertEquals(newTypeResolverBuilder, updatedSettings.getTypeResolverBuilder());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithDateFormat() {
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        BaseSettings updatedSettings = baseSettings.withDateFormat(newDateFormat);

        assertEquals(newDateFormat, updatedSettings.getDateFormat());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();
        BaseSettings updatedSettings = baseSettings.withHandlerInstantiator(newHandlerInstantiator);

        assertEquals(newHandlerInstantiator, updatedSettings.getHandlerInstantiator());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithLocale() {
        Locale newLocale = Locale.FRANCE;
        BaseSettings updatedSettings = baseSettings.with(newLocale);

        assertEquals(newLocale, updatedSettings.getLocale());
        assertNotEquals(baseSettings, updatedSettings);
    }

    @Test
    public void testWithTimeZone() {
        TimeZone newTimeZone = TimeZone.getTimeZone("GMT");
        BaseSettings updatedSettings = baseSettings.with(newTimeZone);

        assertEquals(newTimeZone, updatedSettings.getTimeZone());
        assertNotEquals(baseSettings, updatedSettings);
        assertNotEquals(dateFormat.getTimeZone(), updatedSettings.getDateFormat().getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant newBase64Variant = new Base64Variant();
        BaseSettings updatedSettings = baseSettings.with(newBase64Variant);

        assertEquals(newBase64Variant, updatedSettings.getBase64Variant());
        assertNotEquals(baseSettings, updatedSettings);
    }
}