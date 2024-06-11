import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.cfg.BaseSettings.Builder;

public class BaseSettingsTest {

 private BaseSettings settings;

 @Before
 public void setUp() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Default();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.DEFAULT();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.getDefault();
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variant.DEFAULT;

 Builder builder = new BaseSettings.Builder(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);
 settings = builder.build();
 }

 @Test
 public void testGetClassIntrospector() {
 ClassIntrospector actual = settings.getClassIntrospector();
 assertEquals("Expected: classIntrospector, but was: " + actual, settings._classIntrospector, actual);
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector actual = settings.getAnnotationIntrospector();
 assertEquals("Expected: annotationIntrospector, but was: " + actual, settings._annotationIntrospector, actual);
 }

 @Test
 public void testGetVisibilityChecker() {
 VisibilityChecker<?> actual = settings.getVisibilityChecker();
 assertEquals("Expected: visibilityChecker, but was: " + actual, settings._visibilityChecker, actual);
 }

 @Test
 public void testGetPropertyNamingStrategy() {
 PropertyNamingStrategy actual = settings.getPropertyNamingStrategy();
 assertEquals("Expected: propertyNamingStrategy, but was: " + actual, settings._propertyNamingStrategy, actual);
 }

 @Test
 public void testGetTypeFactory() {
 TypeFactory actual = settings.getTypeFactory();
 assertEquals("Expected: typeFactory, but was: " + actual, settings._typeFactory, actual);
 }

 @Test
 public void testGetTypeResolverBuilder() {
 TypeResolverBuilder<?> actual = settings.getTypeResolverBuilder();
 assertEquals("Expected: typeResolverBuilder, but was: " + actual, settings._typeResolverBuilder, actual);
 }

 @Test
 public void testGetDateFormat() {
 DateFormat actual = settings.getDateFormat();
 assertEquals("Expected: dateFormat, but was: " + actual, settings._dateFormat, actual);
 }

 @Test
 public void testGetHandlerInstantiator() {
 HandlerInstantiator actual = settings.getHandlerInstantiator();
 assertEquals("Expected: handlerInstantiator, but was: " + actual, settings._handlerInstantiator, actual);
 }

 @Test
 public void testGetLocale() {
 Locale actual = settings.getLocale();
 assertEquals("Expected: locale, but was: " + actual, settings._locale, actual);
 }

 @Test
 public void testGetTimeZone() {
 TimeZone actual = settings.getTimeZone();
 assertEquals("Expected: timeZone, but was: " + actual, settings._timeZone, actual);
 }

 @Test
 public void testGetBase64Variant() {
 Base64Variant actual = settings.getBase64Variant();
 assertEquals("Expected: defaultBase64, but was: " + actual, settings._defaultBase64, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullClassIntrospector() {
 ClassIntrospector ci = null;
 settings = settings.withClassIntrospector(ci);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullAnnotationIntrospector() {
 AnnotationIntrospector ai = null;
 settings = settings.withAnnotationIntrospector(ai);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullVisibilityChecker() {
 VisibilityChecker<?> vc = null;
 settings = settings.withVisibilityChecker(vc);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullPropertyNamingStrategy() {
 PropertyNamingStrategy pns = null;
 settings = settings.withPropertyNamingStrategy(pns);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTypeFactory() {
 TypeFactory tf = null;
 settings = settings.withTypeFactory(tf);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTypeResolverBuilder() {
 TypeResolverBuilder<?> typer = null;
 settings = settings.withTypeResolverBuilder(typer);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullDateFormat() {
 DateFormat df = null;
 settings = settings.withDateFormat(df);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullHandlerInstantiator() {
 HandlerInstantiator hi = null;
 settings = settings.withHandlerInstantiator(hi);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullLocale() {
 Locale locale = null;
 settings = settings.with(locale);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTimeZone() {
 TimeZone timeZone = null;
 settings = settings.with(timeZone);
 fail("Expected: IllegalArgumentException, but was: " + settings);
 }

 @Test
 public void testWithNullDateFormatAndDefaultTimeZone() {
 DateFormat df = null;
 settings = settings.withDateFormat(df);
 DateFormat expectedDateFormat = new StdDateFormat();
 assertEquals("Expected: dateFormat, but was: " + settings.getDateFormat(), expectedDateFormat, settings.getDateFormat());
 }

 @Test
 public void testWithNullDateFormatAndCustomTimeZone() {
 DateFormat df = null;
 TimeZone customTimeZone = new TimeZone.Builder().setID("CustomTimeZone").build();
 settings = settings.with(df, customTimeZone);
 DateFormat expectedDateFormat = new StdDateFormat().withTimeZone(customTimeZone);
 assertEquals("Expected: dateFormat, but was: " + settings.getDateFormat(), expectedDateFormat, settings.getDateFormat());
 }

 @Test
 public void testWithNullBase64Variant() {
 Base64Variant base64 = null;
 settings = settings.with(base64);
 assertEquals("Expected: defaultBase64, but was: " + settings.getBase64Variant(), Base64Variant.DEFAULT, settings.getBase64Variant());
 }
}