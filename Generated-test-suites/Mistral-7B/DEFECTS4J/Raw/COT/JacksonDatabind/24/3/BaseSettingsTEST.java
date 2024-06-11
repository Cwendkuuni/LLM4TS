import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class BaseSettingsTest {

 private BaseSettings baseSettings;

 @Test
 public void testBaseSettingsWithValidArguments() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker<>();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder<>();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.getDefault();
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = null; // default value

 baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

 // Perform your assertions here
 }

 @Test
 public void testGetClassIntrospector() {
 assertNotNull(baseSettings.getClassIntrospector());
 }

 @Test
 public void testGetAnnotationIntrospector() {
 assertNotNull(baseSettings.getAnnotationIntrospector());
 }

 @Test
 public void testGetVisibilityChecker() {
 assertNotNull(baseSettings.getVisibilityChecker());
 }

 @Test
 public void testGetPropertyNamingStrategy() {
 assertNotNull(baseSettings.getPropertyNamingStrategy());
 }

 @Test
 public void testGetTypeFactory() {
 assertNotNull(baseSettings.getTypeFactory());
 }

 @Test
 public void testGetTypeResolverBuilder() {
 assertNotNull(baseSettings.getTypeResolverBuilder());
 }

 @Test
 public void testGetDateFormat() {
 assertNotNull(baseSettings.getDateFormat());
 }

 @Test
 public void testGetHandlerInstantiator() {
 assertNotNull(baseSettings.getHandlerInstantiator());
 }

 @Test
 public void testGetLocale() {
 assertNotNull(baseSettings.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 assertNotNull(baseSettings.getTimeZone());
 }

 @Test
 public void testGetBase64Variant() {
 assertSame(baseSettings.getBase64Variant(), baseSettings.getDefaultBase64());
 }

 @Test
 public void testGetDefaultBase64() {
 assertNull(baseSettings.getDefaultBase64());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTimeZone() {
 baseSettings = baseSettings.with(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testWithNullLocale() {
 Locale locale = null;
 baseSettings = baseSettings.with(locale);
 assertEquals(locale, baseSettings.getLocale());
 }

 @Test
 public void testWithTimeZone() {
 TimeZone timeZone = new TimeZone("UTC");
 baseSettings = baseSettings.with(timeZone);
 assertNotNull(baseSettings.getTimeZone());
 assertSame(baseSettings.getTimeZone(), timeZone);
 }

 @Test
 public void testWithLocale() {
 Locale locale = new Locale("fr", "FR");
 baseSettings = baseSettings.with(locale);
 assertEquals(locale, baseSettings.getLocale());
 }

 @Test
 public void testWithNullDateFormat() {
 baseSettings = baseSettings.with(null);
 assertNotNull(baseSettings.getDateFormat());
 }

 @Test
 public void testWithDateFormat() {
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 baseSettings = baseSettings.withDateFormat(dateFormat);
 assertNotNull(baseSettings.getDateFormat());
 assertSame(baseSettings.getDateFormat(), dateFormat);
 }

}